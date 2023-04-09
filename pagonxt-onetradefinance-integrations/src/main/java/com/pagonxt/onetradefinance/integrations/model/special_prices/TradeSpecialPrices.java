package com.pagonxt.onetradefinance.integrations.model.special_prices;

import java.util.Objects;

/**
 * Model class for Trade Special Prices
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class TradeSpecialPrices {

    private String conceptName;

    private String periodicity;

    private String percentage;

    private String amount;

    private String currencyCode;

    /**
     * getter method
     * @return a string with the concept name
     */
    public String getConceptName() {
        return conceptName;
    }

    /**
     * setter method
     * @param conceptName a string with the concept name
     */
    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    /**
     * getter method
     * @return a string with the periodicity
     */
    public String getPeriodicity() {
        return periodicity;
    }

    /**
     * setter method
     * @param periodicity a string with the periodicity
     */
    public void setPeriodicity(String periodicity) {
        this.periodicity = periodicity;
    }

    /**
     * getter method
     * @return a string with the percentage
     */
    public String getPercentage() {
        return percentage;
    }

    /**
     * setter method
     * @param percentage a string with the percentage
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * getter method
     * @return a string with the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * setter method
     * @param amount a string with the amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
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
        if (!(o instanceof TradeSpecialPrices)) {
            return false;
        }
        TradeSpecialPrices that = (TradeSpecialPrices) o;
        return Objects.equals(getConceptName(), that.getConceptName()) &&
                Objects.equals(getPeriodicity(), that.getPeriodicity()) &&
                Objects.equals(getPercentage(), that.getPercentage()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getCurrencyCode(), that.getCurrencyCode());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getConceptName(), getPeriodicity(), getPercentage(), getAmount(), getCurrencyCode());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "TradeSpecialPrices{" +
                "conceptName='" + conceptName + '\'' +
                ", periodicity='" + periodicity + '\'' +
                ", percentage='" + percentage + '\'' +
                ", amount=" + amount + '\'' +
                ", currencyCode=" + currencyCode +
                '}';
    }
}
