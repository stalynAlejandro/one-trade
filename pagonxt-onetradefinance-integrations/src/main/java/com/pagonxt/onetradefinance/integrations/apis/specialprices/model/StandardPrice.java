package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

/**
 *  Model class for standard prices
 *  Include class attributes, getters and setters
 *  @author -
 *  @version jdk-11.0.13
 *  @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardPrice {

    /**
     * Array of standard price tiers.
     * There can be multiple tiers for each concept.
     * If there is 1 tier (for the maximum amount and term), the conditions are the same,
     * regardless of the amount or term. If there is more than 1 tier, the price depends on the amount or term
     * (for example, in a loan or deposit).
     */
    @JsonProperty("standardTiersList")
    private List<StandardTier> standardTier;

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.StandardTier
     * @return a list of standard tiers
     */
    public List<StandardTier> getStandardTier() {
        return standardTier;
    }

    /**
     * setter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.StandardTier
     * @param standardTier a list of standard tiers
     */
    public void setStandardTier(List<StandardTier> standardTier) {
        this.standardTier = standardTier;
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
        StandardPrice that = (StandardPrice) o;
        return Objects.equals(standardTier, that.standardTier);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(standardTier);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "StandardPrice{" +
                "standardTiersList=" + standardTier +
                '}';
    }
}
