package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectModificationBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.common.FilterConstants;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.ExchangeInsuranceFlowableMapper;
import com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper;
import com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable;
import com.pagonxt.onetradefinance.work.utils.DateUtils;
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
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.work.common.CaseCommonConstants.*;

/**
 * service class for a request of advance of an export collection
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
 * @see com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper
 * @see com.pagonxt.onetradefinance.work.service.mapper.ExchangeInsuranceFlowableMapper
 * @since jdk-11.0.13
 */
@Service
public class ExportCollectionAdvanceRequestService {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionAdvanceRequestService.class);

    private static final String CREATE_EXPORT_COLLECTION_ADVANCE_REQUEST = "createExportCollectionAdvanceRequest";
    private static final String UPDATE_EXPORT_COLLECTION_ADVANCE_REQUEST = "updateExportCollectionAdvanceRequest";
    private static final String CONFIRM_EXPORT_COLLECTION_ADVANCE_REQUEST = "confirmExportCollectionAdvanceRequest";
    private static final String EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_KEY = "CLE_C003";
    public static final String DATA_OBJECT_DEFINITION_KEY = "CLE_DO004";
    public static final String DATA_OBJECT_OPERATION_CREATE_ADVANCE_DRAFT = "createDraft";
    public static final String DATA_OBJECT_OPERATION_UPDATE_ADVANCE_DRAFT = "updateDraft";
    public static final String ERROR_MAPPING_REQUEST = "Error mapping request";

    //class attributes
    private final CmmnRuntimeService cmmnRuntimeService;
    private final PagoNxtRequestUtils pagoNxtRequestUtils;
    private final ExportCollectionUtils exportCollectionUtils;
    private final ExportCollectionMapper exportCollectionMapper;
    private final CaseCommonVariablesHelper caseCommonVariablesHelper;
    private final CaseSecurityService caseSecurityService;
    private final TaskUtils taskUtils;
    private final DataObjectRuntimeService dataObjectRuntimeService;
    private final ExchangeInsuranceFlowableMapper exchangeInsuranceFlowableMapper;
    private final ExportCollectionValidation exportCollectionValidation;
    private final CmmnHistoryService cmmnHistoryService;

    /**
     * constructor method
     * @param cmmnRuntimeService                : a CmmnRuntimeService object
     * @param dataObjectRuntimeService          : a DataObjectRuntimeService object
     * @param pagoNxtRequestUtils               : a PagoNxtRequestUtils object
     * @param exportCollectionUtils             : a ExportCollectionUtils object
     * @param taskUtils                         : a TaskUtils object
     * @param exportCollectionMapper            : a ExportCollectionMapper object
     * @param exportCollectionValidation        : a ExportCollectionValidation object
     * @param caseSecurityService               : a CaseSecurityService object
     * @param cmmnHistoryService                : a CmmnHistoryService object
     * @param caseCommonVariablesHelper         : a CaseCommonVariablesHelper object
     * @param exchangeInsuranceFlowableMapper   : a ExchangeInsuranceFlowableMapper object
     */
    public ExportCollectionAdvanceRequestService(CmmnRuntimeService cmmnRuntimeService,
                                                 DataObjectRuntimeService dataObjectRuntimeService,
                                                 PagoNxtRequestUtils pagoNxtRequestUtils,
                                                 ExportCollectionUtils exportCollectionUtils,
                                                 ExportCollectionMapper exportCollectionMapper,
                                                 ExchangeInsuranceFlowableMapper exchangeInsuranceFlowableMapper,
                                                 CaseCommonVariablesHelper caseCommonVariablesHelper,
                                                 CaseSecurityService caseSecurityService,
                                                 TaskUtils taskUtils,
                                                 ExportCollectionValidation exportCollectionValidation,
                                                 CmmnHistoryService cmmnHistoryService) {
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.dataObjectRuntimeService = dataObjectRuntimeService;
        this.pagoNxtRequestUtils = pagoNxtRequestUtils;
        this.exportCollectionUtils = exportCollectionUtils;
        this.exportCollectionMapper = exportCollectionMapper;
        this.caseCommonVariablesHelper = caseCommonVariablesHelper;
        this.caseSecurityService = caseSecurityService;
        this.taskUtils = taskUtils;
        this.exchangeInsuranceFlowableMapper = exchangeInsuranceFlowableMapper;
        this.exportCollectionValidation = exportCollectionValidation;
        this.cmmnHistoryService = cmmnHistoryService;
    }

    /**
     * Method to create a request of advance of an export collection
     * @param request : a request of advance of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return a ExportCollectionAdvanceRequest object
     */
    @Transactional
    public ExportCollectionAdvanceRequest createExportCollectionAdvanceRequest(ExportCollectionAdvanceRequest request) {
        exportCollectionValidation.validateDraft(request);
        if (!Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("In a draft creation, the field 'code' cannot be informed.",
                    CREATE_EXPORT_COLLECTION_ADVANCE_REQUEST);
        }
        // Create Export Collection Advance Case
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                .caseDefinitionKey(EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_KEY).start();

        String code = (String) caseInstance.getCaseVariables().get(EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_SEQ);
        Map<String, Object> variables = generateRequestVariablesMap(request);
        variables.put(REQUEST_CODE, code);
        try {
            DataObjectInstanceVariableContainer operation = generateVariableOperation(variables);
            cmmnRuntimeService.setVariable(caseInstance.getId(), OPERATION, operation);
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Mandatory data for create a operation in flow" +
                    " with code %s is missing", code), CREATE_EXPORT_COLLECTION_ADVANCE_REQUEST,e);
        }
        cmmnRuntimeService.setVariable(caseInstance.getId(), REQUEST_SAVED_STEP, request.getSavedStep());
        cmmnRuntimeService.setVariable(caseInstance.getId(), COUNTRY, request.getCountry());
        request.setCode(code);
        request.setProduct((String) caseInstance.getCaseVariables().get(VARNAME_PRODUCT));
        request.setEvent((String) caseInstance.getCaseVariables().get(VARNAME_EVENT));
        pagoNxtRequestUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);
        LOG.debug("createExportCollectionAdvanceRequest(request: {}) created instance {}", request, caseInstance);
        return request;
    }

    /**
     * Method to update a request of advance of an export collection
     * @param request : a request of advance of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return a ExportCollectionAdvanceRequest object
     */
    @Transactional
    public ExportCollectionAdvanceRequest updateExportCollectionAdvanceRequest(ExportCollectionAdvanceRequest request) {
        LOG.debug("updateExportCollectionAdvanceRequest(request: {})", request);
        exportCollectionValidation.validateDraft(request);
        if (Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("Mandatory data is missing: code",
                    UPDATE_EXPORT_COLLECTION_ADVANCE_REQUEST);
        }
        CaseInstance caseInstance = getCaseInstanceByCode(request.getCode());
        caseSecurityService.checkEdit(request.getRequester(), caseInstance);
        Map<String, Object> variables = generateRequestVariablesMap(request);
        variables.put(REQUEST_CODE, request.getCode());
        try {
            updateVariableOperation(variables);
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Mandatory data for update a operation in flow" +
                    " with code %s is missing", request.getCode()), UPDATE_EXPORT_COLLECTION_ADVANCE_REQUEST,e);
        }
        cmmnRuntimeService.setVariable(caseInstance.getId(), REQUEST_SAVED_STEP, request.getSavedStep());
        //update draft timers
        pagoNxtRequestUtils.updateDraftTimers(caseInstance.getId());
        pagoNxtRequestUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);
        return request;
    }

    /**
     * Method to confirm a request of advance of an export collection
     * @param request : a request of advance of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return a ExportCollectionAdvanceRequest object
     */
    @Transactional
    public ExportCollectionAdvanceRequest confirmExportCollectionAdvanceRequest(
            ExportCollectionAdvanceRequest request) {

        LOG.debug("confirmExportCollectionAdvanceRequest(request: {})", request);
        exportCollectionValidation.validateConfirm(request);
        //TODO: ¡¡¡Enviar el SLA definitivo!!!
        if (Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("Mandatory data is missing: code",
                    CONFIRM_EXPORT_COLLECTION_ADVANCE_REQUEST);
        }
        CaseInstance caseInstance = getCaseInstanceByCode(request.getCode());
        caseSecurityService.checkEdit(request.getRequester(), caseInstance);

        pagoNxtRequestUtils.setOperationDocumentsFromRequestDocuments(caseInstance.getId(),
                request.getRequester().getUser().getUserType());

        taskUtils.completeTaskDraft(caseInstance, request.getRequester().getUser());
        return request;
    }

    /**
     * Method to get a complete info task of a request of advance of an export collection
     * @param taskId    : a string with the task id (complete info task)
     * @param userInfo  : a UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a CompleteInfoExportCollectionAdvanceRequest object
     */
    @Transactional
    public CompleteInfoExportCollectionAdvanceRequest getCompleteInfoExportCollectionAdvanceRequest(String taskId,
                                                                                                    UserInfo userInfo) {
        caseSecurityService.checkReadTask(userInfo, taskId);
        Map<String, Object> taskVariables = taskUtils.getTaskVariablesCompleteInfo(taskId);
        TaskRepresentation task = taskUtils.getTask(taskId, false);
        CaseInstance caseInstance = getCaseInstanceByCode((String) taskVariables.get(FilterConstants.OPERATION_CODE));
        try {
            return mapToCompleteInfoExportCollectionAdvanceRequest(caseInstance.getCaseVariables(),
                    task, taskVariables, userInfo.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, "errorMappingCompleteInfo", e);
        }
    }

    /**
     * Method to complete a complete info task of a request of advance of an export collection
     * @param taskId    : a string with the task id (complete info task)
     * @param request   : a request of advance of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     */
    @Transactional
    public void completeCompleteInfoExportCollectionAdvanceRequest(ExportCollectionAdvanceRequest request,
                                                                   String taskId) {
        caseSecurityService.checkEditTask(request.getRequester(), taskId);

        Map<String, Object> taskVariables = taskUtils.setCompleteInfoTaskVariables(taskId, request.getComment(),
                generateRequestVariablesMap(request));

        pagoNxtRequestUtils.uploadDocuments(request, getCaseInstanceByCode(request.getCode()).getId());
        taskUtils.completeTaskCompleteInfo(taskId, taskVariables, request.getRequester().getUser(),
                FieldConstants.VALUE_COMPLETE);
    }

    /**
     * Method to get petition details of a request of advance of an export collection
     * @param requestId: a string with the request id
     * @param userInfo : an UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest
     * @see  com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a CompleteInfoExportCollectionAdvanceRequest object
     */
    @Transactional
    public CompleteInfoExportCollectionAdvanceRequest getPetitionRequestDetails(String requestId, UserInfo userInfo) {
        caseSecurityService.checkRead(userInfo, requestId);
        HistoricCaseInstance caseInstance = getHistoricCaseInstanceByCode(requestId);
        try {
            return mapToDetailedExportCollectionAdvanceRequest(caseInstance.getCaseVariables(), userInfo.getUser()
                    .getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, "errorMappingCompleteInfo", e);
        }
    }

    /**
     * Method to get a request of advance of an export collection by code
     * @param code: a string with the code
     * @param userInfo : an UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return a ExportCollectionAdvanceRequest object
     */
    public ExportCollectionAdvanceRequest getExportCollectionAdvanceRequestByCode(String code, UserInfo userInfo) {
        CaseInstance caseInstance = getCaseInstanceByCode(code);
        caseSecurityService.checkRead(userInfo, caseInstance);
        try {
            return mapToExportCollectionAdvanceRequest(caseInstance.getCaseVariables(), userInfo.getUser()
                    .getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, "getExportCollectionRequestByCode", e);
        }
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
                .operation(DATA_OBJECT_OPERATION_CREATE_ADVANCE_DRAFT);

        for(Map.Entry<String, Object> variable : variables.entrySet()) {
            builder.value(variable.getKey(), variable.getValue());
        }
        return builder.create();
    }

    /**
     * Update the flowable data object to include in the case variables.
     * @param variables : a collection of variables
     */
    private void updateVariableOperation(Map<String, Object> variables) {

        DataObjectModificationBuilder modificationBuilder = dataObjectRuntimeService
                .createDataObjectModificationBuilder().definitionKey(DATA_OBJECT_DEFINITION_KEY)
                .operation(DATA_OBJECT_OPERATION_UPDATE_ADVANCE_DRAFT);

        for(Map.Entry<String, Object> variable : variables.entrySet()) {
            modificationBuilder.value(variable.getKey(), variable.getValue());
        }
        modificationBuilder.modify();
    }

    /**
     * Method to generate a map of request variables
     * @param request: a request of advance of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return a ExportCollectionAdvanceRequest object
     */
    private Map<String, Object> generateRequestVariablesMap(ExportCollectionAdvanceRequest request) {
        HashMap<String, Object> variables = new HashMap<>();
        pagoNxtRequestUtils.mapCommonVariables(variables, request);
        pagoNxtRequestUtils.mapCustomerVariables(variables, request.getCustomer());

        pagoNxtRequestUtils.mapAdvanceRiskLineVariables(variables, request.getRiskLine());
        variables.put(REQUEST_ADVANCE_DUE_DATE, DateUtils.dateToLocalDate(request.getExpiration()));
        variables.put(REQUEST_ADVANCE_CURRENCY, request.getCurrency());
        variables.put(REQUEST_ADVANCE_AMOUNT, request.getAmount());

        try {
            List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList = exchangeInsuranceFlowableMapper
                    .toFlowable(request.getExchangeInsurances());

            variables.put(REQUEST_ADVANCE_EXCHANGE_INSURANCE, caseCommonVariablesHelper
                    .getJsonNode(exchangeInsuranceFlowableList));
        } catch (JsonProcessingException e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, REQUEST_ADVANCE_EXCHANGE_INSURANCE, e);
        }
        variables.put(EXCHANGE_INSURANCE_USE_DATE, request.getExchangeInsuranceUseDate());
        variables.put(EXCHANGE_INSURANCE_AMOUNT_TO_USE, request.getExchangeInsuranceAmountToUse());
        variables.put(EXCHANGE_INSURANCE_BUY_CURRENCY, request.getExchangeInsuranceBuyCurrency());
        variables.put(EXCHANGE_INSURANCE_SELL_CURRENCY, request.getExchangeInsuranceSellCurrency());

        variables.put(EXPORT_COLLECTION, exportCollectionUtils
                .findDataObjectByIdAndModel(request.getExportCollection().getCode(), DATA_OBJECT_MODEL));

        return variables;
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
                .caseDefinitionKey(EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_KEY)
                .variableValueEquals(EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_SEQ, code)
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
                .caseDefinitionKey(EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_KEY)
                .variableValueEquals(EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_SEQ, code)
                .singleResult();
        if (caseInstance == null) {
            throw new ResourceNotFoundException(String.format("No case found with code %s", code), "errorCaseNotFound");
        }
        return caseInstance;
    }

    /**
     * Method to map data
     * @param variables : a collection of variables
     * @param userType  : a string with the user type
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @throws JsonProcessingException : handles Json Processing exceptions
     * @return an ExportCollectionAdvanceRequest object
     */
    private ExportCollectionAdvanceRequest mapToExportCollectionAdvanceRequest(
            Map<String, Object> variables, String userType) throws JsonProcessingException {

        DataObjectInstanceVariableContainer operation = (DataObjectInstanceVariableContainerImpl) variables
                .get(OPERATION);

        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = new ExportCollectionAdvanceRequest();
        exportCollectionAdvanceRequest.setCode((String) variables.get(EXPORT_COLLECTION_ADVANCE_REQUEST_CASE_SEQ));
        // Common request fields
        pagoNxtRequestUtils.setCommonVariablesFromMapVariables(exportCollectionAdvanceRequest, variables, userType);

        //TODO: set slaEnd

        //custom fields
        exportCollectionAdvanceRequest.setExportCollection(exportCollectionMapper
                .mapDataObjectInstanceVariableContainerToExportCollection((DataObjectInstanceVariableContainer)
                        operation.getVariable(EXPORT_COLLECTION)));

        RiskLine riskLine = new RiskLine();
        riskLine.setRiskLineId(operation.getString(REQUEST_ADVANCE_RISK_LINE_ID));
        riskLine.setIban(operation.getString(REQUEST_ADVANCE_RISK_LINE_IBAN));

        riskLine.setAvailableAmount(ParseUtils
                .parseDoubleToString(operation.getDouble(REQUEST_ADVANCE_RISK_LINE_AVAILABLE_AMOUNT)));

        riskLine.setExpires(DateUtils.localDateToDate(operation.getLocalDate(REQUEST_ADVANCE_RISK_LINE_DUE_DATE)));
        riskLine.setCurrency(operation.getString(REQUEST_ADVANCE_RISK_LINE_CURRENCY));
        exportCollectionAdvanceRequest.setRiskLine(riskLine);
        exportCollectionAdvanceRequest.setCurrency(operation.getString(REQUEST_ADVANCE_CURRENCY));
        exportCollectionAdvanceRequest.setAmount(operation.getDouble(REQUEST_ADVANCE_AMOUNT));

        exportCollectionAdvanceRequest
                .setExpiration(DateUtils.localDateToDate(operation.getLocalDate(REQUEST_ADVANCE_DUE_DATE)));

        Customer customer = new Customer();
        customer.setPersonNumber(operation.getString(REQUEST_CUSTOMER_CODE));
        exportCollectionAdvanceRequest.setCustomer(customer);
        exportCollectionAdvanceRequest.setExchangeInsurances(exchangeInsuranceFlowableMapper.toModel(operation));
        exportCollectionAdvanceRequest.setSavedStep((Integer) variables.get(REQUEST_SAVED_STEP));
        exportCollectionAdvanceRequest.setContractReference(operation.getString(ADVANCE_CONTRACT_REFERENCE));
        return exportCollectionAdvanceRequest;
    }

    /**
     * Method to map data
     * @param caseVariables : a collection of case variables
     * @param task          : a TaskRepresentation object
     * @param taskVariables : a collection of task variables
     * @param userType      : a string with the user type
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest
     * @see com.flowable.platform.service.task
     * @return a CompleteInfoExportCollectionAdvanceRequest object
     * @throws JsonProcessingException handles a Json Processing exception
     */
    private CompleteInfoExportCollectionAdvanceRequest mapToCompleteInfoExportCollectionAdvanceRequest(
            Map<String, Object> caseVariables, TaskRepresentation task, Map<String, Object> taskVariables,
            String userType) throws JsonProcessingException {

        CompleteInfoExportCollectionAdvanceRequest completeInfoExportCollectionAdvanceRequest =
                new CompleteInfoExportCollectionAdvanceRequest();

        ExportCollectionAdvanceRequest request = mapToExportCollectionAdvanceRequest(caseVariables, userType);
        request.setComment(null);
        completeInfoExportCollectionAdvanceRequest.setRequest(request);

        pagoNxtRequestUtils.mapCompleteInfoPagoNxtRequest(completeInfoExportCollectionAdvanceRequest,
                caseVariables, task, taskVariables);

        return completeInfoExportCollectionAdvanceRequest;
    }

    /**
     * Method to map data
     * @param caseVariables : a collection of case variables
     * @param userType      : a string with the user type
     * @return a CompleteInfoExportCollectionAdvanceRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest
     * @throws JsonProcessingException handles a Json Processing exception
     */
    private CompleteInfoExportCollectionAdvanceRequest mapToDetailedExportCollectionAdvanceRequest(
            Map<String, Object> caseVariables, String userType) throws JsonProcessingException {

        CompleteInfoExportCollectionAdvanceRequest completeInfoExportCollectionAdvanceRequest =
                new CompleteInfoExportCollectionAdvanceRequest();

        ExportCollectionAdvanceRequest request = mapToExportCollectionAdvanceRequest(caseVariables, userType);
        completeInfoExportCollectionAdvanceRequest.setRequest(request);
        pagoNxtRequestUtils.mapDetailedPagoNxtRequest(completeInfoExportCollectionAdvanceRequest, caseVariables);
        return completeInfoExportCollectionAdvanceRequest;
    }
}
