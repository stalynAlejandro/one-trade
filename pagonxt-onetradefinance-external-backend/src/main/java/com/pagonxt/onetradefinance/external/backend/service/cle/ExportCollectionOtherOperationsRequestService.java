package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest;
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
public class ExportCollectionOtherOperationsRequestService {

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
    public ExportCollectionOtherOperationsRequestService(RestTemplateWorkService restTemplateWorkService,
                                                         TaskConcurrencyMonitor taskConcurrencyMonitor) {
        this.restTemplateWorkService = restTemplateWorkService;
        this.taskConcurrencyMonitor = taskConcurrencyMonitor;
    }

    /**
     * Method used when we confirm a request for other operations of the export collection
     * @param request object with the data of the request
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     * @return ExportCollectionOtherOperationsRequest object with data mapped
     */
    public ExportCollectionOtherOperationsRequest confirmExportCollectionOtherOperationsRequest(
            ExportCollectionOtherOperationsRequest request) {
        ControllerResponse response = restTemplateWorkService.postExportCollectionOtherOperationsRequest(request);
        return mapExportCollectionOtherOperationsRequest(response);
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
    public ControllerResponse getCompleteInfoExportCollectionOtherOperationsRequest(String taskId,
                                                                                    boolean forOperation,
                                                                                    UserInfo userInfo) {
        ControllerResponse result = restTemplateWorkService
                .findCompleteInfoExportCollectionOtherOperationsRequest(taskId, new AuthenticatedRequest(userInfo));
        taskConcurrencyMonitor.generateWarningGetCompleteInfo(taskId,
                result, forOperation, userInfo.getUser().getUserDisplayedName());
        result.setEntity(mapCompleteInfoExportCollectionOtherOperationsRequest(result));
        return result;
    }

    /**
     * Method to obtain request details
     * @param requestId the request id (case code)
     * @param userInfo UserInfo object
     * @return Controller Response object
     */
    public ControllerResponse getPetitionRequestDetails(String requestId, UserInfo userInfo) {
        ControllerResponse result = restTemplateWorkService
                .findDetailsExportCollectionOtherOperationsRequest(requestId, new AuthenticatedRequest(userInfo));
        result.setEntity(mapCompleteInfoExportCollectionOtherOperationsRequest(result));
        return result;
    }

    /**
     * Method used when we finish a complete information task and can continue with the request in work
     * @param request object with the data of the request
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     */
    public void completeCompleteInfoExportCollectionOtherOperationsRequest(
            ExportCollectionOtherOperationsRequest request, String taskId) {
        restTemplateWorkService.completeCompleteInfoExportCollectionOtherOperationsRequest(request, taskId);
    }

    /**
     * Method used when we must cancel a complete information task.
     * @param taskId the task id
     * @param request UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void cancelCompleteInfoExportCollectionOtherOperationsRequest(String taskId, UserInfo request) {
        restTemplateWorkService.cancelCompleteInfoTask(taskId, new AuthenticatedRequest(request));
    }

    /**
     * Method to map data of a request
     * @param response Controller Response object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     * @return ExportCollectionOtherOperationsRequest object with data mapped
     */
    private ExportCollectionOtherOperationsRequest mapExportCollectionOtherOperationsRequest(
            ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(), ExportCollectionOtherOperationsRequest.class);
    }

    /**
     * Method to map data of a complete information request
     * @param response Controller Response object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return CompleteInfoExportCollectionOtherOperationsRequest object with data mapped
     */
    private CompleteInfoExportCollectionOtherOperationsRequest mapCompleteInfoExportCollectionOtherOperationsRequest(
            ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(),CompleteInfoExportCollectionOtherOperationsRequest.class);
    }
}
