package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionAdvanceCancellationRequestServiceTest {

    @InjectMocks
    private ExportCollectionAdvanceCancellationRequestService exportCollectionAdvanceCancellationRequestService;
    @Mock
    private RestTemplateWorkService restTemplateWorkService;
    @Mock
    private TaskConcurrencyMonitor taskConcurrencyMonitor;

    @Test
    void confirmExportCollectionAdvanceCancellationRequest_whenPassingRequest_thenInvokePost() {
        // Given
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.postExportCollectionAdvanceCancellationRequest(any())).thenReturn(controllerResponse);
        // When
        ExportCollectionAdvanceCancellationRequest result = exportCollectionAdvanceCancellationRequestService.confirmExportCollectionAdvanceCancellationRequest(request);
        // Then
        verify(restTemplateWorkService, times(1)).postExportCollectionAdvanceCancellationRequest(any());
        assertEquals(code, result.getCode());
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceCancellationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        request.setReturnReason("returnReason1");
        request.setReturnComment("returnComment1");
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findCompleteInfoExportCollectionAdvanceCancellationRequest(eq("taskId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionAdvanceCancellationRequestService.getCompleteInfoExportCollectionAdvanceCancellationRequest("taskId1", true, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findCompleteInfoExportCollectionAdvanceCancellationRequest(eq("taskId1"), any());
        verify(taskConcurrencyMonitor).generateWarningGetCompleteInfo("taskId1", controllerResponse, true, "User");
        CompleteInfoPagoNxtRequest resultEntity = (CompleteInfoPagoNxtRequest) result.getEntity();
        assertEquals(request.getReturnComment(), resultEntity.getReturnComment());
        assertEquals(request.getReturnReason(), resultEntity.getReturnReason());
    }

    @Test
    void completeCompleteInfoExportCollectionCancellationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, "taskId")).thenReturn(controllerResponse);
        // When
        exportCollectionAdvanceCancellationRequestService.completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, "taskId");
        // Then
        verify(restTemplateWorkService, times(1)).completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, "taskId");
    }

    @Test
    void cancelCompleteInfoExportCollectionCancellationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);

        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo))).thenReturn(controllerResponse);
        // When
        exportCollectionAdvanceCancellationRequestService.cancelCompleteInfoExportCollectionAdvanceCancellationRequest("taskId", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo));
    }

    @Test
    void getPetitionRequestDetails_whenPassingRequestId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findDetailsExportCollectionAdvanceCancellationRequest(eq("requestId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionAdvanceCancellationRequestService.getPetitionRequestDetails("requestId1", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findDetailsExportCollectionAdvanceCancellationRequest(eq("requestId1"), any());
    }
}
