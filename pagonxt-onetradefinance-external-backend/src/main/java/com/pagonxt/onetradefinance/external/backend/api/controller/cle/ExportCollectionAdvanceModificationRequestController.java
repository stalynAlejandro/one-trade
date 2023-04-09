package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.controller.IRequestController;
import com.pagonxt.onetradefinance.external.backend.api.controller.RequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionAdvanceModificationRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest;
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
 * Controller with endpoints for work with CLE_004: Export Collection Advance Modification Request, from external app
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceModificationRequestService
 * @see ExportCollectionAdvanceModificationRequestDtoSerializer
 * @see CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see OfficeInfoService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/export-collection-advance-modification-request")
public class ExportCollectionAdvanceModificationRequestController extends RequestController implements IRequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.
            getLogger(ExportCollectionAdvanceModificationRequestController.class);

    /**
     * Class attributes
     */
    private final ExportCollectionAdvanceModificationRequestService exportCollectionAdvanceModificationRequestService;

    private final ExportCollectionAdvanceModificationRequestDtoSerializer serializer;

    private final CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer completeInfoSerializer;

    /**
     * Export Collection Advance Modification Request Constructor
     * @param exportCollectionAdvanceModificationRequestService Service that provides necessary functionality
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param completeInfoSerializer Component for the conversion and adaptation of the data structure
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param officeInfoService Service that provides necessary functionality with office
     */
    public ExportCollectionAdvanceModificationRequestController(
            ExportCollectionAdvanceModificationRequestService exportCollectionAdvanceModificationRequestService,
            ExportCollectionAdvanceModificationRequestDtoSerializer serializer,
            CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer completeInfoSerializer,
            UserInfoService userInfoService,
            OfficeInfoService officeInfoService) {
        super(officeInfoService, userInfoService);
        this.exportCollectionAdvanceModificationRequestService = exportCollectionAdvanceModificationRequestService;
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
    public ControllerResponse performExportCollectionAdvanceModificationRequestTask(
            @RequestBody(required = false) ExportCollectionAdvanceModificationRequestDto requestDto,
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
    public ControllerResponse getExportCollectionAdvanceModificationRequest(
            @RequestParam String type,
            @RequestParam(name = PARAM_CASE_ID, required = false) String caseId,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId,
            @RequestParam(name = PARAM_FOR_OPERATION, required = false) boolean forOperation) {
        return getSwitch(this, type, caseId, taskId, forOperation);
    }

    /**
     * This method confirms a request in external app and starts a new advance modification request in Flowable Work
     * @param advanceModificationDto a json data object with the necessary info to start the request
     * @return a controller response with a json data object
     */
     @Override
     public ControllerResponse confirm(CommonRequestDto advanceModificationDto) {
         ExportCollectionAdvanceModificationRequest request = serializer.toModel(
                 (ExportCollectionAdvanceModificationRequestDto) advanceModificationDto);
         injectRequestInfo(request);
         ExportCollectionAdvanceModificationRequest savedAdvanceModification =
                 exportCollectionAdvanceModificationRequestService.
                         confirmExportCollectionAdvanceModificationRequest(request);
         LOG.debug("confirmExportCollectionAdvanceModificationRequest(request: {}):" +
                 " Saved export collection advance modification : {}",
                 advanceModificationDto, savedAdvanceModification);
         return ControllerResponse.success("confirmExportCollectionAdvanceModificationRequest",
                 serializer.toDto(savedAdvanceModification));
     }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param forOperation a boolean parameter
     * @return json data object
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId, boolean forOperation) {
        ControllerResponse result = exportCollectionAdvanceModificationRequestService.
                getCompleteInfoExportCollectionAdvanceModificationRequest(taskId, forOperation, getUserInfo());
        LOG.debug("getCompleteInfoExportCollectionAdvanceModificationRequest(taskId: {}):" +
                " Get export collection advance modification complete info : {}", taskId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getCompleteInfoExportCollectionAdvanceModificationRequest");
        }
        result.setEntity(completeInfoSerializer.
                toDto((CompleteInfoExportCollectionAdvanceModificationRequest) result.getEntity()));
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
        ExportCollectionAdvanceModificationRequest request = serializer.toModel(
                (ExportCollectionAdvanceModificationRequestDto) requestDto);
        injectRequestInfo(request);
        exportCollectionAdvanceModificationRequestService.
                completeCompleteInfoExportCollectionAdvanceModificationRequest(request, taskId);
        LOG.debug("completeCompleteInfoExportCollectionAdvanceModificationRequest(taskId: {}, requestDto: {}):" +
                " Complete export collection advance modification complete info", taskId, requestDto);
        return ControllerResponse.success("completeCompleteInfoExportCollectionAdvanceModificationRequest", requestDto);
    }

    /**
     * This method sends a "complete information" task to "cancel request" task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @return a controllerResponse with the task id to cancel
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        exportCollectionAdvanceModificationRequestService.
                cancelCompleteInfoExportCollectionAdvanceModificationRequest(taskId, getUserInfo());
        LOG.debug("cancelCompleteInfoExportCollectionAdvanceModificationRequest(taskId: " +
                "{}): Cancel export collection advance modification complete info", taskId);
        return ControllerResponse.success("cancelCompleteInfoExportCollectionAdvanceModificationRequest", taskId);
    }

    /**
     * This method get data from a case (CLE-ADV-MOD-XXX)
     * @param requestId a string with the case id (CLE-ADV-MOD-XXX)
     * @return json data object
     */
    @Override
    public ControllerResponse getCaseDetails(String requestId) {
        ControllerResponse result = exportCollectionAdvanceModificationRequestService.
                getPetitionRequestDetails(requestId, getUserInfo());
        LOG.debug("getPetitionRequestDetails(requestId: {}): Get case info : {}", requestId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getPetitionRequestDetails");
        }
        result.setEntity(completeInfoSerializer.
                toDto((CompleteInfoExportCollectionAdvanceModificationRequest) result.getEntity()));
        return result;
    }
}
