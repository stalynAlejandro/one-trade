package com.pagonxt.onetradefinance.work.api.backend.trade;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContractsQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.work.service.TaskOperationService;
import com.pagonxt.onetradefinance.work.service.trade.TradeRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for MiniTrade
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.trade.TradeRequestService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("/backend/trade-request")
public class TradeRequestController {

    //member variable
    private final TradeRequestService tradeRequestService;

    /**
     * Constructor Method
     *
     * @param tradeRequestService  : a TradeRequestService object
     * @param taskOperationService : a {@link TaskOperationService} object
     */
    public TradeRequestController(TradeRequestService tradeRequestService, TaskOperationService taskOperationService) {
        this.tradeRequestService = tradeRequestService;
    }

    /**
     * Create a case draft in MiniTrade
     * @param request a TradeRequest object with request data
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse createTradeRequest(@RequestBody TradeRequest request) {
        request = tradeRequestService.createTradeRequest(request);
        return ControllerResponse.success("tradeRequestCreated", request);
    }

    /**
     * Update a case draft in MiniTrade
     * @param request a TradeRequest object with request data
     * @return a ControllerResponse object
     */
    @PutMapping
    public ControllerResponse updateTradeRequest(
            @RequestBody TradeRequest request) {
        request = tradeRequestService.updateTradeRequest(request);
        return ControllerResponse.success("tradeRequestUpdated", request);
    }

    /**
     * Confirm a draft in MiniTrade
     * @param code              : a String with the draft code
     * @param authentication    : a {@link AuthenticatedRequest} object with the requester info
     * @return                  : a {@link ControllerResponse} object
     */
    @PutMapping("/{code}/confirm")
    public ControllerResponse confirmTradeRequest(
            @PathVariable(name = "code") String code, @RequestBody AuthenticatedRequest authentication) {
        tradeRequestService.confirmTradeRequest(code, authentication);
        return ControllerResponse.success("tradeRequestConfirmed", null);
    }

    /**
     * Get data form a case by code
     * @param code          : a String object with the case code
     * @param authentication an AuthenticatedRequest object
     * @return a ControllerResponse object
     */
    @PostMapping("/get/{code}")
    public ControllerResponse getTradeRequest(
            @PathVariable(name = "code") String code, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        TradeRequest request = tradeRequestService.getRequestByCode(code, user);
        return ControllerResponse.success("getTradeRequest", request);
    }

    /**
     * Get task detail by task id
     * @param taskId        : a String object with the task id
     * @param authentication an AuthenticatedRequest object
     * @return a ControllerResponse object
     */
    @PostMapping("/tasks/{taskId}")
    public ControllerResponse getTradeExternalTaskById(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        TradeExternalTaskRequest request = tradeRequestService.getTradeExternalTaskByTaskId(taskId, user);
        return ControllerResponse.success("getTradeExternalTaskById", request);
    }

    /**
     * get contracts by contract type
     * @param contractType          : a Stirng with the contract type
     * @param tradeContractQuery    : a {@link TradeContractsQuery}
     * @return                      : a {@link ControllerResponse}
     */
    @PostMapping("/contracts/{contractType}/search")
    public ControllerResponse searchContractsByContractType(@PathVariable(name = "contractType") String contractType,
            @RequestBody TradeContractsQuery tradeContractQuery) {
        List<TradeContract> request = tradeRequestService.getContracts(contractType, tradeContractQuery);
        return ControllerResponse.success("searchContractsByContractType", request);
    }
    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId  a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param request an {@link TradeRequest} object
     * @return a ControllerResponse object
     */
    @PutMapping("/tasks/{taskId}/complete-info")
    public ControllerResponse externalTaskCompleteInfo(
            @RequestBody TradeRequest request, @PathVariable String taskId) {
        tradeRequestService.externalTaskCompleteInfo(request,taskId);
        return ControllerResponse.success("externalTaskCompleteInfo", null);
    }

    /**
     * Method to cancel a complete info task
     * @param taskId   : a string with the task id. It's required a "complete information" task.
     * @param userInfo : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PutMapping("/tasks/{taskId}/request-cancellation")
    public ControllerResponse externalTaskRequestCancellation(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest userInfo) {
        tradeRequestService.externalTaskRequestCancellation(taskId, userInfo);
        return ControllerResponse.success("externalTaskRequestCancellation", null);
    }
}
