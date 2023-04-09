package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Model class for the Fixed rate standard price
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardFixedRatePrice extends FixedPrice {

    /**
     * Rate, expressed as a percentage.
     * The value has 9 digits, of which 6 are decimals, separated by a point.
     */
    @JsonProperty("standardRate")
    private Double standardRate;

    /**
     * Minimum amount that is the lower limit for the resultant amount after the rate is applied.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    @JsonProperty("standardMinimumAmount")
    private Double standardMinimumAmount;

    /**
     * Maximum amount that is the upper limit for the resultant amount after the rate is applied.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    @JsonProperty("standardMaximumAmount")
    private Double standardMaximumAmount;

    /**
     * getter method
     * @return a double value with the standard rate
     */
    public Double getStandardRate() {
        return standardRate;
    }

    /**
     * setter method
     * @param standardRate a double value with the standard rate
     */
    public void setStandardRate(Double standardRate) {
        this.standardRate = standardRate;
    }

    /**
     * getter method
     * @return a double value with the standard minimum amount
     */
    public Double getStandardMinimumAmount() {
        return standardMinimumAmount;
    }

    /**
     * setter method
     * @param standardMinimumAmount a double value with the standard minimum amount
     */
    public void setStandardMinimumAmount(Double standardMinimumAmount) {
        this.standardMinimumAmount = standardMinimumAmount;
    }

    /**
     * getter method
     * @return a double value with the standard maximum amount
     */
    public Double getStandardMaximumAmount() {
        return standardMaximumAmount;
    }

    /**
     * setter method
     * @param standardMaximumAmount a double value with the standard maximum amount
     */
    public void setStandardMaximumAmount(Double standardMaximumAmount) {
        this.standardMaximumAmount = standardMaximumAmount;
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
        StandardFixedRatePrice that = (StandardFixedRatePrice) o;
        return Objects.equals(standardRate, that.standardRate) &&
                Objects.equals(standardMinimumAmount, that.standardMinimumAmount) &&
                Objects.equals(standardMaximumAmount, that.standardMaximumAmount);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(standardRate, standardMinimumAmount, standardMaximumAmount);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "StandardFixedRatePrice{" +
                "standardRate=" + standardRate +
                ", standardMinimumAmount=" + standardMinimumAmount +
                ", standardMaximumAmount=" + standardMaximumAmount +
                '}';
    }
}
