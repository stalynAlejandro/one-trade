package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.controller.RequestController;
import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionOtherOperationsRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionOtherOperationsRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionOtherOperationsRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with endpoints for work with CLE_006: Export Collection Other Operations Request, from external app
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionOtherOperationsRequestService
 * @see ExportCollectionOtherOperationsRequestDtoSerializer
 * @see CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@Validated
@RestController
@RequestMapping("${controller.path}/export-collection-other-operations-request")
public class ExportCollectionOtherOperationsRequestController extends RequestController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionOtherOperationsRequestController.class);

    /**
     * Class attributes
     */
    private final ExportCollectionOtherOperationsRequestService exportCollectionOtherOperationsRequestService;
    private final ExportCollectionOtherOperationsRequestDtoSerializer serializer;
    private final CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer completeInfoSerializer;

    /**
     * Export Collection Other Operations Request Controller Constructor
     * @param exportCollectionOtherOperationsRequestService Service that provides necessary functionality
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param completeInfoSerializer Component for the conversion and adaptation of the data structure
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param officeInfoService Service that provides necessary functionality with office
     */
    public ExportCollectionOtherOperationsRequestController(
            ExportCollectionOtherOperationsRequestService exportCollectionOtherOperationsRequestService,
            ExportCollectionOtherOperationsRequestDtoSerializer serializer,
            CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer completeInfoSerializer,
            UserInfoService userInfoService,
            OfficeInfoService officeInfoService) {

        super(officeInfoService, userInfoService);
        this.exportCollectionOtherOperationsRequestService = exportCollectionOtherOperationsRequestService;
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
    public ControllerResponse performExportCollectionOtherOperationsRequestTask(
            @RequestBody(required = false) ExportCollectionOtherOperationsRequestDto requestDto,
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
    public ControllerResponse getExportCollectionOtherOperationsRequest(
            @RequestParam String type,
            @RequestParam(name = PARAM_CASE_ID, required = false) String caseId,
            @RequestParam(name = PARAM_TASK_ID, required = false) String taskId,
            @RequestParam(name = PARAM_FOR_OPERATION, required = false) boolean forOperation) {
        return getSwitch(this, type, caseId, taskId, forOperation);
    }

    /**
     * This method confirms a request in external app and starts a new other operations request in Flowable Work
     * @param otherOperationsDto a json data object with the necessary info to start the request
     * @return a controller response with a json data object
     */
    @Override
    public ControllerResponse confirm(CommonRequestDto otherOperationsDto) {
        ExportCollectionOtherOperationsRequest request = serializer.toModel(
                (ExportCollectionOtherOperationsRequestDto) otherOperationsDto);
        injectRequestInfo(request);
        ExportCollectionOtherOperationsRequest savedAdvanceModification =
                exportCollectionOtherOperationsRequestService.confirmExportCollectionOtherOperationsRequest(request);
        LOG.debug("confirmExportCollectionOtherOperationsRequest(request: {}): " +
                "Saved export collection other operations : {}", otherOperationsDto, savedAdvanceModification);
        return ControllerResponse.success("confirmExportCollectionOtherOperationsRequest", serializer.
                toDto(savedAdvanceModification));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param forOperation a boolean parameter
     * @return json data object
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId,  boolean forOperation) {
        ControllerResponse result = exportCollectionOtherOperationsRequestService.
                getCompleteInfoExportCollectionOtherOperationsRequest(taskId, forOperation, getUserInfo());
        LOG.debug("getCompleteInfoExportCollectionOtherOperationsRequest(taskId: {}):" +
                " Get export collection other operations complete info : {}", taskId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getCompleteInfoExportCollectionOtherOperationsRequest");
        }
        result.setEntity(completeInfoSerializer.
                toDto((CompleteInfoExportCollectionOtherOperationsRequest) result.getEntity()));
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
        ExportCollectionOtherOperationsRequest request = serializer.toModel(
                (ExportCollectionOtherOperationsRequestDto) requestDto);
        injectRequestInfo(request);
        exportCollectionOtherOperationsRequestService.
                completeCompleteInfoExportCollectionOtherOperationsRequest(request, taskId);
        LOG.debug("completeCompleteInfoExportCollectionOtherOperationsRequest(taskId: {}, requestDto: {}):" +
                " Complete export collection other operations complete info", taskId, requestDto);
        return ControllerResponse.success("completeCompleteInfoExportCollectionOtherOperationsRequest", requestDto);
    }

    /**
     * This method sends a "complete information" task to "cancel request" task
     * @param taskId a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @return a controllerResponse with the task id to cancel
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        exportCollectionOtherOperationsRequestService.
                cancelCompleteInfoExportCollectionOtherOperationsRequest(taskId, getUserInfo());
        LOG.debug("cancelCompleteInfoExportCollectionOtherOperationsRequest(taskId: {}):" +
                " Cancel export collection advance modification complete info", taskId);
        return ControllerResponse.success("cancelCompleteInfoExportCollectionOtherOperationsRequest", taskId);
    }

    /**
     * This method get data from a case (CLE-OTH-XXX)
     * @param requestId a string with the case id (CLE-OTH-XXX)
     * @return json data object
     */
    @Override
    public ControllerResponse getCaseDetails(String requestId) {
        ControllerResponse result = exportCollectionOtherOperationsRequestService.
                getPetitionRequestDetails(requestId, getUserInfo());
        LOG.debug("getPetitionRequestDetails(requestId: {}): Get case info : {}", requestId, result);
        if ("success".equals(result.getType())) {
            result.setKey("getPetitionRequestDetails");
        }
        result.setEntity(completeInfoSerializer.
                toDto((CompleteInfoExportCollectionOtherOperationsRequest) result.getEntity()));
        return result;
    }
}
