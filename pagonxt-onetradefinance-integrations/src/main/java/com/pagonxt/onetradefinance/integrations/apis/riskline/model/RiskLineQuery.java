package com.pagonxt.onetradefinance.integrations.apis.riskline.model;

import java.util.Objects;

/**
 * Model class for the queries about risk lines
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class RiskLineQuery {

    private String customerId;

    private String productId;

    private String expirationDate;

    private Double operationAmount;

    private String operationCurrency;

    private String valueDateOperation;

    private String riskLineId;

    private boolean active;

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
     * @return a string with the product id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * setter method
     * @param productId a string with the product id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * getter method
     * @return a string with the expiration date
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * setter method
     * @param expirationDate a string with the expiration date
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * getter method
     * @return a string with the operation amount
     */
    public Double getOperationAmount() {
        return operationAmount;
    }

    /**
     * setter method
     * @param operationAmount a string with the operation amount
     */
    public void setOperationAmount(Double operationAmount) {
        this.operationAmount = operationAmount;
    }

    /**
     * getter method
     * @return a string with the operation currency
     */
    public String getOperationCurrency() {
        return operationCurrency;
    }

    /**
     * setter method
     * @param operationCurrency a string with the operation currency
     */
    public void setOperationCurrency(String operationCurrency) {
        this.operationCurrency = operationCurrency;
    }

    /**
     * getter method
     * @return a string with the value date of the operation
     */
    public String getValueDateOperation() {
        return valueDateOperation;
    }

    /**
     * setter method
     * @param valueDateOperation a string with the value date of the operation
     */
    public void setValueDateOperation(String valueDateOperation) {
        this.valueDateOperation = valueDateOperation;
    }

    /**
     * getter method
     * @return true or false if the risk line is active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * setter method
     * @param active true or false if the risk line is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * getter method
     * @return a string with the risk line id
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
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RiskLineQuery)) {
            return false;
        }
        RiskLineQuery that = (RiskLineQuery) o;
        return isActive() == that.isActive() &&
                Objects.equals(getCustomerId(), that.getCustomerId()) &&
                Objects.equals(getProductId(), that.getProductId()) &&
                Objects.equals(getExpirationDate(), that.getExpirationDate()) &&
                Objects.equals(getOperationAmount(), that.getOperationAmount()) &&
                Objects.equals(getOperationCurrency(), that.getOperationCurrency()) &&
                Objects.equals(getValueDateOperation(), that.getValueDateOperation()) &&
                Objects.equals(getRiskLineId(), that.getRiskLineId());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(),
                getProductId(),
                getExpirationDate(),
                getOperationAmount(),
                getOperationCurrency(),
                getValueDateOperation(),
                getRiskLineId(),
                isActive());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "RiskLineQuery{" +
                "customerId='" + customerId + '\'' +
                ", productId='" + productId + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                ", operationAmount=" + operationAmount +
                ", operationCurrency='" + operationCurrency + '\'' +
                ", valueDateOperation='" + valueDateOperation + '\'' +
                ", riskLineId='" + riskLineId + '\'' +
                ", active=" + active +
                '}';
    }
}
