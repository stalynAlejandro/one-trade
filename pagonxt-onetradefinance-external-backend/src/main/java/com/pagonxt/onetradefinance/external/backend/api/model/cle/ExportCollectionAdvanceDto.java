package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;

import java.util.Objects;

/**
 * DTO Class for export collection advance
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceDto {

    //Class attributes
    private String code;

    private CustomerDto customer;

    private String creationDate;

    private String approvalDate;

    private String contractReference;

    private String amount;

    private String currency;

    private ExportCollectionDto exportCollection;

    private String requestExpiration;

    /**
     * getter method
     * @return the request code
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
     * @return the request customer
     */
    public CustomerDto getCustomer() {
        return customer;
    }

    /**
     * setter method
     * @param customer an object with the request customer
     */
    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    /**
     * getter method
     * @return the request creation date
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * setter method
     * @param creationDate a string with the request creation date
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * getter method
     * @return the request approval date
     */
    public String getApprovalDate() {
        return approvalDate;
    }

    /**
     * setter emthod
     * @param approvalDate a string with the request approval date
     */
    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    /**
     * getter method
     * @return the request contract reference
     */
    public String getContractReference() {
        return contractReference;
    }

    /**
     * setter method
     * @param contractReference a string with the request contract reference
     */
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    /**
     * getter method
     * @return the request amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * setter method
     * @param amount a string with request amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * getter method
     * @return the request currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a string with the request currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * getter method
     * @return a ExportCollection object (export collection referenced)
     */
    public ExportCollectionDto getExportCollection() {
        return exportCollection;
    }

    /**
     * setter method
     * @param exportCollection a ExportCollection object (export collection referenced)
     */
    public void setExportCollection(ExportCollectionDto exportCollection) {
        this.exportCollection = exportCollection;
    }

    /**
     * getter method
     * @return the request expiration date
     */
    public String getRequestExpiration() {
        return requestExpiration;
    }

    /**
     * setter method
     * @param requestExpiration a string with the request expiration date
     */
    public void setRequestExpiration(String requestExpiration) {
        this.requestExpiration = requestExpiration;
    }

    /**
     * Class constructor
     * @param code export collection advance code
     * @param customer customer id
     * @param creationDate export collection advance creation date
     * @param approvalDate export collection advance approval date
     * @param contractReference export collection advance contract reference
     * @param amount advance amount
     * @param currency advance currency
     * @param exportCollection export collection reference
     * @param requestExpiration export collection advance request expiration
     */
    public ExportCollectionAdvanceDto(String code, CustomerDto customer, String creationDate,
                                      String approvalDate, String contractReference,
                                      String amount, String currency,
                                      ExportCollectionDto exportCollection, String requestExpiration) {
        this.code = code;
        this.customer = customer;
        this.creationDate = creationDate;
        this.approvalDate = approvalDate;
        this.contractReference = contractReference;
        this.amount = amount;
        this.currency = currency;
        this.exportCollection = exportCollection;
        this.requestExpiration = requestExpiration;
    }

    /**
     * Empty class constructor
     */
    public ExportCollectionAdvanceDto() {
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
        if (!(o instanceof ExportCollectionAdvanceDto)) {
            return false;
        }
        ExportCollectionAdvanceDto that = (ExportCollectionAdvanceDto) o;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getCustomer(), that.getCustomer()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getApprovalDate(), that.getApprovalDate()) &&
                Objects.equals(getContractReference(), that.getContractReference()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getCurrency(), that.getCurrency()) &&
                Objects.equals(getExportCollection(), that.getExportCollection()) &&
                Objects.equals(getRequestExpiration(), that.getRequestExpiration());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getCustomer(), getCreationDate(),
                getApprovalDate(), getContractReference(), getAmount(),
                getCurrency(), getExportCollection(), getRequestExpiration());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionAdvanceDto{" +
                "code='" + code + '\'' +
                ", customer=" + customer +
                ", creationDate='" + creationDate + '\'' +
                ", approvalDate='" + approvalDate + '\'' +
                ", contractReference='" + contractReference + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", exportCollection=" + exportCollection +
                ", requestExpiration='" + requestExpiration + '\'' +
                '}';
    }
}
