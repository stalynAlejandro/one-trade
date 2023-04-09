package com.pagonxt.onetradefinance.work.service.trade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.dataobject.api.repository.DataObjectDefinition;
import com.flowable.dataobject.api.repository.DataObjectRepositoryService;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContractsQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.work.common.CaseCommonConstants;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.trade.TradeContractMapper;
import com.pagonxt.onetradefinance.work.service.mapper.trade.TradeRequestMapper;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import com.pagonxt.onetradefinance.work.utils.trade.TradeUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.history.HistoricCaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.TaskService;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.work.utils.trade.TradeUtils.*;

@Service
public class TradeRequestService {
    //Final Variables
    private static final String CREATE_TRADE_REQUEST = "createTradeRequest";
    private static final String UPDATE_TRADE_REQUEST = "updateTradeRequest";
    private static final String CONFIRM_TRADE_REQUEST = "confirmTradeRequest";
    private static final String GET_TRADE_EXTERNAL_TASK = "getTradeExternalTask";
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_START_ITEM = 0;
    private static final String ERROR_MAPPING_REQUEST = "Error mapping request";
    private static final String MINITRADE_CASE_DEFINITION_KEY = "MTR_C001";
    private static final String REQUEST_JSON = "requestJSON";
    private static final String MANDATORY_DATA_CREATE_OPERATION_EXCEPTION_MESSAGE = "Mandatory data for create a " +
            "operation in flow is missing: %s";
    private static final String CASE_RESOURCE_CREATE_OPERATION_EXCEPTION_MESSAGE = "Some case resource for create a " +
            "operation in flow is missing: %s";
    public static final String SEARCH_CONTRACTS_BY_CONTRACT_TYPE = "searchContractsByContractType";

    //Class variables
    private final PagoNxtRequestUtils pagoNxtRequestUtils;
    private final CmmnRuntimeService cmmnRuntimeService;
    private final CmmnHistoryService cmmnHistoryService;
    private final CaseSecurityService caseSecurityService;
    private final TradeUtils tradeUtils;
    private final TaskUtils taskUtils;
    private final ObjectMapper mapper;
    private final TradeContractMapper tradeContractMapper;
    private final TradeRequestMapper tradeRequestMapper;
    private final DataObjectRuntimeService dataObjectRuntimeService;
    private final DataObjectRepositoryService dataObjectRepositoryService;
    private final TaskService taskService;

    /**
     * Constructor method
     *
     * @param tradeUtils                  : a TradeUtils object
     * @param pagoNxtRequestUtils         : a PagoNxtRequestUtils object
     * @param cmmnRuntimeService          : a CmmnRuntimeService object
     * @param cmmnHistoryService          : a CmmnHistoryService object
     * @param caseSecurityService         : a CaseSecurityService object
     * @param taskUtils                   : a TaskUtils object
     * @param mapper                      : a ObjectMapper object
     * @param tradeContractMapper         : a {@link TradeContractMapper} object
     * @param tradeRequestMapper          : a {@link DataObjectRuntimeService}   object
     * @param dataObjectRuntimeService    : an DataObjectRuntimeService object
     * @param dataObjectRepositoryService : an DataObjectRepositoryService object
     * @param taskService                 : an {@link TaskService} object
     */
    public TradeRequestService(TradeUtils tradeUtils,
                               PagoNxtRequestUtils pagoNxtRequestUtils,
                               CmmnRuntimeService cmmnRuntimeService,
                               CmmnHistoryService cmmnHistoryService,
                               CaseSecurityService caseSecurityService,
                               TaskUtils taskUtils,
                               ObjectMapper mapper,
                               TradeContractMapper tradeContractMapper,
                               TradeRequestMapper tradeRequestMapper,
                               DataObjectRuntimeService dataObjectRuntimeService,
                               DataObjectRepositoryService dataObjectRepositoryService, TaskService taskService) {
        this.pagoNxtRequestUtils = pagoNxtRequestUtils;
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.cmmnHistoryService = cmmnHistoryService;
        this.caseSecurityService = caseSecurityService;
        this.tradeUtils = tradeUtils;
        this.taskUtils = taskUtils;
        this.mapper = mapper;
        this.tradeContractMapper = tradeContractMapper;
        this.tradeRequestMapper = tradeRequestMapper;
        this.dataObjectRuntimeService = dataObjectRuntimeService;
        this.dataObjectRepositoryService = dataObjectRepositoryService;
        this.taskService = taskService;
    }

    /**
     * Method to create a trade request
     * @param request : a TradeRequest object with initial data
     * @return a TradeRequest object with more data
     */
    @Transactional
    public TradeRequest createTradeRequest(TradeRequest request)  {
        if (request.getProduct() == null || request.getEvent() == null){
            throw new InvalidRequestException("Request does not contain some mandatory data (product, event)",
                    CREATE_TRADE_REQUEST);
        }

        if (!Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("In a draft creation, the field 'code' cannot be informed.",
                    CREATE_TRADE_REQUEST);
        }

        try {
            CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
                    .caseDefinitionKey(MINITRADE_CASE_DEFINITION_KEY).start();
            String code = (String) caseInstance.getCaseVariables().get(CASE_VAR_OPERATION_CODE);
            request.setCode(code);

            mapValueToJsonNode(caseInstance, request, CREATE_TRADE_REQUEST);

            cmmnRuntimeService.setVariable(caseInstance.getId(), CASE_VAR_REQUEST_SAVED_STEP, request.getSavedStep());
            cmmnRuntimeService.setVariable(caseInstance.getId(), CASE_VAR_COUNTRY, request.getCountry());
            cmmnRuntimeService.setVariable(caseInstance.getId(), CaseCommonConstants.VARNAME_PRODUCT,
                    request.getProduct());
            cmmnRuntimeService.setVariable(caseInstance.getId(), CaseCommonConstants.VARNAME_EVENT, request.getEvent());


            //upload documents
            tradeUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);
            return request;
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String.format(MANDATORY_DATA_CREATE_OPERATION_EXCEPTION_MESSAGE,
                    e.getMessage()), CREATE_TRADE_REQUEST,e);
        } catch (FlowableObjectNotFoundException e){
            throw new ResourceNotFoundException(String.format(CASE_RESOURCE_CREATE_OPERATION_EXCEPTION_MESSAGE,
                    e.getMessage()), CREATE_TRADE_REQUEST,e);
        }


    }

    /**
     * Method to update a trade request
     * @param request : a TradeRequest object with initial data
     * @return a TradeRequest object with more data
     */
    @Transactional
    public TradeRequest updateTradeRequest(TradeRequest request) {
        if (request.getProduct() == null || request.getEvent() == null || request.getCode() == null){
            throw new InvalidRequestException("Request does not contain some mandatory data (product, event, code)",
                    UPDATE_TRADE_REQUEST);
        }

        if (Strings.isBlank(request.getCode())) {
            throw new InvalidRequestException("Mandatory data is missing: code", UPDATE_TRADE_REQUEST);
        }
        try {
            CaseInstance caseInstance = getCaseInstanceByCode(request.getCode());
            caseSecurityService.checkEdit(request.getRequester(), caseInstance);
            if (isOperationConfirmed(caseInstance)) {
                throw new InvalidRequestException("The operation has already been confirmed", UPDATE_TRADE_REQUEST);
            }

            mapValueToJsonNode(caseInstance, request, UPDATE_TRADE_REQUEST);

            cmmnRuntimeService.setVariable(caseInstance.getId(), CASE_VAR_REQUEST_SAVED_STEP, request.getSavedStep());
            //update draft timers
            pagoNxtRequestUtils.updateDraftTimers(caseInstance.getId());
            //upload documents
            tradeUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);

            return request;
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String
                    .format("Mandatory data for update a operation in flow is missing: %s",
                            e.getMessage()), UPDATE_TRADE_REQUEST,e);
        } catch (FlowableObjectNotFoundException e){
            throw new ResourceNotFoundException(String
                    .format("Some case resource for update a operation in flow is missing: %s",
                            e.getMessage()), UPDATE_TRADE_REQUEST,e);
        }
    }

    /**
     * Method to confirm a MiniTrade Request
     * @param code      : a String with the request code
     * @param request   : a {@link AuthenticatedRequest} object with the requester info
     */
    @Transactional
    public void confirmTradeRequest(String code, AuthenticatedRequest request) {
        if (StringUtils.isBlank(code)){
            throw new InvalidRequestException("Request code  is mandatory",
                    CONFIRM_TRADE_REQUEST);
        }

        CaseInstance caseInstance = getCaseInstanceByCode(code);
        caseSecurityService.checkEdit(request.getRequester(), caseInstance);
        if (isOperationConfirmed(caseInstance)){
            throw new InvalidRequestException("The operation has already been confirmed", CONFIRM_TRADE_REQUEST);
        }

        pagoNxtRequestUtils.setOperationDocumentsFromRequestDocuments(caseInstance.getId(), request
                .getRequester().getUser().getUserType());

        taskUtils.completeTaskDraft(caseInstance, request.getRequester().getUser());
        //Delete the draft variable 'requestJSON' because it will no longer be necessary after confirming the draft.
        cmmnRuntimeService.removeVariable(caseInstance.getId(), REQUEST_JSON);
    }

    /**
     * Method to obtain a request through a code
     * @param code : a String object with the code
     * @param user : a UserInfo object
     * @return a TradeRequest object
     */
    public TradeRequest getRequestByCode(String code, UserInfo user) {
        HistoricCaseInstance caseInstance = getHistoricCaseInstanceByCode(code);
        caseSecurityService.checkRead(user, caseInstance.getCaseVariables());
        try {
            return tradeRequestMapper.mapToTradeRequest(caseInstance.getCaseVariables(),
                    user.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, "getRequestByCode", e);
        }
    }

    /**
     * Method to obtain an external task through a task id
     * @param taskId    : a String object with the task id
     * @param userInfo  : a UserInfo object
     * @return : a TradeExternalTaskRequest object
     */
    @Transactional
    public TradeExternalTaskRequest getTradeExternalTaskByTaskId(String taskId, UserInfo userInfo) {
        caseSecurityService.checkReadTask(userInfo, taskId);
        Map<String, Object> taskVariables = tradeUtils.getTradeExternalTaskVariables(taskId);
        try {
            return tradeUtils.getTradeExternalTaskRequestFromMapVariables(taskVariables,
                    userInfo.getUser().getUserType());
        } catch (Exception e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, GET_TRADE_EXTERNAL_TASK, e);
        }
    }

    /**
     * Method to map request data into a JsonNode object
     * @param caseInstance: a CaseInstance object
     * @param request     : a TradeRequest object
     * @param operation   : a String object with the operation (CREATE, UPDATE)
     */
    public void mapValueToJsonNode(CaseInstance caseInstance, TradeRequest request, String operation){
        try {
            JsonNode jsonNode = mapper.valueToTree(request);
            cmmnRuntimeService.setVariable(caseInstance.getId(), REQUEST_JSON, jsonNode);
        } catch (JsonParseException e) {
            throw new MappingException(ERROR_MAPPING_REQUEST, operation, e);
        }
    }

    /**
     * Method to obtain a case instance through a code
     * @param code: a String object with case code
     * @return a CaseInstance object
     */
    private CaseInstance getCaseInstanceByCode(String code) {
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceQuery()
                .includeCaseVariables()
                .variableValueEquals(CASE_VAR_OPERATION_CODE, code)
                .singleResult();
        if (caseInstance == null) {
            throw new ResourceNotFoundException(String.format("No case found with code %s", code), "errorCaseNotFound");
        }
        return caseInstance;
    }

    /**
     * Method to obtain an historic case instance through a code
     * @param code: a String object with case code
     * @return a HistoricCaseInstance object
     */
    private HistoricCaseInstance getHistoricCaseInstanceByCode(String code) {
        HistoricCaseInstance caseInstance = cmmnHistoryService.createHistoricCaseInstanceQuery()
                .includeCaseVariables()
                .variableValueEquals(CASE_VAR_OPERATION_CODE, code)
                .singleResult();
        if (caseInstance == null) {
            throw new ResourceNotFoundException(String.format("No case found with code %s", code), "errorCaseNotFound");
        }
        return caseInstance;
    }

    /**
     * Method to check if an operation has been confirmed
     * @param caseInstance : a CaseInstance object
     * @return true or false
     */
    private boolean isOperationConfirmed(CaseInstance caseInstance){
        return caseInstance.getCaseVariables().get(CaseCommonConstants.VARNAME_REGISTRATION_COMPLETED) != null;
    }

    public List<TradeContract> getContracts(String contractType, TradeContractsQuery tradeContractQuery) {
        //Find Data Object definition by external id
        DataObjectDefinition dataObjectDefinition = dataObjectRepositoryService.createDataObjectDefinitionQuery()
                .latestVersion().externalId(contractType).singleResult();
        if (dataObjectDefinition == null) {
            throw new ResourceNotFoundException(String.format("Some case resource for contracts search is missing:"
                    + " Data object definition with externalId '%s' not found", contractType),
                    SEARCH_CONTRACTS_BY_CONTRACT_TYPE);
        }
        //Security check not needed
        DataObjectInstanceVariableContainerQuery query =  this.dataObjectRuntimeService.createDataObjectInstanceQuery()
                    .definitionKey(dataObjectDefinition.getKey()).operation("searchContracts");

        //filter by customerCode
        if (StringUtils.isNotEmpty(tradeContractQuery.getCustomerCode())) {
            query.value("customerCode", tradeContractQuery.getCustomerCode());
        }
        //filter by status
        if (tradeContractQuery.getStatus() != null) {
            query.value("status", tradeContractQuery.getStatus());
        }
        //filter by contract reference
        if (StringUtils.isNotEmpty(tradeContractQuery.getContractReference())){
            query.value("contractReference",tradeContractQuery.getContractReference());
        }
        //order and sort query
        try {
            if (StringUtils.isNotEmpty(tradeContractQuery.getSort())) {
                query.orderBy(tradeContractQuery::getSort);
                String order = StringUtils.defaultIfBlank(tradeContractQuery.getOrder(), "asc");
                if ("asc".equals(order)) {
                    query.asc();
                } else {
                    if (!"desc".equals(order)) {
                        throw new FlowableIllegalArgumentException("Value for param 'order' is not valid : '"
                                + order + "', must be 'asc' or 'desc'");
                    }
                    query.desc();
                }
            }
            //query pagination
            int start = ObjectUtils.defaultIfNull(tradeContractQuery.getStart(), DEFAULT_START_ITEM);
            int size = ObjectUtils.defaultIfNull(tradeContractQuery.getSize(), DEFAULT_PAGE_SIZE);
            List<DataObjectInstanceVariableContainer> results = query.listPage(start, size);
            return results.stream().map(tradeContractMapper::mapToTradeContract).collect(Collectors.toList());
        } catch (FlowableIllegalArgumentException e) {
            throw new InvalidRequestException(String.format("Invalid query "
                            + "for contracts search: %s"
                    , e.getMessage()), SEARCH_CONTRACTS_BY_CONTRACT_TYPE, e);
        } catch (FlowableObjectNotFoundException e) {
            throw new ResourceNotFoundException(String.format("Some case resource for contracts search is missing: %s",
                    e.getMessage()), SEARCH_CONTRACTS_BY_CONTRACT_TYPE, e);
        }
    }
    /**
     * Method to complete a complete info task of a request of an export collection
     * @param taskId    : a string with the task id (complete info task)
     * @param request   : a request of an export collection
     * @see  com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     */
    @Transactional
    public void externalTaskCompleteInfo(TradeRequest request, String taskId) {
        try {
            caseSecurityService.checkEditTask(request.getRequester(), taskId);
        } catch (FlowableObjectNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage(),"externalTaskCompleteInfo");
        }
        // It's important to set the new variable before retrieving them all
        taskService.setVariable(taskId, TradeUtils.EXTERNAL_TASK_VAR_COMPLETE_INFO_OUTCOME,
                TradeUtils.EXTERNAL_TASK_ACTION_CONTINUE);
        taskService.setVariable(taskId,TradeUtils.EXTERNAL_TASK_VAR_COMPLETE_INFO_COMMENT,
                request.getComment());
        //Get the complete task variables map
        Map<String, Object> taskVariables = taskUtils.getTaskVariablesCompleteInfo(taskId);
        String caseOperationCode = (String) ((Map<String, Object>)taskVariables.get(TaskUtils.SCOPE_ROOT))
                .get(CASE_VAR_OPERATION_CODE);
        if (!request.getCode().equals(caseOperationCode)){
            throw new InvalidRequestException(String.
                    format("Codes do not correspond: Case code for task '%s' is '%s', but code in request is '%s'",
                            taskId, caseOperationCode, request.getCode()), "externalTaskCompleteInfo");
        }
        // Update only priority and details, except contract reference
        Map<String, Object> operation =
                (Map<String, Object>) ((Map<String, Object>) taskVariables.get(TaskUtils.SCOPE_ROOT))
                        .get(CASE_VAR_OPERATION);
        operation.put(OPERATION_VAR_PRIORITY,request.getPriority());
        for(Map.Entry<String, Object> variable : request.getDetails().entrySet()) {
            if (!variable.getKey().equals(OPERATION_VAR_CONTRACT_REFERENCE)) {
                operation.put(variable.getKey(), variable.getValue());
            }
        }

        //Add only new documents
        List<Document> documentsToAdd = request.getDocuments().stream()
                .filter(doc -> Strings.isBlank(doc.getDocumentId())).collect(Collectors.toList());
        tradeUtils.addDocumentsToCase(request.getRequester(),
                documentsToAdd, getCaseInstanceByCode(request.getCode()).getId());
        //In MiniTrade, the form outcome is obtained from another variable in the form, so you do not have to indicate
        taskUtils.completeTaskCompleteInfo(taskId, taskVariables, request.getRequester().getUser(), null);
    }
    /**
     * Method to cancel a complete info tasks
     * @param taskId    : a string with the task id
     * @param userInfo  : an AuthenticatedRequest object with user info
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     */
    @Transactional
    public void externalTaskRequestCancellation(String taskId, AuthenticatedRequest userInfo) {
        try {
            caseSecurityService.checkEditTask(userInfo.getRequester(), taskId);
        } catch (FlowableObjectNotFoundException e){
            throw new ResourceNotFoundException(e.getMessage(),"externalTaskRequestCancellation");
        }
        // It's important to set the new variable before retrieving them all
        taskService.setVariable(taskId, TradeUtils.EXTERNAL_TASK_VAR_COMPLETE_INFO_OUTCOME,
                TradeUtils.EXTERNAL_TASK_ACTION_REQUEST_FOR_CANCELATION);
        taskService.setVariable(taskId,EXTERNAL_TASK_VAR_COMPLETE_INFO_COMMENT, null);
        Map<String, Object> taskVariables = taskUtils.getTaskVariablesCompleteInfo(taskId);
        //In MiniTrade, the form outcome is obtained from another variable in the form, so you do not have to indicate
        taskUtils.completeTaskCompleteInfo(taskId, taskVariables, userInfo.getRequester().getUser(), null);
    }

}
