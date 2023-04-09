package com.pagonxt.onetradefinance.integrations.model.special_prices;

import java.util.Objects;

/**
 * Model class for Special Fixed Amount Price
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class SpecialFixedAmountPrice {

    /**
     * Data structure containing details of the special fixed-amount price
     */
    private Double specialFixedAmount;

    /**
     * Base currency code of the local bank.
     * The value is in the alpha-3 format defined in ISO 4217
     * (https://www.iso.org/iso-4217-currency-codes.html).
     */
    private String currencyCode;

    /**
     * getter method
     * @return a double value with the special fixed amount
     */
    public Double getSpecialFixedAmount() {
        return specialFixedAmount;
    }

    /**
     * setter method
     * @param specialFixedAmount a double value with the special fixed amount
     */
    public void setSpecialFixedAmount(Double specialFixedAmount) {
        this.specialFixedAmount = specialFixedAmount;
    }

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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpecialFixedAmountPrice that = (SpecialFixedAmountPrice) o;
        return Objects.equals(specialFixedAmount, that.specialFixedAmount) &&
                Objects.equals(currencyCode, that.currencyCode);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(specialFixedAmount, currencyCode);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SpecialFixedAmountPrice{" +
                "specialFixedAmount=" + specialFixedAmount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
