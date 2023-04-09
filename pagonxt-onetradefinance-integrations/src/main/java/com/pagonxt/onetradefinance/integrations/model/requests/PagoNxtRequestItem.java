package com.pagonxt.onetradefinance.integrations.model.requests;

import java.util.Date;
import java.util.Objects;

/**
 * Model class for get data about every item of the list of requests
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class PagoNxtRequestItem {

    private String rowId;

    private String mercuryRef;

    private String operationId;

    private Date issuanceDate;

    private String product;

    private String event;

    private String task;

    private String priority;

    private String status;

    private Double amount;

    private String currency;

    private String contractReference;

    private Date requestedDate;

    private String client;

    private String requesterName;

    private String office;

    private String resolution;

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
     * @return a Date object with the issuance data
     */
    public Date getIssuanceDate() {
        return issuanceDate;
    }

    /**
     * setter method
     * @param issuanceDate a Date object with the issuance data
     */
    public void setIssuanceDate(Date issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    /**
     * getter method
     * @return a string with product
     */
    public String getProduct() {
        return product;
    }

    /**
     * setter method
     * @param product a string with product
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
     * @return a string with the task id
     */
    public String getTask() {
        return task;
    }

    /**
     * setter method
     * @param task a string with the task id
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
     * @return a string with the request status
     */
    public String getStatus() {
        return status;
    }

    /**
     * setter method
     * @param status a string with the request status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getter method
     * @return a double value with the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * setter method
     * @param amount a double value with the amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * getter method
     * @return a string with the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a string with the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * getter method
     * @return a string with the contract reference
     */
    public String getContractReference() {
        return contractReference;
    }

    /**
     * setter method
     * @param contractReference a string with the contract reference
     */
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    /**
     * getter method
     * @return a Date object with the requested date
     */
    public Date getRequestedDate() {
        return requestedDate;
    }

    /**
     * setter method
     * @param requestedDate a Date object with the requested date
     */
    public void setRequestedDate(Date requestedDate) {
        this.requestedDate = requestedDate;
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
     * getter method
     * @return a string with the office number
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the office number
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return a string with the resolution
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * setter method
     * @param resolution a string with the resolution
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
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
        if (!(o instanceof PagoNxtRequestItem)) {
            return false;
        }
        PagoNxtRequestItem that = (PagoNxtRequestItem) o;
        return Objects.equals(getRowId(), that.getRowId())
                && Objects.equals(getMercuryRef(), that.getMercuryRef())
                && Objects.equals(getOperationId(), that.getOperationId())
                && Objects.equals(getIssuanceDate(), that.getIssuanceDate())
                && Objects.equals(getProduct(), that.getProduct())
                && Objects.equals(getEvent(), that.getEvent())
                && Objects.equals(getTask(), that.getTask())
                && Objects.equals(getPriority(), that.getPriority())
                && Objects.equals(getStatus(), that.getStatus())
                && Objects.equals(getAmount(), that.getAmount())
                && Objects.equals(getCurrency(), that.getCurrency())
                && Objects.equals(getContractReference(), that.getContractReference())
                && Objects.equals(getRequestedDate(), that.getRequestedDate())
                && Objects.equals(getClient(), that.getClient())
                && Objects.equals(getRequesterName(), that.getRequesterName())
                && Objects.equals(getOffice(), that.getOffice())
                && Objects.equals(getResolution(), that.getResolution());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getRowId(), getMercuryRef(), getOperationId(),
                getIssuanceDate(), getProduct(), getEvent(), getTask(), getPriority(),
                getStatus(), getAmount(), getCurrency(), getContractReference()
                , getRequestedDate(), getClient(), getRequesterName(), getOffice(), getResolution());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "PagoNxtRequestItem{" +
                "rowId='" + rowId + '\'' +
                ", mercuryRef='" + mercuryRef + '\'' +
                ", operationId='" + operationId + '\'' +
                ", issuanceDate=" + issuanceDate +
                ", product='" + product + '\'' +
                ", event='" + event + '\'' +
                ", task='" + task + '\'' +
                ", priority='" + priority + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", contractReference='" + contractReference + '\'' +
                ", requestedDate=" + requestedDate +
                ", client='" + client + '\'' +
                ", requesterName='" + requesterName + '\'' +
                ", office='" + office + '\'' +
                ", resolution='" + resolution + '\'' +
                '}';
    }
}
