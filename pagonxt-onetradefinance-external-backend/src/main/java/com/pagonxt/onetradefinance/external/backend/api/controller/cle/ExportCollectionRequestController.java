package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.controller.RequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
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
 * Controller with endpoints for work with CLE_001: Export Collection Request, from external app
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionRequestService
 * @see ExportCollectionRequestDtoSerializer
 * @see CompleteInfoExportCollectionRequestDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/export-collection-request")
public class ExportCollectionRequestController extends RequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionRequestController.class);


    /**
     * Class attributes
     */
    private final ExportCollectionRequestService exportCollectionRequestService;

    private final ExportCollectionRequestDtoSerializer serializer;

    private final CompleteInfoExportCollectionRequestDtoSerializer completeInfoSerializer;

    /**
     * Export Collection Request Controller Constructor
     * @param exportCollectionRequestService Service that provides necessary functionality to this controller
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param completeInfoSerializer Component for the conversion and adaptation of the data structure
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param officeInfoService Service that provides necessary functionality with office
     */
    public ExportCollectionRequestController(ExportCollectionRequestService exportCollectionRequestService,
                                             ExportCollectionRequestDtoSerializer serializer,
                                             CompleteInfoExportCollectionRequestDtoSerializer completeInfoSerializer,
                                             UserInfoService userInfoService,
                                             OfficeInfoService officeInfoService) {
        super(officeInfoService, userInfoService);
        this.exportCollectionRequestService = exportCollectionRequestService;
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
    public ControllerResponse saveExportCollectionRequestDraft(@RequestBody ExportCollectionRequestDto draftDto) {
        ExportCollectionRequest request = serializer.toModel(draftDto);
        injectRequestInfo(request);
        ExportCollectionRequest savedDraft =
                exportCollectionRequestService.createOrUpdateExportCollectionRequestDraft(request);
        LOG.debug("saveExportCollectionRequestDraft(request: {}):" +
                " Saved export collection draft : {}", draftDto, savedDraft);
        return ControllerResponse.success("saveExportCollectionRequestDraft", serializer.toDto(savedDraft));
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
    public ControllerResponse performExportCollectionRequestTask(
            @RequestBody(required = false) ExportCollectionRequestDto requestDto,
            @RequestParam String type,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId) {
        return putSwitch(this, requestDto, type, taskId);
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
    public ControllerResponse getExportCollectionRequest(
            @RequestParam String type,
            @RequestParam(name = PARAM_CASE_ID, required = false) String caseId,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId,
            @RequestParam(name = PARAM_FOR_OPERATION, required = false) boolean forOperation) {
        return getSwitch(this, type, caseId, taskId, forOperation);
    }

    /**
     * This method confirms a request from external app and starts a new export collection request in Flowable Work
     * @param requestDto json data object
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse confirm(CommonRequestDto requestDto) {
        ExportCollectionRequest request = serializer.toModel((ExportCollectionRequestDto) requestDto);
        injectRequestInfo(request);
        ExportCollectionRequest createdRequest = exportCollectionRequestService.confirmExportCollectionRequest(request);
        LOG.debug("confirmExportCollectionRequest(request: {}): Created request : {}", requestDto, createdRequest);
        return ControllerResponse.success("confirmExportCollectionRequest", serializer.toDto(createdRequest));
    }

    /**
     * This method recovers data from a draft for complete an export collection request
     * @param id draft id
     * @return a controllerResponse with new json data object
     */
    @Override
    public ControllerResponse getDraft(String id) {
        ExportCollectionRequest foundDraft =
                exportCollectionRequestService.getExportCollectionRequestDraft(id, getUserInfo());
        LOG.debug("getExportCollectionRequestDraft(id: {}): Get export collection draft : {}", id, foundDraft);
        return ControllerResponse.success("findExportCollectionRequestDraft", serializer.toDto(foundDraft));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param forOperation a boolean parameter
     * @return json data object
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId, boolean forOperation) {
        ControllerResponse result = exportCollectionRequestService.
                getCompleteInfoExportCollectionRequest(taskId, forOperation, getUserInfo());
        LOG.debug("getCompleteInfoExportCollectionRequest(taskId: {}):" +
                " Get export collection complete info : {}", taskId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getCompleteInfoExportCollectionRequest");
        }
        result.setEntity(completeInfoSerializer.toDto((CompleteInfoExportCollectionRequest) result.getEntity()));
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
        ExportCollectionRequest request = serializer.toModel((ExportCollectionRequestDto) requestDto);
        injectRequestInfo(request);
        exportCollectionRequestService.completeCompleteInfoExportCollectionRequest(request, taskId);
        LOG.debug("completeCompleteInfoExportCollectionRequest(taskId: {}, requestDto: {}):" +
                " Complete export collection complete info", taskId, requestDto);
        return ControllerResponse.success("completeCompleteInfoExportCollectionRequest", requestDto);
    }

    /**
     * This method sends a "complete information" task to "cancel request" task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @return a controllerResponse with the task id to cancel
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        exportCollectionRequestService.cancelCompleteInfoExportCollectionRequest(taskId, getUserInfo());
        LOG.debug("cancelCompleteInfoExportCollectionRequest(taskId: {}):" +
                " Cancel export collection complete info", taskId);
        return ControllerResponse.success("cancelCompleteInfoExportCollectionRequest", taskId);
    }

    /**
     * This method get data from a case (CLE-XXX)
     * @param requestId a string with the case id (CLE-XXX)
     * @return json data object
     */
    @Override
    public ControllerResponse getCaseDetails(String requestId) {
        ControllerResponse result = exportCollectionRequestService.getPetitionRequestDetails(requestId, getUserInfo());
        LOG.debug("getPetitionRequestDetails(requestId: {}): Get case info :" +
                " {}", requestId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getPetitionRequestDetails");
        }
        result.setEntity(completeInfoSerializer.toDto((CompleteInfoExportCollectionRequest) result.getEntity()));
        return result;
    }
}
