package com.pagonxt.onetradefinance.external.backend.concurrency;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class TaskAccessRegistryTest {

    @Test
    void constructor_nullUser_createsEmptyRegistry() {
        // When
        TaskAccessRegistry result = new TaskAccessRegistry("taskId", Duration.ofMinutes(1), null);

        // Then
        Map<LocalDate, String> registry = extractTaskRegistry(result);
        assertTrue(registry.isEmpty(), "Registry should be empty");
    }

    @Test
    void constructor_withUser_createsRegistryWithUser() {
        // Given
        String user = "user";

        // When
        TaskAccessRegistry result = new TaskAccessRegistry("taskId", Duration.ofMinutes(1), user);

        // Then
        Map<LocalDate, String> registry = extractTaskRegistry(result);
        assertFalse(registry.isEmpty(), "Registry should not be empty");
    }

    @Test
    void checkConcurrentAccess_emptyRegistry_returnsEmptyLog() {
        // Given
        TaskAccessRegistry registry = new TaskAccessRegistry("taskId", Duration.ofMinutes(1), null);

        // When
        TaskAccessLog result = registry.checkConcurrentAccess();

        // Then
        assertFalse(result.wasEdited(), "Result should be empty");
        assertEquals("taskId", result.getTaskId(), "Result should have a valid taskId");
        assertEquals(Duration.ofMinutes(1), result.getThreshold(), "Result should have a valid threshold");
        assertEquals(0, result.getUsers().size(), "Result should have an empty list of users");
        assertEquals(-410448604, result.hashCode(), "Result should have a valid hash code");
        assertEquals("TaskAccessLog{taskId='taskId', threshold=PT1M, users=[]}", result.toString(), "Result should be represented as a valid string");
        assertEquals("TaskAccessRegistry{taskId='taskId', threshold=PT1M, registry={}}", registry.toString(), "Registry should have a valid String representation");
    }

    @Test
    void checkConcurrentAccess_existsUserInRegistry_returnsValidData() {
        // Given
        TaskAccessRegistry registry = new TaskAccessRegistry("taskId", Duration.ofMinutes(1), "user");

        // When
        TaskAccessLog result = registry.checkConcurrentAccess();

        // Then
        assertTrue(result.wasEdited(), "Result should not be empty");
    }

    @Test
    void registerTaskModificationByUser_addsUserToRegistry() {
        // Given
        TaskAccessRegistry registry = new TaskAccessRegistry("taskId", Duration.ofMinutes(1), null);

        // When
        registry.registerTaskModificationByUser("user");

        // Then
        Map<LocalDate, String> registryMap = extractTaskRegistry(registry);
        assertFalse(registryMap.isEmpty(), "Registry should not be empty");
    }

    @Test
    void cleanRegistry_nonEmptyRegistry_registryGetsCleaned() throws Exception {
        // Given
        TaskAccessRegistry registry = new TaskAccessRegistry("taskId", Duration.ofMillis(500), "user");

        // When
        TimeUnit.SECONDS.sleep(1);
        ReflectionTestUtils.invokeMethod(registry, "cleanRegistry");

        // Then
        Map<LocalDate, String> registryMap = extractTaskRegistry(registry);
        assertTrue(registryMap.isEmpty(), "Result should be empty");
    }

    private Map<LocalDate, String> extractTaskRegistry(TaskAccessRegistry registry) {
        @SuppressWarnings("unchecked")
        Map<LocalDate, String> result = (Map<LocalDate, String>) ReflectionTestUtils.getField(registry, "registry");
        return result;
    }
}
