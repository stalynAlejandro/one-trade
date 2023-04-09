package com.pagonxt.onetradefinance.integrations.model;

import java.util.Date;
import java.util.Objects;

/**
 * Model class for risk lines
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class RiskLine {

    private String riskLineId;

    private String client;

    private String iban;

    private String status;

    private String limitAmount;

    private String availableAmount;

    private Date expires;

    private String currency;

    /**
     * empty constructor method
     */
    public RiskLine() {
    }

    /**
     * constructor method
     * @param id a string with id of the risk line
     */
    public RiskLine(String id) {
        this.riskLineId = id;
    }

    /**
     * Constructor method
     * Maximum number of arguments is 8, so the full 8-arguments constructor
     * has been separated into a 1-argument constructor and this method.
     * @param availableAmount   : a string with the available amount
     * @param client            : a string with client id
     * @param currency          : a string with the currency
     * @param expires           : a Date object with expiration date
     * @param iban              : a string with the iban
     * @param limitAmount       : a string with the limit of amount
     * @param status            : a string with the status
     */
    public RiskLine with(String client, String iban, String status, String limitAmount,
                         String availableAmount, Date expires, String currency) {
        this.setClient(client);
        this.setIban(iban);
        this.setStatus(status);
        this.setLimitAmount(limitAmount);
        this.setAvailableAmount(availableAmount);
        this.setExpires(expires);
        this.setCurrency(currency);
        return this;
    }

    /**
     * getter method
     * @return a string with the id of the risk line
     */
    public String getRiskLineId() {
        return riskLineId;
    }

    /**
     * setter method
     * @param riskLineId a string with the id of the risk line
     */
    public void setRiskLineId(String riskLineId) {
        this.riskLineId = riskLineId;
    }

    /**
     * getter method
     * @return a string with the client id
     */
    public String getClient() {
        return client;
    }

    /**
     * setter method
     * @param client a string with the client id
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * getter method
     * @return a string with the iban
     */
    public String getIban() {
        return iban;
    }

    /**
     * setter method
     * @param iban a string with the iban
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * getter method
     * @return a string with the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * setter method
     * @param status  a string with the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getter method
     * @return a string with the limit of the amount
     */
    public String getLimitAmount() {
        return limitAmount;
    }

    /**
     * setter method
     * @param limitAmount a string with the limit of the amount
     */
    public void setLimitAmount(String limitAmount) {
        this.limitAmount = limitAmount;
    }

    /**
     * getter method
     * @return a string with the available amount
     */
    public String getAvailableAmount() {
        return availableAmount;
    }

    /**
     * setter method
     * @param availableAmount a string with the available amount
     */
    public void setAvailableAmount(String availableAmount) {
        this.availableAmount = availableAmount;
    }

    /**
     * getter method
     * @return a Date object with the expiration date
     */
    public Date getExpires() {
        return expires;
    }

    /**
     * setter method
     * @param expires a Date object with the expiration date
     */
    public void setExpires(Date expires) {
        this.expires = expires;
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
        RiskLine riskLine = (RiskLine) o;
        return Objects.equals(riskLineId, riskLine.riskLineId) &&
                Objects.equals(client, riskLine.client) &&
                Objects.equals(iban, riskLine.iban) &&
                Objects.equals(status, riskLine.status) &&
                Objects.equals(limitAmount, riskLine.limitAmount) &&
                Objects.equals(availableAmount, riskLine.availableAmount) &&
                Objects.equals(expires, riskLine.expires) &&
                Objects.equals(currency, riskLine.currency);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(riskLineId, client, iban, status, limitAmount, availableAmount, expires, currency);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "RiskLine{" +
                "id='" + riskLineId + '\'' +
                ", client='" + client + '\'' +
                ", iban='" + iban + '\'' +
                ", status='" + status + '\'' +
                ", limitAmount='" + limitAmount + '\'' +
                ", availableAmount='" + availableAmount + '\'' +
                ", expires='" + expires + '\'' +
                ", currency='" + currency + '\'' +
                '}';
    }
}
