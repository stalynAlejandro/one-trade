package com.pagonxt.onetradefinance.integrations.model.special_prices;

import java.util.Objects;

/**
 * Model class for Special Variable Rate Price
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class SpecialVariableRatePrice {

    /**
     * Reference code for the special index
     */
    private String specialIndexCode;

    /**
     * Reference name for the special index
     */
    private String specialIndexName;

    /**
     * Special index rate at the time of enquiry, expressed as a percentage.
     * The value has 9 digits, of which 6 are decimals, separated by a point.
     */
    private Double specialIndexRate;

    /**
     * Special differential rate to be applied to the special index rate, expressed as a percentage.
     * The value has 9 digits, of which 6 are decimals, separated by a point.
     */
    private Double specialDifferentialRate;

    /**
     * Action of the special differential rate on the special index rate.
     * The possible values are:
     * - add
     * - subtract
     * - multiply
     */
    private String specialDifferentialAction;

    /**
     * Minimum rate that is the lower limit for the variable rate at the time of enquiry,
     * expressed as a percentage.
     * The value has 9 digits, of which 6 are decimals, separated by a point.
     */
    private Double specialMinimumRate;

    /**
     * Maximum rate that is the upper limit for the variable rate at the time of enquiry,
     * expressed as a percentage.
     * The value has 9 digits, of which 6 are decimals, separated by a point.
     */
    private Double specialMaximumRate;

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
     * @return a string with the special index code
     */
    public String getSpecialIndexCode() {
        return specialIndexCode;
    }

    /**
     * setter method
     * @param specialIndexCode a string with the special index code
     */
    public void setSpecialIndexCode(String specialIndexCode) {
        this.specialIndexCode = specialIndexCode;
    }

    /**
     * getter method
     * @return a string with the special index name
     */
    public String getSpecialIndexName() {
        return specialIndexName;
    }

    /**
     * setter method
     * @param specialIndexName a string with the special index name
     */
    public void setSpecialIndexName(String specialIndexName) {
        this.specialIndexName = specialIndexName;
    }

    /**
     * getter method
     * @return a double value with the special index rate
     */
    public Double getSpecialIndexRate() {
        return specialIndexRate;
    }

    /**
     * setter method
     * @param specialIndexRate a double value with the special index rate
     */
    public void setSpecialIndexRate(Double specialIndexRate) {
        this.specialIndexRate = specialIndexRate;
    }

    /**
     * getter method
     * @return a double value with special differential rate
     */
    public Double getSpecialDifferentialRate() {
        return specialDifferentialRate;
    }

    /**
     * setter method
     * @param specialDifferentialRate a double value with special differential rate
     */
    public void setSpecialDifferentialRate(Double specialDifferentialRate) {
        this.specialDifferentialRate = specialDifferentialRate;
    }

    /**
     * getter method
     * @return a string with the special differential action
     */
    public String getSpecialDifferentialAction() {
        return specialDifferentialAction;
    }

    /**
     * setter method
     * @param specialDifferentialAction a string with the special differential action
     */
    public void setSpecialDifferentialAction(String specialDifferentialAction) {
        this.specialDifferentialAction = specialDifferentialAction;
    }

    /**
     * getter method
     * @return a double value with the special minimum rate
     */
    public Double getSpecialMinimumRate() {
        return specialMinimumRate;
    }

    /**
     * setter method
     * @param specialMinimumRate a double value with the special minimum rate
     */
    public void setSpecialMinimumRate(Double specialMinimumRate) {
        this.specialMinimumRate = specialMinimumRate;
    }

    /**
     * getter method
     * @return a double value with the special maximum rate
     */
    public Double getSpecialMaximumRate() {
        return specialMaximumRate;
    }

    /**
     * setter method
     * @param specialMaximumRate a double value with the special maximum rate
     */
    public void setSpecialMaximumRate(Double specialMaximumRate) {
        this.specialMaximumRate = specialMaximumRate;
    }

    /**
     * getter method
     * @return a double value with the special minimum amount
     */
    public Double getSpecialMinimumAmount() {
        return specialMinimumAmount;
    }

    /**
     * setter method
     * @param specialMinimumAmount a double value with the special minimum amount
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
        SpecialVariableRatePrice that = (SpecialVariableRatePrice) o;
        return Objects.equals(specialIndexCode, that.specialIndexCode) &&
                Objects.equals(specialIndexName, that.specialIndexName) &&
                Objects.equals(specialIndexRate, that.specialIndexRate) &&
                Objects.equals(specialDifferentialRate, that.specialDifferentialRate) &&
                Objects.equals(specialDifferentialAction, that.specialDifferentialAction) &&
                Objects.equals(specialMinimumRate, that.specialMinimumRate) &&
                Objects.equals(specialMaximumRate, that.specialMaximumRate) &&
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
        return Objects.hash(specialIndexCode, specialIndexName, specialIndexRate, specialDifferentialRate,
                specialDifferentialAction, specialMinimumRate, specialMaximumRate, specialMinimumAmount,
                specialMaximumAmount, currencyCode);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SpecialVariableRatePrice{" +
                "specialIndexCode='" + specialIndexCode + '\'' +
                ", specialIndexName='" + specialIndexName + '\'' +
                ", specialIndexRate=" + specialIndexRate +
                ", specialDifferentialRate=" + specialDifferentialRate +
                ", specialDifferentialAction='" + specialDifferentialAction + '\'' +
                ", specialMinimumRate=" + specialMinimumRate +
                ", specialMaximumRate=" + specialMaximumRate +
                ", specialMinimumAmount=" + specialMinimumAmount +
                ", specialMaximumAmount=" + specialMaximumAmount +
                ", currencyCode='" + currencyCode + '\'' +
                '}';
    }
}
