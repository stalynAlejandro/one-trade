package com.pagonxt.onetradefinance.external.backend.service.trade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Service;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see RestTemplateWorkService
 * @see TaskConcurrencyMonitor
 * @since jdk-11.0.13
 */
@Service
public class TradeRequestService {

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;
    private final TaskConcurrencyMonitor taskConcurrencyMonitor;

    /**
     * Class variables
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Class constructor
     *
     * @param restTemplateWorkService   the Rest Template Work Service
     * @param taskConcurrencyMonitor    the Task Concurrency Monitor
     */
    public TradeRequestService(RestTemplateWorkService restTemplateWorkService, TaskConcurrencyMonitor taskConcurrencyMonitor) {
        this.restTemplateWorkService = restTemplateWorkService;
        this.taskConcurrencyMonitor = taskConcurrencyMonitor;
    }

    /**
     * Method used when we confirm a request of the trade request
     * @param request object with the data of the request
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     * @return TradeRequest object with data mapped
     */
    public TradeRequest confirmTradeRequest(TradeRequest request) {
        TradeRequest savedDraft = this.createOrUpdateTradeRequestDraft(request);
        restTemplateWorkService.putTradeRequest(savedDraft.getCode(), new AuthenticatedRequest(request.getRequester()));
        return savedDraft;
    }

    /**
     * Method used when we create a draft or update a draft of a trade request
     * @param request object with the data of the request
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     * @return TradeRequest object with data mapped
     */
    public TradeRequest createOrUpdateTradeRequestDraft(TradeRequest request) {
        String code = request.getCode();
        ControllerResponse response;
        if (Strings.isBlank(code)) {
            response = restTemplateWorkService.postTradeRequestDraft(request);
        } else {
            response = restTemplateWorkService.putTradeRequestDraft(request);
        }
        return getTradeRequest(mapTradeRequest(response).getCode(), request.getRequester());
    }

    /**
     * Method used when we finish a complete information task and can continue with the request in work
     * @param request object with the data of the request
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     */
    public void completeCompleteInfoTradeRequest(TradeRequest request, String taskId) {
        restTemplateWorkService.completeCompleteInfoTradeRequest(request, taskId);
    }

    /**
     * Method used when we must cancel a complete information task.
     * @param taskId the task id
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void cancelTradeExternalTask(String taskId, UserInfo userInfo) {
        restTemplateWorkService.cancelTradeExternalTask(taskId, new AuthenticatedRequest(userInfo));
    }

    /**
     * Method used when we need to get data from a draft of a trade request
     * @param id the request id
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     * @return TradeRequest object with data mapped
     */
    public TradeRequest getTradeRequest(String id, UserInfo userInfo) {
        ControllerResponse response = restTemplateWorkService
                .findTradeRequest(id, new AuthenticatedRequest(userInfo));
        return mapTradeRequest(response);
    }

    /**
     * Method used when need to get data from a complete information task
     * @param taskId the task id
     * @param forOperation boolean value
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return ControllerResponse object
     */
    public ControllerResponse getTradeExternalTaskRequest(String taskId,
                                                          boolean forOperation,
                                                          UserInfo userInfo) {
        ControllerResponse result = restTemplateWorkService
                .findTradeExternalTaskRequest(taskId, new AuthenticatedRequest(userInfo));
        taskConcurrencyMonitor.generateWarningGetCompleteInfo(taskId,
                result, forOperation, userInfo.getUser().getUserDisplayedName());
        result.setEntity(mapTradeExternalTaskRequest(result));
        return result;
    }

    /**
     * Method to map data of a request
     * @param response Controller Response object
     * @see ControllerResponse
     * @see TradeRequest
     * @return TradeRequest object with data mapped
     */
    private TradeRequest mapTradeRequest(ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(), TradeRequest.class);
    }

    /**
     * Method to map data of a external task
     * @param response Controller Response object
     * @see ControllerResponse
     * @see TradeExternalTaskRequest
     * @return TradeExternalTaskRequest object with data mapped
     */
    private TradeExternalTaskRequest mapTradeExternalTaskRequest(ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(), TradeExternalTaskRequest.class);
    }
}
