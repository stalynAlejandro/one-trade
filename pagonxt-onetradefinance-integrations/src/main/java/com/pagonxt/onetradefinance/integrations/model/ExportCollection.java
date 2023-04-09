package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

/**
 * Model class for export collections
 * Includes generic data for the requests of export collection (advance, new request,...)
 * Include class attributes,constructors getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see Customer
 * @see Account
 * @since jdk-11.0.13
 */
public class ExportCollection {

    private String code;

    @NotNull
    private Customer customer;

    private Date creationDate;

    private Date approvalDate;

    private String contractReference;

    private Double amount;

    private String currency;

    private Account nominalAccount;

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
     * @return an account object with the nominal account
     */
    public Account getNominalAccount() {
        return nominalAccount;
    }

    /**
     * setter method
     * @param nominalAccount an account object with the nominal account
     */
    public void setNominalAccount(Account nominalAccount) {
        this.nominalAccount = nominalAccount;
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
     * @param nominalAccount    : an Account object with the nominal account
     */
    public ExportCollection(String code,
                            Customer customer,
                            Date creationDate,
                            Date approvalDate,
                            String contractReference,
                            Double amount,
                            String currency,
                            Account nominalAccount) {
        this.code = code;
        this.customer = customer;
        this.creationDate = creationDate;
        this.approvalDate = approvalDate;
        this.contractReference = contractReference;
        this.amount = amount;
        this.currency = currency;
        this.nominalAccount = nominalAccount;
    }

    /**
     * empty constructor method
     */
    public ExportCollection() {
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
        ExportCollection that = (ExportCollection) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(customer, that.customer) &&
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(approvalDate, that.approvalDate) &&
                Objects.equals(contractReference, that.contractReference) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(nominalAccount, that.nominalAccount);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(code, customer, creationDate,
                approvalDate, contractReference, amount, currency, nominalAccount);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollection{" +
                "code='" + code + '\'' +
                ", customer=" + customer +
                ", creationDate=" + creationDate +
                ", approvalDate=" + approvalDate +
                ", contractReference='" + contractReference + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", nominalAccount=" + nominalAccount +
                '}';
    }
}
