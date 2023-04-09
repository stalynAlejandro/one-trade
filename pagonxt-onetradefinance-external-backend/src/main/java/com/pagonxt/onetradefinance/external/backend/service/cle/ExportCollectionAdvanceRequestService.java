package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Service;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @see com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor
 * @since jdk-11.0.13
 */
@Service
public class ExportCollectionAdvanceRequestService {

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;
    private final TaskConcurrencyMonitor taskConcurrencyMonitor;

    /**
     * Class variables
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     * @param taskConcurrencyMonitor TaskConcurrencyMonitor object
     */
    public ExportCollectionAdvanceRequestService(RestTemplateWorkService restTemplateWorkService,
                                                 TaskConcurrencyMonitor taskConcurrencyMonitor) {
        this.restTemplateWorkService = restTemplateWorkService;
        this.taskConcurrencyMonitor = taskConcurrencyMonitor;
    }

    /**
     * Method used when we confirm a request for advance of the export collection
     * @param request object with the data of the request
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ExportCollectionAdvanceRequest object with data mapped
     */
    public ExportCollectionAdvanceRequest confirmExportCollectionAdvanceRequest(
            ExportCollectionAdvanceRequest request) {
        ExportCollectionAdvanceRequest savedDraft = this.createOrUpdateExportCollectionAdvanceRequestDraft(request);
        ControllerResponse response = restTemplateWorkService.putExportCollectionAdvanceRequest(savedDraft);
        return mapExportCollectionAdvanceRequest(response);
    }

    /**
     * Method used when we create a draft or update a draft of a request for advance of the export collection
     * @param request object with the data of the request
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ExportCollectionAdvanceRequest object with data mapped
     */
    public ExportCollectionAdvanceRequest createOrUpdateExportCollectionAdvanceRequestDraft(
            ExportCollectionAdvanceRequest request) {
        String code = request.getCode();
        ControllerResponse response;
        if (Strings.isBlank(code)) {
            response = restTemplateWorkService.postExportCollectionAdvanceRequestDraft(request);
        } else {
            response = restTemplateWorkService.putExportCollectionAdvanceRequestDraft(request);
        }
        return mapExportCollectionAdvanceRequest(response);
    }

    /**
     * Method used when we need to get data from a draft of a request for advance of the export collection
     * @param id the request id
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ExportCollectionAdvanceRequest object with data mapped
     */
    public ExportCollectionAdvanceRequest getExportCollectionAdvanceRequestDraft(String id, UserInfo userInfo) {
        ControllerResponse response = restTemplateWorkService.
                findExportCollectionAdvanceRequestDraft(id, new AuthenticatedRequest(userInfo));
        return mapExportCollectionAdvanceRequest(response);
    }


    /**
     * Method used when need to get data from a complete information task
     * @param taskId the task id
     * @param forOperation boolean value
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return ControllerResponse object
     */
    public ControllerResponse getCompleteInfoExportCollectionAdvanceRequest(String taskId,
                                                                            boolean forOperation,
                                                                            UserInfo userInfo) {
        ControllerResponse result = restTemplateWorkService
                .findCompleteInfoExportCollectionAdvanceRequest(taskId, new AuthenticatedRequest(userInfo));
        taskConcurrencyMonitor.generateWarningGetCompleteInfo(taskId,
                result, forOperation, userInfo.getUser().getUserDisplayedName());
        result.setEntity(mapCompleteInfoExportCollectionAdvanceRequest(result));
        return result;
    }

    /**
     * Method used when we finish a complete information task and can continue with the request in work
     * @param request object with the data of the request
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     */
    public void completeCompleteInfoExportCollectionAdvanceRequest(ExportCollectionAdvanceRequest request,
                                                                   String taskId) {
        restTemplateWorkService.completeCompleteInfoExportCollectionAdvanceRequest(request, taskId);
    }

    /**
     * Method used when we must cancel a complete information task.
     * @param taskId the task id
     * @param request UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void cancelCompleteInfoExportCollectionAdvanceRequest(String taskId, UserInfo request) {
        restTemplateWorkService.cancelCompleteInfoTask(taskId, new AuthenticatedRequest(request));
    }

    /**
     * Method to obtain request details
     * @param requestId the request id (case code)
     * @param userInfo UserInfo object
     * @return Controller Response object
     */
    public ControllerResponse getPetitionRequestDetails(String requestId, UserInfo userInfo) {
        ControllerResponse result = restTemplateWorkService
                .findDetailsExportCollectionAdvanceRequest(requestId, new AuthenticatedRequest(userInfo));
        result.setEntity(mapCompleteInfoExportCollectionAdvanceRequest(result));
        return result;
    }

    /**
     * Method to map data of a request
     * @param response Controller Response object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ExportCollectionAdvanceRequest object with data mapped
     */
    private ExportCollectionAdvanceRequest mapExportCollectionAdvanceRequest(ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(), ExportCollectionAdvanceRequest.class);
    }

    /**
     * Method to map data of a complete information request
     * @param response Controller Response object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return CompleteInfoExportCollectionAdvanceRequest object with data mapped
     */
    private CompleteInfoExportCollectionAdvanceRequest mapCompleteInfoExportCollectionAdvanceRequest(
            ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(), CompleteInfoExportCollectionAdvanceRequest.class);
    }
}
