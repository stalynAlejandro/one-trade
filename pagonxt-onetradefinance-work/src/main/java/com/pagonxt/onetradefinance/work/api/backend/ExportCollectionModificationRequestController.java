package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionModificationRequestService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for a request to modify an export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.ExportCollectionModificationRequestService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/export-collection-modification-request", produces = "application/json")
public class ExportCollectionModificationRequestController {

    //Class attribute
    private final ExportCollectionModificationRequestService exportCollectionModificationRequestService;

    /**
     * constructor method
     * @param exportCollectionModificationRequestService object to provide necessary functionality
     */
    public ExportCollectionModificationRequestController(
            ExportCollectionModificationRequestService exportCollectionModificationRequestService) {
        this.exportCollectionModificationRequestService = exportCollectionModificationRequestService;
    }

    /**
     * This method confirms a request in external app and starts a new modification request in Flowable Work
     * @param request an ExportCollectionModificationRequest object
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse createExportCollectionModificationRequest(
            @RequestBody ExportCollectionModificationRequest request) {
        return ControllerResponse.success("exportCollectionModificationRequestCreated",
                exportCollectionModificationRequestService.createExportCollectionModificationRequest(request));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/complete-info/get/{taskId}")
    public ControllerResponse getCompleteInfoExportCollectionModificationRequest(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest authentication) {
        CompleteInfoExportCollectionModificationRequest completeInfoRequest = exportCollectionModificationRequestService
                .getCompleteInfoExportCollectionModificationRequest(taskId, authentication.getRequester());
        return ControllerResponse.success("getCompleteInfoExportCollectionModificationRequest", completeInfoRequest);
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId  a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param request an ExportCollectionModificationRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/complete-info/complete/{taskId}")
    public ControllerResponse completeCompleteInfoExportCollectionModificationRequest(
            @RequestBody ExportCollectionModificationRequest request, @PathVariable String taskId) {
        exportCollectionModificationRequestService
                .completeCompleteInfoExportCollectionModificationRequest(request, taskId);
        return ControllerResponse.success("completeCompleteInfoExportCollectionModificationRequest", null);
    }

    /**
     * This method get data from a case (CLE-MOD-XXX)
     * @param requestId a string with the case id (CLE-MOD-XXX)
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/details/{requestId}")
    public ControllerResponse getPetitionRequestDetails(
            @PathVariable String requestId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        CompleteInfoExportCollectionModificationRequest request = exportCollectionModificationRequestService
                .getPetitionRequestDetails(requestId, user);
        return ControllerResponse.success("getPetitionRequestDetails", request);
    }
}
