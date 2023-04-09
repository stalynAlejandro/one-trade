package com.pagonxt.onetradefinance.integrations.apis.fxdeal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount;

/**
 * Model class for the details fx deals
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FxDealDetails {

    @JsonProperty("fxDealId")
    private String fxDealId;
    @JsonProperty("fxDealType")
    private String fxDealType;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("sellTotalAmount")
    private CurrencyAmount sellTotalAmount;
    @JsonProperty("sellAvailableAmount")
    private CurrencyAmount sellAvailableAmount;
    @JsonProperty("buyTotalAmount")
    private CurrencyAmount buyTotalAmount;
    @JsonProperty("buyAvailableAmount")
    private CurrencyAmount buyAvailableAmount;
    @JsonProperty("exchangeRate")
    private Double exchangeRate;
    @JsonProperty("signToApplyChange")
    private String signToApplyChange;
    @JsonProperty("fxDealStartDate")
    private String fxDealStartDate;
    @JsonProperty("fxDealExpirationDate")
    private String fxDealExpirationDate;
    @JsonProperty("fxDealUseDate")
    private String fxDealUseDate;

    /**
     * getter method
     * @return a string with the id of fx deal
     */
    public String getFxDealId() {
        return fxDealId;
    }

    /**
     * setter method
     * @param fxDealId a string with the id of fx deal
     */
    public void setFxDealId(String fxDealId) {
        this.fxDealId = fxDealId;
    }

    /**
     * getter method
     * @return a string with the type of fx deal
     */
    public String getFxDealType() {
        return fxDealType;
    }

    /**
     * setter method
     * @param fxDealType a string with the type of fx deal
     */
    public void setFxDealType(String fxDealType) {
        this.fxDealType = fxDealType;
    }

    /**
     * getter method
     * @return a string with the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * setter method
     * @param direction a string with the direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     * @return a CurrencyAmount object with the total amount of the sell (also the currency)
     */
    public CurrencyAmount getSellTotalAmount() {
        return sellTotalAmount;
    }

    /**
     * setter method
     * @param sellTotalAmount a CurrencyAmount object with the total amount of the sell (also the currency)
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     */
    public void setSellTotalAmount(CurrencyAmount sellTotalAmount) {
        this.sellTotalAmount = sellTotalAmount;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     * @return a CurrencyAmount object with the available amount of the sell (also the currency)
     */
    public CurrencyAmount getSellAvailableAmount() {
        return sellAvailableAmount;
    }

    /**
     * setter method
     * @param sellAvailableAmount a CurrencyAmount object with the available amount of the sell (also the currency)
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     */
    public void setSellAvailableAmount(CurrencyAmount sellAvailableAmount) {
        this.sellAvailableAmount = sellAvailableAmount;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     * @return a CurrencyAmount object with the total amount of the buy (also the currency)
     */
    public CurrencyAmount getBuyTotalAmount() {
        return buyTotalAmount;
    }

    /**
     * setter method
     * @param buyTotalAmount a CurrencyAmount object with the total amount of the buy (also the currency)
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     */
    public void setBuyTotalAmount(CurrencyAmount buyTotalAmount) {
        this.buyTotalAmount = buyTotalAmount;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     * @return a CurrencyAmount object with the available amount of the buy (also the currency)
     */
    public CurrencyAmount getBuyAvailableAmount() {
        return buyAvailableAmount;
    }

    /**
     * setter method
     * @param buyAvailableAmount a CurrencyAmount object with the available amount of the buy (also the currency)
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
     */
    public void setBuyAvailableAmount(CurrencyAmount buyAvailableAmount) {
        this.buyAvailableAmount = buyAvailableAmount;
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
     * @return a string with the sign to apply changes
     */
    public String getSignToApplyChange() {
        return signToApplyChange;
    }

    /**
     * setter method
     * @param signToApplyChange a string with the sign to apply changes
     */
    public void setSignToApplyChange(String signToApplyChange) {
        this.signToApplyChange = signToApplyChange;
    }

    /**
     * getter method
     * @return a string with the start date of the deal
     */
    public String getFxDealStartDate() {
        return fxDealStartDate;
    }

    /**
     * setter method
     * @param fxDealStartDate a string with the start date of the deal
     */
    public void setFxDealStartDate(String fxDealStartDate) {
        this.fxDealStartDate = fxDealStartDate;
    }

    /**
     * getter method
     * @return a string with the expiration date of the deal
     */
    public String getFxDealExpirationDate() {
        return fxDealExpirationDate;
    }

    /**
     * setter method
     * @param fxDealExpirationDate a string with the expiration date of the deal
     */
    public void setFxDealExpirationDate(String fxDealExpirationDate) {
        this.fxDealExpirationDate = fxDealExpirationDate;
    }

    /**
     * getter method
     * @return a string with the date of use of the fx deal
     */
    public String getFxDealUseDate() {
        return fxDealUseDate;
    }

    /**
     * setter method
     * @param fxDealUseDate a string with the date of use of the fx deal
     */
    public void setFxDealUseDate(String fxDealUseDate) {
        this.fxDealUseDate = fxDealUseDate;
    }
}
