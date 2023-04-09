package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceRequestService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for a request of an advance of export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceRequestService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/export-collection-advance-request", produces = "application/json")
public class ExportCollectionAdvanceRequestController {

    //Class attribute
    private final ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService;

    /**
     * constructor method
     * @param exportCollectionAdvanceRequestService object to provide necessary functionality
     */
    public ExportCollectionAdvanceRequestController(
            ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService) {
        this.exportCollectionAdvanceRequestService = exportCollectionAdvanceRequestService;
    }

    /**
     * This method creates a request in external app and starts a new advance request in Flowable Work
     * @param request an ExportCollectionAdvanceRequest object
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse createExportCollectionAdvanceRequest(
            @RequestBody ExportCollectionAdvanceRequest request) {
        request = exportCollectionAdvanceRequestService.createExportCollectionAdvanceRequest(request);
        return ControllerResponse.success("exportCollectionAdvanceRequestCreated", request);
    }

    /**
     * This method updates an advance request of export collection
     * @param exportCollectionAdvanceRequest an ExportCollectionAdvanceRequest object
     * @return a ControllerResponse object
     */
    @PutMapping
    public ControllerResponse updateExportCollectionAdvanceRequest(
            @RequestBody ExportCollectionAdvanceRequest exportCollectionAdvanceRequest) {
        exportCollectionAdvanceRequest = exportCollectionAdvanceRequestService
                .updateExportCollectionAdvanceRequest(exportCollectionAdvanceRequest);
        return ControllerResponse.success("updateExportCollectionAdvanceRequest", exportCollectionAdvanceRequest);
    }

    /**
     * This method confirms an advance request of export collection
     * @param exportCollectionAdvanceRequest an ExportCollectionAdvanceRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/confirm")
    public ControllerResponse confirmExportCollectionAdvanceRequest(
            @RequestBody ExportCollectionAdvanceRequest exportCollectionAdvanceRequest) {
        //TODO: ¡¡¡Enviar el SLA definitivo!!!
        exportCollectionAdvanceRequest = exportCollectionAdvanceRequestService
                .confirmExportCollectionAdvanceRequest(exportCollectionAdvanceRequest);
        return ControllerResponse.success("updateExportCollectionAdvanceRequest", exportCollectionAdvanceRequest);
    }

    /**
     * This method gets an advance request of export collection through a code
     * @param code a string with the code
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/get/{code}")
    public ControllerResponse getExportCollectionAdvanceRequest(
            @PathVariable(name = "code") String code, @RequestBody AuthenticatedRequest authentication) {
        ExportCollectionAdvanceRequest request = exportCollectionAdvanceRequestService
                .getExportCollectionAdvanceRequestByCode(code, authentication.getRequester());
        return ControllerResponse.success("getExportCollectionAdvanceRequest", request);
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/complete-info/get/{taskId}")
    public ControllerResponse getCompleteInfoExportCollectionAdvanceRequest(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest authentication) {
        CompleteInfoExportCollectionAdvanceRequest completeInfoRequest = exportCollectionAdvanceRequestService
                .getCompleteInfoExportCollectionAdvanceRequest(taskId, authentication.getRequester());
        return ControllerResponse.success("getCompleteInfoExportCollectionAdvanceRequest", completeInfoRequest);
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId  a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param request an ExportCollectionAdvanceModificationRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/complete-info/complete/{taskId}")
    public ControllerResponse completeCompleteInfoExportCollectionAdvanceRequest(
            @RequestBody ExportCollectionAdvanceRequest request, @PathVariable String taskId) {
        exportCollectionAdvanceRequestService.completeCompleteInfoExportCollectionAdvanceRequest(request, taskId);
        return ControllerResponse.success("completeCompleteInfoExportCollectionAdvanceRequest", null);
    }

    /**
     * This method get data from a case (CLE-ADV-XXX)
     * @param requestId a string with the case id (CLE-ADV-XXX)
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/details/{requestId}")
    public ControllerResponse getPetitionRequestDetails(
            @PathVariable String requestId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        CompleteInfoExportCollectionAdvanceRequest request = exportCollectionAdvanceRequestService
                .getPetitionRequestDetails(requestId, user);
        return ControllerResponse.success("getPetitionRequestDetails", request);
    }
}
