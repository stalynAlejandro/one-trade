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
public class SpecialTier {

    /**
     * Special tier amount.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    @JsonProperty("specialAmountTier")
    private Double specialAmountTier;

    /**
     * Type of special tier amount, for example, payment amount
     */
    @JsonProperty("specialAmountTierType")
    private String specialAmountTierType;

    /**
     * special term tier.
     * The value has a maximum of 5 digits.
     */
    @JsonProperty("specialTermTier")
    private Double specialTermTier;

    /**
     * Type of special term tier.
     * The possible values are:
     * - days
     * - months
     * - years
     */
    @JsonProperty("specialTermTierType")
    private String specialTermTierType;

    /**
     * Data structure containing details of the special fixed-rate price
     */
    @JsonProperty("specialFixedRatePrice")
    private SpecialFixedRatePrice specialFixedRatePrice;

    /**
     * Data structure containing details of the special fixed-amount price
     */
    @JsonProperty("specialFixedAmountPrice")
    private SpecialFixedAmountPrice specialFixedAmountPrice;

    /**
     * getter method
     * @return a double value with special amount tier
     */
    public Double getSpecialAmountTier() {
        return specialAmountTier;
    }

    /**
     * setter method
     * @param specialAmountTier a double value with special amount tier
     */
    public void setSpecialAmountTier(Double specialAmountTier) {
        this.specialAmountTier = specialAmountTier;
    }

    /**
     * getter method
     * @return a string value with the type of the special amount tier
     */
    public String getSpecialAmountTierType() {
        return specialAmountTierType;
    }

    /**
     * setter method
     * @param specialAmountTierType a string value with the type of the special amount tier
     */
    public void setSpecialAmountTierType(String specialAmountTierType) {
        this.specialAmountTierType = specialAmountTierType;
    }

    /**
     * getter method
     * @return a double value with the special term tier
     */
    public Double getSpecialTermTier() {
        return specialTermTier;
    }

    /**
     * setter method
     * @param specialTermTier a double value with the special term tier
     */
    public void setSpecialTermTier(Double specialTermTier) {
        this.specialTermTier = specialTermTier;
    }

    /**
     * getter method
     * @return a string with the type of the special term tier
     */
    public String getSpecialTermTierType() {
        return specialTermTierType;
    }

    /**
     * setter method
     * @param specialTermTierType a string with the type of the special term tier
     */
    public void setSpecialTermTierType(String specialTermTierType) {
        this.specialTermTierType = specialTermTierType;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SpecialFixedRatePrice
     * @return a SpecialFixedRatePrice object
     */
    public SpecialFixedRatePrice getSpecialFixedRatePrice() {
        return specialFixedRatePrice;
    }

    /**
     * setter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SpecialFixedRatePrice
     * @param specialFixedRatePrice a SpecialFixedRatePrice object
     */
    public void setSpecialFixedRatePrice(SpecialFixedRatePrice specialFixedRatePrice) {
        this.specialFixedRatePrice = specialFixedRatePrice;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SpecialFixedAmountPrice
     * @return a SpecialFixedAmountPrice object
     */
    public SpecialFixedAmountPrice getSpecialFixedAmountPrice() {
        return specialFixedAmountPrice;
    }

    /**
     * setter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SpecialFixedAmountPrice
     * @param specialFixedAmountPrice a SpecialFixedAmountPrice object
     */
    public void setSpecialFixedAmountPrice(SpecialFixedAmountPrice specialFixedAmountPrice) {
        this.specialFixedAmountPrice = specialFixedAmountPrice;
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
        if (!(o instanceof SpecialTier)) {
            return false;
        }
        SpecialTier that = (SpecialTier) o;
        return Objects.equals(getSpecialAmountTier(), that.getSpecialAmountTier()) &&
                Objects.equals(getSpecialAmountTierType(), that.getSpecialAmountTierType()) &&
                Objects.equals(getSpecialTermTier(), that.getSpecialTermTier()) &&
                Objects.equals(getSpecialTermTierType(), that.getSpecialTermTierType()) &&
                Objects.equals(getSpecialFixedRatePrice(), that.getSpecialFixedRatePrice()) &&
                Objects.equals(getSpecialFixedAmountPrice(), that.getSpecialFixedAmountPrice());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSpecialAmountTier(),
                            getSpecialAmountTierType(),
                            getSpecialTermTier(),
                            getSpecialTermTierType(),
                            getSpecialFixedRatePrice(),
                            getSpecialFixedAmountPrice());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SpecialTier{" +
                "specialAmountTier=" + specialAmountTier +
                ", specialAmountTierType='" + specialAmountTierType + '\'' +
                ", specialTermTier=" + specialTermTier +
                ", specialTermTierType='" + specialTermTierType + '\'' +
                ", specialFixedRatePrice=" + specialFixedRatePrice +
                ", specialFixedAmountPrice=" + specialFixedAmountPrice +
                '}';
    }
}
