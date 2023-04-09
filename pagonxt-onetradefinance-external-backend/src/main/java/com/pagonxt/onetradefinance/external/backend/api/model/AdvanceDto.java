package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for advances
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto
 * @since jdk-11.0.13
 */
public class AdvanceDto {

    //class attributes
    private RiskLineDto riskLine;

    private Double advanceAmount;

    private CurrencyDto advanceCurrency;

    private String advanceExpiration;

    /**
     * getter method
     * @return a riskLine dto object
     */
    public RiskLineDto getRiskLine() {
        return riskLine;
    }

    /**
     * setter method
     * @param riskLine a riskLineDto object
     */
    public void setRiskLine(RiskLineDto riskLine) {
        this.riskLine = riskLine;
    }

    /**
     * getter method
     * @return the advance amount
     */
    public Double getAdvanceAmount() {
        return advanceAmount;
    }

    /**
     * setter method
     * @param advanceAmount a double value with advance amount
     */
    public void setAdvanceAmount(Double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    /**
     * getter method
     * @return the advance currency
     */
    public CurrencyDto getAdvanceCurrency() {
        return advanceCurrency;
    }

    /**
     * setter method
     * @param advanceCurrency a currencyDto object with the advance currency
     */
    public void setAdvanceCurrency(CurrencyDto advanceCurrency) {
        this.advanceCurrency = advanceCurrency;
    }

    /**
     * getter method
     * @return the advance expiration date
     */
    public String getAdvanceExpiration() {
        return advanceExpiration;
    }

    /**
     * setter method
     * @param advanceExpiration a string with the advance expiration date
     */
    public void setAdvanceExpiration(String advanceExpiration) {
        this.advanceExpiration = advanceExpiration;
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
        AdvanceDto that = (AdvanceDto) o;
        return Objects.equals(riskLine, that.riskLine) &&
                Objects.equals(advanceAmount, that.advanceAmount) &&
                Objects.equals(advanceCurrency, that.advanceCurrency) &&
                Objects.equals(advanceExpiration, that.advanceExpiration);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(riskLine, advanceAmount, advanceCurrency, advanceExpiration);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "AdvanceDto{" +
                "riskLine=" + riskLine +
                ", advanceAmount=" + advanceAmount +
                ", advanceCurrency=" + advanceCurrency +
                ", advanceExpiration='" + advanceExpiration + '\'' +
                '}';
    }

    /**
     * Method to check if any class attribute is null
     * @return true if there is at least one null class attribute or false if there isn't
     */
    public boolean isNotEmpty() {
        return riskLine != null ||
                advanceAmount != null ||
                advanceCurrency != null ||
                advanceExpiration != null;
    }
}
