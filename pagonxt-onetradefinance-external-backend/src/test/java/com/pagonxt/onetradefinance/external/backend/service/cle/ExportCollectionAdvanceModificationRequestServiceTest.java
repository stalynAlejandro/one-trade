package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionAdvanceModificationRequestServiceTest {

    @InjectMocks
    private ExportCollectionAdvanceModificationRequestService exportCollectionAdvanceModificationRequestService;
    @Mock
    private RestTemplateWorkService restTemplateWorkService;
    @Mock
    private TaskConcurrencyMonitor taskConcurrencyMonitor;

    @Test
    void confirmExportCollectionAdvanceModificationRequest_whenPassingRequest_thenInvokePost() {
        // Given
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.postExportCollectionAdvanceModificationRequest(any())).thenReturn(controllerResponse);
        // When
        ExportCollectionAdvanceModificationRequest result = exportCollectionAdvanceModificationRequestService.confirmExportCollectionAdvanceModificationRequest(request);
        // Then
        verify(restTemplateWorkService, times(1)).postExportCollectionAdvanceModificationRequest(any());
        assertEquals(code, result.getCode());
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceModificationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceModificationRequest request = new CompleteInfoExportCollectionAdvanceModificationRequest();
        request.setReturnReason("returnReason1");
        request.setReturnComment("returnComment1");
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findCompleteInfoExportCollectionAdvanceModificationRequest(eq("taskId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionAdvanceModificationRequestService.getCompleteInfoExportCollectionAdvanceModificationRequest("taskId1", true, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findCompleteInfoExportCollectionAdvanceModificationRequest(eq("taskId1"), any());
        verify(taskConcurrencyMonitor).generateWarningGetCompleteInfo("taskId1", controllerResponse, true, "User");
        CompleteInfoPagoNxtRequest resultEntity = (CompleteInfoPagoNxtRequest) result.getEntity();
        assertEquals(request.getReturnComment(), resultEntity.getReturnComment());
        assertEquals(request.getReturnReason(), resultEntity.getReturnReason());
    }

    @Test
    void completeCompleteInfoExportCollectionModificationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.completeCompleteInfoExportCollectionAdvanceModificationRequest(request, "taskId")).thenReturn(controllerResponse);
        // When
        exportCollectionAdvanceModificationRequestService.completeCompleteInfoExportCollectionAdvanceModificationRequest(request, "taskId");
        // Then
        verify(restTemplateWorkService, times(1)).completeCompleteInfoExportCollectionAdvanceModificationRequest(request, "taskId");
    }

    @Test
    void cancelCompleteInfoExportCollectionModificationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);

        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo))).thenReturn(controllerResponse);
        // When
        exportCollectionAdvanceModificationRequestService.cancelCompleteInfoExportCollectionAdvanceModificationRequest("taskId", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo));
    }

    @Test
    void getPetitionRequestDetails_whenPassingRequestId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findDetailsExportCollectionAdvanceModificationRequest(eq("requestId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionAdvanceModificationRequestService.getPetitionRequestDetails("requestId1", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findDetailsExportCollectionAdvanceModificationRequest(eq("requestId1"), any());
    }
}
