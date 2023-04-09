package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceCancellationRequestService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for a request to cancel an advance of an export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceCancellationRequestService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/export-collection-advance-cancellation-request", produces = "application/json")
public class ExportCollectionAdvanceCancellationRequestController {

    //Class attribute
    private final ExportCollectionAdvanceCancellationRequestService exportCollectionAdvanceCancellationRequestService;

    /**
     * constructor method
     * @param exportCollectionAdvanceCancellationRequestService object to provide necessary functionality
     */
    public ExportCollectionAdvanceCancellationRequestController(
            ExportCollectionAdvanceCancellationRequestService exportCollectionAdvanceCancellationRequestService) {
        this.exportCollectionAdvanceCancellationRequestService = exportCollectionAdvanceCancellationRequestService;
    }


    /**
     * Method to create a request to cancel an advance of an export collection (new case)
     * @param request an ExportCollectionAdvanceCancellationRequest object
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse createExportCollectionAdvanceCancellationRequest(
            @RequestBody ExportCollectionAdvanceCancellationRequest request) {
        return ControllerResponse.success("exportCollectionAdvanceCancellationRequestCreated",
                exportCollectionAdvanceCancellationRequestService.
                        createExportCollectionAdvanceCancellationRequest(request));
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId         : a string with the task id. It's required a "complete information" task.
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/complete-info/get/{taskId}")
    public ControllerResponse getCompleteInfoExportCollectionAdvanceCancellationRequest(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest authentication) {
        CompleteInfoExportCollectionAdvanceCancellationRequest completeInfoRequest =
                exportCollectionAdvanceCancellationRequestService
                        .getCompleteInfoExportCollectionAdvanceCancellationRequest(
                                taskId, authentication.getRequester());
        return ControllerResponse
                .success("getCompleteInfoExportCollectionAdvanceCancellationRequest", completeInfoRequest);
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId  a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param request an ExportCollectionAdvanceCancellationRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/complete-info/complete/{taskId}")
    public ControllerResponse completeCompleteInfoExportCollectionAdvanceCancellationRequest(
            @RequestBody ExportCollectionAdvanceCancellationRequest request, @PathVariable String taskId) {
        exportCollectionAdvanceCancellationRequestService
                .completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, taskId);
        return ControllerResponse.success("completeCompleteInfoExportCollectionAdvanceCancellationRequest", null);
    }

    /**
     * This method get data from a case (CLE-ADV-CAN-XXX)
     * @param requestId a string with the case id (CLE-ADV-CAN-XXX)
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/details/{requestId}")
    public ControllerResponse getPetitionRequestDetails(
            @PathVariable String requestId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        CompleteInfoExportCollectionAdvanceCancellationRequest request =
                exportCollectionAdvanceCancellationRequestService.getPetitionRequestDetails(requestId, user);
        return ControllerResponse.success("getPetitionRequestDetails", request);
    }
}
