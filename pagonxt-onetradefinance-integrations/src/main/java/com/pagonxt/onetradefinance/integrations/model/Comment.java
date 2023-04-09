package com.pagonxt.onetradefinance.integrations.model;

/**
 * Model class for comments
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Comment {

    private String timestamp;
    private boolean visibleForOffice;
    private boolean visibleForClient;
    private String content;
    private String taskId;
    private String taskName;
    private String userId;
    private String userDisplayedName;

    /**
     * getter method
     * @return a string with the time stamp of the comment
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * setter method
     * @param timestamp a string with the time stamp of the comment
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * getter method
     * @return true or false if the comment is visible for office users
     */
    public boolean getVisibleForOffice() {
        return visibleForOffice;
    }

    /**
     * setter method
     * @param visibleForOffice true or false if the comment is visible for office users
     */
    public void setVisibleForOffice(boolean visibleForOffice) {
        this.visibleForOffice = visibleForOffice;
    }

    /**
     * getter method
     * @return true or false if the comment is visible for clients
     */
    public boolean getVisibleForClient() {
        return visibleForClient;
    }

    /**
     * setter method
     * @param visibleForClient true or false if the comment is visible for clients
     */
    public void setVisibleForClient(boolean visibleForClient) {
        this.visibleForClient = visibleForClient;
    }

    /**
     * getter method
     * @return a string with the content of the comment
     */
    public String getContent() {
        return content;
    }

    /**
     * setter method
     * @param content a string with the content of the comment
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * getter method
     * @return a string with the task id
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * setter method
     * @param taskId a string with the task id
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * getter method
     * @return a string with the task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * setter method
     * @param taskName a string with the task name
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * getter method
     * @return a string with the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setter method
     * @param userId a string with the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * getter method
     * @return a string with the user displayed name
     */
    public String getUserDisplayedName() {
        return userDisplayedName;
    }

    /**
     * setter method
     * @param userDisplayedName a string with the user displayed name
     */
    public void setUserDisplayedName(String userDisplayedName) {
        this.userDisplayedName = userDisplayedName;
    }

}
