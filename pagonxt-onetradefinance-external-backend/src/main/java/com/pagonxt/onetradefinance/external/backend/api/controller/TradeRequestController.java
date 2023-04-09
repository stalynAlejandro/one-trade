package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.TradeExternalTaskRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.TradeRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class TradeRequestController extends RequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(TradeRequestController.class);

    private final TradeRequestDtoSerializer serializer;
    private final TradeRequestService tradeRequestService;
    private final TradeExternalTaskRequestDtoSerializer completeInfoSerializer;
    /**
     * Request Controller constructor
     *
     * @param officeInfoService         Service that provides necessary functionality with office
     * @param userInfoService           Service that provides necessary functionality with userInfo
     * @param serializer                Trade Request Serializer
     * @param tradeRequestService       Service that provides necessary functionality with tradeRequest
     * @param completeInfoSerializer    Complete info Serializer
     *
     */
    public TradeRequestController(OfficeInfoService officeInfoService,
                                  UserInfoService userInfoService,
                                  TradeRequestDtoSerializer serializer,
                                  TradeRequestService tradeRequestService,
                                  TradeExternalTaskRequestDtoSerializer completeInfoSerializer) {
        super(officeInfoService, userInfoService);
        this.serializer = serializer;
        this.tradeRequestService = tradeRequestService;
        this.completeInfoSerializer = completeInfoSerializer;
    }

    /**
     * This method confirms a request from external app and starts a new import collection request in Flowable Work
     * @param requestDto json data object
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse confirm(CommonRequestDto requestDto) {
        TradeRequest request = serializer.toModel(requestDto);
        injectTradeRequestInfo(request);
        TradeRequest createdRequest = tradeRequestService.confirmTradeRequest(request);
        LOG.debug("confirm(request: {}): Created request : {}", requestDto, createdRequest);
        return ControllerResponse.success("confirm", serializer.toDto(createdRequest));
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param requestDto json data object
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse completeCompleteInfo(String taskId, CommonRequestDto requestDto) {
        TradeRequest request = serializer.toModel(requestDto);
        injectTradeRequestInfo(request);
        tradeRequestService.completeCompleteInfoTradeRequest(request, taskId);
        LOG.debug("completeCompleteInfo(taskId: {}, requestDto: {}):" +
                " Complete import collection complete info", taskId, requestDto);
        return ControllerResponse.success("completeCompleteInfo", requestDto);
    }

    /**
     * This method sends a "complete information" task to "cancel request" task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @return a controllerResponse with the task id to cancel
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        tradeRequestService.cancelTradeExternalTask(taskId, getUserInfo());
        LOG.debug("cancelCompleteInfo(taskId: {}):" +
                " Cancel import collection complete info", taskId);
        return ControllerResponse.success("cancelCompleteInfo", taskId);
    }

    /**
     * This method gets information from a case or a task in Flowable Work.
     * @param type      : the type of data that we want to retrieve
     * @param caseId   : the case id
     * @param taskId   : the task id
     * @return a controllerResponse with new json data object
     */
    @GetMapping
    @Secured("ROLE_USER")
    public ControllerResponse getImportCollectionRequest(
            @RequestParam String type,
            @RequestParam(name = PARAM_CASE_ID, required = false) String caseId,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId,
            @RequestParam(name = PARAM_FOR_OPERATION, required = false) boolean forOperation) {
        return getSwitch(this, type, caseId, taskId, forOperation);
    }

    /**
     * This method recovers data from a draft for complete an import collection request
     * @param id draft id
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse getDraft(String id) {
        TradeRequest foundDraft = tradeRequestService.getTradeRequest(id, getUserInfo());
        LOG.debug("getDraft(id: {}): Get import collection draft : {}", id, foundDraft);
        return ControllerResponse.success("getDraft", serializer.toDto(foundDraft));
    }

    /**
     * This method get data from a case (MTR-XXX)
     * @param requestId a string with the case id (MTR-XXX)
     * @return json data object
     */
    @Override
    public ControllerResponse getCaseDetails(String requestId) {
        TradeRequest foundTradeRequest = tradeRequestService.getTradeRequest(requestId, getUserInfo());
        LOG.debug("getCaseDetails(id: {}): Get import collection case details : {}", requestId, foundTradeRequest);
        return ControllerResponse.success("getCaseDetails", serializer.toDto(foundTradeRequest));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param forOperation a boolean parameter
     * @return json data object
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId, boolean forOperation) {
        ControllerResponse result = tradeRequestService.getTradeExternalTaskRequest(taskId, forOperation, getUserInfo());
        LOG.debug("getCompleteInfo(taskId: {}): Get import collection complete info : {}", taskId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getCompleteInfo");
        }
        result.setEntity(completeInfoSerializer.toDto((TradeExternalTaskRequest) result.getEntity()));
        return result;
    }
}
