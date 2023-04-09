package com.pagonxt.onetradefinance.external.backend.concurrency;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.configuration.ConcurrencyConfigurationProperties;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class TaskConcurrencyMonitorTest {

    @Test
    void registerTaskModificationByUser_noTaskId_throwsNullPointerException() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT5M");

        // When
        Exception thrown = assertThrows(NullPointerException.class,
                () -> taskConcurrencyMonitor.registerTaskModificationByUser(null, null),
                "Should throw NullPointerException");

        // Then
        assertEquals("Task id is null", thrown.getMessage(), "Thrown exception should have a valid message");
    }

    @Test
    void registerTaskModificationByUser_noUserId_throwsNullPointerException() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT10M");

        // When
        Exception thrown = assertThrows(NullPointerException.class,
                () -> taskConcurrencyMonitor.registerTaskModificationByUser("taskId", null),
                "Should throw NullPointerException");

        // Then
        assertEquals("User is null", thrown.getMessage(), "Thrown exception should have a valid message");
    }

    @Test
    void registerTaskModificationByUser_noPreviousTaskRegistry_createsTaskRegistry() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");

        // When
        taskConcurrencyMonitor.registerTaskModificationByUser("taskId", "user");

        // Then
        Map<String, TaskAccessRegistry> taskAccessRegistryMap = extractMonitorRegistry(taskConcurrencyMonitor);
        assertFalse(taskAccessRegistryMap.isEmpty(), "Task access registry map should not be empty");
        TaskAccessRegistry registry = taskAccessRegistryMap.get("taskId");
        assertEquals(new TaskAccessRegistry("taskId", Duration.ofMinutes(15), null), registry, "Created registry should match pattern");
        assertEquals(-880873057, registry.hashCode(), "Created registry should have a valid hash code");
    }

    @Test
    void registerTaskModificationByUser_previousTaskRegistry_updatesTaskRegistry() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        taskConcurrencyMonitor.registerTaskModificationByUser("taskId", "user");

        // When
        taskConcurrencyMonitor.registerTaskModificationByUser("taskId", "anotherUser");

        // Then
        Map<String, TaskAccessRegistry> taskAccessRegistryMap = extractMonitorRegistry(taskConcurrencyMonitor);
        Map<LocalDate, String> registry = extractTaskRegistry(taskAccessRegistryMap.get("taskId"));
        assertEquals(2, registry.size(), "Task registry should contain 2 items");
    }

    @Test
    void checkConcurrentTaskAccess_nullTaskId_throwsNullPointerException() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");

        // When
        Exception thrown = assertThrows(NullPointerException.class,
                () -> taskConcurrencyMonitor.checkConcurrentTaskAccess(null),
                "Should throw NullPointerException");

        // Then
        assertEquals("Task id is null", thrown.getMessage(), "Thrown exception should have a valid message");
    }

    @Test
    void checkConcurrentTaskAccess_noPreviousTaskRegistry_returnsEmptyAccessLog() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        TaskAccessLog expectedResult = new TaskAccessLog("taskId", Duration.parse("PT15M"));

        // When
        TaskAccessLog result = taskConcurrencyMonitor.checkConcurrentTaskAccess("taskId");

        // Then
        assertEquals(expectedResult, result, "Result should match pattern");
    }

    @Test
    void checkConcurrentTaskAccess_userDidModifyTask_returnsValidAccessLog() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        taskConcurrencyMonitor.registerTaskModificationByUser("taskId", "user");
        TaskAccessLog expectedResult = new TaskAccessLog("taskId", Duration.parse("PT15M"), List.of("user"));

        // When
        TaskAccessLog result = taskConcurrencyMonitor.checkConcurrentTaskAccess("taskId");

        // Then
        assertEquals(expectedResult, result, "Result should match pattern");
        assertEquals("taskId", result.getTaskId(), "Result should have a valid taskId");
        assertEquals(Duration.ofMinutes(15), result.getThreshold(), "Result should have a valid threshold");
        assertEquals(List.of("user"), result.getUsers(), "Result should have a valid users list");
        assertEquals("TaskAccessLog{taskId='taskId', threshold=PT15M, users=[user]}", result.toString(), "Result should have a valid String representation");
        assertEquals(-406823227, result.hashCode(), "Result should have a valid hash code");
    }

    @Test
    void checkConcurrentTaskAccess_userModificationExpired_returnsEmptyAccessLog() throws Exception {
        // Given
        Duration duration = Duration.ofMillis(500);
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor(duration); // Modifications are alive for half a second
        taskConcurrencyMonitor.registerTaskModificationByUser("taskId", "user");      // This modification will expire in 1 second
        TimeUnit.SECONDS.sleep(1);
        TaskAccessLog expectedResult = new TaskAccessLog("taskId", Duration.parse("PT0.5S"));

        // When
        TaskAccessLog result = taskConcurrencyMonitor.checkConcurrentTaskAccess("taskId");

        // Then
        assertEquals(expectedResult, result, "Result should match pattern");
    }

    @Test
    void writeWarningIfEdited_userDidModifyTask_setWarningAndKey() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        taskConcurrencyMonitor.registerTaskModificationByUser("taskId", "user");
        ControllerResponse controllerResponse = mock(ControllerResponse.class);
        when(controllerResponse.getType()).thenReturn("success");
        // When
        taskConcurrencyMonitor.writeWarningIfEdited("taskId", controllerResponse);
        // Then
        verify(controllerResponse).setType("warning");
        verify(controllerResponse).setKey("taskWasRecentlyEdited");
    }

    @Test
    void writeWarningIfEdited_whenResponseError_notSetWarningAndKey() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        taskConcurrencyMonitor.registerTaskModificationByUser("taskId", "user");
        ControllerResponse controllerResponse = mock(ControllerResponse.class);
        when(controllerResponse.getType()).thenReturn("error");
        // When
        taskConcurrencyMonitor.writeWarningIfEdited("taskId", controllerResponse);
        // Then
        verify(controllerResponse, never()).setType("warning");
        verify(controllerResponse, never()).setKey("taskWasRecentlyEdited");
    }

    @Test
    void writeWarningIfEdited_noPreviousTaskRegistry_notSetWarningAndKey() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        ControllerResponse controllerResponse = mock(ControllerResponse.class);
        when(controllerResponse.getType()).thenReturn("error");
        // When
        taskConcurrencyMonitor.writeWarningIfEdited("taskId", controllerResponse);
        // Then
        verify(controllerResponse, never()).setType("warning");
        verify(controllerResponse, never()).setKey("taskWasRecentlyEdited");
    }

    @Test
    void generateWarningGetCompleteInfo_whenForOperationTrue_createsTaskRegistry() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        ControllerResponse controllerResponse = mock(ControllerResponse.class);
        when(controllerResponse.getType()).thenReturn("success");
        // When
        taskConcurrencyMonitor.generateWarningGetCompleteInfo("taskId", controllerResponse, true, "user");
        // Then
        Map<String, TaskAccessRegistry> taskAccessRegistryMap = extractMonitorRegistry(taskConcurrencyMonitor);
        assertFalse(taskAccessRegistryMap.isEmpty(), "Task access registry map should not be empty");
        TaskAccessRegistry registry = taskAccessRegistryMap.get("taskId");
        assertEquals(new TaskAccessRegistry("taskId", Duration.ofMinutes(15), null), registry, "Created registry should match pattern");
        assertEquals(-880873057, registry.hashCode(), "Created registry should have a valid hash code");
    }

    @Test
    void generateWarningGetCompleteInfo_whenForOperationFalse_notCreatesTaskRegistry() {
        // Given
        TaskConcurrencyMonitor taskConcurrencyMonitor = composeTaskConcurrencyMonitor("PT15M");
        ControllerResponse controllerResponse = mock(ControllerResponse.class);
        when(controllerResponse.getType()).thenReturn("success");
        // When
        taskConcurrencyMonitor.generateWarningGetCompleteInfo("taskId", controllerResponse, false, "user");
        // Then
        Map<String, TaskAccessRegistry> taskAccessRegistryMap = extractMonitorRegistry(taskConcurrencyMonitor);
        assertTrue(taskAccessRegistryMap.isEmpty(), "Task access registry map should be empty");
    }

    private TaskConcurrencyMonitor composeTaskConcurrencyMonitor(String threshold) {
        return composeTaskConcurrencyMonitor(Duration.parse(threshold));
    }

    private TaskConcurrencyMonitor composeTaskConcurrencyMonitor(Duration threshold) {
        ConcurrencyConfigurationProperties configuration = new ConcurrencyConfigurationProperties();
        ReflectionTestUtils.setField(configuration, "threshold", threshold);
        return new TaskConcurrencyMonitor(configuration);
    }

    private Map<String, TaskAccessRegistry> extractMonitorRegistry(TaskConcurrencyMonitor taskConcurrencyMonitor) {
        @SuppressWarnings("unchecked")
        Map<String, TaskAccessRegistry> result =
                (Map<String, TaskAccessRegistry>) ReflectionTestUtils.getField(taskConcurrencyMonitor, "taskAccessRegistryMap");
        return result;
    }

    private Map<LocalDate, String> extractTaskRegistry(TaskAccessRegistry taskRegistry) {
        @SuppressWarnings("unchecked")
        Map<LocalDate, String> result = (Map<LocalDate, String>) ReflectionTestUtils.getField(taskRegistry, "registry");
        return result;
    }
}
