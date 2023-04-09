package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 *  Model class for the special tiers
 *  Include class attributes, getters and setters
 *  @author -
 *  @version jdk-11.0.13
 *  @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardTier {

    /**
     * Standard tier amount.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    @JsonProperty("standardAmountTier")
    private Double standardAmountTier;

    /**
     * Type of standard tier amount, for example, payment amount
     */
    @JsonProperty("standardAmountTierType")
    private String standardAmountTierType;

    /**
     * Standard term tier.
     * The value has a maximum of 5 digits.
     */
    @JsonProperty("standardTermTier")
    private Double standardTermTier;

    /**
     * Type of standard term tier.
     * The possible values are:
     * - days
     * - months
     * - years
     */
    @JsonProperty("standardTermTierType")
    private String standardTermTierType;

    /**
     * Data structure containing details of the standard fixed-rate price
     */
    @JsonProperty("standardFixedRatePrice")
    private StandardFixedRatePrice standardFixedRatePrice;

    /**
     * Data structure containing details of the standard fixed-amount price
     */
    @JsonProperty("standardFixedAmountPrice")
    private StandardFixedAmountPrice standardFixedAmountPrice;

    /**
     * getter method
     * @return a double value with the standard amount tier
     */
    public Double getStandardAmountTier() {
        return standardAmountTier;
    }

    /**
     * setter method
     * @param standardAmountTier a double value with the standard amount tier
     */
    public void setStandardAmountTier(Double standardAmountTier) {
        this.standardAmountTier = standardAmountTier;
    }

    /**
     * getter method
     * @return a string with the type of the standard amount tier
     */
    public String getStandardAmountTierType() {
        return standardAmountTierType;
    }

    /**
     * setter method
     * @param standardAmountTierType a string with the type of the standard amount tier
     */
    public void setStandardAmountTierType(String standardAmountTierType) {
        this.standardAmountTierType = standardAmountTierType;
    }

    /**
     * getter method
     * @return a double value with the standard term tier
     */
    public Double getStandardTermTier() {
        return standardTermTier;
    }

    /**
     * setter method
     * @param standardTermTier a double value with the standard term tier
     */
    public void setStandardTermTier(Double standardTermTier) {
        this.standardTermTier = standardTermTier;
    }

    /**
     * getter method
     * @return a string with the type of the standard term tier
     */
    public String getStandardTermTierType() {
        return standardTermTierType;
    }

    /**
     * setter method
     * @param standardTermTierType a string with the type of the standard term tier
     */
    public void setStandardTermTierType(String standardTermTierType) {
        this.standardTermTierType = standardTermTierType;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.StandardFixedRatePrice
     * @return a StandardFixedRatePrice object
     */
    public StandardFixedRatePrice getStandardFixedRatePrice() {
        return standardFixedRatePrice;
    }

    /**
     * setter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.StandardFixedRatePrice
     * @param standardFixedRatePrice a StandardFixedRatePrice object
     */
    public void setStandardFixedRatePrice(StandardFixedRatePrice standardFixedRatePrice) {
        this.standardFixedRatePrice = standardFixedRatePrice;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.StandardFixedAmountPrice
     * @return a StandardFixedAmountPrice object
     */
    public StandardFixedAmountPrice getStandardFixedAmountPrice() {
        return standardFixedAmountPrice;
    }

    /**
     * setter method
     * @param standardFixedAmountPrice a StandardFixedAmountPrice object
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.StandardFixedAmountPrice
     */
    public void setStandardFixedAmountPrice(StandardFixedAmountPrice standardFixedAmountPrice) {
        this.standardFixedAmountPrice = standardFixedAmountPrice;
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
        StandardTier that = (StandardTier) o;
        return Objects.equals(standardAmountTier, that.standardAmountTier) &&
                Objects.equals(standardAmountTierType, that.standardAmountTierType) &&
                Objects.equals(standardTermTier, that.standardTermTier) &&
                Objects.equals(standardTermTierType, that.standardTermTierType) &&
                Objects.equals(standardFixedRatePrice, that.standardFixedRatePrice) &&
                Objects.equals(standardFixedAmountPrice, that.standardFixedAmountPrice);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(standardAmountTier, standardAmountTierType, standardTermTier,
                standardTermTierType, standardFixedRatePrice, standardFixedAmountPrice);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "StandardTiers{" +
                "standardAmountTier=" + standardAmountTier +
                ", standardAmountTypeTier='" + standardAmountTierType + '\'' +
                ", standardTermTier=" + standardTermTier +
                ", standardTermTierType='" + standardTermTierType + '\'' +
                ", standardFixedRatePrice=" + standardFixedRatePrice +
                ", standardFixedAmountPrice=" + standardFixedAmountPrice +
                '}';
    }
}
