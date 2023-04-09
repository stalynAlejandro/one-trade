package com.pagonxt.onetradefinance.integrations.apis.common.model;

/**
 * Model class for currency and amount
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CurrencyAmount {

    private Double amount;

    private String currency;

    /**
     * getter method
     * @return a double value with the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * setter method
     * @param amount a double value with the amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * getter method
     * @return a string with the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a string with the currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
