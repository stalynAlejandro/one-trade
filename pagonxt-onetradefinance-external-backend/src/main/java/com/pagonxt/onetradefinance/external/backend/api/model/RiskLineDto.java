package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO class for risk lines
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class RiskLineDto {
    private String riskLineId;

    private String client;

    private String iban;

    private String status;

    private String limitAmount;

    private String availableAmount;

    private String expires;

    private String currency;

    /**
     * getter method
     * @return the risk line id
     */
    public String getRiskLineId() {
        return riskLineId;
    }

    /**
     * setter method
     * @param riskLineId a string with the risk line id
     */
    public void setRiskLineId(String riskLineId) {
        this.riskLineId = riskLineId;
    }

    /**
     * getter method
     * @return the client id
     */
    public String getClient() {
        return client;
    }

    /**
     * setter method
     * @param client a string with client id
     */
    public void setClient(String client) {
        this.client = client;
    }

    /**
     * getter method
     * @return the account IBAN
     */
    public String getIban() {
        return iban;
    }

    /**
     * setter method
     * @param iban a string with account IBAN
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    /**
     * getter method
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * setter method
     * @param status a string with the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * getter method
     * @return the limit amount
     */
    public String getLimitAmount() {
        return limitAmount;
    }

    /**
     * setter method
     * @param limitAmount a string with the limit amount
     */
    public void setLimitAmount(String limitAmount) {
        this.limitAmount = limitAmount;
    }

    /**
     * getter method
     * @return the available amount
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
     * @return the expiration date
     */
    public String getExpires() {
        return expires;
    }

    /**
     * setter method
     * @param expires a string with the expiration date
     */
    public void setExpires(String expires) {
        this.expires = expires;
    }

    /**
     * getter method
     * @return the account currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a string with the account currency
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Empty class constructor
     */
    public RiskLineDto() {
    }

    /**
     * Class constructor
     * @param riskLineId risk line id
     */
    public RiskLineDto(String riskLineId) {
        this.riskLineId = riskLineId;
    }

    /**
     * Class constructor
     * Maximum number of arguments is 8, so the full 8-arguments constructor
     * has been separated into a 1-argument constructor and this method.
     * @param client client id
     * @param iban account iban
     * @param status account status
     * @param limitAmount limit amount
     * @param availableAmount available amount
     * @param expires expiration date
     * @param currency amount currency
     * @return a risk line object
     */
    public RiskLineDto with(String client, String iban, String status, String limitAmount,
                            String availableAmount, String expires, String currency) {
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
        RiskLineDto riskLine = (RiskLineDto) o;
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
        return "RiskLineDto{" +
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
