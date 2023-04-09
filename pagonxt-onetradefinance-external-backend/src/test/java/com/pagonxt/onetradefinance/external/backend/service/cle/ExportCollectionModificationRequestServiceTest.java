package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionModificationRequestServiceTest {

    @InjectMocks
    private ExportCollectionModificationRequestService exportCollectionModificationRequestService;
    @Mock
    private RestTemplateWorkService restTemplateWorkService;
    @Mock
    private TaskConcurrencyMonitor taskConcurrencyMonitor;

    @Test
    void confirmExportCollectionModificationRequest_whenPassingRequest_thenInvokePost() {
        // Given
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.postExportCollectionModificationRequest(any())).thenReturn(controllerResponse);
        // When
        ExportCollectionModificationRequest result = exportCollectionModificationRequestService.confirmExportCollectionModificationRequest(request);
        // Then
        verify(restTemplateWorkService, times(1)).postExportCollectionModificationRequest(any());
        assertEquals(code, result.getCode());
    }

    @Test
    void getCompleteInfoExportCollectionModificationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionModificationRequest request = new CompleteInfoExportCollectionModificationRequest();
        request.setReturnReason("returnReason1");
        request.setReturnComment("returnComment1");
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findCompleteInfoExportCollectionModificationRequest(eq("taskId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionModificationRequestService.getCompleteInfoExportCollectionModificationRequest("taskId1", true, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findCompleteInfoExportCollectionModificationRequest(eq("taskId1"), any());
        verify(taskConcurrencyMonitor).generateWarningGetCompleteInfo("taskId1", controllerResponse, true, "User");
        CompleteInfoPagoNxtRequest resultEntity = (CompleteInfoPagoNxtRequest) result.getEntity();
        assertEquals(request.getReturnComment(), resultEntity.getReturnComment());
        assertEquals(request.getReturnReason(), resultEntity.getReturnReason());
    }

    @Test
    void completeCompleteInfoExportCollectionModificationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.completeCompleteInfoExportCollectionModificationRequest(request, "taskId")).thenReturn(controllerResponse);
        // When
        exportCollectionModificationRequestService.completeCompleteInfoExportCollectionModificationRequest(request, "taskId");
        // Then
        verify(restTemplateWorkService, times(1)).completeCompleteInfoExportCollectionModificationRequest(request, "taskId");
    }

    @Test
    void cancelCompleteInfoExportCollectionModificationRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);

        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo))).thenReturn(controllerResponse);
        // When
        exportCollectionModificationRequestService.cancelCompleteInfoExportCollectionModificationRequest("taskId", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo));
    }

    @Test
    void getPetitionRequestDetails_whenPassingRequestId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findDetailsExportCollectionModificationRequest(eq("requestId1"), any())).thenReturn(controllerResponse);
        // When
        exportCollectionModificationRequestService.getPetitionRequestDetails("requestId1", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findDetailsExportCollectionModificationRequest(eq("requestId1"), any());
    }
}
