package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.controller.IRequestController;
import com.pagonxt.onetradefinance.external.backend.api.controller.RequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionAdvanceRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionAdvanceRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with endpoints for work with CLE_003: Export Collection Advance Request, from external app
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceRequestService
 * @see ExportCollectionAdvanceRequestDtoSerializer
 * @see CompleteInfoExportCollectionAdvanceRequestDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/export-collection-advance-request")
public class ExportCollectionAdvanceRequestController extends RequestController implements IRequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionAdvanceRequestController.class);

    /**
     * Class attributes
     */
    private final ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService;

    private final ExportCollectionAdvanceRequestDtoSerializer serializer;

    private final CompleteInfoExportCollectionAdvanceRequestDtoSerializer completeInfoSerializer;

    /**
     * Export Collection Advance Request Controller Constructor
     * @param exportCollectionAdvanceRequestService Service that provides necessary functionality to this controller
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param completeInfoSerializer Component for the conversion and adaptation of the data structure
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param officeInfoService Service that provides necessary functionality with office
     */
    public ExportCollectionAdvanceRequestController(
            ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService,
            ExportCollectionAdvanceRequestDtoSerializer serializer,
            CompleteInfoExportCollectionAdvanceRequestDtoSerializer completeInfoSerializer,
            UserInfoService userInfoService,
            OfficeInfoService officeInfoService) {
        super(officeInfoService, userInfoService);
        this.exportCollectionAdvanceRequestService = exportCollectionAdvanceRequestService;
        this.serializer = serializer;
        this.completeInfoSerializer = completeInfoSerializer;
    }

    /**
     * This method save a request not completed from external app and starts a draft task for request in Flowable Work.
     * @param draftDto json data object
     * @return a controllerResponse with new json data object
     */
    @PostMapping
    @Secured("ROLE_USER")
    public ControllerResponse saveExportCollectionAdvanceRequestDraft(
            @RequestBody ExportCollectionAdvanceRequestDto draftDto) {
        ExportCollectionAdvanceRequest request = serializer.toModel(draftDto);
        injectRequestInfo(request);
        ExportCollectionAdvanceRequest savedDraft =
                exportCollectionAdvanceRequestService.createOrUpdateExportCollectionAdvanceRequestDraft(request);
        LOG.debug("saveExportCollectionAdvanceRequestDraft(request: {}):" +
                " Saved export collection advance draft : {}", draftDto, savedDraft);
        return ControllerResponse.success("saveExportCollectionAdvanceRequestDraft", serializer.toDto(savedDraft));
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
    public ControllerResponse performExportCollectionAdvanceRequestTask(
            @RequestBody(required = false) ExportCollectionAdvanceRequestDto requestDto,
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
    public ControllerResponse getExportCollectionAdvanceRequest(
            @RequestParam String type,
            @RequestParam(name = PARAM_CASE_ID, required = false) String caseId,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId,
            @RequestParam(name = PARAM_FOR_OPERATION, required = false) boolean forOperation) {
        return getSwitch(this, type, caseId, taskId, forOperation);
    }

    /**
     * This method confirms a request from external app
     * and starts a new export collection advance request in Flowable Work
     * @param requestDto json data object
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse confirm(CommonRequestDto requestDto) {
        ExportCollectionAdvanceRequest request = serializer.toModel((ExportCollectionAdvanceRequestDto) requestDto);
        injectRequestInfo(request);
        ExportCollectionAdvanceRequest createdRequest =
                exportCollectionAdvanceRequestService.confirmExportCollectionAdvanceRequest(request);
        LOG.debug("confirmExportCollectionAdvanceRequest(request: {}):" +
                " Created request : {}", requestDto, createdRequest);
        return ControllerResponse.success("confirmExportCollectionAdvanceRequest", serializer.toDto(createdRequest));
    }

    /**
     * This method recovers data from a draft for complete an export collection advance request
     * @param id draft id
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse getDraft(String id) {
        ExportCollectionAdvanceRequest foundDraft = exportCollectionAdvanceRequestService.
                getExportCollectionAdvanceRequestDraft(id, getUserInfo());
        LOG.debug("getExportCollectionAdvanceRequestDraft(id: {}):" +
                " Get export collection advance draft : {}", id, foundDraft);
        return ControllerResponse.success("findExportCollectionAdvanceRequestDraft", serializer.toDto(foundDraft));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param forOperation a boolean parameter
     * @return json data object
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId, boolean forOperation) {
        ControllerResponse result = exportCollectionAdvanceRequestService.
                getCompleteInfoExportCollectionAdvanceRequest(taskId, forOperation, getUserInfo());
        LOG.debug("getCompleteInfoExportCollectionAdvanceRequest(taskId: {}):" +
                " Get export collection advance complete info : {}", taskId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getCompleteInfoExportCollectionAdvanceRequest");
        }
        result.setEntity(completeInfoSerializer.toDto((CompleteInfoExportCollectionAdvanceRequest) result.getEntity()));
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
        ExportCollectionAdvanceRequest request = serializer.toModel((ExportCollectionAdvanceRequestDto) requestDto);
        injectRequestInfo(request);
        exportCollectionAdvanceRequestService.completeCompleteInfoExportCollectionAdvanceRequest(request, taskId);
        LOG.debug("completeCompleteInfoExportCollectionAdvanceRequest(taskId: {}, requestDto: {}):" +
                " Complete export collection advance complete info", taskId, requestDto);
        return ControllerResponse.success("completeCompleteInfoExportCollectionAdvanceRequest", requestDto);
    }

    /**
     * This method sends a "complete information" task to "cancel request" task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @return a controllerResponse with the task id to cancel
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        exportCollectionAdvanceRequestService.cancelCompleteInfoExportCollectionAdvanceRequest(taskId, getUserInfo());
        LOG.debug("cancelCompleteInfoExportCollectionAdvanceRequest(taskId: {}): " +
                "Cancel export collection advance complete info", taskId);
        return ControllerResponse.success("cancelCompleteInfoExportCollectionAdvanceRequest", taskId);
    }

    /**
     * This method get data from a case (CLE-ADV-MOD-XXX)
     * @param requestId a string with the case id (CLE-ADV-MOD-XXX)
     * @return json data object
     */
    @Override
    public ControllerResponse getCaseDetails(String requestId) {
        ControllerResponse result = exportCollectionAdvanceRequestService.
                getPetitionRequestDetails(requestId, getUserInfo());
        LOG.debug("getPetitionRequestDetails(requestId: {}): Get case info : {}", requestId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getPetitionRequestDetails");
        }
        result.setEntity(completeInfoSerializer.toDto((CompleteInfoExportCollectionAdvanceRequest) result.getEntity()));
        return result;
    }
}
