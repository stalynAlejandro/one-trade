package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectModificationBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.work.common.FilterConstants;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.utils.DateUtils;
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

/**
 * service class for a request of an export collection
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.cmmn.api.CmmnRuntimeService
 * @see com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils
 * @see com.flowable.dataobject.api.runtime.DataObjectRuntimeService
 * @see com.pagonxt.onetradefinance.work.utils.TaskUtils
 * @see com.pagonxt.onetradefinance.work.validation.ExportCollectionValidation
 * @see com.pagonxt.onetradefinance.work.security.CaseSecurityService
 * @see org.flowable.cmmn.api.CmmnHistoryService
 * @since jdk-11.0.13
 */
@Service
public class ExportCollectionRequestService {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionRequestService.class);

    private static final String CREATE_EXPORT_COLLECTION_REQUEST = "createExportCollectionRequest";
    private static final String UPDATE_EXPORT_COLLECTION_REQUEST = "updateExportCollectionRequest";
    private static final String CONFIRM_EXPORT_COLLECTION_REQUEST = "confirmExportCollectionRequest";
    public static final String GET_EXPORT_COLLECTION_REQUEST_BY_CODE = "getExportCollectionRequestByCode";
    public static final String DATA_OBJECT_DEFINITION_KEY = "CLE_DO001";
    public static final String DATA_OBJECT_OPERATION_CREATE_DRAFT = "createDraft";
    public static final String DATA_OBJECT_OPERATION_UPDATE_DRAFT = "updateDraft";
    private static final String EXPORT_COLLECTION_REQUEST_CASE_KEY = "CLE_C001";
    public static final String ERROR_MAPPING_REQUEST = "Error mapping request";

    //class attributes
    private final CmmnRuntimeService cmmnRuntimeService;
    private final PagoNxtRequestUtils pagoNxtRequestUtils;
    private final CaseSecurityService caseSecurityService;
    private final TaskUtils taskUtils;
    private final DataObjectRuntimeService dataObjectRuntimeService;
    private final ExportCollectionValidation exportCollectionValidation;
    private final CmmnHistoryService cmmnHistoryService;

    /**
     * constructor method
     * @param cmmnRuntimeService                : a CmmnRuntimeService object
     * @param dataObjectRuntimeService          : a DataObjectRuntimeService object
     * @param pagoNxtRequestUtils               : a PagoNxtRequestUtils object
     * @param taskUtils                         : a TaskUtils object
     * @param exportCollectionValidation        : a ExportCollectionValidation object
     * @param caseSecurityService               : a CaseSecurityService object
     * @param cmmnHistoryService                : a CmmnHistoryService object
     */
    public ExportCollectionRequestService(CmmnRuntimeService cmmnRuntimeService,
                                          DataObjectRuntimeService dataObjectRuntimeService,
                                          PagoNxtRequestUtils pagoNxtRequestUtils,
                                          TaskUtils taskUtils,
                                          CaseSecurityService caseSecurityService,
                                          CmmnHistoryService cmmnHistoryService,
                                          ExportCollectionValidation exportCollectionValidation) {
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.dataObjectRuntimeService = dataObjectRuntimeService;
        this.pagoNxtRequestUtils = pagoNxtRequestUtils;
        this.caseSecurityService = caseSecurityService;
        this.taskUtils = taskUtils;
        this.exportCollectionValidation = exportCollectionValidation;
        this.cmmnHistoryService = cmmnHistoryService;
    }

    /**
     * Method to create a request of an export collection
     * @param request : a request of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return a ExportCollectionRequest object
     */
    @Transactional
    public ExportCollectionRequest createExportCollectionRequest(ExportCollectionRequest request) {
        exportCollectionValidation.validateDraft(request);
        if (!Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("In a draft creation, the field 'code' cannot be informed.",
                    CREATE_EXPORT_COLLECTION_REQUEST);
        }
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                .caseDefinitionKey(EXPORT_COLLECTION_REQUEST_CASE_KEY).start();

        String code = (String) caseInstance.getCaseVariables().get(EXPORT_COLLECTION_REQUEST_CASE_SEQ);
        Map<String, Object> variables = generateRequestVariablesMap(request);
        variables.put(REQUEST_CODE, code);
        try {
            DataObjectInstanceVariableContainer operation = generateVariableOperation(variables);
            cmmnRuntimeService.setVariable(caseInstance.getId(), OPERATION, operation);
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Mandatory data for create a operation in flow" +
                    " with code %s is missing", code), CREATE_EXPORT_COLLECTION_REQUEST,e);
        }
        cmmnRuntimeService.setVariable(caseInstance.getId(), REQUEST_SAVED_STEP, request.getSavedStep());
        cmmnRuntimeService.setVariable(caseInstance.getId(),COUNTRY,request.getCountry());
        request.setCode(code);
        request.setProduct((String) caseInstance.getCaseVariables().get(PRODUCT));
        request.setEvent((String) caseInstance.getCaseVariables().get(EVENT));
        //upload documents
        pagoNxtRequestUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);
        LOG.debug("createExportCollectionRequest(request: {}) created instance {}", request, caseInstance);
        return request;
    }

    /**
     * Method to update a request of an export collection
     * @param request : a request of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return a ExportCollectionRequest object
     */
    @Transactional
    public ExportCollectionRequest updateExportCollectionRequest(ExportCollectionRequest request) {
        // TODO: Comprobar que no se está actualizando el requester, la oficina...
        LOG.debug("updateExportCollectionRequest(request: {})", request);
        exportCollectionValidation.validateDraft(request);
        if (Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("Mandatory data is missing: code", UPDATE_EXPORT_COLLECTION_REQUEST);
        }
        CaseInstance caseInstance = getCaseInstanceByCode(request.getCode());
        caseSecurityService.checkEdit(request.getRequester(), caseInstance);
        Map<String, Object> variables = generateRequestVariablesMap(request);
        variables.put(REQUEST_CODE, request.getCode());
        try {
            updateVariableOperation(variables);
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Mandatory data for update a operation in flow with" +
                    " code %s is missing", request.getCode()), UPDATE_EXPORT_COLLECTION_REQUEST,e);
        }
        cmmnRuntimeService.setVariable(caseInstance.getId(), REQUEST_SAVED_STEP, request.getSavedStep());
        //update draft timers
        pagoNxtRequestUtils.updateDraftTimers(caseInstance.getId());
        pagoNxtRequestUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);
        return request;
    }

    /**
     * Method to confirm a request of an export collection
     * @param request : a request of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return a ExportCollectionRequest object
     */
    @Transactional
    public ExportCollectionRequest confirmExportCollectionRequest(ExportCollectionRequest request) {
        LOG.debug("confirmExportCollectionRequest(request: {})", request);
        //TODO: ¡¡¡Enviar el SLA definitivo!!!
        // TODO COMPROBAR QUE EL BORRADOR NO HA SIDO CONFIRMADO, ANTES DE VOLVER A CONFIRMARLO
        exportCollectionValidation.validateConfirm(request);
        if (Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("Mandatory data is missing: code", CONFIRM_EXPORT_COLLECTION_REQUEST);
        }
        CaseInstance caseInstance = getCaseInstanceByCode(request.getCode());
        caseSecurityService.checkEdit(request.getRequester(), caseInstance);

        pagoNxtRequestUtils.setOperationDocumentsFromRequestDocuments(caseInstance.getId(), request
                .getRequester().getUser().getUserType());

        taskUtils.completeTaskDraft(caseInstance, request.getRequester().getUser());
        return request;
    }

    /**
     * Method to get a complete info task of a request of an export collection
     * @param taskId    : a string with the task id (complete info task)
     * @param userInfo  : a UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a CompleteInfoExportCollectionRequest object
     */
    @Transactional
    public CompleteInfoExportCollectionRequest getCompleteInfoExportCollectionRequest(String taskId,
                                                                                      UserInfo userInfo) {
        caseSecurityService.checkReadTask(userInfo, taskId);
        Map<String, Object> taskVariables = taskUtils.getTaskVariablesCompleteInfo(taskId);
        TaskRepresentation task = taskUtils.getTask(taskId, false);
        CaseInstance caseInstance = getCaseInstanceByCode((String) taskVariables.get(FilterConstants.OPERATION_CODE));
        try {
            return mapToCompleteInfoExportCollectionRequest(caseInstance.getCaseVariables(), task, taskVariables,
                    userInfo.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, "errorMappingCompleteInfo", e);
        }
    }

    /**
     * Method to complete a complete info task of a request of an export collection
     * @param taskId    : a string with the task id (complete info task)
     * @param request   : a request of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     */
    @Transactional
    public void completeCompleteInfoExportCollectionRequest(ExportCollectionRequest request, String taskId) {
        caseSecurityService.checkEditTask(request.getRequester(), taskId);

        Map<String, Object> taskVariables = taskUtils.setCompleteInfoTaskVariables(taskId, request.getComment(),
                generateRequestVariablesMap(request));

        pagoNxtRequestUtils.uploadDocuments(request, getCaseInstanceByCode(request.getCode()).getId());
        taskUtils.completeTaskCompleteInfo(taskId, taskVariables, request.getRequester().getUser(),
                FieldConstants.VALUE_COMPLETE);
    }

    /**
     * Method to get petition details of a request of an export collection
     * @param requestId: a string with the request id
     * @param userInfo : an UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest
     * @see  com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a CompleteInfoExportCollectionRequest object
     */
    @Transactional
    public CompleteInfoExportCollectionRequest getPetitionRequestDetails(String requestId, UserInfo userInfo) {
        caseSecurityService.checkRead(userInfo, requestId);
        HistoricCaseInstance caseInstance = getHistoricCaseInstanceByCode(requestId);
        try {
            return mapToDetailedExportCollectionRequest(caseInstance.getCaseVariables(),
                    userInfo.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, "errorMappingCompleteInfo", e);
        }
    }

    /**
     * Method to get a request of advance of an export collection by code
     * @param code: a string with the code
     * @param user : an UserInfo object
     * @see  com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return a ExportCollectionRequest object
     */
    public ExportCollectionRequest getExportCollectionRequestByCode(String code, UserInfo user) {
        CaseInstance caseInstance = getCaseInstanceByCode(code);
        caseSecurityService.checkRead(user, caseInstance);
        try {
            return mapToExportCollectionRequest(caseInstance.getCaseVariables(), user.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, GET_EXPORT_COLLECTION_REQUEST_BY_CODE, e);
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
                .operation(DATA_OBJECT_OPERATION_CREATE_DRAFT);

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
                .operation(DATA_OBJECT_OPERATION_UPDATE_DRAFT);

        for(Map.Entry<String, Object> variable : variables.entrySet()) {
            modificationBuilder.value(variable.getKey(), variable.getValue());
        }
        modificationBuilder.modify();
    }

    /**
     * Method to generate a map of request variables
     * @param request: a request of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return a ExportCollectionRequest object
     */
    private Map<String, Object> generateRequestVariablesMap(ExportCollectionRequest request) {
        HashMap<String, Object> variables = new HashMap<>();
        pagoNxtRequestUtils.mapCommonVariables(variables, request);
        pagoNxtRequestUtils.mapCustomerVariables(variables, request.getCustomer());

        variables.put(REQUEST_CURRENCY, request.getCurrency());
        variables.put(REQUEST_AMOUNT, request.getAmount());
        variables.put(REQUEST_CUSTOMER_REFERENCE, request.getClientReference());
        variables.put(REQUEST_DEBTOR_NAME, request.getDebtorName());
        variables.put(REQUEST_DEBTOR_BANK, request.getDebtorBank());
        variables.put(REQUEST_COLLECTION_TYPE, (request.getCollectionType()));

        variables.put(REQUEST_CUSTOMER_HAS_ACCOUNT, ParseUtils.parseBooleanToYesNo(request.isCustomerHasAccount()));
        variables.put(REQUEST_NOMINAL_ACCOUNT_ID, request.getNominalAccount().getAccountId());
        variables.put(REQUEST_NOMINAL_ACCOUNT_IBAN, request.getNominalAccount().getIban());
        variables.put(REQUEST_NOMINAL_ACCOUNT_CURRENCY, request.getNominalAccount().getCurrency());
        variables.put(REQUEST_COMMISSION_ACCOUNT_ID, request.getCommissionAccount().getAccountId());
        variables.put(REQUEST_COMMISSION_ACCOUNT_IBAN, request.getCommissionAccount().getIban());
        variables.put(REQUEST_COMMISSION_ACCOUNT_CURRENCY, request.getCommissionAccount().getCurrency());

        variables.put(REQUEST_APPLYING_FOR_ADVANCE, ParseUtils.parseBooleanToYesNo(request.isApplyingForAdvance()));
        pagoNxtRequestUtils.mapAdvanceRiskLineVariables(variables, request.getAdvanceRiskLine());
        variables.put(REQUEST_ADVANCE_DUE_DATE, DateUtils.dateToLocalDate(request.getAdvanceExpiration()));
        variables.put(REQUEST_ADVANCE_CURRENCY, request.getAdvanceCurrency());
        variables.put(REQUEST_ADVANCE_AMOUNT, request.getAdvanceAmount());

        variables.put(REQUEST_CUSTOMER_PRICES_AGREEMENT_COLLECTED, ParseUtils
                .parseBooleanToYesNo(request.isClientAcceptance()));

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
                .caseDefinitionKey(EXPORT_COLLECTION_REQUEST_CASE_KEY)
                .variableValueEquals(EXPORT_COLLECTION_REQUEST_CASE_SEQ, code)
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
                .caseDefinitionKey(EXPORT_COLLECTION_REQUEST_CASE_KEY)
                .variableValueEquals(EXPORT_COLLECTION_REQUEST_CASE_SEQ, code)
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
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @throws JsonProcessingException : handles Json Processing exceptions
     * @return an ExportCollectionRequest object
     */
  private ExportCollectionRequest mapToExportCollectionRequest(Map<String, Object> variables,
                                                               String userType) throws JsonProcessingException {

        DataObjectInstanceVariableContainer operation = (DataObjectInstanceVariableContainerImpl) variables
                .get(OPERATION);

        ExportCollectionRequest exportCollectionRequest = new ExportCollectionRequest();
        exportCollectionRequest.setCode((String) variables.get(EXPORT_COLLECTION_REQUEST_CASE_SEQ));
        // Common request fields
        pagoNxtRequestUtils.setCommonVariablesFromMapVariables(exportCollectionRequest, variables, userType);

        //TODO: set slaEnd

        // Custom
        exportCollectionRequest.setCurrency(operation.getString(REQUEST_CURRENCY));
        exportCollectionRequest.setAmount(operation.getDouble(REQUEST_AMOUNT));

        exportCollectionRequest.setCustomerHasAccount(ParseUtils
                .parseYesNoToBoolean(operation.getString(REQUEST_CUSTOMER_HAS_ACCOUNT)));

        Account nominalAccount = new Account();
        nominalAccount.setAccountId(operation.getString(REQUEST_NOMINAL_ACCOUNT_ID));
        exportCollectionRequest.setNominalAccount(nominalAccount);
        Account commissionAccount = new Account();
        commissionAccount.setAccountId(operation.getString(REQUEST_COMMISSION_ACCOUNT_ID));
        exportCollectionRequest.setCommissionAccount(commissionAccount);

        exportCollectionRequest.setApplyingForAdvance(ParseUtils
                .parseYesNoToBoolean(operation.getString(REQUEST_APPLYING_FOR_ADVANCE)));

        RiskLine riskLine = new RiskLine();
        riskLine.setRiskLineId(operation.getString(REQUEST_ADVANCE_RISK_LINE_ID));
        riskLine.setIban(operation.getString(REQUEST_ADVANCE_RISK_LINE_IBAN));

        riskLine.setAvailableAmount(ParseUtils.parseDoubleToString(operation
                .getDouble(REQUEST_ADVANCE_RISK_LINE_AVAILABLE_AMOUNT)));

        riskLine.setExpires(DateUtils.localDateToDate(operation.getLocalDate(REQUEST_ADVANCE_RISK_LINE_DUE_DATE)));
        riskLine.setCurrency(operation.getString(REQUEST_ADVANCE_RISK_LINE_CURRENCY));
        exportCollectionRequest.setAdvanceRiskLine(riskLine);
        exportCollectionRequest.setAdvanceCurrency(operation.getString(REQUEST_ADVANCE_CURRENCY));
        exportCollectionRequest.setAdvanceAmount(operation.getDouble(REQUEST_ADVANCE_AMOUNT));

        exportCollectionRequest.setClientAcceptance(ParseUtils
                .parseYesNoToBoolean(operation.getString(REQUEST_CUSTOMER_PRICES_AGREEMENT_COLLECTED)));

        exportCollectionRequest.setAdvanceExpiration(DateUtils
                .localDateToDate(operation.getLocalDate(REQUEST_ADVANCE_DUE_DATE)));

        exportCollectionRequest.setClientReference(operation.getString(REQUEST_CUSTOMER_REFERENCE));
        exportCollectionRequest.setDebtorName(operation.getString(REQUEST_DEBTOR_NAME));
        exportCollectionRequest.setDebtorBank(operation.getString(REQUEST_DEBTOR_BANK));
        exportCollectionRequest.setCollectionType(operation.getString(REQUEST_COLLECTION_TYPE));
        Customer customer = new Customer();
        customer.setPersonNumber(operation.getString(REQUEST_CUSTOMER_CODE));
        customer.setName(operation.getString(REQUEST_CUSTOMER_FULL_NAME));
        exportCollectionRequest.setCustomer(customer);
        exportCollectionRequest.setSavedStep((Integer) variables.get(REQUEST_SAVED_STEP));
        exportCollectionRequest.setContractReference(operation.getString(CONTRACT_REFERENCE));
        exportCollectionRequest.setDisplayedStatus(operation.getString(DISPLAYED_STATUS));
        return exportCollectionRequest;
    }

    /**
     * Method to map data
     * @param caseVariables : a collection of case variables
     * @param task          : a TaskRepresentation object
     * @param taskVariables : a collection of task variables
     * @param userType      : a string with the user type
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest
     * @see com.flowable.platform.service.task
     * @return a CompleteInfoExportCollectionRequest object
     * @throws JsonProcessingException handles a Json Processing exception
     */
    private CompleteInfoExportCollectionRequest mapToCompleteInfoExportCollectionRequest(
            Map<String, Object> caseVariables, TaskRepresentation task, Map<String, Object> taskVariables,
            String userType) throws JsonProcessingException {

        CompleteInfoExportCollectionRequest completeInfoExportCollectionRequest =
                new CompleteInfoExportCollectionRequest();

        ExportCollectionRequest request = mapToExportCollectionRequest(caseVariables, userType);
        request.setComment(null);
        completeInfoExportCollectionRequest.setRequest(request);

        pagoNxtRequestUtils.mapCompleteInfoPagoNxtRequest(completeInfoExportCollectionRequest,
                caseVariables, task, taskVariables);

        return completeInfoExportCollectionRequest;
    }

    /**
     * Method to map data
     * @param caseVariables : a collection of case variables
     * @param userType      : a string with the user type
     * @return a CompleteInfoExportCollectionRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest
     * @throws JsonProcessingException handles a Json Processing exception
     */
    private CompleteInfoExportCollectionRequest mapToDetailedExportCollectionRequest(
            Map<String, Object> caseVariables, String userType) throws JsonProcessingException {

        CompleteInfoExportCollectionRequest completeInfoExportCollectionRequest =
                new CompleteInfoExportCollectionRequest();

        ExportCollectionRequest request = mapToExportCollectionRequest(caseVariables, userType);
        completeInfoExportCollectionRequest.setRequest(request);
        pagoNxtRequestUtils.mapDetailedPagoNxtRequest(completeInfoExportCollectionRequest, caseVariables);
        return completeInfoExportCollectionRequest;
    }
}
