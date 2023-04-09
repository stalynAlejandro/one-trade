package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * Model class for advances of export collections
 * Include class attributes, constructors getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see Customer
 * @see ExportCollection
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvance {

    private String code;

    @NotNull
    private Customer customer;

    private Date creationDate;

    private Date approvalDate;

    private String contractReference;

    private Double amount;

    private String currency;

    private ExportCollection exportCollection;

    private Date expirationDate;

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
     * @return a Customer object with the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * setter method
     * @param customer a Customer object with the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
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
     * @return a Date object with the approval date
     */
    public Date getApprovalDate() {
        return approvalDate;
    }

    /**
     * setter method
     * @param approvalDate a Date object with the approval date
     */
    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
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
     * @return a string value with the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a string value with the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * getter method
     * @return an Export Collection object with data about request
     */
    public ExportCollection getExportCollection() {
        return exportCollection;
    }

    /**
     * setter method
     * @param exportCollection an Export Collection object with data about request
     */
    public void setExportCollection(ExportCollection exportCollection) {
        this.exportCollection = exportCollection;
    }

    /**
     * getter method
     * @return a Date object with the expiration date
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * setter method
     * @param expirationDate a Date object with the expiration date
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * constructor method
     * @param code              : a string with the code
     * @param customer          : a Customer object with the customer
     * @param creationDate      : a Date object with the creation date
     * @param approvalDate      : a Date object with the approval date
     * @param contractReference : a string with the contract reference
     * @param amount            : a double value with the amount
     * @param currency          : a string with the currency
     * @param exportCollection  : an Export Collection object with data about request
     * @param expirationDate    : a Date object with the expiration Date
     */
    public ExportCollectionAdvance(String code,
                                   Customer customer,
                                   Date creationDate,
                                   Date approvalDate,
                                   String contractReference,
                                   Double amount,
                                   String currency,
                                   ExportCollection exportCollection,
                                   Date expirationDate) {
        this.code = code;
        this.customer = customer;
        this.creationDate = creationDate;
        this.approvalDate = approvalDate;
        this.contractReference = contractReference;
        this.amount = amount;
        this.currency = currency;
        this.exportCollection = exportCollection;
        this.expirationDate = expirationDate;
    }

    /**
     * empty constructor method
     */
    public ExportCollectionAdvance() {
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
        if (!(o instanceof ExportCollectionAdvance)) {
            return false;
        }
        ExportCollectionAdvance that = (ExportCollectionAdvance) o;
        return Objects.equals(getCode(), that.getCode()) &&
                Objects.equals(getCustomer(), that.getCustomer()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getApprovalDate(), that.getApprovalDate()) &&
                Objects.equals(getContractReference(), that.getContractReference()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getCurrency(), that.getCurrency()) &&
                Objects.equals(getExportCollection(), that.getExportCollection()) &&
                Objects.equals(getExpirationDate(), that.getExpirationDate());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getCustomer(), getCreationDate(), getApprovalDate(),
                getContractReference(), getAmount(), getCurrency(), getExportCollection(), getExpirationDate());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionAdvance{" +
                "code='" + code + '\'' +
                ", customer=" + customer +
                ", creationDate=" + creationDate +
                ", approvalDate=" + approvalDate +
                ", contractReference='" + contractReference + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", exportCollection=" + exportCollection +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
