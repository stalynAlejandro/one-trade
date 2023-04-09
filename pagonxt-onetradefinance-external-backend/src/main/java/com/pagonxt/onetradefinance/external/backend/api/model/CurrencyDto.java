package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for currencies (foreign exchange)
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CurrencyDto {

    //class attributes
    private String id;

    private String currency;

    /**
     * getter method
     * @return the currency id
     */
    public String getId() {
        return id;
    }

    /**s
     * setter method
     * @param id a string with the currency id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return the currency name
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a string with the currency name
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
        CurrencyDto that = (CurrencyDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(currency, that.currency);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, currency);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "CurrencyDto{" +
                "id='" + id + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
