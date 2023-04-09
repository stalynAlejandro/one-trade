package com.pagonxt.onetradefinance.integrations.model.requests;

import com.pagonxt.onetradefinance.integrations.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Model class to get info for queries about requests
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.User
 * @since jdk-11.0.13
 */
public class MyRequestsQuery {

    private User requester;

    private String code;

    private List<String> productId = new ArrayList<>();

    private List<String> eventId = new ArrayList<>();

    private List<String> priority = new ArrayList<>();

    private Date fromDate;

    private Date toDate;

    private List<String> currency = new ArrayList<>();

    private Double fromAmount;

    private Double toAmount;

    private String sortField;

    private int sortOrder = 0;

    private Integer fromPage;

    private Integer pageSize;

    private String customerName;

    private String customerPersonNumber;

    private String customerTaxId;

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
     * @return a string with the request code
     */
    public String getCode() {
        return code;
    }

    /**
     * setter method
     * @param code a string with the request code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * getter method
     * @return a list with the product id
     */
    public List<String> getProductId() {
        return productId;
    }

    /**
     * setter method
     * @param productId a list with the product id
     */
    public void setProductId(List<String> productId) {
        this.productId = productId;
    }

    /**
     * getter method
     * @return a list with the event id
     */
    public List<String> getEventId() {
        return eventId;
    }

    /**
     * setter method
     * @param eventId a list with the event id
     */
    public void setEventId(List<String> eventId) {
        this.eventId = eventId;
    }

    /**
     * getter method
     * @return a list with priority
     */
    public List<String> getPriority() {
        return priority;
    }

    /**
     * setter method
     * @param priority a list with priority
     */
    public void setPriority(List<String> priority) {
        this.priority = priority;
    }

    /**
     * getter method
     * @return a Date object with the start date of request
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * setter method
     * @param fromDate a Date object with the start date of request
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * getter method
     * @return a string with the finish date of request
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * setter method
     * @param toDate a string with the finish date of request
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * getter method
     * @return a list with currency
     */
    public List<String> getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a list with currency
     */
    public void setCurrency(List<String> currency) {
        this.currency = currency;
    }

    /**
     * getter method
     * @return a double value with minimum amount
     */
    public Double getFromAmount() {
        return fromAmount;
    }

    /**
     * setter method
     * @param fromAmount a double value with minimum amount
     */
    public void setFromAmount(Double fromAmount) {
        this.fromAmount = fromAmount;
    }


    /**
     * getter method
     * @return a double value with maximum amount
     */
    public Double getToAmount() {
        return toAmount;
    }

    /**
     * setter method
     * @param toAmount a double value with maximum amaunt
     */
    public void setToAmount(Double toAmount) {
        this.toAmount = toAmount;
    }
    /**
     * getter method
     * @return a string with sort field
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * setter method
     * @param sortField a string with sort field
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * getter method
     * @return a string with sort order
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * setter method
     * @param sortOrder a string with sort order
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
     * getter method
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
     * getter method
     * @return a string value with the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * setter method
     * @param customerName a string value with the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * getter method
     * @return a string value with the person number of the customer
     */
    public String getCustomerPersonNumber() {
        return customerPersonNumber;
    }

    /**
     * setter method
     * @param customerPersonNumber a string value with the person number of the customer
     */
    public void setCustomerPersonNumber(String customerPersonNumber) {
        this.customerPersonNumber = customerPersonNumber;
    }

    /**
     * getter method
     * @return a string value with the tax id of the customer
     */
    public String getCustomerTaxId() {
        return customerTaxId;
    }

    /**
     * setter method
     * @param customerTaxId a string value with the tax id of the customer
     */
    public void setCustomerTaxId(String customerTaxId) {
        this.customerTaxId = customerTaxId;
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
        if (!(o instanceof MyRequestsQuery)) {
            return false;
        }
        MyRequestsQuery that = (MyRequestsQuery) o;
        return getSortOrder() == that.getSortOrder()
                && Objects.equals(getRequester(), that.getRequester())
                && Objects.equals(getCode(), that.getCode())
                && Objects.equals(getProductId(), that.getProductId())
                && Objects.equals(getEventId(), that.getEventId())
                && Objects.equals(getPriority(), that.getPriority())
                && Objects.equals(getFromDate(), that.getFromDate())
                && Objects.equals(getToDate(), that.getToDate())
                && Objects.equals(getCurrency(), that.getCurrency())
                && Objects.equals(getFromAmount(), that.getFromAmount())
                && Objects.equals(getToAmount(), that.getToAmount())
                && Objects.equals(getSortField(), that.getSortField())
                && Objects.equals(getFromPage(), that.getFromPage())
                && Objects.equals(getPageSize(), that.getPageSize())
                && Objects.equals(getCustomerName(), that.getCustomerName())
                && Objects.equals(getCustomerPersonNumber(), that.getCustomerPersonNumber())
                && Objects.equals(getCustomerTaxId(), that.getCustomerTaxId());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getRequester(), getCode(), getProductId(), getEventId(),
                getPriority(), getFromDate(), getToDate(), getCurrency(),
                getFromAmount(), getToAmount(), getSortField(), getSortOrder(),
                getFromPage(), getPageSize(), getCustomerName(), getCustomerPersonNumber(), getCustomerTaxId());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "MyRequestsQuery{" +
                "requester=" + requester +
                ", code='" + code + '\'' +
                ", productId=" + productId +
                ", eventId=" + eventId +
                ", priority=" + priority +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", currency=" + currency +
                ", fromAmount=" + fromAmount +
                ", toAmount=" + toAmount +
                ", sortField='" + sortField + '\'' +
                ", sortOrder=" + sortOrder +
                ", fromPage=" + fromPage +
                ", pageSize=" + pageSize +
                ", customerName='" + customerName + '\'' +
                ", customerPersonNumber='" + customerPersonNumber + '\'' +
                ", customerTaxId='" + customerTaxId + '\'' +
                '}';
    }
}
