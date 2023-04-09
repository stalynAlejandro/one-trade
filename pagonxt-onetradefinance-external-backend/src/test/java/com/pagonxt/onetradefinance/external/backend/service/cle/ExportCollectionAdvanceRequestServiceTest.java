package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionAdvanceRequestServiceTest {

    @InjectMocks
    private ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService;
    @Mock
    private RestTemplateWorkService restTemplateWorkService;
    @Mock
    private TaskConcurrencyMonitor taskConcurrencyMonitor;

    @Test
    void confirmExportCollectionAdvanceRequest_whenPassingRequest_thenInvokePut() {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.putExportCollectionAdvanceRequestDraft(any())).thenReturn(controllerResponse);
        when(restTemplateWorkService.putExportCollectionAdvanceRequest(any())).thenReturn(controllerResponse);
        // When
        ExportCollectionAdvanceRequest result = exportCollectionAdvanceRequestService.confirmExportCollectionAdvanceRequest(request);
        // Then
        verify(restTemplateWorkService, times(1)).putExportCollectionAdvanceRequestDraft(any());
        verify(restTemplateWorkService, times(1)).putExportCollectionAdvanceRequest(any());
        assertEquals(code, result.getCode());
    }

    @Test
    void createOrUpdateExportCollectionAdvanceRequestDraft_whenPassingDraftWithoutCode_thenInvokePost() {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        ExportCollectionAdvanceRequest responseDraft = new ExportCollectionAdvanceRequest();
        String code = "Code Draft";
        responseDraft.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", responseDraft);
        when(restTemplateWorkService.postExportCollectionAdvanceRequestDraft(request)).thenReturn(controllerResponse);
        // When
        ExportCollectionAdvanceRequest result = exportCollectionAdvanceRequestService.createOrUpdateExportCollectionAdvanceRequestDraft(request);
        // Then
        verify(restTemplateWorkService, times(1)).postExportCollectionAdvanceRequestDraft(request);
        assertEquals(code, result.getCode());
    }

    @Test
    void createOrUpdateExportCollectionAdvanceRequestDraft_whenPassingDraftWithCode_thenInvokePut() {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.putExportCollectionAdvanceRequestDraft(request)).thenReturn(controllerResponse);
        // When
        ExportCollectionAdvanceRequest result = exportCollectionAdvanceRequestService.createOrUpdateExportCollectionAdvanceRequestDraft(request);
        // Then
        verify(restTemplateWorkService, times(1)).putExportCollectionAdvanceRequestDraft(request);
        assertEquals(code, result.getCode());
    }

    @Test
    void getExportCollectionAdvanceRequestDraft_whenPassingDraftId_thenInvokeFind() {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        UserInfo userInfo = mock(UserInfo.class);
        AuthenticatedRequest authentication = new AuthenticatedRequest(userInfo);
        when(restTemplateWorkService.findExportCollectionAdvanceRequestDraft(code, authentication)).thenReturn(controllerResponse);
        // When
        ExportCollectionAdvanceRequest result = exportCollectionAdvanceRequestService.getExportCollectionAdvanceRequestDraft(code, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findExportCollectionAdvanceRequestDraft(code, authentication);
        assertEquals(request.getCode(), result.getCode());
    }


    @Test
    void getCompleteInfoExportCollectionAdvanceRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceRequest request = new CompleteInfoExportCollectionAdvanceRequest();
        request.setReturnReason("returnReason1");
        request.setReturnComment("returnComment1");
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findCompleteInfoExportCollectionAdvanceRequest(eq("taskId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionAdvanceRequestService.getCompleteInfoExportCollectionAdvanceRequest("taskId1", true, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findCompleteInfoExportCollectionAdvanceRequest(eq("taskId1"), any());
        verify(taskConcurrencyMonitor).generateWarningGetCompleteInfo("taskId1", controllerResponse, true, "User");
        CompleteInfoPagoNxtRequest resultEntity = (CompleteInfoPagoNxtRequest) result.getEntity();
        assertEquals(request.getReturnComment(), resultEntity.getReturnComment());
        assertEquals(request.getReturnReason(), resultEntity.getReturnReason());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.completeCompleteInfoExportCollectionAdvanceRequest(request, "taskId")).thenReturn(controllerResponse);
        // When
        exportCollectionAdvanceRequestService.completeCompleteInfoExportCollectionAdvanceRequest(request, "taskId");
        // Then
        verify(restTemplateWorkService, times(1)).completeCompleteInfoExportCollectionAdvanceRequest(request, "taskId");
    }

    @Test
    void cancelCompleteInfoExportCollectionAdvanceRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);

        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo))).thenReturn(controllerResponse);
        // When
        exportCollectionAdvanceRequestService.cancelCompleteInfoExportCollectionAdvanceRequest("taskId", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo));
    }

    @Test
    void getPetitionRequestDetails_whenPassingRequestId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findDetailsExportCollectionAdvanceRequest(eq("requestId1"), any())).thenReturn(controllerResponse);
        // When
        exportCollectionAdvanceRequestService.getPetitionRequestDetails("requestId1", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findDetailsExportCollectionAdvanceRequest(eq("requestId1"), any());
    }
}
