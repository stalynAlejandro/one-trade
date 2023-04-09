package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 *  Model class for special prices
 *  Include class attributes, getters and setters
 *  @author -
 *  @version jdk-11.0.13
 *  @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpecialPrice {

    @JsonProperty("specialPriceLevel")
    private String specialPriceLevel;

    @JsonProperty("specialPriceType")
    private String specialPriceType;

    /**
     * Array of standard price tiers.
     * There can be multiple tiers for each concept.
     * If there is 1 tier (for the maximum amount and term), the conditions are the same,
     * regardless of the amount or term. If there is more than 1 tier, the price depends on the amount or term
     * (for example, in a loan or deposit).
     */
    @JsonProperty("specialTiersList")
    private List<SpecialTier> specialTier;

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SpecialTier
     * @return a list of special tiers
     */
    public List<SpecialTier> getSpecialTier() {
        return specialTier;
    }

    /**
     * setter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SpecialTier
     * @param specialTier a list of special tiers
     */
    public void setSpecialTier(List<SpecialTier> specialTier) {
        this.specialTier = specialTier;
    }

    /**
     * getter method
     * @return a string with the special prices level
     */
    public String getSpecialPriceLevel() {
        return specialPriceLevel;
    }

    /**
     * setter method
     * @param specialPriceLevel a string with the special prices level
     */
    public void setSpecialPriceLevel(String specialPriceLevel) {
        this.specialPriceLevel = specialPriceLevel;
    }

    /**
     * getter method
     * @return a string with the special price type
     */
    public String getSpecialPriceType() {
        return specialPriceType;
    }

    /**
     * setter method
     * @param specialPriceType a string with the special price type
     */
    public void setSpecialPriceType(String specialPriceType) {
        this.specialPriceType = specialPriceType;
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
        if (!(o instanceof SpecialPrice)) {
            return false;
        }
        SpecialPrice that = (SpecialPrice) o;
        return Objects.equals(getSpecialTier(), that.getSpecialTier()) &&
                Objects.equals(getSpecialPriceLevel(), that.getSpecialPriceLevel()) &&
                Objects.equals(getSpecialPriceType(), that.getSpecialPriceType());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getSpecialTier(), getSpecialPriceLevel(), getSpecialPriceType());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SpecialPrice{" +
                "specialTier=" + specialTier +
                ", specialPriceLevel='" + specialPriceLevel + '\'' +
                ", specialPriceType='" + specialPriceType + '\'' +
                '}';
    }
}
