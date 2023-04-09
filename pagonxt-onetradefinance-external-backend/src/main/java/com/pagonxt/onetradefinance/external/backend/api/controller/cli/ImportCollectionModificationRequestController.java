package com.pagonxt.onetradefinance.external.backend.api.controller.cli;

import com.pagonxt.onetradefinance.external.backend.api.controller.TradeRequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoTradeRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.ImportCollectionModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.TradeExternalTaskRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

/**
 * Controller with endpoints for work with CLE_002: Import Collection Modification Request, from external app
 * @author -
 * @version jdk-11.0.13
 * @see TradeRequestService
 * @see ImportCollectionModificationRequestDtoSerializer
 * @see UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/import-collection-modification-request")
public class ImportCollectionModificationRequestController extends TradeRequestController {

    /**
     * Import Collection Request Controller Constructor
     *
     * @param tradeRequestService                       Service that executes trade request actions
     * @param serializer                                Component for the conversion and adaptation of the data structure
     * @param completeInfoTradeRequestDtoSerializer     Serializer for CompleteInfoTradeRequestDtoSerializer object
     * @param userInfoService                           Service that provides necessary functionality with userInfo
     * @param officeInfoService                         Service that provides necessary functionality with office
     */
    public ImportCollectionModificationRequestController(TradeRequestService tradeRequestService,
                                             ImportCollectionModificationRequestDtoSerializer serializer,
                                             CompleteInfoTradeRequestDtoSerializer completeInfoTradeRequestDtoSerializer,
                                             UserInfoService userInfoService,
                                             OfficeInfoService officeInfoService) {
        super(officeInfoService, userInfoService, serializer, tradeRequestService,
                new TradeExternalTaskRequestDtoSerializer(serializer, completeInfoTradeRequestDtoSerializer));
    }

    /**
     * This method performs an action in a task in Flowable Work.
     * @param requestDto    : json data object
     * @param type          : the type of action to perform
     * @param taskId        : the task id
     * @return a controllerResponse with new json data object
     */
    @PutMapping
    @Secured("ROLE_USER")
    public ControllerResponse performImportCollectionModificationRequestTask(
            @RequestBody(required = false) ImportCollectionModificationRequestDto requestDto,
            @RequestParam String type,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId) {
        return putSwitch(this, requestDto, type, taskId);
    }

}
