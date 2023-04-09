package com.pagonxt.onetradefinance.work.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for exchange insurances, in flowable
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ExchangeInsuranceFlowable {

    //class attributes
    @JsonProperty("id_")
    private String id;
    @JsonProperty("type_")
    private String type;
    @JsonProperty("useDate_")
    private String useDate;
    @JsonProperty("sellAvailableAmount_")
    private Double sellAvailableAmount;
    @JsonProperty("buyAvailableAmount_")
    private Double buyAvailableAmount;
    @JsonProperty("exchangeRate_")
    private Double exchangeRate;
    @JsonProperty("amountToUse_")
    private Double amountToUse;

    /**
     * getter method
     * @return : a string with the id
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id : a string with the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return : a string with the type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type : a string with the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return : a string with the use date
     */
    public String getUseDate() {
        return useDate;
    }

    /**
     * setter method
     * @param useDate : a string with the use date
     */
    public void setUseDate(String useDate) {
        this.useDate = useDate;
    }

    /**
     * getter method
     * @return : a Double value with the available amount of sell
     */
    public Double getSellAvailableAmount() {
        return sellAvailableAmount;
    }

    /**
     * setter method
     * @param sellAvailableAmount : a Double value with the available amount of sell
     */
    public void setSellAvailableAmount(Double sellAvailableAmount) {
        this.sellAvailableAmount = sellAvailableAmount;
    }

    /**
     * getter method
     * @return : a Double value with the available amount of buy
     */
    public Double getBuyAvailableAmount() {
        return buyAvailableAmount;
    }

    /**
     * setter method
     * @param buyAvailableAmount : a Double value with the available amount of buy
     */
    public void setBuyAvailableAmount(Double buyAvailableAmount) {
        this.buyAvailableAmount = buyAvailableAmount;
    }

    /**
     * getter method
     * @return : a Double value with the exchange rate
     */
    public Double getExchangeRate() {
        return exchangeRate;
    }

    /**
     * setter method
     * @param exchangeRate : a Double value with the exchange rate
     */
    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    /**
     * getter method
     * @return : a Double value with the amount to use
     */
    public Double getAmountToUse() {
        return amountToUse;
    }

    /**
     * setter method
     * @param amountToUse : a Double value with the amount to use
     */
    public void setAmountToUse(Double amountToUse) {
        this.amountToUse = amountToUse;
    }
}
