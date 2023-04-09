package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Model class for accounts
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Account {

    @NotNull
    private String accountId;

    //TODO se permite el null en este campo, ya que se recibe el valor null.
    // Ver si cuando lleguen datos reales hay que modificar.
    private String customerId;

    @NotNull
    private String iban;

    private String costCenter;

    @NotNull
    private String currency;

    /**
     * Empty constructor method
     */
    public Account() {}

    /**
     * constructor method
     * @param accountId     : a string with the account id
     * @param customerId    : a string with the customer id
     * @param iban          : a string with the account iban
     * @param costCenter    : a string with the cost center
     * @param currency      : a string with the currency
     */
    public Account(String accountId, String customerId, String iban, String costCenter, String currency) {
        this.accountId = accountId;
        this.customerId = customerId;
        this.iban = iban;
        this.costCenter = costCenter;
        this.currency = currency;
    }

    /**
     * getter method
     * @return a string with the account id
     */
    public String getAccountId() {
        return accountId;
    }


    /**
     * setter method
     * @param accountId a string with the account id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * getter method
     * @return a string with the customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * setter method
     * @param customerId a string with the customer id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * getter method
     * @return a string with the account iban
     */
    public String getIban() {
        return iban;
    }

    /**
     * setter method
     * @param iban a string with the account iban
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * getter method
     * @return a string with the cost center
     */
    public String getCostCenter() {
        return costCenter;
    }

    /**
     * setter method
     * @param costCenter a string with the cost center
     */
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
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
        Account account = (Account) o;
        return Objects.equals(accountId, account.accountId) &&
                Objects.equals(customerId, account.customerId) &&
                Objects.equals(iban, account.iban) &&
                Objects.equals(costCenter, account.costCenter) &&
                Objects.equals(currency, account.currency);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountId, customerId, iban, costCenter, currency);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Account{" +
                "accountId='" + accountId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", iban='" + iban + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
