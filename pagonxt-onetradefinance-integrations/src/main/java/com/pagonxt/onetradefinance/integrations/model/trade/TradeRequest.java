package com.pagonxt.onetradefinance.integrations.model.trade;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.document.Document;

import javax.validation.constraints.Size;
import java.util.*;

/**
 * model class for PagoNxt request.
 * Includes data used in some requests of export collections (advance modification,...)
 * @author -
 * @version jdk-11.0.13
 * @see Document
 * @since jdk-11.0.13
 */
@JsonPropertyOrder(alphabetic = true)
public class TradeRequest extends AuthenticatedRequest {
    @JsonPropertyOrder(alphabetic = true)
    Map<String, Object> details = new LinkedHashMap<>();

    private String code;

    private Date creationDate;

    private String product;

    private String event;

    private String country;
    @JsonPropertyOrder(alphabetic = true)
    private List<Document> documents = new ArrayList<>();

   private String priority;

    private String office;

    private String middleOffice;

    @Size(max = 500)
    private String comment;

    private Date slaEnd;

    private String displayedStatus;

    private Integer savedStep;

    private Customer customer;

    private String resolution;

    private String resolutionReason;

    /**
     * getter method
     * @return a Map object with the request details
     */
    public Map<String, Object> getDetails() {
        return details;
    }

    /**
     * setter method
     * @param details: a Map object with the request details
     */
    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    /**
     * setter method
     * @param key   : a String object with the key value
     * @param value : an object with the value
     */
    @JsonAnySetter
    void setDetail(String key, Object value) {
        details.put(key, value);
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
     * @return a Date object with the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * setter method
     * @param creationDate a Date object with the creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
     * @return a string with the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter method
     * @param country a string with the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getter method
     * @return a list of documents
     */
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * setter method
     * @param documents  a list of documents
     */
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
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
     * @return a string with the office code
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the office code
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return a string with the middle office code
     */
    public String getMiddleOffice() {
        return middleOffice;
    }

    /**
     * setter method
     * @param middleOffice a string with the middle office code
     */
    public void setMiddleOffice(String middleOffice) {
        this.middleOffice = middleOffice;
    }

    /**
     * getter method
     * @return a string with the comments
     */
    public String getComment() {
        return comment;
    }

    /**
     * setter method
     * @param comment a string with the comments
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * getter method
     * @return a date object with the end date of the SLA
     */
    public Date getSlaEnd() {
        return slaEnd;
    }

    /**
     * setter method
     * @param slaEnd a date object with the end date of the SLA
     */
    public void setSlaEnd(Date slaEnd) {
        this.slaEnd = slaEnd;
    }

    /**
     * getter method
     * @return a string with the displayed status
     */
    public String getDisplayedStatus() {
        return displayedStatus;
    }

    /**
     * setter method
     * @param displayedStatus a string with the displayed status
     */
    public void setDisplayedStatus(String displayedStatus) {
        this.displayedStatus = displayedStatus;
    }

    /**
     * getter method
     * @return an Integer object with the saved step
     */
    public Integer getSavedStep() {
        return savedStep;
    }

    /**
     * setter method
     * @param savedStep : an Integer object with the saved step
     */
    public void setSavedStep(Integer savedStep) {
        this.savedStep = savedStep;
    }

    /**
     * getter method
     * @return a Customer object
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * setter method
     * @param customer : a Customer object
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * getter method
     * @return a String object with the resolution
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * setter method
     * @param resolution: a String object with the resolution
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * getter method
     * @return a String object with the resolution reason
     */
    public String getResolutionReason() {
        return resolutionReason;
    }

    /**
     * setter method
     * @param resolutionReason: a String object with the resolution reason
     */
    public void setResolutionReason(String resolutionReason) {
        this.resolutionReason = resolutionReason;
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
        if (!super.equals(o)) {
            return false;
        }
        TradeRequest that = (TradeRequest) o;
        return Objects.equals(details, that.details) &&
                Objects.equals(code, that.code) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(product, that.product) &&
                Objects.equals(event, that.event) &&
                Objects.equals(country, that.country) &&
                Objects.equals(documents, that.documents) &&
                Objects.equals(priority, that.priority) &&
                Objects.equals(office, that.office) &&
                Objects.equals(middleOffice, that.middleOffice) &&
                Objects.equals(comment, that.comment) &&
                Objects.equals(slaEnd, that.slaEnd) &&
                Objects.equals(displayedStatus, that.displayedStatus) &&
                Objects.equals(savedStep, that.savedStep) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(resolution, that.resolution) &&
                Objects.equals(resolutionReason, that.resolutionReason);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), details, code, creationDate, product, event,
                country, documents, priority, office, middleOffice, comment, slaEnd, displayedStatus,
                savedStep, customer, resolution, resolutionReason);
    }



}
