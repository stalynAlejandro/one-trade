package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for operation details
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.AccountDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto
 * @since jdk-11.0.13
 */
public class OperationDetailsDto {

    //Class attributes
    private Double collectionAmount;

    private boolean hasAccount;

    private AccountDto clientAccount;

    private AccountDto commissionAccount;

    private CurrencyDto collectionCurrency;

    private String clientReference;

    private String debtorName;

    private String debtorBank;

    private String collectionType;

    private String office;

    private String comments;

    /**
     * getter method
     * @return the collection amount(requested amount)
     */
    public Double getCollectionAmount() {
        return collectionAmount;
    }

    /**
     * setter method
     * @param collectionAmount a double value with the collection amount(requested amount)
     */
    public void setCollectionAmount(Double collectionAmount) {
        this.collectionAmount = collectionAmount;
    }

    /**
     * getter method
     * @return true or false, if the client has account
     */
    public boolean isHasAccount() {
        return hasAccount;
    }

    /**
     * setter method
     * @param hasAccount true or false, if the client has account
     */
    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }

    /**
     * getter method
     * @return the client account
     */
    public AccountDto getClientAccount() {
        return clientAccount;
    }

    /**
     * setter method
     * @param clientAccount an AccountDto object with the client account
     */
    public void setClientAccount(AccountDto clientAccount) {
        this.clientAccount = clientAccount;
    }

    /**
     * getter method
     * @return the commission account of the client
     */
    public AccountDto getCommissionAccount() {
        return commissionAccount;
    }

    /**
     * setter method
     * @param commissionAccount an AccountDto object commission account of the client
     */
    public void setCommissionAccount(AccountDto commissionAccount) {
        this.commissionAccount = commissionAccount;
    }

    /**
     * getter method
     * @return the collection currency
     */
    public CurrencyDto getCollectionCurrency() {
        return collectionCurrency;
    }

    /**
     * setter method
     * @param collectionCurrency a CurrencyDto object with the collection currency
     */
    public void setCollectionCurrency(CurrencyDto collectionCurrency) {
        this.collectionCurrency = collectionCurrency;
    }

    /**
     * getter method
     * @return the client reference
     */
    public String getClientReference() {
        return clientReference;
    }

    /**
     * setter method
     * @param clientReference a string with client reference
     */
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    /**
     * getter method
     * @return the debtor name
     */
    public String getDebtorName() {
        return debtorName;
    }

    /**
     * setter method
     * @param debtorName a string with the debtor name
     */
    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    /**
     * getter method
     * @return the debtor bank
     */
    public String getDebtorBank() {
        return debtorBank;
    }

    /**
     * setter method
     * @param debtorBank a string with the debtor bank
     */
    public void setDebtorBank(String debtorBank) {
        this.debtorBank = debtorBank;
    }

    /**
     * getter method
     * @return the collection type
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * setter method
     * @param collectionType a string with the collection type
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * getter method
     * @return the request office
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the request office
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return the request comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * setter method
     * @param comments a string with the request comments
     */
    public void setComments(String comments) {
        this.comments = comments;
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
        OperationDetailsDto that = (OperationDetailsDto) o;
        return hasAccount == that.hasAccount &&
                Objects.equals(collectionAmount, that.collectionAmount) &&
                Objects.equals(clientAccount, that.clientAccount) &&
                Objects.equals(commissionAccount, that.commissionAccount) &&
                Objects.equals(collectionCurrency, that.collectionCurrency) &&
                Objects.equals(clientReference, that.clientReference) &&
                Objects.equals(debtorName, that.debtorName) &&
                Objects.equals(debtorBank, that.debtorBank) &&
                Objects.equals(collectionType, that.collectionType) &&
                Objects.equals(office, that.office) &&
                Objects.equals(comments, that.comments);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(collectionAmount, hasAccount, clientAccount, commissionAccount, collectionCurrency,
                clientReference, debtorName, debtorBank, collectionType, office, comments);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "OperationDetailsDto{" +
                "collectionAmount=" + collectionAmount +
                ", hasAccount=" + hasAccount +
                ", clientAccount=" + clientAccount +
                ", commissionAccount=" + commissionAccount +
                ", collectionCurrency=" + collectionCurrency +
                ", clientReference='" + clientReference + '\'' +
                ", debtorName='" + debtorName + '\'' +
                ", debtorBank='" + debtorBank + '\'' +
                ", collectionType='" + collectionType + '\'' +
                ", office='" + office + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
