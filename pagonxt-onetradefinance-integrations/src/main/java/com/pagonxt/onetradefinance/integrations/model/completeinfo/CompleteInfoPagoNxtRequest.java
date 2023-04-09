package com.pagonxt.onetradefinance.integrations.model.completeinfo;

import java.util.Date;

/**
 * Model class for a request of export collection
 * This class provides generic information that is used by the rest of the classes in this package.
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CompleteInfoPagoNxtRequest {

    private String returnReason;
    private String returnComment;
    private Date requestCreationTime;
    private String requestCreatorName;
    private Date taskCreationTime;

    /**
     * getter method
     * @return a string with the return reason
     */
    public String getReturnReason() {
        return returnReason;
    }

    /**
     * setter method
     * @param returnReason a string with the return reason
     */
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    /**
     * getter method
     * @return a string with the return comment
     */
    public String getReturnComment() {
        return returnComment;
    }

    /**
     * setter method
     * @param returnComment a string with the return comment
     */
    public void setReturnComment(String returnComment) {
        this.returnComment = returnComment;
    }

    /**
     * getter method
     * @return a Date object with the creation time of the request
     */
    public Date getRequestCreationTime() {
        return requestCreationTime;
    }

    /**
     * setter method
     * @param requestCreationTime a Date object with the creation time of the request
     */
    public void setRequestCreationTime(Date requestCreationTime) {
        this.requestCreationTime = requestCreationTime;
    }

    /**
     * getter method
     * @return a string with the creator name of the request
     */
    public String getRequestCreatorName() {
        return requestCreatorName;
    }

    /**
     * setter method
     * @param requestCreatorName a string with the creator name of the request
     */
    public void setRequestCreatorName(String requestCreatorName) {
        this.requestCreatorName = requestCreatorName;
    }

    /**
     * getter method
     * @return a Date object with the creation time of the request
     */
    public Date getTaskCreationTime() {
        return taskCreationTime;
    }

    /**
     * setter method
     * @param taskCreationTime a Date object with the creation time of the request
     */
    public void setTaskCreationTime(Date taskCreationTime) {
        this.taskCreationTime = taskCreationTime;
    }
}
