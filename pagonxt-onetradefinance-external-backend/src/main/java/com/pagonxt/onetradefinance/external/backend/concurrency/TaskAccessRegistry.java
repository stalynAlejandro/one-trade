package com.pagonxt.onetradefinance.external.backend.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class creates an object to log access to a task
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class TaskAccessRegistry {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(TaskAccessRegistry.class);

    /**
     * Class attributes
     * TaskId exists for comparison purposes only
     */
    private final String taskId;

    private final Duration threshold;

    private final Map<LocalDateTime, String> registry = new HashMap<>();

    /**
     * Class constructor
     * Package access constructor, to avoid usage from outside the concurrency monitor
     * @param taskId the task identifier
     * @param threshold access durations limit
     * @param userId the user identifier
     */
    TaskAccessRegistry(String taskId, Duration threshold, String userId) {
        LOG.debug("new TaskAccessRegistry(taskId: {}, threshold: {}, userId: {})", taskId, threshold, userId);
        this.taskId = taskId;
        this.threshold = threshold;
        if (userId != null) {
            registerTaskModificationByUser(userId);
        }
    }

    /**
     * This method cleans the registry and creates new access log
     * Package access method, to avoid usage from outside the concurrency monitor
     * @return a new task access log
     */
    TaskAccessLog checkConcurrentAccess() {
        LOG.debug("TaskAccessRegistry.checkConcurrentAccess()");
        cleanRegistry();
        return new TaskAccessLog(taskId, threshold, registry.values());
    }

    /**
     * This method registers a task modification by user
     * Package access method, to avoid usage from outside the concurrency monitor
     * @param user the user identifier
     */
    void registerTaskModificationByUser(String user) {
        LOG.debug("TaskAccessRegistry.registerTaskModificationByUser(user: {})", user);
        // Key collisions can occur, specially when running tests. Ensure a different timestamp each time:
        LocalDateTime key = LocalDateTime.now();
        while (registry.containsKey(key)) {
            // It is OK if this block does not get covered by tests
            key = key.plusNanos(1);
            LOG.debug("TaskAccessRegistry.registerTaskModificationByUser -" +
                    " key collision detected, adding 1 nano to new key");
        }
        registry.put(key, user);
    }

    /**
     * This method cleans the registry
     */
    private void cleanRegistry() {
        LOG.debug("TaskAccessRegistry.cleanRegistry()");
        if (registry.isEmpty()) {
            LOG.debug("TaskAccessRegistry.cleanRegistry - registry is empty");
            return;
        }
        int startSize = registry.size();
        LocalDateTime expirationLimit = LocalDateTime.now().minus(threshold);
        List<Map.Entry<LocalDateTime, String>> expiredEntries = registry.entrySet().stream()
                .filter(item -> expirationLimit.isAfter(item.getKey()))
                .collect(Collectors.toList());
        expiredEntries.forEach(item -> registry.remove(item.getKey(), item.getValue()));
        LOG.debug("TaskAccessRegistry.cleanRegistry - before cleanup, registry had {} entries;" +
                " now it has {}", startSize, registry.size());
    }

    /**
     * Equals method
     * Only use taskId for comparison purposes
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TaskAccessRegistry that = (TaskAccessRegistry) o;
        return Objects.equals(taskId, that.taskId);
    }

    /**
     * hashCode method
     * Only use taskId for comparison purposes
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "TaskAccessRegistry{" +
                "taskId='" + taskId + '\'' +
                ", threshold=" + threshold +
                ", registry=" + registry +
                '}';
    }
}
