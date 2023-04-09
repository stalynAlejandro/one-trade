package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.configuration.RestTemplateProperties;
import com.pagonxt.onetradefinance.external.backend.service.exception.FlowableServerException;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContractsQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class RestTemplateWorkServiceTest {

    @InjectMocks
    private RestTemplateWorkService restTemplateWorkService;
    @Mock
    private RestTemplateProperties restTemplateProperties;
    @Mock
    private RestTemplate restTemplate;
    @Spy
    private HttpHeaders httpHeaders = new HttpHeaders();

    @Captor
    private ArgumentCaptor<HttpEntity<ExportCollectionRequest>> exportCollectionRequestCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<Document>> documentCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<ExportCollectionModificationRequest>> exportCollectionModificationRequestCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<ExportCollectionQuery>> exportCollectionQueryCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<MyTasksQuery>> myTasksQueryCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<MyRequestsQuery>> myRequestsQueryCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<ExportCollectionAdvanceRequest>> exportCollectionAdvanceRequestCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<ExportCollectionAdvanceModificationRequest>> exportCollectionAdvanceModificationRequestCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<ExportCollectionAdvanceCancellationRequest>> exportCollectionAdvanceCancellationRequestCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<ExportCollectionOtherOperationsRequest>> exportCollectionOtherOperationsRequestCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<Boolean>> booleanCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<String>> stringCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<OfficeInfo>> officeInfoCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<List<String>>> listStringCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<TradeRequest>> tradeRequestCaptor;
    @Captor
    private ArgumentCaptor<HttpEntity<TradeContractsQuery>> tradeContractsQueryCaptor;

    @Test
    void postExportCollection_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-request"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.postExportCollectionRequestDraft(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-request"),
                eq(HttpMethod.POST), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionRequestCaptor.getValue().getBody());
    }

    @Test
    void putExportCollectionDraft_whenPassingRequest_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-request"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.putExportCollectionRequestDraft(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-request"),
                eq(HttpMethod.PUT), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionRequestCaptor.getValue().getBody());
    }

    @Test
    void putExportCollectionRequest_whenPassingRequest_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-request/confirm"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.putExportCollectionRequest(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-request/confirm"),
                eq(HttpMethod.PUT), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionRequestCaptor.getValue().getBody());
    }

    @Test
    void findExportCollectionDraft_whenPassingDraftId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        AuthenticatedRequest authentication = new AuthenticatedRequest(null);
        HttpEntity<AuthenticatedRequest> entity = new HttpEntity<>(authentication, httpHeaders);
        when(restTemplate.exchange("TestUrl/backend/export-collection-request/get/draftId",
                HttpMethod.POST, entity, ControllerResponse.class)).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        String draftId = "draftId";
        // When
        restTemplateWorkService.findExportCollectionRequestDraft(draftId, authentication);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-request/get/draftId"),
                eq(HttpMethod.POST), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(authentication, exportCollectionRequestCaptor.getValue().getBody(), "Should invoke the method passing the authentication");
    }

    @Test
    void deleteExportCollectionDraft_whenPassingDraftId_throwsUnsupportedOperationException() {
        // Given
        String draftId = "draftId";
        // When
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> restTemplateWorkService.deleteExportCollectionRequestDraft(draftId), "Should throw UnsupportedOperationException");
        // Then
        assertEquals("There is no implementation on Flowable Work for this request", exception.getMessage(), "Exception should return the right message");
    }

    @Test
    void findDocument_whenPassingDocumentId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/document/get/documentId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        String documentId = "documentId";
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findDocument(documentId, null);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/document/get/documentId"),
                eq(HttpMethod.POST), documentCaptor.capture(), eq(ControllerResponse.class));
        assertNull(documentCaptor.getValue().getBody());
    }

    @Test
    void postExportCollectionModificationRequest_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-modification-request"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        // When
        restTemplateWorkService.postExportCollectionModificationRequest(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-modification-request"),
                eq(HttpMethod.POST), exportCollectionModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void postExportCollections_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        when(restTemplate.exchange(eq("TestUrl/backend/export-collections"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionQuery query = new ExportCollectionQuery();
        // When
        restTemplateWorkService.postExportCollections(query);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collections"),
                eq(HttpMethod.POST), exportCollectionQueryCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(query, exportCollectionQueryCaptor.getValue().getBody());
    }

    @Test
    void postMyRequest_whenPassingRequestQuery_thenInvokeRestTemplate() {
        // Given
        MyRequestsList myRequestsList = new MyRequestsList();
        ResponseEntity<MyRequestsList> response = new ResponseEntity<>(myRequestsList, HttpStatus.ACCEPTED);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        when(restTemplate.exchange(eq("TestUrl/backend/my-requests"),
                eq(HttpMethod.POST), any(), eq(MyRequestsList.class))).thenReturn(response);
        MyRequestsQuery query = new MyRequestsQuery();
        // When
        restTemplateWorkService.postMyRequests(query);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/my-requests"),
                eq(HttpMethod.POST), myRequestsQueryCaptor.capture(), eq(MyRequestsList.class));
        assertEquals(query, myRequestsQueryCaptor.getValue().getBody());
    }

    @Test
    void postMyRequest_whenPassingTaskQuery_thenInvokeRestTemplate() {
        // Given
        MyTasksList myTasksList = new MyTasksList();
        ResponseEntity<MyTasksList> response = new ResponseEntity<>(myTasksList, HttpStatus.ACCEPTED);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        when(restTemplate.exchange(eq("TestUrl/backend/my-tasks"),
                eq(HttpMethod.POST), any(), eq(MyTasksList.class))).thenReturn(response);
        MyTasksQuery query = new MyTasksQuery();
        // When
        restTemplateWorkService.postMyTasks(query);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/my-tasks"),
                eq(HttpMethod.POST), myTasksQueryCaptor.capture(), eq(MyTasksList.class));
        assertEquals(query, myTasksQueryCaptor.getValue().getBody());
    }

    @Test
    void extractResult_whenFlowableWorkError_thenThrowFlowableServerException() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.error("Test error");
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/document/get/documentId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        String documentId = "documentId";
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        Exception exception = assertThrows(FlowableServerException.class, () -> restTemplateWorkService.findDocument(documentId, null));
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/document/get/documentId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class));
        assertEquals("Test error", exception.getMessage());
    }

    @Test
    void extractResult_whenNullBodyResponse_thenThrowFlowableServerException() {
        // Given
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/document/get/documentId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        String documentId = "documentId";
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        Exception exception = assertThrows(FlowableServerException.class, () -> restTemplateWorkService.findDocument(documentId, null));
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/document/get/documentId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class));
        assertEquals("Unknown Error", exception.getMessage());
    }

    @Test
    void postExportCollectionAdvanceRequest_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-request"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.postExportCollectionAdvanceRequestDraft(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-request"),
                eq(HttpMethod.POST), exportCollectionAdvanceRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceRequestCaptor.getValue().getBody());
    }

    @Test
    void putExportCollectionAdvanceRequestDraft_whenPassingRequest_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-request"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.putExportCollectionAdvanceRequestDraft(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-request"),
                eq(HttpMethod.PUT), exportCollectionAdvanceRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceRequestCaptor.getValue().getBody());
    }

    @Test
    void putExportCollectionAdvanceRequest_whenPassingRequest_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-request/confirm"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.putExportCollectionAdvanceRequest(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-request/confirm"),
                eq(HttpMethod.PUT), exportCollectionAdvanceRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceRequestCaptor.getValue().getBody());
    }

    @Test
    void findExportCollectionAdvanceRequestDraft_whenPassingDraftId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-request/get/draftId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        AuthenticatedRequest authentication = mock(AuthenticatedRequest.class);
        String draftId = "draftId";
        // When
        restTemplateWorkService.findExportCollectionAdvanceRequestDraft(draftId, authentication);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-request/get/draftId"),
                eq(HttpMethod.POST), exportCollectionAdvanceRequestCaptor.capture(), eq(ControllerResponse.class));
    }

    @Test
    void postExportCollectionAdvanceModificationRequest_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-modification-request"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        // When
        restTemplateWorkService.postExportCollectionAdvanceModificationRequest(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-modification-request"),
                eq(HttpMethod.POST), exportCollectionAdvanceModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void postExportCollectionAdvanceCancellationRequest_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        // When
        restTemplateWorkService.postExportCollectionAdvanceCancellationRequest(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request"),
                eq(HttpMethod.POST), exportCollectionAdvanceCancellationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceCancellationRequestCaptor.getValue().getBody());
    }

    @Test
    void postExportCollectionOtherOperationsRequest_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-other-operations-request"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        // When
        restTemplateWorkService.postExportCollectionOtherOperationsRequest(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-other-operations-request"),
                eq(HttpMethod.POST), exportCollectionOtherOperationsRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionOtherOperationsRequestCaptor.getValue().getBody());
    }

    @Test
    void findCompleteInfoExportCollectionRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findCompleteInfoExportCollectionRequest("taskId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionRequestCaptor.getValue().getBody());
    }

    @Test
    void completeCompleteInfoExportCollectionRequest_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.completeCompleteInfoExportCollectionRequest(request, "taskId");
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionRequestCaptor.getValue().getBody());
    }

    @Test
    void cancelCompleteInfoTask_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/task-operation/complete-info/cancel/taskId"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.cancelCompleteInfoTask("taskId", null);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/task-operation/complete-info/cancel/taskId"),
                eq(HttpMethod.PUT), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNull(exportCollectionRequestCaptor.getValue().getBody());
    }

    @Test
    void findCompleteInfoExportCollectionModificationRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-modification-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findCompleteInfoExportCollectionModificationRequest("taskId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-modification-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), exportCollectionModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void completeCompleteInfoExportCollectionModificationRequest_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-modification-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.completeCompleteInfoExportCollectionModificationRequest(request, "taskId");
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-modification-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), exportCollectionModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void findCompleteInfoExportCollectionAdvanceRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findCompleteInfoExportCollectionAdvanceRequest("taskId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), exportCollectionAdvanceRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionAdvanceRequestCaptor.getValue().getBody());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceRequest_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.completeCompleteInfoExportCollectionAdvanceRequest(request, "taskId");
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), exportCollectionAdvanceRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceRequestCaptor.getValue().getBody());
    }

    @Test
    void findCompleteInfoExportCollectionAdvanceModificationRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-modification-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findCompleteInfoExportCollectionAdvanceModificationRequest("taskId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-modification-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), exportCollectionAdvanceModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionAdvanceModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceModificationRequest_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-modification-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.completeCompleteInfoExportCollectionAdvanceModificationRequest(request, "taskId");
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-modification-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), exportCollectionAdvanceModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void findCompleteInfoExportCollectionAdvanceCancellationRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findCompleteInfoExportCollectionAdvanceCancellationRequest("taskId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), exportCollectionAdvanceCancellationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionAdvanceCancellationRequestCaptor.getValue().getBody());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceCancellationRequest_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, "taskId");
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), exportCollectionAdvanceCancellationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionAdvanceCancellationRequestCaptor.getValue().getBody());
    }

    @Test
    void findCompleteInfoExportCollectionOtherOperationsRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-other-operations-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findCompleteInfoExportCollectionOtherOperationsRequest("taskId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-other-operations-request/complete-info/get/taskId"),
                eq(HttpMethod.POST), exportCollectionOtherOperationsRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionOtherOperationsRequestCaptor.getValue().getBody());
    }

    @Test
    void completeCompleteInfoExportCollectionOtherOperationsRequest_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-other-operations-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.completeCompleteInfoExportCollectionOtherOperationsRequest(request, "taskId");
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-other-operations-request/complete-info/complete/taskId"),
                eq(HttpMethod.PUT), exportCollectionOtherOperationsRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, exportCollectionOtherOperationsRequestCaptor.getValue().getBody());
    }

    @Test
    void findComments_whenPassingTaskId_thenInvokeRestTemplatePost() {
        // Given
        Comment comment = new Comment();
        comment.setTaskName("Case 123");
        List<Comment> comments = List.of(comment);
        ControllerResponse controllerResponse = ControllerResponse.success("", comments);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        AuthenticatedRequest authentication = new AuthenticatedRequest();
        HttpEntity<AuthenticatedRequest> httpEntity = new HttpEntity<>(authentication, httpHeaders);
        when(restTemplate.exchange("TestUrl/backend/case-data/get/CLE-TEST/comments",
                HttpMethod.POST, httpEntity, ControllerResponse.class)).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findComments( "CLE-TEST", authentication);
        // Then
        verify(restTemplate, times(1)).exchange("TestUrl/backend/case-data/get/CLE-TEST/comments",
                HttpMethod.POST, httpEntity, ControllerResponse.class);
    }

    @Test
    void postHistoricTasks_whenPassingHistoricTasksQuery_thenInvokeRestTemplatePost() {
        // Given
        HistoricTasksQuery historicTasksQuery = new HistoricTasksQuery();
        HistoricTasksList historicTasksList = new HistoricTasksList();
        ResponseEntity<HistoricTasksList> response = new ResponseEntity<>(historicTasksList, HttpStatus.ACCEPTED);
        HttpEntity<HistoricTasksQuery> httpEntity = new HttpEntity<>(historicTasksQuery, httpHeaders);
        when(restTemplate.exchange("TestUrl/backend/historic-tasks",
                HttpMethod.POST, httpEntity, HistoricTasksList.class)).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.postHistoricTasks(historicTasksQuery);
        // Then
        verify(restTemplate, times(1)).exchange("TestUrl/backend/historic-tasks",
                HttpMethod.POST, httpEntity, HistoricTasksList.class);
    }

    @Test
    void findDetailsExportCollectionRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-request/details/requestId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findDetailsExportCollectionRequest("requestId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-request/details/requestId"),
                eq(HttpMethod.POST), exportCollectionRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionRequestCaptor.getValue().getBody());
    }

    @Test
    void findDetailsExportCollectionModificationRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-modification-request/details/requestId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findDetailsExportCollectionModificationRequest("requestId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-modification-request/details/requestId"),
                eq(HttpMethod.POST), exportCollectionModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void findDetailsExportCollectionAdvanceRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-request/details/requestId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findDetailsExportCollectionAdvanceRequest("requestId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-request/details/requestId"),
                eq(HttpMethod.POST), exportCollectionAdvanceRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionAdvanceRequestCaptor.getValue().getBody());
    }

    @Test
    void findDetailsExportCollectionAdvanceModificationRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-modification-request/details/requestId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findDetailsExportCollectionAdvanceModificationRequest("requestId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-modification-request/details/requestId"),
                eq(HttpMethod.POST), exportCollectionAdvanceModificationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionAdvanceModificationRequestCaptor.getValue().getBody());
    }

    @Test
    void findDetailsExportCollectionAdvanceCancellationRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request/details/requestId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findDetailsExportCollectionAdvanceCancellationRequest("requestId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-advance-cancellation-request/details/requestId"),
                eq(HttpMethod.POST), exportCollectionAdvanceCancellationRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionAdvanceCancellationRequestCaptor.getValue().getBody());
    }

    @Test
    void findDetailsExportCollectionOtherOperationsRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/export-collection-other-operations-request/details/requestId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findDetailsExportCollectionOtherOperationsRequest("requestId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/export-collection-other-operations-request/details/requestId"),
                eq(HttpMethod.POST), exportCollectionOtherOperationsRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(exportCollectionOtherOperationsRequestCaptor.getValue().getBody());
    }

    @Test
    void isValidOffice_whenPassingRequestAndOfficeId_thenInvokeRestTemplatePost() {
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/office/getValidOffice/officeId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.isValidOffice("officeId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/office/getValidOffice/officeId"),
                eq(HttpMethod.POST), booleanCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(booleanCaptor.getValue().getBody());
    }

    @Test
    void isValidMiddleOffice_whenPassingRequestAndOfficeId_thenInvokeRestTemplatePost() {
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/office/getValidMiddleOffice/officeId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.isValidMiddleOffice("officeId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/office/getValidMiddleOffice/officeId"),
                eq(HttpMethod.POST), booleanCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(booleanCaptor.getValue().getBody());
    }

    @Test
    void getMiddleOffice_whenPassingRequestAndOfficeId_thenInvokeRestTemplatePost() {
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/office/getMiddleOffice/officeId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.getMiddleOffice("officeId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/office/getMiddleOffice/officeId"),
                eq(HttpMethod.POST), stringCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(stringCaptor.getValue().getBody());
    }

    @Test
    void getOfficeInfo_whenPassingRequestAndOfficeId_thenInvokeRestTemplatePost() {
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/office/getOfficeInfo/officeId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.getOfficeInfo("officeId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/office/getOfficeInfo/officeId"),
                eq(HttpMethod.POST), officeInfoCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(officeInfoCaptor.getValue().getBody());
    }

    @Test
    void getOffices_whenPassingRequestAndOfficeId_thenInvokeRestTemplatePost() {
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/office/getOffices/officeId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.getOffices("officeId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/office/getOffices/officeId"),
                eq(HttpMethod.POST), listStringCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(listStringCaptor.getValue().getBody());
    }

    @Test
    void postTradeRequestDraft_whenPassingRequest_thenInvokeRestTemplate() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/trade-request"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        TradeRequest request = new TradeRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.postTradeRequestDraft(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request"),
                eq(HttpMethod.POST), tradeRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, tradeRequestCaptor.getValue().getBody());
    }

    @Test
    void putTradeRequestDraft_whenPassingRequest_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/trade-request"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        TradeRequest request = new TradeRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.putTradeRequestDraft(request);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request"),
                eq(HttpMethod.PUT), tradeRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, tradeRequestCaptor.getValue().getBody());
    }

    @Test
    void putTradeRequest_whenPassingRequest_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/trade-request/caseId/confirm"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        AuthenticatedRequest authentication = new AuthenticatedRequest(null);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.putTradeRequest("caseId", authentication);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request/caseId/confirm"),
                eq(HttpMethod.PUT), tradeRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(authentication, tradeRequestCaptor.getValue().getBody());
    }

    @Test
    void findTradeRequestDraft_whenPassingDraftId_thenInvokeRestTemplatePost() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        AuthenticatedRequest authentication = new AuthenticatedRequest(null);
        HttpEntity<AuthenticatedRequest> entity = new HttpEntity<>(authentication, httpHeaders);
        when(restTemplate.exchange("TestUrl/backend/trade-request/get/draftId",
                HttpMethod.POST, entity, ControllerResponse.class)).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findTradeRequest("draftId", authentication);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request/get/draftId"),
                eq(HttpMethod.POST), tradeRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(authentication, tradeRequestCaptor.getValue().getBody(), "Should invoke the method passing the authentication");
    }

    @Test
    void findTradeContracts_whenPassingQuery_thenInvokeRestTemplatePost() {
        // Given
        TradeContractsQuery tradeContractsQuery = new TradeContractsQuery();
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        HttpEntity<TradeContractsQuery> entity = new HttpEntity<>(tradeContractsQuery, httpHeaders);
        when(restTemplate.exchange("TestUrl/backend/trade-request/contracts/cli_request/search",
                HttpMethod.POST, entity, ControllerResponse.class)).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findTradeContracts("cli_request", tradeContractsQuery);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request/contracts/cli_request/search"),
                eq(HttpMethod.POST), tradeContractsQueryCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(tradeContractsQuery, tradeContractsQueryCaptor.getValue().getBody(), "Should invoke the method passing the authentication");
    }
    @Test
    void findTradeExternalTaskRequest_whenPassingTaskId_thenInvokeRestTemplateGet() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/trade-request/tasks/taskId"),
                eq(HttpMethod.POST), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.findTradeExternalTaskRequest("taskId", new AuthenticatedRequest());
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request/tasks/taskId"),
                eq(HttpMethod.POST), tradeRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNotNull(tradeRequestCaptor.getValue().getBody());
    }

    @Test
    void completeCompleteInfoTradeRequest_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/trade-request/tasks/taskId/complete-info"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        TradeRequest request = new TradeRequest();
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.completeCompleteInfoTradeRequest(request, "taskId");
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request/tasks/taskId/complete-info"),
                eq(HttpMethod.PUT), tradeRequestCaptor.capture(), eq(ControllerResponse.class));
        assertEquals(request, tradeRequestCaptor.getValue().getBody());
    }

    @Test
    void cancelTradeExternalTask_whenPassingRequestAndTaskId_thenInvokeRestTemplatePut() {
        // Given
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        ResponseEntity<ControllerResponse> response = new ResponseEntity<>(controllerResponse, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(eq("TestUrl/backend/trade-request/tasks/taskId/request-cancellation"),
                eq(HttpMethod.PUT), any(), eq(ControllerResponse.class))).thenReturn(response);
        when(restTemplateProperties.getFlowableWorkUrl()).thenReturn("TestUrl");
        // When
        restTemplateWorkService.cancelTradeExternalTask("taskId", null);
        // Then
        verify(restTemplate, times(1)).exchange(eq("TestUrl/backend/trade-request/tasks/taskId/request-cancellation"),
                eq(HttpMethod.PUT), tradeRequestCaptor.capture(), eq(ControllerResponse.class));
        assertNull(tradeRequestCaptor.getValue().getBody());
    }
}
