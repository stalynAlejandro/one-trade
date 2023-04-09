package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.controller.IRequestController;
import com.pagonxt.onetradefinance.external.backend.api.controller.RequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceCancellationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceCancellationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionAdvanceCancellationRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with endpoints for work with CLE_005: Export Collection Advance Cancellation, from external app
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceCancellationRequestService
 * @see ExportCollectionAdvanceCancellationRequestDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/export-collection-advance-cancellation-request")
public class ExportCollectionAdvanceCancellationRequestController extends RequestController implements IRequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger
            (ExportCollectionAdvanceCancellationRequestController.class);

    /**
     * Class attributes
     */
    private final ExportCollectionAdvanceCancellationRequestService exportCollectionAdvanceCancellationRequestService;

    private final ExportCollectionAdvanceCancellationRequestDtoSerializer serializer;

    private final CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer completeInfoSerializer;


    /**
     * Export Collection Advance Cancellation Request Constructor
     * @param exportCollectionAdvanceCancellationRequestService Service that provides necessary functionality
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param completeInfoSerializer Component for the conversion and adaptation of the data structure
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param officeInfoService Service that provides necessary functionality with office
     */
    public ExportCollectionAdvanceCancellationRequestController(
            ExportCollectionAdvanceCancellationRequestService exportCollectionAdvanceCancellationRequestService,
            ExportCollectionAdvanceCancellationRequestDtoSerializer serializer,
            CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer completeInfoSerializer,
            UserInfoService userInfoService,
            OfficeInfoService officeInfoService) {
        super(officeInfoService, userInfoService);
        this.exportCollectionAdvanceCancellationRequestService = exportCollectionAdvanceCancellationRequestService;
        this.serializer = serializer;
        this.completeInfoSerializer = completeInfoSerializer;
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
    public ControllerResponse performExportCollectionAdvanceCancellationRequestTask(
            @RequestBody(required = false) ExportCollectionAdvanceCancellationRequestDto requestDto,
            @RequestParam String type,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId) {
        return putSwitch(this, requestDto, type, taskId);
    }

    /**
     * This method gets information from a case or a task in Flowable Work.
     * @param type     : the type of data that we want to retrieve
     * @param caseId   : the case id
     * @param taskId   : the task id
     * @return a controllerResponse with new json data object
     */
    @GetMapping
    @Secured("ROLE_USER")
    public ControllerResponse getExportCollectionAdvanceCancellationRequest(
            @RequestParam String type,
            @RequestParam(name = PARAM_CASE_ID, required = false) String caseId,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId,
            @RequestParam(name = PARAM_FOR_OPERATION, required = false) boolean forOperation) {
        return getSwitch(this, type, caseId, taskId, forOperation);
    }

    /**
     * This method confirms a request in external app and starts a new advance cancellation request in Flowable Work
     * @param advanceCancellationDto a json data object with the necessary info to start the request
     * @return a controller response with a json data object
     */
     @Override
     public ControllerResponse confirm(CommonRequestDto advanceCancellationDto) {
         ExportCollectionAdvanceCancellationRequest request = serializer.toModel(
                 (ExportCollectionAdvanceCancellationRequestDto) advanceCancellationDto);
         injectRequestInfo(request);
         ExportCollectionAdvanceCancellationRequest savedAdvanceCancellation =
                 exportCollectionAdvanceCancellationRequestService.
                         confirmExportCollectionAdvanceCancellationRequest(request);
         LOG.debug("confirmExportCollectionAdvanceCancellationRequest(request: {}): " +
                 "Saved export collection advance Cancellation : {}", advanceCancellationDto, savedAdvanceCancellation);
         return ControllerResponse.success("confirmExportCollectionAdvanceCancellationRequest",
                 serializer.toDto(savedAdvanceCancellation));
     }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param forOperation a boolean parameter
     * @return json data object
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId, boolean forOperation) {
        ControllerResponse result = exportCollectionAdvanceCancellationRequestService.
                getCompleteInfoExportCollectionAdvanceCancellationRequest(taskId, forOperation, getUserInfo());
        LOG.debug("getCompleteInfoExportCollectionAdvanceCancellationRequest(taskId: {}):" +
                " Get export collection advance Cancellation complete info : {}", taskId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getCompleteInfoExportCollectionAdvanceCancellationRequest");
        }
        result.setEntity(completeInfoSerializer.toDto
                ((CompleteInfoExportCollectionAdvanceCancellationRequest) result.getEntity()));
        return result;
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param requestDto json data object
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse completeCompleteInfo(String taskId, CommonRequestDto requestDto) {
        ExportCollectionAdvanceCancellationRequest request = serializer.toModel(
                (ExportCollectionAdvanceCancellationRequestDto) requestDto);
        injectRequestInfo(request);
        exportCollectionAdvanceCancellationRequestService.
                completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, taskId);
        LOG.debug("completeCompleteInfoExportCollectionAdvanceCancellationRequest(taskId:" +
                " {}, requestDto: {}): Complete export collection advance Cancellation complete info",
                taskId, requestDto);
        return ControllerResponse.success("completeCompleteInfoExportCollectionAdvanceCancellationRequest", requestDto);
    }

    /**
     * This method sends a "complete information" task to "cancel request" task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @return a controllerResponse with the task id to cancel
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        exportCollectionAdvanceCancellationRequestService.
                cancelCompleteInfoExportCollectionAdvanceCancellationRequest(taskId, getUserInfo());
        LOG.debug("cancelCompleteInfoExportCollectionAdvanceCancellationRequest(taskId: {}):" +
                " Cancel export collection advance Cancellation complete info", taskId);
        return ControllerResponse.success("cancelCompleteInfoExportCollectionAdvanceCancellationRequest", taskId);
    }

    /**
     * This method get data from a case (CLE-ADV-CAN-XXX)
     * @param requestId a string with the case id (CLE-ADV-CAN-XXX)
     * @return json data object
     */
    @Override
    public ControllerResponse getCaseDetails(String requestId) {
        ControllerResponse result = exportCollectionAdvanceCancellationRequestService.
                getPetitionRequestDetails(requestId, getUserInfo());
        LOG.debug("getPetitionRequestDetails(requestId: {}): Get case info : {}", requestId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getPetitionRequestDetails");
        }
        result.setEntity(completeInfoSerializer.
                toDto((CompleteInfoExportCollectionAdvanceCancellationRequest) result.getEntity()));
        return result;
    }
}
