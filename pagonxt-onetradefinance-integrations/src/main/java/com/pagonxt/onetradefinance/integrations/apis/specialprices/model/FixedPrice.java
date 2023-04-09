package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 *  Model class for the fixed prices
 *  Include class attributes, getters and setters
 *  @author -
 *  @version jdk-11.0.13
 *  @since jdk-11.0.13
 */
public abstract class FixedPrice {

    /**
     * Base currency code of the local bank.
     * The value is in the alpha-3 format defined in ISO 4217
     * (https://www.iso.org/iso-4217-currency-codes.html).
     */
    @JsonProperty("currencyCode")
    private String currencyCode;

    /**
     * getter method
     * @return a string with the currency code
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * setter method
     * @param currencyCode a string with the currency code
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
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
        if (!(o instanceof FixedPrice)) {
            return false;
        }
        FixedPrice that = (FixedPrice) o;
        return Objects.equals(getCurrencyCode(), that.getCurrencyCode());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCurrencyCode());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "StandardRatePrice{" +
                "currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
