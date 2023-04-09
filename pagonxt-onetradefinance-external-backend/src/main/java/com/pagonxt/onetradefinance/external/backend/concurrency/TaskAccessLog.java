package com.pagonxt.onetradefinance.external.backend.concurrency;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This class creates an object to log the access to a task
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class TaskAccessLog {

    /**
     * Class attributes
     */
    private final String taskId;

    private final Duration threshold;

    private final List<String> users;

    /**
     * Class constructor
     * @param taskId task identifier
     * @param threshold access time limit
     */
    public TaskAccessLog(String taskId, Duration threshold) {
        this(taskId, threshold, List.of());
    }

    /**
     * Class constructor
     * @param taskId task identifier
     * @param threshold access time limit
     * @param users list of users that access to task
     */
    public TaskAccessLog(String taskId, Duration threshold, Collection<String> users) {
        this.taskId = taskId;
        this.threshold = threshold;
        this.users = new ArrayList<>(users);
    }

    /**
     * This method checks if the list of users is not empty.
     * @return true or false
     */
    public boolean wasEdited() {
        return !users.isEmpty();
    }

    /**
     * getter method
     * @return the task id
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * getter method
     * @return access time limit
     */
    public Duration getThreshold() {
        return threshold;
    }

    /**
     * getter method
     * @return a list of users
     */
    public Collection<String> getUsers() {
        return users;
    }

    /**
     * Equals method
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
        TaskAccessLog that = (TaskAccessLog) o;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(threshold, that.threshold) &&
                Objects.equals(users, that.users);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(taskId, threshold, users);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "TaskAccessLog{" +
                "taskId='" + taskId + '\'' +
                ", threshold=" + threshold +
                ", users=" + users +
                '}';
    }
}
