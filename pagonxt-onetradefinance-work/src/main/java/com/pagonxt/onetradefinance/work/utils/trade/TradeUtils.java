package com.pagonxt.onetradefinance.work.utils.trade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.core.content.api.CoreContentItem;
import com.flowable.core.content.api.CoreContentService;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectModificationBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.content.PlatformContentItemService;
import com.flowable.platform.service.task.PlatformTaskService;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.constants.UserConstants;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.ValidationError;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.service.CollectionTypeService;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.service.OfficeInfoService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.trade.TradeRequestMapper;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import com.pagonxt.onetradefinance.work.utils.ValidationUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_CUSTOMER;


@Component
public class TradeUtils extends ValidationUtils {
    public static final String CASE_VAR_OPERATION_CODE = "operationCode";
    public static final String CASE_VAR_OPERATION = "operation";
    public static final String CASE_VAR_COUNTRY = "country";
    public static final String CASE_VAR_REQUEST_SAVED_STEP = "requestSavedStep";
    public static final String OPERATION_VAR_PRIORITY = "priority";
    public static final String OPERATION_VAR_CONTRACT_REFERENCE = "contractReference";
    public static final String EXTERNAL_TASK_VAR_COMPLETE_INFO_OUTCOME = "completeInfoOutcome";
    public static final String EXTERNAL_TASK_VAR_COMPLETE_INFO_COMMENT = "completeInfoComment";
    public static final String EXTERNAL_TASK_ACTION_CONTINUE = "CONTINUE";
    public static final String EXTERNAL_TASK_ACTION_REQUEST_FOR_CANCELATION = "REQUEST_FOR_CANCELATION";

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(TradeUtils.class);

    /* Member Variables*/
    private final CaseCommonVariablesHelper caseCommonVariablesHelper;
    private final CoreContentService contentService;
    private final CmmnRuntimeService cmmnRuntimeService;
    private final PagoNxtRequestUtils pagoNxtRequestUtils;
    private final DataObjectRuntimeService dataObjectRuntimeService;
    private final PlatformTaskService platformTaskService;
    private final TradeRequestMapper tradeRequestMapper;
    private final PlatformContentItemService platformContentItemService;


    /**
     * Constructor Method
     *
     * @param collectionTypeService      : a {@link CollectionTypeService} object
     * @param caseCommonVariablesHelper  : a {@link CaseCommonVariablesHelper} object
     * @param contentService             : a {@link CoreContentService} object
     * @param cmmnRuntimeService         : a {@link CmmnRuntimeService} object
     * @param pagoNxtRequestUtils        : a {@link PagoNxtRequestUtils} object
     * @param dataObjectRuntimeService   : a {@link DataObjectRuntimeService} object
     * @param platformTaskService        : a {@link PlatformTaskService} object
     * @param tradeRequestMapper         : a {@link TradeRequestMapper} object
     * @param platformContentItemService : a {@link PlatformContentItemService} object
     */
    public TradeUtils(OfficeInfoService officeInfoService,
                      CollectionTypeService collectionTypeService,
                      CaseCommonVariablesHelper caseCommonVariablesHelper,
                      CoreContentService contentService,
                      CmmnRuntimeService cmmnRuntimeService,
                      PagoNxtRequestUtils pagoNxtRequestUtils,
                      DataObjectRuntimeService dataObjectRuntimeService,
                      PlatformTaskService platformTaskService,
                      TradeRequestMapper tradeRequestMapper, PlatformContentItemService platformContentItemService) {
        super(officeInfoService,collectionTypeService);
        this.caseCommonVariablesHelper = caseCommonVariablesHelper;
        this.contentService = contentService;
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.pagoNxtRequestUtils = pagoNxtRequestUtils;
        this.dataObjectRuntimeService = dataObjectRuntimeService;
        this.platformTaskService = platformTaskService;
        this.tradeRequestMapper = tradeRequestMapper;
        this.platformContentItemService = platformContentItemService;
    }

    /**
     * class method to validate the contract of the middle office
     * If there is an error, the method adds it to the list of validations
     * @param request          : a TradeRequest object
     * @param validationErrors : a list of validation errors
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     * @return a list of validation errors
     */

    public List<ValidationError> validateContractMiddleOffice(TradeRequest request,
                                                              List<ValidationError> validationErrors) {
        if(request.getMiddleOffice() == null) {
            return validationErrors;
        }
        //If Requester is of Middle Office type, request MiddleOffice must be equals requester Middle office.
        // If not, request office is not valid because his middle office is not equal that the requester middle office
        if(request.getRequester()!=null &&
                request.getRequester().getUser()!=null &&
                request.getRequester().getUser().getUserType()!=null &&
                UserConstants.USER_TYPE_MIDDLE_OFFICE.equals(request.getRequester().getUser().getUserType()) &&
                !request.getMiddleOffice().equals(request.getRequester().getMiddleOffice()))
        {
            ValidationError error = new ValidationError(OFFICE, NOT_ALLOWED_VALUE, null);
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * class method to validate documents
     * If there is an error, the method adds it to the list of validations
     * @param request          : a TradeRequest object
     * @param validationErrors : a list of validation errors
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     * @return a list of validation errors
     */
    public List<ValidationError> validateDocuments(TradeRequest request, List<ValidationError> validationErrors) {
        if (request.getDocuments() != null) {
            request.getDocuments().forEach(document -> validateFileExtension("documents",
                    document.getFilename().toLowerCase(), validationErrors));
        }
        return validationErrors;
    }

    /**
     * Method to map common variables
     * @param variables : a collection of variables
     * @param request   : a {@link TradeRequest} object
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     */
    public void mapCommonVariables(Map<String, Object> variables, TradeRequest request) {
        variables.put(REQUESTER_ID, request.getRequester().getUser().getUserId());
        variables.put(REQUESTER_DISPLAYED_NAME, request.getRequester().getUser().getUserDisplayedName());
        variables.put(REQUESTER_TYPE, request.getRequester().getUser().getUserType());
        variables.put(COUNTRY, request.getCountry());

        variables.put(REQUEST_OFFICE, request.getOffice());
        variables.put(REQUEST_MIDDLE_OFFICE, request.getMiddleOffice());

        variables.put(REQUEST_COMMENTS, request.getComment());
        variables.put(REQUEST_PRIORITY, request.getPriority());
    }

    /**
     * Method to map customer variables
     * @param variables : a collection of variables
     * @param customer  : a Customer object
     * @see com.pagonxt.onetradefinance.integrations.model.Customer
     */
    public void mapCustomerVariables(Map<String, Object> variables, Customer customer) {
        pagoNxtRequestUtils.mapCustomerVariables(variables,customer);
    }

    /**
     * Method to update draft document on case instance
     * @param request       : a TradeRequest object
     * @param caseInstance  : a CaseInstance object
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     */
    public void updateDraftDocumentsOnCaseInstance(TradeRequest request, CaseInstance caseInstance) {
        List<Document> updatedDocumentList = new ArrayList<>();
        List<Document> newDocumentList = request.getDocuments();
        List<Document> currentDocumentList;
        try {
            currentDocumentList = caseCommonVariablesHelper.getRequestDocuments(caseInstance);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Processing error updating draft documents",
                    "updateDraftDocumentsOnCaseInstance", e);
        }
        // Delete removed documents from original list
        if (currentDocumentList != null && !currentDocumentList.isEmpty()) {
            List<String> currentDocumentsToRemoveIds =
                    currentDocumentList.stream().map(Document::getDocumentId).collect(Collectors.toList());

            List<String> newDocumentsIds =
                    newDocumentList.stream().map(Document::getDocumentId).collect(Collectors.toList());
            // Delete documents that are not in the new list from content and case variable
            currentDocumentsToRemoveIds.removeAll(newDocumentsIds);
            for (String docToRemoveId : currentDocumentsToRemoveIds) {
                contentService.deleteContentItem(docToRemoveId);

                currentDocumentList = currentDocumentList.stream()
                        .filter(doc -> !docToRemoveId.equals(doc.getDocumentId())).collect(Collectors.toList());
            }
            updatedDocumentList.addAll(currentDocumentList);
        }
        // Add new documents to case content and case variable
        List<Document> documentsToAdd = newDocumentList.stream()
                .filter(doc -> Strings.isBlank(doc.getDocumentId())).collect(Collectors.toList());

        saveDocuments(request.getRequester(), caseInstance.getId(), documentsToAdd, updatedDocumentList, true);
        //add document to case variable
        try {
            cmmnRuntimeService.setVariable(caseInstance.getId(), REQUEST_DOCUMENTS, caseCommonVariablesHelper
                    .getJsonNode(updatedDocumentList));
        } catch (JsonProcessingException e) {
            throw new ServiceException("Processing error updating draft documents",
                    "updateDraftDocumentsOnCaseInstance", e);
        }
        request.setDocuments(updatedDocumentList);
    }

    /**
     * Method to create a data object instance
     * @param variables                 : a {@link Map} of variables
     * @param dataObjectDefinitionKey   : a string with de data object definition key
     * @param dataObjectOperCreateDraft : a string with the name of the data object creation operation
     * @return                          : a new {@link DataObjectInstanceVariableContainer} object
     */
    public DataObjectInstanceVariableContainer createOperationDataObjectInstance(Map<String, Object> variables,
                                                                                 String dataObjectDefinitionKey,
                                                                                 String dataObjectOperCreateDraft) {

        DataObjectInstanceVariableContainerBuilder builder = dataObjectRuntimeService
                .createDataObjectValueInstanceBuilderByDefinitionKey(dataObjectDefinitionKey)
                .operation(dataObjectOperCreateDraft);

        for (Map.Entry<String, Object> variable : variables.entrySet()) {
            builder.value(variable.getKey(), variable.getValue());
        }
        return builder.create();
    }

    /**
     * Method to update a data object instance
     * @param variables                         : a Map with the operation fields
     * @param dataObjectDefinitionKey           : a String with de name of the data object definition key
     * @param dataObjectOperationUpdateDraft    : a String with the name of the operation
     *                                          to be applied in the data object
     */
    public void updateOperationDataObjectInstance(Map<String, Object> variables,
                                                  String dataObjectDefinitionKey,
                                                  String dataObjectOperationUpdateDraft) {

        DataObjectModificationBuilder modificationBuilder = dataObjectRuntimeService
                .createDataObjectModificationBuilder().definitionKey(dataObjectDefinitionKey)
                .operation(dataObjectOperationUpdateDraft);
        for (Map.Entry<String, Object> variable : variables.entrySet()) {
            modificationBuilder.value(variable.getKey(), variable.getValue());
        }
        modificationBuilder.modify();
    }



    /**
     *  Method to convert a map of task variables to a {@link TradeExternalTaskRequest} object
     * @param taskVariables     : a Map of task variables
     * @param userType          : a String whith the user type
     * @return                  : a {@link TradeExternalTaskRequest} object
     * @throws MappingException        : handles exceptions
     */
    public TradeExternalTaskRequest getTradeExternalTaskRequestFromMapVariables(
            Map<String, Object> taskVariables,
            String userType)  {
        try {
            TradeExternalTaskRequest externalTaskRequest = new TradeExternalTaskRequest();
            Map<String, Object> rootMap = (Map<String, Object>) taskVariables.get("root");
            String caseId = (String) rootMap.get("id");
            CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables()
                    .caseInstanceId(caseId).singleResult();
            Map<String, Object> caseVariables = caseInstance.getCaseVariables();
            DataObjectInstanceVariableContainerImpl operation = (DataObjectInstanceVariableContainerImpl) caseVariables
                    .get(OPERATION);
            TradeRequest request = tradeRequestMapper.mapToTradeRequest(caseVariables, userType);
            externalTaskRequest.setRequest(request);
            Instant createTimeInstant = (Instant) ((Map<String, Object>) taskVariables.get("task")).get("createTime");
            externalTaskRequest.setTaskCreationTime(Date.from(createTimeInstant));
            externalTaskRequest.setRequestCreatorName(operation.getString(REQUESTER_DISPLAYED_NAME));
            externalTaskRequest.setRequestCreationTime(operation.getDate(CREATION_DATE));
            externalTaskRequest.setReturnComment((String) rootMap.get(RETURN_COMMENT));
            externalTaskRequest.setReturnReason((String) rootMap.get(RETURN_REASON));
            return externalTaskRequest;
        } catch (JsonProcessingException e) {
            throw new MappingException("Error mapping request", "errorMappingRequest", e);
        } catch (ClassCastException ex) {
            LOG.error("Error casting an object to Instant", ex);
            throw new ClassCastException("Cannot cast createTime value to Instant");
        }
    }

        /**
     *  Method to get a map of task variables from a task id
     * @param taskId    : a String with the task id of a external task
     * @return          : return a Map of task variables
     */
    public Map<String, Object> getTradeExternalTaskVariables(String taskId) {
        if (Strings.isBlank(taskId)) {
            throw new InvalidRequestException("The field 'taskId' must be informed.", "errorTaskId");
        }
        Map<String, Object> taskVariables = platformTaskService.getTaskVariables(taskId);
        if (taskVariables == null) {
            throw new ResourceNotFoundException(String.format("No task found with id %s", taskId), "errorTaskNotFound");
        }
        //Comprobar que la tarea está categorizada correctamente
        if (!Boolean.TRUE.toString().equals(taskVariables.get(IS_EXTERNAL_TASK)) ||
                !TaskUtils.COMPLETE_INFORMATION.equals(taskVariables.get(EXTERNAL_TASK_TYPE))) {
            throw new InvalidRequestException(String.format("The task found with id %s is not" +
                    " the right type", taskId), "errorInvalidTask");
        }
        //Check that the task is not completed
        Map<String, Object> taskMap = (Map<String, Object>) taskVariables.get("task");
        if (taskMap.containsKey("endTime")){
            throw new InvalidRequestException(String.format("The task found with id %s is already" +
                    " completed", taskId), "errorInvalidTask");
        }
        return taskVariables;
    }


    /**
     * Method to save documents
     * @param userInfo       : a {@link UserInfo} object
     * @param caseInstanceId: a string object with the id of the case instance
     * @param documentsToAdd: a list of documents to add
     * @param documentList  : a list of documents
     * @param isDraft       : boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.document.Document
     */
    private void saveDocuments(UserInfo userInfo, String caseInstanceId, List<Document> documentsToAdd,
                               List<Document> documentList, boolean isDraft) {
        // PGNXTOTF-329 : For drafts, use the less restrictive setting.
        String userType = isDraft ? USER_TYPE_CUSTOMER : userInfo.getUser().getUserType();
        for (Document newDoc : documentsToAdd) {
            String newContentId = pagoNxtRequestUtils.createContent(newDoc, caseInstanceId, userType);
            newDoc.setDocumentId(newContentId);
            newDoc.setData(null);
            documentList.add(newDoc);
        }
    }

    /**
     *Method to add documents to a case instance
     * @param userInfo          : a {@link UserInfo} object with the info of the requester
     * @param documentsToAdd    : a {@link Document} list to add
     * @param caseInstanceId    : a String with the id of the case where to add the new documents
     */
    public void addDocumentsToCase(UserInfo userInfo,  List<Document> documentsToAdd, String caseInstanceId) {
        List<Document> savedDocuments = new ArrayList<>();
        //Create content in case for the new documents
        saveDocuments(userInfo, caseInstanceId, documentsToAdd, savedDocuments, false);
        //Get case instance variable containing the documents array
        ArrayList<CoreContentItem> operationDocuments = (ArrayList<CoreContentItem>) cmmnRuntimeService
                .getVariable(caseInstanceId, FieldConstants.OPERATION_DOCUMENTS);

        if (operationDocuments == null) {
            operationDocuments = new ArrayList<>();
        }
        //Add new documents to the case instance variable
        for (Document document : savedDocuments) {
            CoreContentItem contentEntity = platformContentItemService.getContentItem(document.getDocumentId());
            operationDocuments.add(contentEntity);
        }
        //Update the case instance variable
        cmmnRuntimeService.setVariable(caseInstanceId, FieldConstants.OPERATION_DOCUMENTS, operationDocuments);
    }
}