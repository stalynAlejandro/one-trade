package com.pagonxt.onetradefinance.external.backend.service.trade;

import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class TradeRequestServiceTest {

    @InjectMocks
    private TradeRequestService tradeRequestService;
    @Mock
    private RestTemplateWorkService restTemplateWorkService;
    @Mock
    private TaskConcurrencyMonitor taskConcurrencyMonitor;


    @Test
    void createOrUpdateTradeRequestDraft_whenPassingDraftWithoutCode_thenInvokePost() {
        // Given
        TradeRequest request = new TradeRequest();
        TradeRequest responseDraft = new TradeRequest();
        String code = "Code Draft";
        responseDraft.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", responseDraft);
        when(restTemplateWorkService.postTradeRequestDraft(request)).thenReturn(controllerResponse);
        when(restTemplateWorkService.findTradeRequest(eq(code), any())).thenReturn(controllerResponse);
        // When
        TradeRequest result = tradeRequestService.createOrUpdateTradeRequestDraft(request);
        // Then
        verify(restTemplateWorkService, times(1)).postTradeRequestDraft(request);
        assertEquals(code, result.getCode());
    }

    @Test
    void createOrUpdateTradeRequestDraft_whenPassingDraftWithCode_thenInvokePut() {
        // Given
        TradeRequest request = new TradeRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.putTradeRequestDraft(request)).thenReturn(controllerResponse);
        when(restTemplateWorkService.findTradeRequest(eq(code), any())).thenReturn(controllerResponse);
        // When
        TradeRequest result = tradeRequestService.createOrUpdateTradeRequestDraft(request);
        // Then
        verify(restTemplateWorkService, times(1)).putTradeRequestDraft(request);
        assertEquals(code, result.getCode());
    }

    @Test
    void confirmTradeRequest_whenPassingRequest_thenInvokePut() {
        // Given
        TradeRequest request = new TradeRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.putTradeRequestDraft(any())).thenReturn(controllerResponse);
        when(restTemplateWorkService.findTradeRequest(eq(code), any())).thenReturn(controllerResponse);
        // When
        TradeRequest result = tradeRequestService.confirmTradeRequest(request);
        // Then
        verify(restTemplateWorkService, times(1)).putTradeRequestDraft(any());
        verify(restTemplateWorkService, times(1)).putTradeRequest(eq("Code Draft"), any());
        assertEquals(code, result.getCode());
    }

    @Test
    void completeCompleteInfoTradeRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        TradeRequest request = new TradeRequest();
        // When
        tradeRequestService.completeCompleteInfoTradeRequest(request, "taskId");
        // Then
        verify(restTemplateWorkService, times(1)).completeCompleteInfoTradeRequest(request, "taskId");
    }

    @Test
    void cancelTradeExternalTask_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("office", "Office", "OFFICE"));
        // When
        tradeRequestService.cancelTradeExternalTask("taskId", userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).cancelTradeExternalTask("taskId", new AuthenticatedRequest(userInfo));
    }

    @Test
    void getTradeRequest_whenPassingDraftId_thenInvokeFind() {
        // Given
        TradeRequest request = new TradeRequest();
        String code = "Code Draft";
        request.setCode(code);
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findTradeRequest(eq(code), any())).thenReturn(controllerResponse);
        // When
        TradeRequest result = tradeRequestService.getTradeRequest(code, new UserInfo());
        // Then
        verify(restTemplateWorkService, times(1)).findTradeRequest(eq(code), any());
        assertEquals(request.getCode(), result.getCode());
    }

    @Test
    void getTradeExternalTaskRequest_whenPassingTaskId_thenInvokeFind() {
        // Given
        UserInfo userInfo = new UserInfo(new User("user", "User", "USER"));
        TradeExternalTaskRequest request = new TradeExternalTaskRequest();
        request.setReturnReason("returnReason1");
        request.setReturnComment("returnComment1");
        ControllerResponse controllerResponse = ControllerResponse.success("", request);
        when(restTemplateWorkService.findTradeExternalTaskRequest(eq("taskId1"), any())).thenReturn(controllerResponse);
        // When
        ControllerResponse result = tradeRequestService.getTradeExternalTaskRequest("taskId1", true, userInfo);
        // Then
        verify(restTemplateWorkService, times(1)).findTradeExternalTaskRequest(eq("taskId1"), any());
        verify(taskConcurrencyMonitor).generateWarningGetCompleteInfo("taskId1", controllerResponse, true, "User");
        TradeExternalTaskRequest resultEntity = (TradeExternalTaskRequest) result.getEntity();
        assertEquals(request.getReturnComment(), resultEntity.getReturnComment());
        assertEquals(request.getReturnReason(), resultEntity.getReturnReason());
    }
}
