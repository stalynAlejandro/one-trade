package com.pagonxt.onetradefinance.integrations.model.trade;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.document.Document;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * model class for PagoNxt request.
 * Includes data used in some requests of export collections (advance modification,...)
 *
 * @author -
 * @version jdk-11.0.13
 * @see Document
 * @since jdk-11.0.13
 */
@JsonPropertyOrder(alphabetic = true)
public class TradeContract {
    @JsonPropertyOrder(alphabetic = true)
    Map<String, Object> details = new LinkedHashMap<>();

    private String code;

    private String contractReference;

    private Date creationDate;

    private Date approvalDate;

    private String status;

    private String statusReason;

    private String country;

    private String comment;

    private Customer customer;


    /**
     * getter method
     *
     * @return a Map object with the request details
     */
    public Map<String, Object> getDetails() {
        return details;
    }

    /**
     * setter method
     *
     * @param details: a Map object with the request details
     */
    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    /**
     * setter method
     *
     * @param key   : a String object with the key value
     * @param value : an object with the value
     */
    @JsonAnySetter
    void setDetail(String key, Object value) {
        details.put(key, value);
    }

    /**
     * getter method
     *
     * @return a string with the code
     */
    public String getCode() {
        return code;
    }

    /**
     * setter method
     *
     * @param code a string with the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * getter method
     *
     * @return a string with the contract reference
     */
    public String getContractReference() {
        return contractReference;
    }

    /**
     * setter method
     *
     * @param contractReference a string with the contract reference
     */
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    /**
     * getter method
     *
     * @return a Date object with the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * setter method
     *
     * @param creationDate a Date object with the creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * getter method
     *
     * @return a Date object with the approval date
     */
    public Date getApprovalDate() {
        return approvalDate;
    }
    /**
     * setter method
     *
     * @param approvalDate a Date object with the approval date
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }
    /**
     * getter method
     *
     * @return a String with the status
     */
    public String getStatus() {
        return status;
    }
    /**
     * setter method
     *
     * @param status a String with the status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    /**
     * getter method
     *
     * @return a String with the status reason
     */
    public String getStatusReason() {
        return statusReason;
    }
    /**
     * setter method
     *
     * @param statusReason a String with the status reason
     */
    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    /**
     * getter method
     *
     * @return a string with the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter method
     *
     * @param country a string with the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getter method
     *
     * @return a string with the comments
     */
    public String getComment() {
        return comment;
    }

    /**
     * setter method
     *
     * @param comment a string with the comments
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * getter method
     *
     * @return a Customer object
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * setter method
     *
     * @param customer : a Customer object
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeContract)) return false;
        TradeContract that = (TradeContract) o;
        return Objects.equals(getDetails(), that.getDetails()) &&
                Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getContractReference(), that.getContractReference()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getApprovalDate(), that.getApprovalDate()) &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getStatusReason(), that.getStatusReason()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getComment(), that.getComment()) &&
                Objects.equals(getCustomer(), that.getCustomer());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getDetails(), getCode(), getContractReference(), getCreationDate(), getApprovalDate(),
                getStatus(), getStatusReason(), getCountry(), getComment(), getCustomer());
    }
}
