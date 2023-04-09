package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Model class for advance requests of export collections
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see Customer
 * @see ExportCollection
 * @see RiskLine
 * @see PagoNxtRequest
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceRequest extends PagoNxtRequest {

    @NotNull
    private Customer customer;

    @NotNull
    private ExportCollection exportCollection;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("9999999999.99")
    private Double amount;

    @NotNull
    private String currency;
    private List<ExchangeInsurance> exchangeInsurances;
    private Date exchangeInsuranceUseDate;
    private Double exchangeInsuranceAmountToUse;
    private String exchangeInsuranceSellCurrency;
    private String exchangeInsuranceBuyCurrency;

    @NotNull
    private RiskLine riskLine;
    private Date expiration;
    private Integer savedStep;

    private String contractReference;

    /**
     * getter method
     * @return a Customer object with the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * setter method
     * @param customer a Customer object with the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * getter method
     * @return an ExportCollection object with the data of request
     */
    public ExportCollection getExportCollection() {
        return exportCollection;
    }

    /**
     * setter method
     * @param exportCollection an ExportCollection object with the data of request
     */
    public void setExportCollection(ExportCollection exportCollection) {
        this.exportCollection = exportCollection;
    }

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

    /**
     * getter method
     * @return a list with the exchange insurances
     */
    public List<ExchangeInsurance> getExchangeInsurances() {
        return exchangeInsurances;
    }

    /**
     * getter method
     * @return a Date object with the use date of the exchange insurance
     */
    public Date getExchangeInsuranceUseDate() {
        return exchangeInsuranceUseDate;
    }

    /**
     * setter method
     * @param exchangeInsuranceUseDate a Date object with the use date of the exchange insurance
     */
    public void setExchangeInsuranceUseDate(Date exchangeInsuranceUseDate) {
        this.exchangeInsuranceUseDate = exchangeInsuranceUseDate;
    }

    /**
     * getter method
     * @return a double value with the amount to use in exchange insurance
     */
    public Double getExchangeInsuranceAmountToUse() {
        return exchangeInsuranceAmountToUse;
    }

    /**
     * setter method
     * @param exchangeInsuranceAmountToUse a double value with the amount to use in exchange insurance
     */
    public void setExchangeInsuranceAmountToUse(Double exchangeInsuranceAmountToUse) {
        this.exchangeInsuranceAmountToUse = exchangeInsuranceAmountToUse;
    }

    /**
     * getter method
     * @return a string with the sell currency of the exchange insurance
     */
    public String getExchangeInsuranceSellCurrency() {
        return exchangeInsuranceSellCurrency;
    }

    /**
     * setter method
     * @param exchangeInsuranceSellCurrency a string with the sell currency of the exchange insurance
     */
    public void setExchangeInsuranceSellCurrency(String exchangeInsuranceSellCurrency) {
        this.exchangeInsuranceSellCurrency = exchangeInsuranceSellCurrency;
    }

    /**
     * getter method
     * @return a string with the buy currency of the exchange insurance
     */
    public String getExchangeInsuranceBuyCurrency() {
        return exchangeInsuranceBuyCurrency;
    }

    /**
     * setter method
     * @param exchangeInsuranceBuyCurrency a string with the buy currency of the exchange insurance
     */
    public void setExchangeInsuranceBuyCurrency(String exchangeInsuranceBuyCurrency) {
        this.exchangeInsuranceBuyCurrency = exchangeInsuranceBuyCurrency;
    }

    /**
     * setter method
     * @param exchangeInsurances a list of exchange insurances
     */
    public void setExchangeInsurances(List<ExchangeInsurance> exchangeInsurances) {
        this.exchangeInsurances = exchangeInsurances;
    }

    /**
     * getter method
     * @return a RiskLine object with the risk line
     */
    public RiskLine getRiskLine() {
        return riskLine;
    }

    /**
     * setter method
     * @param riskLine a RiskLine object with the risk line
     */
    public void setRiskLine(RiskLine riskLine) {
        this.riskLine = riskLine;
    }

    /**
     * getter method
     * @return a Date object with the expiration date
     */
    public Date getExpiration() {
        return expiration;
    }

    /**
     * setter method
     * @param expiration a Date object with the expiration date
     */
    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    /**
     * getter method
     * @return an integer value with the saved step( step from external app)
     */
    public Integer getSavedStep() {
        return savedStep;
    }

    /**
     * setter method
     * @param savedStep an integer value with the saved step( step from external app)
     */
    public void setSavedStep(Integer savedStep) {
        this.savedStep = savedStep;
    }

    /**
     * getter method
     * @return a string value with the contract reference
     */
    public String getContractReference() {
        return contractReference;
    }

    /**
     * setter method
     * @param contractReference a string value with the contract reference
     */
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
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
        if (!super.equals(o)) {
            return false;
        }
        ExportCollectionAdvanceRequest request = (ExportCollectionAdvanceRequest) o;
        return Objects.equals(customer, request.customer) &&
                Objects.equals(exportCollection, request.exportCollection) &&
                Objects.equals(amount, request.amount) &&
                Objects.equals(currency, request.currency) &&
                Objects.equals(exchangeInsurances, request.exchangeInsurances) &&
                Objects.equals(exchangeInsuranceUseDate, request.exchangeInsuranceUseDate) &&
                Objects.equals(exchangeInsuranceAmountToUse, request.exchangeInsuranceAmountToUse) &&
                Objects.equals(exchangeInsuranceSellCurrency, request.exchangeInsuranceSellCurrency) &&
                Objects.equals(exchangeInsuranceBuyCurrency, request.exchangeInsuranceBuyCurrency) &&
                Objects.equals(riskLine, request.riskLine) &&
                Objects.equals(expiration, request.expiration) &&
                Objects.equals(savedStep, request.savedStep) &&
                Objects.equals(contractReference, request.contractReference);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customer, exportCollection, amount, currency, exchangeInsurances,
                exchangeInsuranceUseDate, exchangeInsuranceAmountToUse, exchangeInsuranceSellCurrency,
                exchangeInsuranceBuyCurrency, riskLine, expiration, savedStep, contractReference);
    }
}
