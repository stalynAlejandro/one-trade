package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;

import java.util.Objects;

/**
 * DTO Class for export collection
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.AccountDto
 * @since jdk-11.0.13
 */
public class ExportCollectionDto {

    //Class Attributes
    private String code;

    private CustomerDto customer;

    private String creationDate;

    private String approvalDate;

    private String contractReference;

    private String amount;

    private String currency;

    private AccountDto nominalAccount;

    /**
     * getter method
     * @return the request code
     */
    public String getCode() {
        return code;
    }

    /**
     * setter method
     * @param code a string with request code
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
     * @param customer a CustomerDto object with the request customer
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
     * @param creationDate a String with the request creation date
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
     * setter method
     * @param approvalDate a string with the approval date
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
     * @param contractReference a string with the contract reference
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
     * @param amount a string with the request amount
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
     * @return the nominal account
     */
    public AccountDto getNominalAccount() {
        return nominalAccount;
    }

    /**
     * setter method
     * @param nominalAccount a string with the nominal account
     */
    public void setNominalAccount(AccountDto nominalAccount) {
        this.nominalAccount = nominalAccount;
    }

    /**
     * Class constructor
     * @param code export collection code
     * @param customer customer id
     * @param creationDate export collection creation date
     * @param approvalDate export collection approval date
     * @param contractReference export collection contract reference
     * @param amount export collection amount
     * @param currency export collection currency
     * @param nominalAccount export collection nominal account
     */
    public ExportCollectionDto(String code, CustomerDto customer, String creationDate,
                               String approvalDate, String contractReference, String amount,
                               String currency, AccountDto nominalAccount) {
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
     * Empty class constructor
     */
    public ExportCollectionDto() {
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
        ExportCollectionDto that = (ExportCollectionDto) o;
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
        return Objects.hash(code, customer, creationDate, approvalDate,
                contractReference, amount, currency, nominalAccount);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionDto{" +
                "code='" + code + '\'' +
                ", customer=" + customer +
                ", creationDate='" + creationDate + '\'' +
                ", approvalDate='" + approvalDate + '\'' +
                ", contractReference='" + contractReference + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", nominalAccount=" + nominalAccount +
                '}';
    }
}
