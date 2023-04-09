package com.pagonxt.onetradefinance.integrations.model.tasks;

import com.pagonxt.onetradefinance.integrations.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Model class to get info for queries about tasks
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.User
 * @since jdk-11.0.13
 */
public class MyTasksQuery {

    private String scope;

    private User requester;

    private String code;

    private String customerName;

    private String customerPersonNumber;

    private String customerTaxId;

    private String requesterName;

    private String ownerName;

    private List<String> productId = new ArrayList<>();

    private List<String> eventId = new ArrayList<>();

    private List<String> taskId = new ArrayList<>();

    private List<String> priority = new ArrayList<>();

    private List<String> status = new ArrayList<>();

    private Date fromDate;

    private Date toDate;

    private List<String> currency = new ArrayList<>();

    private Double fromAmount;

    private Double toAmount;

    private String sortField;

    private int sortOrder = 0;

    private Integer fromPage;

    private Integer pageSize;

    /**
     * getter method
     * @return a string with the scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * setter method
     * @param scope a string with the scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * getter method
     * @return a User object with the requester
     */
    public User getRequester() {
        return requester;
    }

    /**
     * setter method
     * @param requester a User object with the requester
     */
    public void setRequester(User requester) {
        this.requester = requester;
    }

    /**
     * getter method
     * @return a string with the code
     */
    public String getCode() {
        return code;
    }

    /**
     * setter method
     * @param code a string with the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * getter method
     * @return a string with the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * setter method
     * @param customerName a string with the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * getter method
     * @return a string with the person number of the customer
     */
    public String getCustomerPersonNumber() {
        return customerPersonNumber;
    }

    /**
     * setter method
     * @param customerPersonNumber a string with the person number of the customer
     */
    public void setCustomerPersonNumber(String customerPersonNumber) {
        this.customerPersonNumber = customerPersonNumber;
    }

    /**
     * getter method
     * @return a string with tax id of the customer
     */
    public String getCustomerTaxId() {
        return customerTaxId;
    }

    /**
     * setter method
     * @param customerTaxId a string with tax id of the customer
     */
    public void setCustomerTaxId(String customerTaxId) {
        this.customerTaxId = customerTaxId;
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
     * @return a string with the owner name
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * setter method
     * @param ownerName a string with the owner name
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * getter method
     * @return a list of product id
     */
    public List<String> getProductId() {
        return productId;
    }

    /**
     * setter method
     * @param productId a list of product id
     */
    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    /**
     * getter method
     * @return a list of event id
     */
    public List<String> getEventId() {
        return eventId;
    }

    /**
     * setter method
     * @param eventId a list of event id
     */
    public void setEventId(List<String> eventId) {
        this.eventId = eventId;
    }

    /**
     * getter method
     * @return a list of task id
     */
    public List<String> getTaskId() {
        return taskId;
    }

    /**
     * setter method
     * @param taskId a list of task id
     */
    public void setTaskId(List<String> taskId) {
        this.taskId = taskId;
    }

    /**
     * getter method
     * @return a list of priorities
     */
    public List<String> getPriority() {
        return priority;
    }

    /**
     * setter method
     * @param priority a list of priorities
     */
    public void setPriority(List<String> priority) {
        this.priority = priority;
    }

    /**
     * getter method
     * @return a list of status
     */
    public List<String> getStatus() {
        return status;
    }

    /**
     * setter method
     * @param status a list of status
     */
    public void setStatus(List<String> status) {
        this.status = status;
    }

    /**
     * getter method
     * @return a Date object with from date
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * setter method
     * @param fromDate a Date object with from date
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * getter method
     * @return a Date object with to date
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * setter method
     * @param toDate a Date object with to date
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }
    /**
     * getter method
     * @return a list of currencies
     */
    public List<String> getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a list of currencies
     */
    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    /**
     * getter method
     * @return a double value with the minimum amount
     */
    public Double getFromAmount() {
        return fromAmount;
    }

    /**
     * setter method
     * @param fromAmount a double value with the minimum amount
     */
    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }

    /**
     * getter method
     * @return a double value with the maximum amount
     */
    public Double getToAmount() {
        return toAmount;
    }

    /**
     * setter method
     * @param toAmount a double value with the maximum amount
     */
    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }

    /**
     * getter method
     * @return a string with the sort field
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * setter method
     * @param sortField a string with the sort field
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * getter method
     * @return a integer value with the sort order
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * setter method
     * @param sortOrder a string with the sort order
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * getter method
     * @return an integer value with page where to start the list
     */
    public Integer getFromPage() {
        return fromPage;
    }

    /**
     * setter method
     * @param fromPage an integer value with page where to start the list
     */
    public void setFromPage(Integer fromPage) {
        this.fromPage = fromPage;
    }

    /**
     * getter methdod
     * @return an integer value with the numbers of elements to show in the list
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * setter method
     * @param pageSize an integer value with the numbers of elements to show in the list
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
        MyTasksQuery that = (MyTasksQuery) o;
        return sortOrder == that.sortOrder &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(requester, that.requester) &&
                Objects.equals(code, that.code) &&
                Objects.equals(customerName, that.customerName) &&
                Objects.equals(customerPersonNumber, that.customerPersonNumber) &&
                Objects.equals(customerTaxId, that.customerTaxId) &&
                Objects.equals(requesterName, that.requesterName) &&
                Objects.equals(ownerName, that.ownerName) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(taskId, that.taskId) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(status, that.status) &&
                Objects.equals(fromDate, that.fromDate) &&
                Objects.equals(toDate, that.toDate) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(fromAmount, that.fromAmount) &&
                Objects.equals(toAmount, that.toAmount) &&
                Objects.equals(sortField, that.sortField) &&
                Objects.equals(fromPage, that.fromPage) &&
                Objects.equals(pageSize, that.pageSize);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(scope, requester, code, customerName, customerPersonNumber, customerTaxId, requesterName,
                ownerName, productId, eventId, taskId, priority, status, fromDate, toDate, currency, fromAmount,
                toAmount, sortField, sortOrder, fromPage, pageSize);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "MyTasksQuery{" +
                "scope='" + scope + '\'' +
                ", requester=" + requester +
                ", code='" + code + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPersonNumber='" + customerPersonNumber + '\'' +
                ", customerTaxId='" + customerTaxId + '\'' +
                ", requesterName='" + requesterName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", productId=" + productId +
                ", eventId=" + eventId +
                ", taskId=" + taskId +
                ", priority=" + priority +
                ", status=" + status +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", currency=" + currency +
                ", fromAmount=" + fromAmount +
                ", toAmount=" + toAmount +
                ", sortField='" + sortField + '\'' +
                ", sortOrder=" + sortOrder +
                ", fromPage=" + fromPage +
                ", pageSize=" + pageSize +
                '}';
    }
}
