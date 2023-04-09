package com.pagonxt.onetradefinance.external.backend.api.controller.cli;

import com.pagonxt.onetradefinance.external.backend.api.controller.TradeRequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoTradeRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.ImportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.TradeExternalTaskRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * Controller with endpoints for work trade: Import Collection Request, from external app
 * @author -
 * @version jdk-11.0.13
 * @see TradeRequestService
 * @see ImportCollectionRequestDtoSerializer
 * @see UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/import-collection-request")
public class ImportCollectionRequestController extends TradeRequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImportCollectionRequestController.class);

    /**
     * Class attributes
     */
    private final TradeRequestService tradeRequestService;
    private final ImportCollectionRequestDtoSerializer serializer;

    /**
     * Import Collection Request Controller Constructor
     *
     * @param tradeRequestService       Service that executes trade request actions
     * @param serializer                Component for the conversion and adaptation of the data structure
     * @param completeInfoTradeRequestDtoSerializer    Serializer for CompleteInfoTradeRequestDtoSerializer object
     * @param userInfoService           Service that provides necessary functionality with userInfo
     * @param officeInfoService         Service that provides necessary functionality with office
     */
    public ImportCollectionRequestController(TradeRequestService tradeRequestService,
                                             ImportCollectionRequestDtoSerializer serializer,
                                             CompleteInfoTradeRequestDtoSerializer completeInfoTradeRequestDtoSerializer,
                                             UserInfoService userInfoService,
                                             OfficeInfoService officeInfoService) {
        super(officeInfoService, userInfoService, serializer, tradeRequestService,
                new TradeExternalTaskRequestDtoSerializer(serializer, completeInfoTradeRequestDtoSerializer));
        this.tradeRequestService = tradeRequestService;
        this.serializer = serializer;
    }

    /**
     * This method save a request not completed from external app and starts a draft task for request in Flowable Work.
     * @param draftDto json data object
     * @return a controllerResponse with new json data object
     */
    @PostMapping
    @Secured("ROLE_USER")
    public ControllerResponse saveImportCollectionRequestDraft(@RequestBody ImportCollectionRequestDto draftDto) {
        TradeRequest request = serializer.toModel(draftDto);
        injectTradeRequestInfo(request);
        TradeRequest savedDraft = tradeRequestService.createOrUpdateTradeRequestDraft(request);
        LOG.debug("saveImportCollectionRequestDraft(request: {}):" +
                " Saved import collection draft : {}", draftDto, savedDraft);
        return ControllerResponse.success("saveImportCollectionRequestDraft", serializer.toDto(savedDraft));
    }

    /**
     * This method performs an action in a task in Flowable Work.
     * @param requestDto  : json data object
     * @param type      : the type of action to perform
     * @param taskId   : the task id
     * @return a controllerResponse with new json data object
     */
    @PutMapping
    @Secured("ROLE_USER")
    public ControllerResponse performImportCollectionRequestTask(
            @RequestBody(required = false) ImportCollectionRequestDto requestDto,
            @RequestParam String type,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId) {
        return putSwitch(this, requestDto, type, taskId);
    }

}
