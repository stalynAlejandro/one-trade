package com.pagonxt.onetradefinance.external.backend.api.model;

/**
 * DTO Class for comments
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CommentDto {

    //class attributes
    private String timestamp;
    private String content;
    private String taskName;
    private String userDisplayedName;

    /**
     * getter method
     * @return the comment time stamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * setter method
     * @param timestamp a string with the comment time stamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * getter method
     * @return the comment content
     */
    public String getContent() {
        return content;
    }

    /**
     * setter method
     * @param content a string with the comment content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * getter method
     * @return the task name of the comment
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * setter method
     * @param taskName a string with the task name of the comment
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * getter method
     * @return the user displayed name
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
