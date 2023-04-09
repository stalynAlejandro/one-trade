package com.pagonxt.onetradefinance.integrations.apis.fxdeal.model;

import java.util.Date;

/**
 * Model class for the queries about fx deals
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class FxDealQuery {

    private String customerId;

    private String bankId;

    private String branchId;

    private Date fromDate;

    private Date toDate;

    private String dateType;

    private String sellCurrency;

    private String buyCurrency;

    private String direction;

    private String balanceFxDealType;

    private Double balanceFxDealAmount;

    private String balanceFxDealCurrency;

    /**
     * getter method
     * @return a string with the customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * setter method
     * @param customerId a string with the customer id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * getter method
     * @return a string with the bank id
     */
    public String getBankId() {
        return bankId;
    }

    /**
     * setter method
     * @param bankId a string with the bank id
     */
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    /**
     * getter method
     * @return a string with the branch id
     */
    public String getBranchId() {
        return branchId;
    }

    /**
     * setter method
     * @param branchId a string with the branch id
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    /**
     * getter method
     * @return a string with the start date
     */
    public Date getFromDate() {
        return fromDate;
    }

    /**
     * setter method
     * @param fromDate a string with the start date
     */
    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    /**
     * getter method
     * @return a string with the end date
     */
    public Date getToDate() {
        return toDate;
    }

    /**
     * setter method
     * @param toDate a string with the end date
     */
    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    /**
     * getter method
     * @return a string with the date type
     */
    public String getDateType() {
        return dateType;
    }

    /**
     * setter method
     * @param dateType a string with the date type
     */
    public void setDateType(String dateType) {
        this.dateType = dateType;
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
     * @return a string with the balance type of the fx deal
     */
    public String getBalanceFxDealType() {
        return balanceFxDealType;
    }

    /**
     * setter method
     * @param balanceFxDealType a string with the balance type of the fx deal
     */
    public void setBalanceFxDealType(String balanceFxDealType) {
        this.balanceFxDealType = balanceFxDealType;
    }

    /**
     * getter method
     * @return a double value with the balance amount of the fx deal
     */
    public Double getBalanceFxDealAmount() {
        return balanceFxDealAmount;
    }

    /**
     * setter method
     * @param balanceFxDealAmount a double value with the balance amount of the fx deal
     */
    public void setBalanceFxDealAmount(Double balanceFxDealAmount) {
        this.balanceFxDealAmount = balanceFxDealAmount;
    }

    /**
     * getter method
     * @return a string with the balance currency of the fx deal
     */
    public String getBalanceFxDealCurrency() {
        return balanceFxDealCurrency;
    }

    /**
     * setter method
     * @param balanceFxDealCurrency a string with the balance currency of the fx deal
     */
    public void setBalanceFxDealCurrency(String balanceFxDealCurrency) {
        this.balanceFxDealCurrency = balanceFxDealCurrency;
    }
}
