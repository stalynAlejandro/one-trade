package com.pagonxt.onetradefinance.external.backend.service.cle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.concurrency.TaskConcurrencyMonitor;
import com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
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
public class ExportCollectionRequestService {

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
    public ExportCollectionRequestService(RestTemplateWorkService restTemplateWorkService,
                                          TaskConcurrencyMonitor taskConcurrencyMonitor) {
        this.restTemplateWorkService = restTemplateWorkService;
        this.taskConcurrencyMonitor = taskConcurrencyMonitor;
    }

    /**
     * Method used when we confirm a request of the export collection
     * @param request object with the data of the request
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ExportCollectionRequest object with data mapped
     */
    public ExportCollectionRequest confirmExportCollectionRequest(ExportCollectionRequest request) {
        ExportCollectionRequest savedDraft = this.createOrUpdateExportCollectionRequestDraft(request);
        ControllerResponse response = restTemplateWorkService.putExportCollectionRequest(savedDraft);
        return mapExportCollectionRequest(response);
    }

    /**
     * Method used when we create a draft or update a draft of a request of the export collection
     * @param request object with the data of the request
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ExportCollectionRequest object with data mapped
     */
    public ExportCollectionRequest createOrUpdateExportCollectionRequestDraft(ExportCollectionRequest request) {
        String code = request.getCode();
        ControllerResponse response;
        if (Strings.isBlank(code)) {
            response = restTemplateWorkService.postExportCollectionRequestDraft(request);
        } else {
            response = restTemplateWorkService.putExportCollectionRequestDraft(request);
        }
        return mapExportCollectionRequest(response);
    }

    /**
     * Method used when we need to get data from a draft of a request of the export collection
     * @param id the request id
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ExportCollectionRequest object with data mapped
     */
    public ExportCollectionRequest getExportCollectionRequestDraft(String id, UserInfo userInfo) {
        ControllerResponse response = restTemplateWorkService
                .findExportCollectionRequestDraft(id, new AuthenticatedRequest(userInfo));
        return mapExportCollectionRequest(response);
    }

    /**
     * Method used when we need to delete a draft of a request of the export collection
     * @param id the request id
     */
    public void deleteExportCollectionRequestDraft(String id) {
        restTemplateWorkService.deleteExportCollectionRequestDraft(id);
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
    public ControllerResponse getCompleteInfoExportCollectionRequest(String taskId,
                                                                     boolean forOperation,
                                                                     UserInfo userInfo) {
        ControllerResponse result = restTemplateWorkService
                .findCompleteInfoExportCollectionRequest(taskId, new AuthenticatedRequest(userInfo));
        taskConcurrencyMonitor.generateWarningGetCompleteInfo(taskId,
                result, forOperation, userInfo.getUser().getUserDisplayedName());
        result.setEntity(mapCompleteInfoExportCollectionRequest(result));
        return result;
    }

    /**
     * Method used when we finish a complete information task and can continue with the request in work
     * @param request object with the data of the request
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     */
    public void completeCompleteInfoExportCollectionRequest(ExportCollectionRequest request, String taskId) {
        restTemplateWorkService.completeCompleteInfoExportCollectionRequest(request, taskId);
    }

    /**
     * Method used when we must cancel a complete information task.
     * @param taskId the task id
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     */
    public void cancelCompleteInfoExportCollectionRequest(String taskId, UserInfo userInfo) {
        restTemplateWorkService.cancelCompleteInfoTask(taskId, new AuthenticatedRequest(userInfo));
    }

    /**
     * Method to obtain request details
     * @param requestId the request id (case code)
     * @param userInfo UserInfo object
     * @return Controller Response object
     */
    public ControllerResponse getPetitionRequestDetails(String requestId, UserInfo userInfo) {
        ControllerResponse result = restTemplateWorkService
                .findDetailsExportCollectionRequest(requestId, new AuthenticatedRequest(userInfo));
        result.setEntity(mapCompleteInfoExportCollectionRequest(result));
        return result;
    }

    /**
     * Method to map data of a request
     * @param response Controller Response object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ExportCollectionRequest object with data mapped
     */
    private ExportCollectionRequest mapExportCollectionRequest(ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(), ExportCollectionRequest.class);
    }

    /**
     * Method to map data of a complete information request
     * @param response Controller Response object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return CompleteInfoExportCollectionRequest object with data mapped
     */
    private CompleteInfoExportCollectionRequest mapCompleteInfoExportCollectionRequest(ControllerResponse response) {
        return objectMapper.convertValue(response.getEntity(), CompleteInfoExportCollectionRequest.class);
    }
}
