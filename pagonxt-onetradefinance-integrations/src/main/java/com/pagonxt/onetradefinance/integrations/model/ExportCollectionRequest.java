package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Model class for advance requests of export collections
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see Customer
 * @see RiskLine
 * @see PagoNxtRequest
 * @see Account
 * @since jdk-11.0.13
 */
public class ExportCollectionRequest extends PagoNxtRequest {

    @NotNull
    private String currency;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("9999999999.99")
    private Double amount;

    private boolean customerHasAccount;

    private Account nominalAccount;

    private Account commissionAccount;

    private boolean applyingForAdvance;

    private RiskLine advanceRiskLine;

    private String advanceCurrency;

    @DecimalMin("0.0")
    @DecimalMax("9999999999.99")
    private Double advanceAmount;

    @NotNull
    private Customer customer;

    private Integer savedStep;

    @Size(max = 30)
    private String clientReference;

    @Size(max = 50)
    private String debtorName;

    @Size(max = 50)
    private String debtorBank;

    @NotNull
    private String collectionType;

    private Date advanceExpiration;

    private boolean clientAcceptance;

    private String contractReference;

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
     * @return true or false if the customer has an account
     */
    public boolean isCustomerHasAccount() {
        return customerHasAccount;
    }

    /**
     * setter method
     * @param customerHasAccount true or false if the customer has an account
     */
    public void setCustomerHasAccount(boolean customerHasAccount) {
        this.customerHasAccount = customerHasAccount;
    }

    /**
     * getter method
     * @return an Account object with the nominal account
     */
    public Account getNominalAccount() {
        return nominalAccount;
    }

    /**
     * setter method
     * @param nominalAccount an Account object with the nominal account
     */
    public void setNominalAccount(Account nominalAccount) {
        this.nominalAccount = nominalAccount;
    }

    /**
     * getter method
     * @return an Account object with the commission account
     */
    public Account getCommissionAccount() {
        return commissionAccount;
    }

    /**
     * setter method
     * @param commissionAccount an Account object with the commission account
     */
    public void setCommissionAccount(Account commissionAccount) {
        this.commissionAccount = commissionAccount;
    }

    /**
     * getter method
     * @return true or false if the customer is applying for advance
     */
    public boolean isApplyingForAdvance() {
        return applyingForAdvance;
    }

    /**
     * setter method
     * @param applyingForAdvance true or false if the customer is applying for advance
     */
    public void setApplyingForAdvance(boolean applyingForAdvance) {
        this.applyingForAdvance = applyingForAdvance;
    }

    /**
     * getter method
     * @return a RiskLine object with the risk line
     */
    public RiskLine getAdvanceRiskLine() {
        return advanceRiskLine;
    }

    /**
     * setter method
     * @param advanceRiskLine a RiskLine object with the risk line
     */
    public void setAdvanceRiskLine(RiskLine advanceRiskLine) {
        this.advanceRiskLine = advanceRiskLine;
    }

    /**
     * getter method
     * @return a string with the advance currency
     */
    public String getAdvanceCurrency() {
        return advanceCurrency;
    }

    /**
     * setter method
     * @param advanceCurrency a string with the advance currency
     */
    public void setAdvanceCurrency(String advanceCurrency) {
        this.advanceCurrency = advanceCurrency;
    }

    /**
     * getter method
     * @return a double value with the advance amount
     */
    public Double getAdvanceAmount() {
        return advanceAmount;
    }

    /**
     * setter method
     * @param advanceAmount a double value with the advance amount
     */
    public void setAdvanceAmount(Double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

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
     * @return an integer value with saved step (external app)
     */
    public Integer getSavedStep() {
        return savedStep;
    }

    /**
     * setter method
     * @param savedStep an integer value with saved step (external app)
     */
    public void setSavedStep(Integer savedStep) {
        this.savedStep = savedStep;
    }

    /**
     * getter method
     * @return a string with the client reference
     */
    public String getClientReference() {
        return clientReference;
    }

    /**
     * setter method
     * @param clientReference a string with the client reference
     */
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    /**
     * getter method
     * @return a string with the debtor name
     */
    public String getDebtorName() {
        return debtorName;
    }

    /**
     * setter method
     * @param debtorName a string with the debtor name
     */
    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    /**
     * getter method
     * @return a string with the debtor bank
     */
    public String getDebtorBank() {
        return debtorBank;
    }

    /**
     * setter method
     * @param debtorBank a string with the debtor bank
     */
    public void setDebtorBank(String debtorBank) {
        this.debtorBank = debtorBank;
    }

    /**
     * getter method
     * @return a string with the collection type
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * setter method
     * @param collectionType a string with the collection type
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    /**
     * getter method
     * @return a Date object with the advance expiration
     */
    public Date getAdvanceExpiration() {
        return advanceExpiration;
    }

    /**
     * setter method
     * @param advanceExpiration a Date object with the advance expiration
     */
    public void setAdvanceExpiration(Date advanceExpiration) {
        this.advanceExpiration = advanceExpiration;
    }

    /**
     * getter method
     * @return true or false if client has accepted the prices chart
     */
    public boolean isClientAcceptance() {
        return clientAcceptance;
    }

    /**
     * setter method
     * @param clientAcceptance true or false if client has accepted the prices chart
     */
    public void setClientAcceptance(boolean clientAcceptance) {
        this.clientAcceptance = clientAcceptance;
    }

    /**
     * getter method
     * @return a string with the contract reference
     */
    public String getContractReference() {
        return contractReference;
    }

    /**
     * setter method
     * @param contractReference a string with the contract reference
     */
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }
}
