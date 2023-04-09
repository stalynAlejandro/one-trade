package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.controller.IRequestController;
import com.pagonxt.onetradefinance.external.backend.api.controller.RequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionModificationRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionModificationRequest;
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
 * Controller with endpoints for work with CLE_002: Export Collection Modification Request, from external app
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionModificationRequestService
 * @see ExportCollectionModificationRequestDtoSerializer
 * @see CompleteInfoExportCollectionModificationRequestDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/export-collection-modification-request")
public class ExportCollectionModificationRequestController extends RequestController implements IRequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionModificationRequestController.class);

    /**
     * Class attributes
     */
    private final ExportCollectionModificationRequestService exportCollectionModificationRequestService;
    private final ExportCollectionModificationRequestDtoSerializer serializer;
    private final CompleteInfoExportCollectionModificationRequestDtoSerializer completeInfoSerializer;

    /**
     * Export Collection Advance Modification Request Constructor
     * @param exportCollectionModificationRequestService Service that provides necessary functionality
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param completeInfoSerializer Component for the conversion and adaptation of the data structure
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param officeInfoService Service that provides necessary functionality with office
     */
    public ExportCollectionModificationRequestController(
             ExportCollectionModificationRequestService exportCollectionModificationRequestService,
             ExportCollectionModificationRequestDtoSerializer serializer,
             CompleteInfoExportCollectionModificationRequestDtoSerializer completeInfoSerializer,
             UserInfoService userInfoService,
             OfficeInfoService officeInfoService) {
        super(officeInfoService, userInfoService);
        this.exportCollectionModificationRequestService = exportCollectionModificationRequestService;
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
    public ControllerResponse performExportCollectionModificationRequestTask(
            @RequestBody(required = false) ExportCollectionModificationRequestDto requestDto,
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
    public ControllerResponse getExportCollectionModificationRequest(
            @RequestParam String type,
            @RequestParam(name = PARAM_CASE_ID, required = false) String caseId,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId,
            @RequestParam(name = PARAM_FOR_OPERATION, required = false) boolean forOperation) {
        return getSwitch(this, type, caseId, taskId, forOperation);
    }

    /**
     * This method confirms a request in external app and starts a new modification request in Flowable Work
     * @param modificationDto a json data object with the necessary info to start the request
     * @return a controller response with a json data object
     */
     @Override
     public ControllerResponse confirm(CommonRequestDto modificationDto) {
         ExportCollectionModificationRequest request = serializer.toModel(
                 (ExportCollectionModificationRequestDto) modificationDto);
         injectRequestInfo(request);
         ExportCollectionModificationRequest savedModification =
                 exportCollectionModificationRequestService.confirmExportCollectionModificationRequest(request);
         LOG.debug("confirmExportCollectionModificationRequest(request: {}): Saved export collection modification :" +
                 " {}", modificationDto, savedModification);
         return ControllerResponse.success("confirmExportCollectionModificationRequest", serializer.
                 toDto(savedModification));
     }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param forOperation a boolean parameter
     * @return json data object
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId, boolean forOperation) {
        ControllerResponse result = exportCollectionModificationRequestService.
                getCompleteInfoExportCollectionModificationRequest(taskId, forOperation, getUserInfo());
        LOG.debug("getCompleteInfoExportCollectionModificationRequest(taskId: {}): " +
                "Get export collection complete info : {}", taskId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getCompleteInfoExportCollectionModificationRequest");
        }
        result.setEntity(completeInfoSerializer.
                toDto((CompleteInfoExportCollectionModificationRequest) result.getEntity()));
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
        ExportCollectionModificationRequest request = serializer.toModel(
                (ExportCollectionModificationRequestDto) requestDto);
        injectRequestInfo(request);
        exportCollectionModificationRequestService.
                completeCompleteInfoExportCollectionModificationRequest(request, taskId);
        LOG.debug("completeCompleteInfoExportCollectionModificationRequest(taskId: {}, requestDto: {}):" +
                " Complete export collection modification complete info", taskId, requestDto);
        return ControllerResponse.success("completeCompleteInfoExportCollectionModificationRequest", requestDto);
    }

    /**
     * This method sends a "complete information" task to "cancel request" task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @return a controllerResponse with the task id to cancel
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        exportCollectionModificationRequestService.
                cancelCompleteInfoExportCollectionModificationRequest(taskId, getUserInfo());
        LOG.debug("cancelCompleteInfoExportCollectionModificationRequest(taskId: {}):" +
                " Cancel export collection modification complete info", taskId);
        return ControllerResponse.success("cancelCompleteInfoExportCollectionModificationRequest", taskId);
    }

    /**
     * This method get data from a case (CLE-MOD-XXX)
     * @param requestId a string with the case id (CLE-MOD-XXX)
     * @return json data object
     */
    @Override
    public ControllerResponse getCaseDetails(String requestId) {
        ControllerResponse result = exportCollectionModificationRequestService.
                getPetitionRequestDetails(requestId, getUserInfo());
        LOG.debug("getPetitionRequestDetails(requestId: {}): Get case info : {}", requestId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getPetitionRequestDetails");
        }
        result.setEntity(completeInfoSerializer.
                toDto((CompleteInfoExportCollectionModificationRequest) result.getEntity()));
        return result;
    }
}
