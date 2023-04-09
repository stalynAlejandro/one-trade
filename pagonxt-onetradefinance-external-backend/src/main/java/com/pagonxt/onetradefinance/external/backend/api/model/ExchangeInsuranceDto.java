package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO class for exchange insurance
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ExchangeInsuranceDto {
    //class attributes
    private String exchangeInsuranceId;
    private String type;
    private String useDate;
    private String sellAmount;
    private String sellCurrency;
    private String buyAmount;
    private String buyCurrency;
    private String exchangeRate;
    private String useAmount;

    /**
     * getter method
     * @return the exchange insurance id
     */
    public String getExchangeInsuranceId() {
        return exchangeInsuranceId;
    }

    /**
     * setter method
     * @param exchangeInsuranceId a string with the exchange insurance id
     */
    public void setExchangeInsuranceId(String exchangeInsuranceId) {
        this.exchangeInsuranceId = exchangeInsuranceId;
    }

    /**
     * getter method
     * @return the exchange insurance type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type a string with the exchange insurance type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return the use date of the exchange insurance
     */
    public String getUseDate() {
        return useDate;
    }

    /**
     * setter method
     * @param useDate a string with the use date of the exchange insurance
     */
    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    /**
     * getter method
     * @return the sell amount of the exchange insurance
     */
    public String getSellAmount() {
        return sellAmount;
    }

    /**
     * setter method
     * @param sellAmount a string with the sell amount of the exchange insurance
     */
    public void setSellAmount(String sellAmount) {
        this.sellAmount = sellAmount;
    }

    /**
     * getter method
     * @return the sell currency of the exchange insurance
     */
    public String getSellCurrency() {
        return sellCurrency;
    }

    /**
     * setter method
     * @param sellCurrency a string with the sell amount of the exchange insurance
     */
    public void setSellCurrency(String sellCurrency) {
        this.sellCurrency = sellCurrency;
    }

    /**
     * getter method
     * @return the buy amount of the exchange insurance
     */
    public String getBuyAmount() {
        return buyAmount;
    }

    /**
     * setter method
     * @param buyAmount a string with the buy amount of the exchange insurance
     */
    public void setBuyAmount(String buyAmount) {
        this.buyAmount = buyAmount;
    }

    /**
     * getter method
     * @return the buy currency of the exchange insurance
     */
    public String getBuyCurrency() {
        return buyCurrency;
    }

    /**
     * setter method
     * @param buyCurrency a string with the buy amount of the exchange insurance
     */
    public void setBuyCurrency(String buyCurrency) {
        this.buyCurrency = buyCurrency;
    }

    /**
     * getter method
     * @return the exchange rate of the exchange insurance
     */
    public String getExchangeRate() {
        return exchangeRate;
    }

    /**
     * setter method
     * @param exchangeRate a string with the exchange rate of the exchange insurance
     */
    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * getter method
     * @return the use amount of the exchange insurance
     */
    public String getUseAmount() {
        return useAmount;
    }

    /**
     * setter method
     * @param useAmount a string with the use amount of the exchange insurance
     */
    public void setUseAmount(String useAmount) {
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
        ExchangeInsuranceDto that = (ExchangeInsuranceDto) o;
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
        return Objects.hash(exchangeInsuranceId, type, useDate, sellAmount, sellCurrency, buyAmount,
                buyCurrency, exchangeRate, useAmount);
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
