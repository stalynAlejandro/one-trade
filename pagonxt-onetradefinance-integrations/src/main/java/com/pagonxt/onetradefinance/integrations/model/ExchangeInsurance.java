package com.pagonxt.onetradefinance.integrations.model;

import java.util.Date;
import java.util.Objects;

/**
 * Model class for exchange insurances
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ExchangeInsurance {

    private String exchangeInsuranceId;
    private String type;
    private Date useDate;
    private Double sellAmount;
    private String sellCurrency;
    private Double buyAmount;
    private String buyCurrency;
    private Double exchangeRate;
    private Double useAmount;

    /**
     * getter method
     * @return a string with the id of exchange insurance
     */
    public String getExchangeInsuranceId() {
        return exchangeInsuranceId;
    }

    /**
     * setter method
     * @param exchangeInsuranceId a string with the id of exchange insurance
     */
    public void setExchangeInsuranceId(String exchangeInsuranceId) {
        this.exchangeInsuranceId = exchangeInsuranceId;
    }

    /**
     * getter method
     * @return a string with the type of exchange insurance
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type a string with the type of exchange insurance
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return a Date object with the use date
     */
    public Date getUseDate() {
        return useDate;
    }

    /**
     * setter method
     * @param useDate a Date object with the use date
     */
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }

    /**
     * getter method
     * @return a double value with the sell amount
     */
    public Double getSellAmount() {
        return sellAmount;
    }

    /**
     * setter method
     * @param sellAmount a double value with the sell amount
     */
    public void setSellAmount(Double sellAmount) {
        this.sellAmount = sellAmount;
    }

    /**
     * getter method
     * @return a string with the sell currency
     */
    public String getSellCurrency() {
        return sellCurrency;
    }

    /**
     * setter method
     * @param sellCurrency a string with the sell currency
     */
    public void setSellCurrency(String sellCurrency) {
        this.sellCurrency = sellCurrency;
    }

    /**
     * getter method
     * @return a double value with the buy amount
     */
    public Double getBuyAmount() {
        return buyAmount;
    }

    /**
     * setter method
     * @param buyAmount a double value with the buy amount
     */
    public void setBuyAmount(Double buyAmount) {
        this.buyAmount = buyAmount;
    }

    /**
     * getter method
     * @return a string with the buy currency
     */
    public String getBuyCurrency() {
        return buyCurrency;
    }

    /**
     * setter method
     * @param buyCurrency a string with the buy currency
     */
    public void setBuyCurrency(String buyCurrency) {
        this.buyCurrency = buyCurrency;
    }

    /**
     * getter method
     * @return a double value with the exchange rate
     */
    public Double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * setter method
     * @param exchangeRate a double value with the exchange rate
     */
    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * getter method
     * @return a double value with the use amount
     */
    public Double getUseAmount() {
        return useAmount;
    }

    /**
     * setter method
     * @param useAmount a double value with the use amount
     */
    public void setUseAmount(Double useAmount) {
        this.useAmount = useAmount;
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
        ExchangeInsurance that = (ExchangeInsurance) o;
        return Objects.equals(exchangeInsuranceId, that.exchangeInsuranceId) &&
                Objects.equals(type, that.type) &&
                Objects.equals(useDate, that.useDate) &&
                Objects.equals(sellAmount, that.sellAmount) &&
                Objects.equals(sellCurrency, that.sellCurrency) &&
                Objects.equals(buyAmount, that.buyAmount) &&
                Objects.equals(buyCurrency, that.buyCurrency) &&
                Objects.equals(exchangeRate, that.exchangeRate) &&
                Objects.equals(useAmount, that.useAmount);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(exchangeInsuranceId, type, useDate, sellAmount,
                sellCurrency, buyAmount, buyCurrency, exchangeRate, useAmount);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExchangeInsurance{" +
                "exchangeInsuranceId='" + exchangeInsuranceId + '\'' +
                ", type='" + type + '\'' +
                ", useDate=" + useDate +
                ", sellAmount=" + sellAmount +
                ", sellCurrency='" + sellCurrency + '\'' +
                ", buyAmount=" + buyAmount +
                ", buyCurrency='" + buyCurrency + '\'' +
                ", exchangeRate=" + exchangeRate +
                ", useAmount=" + useAmount +
                '}';
    }
}
