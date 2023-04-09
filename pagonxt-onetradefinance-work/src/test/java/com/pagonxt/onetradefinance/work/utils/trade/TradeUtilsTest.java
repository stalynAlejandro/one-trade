package com.pagonxt.onetradefinance.work.utils.trade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.core.content.api.CoreContentItem;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectModificationBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.content.PlatformContentItemService;
import com.flowable.platform.service.task.PlatformTaskService;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.ValidationError;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.OperationDocumentMapper;
import com.pagonxt.onetradefinance.work.service.mapper.trade.TradeRequestMapper;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstanceQuery;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Instant;
import java.util.*;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.*;

@UnitTest
class TradeUtilsTest {

    @InjectMocks
    TradeUtils tradeUtils;

    @Mock
    PagoNxtRequestUtils pagoNxtRequestUtils;

    @Mock
    CaseCommonVariablesHelper caseCommonVariablesHelper;

    @Mock
    PlatformTaskService platformTaskService;

    @Mock
    CmmnRuntimeService cmmnRuntimeService;

    @Mock
    private OperationDocumentMapper operationDocumentMapper;

    @Mock
    DataObjectRuntimeService dataObjectRuntimeService;

    @Mock
    PlatformContentItemService platformContentItemService;

    @Mock
    TradeRequestMapper tradeRequestMapper;

    @Captor
    ArgumentCaptor<ArrayList<CoreContentItem>> captor;

    @Test
    void validateContractMiddleOffice_requestMiddleOfficeIsNull_returnEmptyList(){

        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();

        validationErrors = tradeUtils.validateContractMiddleOffice(request, validationErrors);

        assertNull(request.getMiddleOffice());
        assertEquals(0, validationErrors.size());

    }

    @Test
    void validateContractMiddleOffice_userMiddleOfficeIsNotEqualsRequesterMiddleOffice_addErrorToReturnList(){
        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        User user = new User("userTest", "UserTest", "MO");
        UserInfo userInfo = new UserInfo(user);
        userInfo.setMiddleOffice("1112");
        request.setRequester(userInfo);
        request.setMiddleOffice("1111");

        validationErrors = tradeUtils.validateContractMiddleOffice(request,validationErrors);

        assertEquals(1, validationErrors.size());
        assertEquals("notAllowedValue", validationErrors.get(0).getViolation() );
    }

    @Test
    void validateContractMiddleOffice_userIsOffice_addErrorToReturnList(){
        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        userInfo.setMiddleOffice("1111");
        request.setRequester(userInfo);
        request.setMiddleOffice("1111");

        validationErrors = tradeUtils.validateContractMiddleOffice(request,validationErrors);

        assertEquals(0, validationErrors.size());

    }

    @Test
    void validateContractMiddleOffice_officeMiddleOfficeEqualsRequesterMiddleOffice_ReturnEmptyList(){
        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        User user = new User("userTest", "UserTest", "MO");
        UserInfo userInfo = new UserInfo(user);
        userInfo.setMiddleOffice("1111");
        request.setRequester(userInfo);
        request.setMiddleOffice("1111");

        validationErrors = tradeUtils.validateContractMiddleOffice(request,validationErrors);

        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateDocuments_requestGetDocumentsIsEmpty_returnEmptyList(){

        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();

        validationErrors = tradeUtils.validateDocuments(request, validationErrors);

        assertEquals(0, request.getDocuments().size());
        assertEquals(0, validationErrors.size());

    }

    @Test
    void validateDocuments_requestGetDocumentsIsNull_returnEmptyList(){

        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        request.setDocuments(null);

        validationErrors = tradeUtils.validateDocuments(request, validationErrors);

        assertNull(request.getDocuments());
        assertEquals(0, validationErrors.size());

    }

    @Test
    void validateDocuments_requestGetDocumentsIsNotNullAndNotEmpty_returnEmptyList(){

        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        List<Document> documents = new ArrayList<>();
        Document document = new Document();
        document.setFilename("test.pdf");
        documents.add(document);
        request.setDocuments(documents);

        validationErrors = tradeUtils.validateDocuments(request, validationErrors);

        assertEquals(0, validationErrors.size());

    }

    @Test
    void validateDocuments_requestGetDocumentsIsNotNull_returnListWithErrors(){

        TradeRequest request = new TradeRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        List<Document> documents = new ArrayList<>();
        Document document = new Document();
        document.setFilename("test.txt");
        documents.add(document);
        request.setDocuments(documents);

        validationErrors = tradeUtils.validateDocuments(request, validationErrors);

        assertEquals(1, validationErrors.size());
        assertEquals("wrongExtension", validationErrors.get(0).getViolation() );
    }

    @Test
    void mapCommonVariables_storesDataCorrectly(){
        TradeRequest request = new TradeRequest();
        User user = new User();
        user.setUserId("id");
        user.setUserDisplayedName("userTest");
        user.setUserType("MO");
        UserInfo userInfo = new UserInfo(user);
        request.setRequester(userInfo);
        request.setCountry("ES");
        request.setOffice("1111");
        request.setMiddleOffice("2222");
        request.setComment("comment");
        request.setPriority("normal");
        Map<String, Object> variables = new HashMap<>();

        tradeUtils.mapCommonVariables(variables, request);

        assertEquals("id", variables.get("requesterId"));
        assertEquals("userTest", variables.get("requesterDisplayedName"));
        assertEquals("MO", variables.get("requesterType"));
        assertEquals("ES", variables.get("country"));
        assertEquals("1111", variables.get("office"));
        assertEquals("2222", variables.get("middleOffice"));
        assertEquals("comment", variables.get("comment"));
        assertEquals("normal", variables.get("priority"));
    }

    @Test
    void mapCustomerVariables_storesDataCorrectly(){
        Map<String, Object> variables = new HashMap<>();
        Customer customer = new Customer();
        customer.setCustomerId("is");
        customer.setName("name");
        customer.setPersonNumber("personNumber");
        customer.setSegment("segment");
        customer.setEmail("customer@email.com");

        tradeUtils.mapCustomerVariables(variables, customer);

        verify(pagoNxtRequestUtils).mapCustomerVariables(variables, customer);


    }

    @Test
    void updateDraftDocumentsOnCaseInstance_currentDocumentListError() throws JsonProcessingException {
        TradeRequest request = new TradeRequest();
        CaseInstance caseInstance = mock(CaseInstance.class);

        when(caseCommonVariablesHelper.getRequestDocuments(caseInstance)).thenThrow(JsonProcessingException.class);

        // When
        Exception exception = assertThrows(ServiceException.class,
                () -> tradeUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance),
                "Expected exception message");

        // Then
        assertEquals("Processing error updating draft documents", exception.getMessage(), "Expected exception message");

    }

    @Test
    void updateDraftDocumentOnCaseInstance_currentDocumentListNotEmptyAndNotNull_addDocumentInUpdatedListDocument() throws JsonProcessingException {
        TradeRequest request = new TradeRequest();
        CaseInstance caseInstance = mock(CaseInstance.class);
        List<Document> documents = new ArrayList<>();
        String documentId = "documentId";
        Document document = new Document();
        document.setDocumentId(documentId);
        document.setMimeType("application/pdf");
        document.setFilename("filename1.pdf");
        documents.add(document);
        request.setDocuments(documents);
        when(caseCommonVariablesHelper.getRequestDocuments(caseInstance)).thenReturn(documents);

        tradeUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);

        assertEquals(1, request.getDocuments().size());

    }
    @Test
    void getTradeExternalTaskRequestFromMapVariables_returnValidObject() {
        //Given
        String expectedReturnComment = "return Comment";
        String exptectedReturnReason = "myReturnReason";
        Map<String, Object> taskVariables = new HashMap<>();
        Map<String, Object> rootMap = new HashMap<>();
        Map<String, Object> caseVariables = new HashMap<>();
        Map<String, Object> taskDetails = new HashMap<>();
        Instant instant = mock(Instant.class);

        rootMap.put("id","CLI_001");
        rootMap.put(RETURN_COMMENT, expectedReturnComment);
        rootMap.put(RETURN_REASON, exptectedReturnReason);
        taskVariables.put("root", rootMap);
        String userType = "OFFICE";
        CaseInstance caseInstance = mock(CaseInstance.class);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseInstanceId(any())).thenReturn(caseInstanceQuery);
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables().caseInstanceId(any()).singleResult())
                .thenReturn(caseInstance);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        caseVariables.put("operation", operation);
        Date date1 = new Date();
        when(operation.getDate(CREATION_DATE)).thenReturn(date1);
        caseVariables.put("operation", operation);
        taskDetails.put("createTime", instant);
        taskVariables.put("task", taskDetails);
        //when
        TradeExternalTaskRequest tradeExternalTaskRequest = tradeUtils.getTradeExternalTaskRequestFromMapVariables(taskVariables, userType);
        //then
        assertEquals(expectedReturnComment, tradeExternalTaskRequest.getReturnComment());
        assertEquals(exptectedReturnReason, tradeExternalTaskRequest.getReturnReason());
    }

    @Test
    void getTradeExternalTaskRequestFromMapVariables_ClassCassException() {
        Map<String, Object> taskVariables = new HashMap<>();
        Map<String, Object> root = mock(Map.class);
        Map<String, Object> caseVariables = new HashMap<>();
        Map<String, Object> taskDetails = new HashMap<>();
        root.put("id","CLI_001");
        taskVariables.put("root", root);
        String userType = "OFFICE";
        CaseInstance caseInstance = mock(CaseInstance.class);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseInstanceId(any())).thenReturn(caseInstanceQuery);
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables().caseInstanceId(any()).singleResult())
                .thenReturn(caseInstance);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        caseVariables.put("operation", operation);
        Date date1 = new Date();
        caseVariables.put("operation", operation);
        taskDetails.put("createTime", date1);
        taskVariables.put("task", taskDetails);
        taskVariables.put(RETURN_COMMENT, "return Comment");
        taskVariables.put(RETURN_REASON, "return Reason");

        // When
        Exception exception = assertThrows(ClassCastException.class,
                () -> tradeUtils.getTradeExternalTaskRequestFromMapVariables(taskVariables, userType),
                "Expected exception message");

        // Then
        assertEquals("Cannot cast createTime value to Instant", exception.getMessage(), "Expected exception message");

    }

    @Test
    void getTradeExternalTaskVariables_taskIdIsBlank_returnInvalidRequestException(){
        String taskId = "";

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeUtils.getTradeExternalTaskVariables(taskId),
                "Expected exception message");

        // Then
        assertEquals("The field 'taskId' must be informed.", exception.getMessage(), "Expected exception message");
    }

    @Test
    void getTradeExternalTaskVariables_taskVariableIsNull_returnResourceNotFoundException(){
        String taskId = "taskId";
        when(platformTaskService.getTaskVariables(taskId)).thenReturn(null);

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> tradeUtils.getTradeExternalTaskVariables(taskId),
                "Expected exception message");

        // Then
        assertEquals("No task found with id taskId", exception.getMessage(), "Expected exception message");
    }

    @Test
    void getTradeExternalTaskVariables_returnTaskVariables(){
        String taskId = "taskId";
        Map<String,Object> variables = new HashMap<>();
        Map<String,Object> taskVariables;
        Map<String,Object> taskMap = new HashMap<>();


        variables.put("isExternalTask","true");
        variables.put("externalTaskType", TaskUtils.COMPLETE_INFORMATION);
        variables.put("task", taskMap);
        when(platformTaskService.getTaskVariables(taskId)).thenReturn(variables);

        taskVariables = tradeUtils.getTradeExternalTaskVariables(taskId);

        assertNotNull(taskVariables);
    }

    @Test
    void getTradeExternalTaskVariables_taskIdIsNotValid_returnInvalidRequestException(){
        String taskId = "taskId";

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeUtils.getTradeExternalTaskVariables(taskId),
                "Expected exception message");

        // Then
        assertEquals("The task found with id taskId is not the right type", exception.getMessage(), "Expected exception message");
    }
    @Test
    void getTradeExternalTaskVariables_taskIsAlreadyCompleted_returnInvalidRequestException(){
        String taskId = "taskId";
        Map<String,Object> variables = new HashMap<>();
        Map<String,Object> taskMap = new HashMap<>();

        taskMap.put("endTime", new Date());
        variables.put("isExternalTask","true");
        variables.put("externalTaskType", TaskUtils.COMPLETE_INFORMATION);
        variables.put("task", taskMap);

        when(platformTaskService.getTaskVariables(taskId)).thenReturn(variables);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> tradeUtils.getTradeExternalTaskVariables(taskId),
                "Expected exception message");

        // Then
        assertEquals("The task found with id taskId is already completed", exception.getMessage(), "Expected exception message");
    }

    @Test
    void createOperationDataObjectInstance_returnDataObjectInstanceVariableBuilder(){
        Map<String, Object> variables = new HashMap<>();
        variables.put("product", "CLI");
        DataObjectInstanceVariableContainer builder;

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class, RETURNS_DEEP_STUBS);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey("PGN_DO001")).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainerMock = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenReturn(dataObjectInstanceVariableContainerMock);

        builder = tradeUtils.createOperationDataObjectInstance(variables, "PGN_DO001", "createDraft");

        verify(dataObjectInstanceVariableContainerBuilderMock).operation("createDraft");
        assertNotNull(builder);

    }
    
    @Test
    void updateOperationDataObjectInstance(){

        Map<String, Object> variables = new HashMap<>();
        variables.put("product", "CLI");
        variables.put("event", "request");

        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class, RETURNS_DEEP_STUBS);
        when(dataObjectRuntimeService.createDataObjectModificationBuilder()).thenReturn(dataObjectModificationBuilderMock);
        
        tradeUtils.updateOperationDataObjectInstance(variables, "PGN_DO001", "updateDraft");

        assertNotNull(dataObjectModificationBuilderMock);
        
    }
    @Test
    void addDocumentsToCase_whenDocuments_thenSetOperationDocuments() {
        // Given
        Document newDocument = new Document(null, "test.txt", "text/plain", new Date(), "document", "newData");
        List<Document> documentsToAdd = List.of(newDocument);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        String caseInstanceId = "caseInstanceIdTest";
        CoreContentItem oldContentEntity = mock(CoreContentItem.class);
        ArrayList<CoreContentItem> oldDocumentList = new ArrayList<>(List.of(oldContentEntity));
        when(cmmnRuntimeService.getVariable(caseInstanceId, "operationDocuments")).thenReturn(oldDocumentList);
        when(platformContentItemService.getContentItem(any())).thenReturn(mock(CoreContentItem.class));
        // When
        tradeUtils.addDocumentsToCase(userInfo, documentsToAdd, caseInstanceId);
        // Then
        verify(cmmnRuntimeService).setVariable(eq(caseInstanceId), eq("operationDocuments"), captor.capture());
        assertEquals(2, captor.getValue().size());
    }
}