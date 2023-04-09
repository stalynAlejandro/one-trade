package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Model class for the Fixed rate special price
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialFixedRatePrice extends FixedPrice {

    /**
     * Rate, expressed as a percentage.
     * The value has 9 digits, of which 6 are decimals, separated by a point.
     */
    @JsonProperty("specialRate")
    private Double specialRate;

    /**
     * Minimum amount that is the lower limit for the resultant amount after the rate is applied.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    @JsonProperty("specialMinimumAmount")
    private Double specialMinimumAmount;

    /**
     * Maximum amount that is the upper limit for the resultant amount after the rate is applied.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    @JsonProperty("specialMaximumAmount")
    private Double specialMaximumAmount;

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
     * @param specialMaximumAmount  a double value with the special maximum amount
     */
    public void setSpecialMaximumAmount(Double specialMaximumAmount) {
        this.specialMaximumAmount = specialMaximumAmount;
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
        if (!(o instanceof SpecialFixedRatePrice)) {
            return false;
        }
        SpecialFixedRatePrice that = (SpecialFixedRatePrice) o;
        return Objects.equals(getSpecialRate(), that.getSpecialRate()) &&
                Objects.equals(getSpecialMinimumAmount(), that.getSpecialMinimumAmount()) &&
                Objects.equals(getSpecialMaximumAmount(), that.getSpecialMaximumAmount());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSpecialRate(), getSpecialMinimumAmount(), getSpecialMaximumAmount());
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
                '}';
    }
}
