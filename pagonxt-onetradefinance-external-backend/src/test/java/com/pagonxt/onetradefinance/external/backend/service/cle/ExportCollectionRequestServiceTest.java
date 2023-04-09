package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionRequestServiceTest {

    @Mock
    private RestTemplateWorkService restTemplateWorkService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private TaskConcurrencyMonitor taskConcurrencyMonitor;

    @InjectMocks
    private ExportCollectionRequestService exportCollectionRequestService;

    @Test
    void confirmExportCollectionRequest_whenPassingRequest_thenInvokePut() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.putExportCollectionRequestDraft(any())).thenReturn(controllerResponse);
        when(restTemplateWorkService.putExportCollectionRequest(any())).thenReturn(controllerResponse);
        // When
        ExportCollectionRequest result = exportCollectionRequestService.confirmExportCollectionRequest(request);
        // Then
        verify(restTemplateWorkService, times(1)).putExportCollectionRequestDraft(any());
        verify(restTemplateWorkService, times(1)).putExportCollectionRequest(any());
        assertEquals(code, result.getCode());
    }

    @Test
    void createOrUpdateExportCollectionRequestDraft_whenPassingDraftWithoutCode_thenInvokePost() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        ExportCollectionRequest responseDraft = new ExportCollectionRequest();
        String code = "Code Draft";
        responseDraft.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", responseDraft);
        when(restTemplateWorkService.postExportCollectionRequestDraft(request)).thenReturn(controllerResponse);
        // When
        ExportCollectionRequest result = exportCollectionRequestService.createOrUpdateExportCollectionRequestDraft(request);
        // Then
        verify(restTemplateWorkService, times(1)).postExportCollectionRequestDraft(request);
        assertEquals(code, result.getCode());
    }

    @Test
    void createOrUpdateExportCollectionRequestDraft_whenPassingDraftWithCode_thenInvokePut() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.putExportCollectionRequestDraft(request)).thenReturn(controllerResponse);
        // When
        ExportCollectionRequest result = exportCollectionRequestService.createOrUpdateExportCollectionRequestDraft(request);
        // Then
        verify(restTemplateWorkService, times(1)).putExportCollectionRequestDraft(request);
        assertEquals(code, result.getCode());
    }

    @Test
    void getExportCollectionRequestDraft_whenPassingDraftId_thenInvokeFind() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findExportCollectionRequestDraft(eq(code), any())).thenReturn(controllerResponse);
        // When
        ExportCollectionRequest result = exportCollectionRequestService.getExportCollectionRequestDraft(code, new UserInfo());
        // Then
        verify(restTemplateWorkService, times(1)).findExportCollectionRequestDraft(eq(code), any());
        assertEquals(request.getCode(), result.getCode());
    }

    @Test
    void deleteExportCollectionRequestDraft_whenPassingDraftId_thenInvokeDelete() {
        // Given
        String code = "Code Draft";
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.deleteExportCollectionRequestDraft(code)).thenReturn(controllerResponse);
        // When
        exportCollectionRequestService.deleteExportCollectionRequestDraft(code);
        // Then
        verify(restTemplateWorkService, times(1)).deleteExportCollectionRequestDraft(code);
    }

    @Test
    void getCompleteInfoExportCollectionRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionRequest request = new CompleteInfoExportCollectionRequest();
        request.setReturnReason("returnReason1");
        request.setReturnComment("returnComment1");
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findCompleteInfoExportCollectionRequest(eq("taskId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionRequestService.getCompleteInfoExportCollectionRequest("taskId1", true, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findCompleteInfoExportCollectionRequest(eq("taskId1"), any());
        verify(taskConcurrencyMonitor).generateWarningGetCompleteInfo("taskId1", controllerResponse, true, "User");
        CompleteInfoPagoNxtRequest resultEntity = (CompleteInfoPagoNxtRequest) result.getEntity();
        assertEquals(request.getReturnComment(), resultEntity.getReturnComment());
        assertEquals(request.getReturnReason(), resultEntity.getReturnReason());
    }

    @Test
    void completeCompleteInfoExportCollectionRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setRequester(new UserInfo(new User("office", "Office", "OFFICE")));
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.completeCompleteInfoExportCollectionRequest(request, "taskId")).thenReturn(controllerResponse);
        // When
        exportCollectionRequestService.completeCompleteInfoExportCollectionRequest(request, "taskId");
        // Then
        verify(restTemplateWorkService, times(1)).completeCompleteInfoExportCollectionRequest(request, "taskId");
    }

    @Test
    void cancelCompleteInfoExportCollectionRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("office", "Office", "OFFICE"));
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo))).thenReturn(controllerResponse);
        // When
        exportCollectionRequestService.cancelCompleteInfoExportCollectionRequest("taskId", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo));
    }

    @Test
    void getPetitionRequestDetails_whenPassingRequestId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findDetailsExportCollectionRequest(eq("requestId1"), any())).thenReturn(controllerResponse);
        // When
        exportCollectionRequestService.getPetitionRequestDetails("requestId1", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findDetailsExportCollectionRequest(eq("requestId1"), any());
    }
}
