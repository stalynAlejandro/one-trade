package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionOtherOperationsRequestServiceTest {

    @InjectMocks
    private ExportCollectionOtherOperationsRequestService exportCollectionOtherOperationsRequestService;
    @Mock
    private RestTemplateWorkService restTemplateWorkService;
    @Mock
    private TaskConcurrencyMonitor taskConcurrencyMonitor;

    @Test
    void confirmExportCollectionOtherOperationsRequest_whenPassingRequest_thenInvokePost() {
        // Given
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.postExportCollectionOtherOperationsRequest(any())).thenReturn(controllerResponse);
        // When
        ExportCollectionOtherOperationsRequest result = exportCollectionOtherOperationsRequestService.confirmExportCollectionOtherOperationsRequest(request);
        // Then
        verify(restTemplateWorkService, times(1)).postExportCollectionOtherOperationsRequest(any());
        assertEquals(code, result.getCode());
    }

    @Test
    void getCompleteInfoExportCollectionOtherOperationsRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionOtherOperationsRequest request = new CompleteInfoExportCollectionOtherOperationsRequest();
        request.setReturnReason("returnReason1");
        request.setReturnComment("returnComment1");
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findCompleteInfoExportCollectionOtherOperationsRequest(eq("taskId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionOtherOperationsRequestService.getCompleteInfoExportCollectionOtherOperationsRequest("taskId1", true, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findCompleteInfoExportCollectionOtherOperationsRequest(eq("taskId1"), any());
        verify(taskConcurrencyMonitor).generateWarningGetCompleteInfo("taskId1", controllerResponse, true, "User");
        CompleteInfoPagoNxtRequest resultEntity = (CompleteInfoPagoNxtRequest) result.getEntity();
        assertEquals(request.getReturnComment(), resultEntity.getReturnComment());
        assertEquals(request.getReturnReason(), resultEntity.getReturnReason());
    }

    @Test
    void completeCompleteInfoExportCollectionOtherOperationsRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.completeCompleteInfoExportCollectionOtherOperationsRequest(request, "taskId")).thenReturn(controllerResponse);
        // When
        exportCollectionOtherOperationsRequestService.completeCompleteInfoExportCollectionOtherOperationsRequest(request, "taskId");
        // Then
        verify(restTemplateWorkService, times(1)).completeCompleteInfoExportCollectionOtherOperationsRequest(request, "taskId");
    }

    @Test
    void cancelCompleteInfoExportCollectionOtherOperationsRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);

        ControllerResponse controllerResponse = ControllerResponse.success("", null);
        when(restTemplateWorkService.cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo))).thenReturn(controllerResponse);
        // When
        exportCollectionOtherOperationsRequestService.cancelCompleteInfoExportCollectionOtherOperationsRequest("taskId", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).cancelCompleteInfoTask("taskId", new AuthenticatedRequest(userInfo));
    }

    @Test
    void getPetitionRequestDetails_whenPassingRequestId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        CompleteInfoExportCollectionOtherOperationsRequest request = new CompleteInfoExportCollectionOtherOperationsRequest();
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findDetailsExportCollectionOtherOperationsRequest(eq("requestId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = exportCollectionOtherOperationsRequestService.getPetitionRequestDetails("requestId1", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findDetailsExportCollectionOtherOperationsRequest(eq("requestId1"), any());
    }
}

