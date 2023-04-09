package com.pagonxt.onetradefinance.work.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.content.engine.impl.persistence.entity.ContentItemEntity;
import com.flowable.core.content.api.CoreContentItem;
import com.flowable.core.content.api.CoreContentService;
import com.flowable.core.content.api.DocumentDefinition;
import com.flowable.core.content.api.DocumentRepositoryService;
import com.flowable.core.content.api.MetadataService;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.content.ContentItemRepresentation;
import com.flowable.platform.service.content.PlatformContentItemService;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.document.DocumentMultipartFile;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.service.CalendarService;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.work.common.CaseCommonConstants;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.OperationDocumentMapper;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.job.service.impl.TimerJobServiceImpl;
import org.flowable.job.service.impl.persistence.entity.TimerJobEntity;
import org.flowable.job.service.impl.persistence.entity.TimerJobEntityManagerImpl;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.*;

/**
 * Class with PagoNxt request utils
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.platform.service.content.PlatformContentItemService
 * @see com.flowable.core.content.api.CoreContentService
 * @see org.flowable.cmmn.api.CmmnRuntimeService
 * @see com.flowable.core.content.api.MetadataService
 * @see com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper
 * @see com.flowable.core.content.api.DocumentRepositoryService
 * @see com.pagonxt.onetradefinance.integrations.service.CalendarService
 * @see com.pagonxt.onetradefinance.work.service.mapper.OperationDocumentMapper
 * @see org.flowable.cmmn.engine.CmmnEngineConfiguration
 * @since jdk-11.0.13
 */
@Component
public class PagoNxtRequestUtils {

    public static final String VISIBILITY = "visibility";
    private static final String UPDATE_TIMER_DUE_DATE = "updateTimerDueDate";

    //class attributes
    private final PlatformContentItemService platformContentItemService;
    private final CoreContentService contentService;
    private final CmmnRuntimeService cmmnRuntimeService;
    private final MetadataService metadataService;
    private final CaseCommonVariablesHelper caseCommonVariablesHelper;
    private final DocumentRepositoryService documentRepositoryService;
    private final OperationDocumentMapper operationDocumentMapper;
    private final CalendarService calendarService;
    private final CmmnEngineConfiguration cmmnEngineConfiguration;
    //TODO: Reemplazar por carga de propiedad de la aplicación
    private static final String TIMEZONE = "Europe/Madrid";

    /**
     * constructor method
     * @param platformContentItemService    : a PlatformContentItemService object
     * @param contentService                : a CoreContentService object
     * @param cmmnRuntimeService            : a CmmnRuntimeService object
     * @param metadataService               : a MetadataService object
     * @param caseCommonVariablesHelper     : a CaseCommonVariablesHelper object
     * @param documentRepositoryService     : a DocumentRepositoryService object
     * @param operationDocumentMapper       : a OperationDocumentMapper object
     * @param calendarService               : a CalendarService object
     * @param cmmnEngineConfiguration       : a CmmnEngineConfiguration object
     */
    public PagoNxtRequestUtils(PlatformContentItemService platformContentItemService,
                               CoreContentService contentService,
                               CmmnRuntimeService cmmnRuntimeService,
                               MetadataService metadataService,
                               CaseCommonVariablesHelper caseCommonVariablesHelper,
                               DocumentRepositoryService documentRepositoryService,
                               OperationDocumentMapper operationDocumentMapper,
                               CalendarService calendarService,
                               CmmnEngineConfiguration cmmnEngineConfiguration) {
        this.platformContentItemService = platformContentItemService;
        this.contentService = contentService;
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.metadataService = metadataService;
        this.caseCommonVariablesHelper = caseCommonVariablesHelper;
        this.documentRepositoryService = documentRepositoryService;
        this.operationDocumentMapper = operationDocumentMapper;
        this.calendarService = calendarService;
        this.cmmnEngineConfiguration = cmmnEngineConfiguration;
    }

    /**
     * Method to update draft document on case instance
     * @param request       : a PagoNxtRequest object
     * @param caseInstance  : a CaseInstance object
     * @see com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest
     * @see org.flowable.cmmn.api.runtime.CaseInstance
     */
    public void updateDraftDocumentsOnCaseInstance(PagoNxtRequest request, CaseInstance caseInstance) {
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
            List<String> currenDocumentsToRemoveIds =
                    currentDocumentList.stream().map(Document::getDocumentId).collect(Collectors.toList());

            List<String> newDocumentsIds =
                    newDocumentList.stream().map(Document::getDocumentId).collect(Collectors.toList());
            // Delete documents that are not in the new list from content and case variable
            currenDocumentsToRemoveIds.removeAll(newDocumentsIds);
            for (String docToRemoveId : currenDocumentsToRemoveIds) {
                contentService.deleteContentItem(docToRemoveId);

                currentDocumentList = currentDocumentList.stream()
                        .filter(doc -> !docToRemoveId.equals(doc.getDocumentId())).collect(Collectors.toList());
            }
            updatedDocumentList.addAll(currentDocumentList);
        }
        // Add new documents to case content and case variable
        List<Document> documentsToAdd = newDocumentList.stream()
                .filter(doc -> Strings.isBlank(doc.getDocumentId())).collect(Collectors.toList());

        saveDocuments(request, caseInstance.getId(), documentsToAdd, updatedDocumentList, true);
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
     * Method to upload documents
     * @param request       : a PagoNxtRequest object
     * @param caseInstanceId: a string object with the id of the case instance
     * @see com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest
     */
    public void uploadDocuments(PagoNxtRequest request, String caseInstanceId) {
        List<Document> documentsToAdd = request.getDocuments().stream()
                .filter(doc -> Strings.isBlank(doc.getDocumentId())).collect(Collectors.toList());

        List<Document> savedDocuments = new ArrayList<>();
        saveDocuments(request, caseInstanceId, documentsToAdd, savedDocuments, false);

        ArrayList<CoreContentItem> operationDocuments = (ArrayList<CoreContentItem>) cmmnRuntimeService
                .getVariable(caseInstanceId, FieldConstants.OPERATION_DOCUMENTS);

        if (operationDocuments == null) {
            operationDocuments = new ArrayList<>();
        }
        addOperationDocuments(caseInstanceId, savedDocuments, operationDocuments);
    }

    /**
     * Method to set operation documents from request documents
     * @param userType      : a string object with the user type
     * @param caseInstanceId: a string object with the id of the case instance
     * @see com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest
     */
    public void setOperationDocumentsFromRequestDocuments(String caseInstanceId, String userType) {
        ArrayList<CoreContentItem> operationDocuments = new ArrayList<>();
        Object requestDocumentsObj = cmmnRuntimeService.getVariable(caseInstanceId, REQUEST_DOCUMENTS);
        List<Document> requestDocumentsList;
        try {
            requestDocumentsList = caseCommonVariablesHelper.convertToDocumentList(requestDocumentsObj);
        } catch (JsonProcessingException e) {
            throw new ServiceException("Processing error setting operation documents",
                    "setOperationDocumentsFromRequestDocuments", e);
        }
        // PGNXTOTF-329 : Set visibility of documents before confirming draft
        requestDocumentsList.forEach(d -> metadataService.setMetadataValue(d.getDocumentId(),
                VISIBILITY, selectVisibility(userType)));

        addOperationDocuments(caseInstanceId, requestDocumentsList, operationDocuments);
    }

    /**
     * Method to add operation documents
     * @param caseInstanceId    : a string object with the id of the case instance
     * @param documentList      : a list of documents
     * @param operationDocuments: an arraylist of operation documents
     * @see com.pagonxt.onetradefinance.integrations.model.document.Document
     * @see com.flowable.core.content.api.CoreContentItem
     */
    private void addOperationDocuments(String caseInstanceId, List<Document> documentList,
                                       ArrayList<CoreContentItem> operationDocuments) {
        for (Document document : documentList) {
            CoreContentItem contentEntity = platformContentItemService.getContentItem(document.getDocumentId());
            operationDocuments.add(contentEntity);
        }
        cmmnRuntimeService.setVariable(caseInstanceId, FieldConstants.OPERATION_DOCUMENTS, operationDocuments);
    }

    /**
     * Method to save documents
     * @param request       : a PagoNxtRequest object
     * @param caseInstanceId: a string object with the id of the case instance
     * @param documentsToAdd: a list of documents to add
     * @param documentList  : a list of documents
     * @param isDraft       : boolean value
     * @see com.pagonxt.onetradefinance.integrations.model.document.Document
     */
    private void saveDocuments(PagoNxtRequest request, String caseInstanceId, List<Document> documentsToAdd,
                               List<Document> documentList, boolean isDraft) {
        // PGNXTOTF-329 : For drafts, use the less restrictive setting.
        String userType = isDraft ? USER_TYPE_CUSTOMER : request.getRequester().getUser().getUserType();
        for (Document newDoc : documentsToAdd) {
            String newContentId = createContent(newDoc, caseInstanceId, userType);
            newDoc.setDocumentId(newContentId);
            newDoc.setData(null);
            documentList.add(newDoc);
        }
    }

    /**
     * Method to create content
     * @param newDoc        : a Document object
     * @param caseInstanceId: a string object with the case instance id
     * @param requesterType : a string object with the requester type
     * @see com.pagonxt.onetradefinance.integrations.model.document.Document
     * @return a string object
     */
    public String createContent(Document newDoc, String caseInstanceId, String requesterType) {
        //Create content item
        DocumentMultipartFile multipartFile = new DocumentMultipartFile(newDoc);

        ContentItemRepresentation contentItemRepresentation =
                platformContentItemService.createContentItemOnCaseInstance(caseInstanceId, null, multipartFile);

        //Set metadata in new content item
        CoreContentItem contentItemEntity =
                platformContentItemService.getContentItem(contentItemRepresentation.getId());

        DocumentDefinition documentMetadataDefinition =
                documentRepositoryService
                        .getDocumentDefinitionByKey(CaseCommonConstants.DOCUMENT_DEFINITION_KEY_METADATA);

        contentItemEntity.setDefinitionId(documentMetadataDefinition.getId());

        metadataService.setMetadataValue(contentItemRepresentation.getId(), "type",
                documentMetadataDefinition.getKey());//Mandatory

        metadataService.setMetadataValue(contentItemRepresentation.getId(), "original", null);
        metadataService.setMetadataValue(contentItemRepresentation.getId(), "copy", null);

        metadataService.setMetadataValue(contentItemRepresentation.getId(), "typology",
                newDoc.getDocumentType());//Set document type key

        metadataService.setMetadataValue(contentItemRepresentation.getId(), VISIBILITY,
                selectVisibility(requesterType));//Set document visibility for uploaded documents from external app

        contentService.saveContentItem(contentItemEntity);
        return contentItemEntity.getId();
    }

    /**
     * Method to select the visibility
     * @param requesterType : a string with the requester type
     * @return  a string object
     */
    public String selectVisibility(String requesterType) {
        switch (requesterType) {
            case USER_TYPE_CUSTOMER:
                return CaseCommonConstants.VISIBILITY_CUSTOMER;
            case USER_TYPE_OFFICE:
            case USER_TYPE_MIDDLE_OFFICE:
                return CaseCommonConstants.VISIBILITY_OFFICE;
            case USER_TYPE_BACKOFFICE:
            default:
                return CaseCommonConstants.VISIBILITY_BO;
        }
    }

    /**
     * Method to check if a item is visible
     * @param contentItemId : a string object with the content item id
     * @param requesterType : a string object with the requester type
     * @return true or false if the item is visible
     */
    public boolean isVisible(String contentItemId, String requesterType) {
        String itemVisibility = (String) metadataService.getMetadataValue(contentItemId, VISIBILITY);
        switch (requesterType) {
            case USER_TYPE_CUSTOMER:
                return CaseCommonConstants.VISIBILITY_CUSTOMER.equals(itemVisibility);
            case USER_TYPE_OFFICE:
            case USER_TYPE_MIDDLE_OFFICE:
                return !CaseCommonConstants.VISIBILITY_BO.equals(itemVisibility);
            case USER_TYPE_BACKOFFICE:
                return true;
            default:
                return false;
        }
    }

    /**
     * Method to map data
     * @param completeInfoExportCollectionRequest   : a completeInfoExportCollectionRequest
     * @param caseVariables                         : case variables
     * @param task                                  : a TaskRepresentation object
     * @param taskVariables                         : a collection of task variables
     * @param <T>                                   : a generic collection
     * @return a completeInfoExportCollectionRequest
     */
    public <T extends CompleteInfoPagoNxtRequest> T mapCompleteInfoPagoNxtRequest(T completeInfoExportCollectionRequest,
                                                                                  Map<String, Object> caseVariables,
                                                                                  TaskRepresentation task,
                                                                                  Map<String, Object> taskVariables) {
        completeInfoExportCollectionRequest.setReturnReason((String) taskVariables.get(RETURN_REASON));
        completeInfoExportCollectionRequest.setReturnComment((String) taskVariables.get(RETURN_COMMENT));

        DataObjectInstanceVariableContainer operation =
                (DataObjectInstanceVariableContainerImpl) caseVariables.get(OPERATION);

        completeInfoExportCollectionRequest.setRequestCreationTime(operation.getDate(CREATION_DATE));
        completeInfoExportCollectionRequest.setRequestCreatorName(operation.getString(REQUESTER_DISPLAYED_NAME));
        completeInfoExportCollectionRequest.setTaskCreationTime(task.getCreateTime());
        return completeInfoExportCollectionRequest;
    }

    /**
     * Method to map data
     * @param completeInfoExportCollectionRequest   : a completeInfoExportCollectionRequest
     * @param caseVariables                         : case variables
     * @param <T>                                   : a generic collection
     * @return a completeInfoExportCollectionRequest
     */
    public <T extends CompleteInfoPagoNxtRequest> T mapDetailedPagoNxtRequest(T completeInfoExportCollectionRequest,
                                                                              Map<String, Object> caseVariables) {
        completeInfoExportCollectionRequest.setReturnReason(null);
        completeInfoExportCollectionRequest.setReturnComment(null);

        DataObjectInstanceVariableContainer operation =
                (DataObjectInstanceVariableContainerImpl) caseVariables.get(OPERATION);

        completeInfoExportCollectionRequest.setRequestCreationTime(operation.getDate(CREATION_DATE));
        completeInfoExportCollectionRequest.setRequestCreatorName(operation.getString(REQUESTER_DISPLAYED_NAME));
        completeInfoExportCollectionRequest.setTaskCreationTime(operation.getDate(CREATION_DATE));
        return completeInfoExportCollectionRequest;
    }

    /**
     * Method to map common variables
     * @param variables : a collection of variables
     * @param request   : a PagoNxtRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest
     */
    public void mapCommonVariables(Map<String, Object> variables, PagoNxtRequest request) {
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
        variables.put(REQUEST_CUSTOMER_ID, customer.getTaxId());
        variables.put(REQUEST_CUSTOMER_FULL_NAME, customer.getName());
        variables.put(REQUEST_CUSTOMER_CODE, customer.getPersonNumber());
        variables.put(REQUEST_CUSTOMER_SEGMENT, customer.getSegment());
        variables.put(REQUEST_CUSTOMER_EMAIL, customer.getEmail());
    }

    /**
     * Method to map advance risk line variables
     * @param variables         : a collection of variables
     * @param advanceRiskLine  : a RiskLine object
     * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
     */
    public void mapAdvanceRiskLineVariables(Map<String, Object> variables, RiskLine advanceRiskLine) {
        variables.put(REQUEST_ADVANCE_RISK_LINE_ID, advanceRiskLine.getRiskLineId());
        variables.put(REQUEST_ADVANCE_RISK_LINE_IBAN, advanceRiskLine.getIban());
        variables.put(REQUEST_ADVANCE_RISK_LINE_DUE_DATE, DateUtils.dateToLocalDate(advanceRiskLine.getExpires()));
        variables.put(REQUEST_ADVANCE_RISK_LINE_AVAILABLE_AMOUNT, advanceRiskLine.getAvailableAmount());
        variables.put(REQUEST_ADVANCE_RISK_LINE_CURRENCY, advanceRiskLine.getCurrency());
    }

    /**
     * Method to map operation type variables
     * @param variables      : a collection of variables
     * @param operationType  : a string object with the operation type
     */
    public void mapOperationTypeVariables(Map<String, Object> variables, String operationType) {
        variables.put(REQUEST_OPERATION_TYPE, operationType);
    }

    /**
     * Method to set common variables from map variables
     * @param request   :a PagoNxtRequest object
     * @param variables :a collection of variables
     * @param userType  :a string object with the user type
     * @see com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest
     * @throws JsonProcessingException handles Json Processing exceptions
     */
    public void setCommonVariablesFromMapVariables(PagoNxtRequest request, Map<String, Object> variables,
                                                   String userType) throws JsonProcessingException {

        DataObjectInstanceVariableContainer operation =
                (DataObjectInstanceVariableContainerImpl) variables.get(OPERATION);

        User user = new User();
        user.setUserId(operation.getString(REQUESTER_ID));
        user.setUserDisplayedName(operation.getString(REQUESTER_DISPLAYED_NAME));
        user.setUserType(operation.getString(REQUESTER_TYPE));
        request.setRequester(new UserInfo(user));

        request.setProduct(operation.getString(PRODUCT));
        request.setEvent(operation.getString(EVENT));
        request.setCountry(operation.getString(COUNTRY));

        request.setOffice(operation.getString(REQUEST_OFFICE));
        request.setMiddleOffice(operation.getString(REQUEST_MIDDLE_OFFICE));

        request.setPriority(operation.getString(REQUEST_PRIORITY));
        request.setComment(operation.getString(REQUEST_COMMENTS));
        request.setDisplayedStatus(operation.getString(DISPLAYED_STATUS));
        // Documents
        List<Document> documents;
        if (VALUE_DRAFT.equals(operation.getString(DISPLAYED_STATUS))) {
            documents = caseCommonVariablesHelper.convertToDocumentList(variables.get(REQUEST_DOCUMENTS));
        } else {
            documents = operationDocumentMapper
                    .mapOperationDocuments((List<ContentItemEntity>) variables.get(OPERATION_DOCUMENTS));
        }
        // Filter documents by visibility
        documents = documents.stream().filter(d -> isVisible(d.getDocumentId(), userType)).collect(Collectors.toList());
        request.setDocuments(documents);
    }

    /**
     * Method to update draft timers
     * @param caseInstanceId : a string with the case instance id
     */
    public void updateDraftTimers(String caseInstanceId) {
        //Get timerJobEntityManager for find timerEventInstances
        TimerJobServiceImpl timerJobService =
                (TimerJobServiceImpl) cmmnEngineConfiguration.getJobServiceConfiguration().getTimerJobService();

        TimerJobEntityManagerImpl timerJobEntityManager =
                (TimerJobEntityManagerImpl) timerJobService.getTimerJobEntityManager();

        //Calculate new reminder timer due date and update
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        Duration nextDayDuration = calendarService.nextWorkDay(1);
        LocalDateTime nextDayLocalDateTime = nowLocalDateTime.plusSeconds(nextDayDuration.getSeconds());
        Date nextWorkDayDate = Date.from(nextDayLocalDateTime.atZone(ZoneId.of(TIMEZONE)).toInstant());

        updateTimerDueDate(caseInstanceId, CaseCommonConstants.TIMER_DEFINITION_ID_DRAFT_REMINDER,
                nextWorkDayDate, timerJobEntityManager);
        //Calculate new reminder timer due date and update
        Duration inTwoDaysDuration = calendarService.nextWorkDay(2);
        LocalDateTime inTwoDaysLocalDateTime = nowLocalDateTime.plusSeconds(inTwoDaysDuration.getSeconds());
        Date inTwoDaysDate = Date.from(inTwoDaysLocalDateTime.atZone(ZoneId.of(TIMEZONE)).toInstant());

        updateTimerDueDate(caseInstanceId, CaseCommonConstants.TIMER_DEFINITION_ID_DRAFT_CANCELATION,
                inTwoDaysDate, timerJobEntityManager);
    }

    /**
     * Method to update timer due date
     * @param caseInstanceId        : a string object with the case instance id
     * @param timerDefinitionId     : a string object with the timer definition id
     * @param newDueDate            : a Date object with the new due date
     * @param timerJobEntityManager : a TimerJobEntityManagerImpl object
     * @see org.flowable.job.service.impl.persistence.entity.TimerJobEntityManagerImpl
     */
    private void updateTimerDueDate(String caseInstanceId, String timerDefinitionId, Date newDueDate,
                                    TimerJobEntityManagerImpl timerJobEntityManager) {

        cmmnEngineConfiguration.getCommandExecutor().execute(commandContext -> {
            PlanItemInstance timerEventInstance = cmmnRuntimeService.createPlanItemInstanceQuery()
                    .caseInstanceId(caseInstanceId)
                    .planItemDefinitionId(timerDefinitionId)
                    .singleResult();
            if (timerEventInstance == null) {
                throw new ResourceNotFoundException(String.format("No timer event instance found" +
                        " with id %s", timerDefinitionId), UPDATE_TIMER_DUE_DATE);
            }
            if (!"available".equalsIgnoreCase(timerEventInstance.getState())) {
                throw new ResourceNotFoundException(String.format("No available timer event instance found" +
                        " for id %s", timerDefinitionId), UPDATE_TIMER_DUE_DATE);
            }
            //Retrieve job entity
            List<TimerJobEntity> timerJobEntityList =
                    timerJobEntityManager.findJobsByScopeIdAndSubScopeId(caseInstanceId, timerEventInstance.getId());

            if (timerJobEntityList.isEmpty()) {
                throw new ResourceNotFoundException(String.format("No timer entity found" +
                        " for id %s", timerDefinitionId), UPDATE_TIMER_DUE_DATE);
            }
            TimerJobEntity timerJobEntity = timerJobEntityList.get(0);
            //update timer job
            timerJobEntity.setDuedate(newDueDate);
            timerJobEntityManager.update(timerJobEntity, true);
            return null;
        });
    }
}
