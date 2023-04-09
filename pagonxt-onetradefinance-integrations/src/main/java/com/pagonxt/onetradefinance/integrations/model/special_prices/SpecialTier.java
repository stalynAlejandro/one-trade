package com.pagonxt.onetradefinance.integrations.model.special_prices;

import java.util.Objects;

/**
 * Model class for Special Tier
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see SpecialFixedRatePrice
 * @see SpecialFixedAmountPrice
 * @since jdk-11.0.13
 */
public class SpecialTier {

    /**
     * Special tier amount.
     * The value uses the data format defined in ISO 20022 and has a maximum of 18 digits,
     * of which 5 can be decimals, separated by a point.
     */
    private Double specialAmountTier;

    /**
     * Type of special tier amount, for example, payment amount or number of operations
     */
    private String specialAmountTierType;

    private Double specialTermTier;

    /**
     * Type of special term tier.
     * The possible values are:
     * - days
     * - months
     * - years
     */
    private String specialTermTierType;

    /**
     * Data structure containing details of the special fixed-rate price
     */
    private SpecialFixedRatePrice specialFixedRatePrice;

    /**
     * Data structure containing details of the special fixed-amount price
     */
    private SpecialFixedAmountPrice specialFixedAmountPrice;

    /**
     * getter method
     * @return a double value with the special amount tier
     */
    public Double getSpecialAmountTier() {
        return specialAmountTier;
    }

    /**
     * setter method
     * @param specialAmountTier  a double value with the special amount tier
     */
    public void setSpecialAmountTier(Double specialAmountTier) {
        this.specialAmountTier = specialAmountTier;
    }

    /**
     * getter method
     * @return a string with the type of the special amount tier
     */
    public String getSpecialAmountTierType() {
        return specialAmountTierType;
    }

    /**
     * setter method
     * @param specialAmountTierType a string with the type of the special amount tier
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
     * @return an object with the special fixed rate price
     */
    public SpecialFixedRatePrice getSpecialFixedRatePrice() {
        return specialFixedRatePrice;
    }

    /**
     * setter method
     * @param specialFixedRatePrice an object with the special fixed rate price
     */
    public void setSpecialFixedRatePrice(SpecialFixedRatePrice specialFixedRatePrice) {
        this.specialFixedRatePrice = specialFixedRatePrice;
    }

    /**
     * getter method
     * @return an object with the special fixed amount price
     */
    public SpecialFixedAmountPrice getSpecialFixedAmountPrice() {
        return specialFixedAmountPrice;
    }

    /**
     * setter method
     * @param specialFixedAmountPrice an object with the special fixed amount price
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SpecialTier that = (SpecialTier) o;
        return Objects.equals(specialAmountTier, that.specialAmountTier) &&
                Objects.equals(specialAmountTierType, that.specialAmountTierType) &&
                Objects.equals(specialTermTier, that.specialTermTier) &&
                Objects.equals(specialTermTierType, that.specialTermTierType) &&
                Objects.equals(specialFixedRatePrice, that.specialFixedRatePrice) &&
                Objects.equals(specialFixedAmountPrice, that.specialFixedAmountPrice);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(specialAmountTier, specialAmountTierType, specialTermTier,
                specialTermTierType, specialFixedRatePrice, specialFixedAmountPrice);
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
                ", specialTermTier='" + specialTermTier + '\'' +
                ", specialTermTierType='" + specialTermTierType + '\'' +
                ", specialFixedRatePrice=" + specialFixedRatePrice +
                ", specialFixedAmountPrice=" + specialFixedAmountPrice +
                '}';
    }
}
