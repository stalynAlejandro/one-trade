package com.pagonxt.onetradefinance.work.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.content.engine.impl.persistence.entity.ContentItemEntity;
import com.flowable.core.content.api.CoreContentItem;
import com.flowable.core.content.api.CoreContentService;
import com.flowable.core.content.api.DocumentDefinition;
import com.flowable.core.content.api.DocumentRepositoryService;
import com.flowable.core.content.api.MetadataService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.content.ContentItemRepresentation;
import com.flowable.platform.service.content.PlatformContentItemService;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.common.CaseCommonConstants;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.mapper.OperationDocumentMapper;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class PagoNxtRequestUtilsTest {

    @InjectMocks
    PagoNxtRequestUtils pagoNxtRequestUtils;
    @Mock
    @SuppressWarnings("unused") // Used by pagoNxtRequestUtils
    private MetadataService metadataService;
    @Mock
    @SuppressWarnings("unused") // Used by pagoNxtRequestUtils
    private CoreContentService contentService;
    @Mock
    private PlatformContentItemService platformContentItemService;
    @Mock
    private DocumentRepositoryService documentRepositoryService;
    @Mock
    CaseCommonVariablesHelper caseCommonVariablesHelper;

    @Mock
    private CmmnRuntimeService cmmnRuntimeService;
    @Mock
    private OperationDocumentMapper operationDocumentMapper;

    @Captor
    ArgumentCaptor<ArrayList<CoreContentItem>> captor;

    @Test
    void updateDraftDocumentsOnCaseInstance_ok_updatesDocuments() throws Exception {
        // Given
        Document newDocument = new Document(null, "test.txt", "text/plain", new Date(), "document", "newData");
        PagoNxtRequest request = new PagoNxtRequest();
        request.setDocuments(List.of(newDocument));
        User user = new User();
        user.setUserType("BO");
        request.setRequester(new UserInfo(user));

//        Document oldDocument = new Document("7", "oldTest.txt", "text/plain", new Date(), "document", "oldData");
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getId()).thenReturn("id1");
//        when(caseInstance.getCaseVariables()).thenReturn(Map.of("operationDocuments", List.of(oldDocument)));

        ContentItemRepresentation representation = mock(ContentItemRepresentation.class);
        when(representation.getId()).thenReturn("generatedId");
        when(platformContentItemService.createContentItemOnCaseInstance(any(), any(), any())).thenReturn(representation);

        CoreContentItem item = mock(CoreContentItem.class);
        when(item.getId()).thenReturn("generatedId");
        when(platformContentItemService.getContentItem("generatedId")).thenReturn(item);

        DocumentDefinition definition = mock(DocumentDefinition.class);
        when(definition.getId()).thenReturn("id1");
        when(definition.getKey()).thenReturn("PGN_CON001");
        when(documentRepositoryService.getDocumentDefinitionByKey("PGN_CON001")).thenReturn(definition);

        when(caseCommonVariablesHelper.getRequestDocuments(any())).thenReturn(List.of());

        // When
        pagoNxtRequestUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);

        // Then
        verify(cmmnRuntimeService, times(1)).setVariable(any(), any(), any());
        assertTrue(request.getDocuments().stream().noneMatch(document -> "7".equals(document.getDocumentId())), "The document with id '7' should not be in the list");
        assertTrue(request.getDocuments().stream().anyMatch(document -> "generatedId".equals(document.getDocumentId())), "The document with id 'generatedId' should be in the list");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(metadataService).setMetadataValue(eq("generatedId"), eq("visibility"), captor.capture());
        assertEquals("PUBLIC_OFFICE_AND_CUSTOMER", captor.getValue());
    }

    @Test
    void updateDraftDocumentsOnCaseInstance_whenRemovedDocuments_deleteOldDocuments() throws Exception {
        // Given
        Document newDocument = new Document(null, "test.txt", "text/plain", new Date(), "document", "newData");
        PagoNxtRequest request = new PagoNxtRequest();
        request.setDocuments(List.of(newDocument));
        User user = new User();
        user.setUserType("BO");
        request.setRequester(new UserInfo(user));

        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getId()).thenReturn("id1");

        ContentItemRepresentation representation = mock(ContentItemRepresentation.class);
        when(representation.getId()).thenReturn("generatedId");
        when(platformContentItemService.createContentItemOnCaseInstance(any(), any(), any())).thenReturn(representation);

        CoreContentItem item = mock(CoreContentItem.class);
        when(item.getId()).thenReturn("generatedId");
        when(platformContentItemService.getContentItem("generatedId")).thenReturn(item);

        DocumentDefinition definition = mock(DocumentDefinition.class);
        when(definition.getId()).thenReturn("id1");
        when(definition.getKey()).thenReturn("PGN_CON001");
        when(documentRepositoryService.getDocumentDefinitionByKey("PGN_CON001")).thenReturn(definition);

        Document oldDocument = new Document("oldDocId", "test.txt", "text/plain", new Date(), "document", "oldData");
        when(caseCommonVariablesHelper.getRequestDocuments(any())).thenReturn(List.of(oldDocument));
        // When
        pagoNxtRequestUtils.updateDraftDocumentsOnCaseInstance(request, caseInstance);
        // Then
        verify(contentService).deleteContentItem("oldDocId");
    }

    @Test
    void uploadDocuments_whenRequestDocuments_thenSetOperationDocuments() {
        // Given
        Document newDocument = new Document(null, "test.txt", "text/plain", new Date(), "document", "newData");
        PagoNxtRequest request = new PagoNxtRequest();
        request.setDocuments(List.of(newDocument));
        User user = new User("userTest", "UserTest", "OFFICE");
        request.setRequester(new UserInfo(user));
        String caseInstanceId = "caseInstanceIdTest";
        CoreContentItem oldContentEntity = mock(CoreContentItem.class);
        ArrayList<CoreContentItem> oldDocumentList = new ArrayList<>(List.of(oldContentEntity));
        when(cmmnRuntimeService.getVariable(caseInstanceId, "operationDocuments")).thenReturn(oldDocumentList);
        when(platformContentItemService.getContentItem(any())).thenReturn(mock(CoreContentItem.class));
        when(platformContentItemService.createContentItemOnCaseInstance(any(), any(), any())).thenReturn(mock(ContentItemRepresentation.class));
        when(documentRepositoryService.getDocumentDefinitionByKey(CaseCommonConstants.DOCUMENT_DEFINITION_KEY_METADATA)).thenReturn(mock(DocumentDefinition.class));
        // When
        pagoNxtRequestUtils.uploadDocuments(request, caseInstanceId);
        // Then
        verify(cmmnRuntimeService).setVariable(eq(caseInstanceId), eq("operationDocuments"), captor.capture());
        assertEquals(2, captor.getValue().size());
    }

    @Test
    void uploadDocuments_whenNullOperationDocuments_thenSetOperationDocuments() {
        // Given
        Document newDocument = new Document(null, "test.txt", "text/plain", new Date(), "document", "newData");
        PagoNxtRequest request = new PagoNxtRequest();
        request.setDocuments(List.of(newDocument));
        User user = new User("userTest", "UserTest", "OFFICE");
        request.setRequester(new UserInfo(user));
        String caseInstanceId = "caseInstanceIdTest";
        when(cmmnRuntimeService.getVariable(caseInstanceId, "operationDocuments")).thenReturn(null);
        when(platformContentItemService.getContentItem(any())).thenReturn(mock(CoreContentItem.class));
        when(platformContentItemService.createContentItemOnCaseInstance(any(), any(), any())).thenReturn(mock(ContentItemRepresentation.class));
        when(documentRepositoryService.getDocumentDefinitionByKey(CaseCommonConstants.DOCUMENT_DEFINITION_KEY_METADATA)).thenReturn(mock(DocumentDefinition.class));
        // When
        pagoNxtRequestUtils.uploadDocuments(request, caseInstanceId);
        // Then
        verify(cmmnRuntimeService).setVariable(eq(caseInstanceId), eq("operationDocuments"), captor.capture());
        assertEquals(1, captor.getValue().size());
    }

    @Test
    void uploadDocuments_whenRequestDocumentsAlreadySaved_thenSetOperationDocuments() {
        // Given
        PagoNxtRequest request = new PagoNxtRequest();
        User user = new User("userTest", "UserTest", "OFFICE");
        request.setRequester(new UserInfo(user));
        String caseInstanceId = "caseInstanceIdTest";
        CoreContentItem oldContentEntity = mock(CoreContentItem.class);
        ArrayList<CoreContentItem> oldDocumentList = new ArrayList<>(List.of(oldContentEntity));
        when(cmmnRuntimeService.getVariable(caseInstanceId, "operationDocuments")).thenReturn(oldDocumentList);
        // When
        pagoNxtRequestUtils.uploadDocuments(request, caseInstanceId);
        // Then
        verify(cmmnRuntimeService).setVariable(eq(caseInstanceId), eq("operationDocuments"), captor.capture());
        assertEquals(1, captor.getValue().size());
    }

    @ParameterizedTest
    @CsvSource({"CUSTOMER, PUBLIC_OFFICE_AND_CUSTOMER",
            "OFFICE, PUBLIC_OFFICE",
            "MO, PUBLIC_OFFICE",
            "BO, INTERNAL_BO",
            "OTHER, INTERNAL_BO"})
    void selectVisibility_whenUserType_returnVisibility(String requesterType, String visibility) {
        // When
        String result = pagoNxtRequestUtils.selectVisibility(requesterType);
        // Then
        assertEquals(visibility, result);
    }

    @ParameterizedTest
    @CsvSource({"CUSTOMER, PUBLIC_OFFICE_AND_CUSTOMER, true", "CUSTOMER, PUBLIC_OFFICE, false", "CUSTOMER, INTERNAL_BO, false",
            "OFFICE, PUBLIC_OFFICE_AND_CUSTOMER, true", "OFFICE, PUBLIC_OFFICE, true", "OFFICE, INTERNAL_BO, false",
            "MO, PUBLIC_OFFICE_AND_CUSTOMER, true", "MO, PUBLIC_OFFICE, true", "MO, INTERNAL_BO, false",
            "BO, PUBLIC_OFFICE_AND_CUSTOMER, true", "BO, PUBLIC_OFFICE, true", "BO, INTERNAL_BO, true",
            "OTHER, PUBLIC_OFFICE_AND_CUSTOMER, false", "OTHER, PUBLIC_OFFICE, false", "OTHER, INTERNAL_BO, false"})
    void isVisible_whenUserType_returnBooleanIsVisible(String requesterType, String visibility, boolean isVisible) {
        // Given
        when(metadataService.getMetadataValue("testId", "visibility")).thenReturn(visibility);
        // When
        boolean result = pagoNxtRequestUtils.isVisible("testId", requesterType);
        // Then
        assertEquals(isVisible, result);
    }

    @Test
    void setOperationDocumentsFromRequestDocuments_ok_setsVariable() throws JsonProcessingException {
        // Given
        String caseInstanceId = "1";
        when(cmmnRuntimeService.getVariable(caseInstanceId, "requestDocuments")).thenReturn(new Object());
        Document oldDocument = new Document("7", "oldTest.txt", "text/plain", new Date(), "document", "oldData");
        when(caseCommonVariablesHelper.convertToDocumentList(any())).thenReturn(List.of(oldDocument));
        CoreContentItem contentEntity = mock(CoreContentItem.class);
        when(platformContentItemService.getContentItem("7")).thenReturn(contentEntity);
        List<CoreContentItem> itemsSet = List.of(contentEntity);
        // When
        pagoNxtRequestUtils.setOperationDocumentsFromRequestDocuments(caseInstanceId, "BO");
        // Then
        verify(cmmnRuntimeService, times(1)).setVariable(caseInstanceId, "operationDocuments", itemsSet);
    }

    @Test
    void setOperationDocumentsFromRequestDocuments_whenJsonProcessingException_thenThrowServiceException() throws JsonProcessingException {
        // Given
        String caseInstanceId = "1";
        when(cmmnRuntimeService.getVariable(caseInstanceId, "requestDocuments")).thenReturn(new Object());
        when(caseCommonVariablesHelper.convertToDocumentList(any())).thenThrow(JsonProcessingException.class);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> pagoNxtRequestUtils.setOperationDocumentsFromRequestDocuments(caseInstanceId, "BO"));
        // Then
        assertEquals("Processing error setting operation documents", exception.getMessage());
        assertEquals("setOperationDocumentsFromRequestDocuments", exception.getKey());
    }

    @Test
    void mapCompleteInfoPagoNxtRequest_ok_returnCompleteInfoPagoNxtRequest() {
        // Given
        CompleteInfoPagoNxtRequest completeInfoPagoNxtRequest = new CompleteInfoPagoNxtRequest();
        Map<String, Object> caseVariables = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        caseVariables.put("operation", operation);
        Date date1 = new Date();
        String requesterName = "requesterName";
        when(operation.getDate(CREATION_DATE)).thenReturn(date1);
        when(operation.getString(REQUESTER_DISPLAYED_NAME)).thenReturn(requesterName);
        TaskRepresentation task = new TaskRepresentation();
        Date date2 = new Date();
        task.setCreateTime(date2);
        Map<String, Object> taskVariables = new HashMap<>();
        String returnReason = "returnReason1";
        String returnComment = "returnComment1";
        taskVariables.put(RETURN_REASON, returnReason);
        taskVariables.put(RETURN_COMMENT, returnComment);
        // When
        CompleteInfoPagoNxtRequest result = pagoNxtRequestUtils.mapCompleteInfoPagoNxtRequest(completeInfoPagoNxtRequest, caseVariables, task, taskVariables);
        // Then
        assertEquals(returnReason, result.getReturnReason());
        assertEquals(returnComment, result.getReturnComment());
        assertEquals(date1, result.getRequestCreationTime());
        assertEquals(requesterName, result.getRequestCreatorName());
        assertEquals(date2, result.getTaskCreationTime());
    }

    @Test
    void mapDetailedPagoNxtRequest_ok_returnCompleteInfoPagoNxtRequest() {
        // Given
        CompleteInfoPagoNxtRequest completeInfoPagoNxtRequest = new CompleteInfoPagoNxtRequest();
        Map<String, Object> caseVariables = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        caseVariables.put("operation", operation);
        Date date1 = new Date();
        String requesterName = "requesterName";
        when(operation.getDate(CREATION_DATE)).thenReturn(date1);
        when(operation.getString(REQUESTER_DISPLAYED_NAME)).thenReturn(requesterName);
        TaskRepresentation task = new TaskRepresentation();
        Date date2 = new Date();
        task.setCreateTime(date2);
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put(RETURN_REASON, null);
        taskVariables.put(RETURN_COMMENT, null);
        // When
        CompleteInfoPagoNxtRequest result = pagoNxtRequestUtils.mapCompleteInfoPagoNxtRequest(completeInfoPagoNxtRequest, caseVariables, task, taskVariables);
        // Then
        assertNull(result.getReturnReason());
        assertNull(result.getReturnComment());
        assertEquals(date1, result.getRequestCreationTime());
        assertEquals(requesterName, result.getRequestCreatorName());
        assertEquals(date2, result.getTaskCreationTime());
    }

    @Test
    void mapCommonVariables_ok_putVariablesInMap() {
        // Given
        PagoNxtRequest pagoNxtRequest = new PagoNxtRequest();
        pagoNxtRequest.setRequester(new UserInfo(new User("user", "User", "userType")));
        pagoNxtRequest.setOffice("1234");
        pagoNxtRequest.setMiddleOffice("MO1234");
        pagoNxtRequest.setPriority("priority1");
        pagoNxtRequest.setComment("comment1");
        Map<String, Object> variables = new HashMap<>();
        // When
        pagoNxtRequestUtils.mapCommonVariables(variables, pagoNxtRequest);
        // Then
        assertEquals("user", variables.get("requesterId"));
        assertEquals("User", variables.get("requesterDisplayedName"));
        assertEquals("userType", variables.get("requesterType"));
        assertEquals("1234", variables.get("office"));
        assertEquals("MO1234", variables.get("middleOffice"));
        assertEquals("priority1", variables.get("priority"));
        assertEquals("comment1", variables.get("comment"));
    }

    @Test
    void mapCustomerVariables_ok_putVariablesInMap() {
        // Given
        Customer customer = new Customer("id1", "ES", "name1", "taxId1", "office1", "personNumber1", "segment1", "email1");
        Map<String, Object> variables = new HashMap<>();
        // When
        pagoNxtRequestUtils.mapCustomerVariables(variables, customer);
        // Then
        assertEquals("taxId1", variables.get("customerID"));
        assertEquals("name1", variables.get("customerFullName"));
        assertEquals("personNumber1", variables.get("customerCode"));
        assertEquals("segment1", variables.get("customerSegment"));
        assertEquals("email1", variables.get("customerEmail"));
    }

    @Test
    void mapOperationTypeVariables_ok_putVariablesInMap() {
        //Given
        String operationType = "operationType";
        Map<String, Object> variables = new HashMap<>();
        // When
        pagoNxtRequestUtils.mapOperationTypeVariables(variables, operationType);
        // Then
        assertEquals("operationType",variables.get("operationType"));
    }

    @Test
    void mapAdvanceRiskLineVariables_ok_putVariablesInMap() {
        // Given
        RiskLine advanceRiskLine = new RiskLine();
        advanceRiskLine.setRiskLineId("id1");
        advanceRiskLine.setIban("IBAN1");
        Date date = new Date();
        LocalDate localDate = DateUtils.dateToLocalDate(date);
        advanceRiskLine.setExpires(date);
        advanceRiskLine.setAvailableAmount("availableAmount");
        advanceRiskLine.setCurrency("EUR");
        Map<String, Object> variables = new HashMap<>();
        // When
        pagoNxtRequestUtils.mapAdvanceRiskLineVariables(variables, advanceRiskLine);
        // Then
        assertEquals("id1", variables.get("advanceRiskLineId"));
        assertEquals("IBAN1", variables.get("advanceRiskLineIBAN"));
        assertEquals(localDate, variables.get("advanceRiskLineExpiration"));
        assertEquals("availableAmount", variables.get("advanceRiskLineAvailableAmount"));
        assertEquals("EUR", variables.get("advanceRiskLineCurrency"));
    }

    @Test
    void setCommonVariablesFromDataObject_whenDraft_putVariablesInRequest() throws JsonProcessingException {
        // When
        DataObjectInstanceVariableContainerImpl operation = generateOperationMock();
        doReturn("DRAFT").when(operation).getString("displayedStatus");
        Map<String, Object> variables = new HashMap<>();
        variables.put("operation", operation);
        Object requestDocuments = mock(Object.class);
        variables.put("requestDocuments", requestDocuments);
        List<Document> documents = Collections.emptyList();
        when(caseCommonVariablesHelper.convertToDocumentList(requestDocuments)).thenReturn(documents);
        PagoNxtRequest pagoNxtRequest = new PagoNxtRequest();
        // When
        pagoNxtRequestUtils.setCommonVariablesFromMapVariables(pagoNxtRequest, variables, "userType1");
        // Then
        assertEquals(documents, pagoNxtRequest.getDocuments());
        verify(caseCommonVariablesHelper).convertToDocumentList(requestDocuments);
    }


    @Test
    void setCommonVariablesFromDataObject_whenNoDraft_putVariablesInRequest() throws JsonProcessingException {
        // When
        DataObjectInstanceVariableContainerImpl operation = generateOperationMock();
        doReturn("NO_DRAFT").when(operation).getString("displayedStatus");
        Map<String, Object> variables = new HashMap<>();
        variables.put("operation", operation);
        List<ContentItemEntity> operationDocuments = mock(List.class);
        variables.put("operationDocuments", operationDocuments);
        List<Document> documents = Collections.emptyList();
        when(operationDocumentMapper.mapOperationDocuments(operationDocuments)).thenReturn(documents);
        PagoNxtRequest pagoNxtRequest = new PagoNxtRequest();
        // When
        pagoNxtRequestUtils.setCommonVariablesFromMapVariables(pagoNxtRequest, variables, "userType1");
        // Then
        assertEquals(documents, pagoNxtRequest.getDocuments());
        verify(operationDocumentMapper).mapOperationDocuments(operationDocuments);
    }

    private static DataObjectInstanceVariableContainerImpl generateOperationMock() {
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        doReturn("requesterId1").when(operation).getString("requesterId");
        doReturn("requesterDisplayedName1").when(operation).getString("requesterDisplayedName");
        doReturn("requesterType1").when(operation).getString("requesterType");
        doReturn("product1").when(operation).getString("product");
        doReturn("event1").when(operation).getString("event");
        doReturn("country1").when(operation).getString("country");
        doReturn("office1").when(operation).getString("office");
        doReturn("middleOffice1").when(operation).getString("middleOffice");
        doReturn("priority1").when(operation).getString("priority");
        doReturn("comment1").when(operation).getString("comment");
        return operation;
    }
}
