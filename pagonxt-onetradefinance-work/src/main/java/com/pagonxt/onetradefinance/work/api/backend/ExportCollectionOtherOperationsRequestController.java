package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionOtherOperationsRequestService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for a request of other operations related with an export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.ExportCollectionOtherOperationsRequestService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/export-collection-other-operations-request", produces = "application/json")
public class ExportCollectionOtherOperationsRequestController {

    //Class attribute
    private final ExportCollectionOtherOperationsRequestService exportCollectionOtherOperationsRequestService;

    /**
     * constructor method
     * @param exportCollectionOtherOperationsRequestService object to provide necessary functionality
     */
    public ExportCollectionOtherOperationsRequestController(
            ExportCollectionOtherOperationsRequestService exportCollectionOtherOperationsRequestService) {
        this.exportCollectionOtherOperationsRequestService = exportCollectionOtherOperationsRequestService;
    }

    /**
     * This method confirms a request in external app and starts a new operation request in Flowable Work
     * @param request an ExportCollectionOtherOperationsRequest object
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse createExportCollectionOtherOperationsRequest(
            @RequestBody ExportCollectionOtherOperationsRequest request) {
        return ControllerResponse.success("exportCollectionOtherOperationsRequestCreated",
                exportCollectionOtherOperationsRequestService.createExportCollectionOtherOperationsRequest(request));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/complete-info/get/{taskId}")
    public ControllerResponse getCompleteInfoExportCollectionOtherOperationsRequest(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest authentication) {
        CompleteInfoExportCollectionOtherOperationsRequest completeInfoRequest =
                exportCollectionOtherOperationsRequestService.getCompleteInfoExportCollectionOtherOperationsRequest(
                        taskId, authentication.getRequester());
        return ControllerResponse.success("getCompleteInfoExportCollectionOtherOperationsRequest", completeInfoRequest);
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId  a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param request an ExportCollectionOtherOperationsRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/complete-info/complete/{taskId}")
    public ControllerResponse completeCompleteInfoExportCollectionOtherOperationsRequest(
            @RequestBody ExportCollectionOtherOperationsRequest request, @PathVariable String taskId) {
        exportCollectionOtherOperationsRequestService
                .completeCompleteInfoExportCollectionOtherOperationsRequest(request, taskId);
        return ControllerResponse.success("completeCompleteInfoExportCollectionOtherOperationsRequest", null);
    }

    /**
     * This method get data from a case (CLE-OTH-XXX)
     * @param requestId a string with the case id (CLE-OTH-XXX)
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/details/{requestId}")
    public ControllerResponse getPetitionRequestDetails(
            @PathVariable String requestId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        CompleteInfoExportCollectionOtherOperationsRequest request = exportCollectionOtherOperationsRequestService
                .getPetitionRequestDetails(requestId, user);
        return ControllerResponse.success("getPetitionRequestDetails", request);
    }
}
