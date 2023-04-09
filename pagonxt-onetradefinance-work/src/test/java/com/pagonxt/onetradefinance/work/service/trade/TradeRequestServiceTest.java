package com.pagonxt.onetradefinance.work.service.trade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.dataobject.api.repository.DataObjectRepositoryService;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContractsQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.trade.TradeContractMapper;
import com.pagonxt.onetradefinance.work.service.mapper.trade.TradeRequestMapper;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import com.pagonxt.onetradefinance.work.utils.trade.TradeUtils;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.history.HistoricCaseInstance;
import org.flowable.cmmn.api.history.HistoricCaseInstanceQuery;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstanceBuilder;
import org.flowable.cmmn.api.runtime.CaseInstanceQuery;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.api.FlowableObjectNotFoundException;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.json.JsonParseException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@UnitTest
class TradeRequestServiceTest {

    @InjectMocks
    TradeRequestService tradeRequestService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnRuntimeService cmmnRuntimeService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnHistoryService cmmnHistoryService;
    @Mock
    PagoNxtRequestUtils pagoNxtRequestUtils;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TradeUtils tradeUtils;
    @Mock
    TaskUtils taskUtils;
    @Mock
    CaseSecurityService caseSecurityService;
    @Mock
    ObjectMapper mapper;
    @Mock
    TradeContractMapper tradeContractMapper;
    @Mock
    TaskService taskService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DataObjectRepositoryService dataObjectRepositoryService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DataObjectRuntimeService dataObjectRuntimeService;
    @Mock
    TradeRequestMapper tradeRequestMapper;

    @Test
    void createTradeRequest_productIsNull_throwsInvalidRequestException() {

        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);

        when(tradeRequest.getProduct()).thenReturn(null);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.createTradeRequest(tradeRequest),
                "Invalid exception message when product is null");

        // Then
        assertEquals("Request does not contain some mandatory data (product, event)", exception.getMessage(), "Invalid exception message when product is null");
    }

    @Test
    void createTradeRequest_EventIsNull_throwsInvalidRequestException() {

        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn(null);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.createTradeRequest(tradeRequest),
                "Invalid exception message when event is null");

        // Then
        assertEquals("Request does not contain some mandatory data (product, event)", exception.getMessage(), "Invalid exception message when event is null");
    }

    @Test
    void createTradeRequest_caseCodeExists_throwsInvalidRequestException() {
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        doReturn("XX").when(tradeRequest).getCode();
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.createTradeRequest(tradeRequest),
                "Invalid exception message when code is blank");

        // Then
        assertEquals("In a draft creation, the field 'code' cannot be informed.", exception.getMessage(), "Invalid exception message when code is blank");
    }

    @Test
    void createTradeRequest_mandatoryDataIsMissing_throwsInvalidRequestException() {
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequestService.createTradeRequest(tradeRequest)).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.createTradeRequest(tradeRequest),
                "Invalid exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for create a operation in flow is missing: null", exception.getMessage(), "Invalid exception message when request has missing values");

    }

    @Test
    void createTradeRequest_caseResourceIsMissing_throwsInvalidRequestException() {
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequestService.createTradeRequest(tradeRequest)).thenThrow(FlowableObjectNotFoundException.class);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> tradeRequestService.createTradeRequest(tradeRequest),
                "Invalid exception message when request has missing values");

        // Then
        assertEquals("Some case resource for create a operation in flow is missing: null", exception.getMessage(), "Invalid exception message when request has missing values");

    }

    @Test
    void createTradeRequest_isOk_returnsTradeRequest(){
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        CaseInstanceBuilder caseInstanceBuilder = mock(CaseInstanceBuilder.class);
        doReturn(caseInstanceBuilder).when(cmmnRuntimeService).createCaseInstanceBuilder();
        doReturn(caseInstanceBuilder).when(caseInstanceBuilder).caseDefinitionKey(any());
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstance).when(caseInstanceBuilder).start();

        TradeRequest result = tradeRequestService.createTradeRequest(tradeRequest);

        verify(tradeUtils).updateDraftDocumentsOnCaseInstance(tradeRequest, caseInstance);
        assertNotNull(result);
    }

    @Test
    void createTradeRequest_errorMappingRequest_throwsServiceException() {
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(mapper.valueToTree(tradeRequest)).thenThrow(JsonParseException.class);
        // When
        Exception exception = assertThrows(ServiceException.class,
                () -> tradeRequestService.createTradeRequest(tradeRequest),
                "Invalid exception message mapping request to JsonNode");

        // Then
        assertEquals("Error mapping request", exception.getMessage(), "Invalid exception message mapping request to JsonNode");

    }

    @Test
    void updateTradeRequest_productIsNull_throwsInvalidRequestException() {

        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);

        when(tradeRequest.getProduct()).thenReturn(null);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Invalid exception message when product is null");

        // Then
        assertEquals("Request does not contain some mandatory data (product, event, code)", exception.getMessage(), "Invalid exception message when product is null");
    }

    @Test
    void updateTradeRequest_eventIsNull_throwsInvalidRequestException() {

        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);

        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn(null);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Invalid exception message when event is null");

        // Then
        assertEquals("Request does not contain some mandatory data (product, event, code)", exception.getMessage(), "Invalid exception message when event is null");
    }

    @Test
    void updateTradeRequest_codeIsNull_throwsInvalidRequestException() {

        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);

        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequest.getCode()).thenReturn(null);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Invalid exception message when code is null");

        // Then
        assertEquals("Request does not contain some mandatory data (product, event, code)", exception.getMessage(), "Invalid exception message when code is null");
    }

    @Test
    void updateTradeRequest_codeIsBlank_throwsInvalidRequestException() {

        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);

        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequest.getCode()).thenReturn("");

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Invalid exception message when code is blank");

        // Then
        assertEquals("Mandatory data is missing: code", exception.getMessage(), "Invalid exception message when code is blank");
    }

    @Test
    void updateTradeRequest_isOk_returnsTradeRequest(){
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequest.getCode()).thenReturn("xx");
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();

        TradeRequest result = tradeRequestService.updateTradeRequest(tradeRequest);

        verify(caseSecurityService).checkEdit(tradeRequest.getRequester(),caseInstance);
        verify(tradeUtils).updateDraftDocumentsOnCaseInstance(tradeRequest, caseInstance);
        assertNotNull(result);
    }

    @Test
    void updateTradeRequest_errorMappingRequest_throwsServiceException() {

        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);

        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequest.getCode()).thenReturn("xx");
        when(mapper.valueToTree(tradeRequest)).thenThrow(JsonParseException.class);

        Exception exception = assertThrows(ServiceException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Invalid exception message mapping request to JsonNode");

        // Then
        assertEquals("Error mapping request", exception.getMessage(), "Invalid exception message mapping request to JsonNode");
    }

    @Test
    void updateTradeRequest_mandatoryDataIsMissing_throwsInvalidRequestException() {
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequest.getCode()).thenReturn("xx");
        when(tradeRequestService.updateTradeRequest(tradeRequest)).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Invalid exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for update a operation in flow is missing: null", exception.getMessage(), "Invalid exception message when request has missing values");

    }

    @Test
    void updateTradeRequest_caseResourceIsMissing_throwsInvalidRequestException() {
        // Given
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequest.getCode()).thenReturn("xx");
        when(tradeRequestService.updateTradeRequest(tradeRequest)).thenThrow(FlowableObjectNotFoundException.class);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Invalid exception message when request has missing values");

        // Then
        assertEquals("Some case resource for update a operation in flow is missing: null", exception.getMessage(), "Invalid exception message when request has missing values");

    }

    @Test
    void confirmTradeRequest_codeIsNull_throwsInvalidRequestException() {

        // Given
        AuthenticatedRequest request = mock(AuthenticatedRequest.class);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.confirmTradeRequest(null, request),
                "Invalid exception message when code is null");

        // Then
        assertEquals("Request code  is mandatory", exception.getMessage(), "Invalid exception message when product is null");
    }

    @Test
    void confirmTradeRequest_isOk_completeTaskDraft(){
        // Given
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        AuthenticatedRequest request = new AuthenticatedRequest(userInfo);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();

        tradeRequestService.confirmTradeRequest("xx", request);

        verify(caseSecurityService).checkEdit(request.getRequester(),caseInstance);
        verify(pagoNxtRequestUtils).setOperationDocumentsFromRequestDocuments(caseInstance.getId(), "OFFICE");
        verify(taskUtils).completeTaskDraft(caseInstance, request.getRequester().getUser());
        verify(cmmnRuntimeService).removeVariable(caseInstance.getId(),"requestJSON");
    }

    @Test
    void getRequestByCode_errorMappingRequest(){
        // When
        Exception thrown = assertThrows(MappingException.class,
                () -> tradeRequestService.getRequestByCode("code", null),
                "Should throw MappingException");

        // Then
        assertEquals("Error mapping request", thrown.getMessage(), "Exception should contain valid message");
    }

    @Test
    void getRequestByCode_returnsValidData(){
        TradeRequestService tradeRequestServiceMocked = mock(TradeRequestService.class, Answers.RETURNS_DEEP_STUBS);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);

        TradeRequest result = tradeRequestServiceMocked.getRequestByCode("code", userInfo);

        assertNotNull(result);
    }

    @Test
    void getHistoricCaseInstanceByCode_errorMappingRequest() {
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnHistoryService).createHistoricCaseInstanceQuery();
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();

        // When
        Exception thrown = assertThrows(MappingException.class,
                () -> tradeRequestService.getRequestByCode("code", null),
                "Should throw MappingException");

        // Then
        assertEquals("Error mapping request", thrown.getMessage(), "Exception should contain valid message");
    }

    @Test
    void getHistoricCaseInstanceByCode_returnsValidData() throws JsonProcessingException {
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnHistoryService).createHistoricCaseInstanceQuery();
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        when(tradeRequestMapper.mapToTradeRequest(any(), eq("OFFICE"))).thenReturn(new TradeRequest());

        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        TradeRequest result = tradeRequestService.getRequestByCode("code", userInfo);
        assertNotNull(result);
    }

    @Test
    void getHistoricCaseInstanceByCode_returnNull(){
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnHistoryService).createHistoricCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(null).when(caseInstanceQuery).singleResult();
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        Exception thrown = assertThrows(ResourceNotFoundException.class,
                () -> tradeRequestService.getRequestByCode("code", userInfo),
                "Exception should contain valid message");
        // Then
        assertEquals("No case found with code code", thrown.getMessage(), "Exception should contain valid message");
    }

    @Test
    void getTradeExternalTaskById_errorMappingRequest(){

        // When
        Exception thrown = assertThrows(MappingException.class,
                () -> tradeRequestService.getTradeExternalTaskByTaskId("code", null),
                "Should throw MappingException");

        // Then
        assertEquals("Error mapping request", thrown.getMessage(), "Exception should contain valid message");
    }

    @Test
    void getTradeExternalTaskById_returnsValidData(){
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);

        TradeExternalTaskRequest result = tradeRequestService.getTradeExternalTaskByTaskId("code", userInfo);

        assertNotNull(result);
    }

    @Test
    void updateTradeRequest_RequestHasBeenValidated_returnsInvalidException(){
        // Given
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("registrationCompleted","true");
        TradeRequest tradeRequest = mock(TradeRequest.class);
        when(tradeRequest.getProduct()).thenReturn("xx");
        when(tradeRequest.getEvent()).thenReturn("xx");
        when(tradeRequest.getCode()).thenReturn("xx");
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);

        // When
        Exception thrown = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.updateTradeRequest(tradeRequest),
                "Exception should contain valid message");

        // Then
        assertEquals("The operation has already been confirmed", thrown.getMessage(), "Exception should contain valid message");
    }

    @Test
    void confirmTradeRequest_RequestHasBeenValidated_returnsInvalidException(){
        // Given
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("registrationCompleted","true");

        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        AuthenticatedRequest request = new AuthenticatedRequest(userInfo);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);

        // When
        Exception thrown = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.confirmTradeRequest("myCode",request),
                "Exception should contain valid message");

        // Then
        assertEquals("The operation has already been confirmed", thrown.getMessage(), "Exception should contain valid message");
    }
    @Test
    void getContracts_contractTypeNotFound_returnsResourceNotFoundException(){
        //Given
        when(dataObjectRepositoryService.createDataObjectDefinitionQuery()
                .latestVersion().externalId(any()).singleResult()).thenReturn(null);
        String expectedMessage = String.format("Some case resource for contracts search is missing:"
                + " Data object definition with externalId '%s' not found", "contractType");
        //When
        Exception thrown = assertThrows(ResourceNotFoundException.class,
                () -> tradeRequestService.getContracts("contractType", null ),
                "Exception should contain valid message");

        // Then
        assertEquals(expectedMessage, thrown.getMessage(), "Exception should contain valid message");
    }
    @Test
    void getContracts_incorrectQuery_returnsInvalidRequestException(){
        //Given
        TradeContractsQuery tradeContractsQuery = new TradeContractsQuery();
        tradeContractsQuery.setOrder("incorrectValueForOrder");
        tradeContractsQuery.setSort("someField");

        String expectedMessage = "Invalid query for contracts search: Value for param 'order' is not valid : 'incorrectValueForOrder', must be 'asc' or 'desc'";
        //When
        Exception thrown = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.getContracts("contractType", tradeContractsQuery),
                "Exception should contain valid message");

        // Then
        assertEquals(expectedMessage, thrown.getMessage(), "Exception should contain valid message");
    }
    @Test
    void getContracts_correctQuery_returnsOK() {
        //Given
        String customerCode = "myCustomerCode";
        String status = "myStatus";
        String contractReference = "myContractReference";
        //TradeRequestService tradeRequestServiceMocked = mock(TradeRequestService.class,Answers.RETURNS_DEEP_STUBS);
        TradeContractsQuery tradeContractsQuery = new TradeContractsQuery();
        tradeContractsQuery.setCustomerCode(customerCode);
        tradeContractsQuery.setStatus(status);
        tradeContractsQuery.setContractReference(contractReference);
        tradeContractsQuery.setOrder("asc");
        tradeContractsQuery.setSort("customerFullName");
        tradeContractsQuery.setStart(0);
        tradeContractsQuery.setSize(11);
        DataObjectInstanceVariableContainerQuery queryMock = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery().definitionKey(any())
                .operation(any())).thenReturn(queryMock);
        //When
        tradeRequestService.getContracts("contractType", tradeContractsQuery);

        // Then
        verify(queryMock, times(1)).value("customerCode",
                tradeContractsQuery.getCustomerCode());
        verify(queryMock, times(1)).value("status",
                tradeContractsQuery.getStatus());
        verify(queryMock, times(1)).value("contractReference",
                tradeContractsQuery.getContractReference());
        verify(queryMock, times(1)).asc();
        verify(queryMock, times(1)).listPage(0,11);
    }

    @Test
    void externalTaskCompleteInfo_isOk_completeTask(){
        // Given
        String taskId = "myTaskId";
        String operationCode = "myOperationCode";
        String expectedComment = "my comment";
        TradeRequest request = new TradeRequest();
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        request.setRequester(userInfo);
        request.setComment(expectedComment);
        request.setCode(operationCode);
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put(TradeUtils.CASE_VAR_OPERATION_CODE,operationCode);
        Map<String, Object> operationMap = new HashMap<>();
        rootMap.put(TradeUtils.CASE_VAR_OPERATION, operationMap);
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put(TaskUtils.SCOPE_ROOT, rootMap);
        when(taskUtils.getTaskVariablesCompleteInfo(any())).thenReturn(taskVariables);
        // When
        tradeRequestService.externalTaskCompleteInfo(request,taskId);
        // Then
        verify(taskService).setVariable(taskId,TradeUtils.EXTERNAL_TASK_VAR_COMPLETE_INFO_OUTCOME,TradeUtils.EXTERNAL_TASK_ACTION_CONTINUE);
        verify(taskService).setVariable(taskId,TradeUtils.EXTERNAL_TASK_VAR_COMPLETE_INFO_COMMENT,request.getComment());
        verify(taskUtils).completeTaskCompleteInfo(eq(taskId), any(), any(), any());
    }
    @Test
    void externalTaskCompleteInfo_taskNotFound_returnsResourceNotFoundException(){
        //Given
        UserInfo userInfo = mock(UserInfo.class);
        TradeRequest request = new TradeRequest();
        request.setRequester(userInfo);
        String taskId = "myTaskId";
        String exceptionMessage = "Task not found";
        doThrow(new FlowableObjectNotFoundException(exceptionMessage)).when(caseSecurityService).checkEditTask(userInfo, taskId);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tradeRequestService.externalTaskCompleteInfo(request,taskId),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("externalTaskCompleteInfo", exception.getKey());
        assertEquals(exceptionMessage, exception.getMessage());
    }
    @Test
    void externalTaskCompleteInfo_codesDoNotCorrespond_returnsInvalidRequestException(){
        // Given
        String taskId = "myTaskId";
        String operationCode = "myOperationCode";
        String expectedComment = "my comment";
        String caseOperationCode = "otherOperationCode";
        String expectedMessage = String.format("Codes do not correspond: Case code" +
                        " for task '%s' is '%s', but code in request is '%s'",
                        taskId, caseOperationCode, operationCode);
        TradeRequest request = new TradeRequest();
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        request.setRequester(userInfo);
        request.setComment(expectedComment);
        request.setCode(operationCode);
        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put(TradeUtils.CASE_VAR_OPERATION_CODE,caseOperationCode);
        Map<String, Object> operationMap = new HashMap<>();
        rootMap.put(TradeUtils.CASE_VAR_OPERATION, operationMap);
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put(TaskUtils.SCOPE_ROOT, rootMap);
        when(taskUtils.getTaskVariablesCompleteInfo(any())).thenReturn(taskVariables);
        //When
        Exception thrown = assertThrows(InvalidRequestException.class,
                () -> tradeRequestService.externalTaskCompleteInfo(request,taskId),
                "Exception should contain valid message");

        // Then
        assertEquals(expectedMessage, thrown.getMessage(), "Exception should contain valid message");
    }
    @Test
    void externalTaskRequestCancellation_isOk_completeTask(){
        // Given
        String taskId = "myTaskId";
        String operationCode = "myOperationCode";
        AuthenticatedRequest request = new AuthenticatedRequest();
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        request.setRequester(userInfo);

        Map<String, Object> rootMap = new HashMap<>();
        rootMap.put(TradeUtils.CASE_VAR_OPERATION_CODE,operationCode);
        Map<String, Object> operationMap = new HashMap<>();
        rootMap.put(TradeUtils.CASE_VAR_OPERATION, operationMap);
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put(TaskUtils.SCOPE_ROOT, rootMap);
        when(taskUtils.getTaskVariablesCompleteInfo(any())).thenReturn(taskVariables);
        // When
        tradeRequestService.externalTaskRequestCancellation(taskId, request);
        // Then
        verify(taskService).setVariable(taskId,TradeUtils.EXTERNAL_TASK_VAR_COMPLETE_INFO_OUTCOME,
                TradeUtils.EXTERNAL_TASK_ACTION_REQUEST_FOR_CANCELATION);
        verify(taskUtils).completeTaskCompleteInfo(eq(taskId), any(), any(), any());
    }
    @Test
    void externalTaskRequestCancellation_taskNotFound_returnsResourceNotFoundException(){
        //Given
        UserInfo userInfo = mock(UserInfo.class);
        AuthenticatedRequest request = new AuthenticatedRequest();
        request.setRequester(userInfo);
        String taskId = "myTaskId";
        String exceptionMessage = "Task not found";
        doThrow(new FlowableObjectNotFoundException(exceptionMessage)).when(caseSecurityService).checkEditTask(userInfo, taskId);
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> tradeRequestService.externalTaskRequestCancellation(taskId, request),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("externalTaskRequestCancellation", exception.getKey());
        assertEquals(exceptionMessage, exception.getMessage());
    }
}