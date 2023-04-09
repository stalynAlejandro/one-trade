package com.pagonxt.onetradefinance.integrations.model.tasks;

import java.util.Date;
import java.util.Objects;

/**
 * Model class for get data about every item of the list of tasks
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class PagoNxtTaskItem {

    private String rowId;

    private String operationId;

    private String taskId;

    private String mercuryRef;

    private Date issuanceDate;

    private String product;

    private String event;

    private String client;

    private String task;

    private String priority;

    private String status;

    private String requesterName;

    /**
     * getter method
     * @return a string with the row id
     */
    public String getRowId() {
        return rowId;
    }

    /**
     * setter method
     * @param rowId a string with the row id
     */
    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    /**
     * getter method
     * @return a string with the operation id
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * setter method
     * @param operationId a string with the operation id
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
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
     * @return a string with the mercury reference
     */
    public String getMercuryRef() {
        return mercuryRef;
    }

    /**
     * setter method
     * @param mercuryRef a string with the mercury reference
     */
    public void setMercuryRef(String mercuryRef) {
        this.mercuryRef = mercuryRef;
    }

    /**
     * getter method
     * @return a Date object with the issuance date
     */
    public Date getIssuanceDate() {
        return issuanceDate;
    }

    /**
     * setter method
     * @param issuanceDate a Date object with the issuance date
     */
    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    /**
     * getter method
     * @return a string with the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * setter method
     * @param product a string with the product
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * getter method
     * @return a string with the event
     */
    public String getEvent() {
        return event;
    }

    /**
     * setter method
     * @param event a string with the event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * getter method
     * @return a string with the client id
     */
    public String getClient() {
        return client;
    }

    /**
     * setter method
     * @param client a string with the client id
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * getter method
     * @return a string with the task
     */
    public String getTask() {
        return task;
    }

    /**
     * setter method
     * @param task a string with the task
     */
    public void setTask(String task) {
        this.task = task;
    }

    /**
     * getter method
     * @return a string with the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * setter method
     * @param priority a string with the priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * getter method
     * @return a string with the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * setter method
     * @param status a string with the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getter method
     * @return a string with the requester name
     */
    public String getRequesterName() {
        return requesterName;
    }

    /**
     * setter method
     * @param requesterName a string with the requester name
     */
    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
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
        PagoNxtTaskItem that = (PagoNxtTaskItem) o;
        return Objects.equals(rowId, that.rowId) &&
                Objects.equals(operationId, that.operationId) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(mercuryRef, that.mercuryRef) &&
                Objects.equals(issuanceDate, that.issuanceDate) &&
                Objects.equals(product, that.product) &&
                Objects.equals(event, that.event) &&
                Objects.equals(client, that.client) &&
                Objects.equals(task, that.task) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(status, that.status) &&
                Objects.equals(requesterName, that.requesterName);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(rowId, operationId, taskId, mercuryRef, issuanceDate, product,
                event, client, task, priority, status, requesterName);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "PagoNxtTaskItem{" +
                "rowId='" + rowId + '\'' +
                ", operationId='" + operationId + '\'' +
                ", taskId='" + taskId + '\'' +
                ", mercuryRef='" + mercuryRef + '\'' +
                ", issuanceDate=" + issuanceDate +
                ", product='" + product + '\'' +
                ", event='" + event + '\'' +
                ", client='" + client + '\'' +
                ", task='" + task + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", requesterName='" + requesterName + '\'' +
                '}';
    }
}
