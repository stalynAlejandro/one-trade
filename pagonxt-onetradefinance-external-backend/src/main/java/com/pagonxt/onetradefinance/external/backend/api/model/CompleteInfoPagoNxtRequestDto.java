package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for Complete Info PagoNxt request, when we have a complete info task in Flowable Work
 * A complete info task requires some data of the external application (see user pending tasks)
 * This class creates an object with info about the task (return reason, comment...)
 * Includes some attributes, getters and setters, equals method, hashCode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CompleteInfoPagoNxtRequestDto {

    /**
     * class attributes
     */
    private String returnReason;
    private String returnComment;
    private String requestCreationTime;
    private String requestCreatorName;
    private String taskCreationTime;

    /**
     * Getter Method
     * @return a return reason
     */
    public String getReturnReason() {
        return returnReason;
    }

    /**
     * Setter Method
     * @param returnReason a string with a return reason
     */
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    /**
     * Getter Method
     * @return a comment
     */
    public String getReturnComment() {
        return returnComment;
    }

    /**
     * Setter Method
     * @param returnComment a string with the return comment
     */
    public void setReturnComment(String returnComment) {
        this.returnComment = returnComment;
    }

    /**
     * Getter Method
     * @return the request creation time
     */
    public String getRequestCreationTime() {
        return requestCreationTime;
    }

    /**
     * Setter Method
     * @param requestCreationTime Ea string with the request creation time
     */
    public void setRequestCreationTime(String requestCreationTime) {
        this.requestCreationTime = requestCreationTime;
    }

    /**
     * Getter Method
     * @return the request creator name
     */
    public String getRequestCreatorName() {
        return requestCreatorName;
    }

    /**
     * Setter Method
     * @param requestCreatorName a string with the request creator name
     */
    public void setRequestCreatorName(String requestCreatorName) {
        this.requestCreatorName = requestCreatorName;
    }

    /**
     * Getter Method
     * @return the task creation time
     */
    public String getTaskCreationTime() {
        return taskCreationTime;
    }

    /**
     * Setter Method
     * @param taskCreationTime a strimg with the task creation time
     */
    public void setTaskCreationTime(String taskCreationTime) {
        this.taskCreationTime = taskCreationTime;
    }

    /**
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CompleteInfoPagoNxtRequestDto that = (CompleteInfoPagoNxtRequestDto) o;
        return Objects.equals(returnReason, that.returnReason) &&
                Objects.equals(returnComment, that.returnComment) &&
                Objects.equals(requestCreationTime, that.requestCreationTime) &&
                Objects.equals(requestCreatorName, that.requestCreatorName) &&
                Objects.equals(taskCreationTime, that.taskCreationTime);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(returnReason, returnComment, requestCreationTime, requestCreatorName, taskCreationTime);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "CompleteInfoPagoNxtRequestDto{" +
                "returnReason='" + returnReason + '\'' +
                ", returnComment='" + returnComment + '\'' +
                ", requestCreationTime='" + requestCreationTime + '\'' +
                ", requestCreatorName='" + requestCreatorName + '\'' +
                ", taskCreationTime='" + taskCreationTime + '\'' +
                '}';
    }
}
