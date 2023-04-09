package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceModificationRequestService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for a request to modify an advance of an export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceModificationRequestService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/export-collection-advance-modification-request", produces = "application/json")
public class ExportCollectionAdvanceModificationRequestController {

    //Class attribute
    private final ExportCollectionAdvanceModificationRequestService exportCollectionAdvanceModificationRequestService;

    /**
     * constructor method
     * @param exportCollectionAdvanceModificationRequestService object to provide necessary functionality
     */
    public ExportCollectionAdvanceModificationRequestController(
            ExportCollectionAdvanceModificationRequestService exportCollectionAdvanceModificationRequestService) {
        this.exportCollectionAdvanceModificationRequestService = exportCollectionAdvanceModificationRequestService;
    }

    /**
     * This method confirms a request in external app and starts a new advance modification request in Flowable Work
     * @param request an ExportCollectionAdvanceModificationRequest object
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse createExportCollectionAdvanceModificationRequest(
            @RequestBody ExportCollectionAdvanceModificationRequest request) {
        return ControllerResponse.success("exportCollectionAdvanceModificationRequestCreated",
                exportCollectionAdvanceModificationRequestService
                        .createExportCollectionAdvanceModificationRequest(request));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/complete-info/get/{taskId}")
    public ControllerResponse getCompleteInfoExportCollectionAdvanceModificationRequest(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest authentication) {
        CompleteInfoExportCollectionAdvanceModificationRequest completeInfoRequest =
                exportCollectionAdvanceModificationRequestService
                        .getCompleteInfoExportCollectionAdvanceModificationRequest(
                                taskId, authentication.getRequester());
        return ControllerResponse.success("getCompleteInfoExportCollectionAdvanceModificationRequest",
                completeInfoRequest);
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId  a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param request an ExportCollectionAdvanceModificationRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/complete-info/complete/{taskId}")
    public ControllerResponse completeCompleteInfoExportCollectionAdvanceModificationRequest(
            @RequestBody ExportCollectionAdvanceModificationRequest request, @PathVariable String taskId) {
        exportCollectionAdvanceModificationRequestService
                .completeCompleteInfoExportCollectionAdvanceModificationRequest(request, taskId);
        return ControllerResponse.success("completeCompleteInfoExportCollectionAdvanceModificationRequest", null);
    }

    /**
     * This method get data from a case (CLE-ADV-MOD-XXX)
     * @param requestId a string with the case id (CLE-ADV-MOD-XXX)
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/details/{requestId}")
    public ControllerResponse getPetitionRequestDetails(
            @PathVariable String requestId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        CompleteInfoExportCollectionAdvanceModificationRequest request =
                exportCollectionAdvanceModificationRequestService.getPetitionRequestDetails(requestId, user);
        return ControllerResponse.success("getPetitionRequestDetails", request);
    }
}
