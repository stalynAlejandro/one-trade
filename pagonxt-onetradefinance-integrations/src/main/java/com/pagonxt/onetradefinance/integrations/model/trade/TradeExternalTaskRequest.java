package com.pagonxt.onetradefinance.integrations.model.trade;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.Objects;
@JsonPropertyOrder(alphabetic = true)
public class TradeExternalTaskRequest {

    //member variables
    private String returnReason;
    private String returnComment;
    private Date taskCreationTime;
    private TradeRequest request;
    private Date requestCreationTime;
    private String requestCreatorName;

    /**
     * getter method
     * @return a String object with the return reason
     */
    public String getReturnReason() {
        return returnReason;
    }

    /**
     * setter method
     * @param returnReason: a String object with the return reason
     */
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    /**
     * getter method
     * @return a String object with the return comment
     */
    public String getReturnComment() {
        return returnComment;
    }

    /**
     * setter method
     * @param returnComment: a String object with the return comment
     */
    public void setReturnComment(String returnComment) {
        this.returnComment = returnComment;
    }

    /**
     * getter method
     * @return a Date object with the creation time of the task
     */
    public Date getTaskCreationTime() {
        return taskCreationTime;
    }

    /**
     * setter method
     * @param taskCreationTime: a Date object with the creation time of the task
     */
    public void setTaskCreationTime(Date taskCreationTime) {
        this.taskCreationTime = taskCreationTime;
    }

    /**
     * getter method
     * @return a TradeRequest object with the request data
     */
    public TradeRequest getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request: a TradeRequest object with the request data
     */
    public void setRequest(TradeRequest request) {
        this.request = request;
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
     * @param requestCreationTime: a Date object with the creation time of the request
     */
    public void setRequestCreationTime(Date requestCreationTime) {
        this.requestCreationTime = requestCreationTime;
    }

    /**
     * getter method
     * @return a String with the name of the request creator
     */
    public String getRequestCreatorName() {
        return requestCreatorName;
    }

    /**
     * setter method
     * @param requestCreatorName: a String with the name of the request creator
     */
    public void setRequestCreatorName(String requestCreatorName) {
        this.requestCreatorName = requestCreatorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TradeExternalTaskRequest that = (TradeExternalTaskRequest) o;
        return Objects.equals(returnReason, that.returnReason) &&
                Objects.equals(returnComment, that.returnComment) &&
                Objects.equals(taskCreationTime, that.taskCreationTime) &&
                Objects.equals(request, that.request) &&
                Objects.equals(requestCreationTime, that.requestCreationTime) &&
                Objects.equals(requestCreatorName, that.requestCreatorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(returnReason, returnComment, taskCreationTime,
                request, requestCreationTime, requestCreatorName);
    }
}
