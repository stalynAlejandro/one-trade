package com.pagonxt.onetradefinance.work.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.platform.service.task.CompleteFormRepresentation;
import com.flowable.platform.service.task.PlatformTaskService;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.model.Task;
import com.pagonxt.onetradefinance.work.service.model.Variable;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.engine.TaskService;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
class TaskUtilsTest {

    @InjectMocks
    private TaskUtils taskUtils;
    @Mock
    private TaskService taskService;
    @Mock
    private PlatformTaskService platformTaskService;
    @Mock
    private CmmnRuntimeService cmmnRuntimeService;
    @Mock
    ObjectMapper mapper;

    @Test
    void completeTaskDraft_taskFound_invokeComplete() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getId()).thenReturn("id1");
        TaskQuery taskQuery = mock(TaskQuery.class);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.includeTaskLocalVariables()).thenReturn(taskQuery);
        when(taskQuery.caseInstanceIdWithChildren("id1")).thenReturn(taskQuery);
        Map<String, Object> taskLocalVariables = new HashMap<>();
        taskLocalVariables.put("isExternalTask", "true");
        taskLocalVariables.put("externalTaskType", "EXTERNAL_REQUEST_DRAFT");
        org.flowable.task.api.Task task = mock(org.flowable.task.api.Task.class);
        when(task.getTaskLocalVariables()).thenReturn(taskLocalVariables);
        when(task.getId()).thenReturn("taskId");
        when(taskQuery.list()).thenReturn(List.of(task));

        // When
        taskUtils.completeTaskDraft(caseInstance, new User());

        // Then
        verify(taskService, times(1)).complete("taskId");
        verify(taskService).setVariableLocal(eq("taskId"), eq("taskCompletionUserId"), any());
        verify(taskService).setVariableLocal(eq("taskId"), eq("taskCompletionUserDisplayedName"), any());
        verify(cmmnRuntimeService).removeVariable("id1", "requestSavedStep");
        verify(cmmnRuntimeService).removeVariable("id1", "requestDocuments");
    }

    @Test
    void completeTaskDraft_severalTasksFound_throwIllegalStateException() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getId()).thenReturn("id1");
        TaskQuery taskQuery = mock(TaskQuery.class);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.includeTaskLocalVariables()).thenReturn(taskQuery);
        when(taskQuery.caseInstanceIdWithChildren("id1")).thenReturn(taskQuery);
        Map<String, Object> taskLocalVariables = new HashMap<>();
        taskLocalVariables.put("isExternalTask", "true");
        taskLocalVariables.put("externalTaskType", "EXTERNAL_REQUEST_DRAFT");
        org.flowable.task.api.Task task = mock(org.flowable.task.api.Task.class);
        when(task.getTaskLocalVariables()).thenReturn(taskLocalVariables);
        when(taskQuery.list()).thenReturn(List.of(task, task));
        User user = new User();

        // When
        assertThrows(IllegalStateException.class,
                () -> taskUtils.completeTaskDraft(caseInstance, user));

        // Then
        verify(taskService, never()).complete("taskId");
    }

    @Test
    void completeTaskDraft_NoTaskFound_throwIllegalStateException() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getId()).thenReturn("id1");
        TaskQuery taskQuery = mock(TaskQuery.class);
        when(taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.includeTaskLocalVariables()).thenReturn(taskQuery);
        when(taskQuery.caseInstanceIdWithChildren("id1")).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(Collections.emptyList());
        User user = new User();

        // When
        assertThrows(IllegalStateException.class,
                () -> taskUtils.completeTaskDraft(caseInstance, user));

        // Then
        verify(taskService, never()).complete("taskId");
    }

    @Test
    void completeTaskCompleteInfo_ok_invokeCompleteTaskForm() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("testVar", "testValue");
        taskVariables.put("root", new HashMap<>());
        when(mapper.writeValueAsString(taskVariables)).thenReturn("stringMap");
        when(mapper.readValue(eq("stringMap"), any(TypeReference.class))).thenReturn(taskVariables);
        ArgumentCaptor<CompleteFormRepresentation> captor = ArgumentCaptor.forClass(CompleteFormRepresentation.class);
        // When
        taskUtils.completeTaskCompleteInfo("TSK-123", taskVariables,new User("externalUserId","externalUserDisplayedName","OFFICE"), FieldConstants.VALUE_COMPLETE);
        // Then
        verify(platformTaskService).completeTaskForm(eq("TSK-123"), captor.capture());
        CompleteFormRepresentation completeFormRepresentation = captor.getValue();
        assertEquals("testValue", completeFormRepresentation.getValues().get("testVar"));
    }
    @Test
    void completeTaskCompleteInfo_withoutFormOutcome_invokeCompleteTaskForm() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("testVar", "testValue");
        taskVariables.put("root", new HashMap<>());
        when(mapper.writeValueAsString(taskVariables)).thenReturn("stringMap");
        when(mapper.readValue(eq("stringMap"), any(TypeReference.class))).thenReturn(taskVariables);
        ArgumentCaptor<CompleteFormRepresentation> captor = ArgumentCaptor.forClass(CompleteFormRepresentation.class);
        // When
        taskUtils.completeTaskCompleteInfo("TSK-123", taskVariables,new User("externalUserId","externalUserDisplayedName","OFFICE"), null);
        // Then
        verify(platformTaskService).completeTaskForm(eq("TSK-123"), captor.capture());
        CompleteFormRepresentation completeFormRepresentation = captor.getValue();
        assertEquals("testValue", completeFormRepresentation.getValues().get("testVar"));
        assertNull(completeFormRepresentation.getOutcome());
    }
    @Test
    void completeTaskCompleteInfo_nullRoot_invokeCompleteTaskForm() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("testVar", "testValue");
        taskVariables.put("root", null);
        when(mapper.writeValueAsString(taskVariables)).thenReturn("stringMap");
        when(mapper.readValue(eq("stringMap"), any(TypeReference.class))).thenReturn(taskVariables);
        ArgumentCaptor<CompleteFormRepresentation> captor = ArgumentCaptor.forClass(CompleteFormRepresentation.class);
        // When
        taskUtils.completeTaskCompleteInfo("TSK-123", taskVariables,new User("externalUserId","externalUserDisplayedName","OFFICE"), FieldConstants.VALUE_COMPLETE);
        // Then
        verify(platformTaskService).completeTaskForm(eq("TSK-123"), captor.capture());
        CompleteFormRepresentation completeFormRepresentation = captor.getValue();
        assertEquals("testValue", completeFormRepresentation.getValues().get("testVar"));
    }

    @Test
    void completeTaskCompleteInfo_errorReMapping_thenThrowServiceException() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("testVar", "testValue");
        when(mapper.writeValueAsString(taskVariables)).thenThrow(JsonProcessingException.class);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> taskUtils.completeTaskCompleteInfo("TSK-123", taskVariables,null, FieldConstants.VALUE_COMPLETE));
        // Then
        assertEquals("errorMappingVariables", exception.getKey());
        assertEquals("Processing error mapping variables", exception.getMessage());
    }

    @Test
    void getTask_ok_thenReturnTask() {
        // Given
        TaskRepresentation task = new TaskRepresentation();
        when(platformTaskService.getTask("TSK-123", false)).thenReturn(task);
        // When
        TaskRepresentation result = taskUtils.getTask("TSK-123", false);
        // Then
        assertEquals(task, result);
    }

    @Test
    void getTask_whenEmptyTaskId_thenThrowInvalidRequestException() {
        // Given
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> taskUtils.getTask("", false));
        // Then
        assertEquals("errorTaskId", exception.getKey());
        assertEquals("The field 'taskId' must be informed.", exception.getMessage());
    }

    @Test
    void getTask_whenTaskNotFound_thenThrowResourceNotFoundException() {
        // Given
        when(platformTaskService.getTask("TSK-123", false)).thenReturn(null);
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> taskUtils.getTask("TSK-123", false));
        // Then
        assertEquals("errorTaskNotFound", exception.getKey());
        assertEquals("No task found with id TSK-123", exception.getMessage());
    }

    @Test
    void getTaskVariablesCompleteInfo_ok_thenReturnVariables() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("isExternalTask", "true");
        taskVariables.put("externalTaskType", "COMPLETE_INFORMATION");
        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(taskVariables);
        // When
        Map<String, Object> result = taskUtils.getTaskVariablesCompleteInfo("TSK-123");
        // Then
        assertEquals(taskVariables, result);
    }

    @Test
    void getTaskVariablesCompleteInfo_whenEmptyTaskId_thenThrowInvalidRequestException() {
        // Given
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> taskUtils.getTaskVariablesCompleteInfo(""));
        // Then
        assertEquals("errorTaskId", exception.getKey());
        assertEquals("The field 'taskId' must be informed.", exception.getMessage());
    }

    @Test
    void getTaskVariablesCompleteInfo_whenTaskNotFound_thenThrowResourceNotFoundException() {
        // Given
        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(null);
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> taskUtils.getTaskVariablesCompleteInfo("TSK-123"));
        // Then
        assertEquals("errorTaskNotFound", exception.getKey());
        assertEquals("No task found with id TSK-123", exception.getMessage());
    }

    @Test
    void getTaskVariablesCompleteInfo_whenNotCompleteInformationTask_thenThrowInvalidRequestException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("isExternalTask", "true");
        taskVariables.put("externalTaskType", "NOT_COMPLETE_INFORMATION");
        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(taskVariables);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> taskUtils.getTaskVariablesCompleteInfo("TSK-123"));
        // Then
        assertEquals("errorInvalidTask", exception.getKey());
        assertEquals("The task found with id TSK-123 is not the right type", exception.getMessage());
    }


    @Test
    void getTaskVariablesCompleteInfo_whenNotExternalTask_thenThrowInvalidRequestException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("isExternalTask", "false");
        taskVariables.put("externalTaskType", "COMPLETE_INFORMATION");
        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(taskVariables);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> taskUtils.getTaskVariablesCompleteInfo("TSK-123"));
        // Then
        assertEquals("errorInvalidTask", exception.getKey());
        assertEquals("The task found with id TSK-123 is not the right type", exception.getMessage());
    }

    @Test
    void setCompleteInfoTaskVariables_whenNotExternalTask_thenThrowInvalidRequestException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("isExternalTask", "true");
        taskVariables.put("externalTaskType", "COMPLETE_INFORMATION");
        Map<String, Object> root = new HashMap<>();
        Map<String, Object> operation = mock(HashMap.class);
        root.put("operation", operation);
        taskVariables.put("root", root);
        when(platformTaskService.getTaskVariables("TSK-123")).thenReturn(taskVariables);
        String comment = "comment1";
        Map<String, Object> requestVariables = new HashMap<>();
        requestVariables.put("testVariable", "testVariable1");
        // When
        Map<String, Object> result = taskUtils.setCompleteInfoTaskVariables("TSK-123", comment, requestVariables);
        // Then
        verify(operation).put("testVariable", "testVariable1");
        assertEquals(taskVariables, result);
    }

    @Test
    void getString_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "string";
        String value = "value";
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getTextValue()).thenReturn(value);
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        String expectedResult = "value";

        // When
        String result = TaskUtils.getString(instance, name);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as String");
    }

    @Test
    void getDouble_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "double";
        String value = "12.34";
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getRawValue()).thenReturn(value);
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        Double expectedResult = 12.34D;

        // When
        Double result = TaskUtils.getDouble(instance, name);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as Double");
    }

    @Test
    void getInteger_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "integer";
        String value = "1234";
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getRawValue()).thenReturn(value);
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        Integer expectedResult = 1234;

        // When
        Integer result = TaskUtils.getInteger(instance, name);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as Integer");
    }

    @Test
    void getDate_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "date";
        Date value = new Date();
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getDateValue()).thenReturn(value);
        when(variable.getRawValue()).thenReturn(value.toString());
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        Date expectedResult = (Date) value.clone();

        // When
        Date result = TaskUtils.getDate(instance, name);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as Date");
    }

    @Test
    void getScopedString_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "string";
        String scope = "scope";
        String value = "value";
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getScopeHierarchyType()).thenReturn(scope);
        when(variable.getTextValue()).thenReturn(value);
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        String expectedResult = "value";

        // When
        String result = TaskUtils.getString(instance, name, scope);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as String");
    }

    @Test
    void getScopedDouble_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "double";
        String scope = "scope";
        String value = "12.34";
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getScopeHierarchyType()).thenReturn(scope);
        when(variable.getRawValue()).thenReturn(value);
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        Double expectedResult = 12.34D;

        // When
        Double result = TaskUtils.getDouble(instance, name, scope);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as Double");
    }

    @Test
    void getScopedInteger_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "integer";
        String scope = "scope";
        String value = "1234";
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getScopeHierarchyType()).thenReturn(scope);
        when(variable.getRawValue()).thenReturn(value);
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        Integer expectedResult = 1234;

        // When
        Integer result = TaskUtils.getInteger(instance, name, scope);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as Integer");
    }

    @Test
    void getScopedDate_ok_returnsVariableValue() {
        // Given
        String name = "name";
        String type = "date";
        String scope = "scope";
        Date value = new Date();
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn(name);
        when(variable.getType()).thenReturn(type);
        when(variable.getDateValue()).thenReturn(value);
        when(variable.getScopeHierarchyType()).thenReturn(scope);
        when(variable.getRawValue()).thenReturn(value.toString());
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of(variable));
        Date expectedResult = (Date) value.clone();

        // When
        Date result = TaskUtils.getDate(instance, name, scope);

        // Then
        assertEquals(expectedResult, result, "Should return the variable value as Date");
    }

    @Test
    void getString_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        String result = TaskUtils.getString(instance, "name");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getDouble_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Double result = TaskUtils.getDouble(instance, "name");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getInteger_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Integer result = TaskUtils.getInteger(instance, "name");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getDate_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Date result = TaskUtils.getDate(instance, "name");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getScopedString_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        String result = TaskUtils.getString(instance, "name", "scope");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getScopedDouble_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Double result = TaskUtils.getDouble(instance, "name", "scope");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getScopedInteger_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Integer result = TaskUtils.getInteger(instance, "name", "scope");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void getScopedDate_noVariable_returnsNull() {
        // Given
        Task instance = mock(Task.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Date result = TaskUtils.getDate(instance, "name", "scope");

        // Then
        assertNull(result, "Should return null");
    }
}
