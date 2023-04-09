package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for accounts
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class AccountDto {

    //class attributes
    private String id;

    private String iban;

    private String currency;

    /**
     * Empty class constructor
     */
    public AccountDto() {
    }

    /**
     * Class Constructor
     * @param id account id
     * @param iban account iban
     * @param currency account currency
     */
    public AccountDto(String id, String iban, String currency) {
        this.id = id;
        this.iban = iban;
        this.currency = currency;
    }

    /**
     * getter method
     * @return the account id
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id a string with account id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return the account IBAN
     */
    public String getIban() {
        return iban;
    }

    /**
     * setter method
     * @param iban a string with account iban
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * getter merthod
     * @return the account currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a string with the account currency
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
        AccountDto that = (AccountDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(iban, that.iban) &&
                Objects.equals(currency, that.currency);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, iban, currency);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "AccountDto{" +
                "id='" + id + '\'' +
                ", iban='" + iban + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
