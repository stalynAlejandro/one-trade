package com.pagonxt.onetradefinance.external.backend.concurrency;

import com.pagonxt.onetradefinance.external.backend.configuration.ConcurrencyConfigurationProperties;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This task creates an object to monitor the task concurrency
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class TaskConcurrencyMonitor {

    /**
     * Class attribute
     */
    private final Duration threshold;

    // Avoid concurrent access to model
    private final Object lock = new Object();

    /**
     * Class constructor
     * @param concurrencyConfigurationProperties the task access limit
     */
    public TaskConcurrencyMonitor(ConcurrencyConfigurationProperties concurrencyConfigurationProperties) {
        this.threshold = concurrencyConfigurationProperties.getThreshold();
    }

    private final Map<String, TaskAccessRegistry> taskAccessRegistryMap = new HashMap<>();

    /**
     * This method registers a task modification by user
     * Package access method, to avoid usage from outside the concurrency monitor
     * @param user the user identifier
     */
    public void registerTaskModificationByUser(String taskId, String user) {
        Objects.requireNonNull(taskId, "Task id is null");
        Objects.requireNonNull(user, "User is null");
        synchronized (lock) {
            if (!taskAccessRegistryMap.containsKey(taskId)) {
                taskAccessRegistryMap.put(taskId, new TaskAccessRegistry(taskId, threshold, user));
            } else {
                taskAccessRegistryMap.get(taskId).registerTaskModificationByUser(user);
            }
        }
    }

    /**
     * This method cleans the registry and creates new access log
     * Package access method, to avoid usage from outside the concurrency monitor
     * @return a new task access log
     */
    public TaskAccessLog checkConcurrentTaskAccess(String taskId) {
        Objects.requireNonNull(taskId, "Task id is null");
        synchronized (lock) {
            if (!taskAccessRegistryMap.containsKey(taskId)) {
                return new TaskAccessLog(taskId, threshold);
            }
            return taskAccessRegistryMap.get(taskId).checkConcurrentAccess();
        }
    }

    /**
     * This method warns when a task is being modified (in external app)
     * @param taskId the task id
     * @param result a controllerResponse object
     */
    public void writeWarningIfEdited(String taskId, ControllerResponse result) {
        if ("success".equals(result.getType()) && checkConcurrentTaskAccess(taskId).wasEdited()) {
            result.setType("warning");
            result.setKey("taskWasRecentlyEdited");
        }
    }

    /**
     * This method generates a warning when we have a complete info task
     * @param taskId the task id
     * @param result a controllerResponse object
     * @param forOperation boolean value
     * @param user the user id
     */
    public void generateWarningGetCompleteInfo(
            String taskId, ControllerResponse result, boolean forOperation, String user) {
        writeWarningIfEdited(taskId, result);
        if (forOperation) {
            registerTaskModificationByUser(taskId, user);
        }
    }
}
