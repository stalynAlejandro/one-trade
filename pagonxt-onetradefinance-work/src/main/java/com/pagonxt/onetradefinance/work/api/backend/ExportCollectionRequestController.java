package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionRequestService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for a request of export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.ExportCollectionRequestService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/export-collection-request", produces = "application/json")
public class ExportCollectionRequestController {

    //Class attribute
    private final ExportCollectionRequestService exportCollectionRequestService;

    /**
     * constructor method
     * @param exportCollectionRequestService object to provide necessary functionality
     */
    public ExportCollectionRequestController(ExportCollectionRequestService exportCollectionRequestService) {
        this.exportCollectionRequestService = exportCollectionRequestService;
    }

    /**
     * This method creates a request in external app and starts a new request in Flowable Work
     * @param request an ExportCollectionRequest object
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse createExportCollectionRequest(@RequestBody ExportCollectionRequest request) {
        request = exportCollectionRequestService.createExportCollectionRequest(request);
        return ControllerResponse.success("exportCollectionRequestCreated", request);
    }

    /**
     * This method updates a request of export collection
     * @param exportCollectionRequest an ExportCollectionRequest object
     * @return a ControllerResponse object
     */
    @PutMapping
    public ControllerResponse updateExportCollectionRequest(
            @RequestBody ExportCollectionRequest exportCollectionRequest) {
        exportCollectionRequest = exportCollectionRequestService.updateExportCollectionRequest(exportCollectionRequest);
        return ControllerResponse.success("updateExportCollectionRequest", exportCollectionRequest);
    }

    /**
     * This method confirms a request of export collection
     * @param exportCollectionRequest an ExportCollectionRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/confirm")
    public ControllerResponse confirmExportCollectionRequest(
            @RequestBody ExportCollectionRequest exportCollectionRequest) {
        //TODO: ¡¡¡Enviar el SLA definitivo!!!
        exportCollectionRequest = exportCollectionRequestService
                .confirmExportCollectionRequest(exportCollectionRequest);
        return ControllerResponse.success("confirmExportCollectionRequest", exportCollectionRequest);
    }

    /**
     * This method gets a request of export collection through a code
     * @param code a string with the code
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/get/{code}")
    public ControllerResponse getExportCollectionRequest(
            @PathVariable(name = "code") String code, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        ExportCollectionRequest request = exportCollectionRequestService.getExportCollectionRequestByCode(code, user);
        return ControllerResponse.success("getExportCollectionRequest", request);
    }

    /**
     * This method obtains data from a "complete information" task.
     * @param taskId a string with the task id. It's required a "complete information" task.
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/complete-info/get/{taskId}")
    public ControllerResponse getCompleteInfoExportCollectionRequest(
            @PathVariable String taskId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        CompleteInfoExportCollectionRequest completeInfoRequest = exportCollectionRequestService
                .getCompleteInfoExportCollectionRequest(taskId, user);
        return ControllerResponse.success("getCompleteInfoExportCollectionRequest", completeInfoRequest);
    }

    /**
     * This method sends new data to a "complete information" task and returns it to previous task
     * @param taskId  a string with the task id. It's required a "complete information" task. (TSK-.......)
     * @param request an ExportCollectionAdvanceModificationRequest object
     * @return a ControllerResponse object
     */
    @PutMapping("/complete-info/complete/{taskId}")
    public ControllerResponse completeCompleteInfoExportCollectionRequest(
            @RequestBody ExportCollectionRequest request, @PathVariable String taskId) {
        exportCollectionRequestService.completeCompleteInfoExportCollectionRequest(request, taskId);
        return ControllerResponse.success("completeCompleteInfoExportCollectionRequest", null);
    }

    /**
     * This method deletes a request of export collection through a code (NOT IMPLEMENTED YET)
     * @param code a string with the code
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/delete/{code}")
    public ControllerResponse deleteExportCollectionRequest(
            @PathVariable(name = "code") String code, @RequestBody AuthenticatedRequest authentication) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * This method get data from a case (CLE-XXX)
     * @param requestId a string with the case id (CLE-ADV-XXX)
     * @param authentication : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/details/{requestId}")
    public ControllerResponse getPetitionRequestDetails(
            @PathVariable String requestId, @RequestBody AuthenticatedRequest authentication) {
        UserInfo user = authentication.getRequester();
        CompleteInfoExportCollectionRequest request = exportCollectionRequestService
                .getPetitionRequestDetails(requestId, user);
        return ControllerResponse.success("getPetitionRequestDetails", request);
    }
}
