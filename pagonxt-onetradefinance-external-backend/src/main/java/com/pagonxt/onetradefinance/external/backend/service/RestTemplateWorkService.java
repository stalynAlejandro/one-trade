package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.configuration.RestTemplateProperties;
import com.pagonxt.onetradefinance.external.backend.service.exception.FlowableServerException;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContractsQuery;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see org.springframework.http.HttpHeaders
 * @see org.springframework.web.client.RestTemplate
 * @see com.pagonxt.onetradefinance.external.backend.configuration.RestTemplateProperties
 * @since jdk-11.0.13
 */
@Component
public class RestTemplateWorkService {
    private static final String EXPORT_COLLECTION_REQUEST_URL = "/backend/export-collection-request";
    private static final String EXPORT_COLLECTION_MODIFICATION_REQUEST_URL =
            "/backend/export-collection-modification-request";
    private static final String EXPORT_COLLECTION_ADVANCE_REQUEST_URL = "/backend/export-collection-advance-request";
    private static final String EXPORT_COLLECTION_ADVANCE_MODIFICATION_REQUEST_URL =
            "/backend/export-collection-advance-modification-request";
    private static final String EXPORT_COLLECTION_ADVANCE_CANCELLATION_REQUEST_URL =
            "/backend/export-collection-advance-cancellation-request";
    private static final String EXPORT_COLLECTION_OTHER_OPERATIONS_REQUEST_URL =
            "/backend/export-collection-other-operations-request";
    private static final String MY_REQUESTS_REQUEST_URL = "/backend/my-requests";
    private static final String MY_TASKS_REQUEST_URL = "/backend/my-tasks";
    private static final String HISTORIC_TASKS_REQUEST_URL = "/backend/historic-tasks";
    private static final String EXPORT_COLLECTIONS_URL = "/backend/export-collections";
    private static final String TASK_OPERATION_URL = "/backend/task-operation";
    private static final String CASE_DATA_URL = "/backend/case-data";
    private static final String DOCUMENT_URL = "/backend/document";
    private static final String OFFICE_URL = "/backend/office";
    private static final String TRADE_REQUEST_URL ="/backend/trade-request";
    private static final String CONFIRM = "/confirm";
    private static final String COMPLETE = "/complete";
    private static final String CANCEL = "/cancel";
    private static final String COMPLETE_INFO = "/complete-info";
    private static final String DETAILS = "/details";
    private static final String GET_VALID_OFFICE = "/getValidOffice/";
    private static final String GET_VALID_MIDDLE_OFFICE = "/getValidMiddleOffice/";
    private static final String GET_MIDDLE_OFFICE = "/getMiddleOffice/";
    private static final String GET_OFFICE_INFO = "/getOfficeInfo/";
    private static final String GET_OFFICES = "/getOffices/";
    private static final String COMMENTS = "/comments";
    private static final String CONTRACTS = "/contracts/";
    private static final String SEARCH = "/search";
    private static final String TASKS = "/tasks";
    private static final String REQUEST_CANCELLATION = "/request-cancellation";
    public static final String SLASH = "/";
    private static final String GET = "/get/";
    private static final String ERROR = "error";

    /**
     * Class attributes
     */
    private final RestTemplateProperties restTemplateProperties;
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;

    /**
     * Class constructor
     * @param restTemplateProperties RestTemplateProperties object
     * @param restTemplate RestTemplate object
     * @param httpHeaders HttpHeaders object
     */
    public RestTemplateWorkService(RestTemplateProperties restTemplateProperties,
                                   RestTemplate restTemplate,
                                   HttpHeaders httpHeaders) {
        this.restTemplateProperties = restTemplateProperties;
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    /**
     * Controller post method
     * @param request ExportCollectionRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ControllerResponse object
     */
    public ControllerResponse postExportCollectionRequestDraft(ExportCollectionRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_REQUEST_URL;
        HttpEntity<ExportCollectionRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller put method (draft)
     * @param request ExportCollectionRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ControllerResponse object
     */
    public ControllerResponse putExportCollectionRequestDraft(ExportCollectionRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_REQUEST_URL;
        HttpEntity<ExportCollectionRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Controller put method
     * @param request ExportCollectionRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ControllerResponse object
     */
    public ControllerResponse putExportCollectionRequest(ExportCollectionRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_REQUEST_URL + CONFIRM;
        HttpEntity<ExportCollectionRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find a draft of export collection
     * @param id request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findExportCollectionRequestDraft(String id, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_REQUEST_URL + GET + sanitize(id);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to delete a draft of export collection
     * @param id request id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse deleteExportCollectionRequestDraft(String id) {
        throw new UnsupportedOperationException("There is no implementation on Flowable Work for this request");
    }

    /**
     * Method to find documents
     * @param documentId the document id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findDocument(String documentId, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + DOCUMENT_URL + GET + sanitize(documentId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to post requests by user
     * @param query MyRequestsQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList
     * @return MyRequestsList object
     */
    public MyRequestsList postMyRequests(MyRequestsQuery query) {
        String url = restTemplateProperties.getFlowableWorkUrl() + MY_REQUESTS_REQUEST_URL;
        HttpEntity<MyRequestsQuery> entity = composeHttpEntity(query, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, MyRequestsList.class));
    }

    /**
     * Controller post method
     * @param request ExportCollectionModificationRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest
     * @return ControllerResponse object
     */
    public ControllerResponse postExportCollectionModificationRequest(ExportCollectionModificationRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_MODIFICATION_REQUEST_URL;
        HttpEntity<ExportCollectionModificationRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller post method
     * @param query ExportCollectionQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery
     * @return ControllerResponse object
     */
    public ControllerResponse postExportCollections(ExportCollectionQuery query) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTIONS_URL;
        HttpEntity<ExportCollectionQuery> entity = composeHttpEntity(query, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to post tasks by user
     * @param query MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList
     * @return MyTasksList object
     */
    public MyTasksList postMyTasks(MyTasksQuery query) {
        String url = restTemplateProperties.getFlowableWorkUrl() + MY_TASKS_REQUEST_URL;
        HttpEntity<MyTasksQuery> entity = composeHttpEntity(query, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, MyTasksList.class));
    }

    /**
     * Controller post method (draft)
     * @param request ExportCollectionAdvanceRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ControllerResponse object
     */
    public ControllerResponse postExportCollectionAdvanceRequestDraft(ExportCollectionAdvanceRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_ADVANCE_REQUEST_URL;
        HttpEntity<ExportCollectionAdvanceRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller put method (draft)
     * @param request ExportCollectionAdvanceRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ControllerResponse object
     */
    public ControllerResponse putExportCollectionAdvanceRequestDraft(ExportCollectionAdvanceRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_ADVANCE_REQUEST_URL;
        HttpEntity<ExportCollectionAdvanceRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Controller put method
     * @param request ExportCollectionAdvanceRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ControllerResponse object
     */
    public ControllerResponse putExportCollectionAdvanceRequest(ExportCollectionAdvanceRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_ADVANCE_REQUEST_URL + CONFIRM;
        HttpEntity<ExportCollectionAdvanceRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find a draft of export collection advance
     * @param id request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findExportCollectionAdvanceRequestDraft(String id, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_REQUEST_URL + GET + sanitize(id);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller post method
     * @param request ExportCollectionAdvanceModificationRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest
     * @return ControllerResponse object
     */
    public ControllerResponse postExportCollectionAdvanceModificationRequest(
            ExportCollectionAdvanceModificationRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_ADVANCE_MODIFICATION_REQUEST_URL;
        HttpEntity<ExportCollectionAdvanceModificationRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller post method
     * @param request ExportCollectionAdvanceCancellationRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     * @return ControllerResponse object
     */
    public ControllerResponse postExportCollectionAdvanceCancellationRequest(
            ExportCollectionAdvanceCancellationRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_ADVANCE_CANCELLATION_REQUEST_URL;
        HttpEntity<ExportCollectionAdvanceCancellationRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller post method
     * @param request ExportCollectionOtherOperationsRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     * @return ControllerResponse object
     */
    public ControllerResponse postExportCollectionOtherOperationsRequest(
            ExportCollectionOtherOperationsRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + EXPORT_COLLECTION_OTHER_OPERATIONS_REQUEST_URL;
        HttpEntity<ExportCollectionOtherOperationsRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to find a complete information task from a request of export collection
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findCompleteInfoExportCollectionRequest(String taskId, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_REQUEST_URL + COMPLETE_INFO + GET + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller method to finish a complete information task
     * @param request ExportCollectionRequest object
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ControllerResponse object
     */
    public ControllerResponse completeCompleteInfoExportCollectionRequest(
            ExportCollectionRequest request, String taskId) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_REQUEST_URL + COMPLETE_INFO + COMPLETE + SLASH + sanitize(taskId);
        HttpEntity<ExportCollectionRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find a complete information task
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findCompleteInfoExportCollectionModificationRequest(String taskId,
                                                                                  AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_MODIFICATION_REQUEST_URL + COMPLETE_INFO + GET + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller method to finish a complete information task
     * @param request  ExportCollectionModificationRequest object
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest
     * @return ControllerResponse object
     */
    public ControllerResponse completeCompleteInfoExportCollectionModificationRequest(
            ExportCollectionModificationRequest request, String taskId) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_MODIFICATION_REQUEST_URL + COMPLETE_INFO + COMPLETE + SLASH + sanitize(taskId);
        HttpEntity<ExportCollectionModificationRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find a complete information task
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findCompleteInfoExportCollectionAdvanceRequest(String taskId,
                                                                             AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_REQUEST_URL + COMPLETE_INFO + GET + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller method to finish a complete information task
     * @param request ExportCollectionAdvanceRequest object
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ControllerResponse object
     */
    public ControllerResponse completeCompleteInfoExportCollectionAdvanceRequest(ExportCollectionAdvanceRequest request,
                                                                                 String taskId) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_REQUEST_URL + COMPLETE_INFO + COMPLETE + SLASH + sanitize(taskId);
        HttpEntity<ExportCollectionAdvanceRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find a complete information task
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findCompleteInfoExportCollectionAdvanceModificationRequest(String taskId,
                                                                                         AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_MODIFICATION_REQUEST_URL + COMPLETE_INFO + GET + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller method to finish a complete information task
     * @param request ExportCollectionAdvanceModificationRequest object
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest
     * @return ControllerResponse object
     */
    public ControllerResponse completeCompleteInfoExportCollectionAdvanceModificationRequest(
            ExportCollectionAdvanceModificationRequest request, String taskId) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_MODIFICATION_REQUEST_URL +
                COMPLETE_INFO + COMPLETE + SLASH + sanitize(taskId);
        HttpEntity<ExportCollectionAdvanceModificationRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find a complete information task
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findCompleteInfoExportCollectionAdvanceCancellationRequest(String taskId,
                                                                                         AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_CANCELLATION_REQUEST_URL + COMPLETE_INFO + GET + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller method to finish a complete information task
     * @param request ExportCollectionAdvanceCancellationRequest object
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     * @return ControllerResponse object
     */
    public ControllerResponse completeCompleteInfoExportCollectionAdvanceCancellationRequest(
            ExportCollectionAdvanceCancellationRequest request, String taskId) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_CANCELLATION_REQUEST_URL +
                COMPLETE_INFO + COMPLETE + SLASH + sanitize(taskId);
        HttpEntity<ExportCollectionAdvanceCancellationRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find a complete information task
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findCompleteInfoExportCollectionOtherOperationsRequest(String taskId,
                                                                                     AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_OTHER_OPERATIONS_REQUEST_URL + COMPLETE_INFO + GET + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller method to finish a complete information task
     * @param request ExportCollectionOtherOperationsRequest object
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     * @return ControllerResponse object
     */
    public ControllerResponse completeCompleteInfoExportCollectionOtherOperationsRequest(
            ExportCollectionOtherOperationsRequest request, String taskId) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_OTHER_OPERATIONS_REQUEST_URL + COMPLETE_INFO + COMPLETE + SLASH + sanitize(taskId);
        HttpEntity<ExportCollectionOtherOperationsRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to cancel a complete information task
     * @param taskId task id
     * @param userInfo AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse cancelCompleteInfoTask(String taskId, AuthenticatedRequest userInfo) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                TASK_OPERATION_URL + COMPLETE_INFO + CANCEL + SLASH + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(userInfo, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Method to find comments from a task
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findComments(String taskId, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + CASE_DATA_URL + GET + sanitize(taskId) + COMMENTS;
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller post method
     * @param query HistoricTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList
     * @return a historic tasks
     */
    public HistoricTasksList postHistoricTasks(HistoricTasksQuery query) {
        String url = restTemplateProperties.getFlowableWorkUrl() + HISTORIC_TASKS_REQUEST_URL;
        HttpEntity<HistoricTasksQuery> entity = composeHttpEntity(query, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, HistoricTasksList.class));
    }

    /**
     * Method to find details from a request
     * @param requestId request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findDetailsExportCollectionRequest(String requestId, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_REQUEST_URL + DETAILS + SLASH + sanitize(requestId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to find details from a request
     * @param requestId request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findDetailsExportCollectionModificationRequest(String requestId,
                                                                             AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_MODIFICATION_REQUEST_URL + DETAILS + SLASH + sanitize(requestId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to find details from a request
     * @param requestId request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findDetailsExportCollectionAdvanceRequest(String requestId,
                                                                        AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_REQUEST_URL + DETAILS + SLASH + sanitize(requestId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to find details from a request
     * @param requestId request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findDetailsExportCollectionAdvanceModificationRequest(String requestId,
                                                                                    AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_MODIFICATION_REQUEST_URL + DETAILS + SLASH + sanitize(requestId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to find details from a request
     * @param requestId request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findDetailsExportCollectionAdvanceCancellationRequest(String requestId,
                                                                                    AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_ADVANCE_CANCELLATION_REQUEST_URL + DETAILS + SLASH + sanitize(requestId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to find details from a request
     * @param requestId request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findDetailsExportCollectionOtherOperationsRequest(String requestId,
                                                                                AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                EXPORT_COLLECTION_OTHER_OPERATIONS_REQUEST_URL + DETAILS + SLASH + sanitize(requestId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to check if the officeId belongs to an office
     * @param officeId  : String with the officeId
     * @param request   : AuthenticatedRequest object
     * @return ControllerResponse object
     */
    public ControllerResponse isValidOffice(String officeId,AuthenticatedRequest request){
        String url = restTemplateProperties.getFlowableWorkUrl() + OFFICE_URL + GET_VALID_OFFICE + sanitize(officeId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url,HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to check if the officeId belongs to a middle office
     * @param officeId  : String with the officeId
     * @param request   : AuthenticatedRequest object
     * @return ControllerResponse object
     */
    public ControllerResponse isValidMiddleOffice(String officeId,AuthenticatedRequest request){
        String url = restTemplateProperties.getFlowableWorkUrl()
                + OFFICE_URL + GET_VALID_MIDDLE_OFFICE + sanitize(officeId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url,HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to get the middleOffice of an office
     * @param officeId  : String with the officeId
     * @param request   : AuthenticatedRequest object
     * @return ControllerResponse object
     */
    public ControllerResponse getMiddleOffice(String officeId,AuthenticatedRequest request){
        String url = restTemplateProperties.getFlowableWorkUrl() + OFFICE_URL + GET_MIDDLE_OFFICE + sanitize(officeId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url,HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to get an object with the info of an office
     * @param officeId  : String with the officeId
     * @param request   : AuthenticatedRequest object
     * @return ControllerResponse object
     */
    public ControllerResponse getOfficeInfo(String officeId,AuthenticatedRequest request){
        String url = restTemplateProperties.getFlowableWorkUrl() + OFFICE_URL + GET_OFFICE_INFO + sanitize(officeId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url,HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to obtain a list of offices to which a middle office belongs
     * @param officeId  : String with the officeId
     * @param request   : AuthenticatedRequest object
     * @return ControllerResponse object
     */
    public ControllerResponse getOffices(String officeId,AuthenticatedRequest request){
        String url = restTemplateProperties.getFlowableWorkUrl() + OFFICE_URL + GET_OFFICES + sanitize(officeId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url,HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller post method
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse postTradeRequestDraft(AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + TRADE_REQUEST_URL;
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller put method (draft)
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse putTradeRequestDraft(AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + TRADE_REQUEST_URL;
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class));
    }

    /**
     * Controller put method
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     */
    public void putTradeRequest(String id, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + TRADE_REQUEST_URL + SLASH + sanitize(id) + CONFIRM;
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class);
    }

    /**
     * Method to find a trade external task from
     * @param taskId task id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findTradeExternalTaskRequest(String taskId, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                TRADE_REQUEST_URL + TASKS + SLASH + sanitize(taskId);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Controller method to finish a complete information task
     * @param request TradeRequest object
     * @param taskId the task id
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @see com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest
     */
    public void completeCompleteInfoTradeRequest(
            TradeRequest request, String taskId) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                TRADE_REQUEST_URL + TASKS + SLASH + sanitize(taskId) + COMPLETE_INFO;
        HttpEntity<TradeRequest> entity = composeHttpEntity(request, httpHeaders);
        restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class);
    }

    /**
     * Method to cancel a trade external task
     * @param taskId task id
     * @param userInfo AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     */
    public void cancelTradeExternalTask(String taskId, AuthenticatedRequest userInfo) {
        String url = restTemplateProperties.getFlowableWorkUrl() +
                TRADE_REQUEST_URL + TASKS + SLASH + sanitize(taskId) + REQUEST_CANCELLATION;
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(userInfo, httpHeaders);
        restTemplate.exchange(url, HttpMethod.PUT, entity, ControllerResponse.class);
    }

    /**
     * Method to find a draft of trade request
     * @param id request id
     * @param request AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findTradeRequest(String id, AuthenticatedRequest request) {
        String url = restTemplateProperties.getFlowableWorkUrl() + TRADE_REQUEST_URL + GET + sanitize(id);
        HttpEntity<AuthenticatedRequest> entity = composeHttpEntity(request, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    /**
     * Method to find a contract of trade
     * @param type  the type of contract
     * @param query object containing the search terms
     * @see TradeContractsQuery
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     * @return ControllerResponse object
     */
    public ControllerResponse findTradeContracts(String type, TradeContractsQuery query) {
        String url = restTemplateProperties.getFlowableWorkUrl() + TRADE_REQUEST_URL +
                CONTRACTS + sanitize(type) + SEARCH;
        HttpEntity<TradeContractsQuery> entity = composeHttpEntity(query, httpHeaders);
        return extractResult(restTemplate.exchange(url, HttpMethod.POST, entity, ControllerResponse.class));
    }

    private <T> T extractResult(ResponseEntity<T> response) {
        T result = response.getBody();
        if (result == null) {
            throw new FlowableServerException();
        }
        if (result instanceof ControllerResponse) {
            ControllerResponse controllerResponse = (ControllerResponse) result;
            if (ERROR.equals(controllerResponse.getType())) {
                throw new FlowableServerException(controllerResponse.getMessage());
            }
        }
        return result;
    }

    private <T> HttpEntity<T> composeHttpEntity(T body, HttpHeaders headers) {
        return new HttpEntity<>(body, headers);
    }

    private String sanitize(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }
}
