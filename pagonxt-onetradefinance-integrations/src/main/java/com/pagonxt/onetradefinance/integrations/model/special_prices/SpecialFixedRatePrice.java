package com.pagonxt.onetradefinance.integrations.model.special_prices;

import java.util.Objects;

/**
 * Model class for Special Fixed Rate Price
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class SpecialFixedRatePrice {

    /**
     * Special rate, expressed as a percentage.
     * The value has 9 digits, of which 6 are decimals, separated by a point.
     */
    private Double specialRate;

    /**
     * Minimum amount that is the lower limit for the resultant amount after the rate is applied.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    private Double specialMinimumAmount;

    /**
     * Maximum amount that is the upper limit for the resultant amount after the rate is applied.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */

    private Double specialMaximumAmount;

    /**
     * Base currency code of the local bank.
     * The value is in the alpha-3 format defined in ISO 4217
     * (https://www.iso.org/iso-4217-currency-codes.html).
     */
    private String currencyCode;

    /**
     * getter method
     * @return a double value with the special rate
     */
    public Double getSpecialRate() {
        return specialRate;
    }

    /**
     * setter method
     * @param specialRate a double value with the special rate
     */
    public void setSpecialRate(Double specialRate) {
        this.specialRate = specialRate;
    }

    /**
     * getter method
     * @return a double value with special minimum amount
     */
    public Double getSpecialMinimumAmount() {
        return specialMinimumAmount;
    }

    /**
     * setter method
     * @param specialMinimumAmount a double value with special minimum amount
     */
    public void setSpecialMinimumAmount(Double specialMinimumAmount) {
        this.specialMinimumAmount = specialMinimumAmount;
    }

    /**
     * getter method
     * @return a double value with the special maximum amount
     */
    public Double getSpecialMaximumAmount() {
        return specialMaximumAmount;
    }

    /**
     * setter method
     * @param specialMaximumAmount a double value with the special maximum amount
     */
    public void setSpecialMaximumAmount(Double specialMaximumAmount) {
        this.specialMaximumAmount = specialMaximumAmount;
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
        SpecialFixedRatePrice that = (SpecialFixedRatePrice) o;
        return Objects.equals(specialRate, that.specialRate) &&
                Objects.equals(specialMinimumAmount, that.specialMinimumAmount) &&
                Objects.equals(specialMaximumAmount, that.specialMaximumAmount) &&
                Objects.equals(currencyCode, that.currencyCode);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(specialRate, specialMinimumAmount, specialMaximumAmount, currencyCode);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SpecialFixedRatePrice{" +
                "specialRate=" + specialRate +
                ", specialMinimumAmount=" + specialMinimumAmount +
                ", specialMaximumAmount=" + specialMaximumAmount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
