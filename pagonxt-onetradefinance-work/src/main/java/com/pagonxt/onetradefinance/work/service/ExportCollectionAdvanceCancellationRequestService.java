package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.work.common.FilterConstants;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper;
import com.pagonxt.onetradefinance.work.utils.ExportCollectionUtils;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import com.pagonxt.onetradefinance.work.validation.ExportCollectionValidation;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.history.HistoricCaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.work.common.CaseCommonConstants.DATA_OBJECT_MODEL_ADVANCE;

/**
 * service class for a cancellation request of advance of an export collection
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.cmmn.api.CmmnRuntimeService
 * @see com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils
 * @see com.flowable.dataobject.api.runtime.DataObjectRuntimeService
 * @see com.pagonxt.onetradefinance.work.utils.ExportCollectionUtils
 * @see com.pagonxt.onetradefinance.work.utils.TaskUtils
 * @see com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper
 * @see com.pagonxt.onetradefinance.work.validation.ExportCollectionValidation
 * @see com.pagonxt.onetradefinance.work.security.CaseSecurityService
 * @see org.flowable.cmmn.api.CmmnHistoryService
 * @since jdk-11.0.13
 */
@Service
public class ExportCollectionAdvanceCancellationRequestService {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionAdvanceCancellationRequestService.class);

    private static final String EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_KEY = "CLE_C005";
    public static final String DATA_OBJECT_DEFINITION_KEY = "CLE_DO007";
    public static final String DATA_OBJECT_OPERATION_CREATE_ADVANCE_CANCELLATION = "createOperation";

    //class attributes
    private final CmmnRuntimeService cmmnRuntimeService;
    private final PagoNxtRequestUtils pagoNxtRequestUtils;
    private final DataObjectRuntimeService dataObjectRuntimeService;
    private final ExportCollectionUtils exportCollectionUtils;
    private final TaskUtils taskUtils;
    private final ExportCollectionMapper exportCollectionMapper;
    private final ExportCollectionValidation exportCollectionValidation;
    private final CaseSecurityService caseSecurityService;
    private final CmmnHistoryService cmmnHistoryService;

    /**
     * constructor method
     * @param cmmnRuntimeService        : a CmmnRuntimeService object
     * @param dataObjectRuntimeService  : a DataObjectRuntimeService object
     * @param pagoNxtRequestUtils       : a PagoNxtRequestUtils object
     * @param exportCollectionUtils     : a ExportCollectionUtils object
     * @param taskUtils                 : a TaskUtils object
     * @param exportCollectionMapper    : a ExportCollectionMapper object
     * @param exportCollectionValidation: a ExportCollectionValidation object
     * @param caseSecurityService       : a CaseSecurityService object
     * @param cmmnHistoryService        : a CmmnHistoryService object
     */
    public ExportCollectionAdvanceCancellationRequestService(CmmnRuntimeService cmmnRuntimeService,
                                                             DataObjectRuntimeService dataObjectRuntimeService,
                                                             PagoNxtRequestUtils pagoNxtRequestUtils,
                                                             ExportCollectionUtils exportCollectionUtils,
                                                             TaskUtils taskUtils,
                                                             ExportCollectionMapper exportCollectionMapper,
                                                             ExportCollectionValidation exportCollectionValidation,
                                                             CaseSecurityService caseSecurityService,
                                                             CmmnHistoryService cmmnHistoryService) {
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.dataObjectRuntimeService = dataObjectRuntimeService;
        this.pagoNxtRequestUtils = pagoNxtRequestUtils;
        this.exportCollectionUtils = exportCollectionUtils;
        this.taskUtils = taskUtils;
        this.exportCollectionValidation = exportCollectionValidation;
        this.exportCollectionMapper = exportCollectionMapper;
        this.caseSecurityService = caseSecurityService;
        this.cmmnHistoryService = cmmnHistoryService;
    }

    /**
     * Method to create a cancellation request of advance of an export collection
     * @param request : a cancellation request of advance of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     * @return a ExportCollectionAdvanceCancellationRequest object
     */
    @Transactional
    public ExportCollectionAdvanceCancellationRequest createExportCollectionAdvanceCancellationRequest(
            ExportCollectionAdvanceCancellationRequest request) {
        exportCollectionValidation.validateConfirm(request);
        // Create Export Collection Advance Cancellation Case
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                .caseDefinitionKey(EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_KEY).start();
        String code = (String) caseInstance.getCaseVariables().get(EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_SEQ);
        Map<String, Object> variables = generateRequestVariablesMap(request);
        variables.put(REQUEST_CODE, code);
        try {
            cmmnRuntimeService.setVariable(caseInstance.getId(), OPERATION, generateVariableOperation(variables));
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Mandatory data for create a operation in flow with" +
                    " code %s is missing", code), "createExportCollectionAdvanceCancellationRequest",e);
        }
        cmmnRuntimeService.setVariable(caseInstance.getId(), COUNTRY, request.getCountry());
        //Upload documents
        pagoNxtRequestUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);
        pagoNxtRequestUtils.setOperationDocumentsFromRequestDocuments(caseInstance.getId(),
                request.getRequester().getUser().getUserType());
        // Set Request variables
        request.setCode(code);
        request.setProduct((String) caseInstance.getCaseVariables().get(PRODUCT));
        request.setEvent((String) caseInstance.getCaseVariables().get(EVENT));
        // Delete data no longer necessary
        cmmnRuntimeService.removeVariable(caseInstance.getId(), REQUEST_DOCUMENTS);
        // Return Request
        LOG.debug("createExportCollectionCancellationRequest(request: {}) created instance {}", request, caseInstance);
        return request;
    }

    /**
     * Method to get a complete info task of a cancellation request of advance of an export collection
     * @param taskId    : a string with the task id (complete info task)
     * @param userInfo  : a UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a CompleteInfoExportCollectionAdvanceCancellationRequest object
     */
    @Transactional
    public CompleteInfoExportCollectionAdvanceCancellationRequest
                    getCompleteInfoExportCollectionAdvanceCancellationRequest(String taskId, UserInfo userInfo) {
        caseSecurityService.checkReadTask(userInfo, taskId);
        Map<String, Object> taskVariables = taskUtils.getTaskVariablesCompleteInfo(taskId);
        TaskRepresentation task = taskUtils.getTask(taskId, false);
        CaseInstance caseInstance = getCaseInstanceByCode((String) taskVariables.get(FilterConstants.OPERATION_CODE));
        try {
            return mapToCompleteInfoExportCollectionAdvanceCancellationRequest(caseInstance.getCaseVariables(),
                    task, taskVariables, userInfo.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException("Error mapping request", "errorMappingCompleteInfo", e);
        }
    }

    /**
     * Method to complete a complete info task of a cancellation request of advance of an export collection
     * @param taskId    : a string with the task id (complete info task)
     * @param request   : a cancellation request of advance of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     */
    @Transactional
    public void completeCompleteInfoExportCollectionAdvanceCancellationRequest(
            ExportCollectionAdvanceCancellationRequest request, String taskId) {
        caseSecurityService.checkEditTask(request.getRequester(), taskId);
        Map<String, Object> taskVariables = taskUtils.setCompleteInfoTaskVariables(taskId,
                request.getComment(), generateRequestVariablesMap(request));
        pagoNxtRequestUtils.uploadDocuments(request, getCaseInstanceByCode(request.getCode()).getId());
        taskUtils.completeTaskCompleteInfo(taskId, taskVariables, request.getRequester().getUser(),
                FieldConstants.VALUE_COMPLETE);
    }

    /**
     * Method to get petition details of a cancellation request of advance of an export collection
     * @param requestId: a string with the request id
     * @param userInfo : an UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest
     * @see  com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a CompleteInfoExportCollectionAdvanceCancellationRequest object
     */
    @Transactional
    public CompleteInfoExportCollectionAdvanceCancellationRequest getPetitionRequestDetails(
            String requestId, UserInfo userInfo) {
        caseSecurityService.checkRead(userInfo, requestId);
        HistoricCaseInstance caseInstance = getHistoricCaseInstanceByCode(requestId);
        try {
            return mapToDetailedExportCollectionAdvanceRequest(caseInstance
                    .getCaseVariables(), userInfo.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException("Error mapping request", "errorMappingCompleteInfo", e);
        }
    }

    /**
     * Method to get a case instance by code
     * @param code: a string with the code
     * @see  org.flowable.cmmn.api.runtime.CaseInstance
     * @return a CaseInstance object
     */
    private CaseInstance getCaseInstanceByCode(String code) {
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceQuery()
                .includeCaseVariables()
                .caseDefinitionKey(EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_KEY)
                .variableValueEquals(EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_SEQ, code)
                .singleResult();
        if (caseInstance == null) {
            throw new ResourceNotFoundException(String.format("No case found with code %s", code), "errorCaseNotFound");
        }
        return caseInstance;
    }

    /**
     * Method to get a historic case instance by code
     * @param code: a string with the code
     * @see  org.flowable.cmmn.api.history.HistoricCaseInstance
     * @return a HistoricCaseInstance object
     */
    private HistoricCaseInstance getHistoricCaseInstanceByCode(String code) {
        HistoricCaseInstance caseInstance = cmmnHistoryService.createHistoricCaseInstanceQuery()
                .includeCaseVariables()
                .caseDefinitionKey(EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_KEY)
                .variableValueEquals(EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_SEQ, code)
                .singleResult();
        if (caseInstance == null) {
            throw new ResourceNotFoundException(String.format("No case found with code %s", code), "errorCaseNotFound");
        }
        return caseInstance;
    }

    /**
     * Create the flowable data object to include in the case variables.
     * @param variables : a collection of variables
     * @see com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer
     * @return a DataObjectInstanceVariableContainer object
     */
    private DataObjectInstanceVariableContainer generateVariableOperation(Map<String, Object> variables) {
        DataObjectInstanceVariableContainerBuilder builder = dataObjectRuntimeService
                .createDataObjectValueInstanceBuilderByDefinitionKey(DATA_OBJECT_DEFINITION_KEY)
                .operation(DATA_OBJECT_OPERATION_CREATE_ADVANCE_CANCELLATION);
        for(Map.Entry<String, Object> variable : variables.entrySet()) {
            builder.value(variable.getKey(), variable.getValue());
        }
        return builder.create();
    }

    /**
     * Method to generate request variables map.
     * @param request : a cancellation request of advance of an export collection
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     * @return a map with request variables
     */
    private Map<String, Object> generateRequestVariablesMap(ExportCollectionAdvanceCancellationRequest request) {
        HashMap<String, Object> variables = new HashMap<>();
        pagoNxtRequestUtils.mapCommonVariables(variables, request);
        pagoNxtRequestUtils.mapCustomerVariables(variables, request.getCustomer());
        variables.put(EXPORT_COLLECTION_ADVANCE, exportCollectionUtils
                .findDataObjectByIdAndModel(request.getExportCollectionAdvance().getCode(), DATA_OBJECT_MODEL_ADVANCE));
        return variables;
    }

    /**
     * Method to map data
     * @param variables : a collection of variables
     * @param userType  : a string with the user type
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     * @throws JsonProcessingException : handles Json Processing exceptions
     * @return an ExportCollectionAdvanceCancellationRequest object
     */
    private ExportCollectionAdvanceCancellationRequest mapToExportCollectionAdvanceCancellationRequest(
            Map<String, Object> variables, String userType) throws JsonProcessingException {

        DataObjectInstanceVariableContainer operation = (DataObjectInstanceVariableContainerImpl) variables
                .get(OPERATION);

        ExportCollectionAdvanceCancellationRequest exportCollectionAdvanceCancellationRequest =
                new ExportCollectionAdvanceCancellationRequest();

        exportCollectionAdvanceCancellationRequest.setCode((String) variables
                .get(EXPORT_COLLECTION_ADVANCE_CANCELLATION_CASE_SEQ));

        // Common request fields
        pagoNxtRequestUtils.setCommonVariablesFromMapVariables(exportCollectionAdvanceCancellationRequest,
                variables, userType);

        // Custom fields
        Customer customer = new Customer();

        customer.setPersonNumber(operation.getString(REQUEST_CUSTOMER_CODE));
        exportCollectionAdvanceCancellationRequest.setCustomer(customer);

        exportCollectionAdvanceCancellationRequest.setExportCollectionAdvance(exportCollectionMapper
                .mapDataObjectInstanceVariableContainerToExportCollectionAdvance((DataObjectInstanceVariableContainer)
                        operation.getVariable(EXPORT_COLLECTION_ADVANCE)));

        return exportCollectionAdvanceCancellationRequest;
    }

    /**
     * Method to map data
     * @param caseVariables : a collection of case variables
     * @param task          : a TaskRepresentation object
     * @param taskVariables : a collection of task variables
     * @param userType      : a string with the user type
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest
     * @see com.flowable.platform.service.task
     * @return a CompleteInfoExportCollectionAdvanceCancellationRequest object
     * @throws JsonProcessingException handles a Json Processing exception
     */
    private CompleteInfoExportCollectionAdvanceCancellationRequest mapToCompleteInfoExportCollectionAdvanceCancellationRequest(
            Map<String, Object> caseVariables, TaskRepresentation task, Map<String, Object> taskVariables,
            String userType) throws JsonProcessingException {

        CompleteInfoExportCollectionAdvanceCancellationRequest completeInfoExportCollectionCancellationRequest =
                new CompleteInfoExportCollectionAdvanceCancellationRequest();

        ExportCollectionAdvanceCancellationRequest request =
                mapToExportCollectionAdvanceCancellationRequest(caseVariables, userType);

        request.setComment(null);

        completeInfoExportCollectionCancellationRequest.setRequest(request);
        pagoNxtRequestUtils.mapCompleteInfoPagoNxtRequest(completeInfoExportCollectionCancellationRequest,
                caseVariables, task, taskVariables);

        return completeInfoExportCollectionCancellationRequest;
    }

    /**
     * Method to map data
     * @param caseVariables : a collection of case variables
     * @param userType      : a string with the user type
     * @return a CompleteInfoExportCollectionAdvanceCancellationRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest
     * @throws JsonProcessingException handles a Json Processing exception
     */
    private CompleteInfoExportCollectionAdvanceCancellationRequest mapToDetailedExportCollectionAdvanceRequest(
            Map<String, Object> caseVariables, String userType) throws JsonProcessingException {

        CompleteInfoExportCollectionAdvanceCancellationRequest completeInfoExportCollectionAdvanceCancellationRequest =
                new CompleteInfoExportCollectionAdvanceCancellationRequest();

        ExportCollectionAdvanceCancellationRequest request =
                mapToExportCollectionAdvanceCancellationRequest(caseVariables, userType);

        completeInfoExportCollectionAdvanceCancellationRequest.setRequest(request);

        pagoNxtRequestUtils
                .mapDetailedPagoNxtRequest(completeInfoExportCollectionAdvanceCancellationRequest, caseVariables);

        return completeInfoExportCollectionAdvanceCancellationRequest;
    }
}
