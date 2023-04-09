package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.platform.service.task.PlatformTaskService;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.history.HistoricCaseInstance;
import org.flowable.cmmn.api.history.HistoricCaseInstanceQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class CaseDataServiceTest {

    @InjectMocks
    CaseDataService caseDataService;

    @Mock
    ObjectMapper mapper;
    @Mock
    CaseSecurityService caseSecurityService;
    @Mock
    PlatformTaskService platformTaskService;
    @Mock
    CmmnHistoryService cmmnHistoryService;

    @Test
    void findComments_whenNoTaskFound_thenThrowResourceNotFoundException() {
        // Given
        String caseId = "caseId1";
        AuthenticatedRequest request = new AuthenticatedRequest();
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnHistoryService).createHistoricCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any());
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        doReturn(null).when(caseInstance).  getCaseVariables();
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> caseDataService.findComments(request, caseId));
        // Then
        assertEquals("findComments", exception.getKey());
        assertEquals("No case found with id caseId1", exception.getMessage());
    }

    @Test
    void findComments_whenInvalidCredentials_thenThrowSecurityException() {
        // Given
        String caseId = "caseId1";
        AuthenticatedRequest request = new AuthenticatedRequest();
        UserInfo userInfo = new UserInfo();
        request.setRequester(userInfo);
        Map<String, Object> caseVariables = new HashMap<>();
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnHistoryService).createHistoricCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any());
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        doReturn(caseVariables).when(caseInstance).  getCaseVariables();
        doThrow(SecurityException.class).when(caseSecurityService).checkRead(userInfo, caseVariables);
        // When and then
        assertThrows(SecurityException.class, () -> caseDataService.findComments(request, caseId));
    }

    @Test
    void findComments_whenErrorMapping_thenThrowServiceException() throws JsonProcessingException {
        // Given
        String requestId = "taskId1";
        String locale = "es_es";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(requestId);
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenThrow(JsonProcessingException.class);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> caseDataService.findComments(request, requestId));
        // Then
        assertEquals("findComments", exception.getKey());
        assertEquals("Processing error retrieving comments", exception.getMessage());
    }

    @Test
    void findComments_whenNoTranslationsFound_thenReturnDefaultName() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String defaultTaskName = "Default task name";
        String displayedName = "User 1";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        String taskIdComment = "taskId2";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(taskIdComment);
        comment.setUserDisplayedName(displayedName);
        comments.add(comment);
        mockGetTaskVariablesExternalTaskResponse(taskIdComment);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        when(platformTaskService.getTask(taskIdComment, true)).thenReturn(taskRepresentation);
        when(taskRepresentation.getTranslations()).thenReturn(objectNode);
        when(objectNode.get(locale)).thenReturn(null);
        when(taskRepresentation.getName()).thenReturn(defaultTaskName);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(1, result.size());
        assertEquals(defaultTaskName, result.get(0).getTaskName());
    }

    @Test
    void findComments_whenNoTranslationOfNameFound_thenReturnDefaultName() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String defaultTaskName = "Default task name";
        String displayedName = "User 1";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        String taskIdComment = "taskId2";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(taskIdComment);
        comment.setUserDisplayedName(displayedName);
        comments.add(comment);
        mockGetTaskVariablesExternalTaskResponse(taskIdComment);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        JsonNode jsonNode = mock(JsonNode.class);
        when(platformTaskService.getTask(taskIdComment, true)).thenReturn(taskRepresentation);
        when(taskRepresentation.getTranslations()).thenReturn(objectNode);
        when(objectNode.get(locale)).thenReturn(jsonNode);
        when(jsonNode.get("name")).thenReturn(null);
        when(taskRepresentation.getName()).thenReturn(defaultTaskName);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(1, result.size());
        assertEquals(defaultTaskName, result.get(0).getTaskName());
    }

    @ParameterizedTest
    @CsvSource({"CUSTOMER, true, true, 1", "CUSTOMER, true, false, 0", "CUSTOMER, false, false, 0",
            "OFFICE, true, true, 1", "OFFICE, true, false, 1", "OFFICE, false, false, 0",
            "MO, true, true, 1", "MO, true, false, 1", "MO, false, false, 0",
            "BO, true, true, 1", "BO, true, false, 1", "BO, false, false, 1",
            "OTHER, true, true, 0", "OTHER, true, false, 0", "OTHER, false, false, 0"})
    void findComments_whenCommentVisibility_thenReturnVisibleComments(String userType, boolean visibleForOffice, boolean visibleForClient, int listSize) throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String taskName = "Task name with separator | CLE-TEST";
        String displayedName = "User 1";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        String taskIdComment = "taskId2";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(taskIdComment);
        comment.setUserDisplayedName(displayedName);
        comment.setVisibleForOffice(visibleForOffice);
        comment.setVisibleForClient(visibleForClient);
        comments.add(comment);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        if(listSize > 0) {
            mockGetTaskVariablesExternalTaskResponse(taskIdComment);
            TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
            ObjectNode objectNode = mock(ObjectNode.class);
            JsonNode jsonNode = mock(JsonNode.class);
            when(platformTaskService.getTask(taskIdComment, true)).thenReturn(taskRepresentation);
            when(taskRepresentation.getTranslations()).thenReturn(objectNode);
            when(objectNode.get(locale)).thenReturn(jsonNode);
            when(jsonNode.get("name")).thenReturn(jsonNode);
            when(jsonNode.asText()).thenReturn(taskName);
        }
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(listSize, result.size());
    }

    @Test
    void findComments_whenCommentWithSeparator_thenReturnSplitTaskName() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String taskName = "Task name with separator | CLE-TEST";
        String displayedName = "User 1";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        mockGetTaskResponse(taskName, displayedName, locale);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(1, result.size());
        assertEquals("Task name with separator", result.get(0).getTaskName());
    }

    @Test
    void findComments_whenCommentWithoutSeparator_thenReturnSameTaskName() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String taskName = "Task name without separator";
        String displayedName = "User 1";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        mockGetTaskResponse(taskName, displayedName, locale);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(1, result.size());
        assertEquals(taskName, result.get(0).getTaskName());
    }

    @Test
    void findComments_whenCommentTaskNotExternalTask_thenReturnBackOfficeDisplayedName() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String taskName = "Task name without separator";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        String taskIdComment = "taskId2";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(taskIdComment);
        comment.setUserDisplayedName(null);
        comments.add(comment);
        Map<String, Object> taskVariables = new HashMap<>();
        Map<String, Object> task = new HashMap<>();
        task.put("isExternalTask", "false");
        taskVariables.put("task", task);
        when(platformTaskService.getTaskVariables(taskIdComment)).thenReturn(taskVariables);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        JsonNode jsonNode = mock(JsonNode.class);
        when(platformTaskService.getTask(taskIdComment, true)).thenReturn(taskRepresentation);
        when(taskRepresentation.getTranslations()).thenReturn(objectNode);
        when(objectNode.get(locale)).thenReturn(jsonNode);
        when(jsonNode.get("name")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn(taskName);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(1, result.size());
        assertEquals("Back Office", result.get(0).getUserDisplayedName());
    }

    @Test
    void findComments_whenCommentTaskNoTask_thenReturnSameDisplayedName() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String taskName = "Task name without separator";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        String taskIdComment = "taskId2";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(taskIdComment);
        comment.setUserDisplayedName("user1");
        comments.add(comment);
        when(platformTaskService.getTaskVariables(taskIdComment)).thenReturn(new HashMap<>());
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        JsonNode jsonNode = mock(JsonNode.class);
        when(platformTaskService.getTask(taskIdComment, true)).thenReturn(taskRepresentation);
        when(taskRepresentation.getTranslations()).thenReturn(objectNode);
        when(objectNode.get(locale)).thenReturn(jsonNode);
        when(jsonNode.get("name")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn(taskName);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUserDisplayedName());
    }

    @Test
    void findComments_whenCommentTaskNotFound_thenReturnBackOfficeDisplayedName() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String taskName = "Task name without separator";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        String taskIdComment = "taskId2";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(taskIdComment);
        comment.setUserDisplayedName(null);
        comments.add(comment);
        when(platformTaskService.getTaskVariables(taskIdComment)).thenReturn(null);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        JsonNode jsonNode = mock(JsonNode.class);
        when(platformTaskService.getTask(taskIdComment, true)).thenReturn(taskRepresentation);
        when(taskRepresentation.getTranslations()).thenReturn(objectNode);
        when(objectNode.get(locale)).thenReturn(jsonNode);
        when(jsonNode.get("name")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn(taskName);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> caseDataService.findComments(request, taskId));
        // Then
        assertEquals("findComments", exception.getKey());
        assertEquals("No task found with id taskId2", exception.getMessage());
    }


    @ParameterizedTest
    @CsvSource({"es_es, Alta",
            "en_us, Request",
            "other, Request"})
    void findComments_whenCommentTaskIdNotFound_thenReturnDefaultName(String locale, String taskName) throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(null);
        comments.add(comment);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(1, result.size());
        assertEquals(taskName, result.get(0).getTaskName());
    }

    @Test
    void findComments_whenComments_thenReturnOrderedComments() throws JsonProcessingException {
        // Given
        String taskId = "taskId1";
        String locale = "es_es";
        String taskName = "Task name without separator";
        String userType = "BO";
        AuthenticatedRequest request = generateAuthenticatedRequest(userType, locale);
        mockGetTaskVariablesResponse(taskId);
        String taskIdComment1 = "taskIdComment1";
        String timestamp1 = "2022-08-30T16:05:05.605Z";
        String taskIdComment2 = "taskIdComment2";
        String timestamp2 = "2022-09-01T15:20:08.206Z";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setTaskId(taskIdComment1);
        comment1.setTimestamp(timestamp1);
        comments.add(comment1);
        mockGetTaskVariablesExternalTaskResponse(taskIdComment1);
        Comment comment2 = new Comment();
        comment2.setTaskId(taskIdComment2);
        comment2.setTimestamp(timestamp2);
        comments.add(comment2);
        mockGetTaskVariablesExternalTaskResponse(taskIdComment2);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        JsonNode jsonNode = mock(JsonNode.class);
        when(platformTaskService.getTask(taskIdComment1, true)).thenReturn(taskRepresentation);
        when(platformTaskService.getTask(taskIdComment2, true)).thenReturn(taskRepresentation);
        when(taskRepresentation.getTranslations()).thenReturn(objectNode);
        when(objectNode.get(locale)).thenReturn(jsonNode);
        when(jsonNode.get("name")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn(taskName);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
        // When
        List<Comment> result = caseDataService.findComments(request, taskId);
        // Then
        assertEquals(2, result.size());
        assertEquals(timestamp2, result.get(0).getTimestamp());
        assertEquals(timestamp1, result.get(1).getTimestamp());
    }

    private static AuthenticatedRequest generateAuthenticatedRequest(String userType, String locale) {
        AuthenticatedRequest request = new AuthenticatedRequest();
        UserInfo userInfo = new UserInfo();
        User user = new User();
        user.setUserType(userType);
        userInfo.setUser(user);
        userInfo.setLocale(locale);
        request.setRequester(userInfo);
        return request;
    }

    private void mockGetTaskVariablesResponse(String requestId) {
        Map<String, Object> caseVariables = new HashMap<>();
        ArrayNode operationComments = mock(ArrayNode.class);
        when(operationComments.toString()).thenReturn("stringComments");
        caseVariables.put("operationComments", operationComments);

        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnHistoryService).createHistoricCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(requestId);
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        doReturn(caseVariables).when(caseInstance).  getCaseVariables();
    }

    private void mockGetTaskResponse(String taskName, String displayedName, String locale) throws JsonProcessingException {
        String taskIdComment = "taskId2";
        when(mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)).thenReturn(mapper);
        List<Comment> comments = new ArrayList<>();
        Comment comment = new Comment();
        comment.setTaskId(taskIdComment);
        comment.setUserDisplayedName(displayedName);
        comments.add(comment);
        mockGetTaskVariablesExternalTaskResponse(taskIdComment);
        when(mapper.readValue(eq("stringComments"), any(TypeReference.class))).thenReturn(comments);
        TaskRepresentation taskRepresentation = mock(TaskRepresentation.class);
        ObjectNode objectNode = mock(ObjectNode.class);
        JsonNode jsonNode = mock(JsonNode.class);
        when(platformTaskService.getTask(taskIdComment, true)).thenReturn(taskRepresentation);
        when(taskRepresentation.getTranslations()).thenReturn(objectNode);
        when(objectNode.get(locale)).thenReturn(jsonNode);
        when(jsonNode.get("name")).thenReturn(jsonNode);
        when(jsonNode.asText()).thenReturn(taskName);
        // Create new class with mocked mapper.
        caseDataService = new CaseDataService(mapper, platformTaskService, caseSecurityService, cmmnHistoryService);
    }

    private void mockGetTaskVariablesExternalTaskResponse(String taskId) {
        Map<String, Object> taskVariables = new HashMap<>();
        Map<String, Object> task = new HashMap<>();
        task.put("isExternalTask", "true");
        taskVariables.put("task", task);
        when(platformTaskService.getTaskVariables(taskId)).thenReturn(taskVariables);
    }
}
