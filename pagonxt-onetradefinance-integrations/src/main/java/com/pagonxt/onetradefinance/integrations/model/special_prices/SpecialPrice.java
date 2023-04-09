package com.pagonxt.onetradefinance.integrations.model.special_prices;

import java.util.List;
import java.util.Objects;

/**
 * Model class for Special Price
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see SpecialTier
 * @since jdk-11.0.13
 */
public class SpecialPrice {

    /**
     * Level of the special price.
     * The possible values are:
     * - agreement
     * - customer
     * - customer-centre
     */
    private String specialPriceLevel;

    /**
     * Type of special price.
     * The possible values are:
     * - Particular = Preferential price negotiated by the customer
     * - Discount = Bonus obtained by fulfilment of specified conditions
     */
    private String specialPriceType;

    /**
     * Array of special price tiers.
     * There can be multiple tiers for each concept. If there is 1 tier (for the maximum amount and term),
     * the conditions are the same, regardless of the amount or term. If there is more than 1 tier,
     * the price depends on the amount or term
     * (for example, in a loan or deposit).
     */
    private List<SpecialTier> specialTiersList;

    /**
     * getter method
     * @return a string with the special price level
     */
    public String getSpecialPriceLevel() {
        return specialPriceLevel;
    }

    /**
     * setter method
     * @param specialPriceLevel a string with the special price level
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
     * getter method
     * @return a list of special tiers
     */
    public List<SpecialTier> getSpecialTiersList() {
        return specialTiersList;
    }

    /**
     * setter method
     * @param specialTiersList a list of special tiers
     */
    public void setSpecialTiersList(List<SpecialTier> specialTiersList) {
        this.specialTiersList = specialTiersList;
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
        SpecialPrice that = (SpecialPrice) o;
        return Objects.equals(specialPriceLevel, that.specialPriceLevel) &&
                Objects.equals(specialPriceType, that.specialPriceType) &&
                Objects.equals(specialTiersList, that.specialTiersList);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(specialPriceLevel, specialPriceType, specialTiersList);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SpecialPrice{" +
                "specialPriceLevel='" + specialPriceLevel + '\'' +
                ", specialPriceType='" + specialPriceType + '\'' +
                ", specialTiersList=" + specialTiersList +
                '}';
    }
}
