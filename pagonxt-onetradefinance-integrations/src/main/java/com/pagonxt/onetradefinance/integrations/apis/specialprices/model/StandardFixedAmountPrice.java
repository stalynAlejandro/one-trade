package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Model class for the Fixed amount standard price
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardFixedAmountPrice extends FixedPrice {

    /**
     * Fixed amount to be credited or charged.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    @JsonProperty("standardFixedAmount")
    private Double standardFixedAmount;

    /**
     * getter method
     * @return a double value with the fixed amount standard price
     */
    public Double getStandardFixedAmount() {
        return standardFixedAmount;
    }

    /**
     * setter method
     * @param standardFixedAmount a double value with the fixed amount standard price
     */
    public void setStandardFixedAmount(Double standardFixedAmount) {
        this.standardFixedAmount = standardFixedAmount;
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
        StandardFixedAmountPrice that = (StandardFixedAmountPrice) o;
        return Objects.equals(standardFixedAmount, that.standardFixedAmount);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(standardFixedAmount);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "StandardFixedAmountPrice{" +
                "standardFixedAmount=" + standardFixedAmount +
                '}';
    }
}
