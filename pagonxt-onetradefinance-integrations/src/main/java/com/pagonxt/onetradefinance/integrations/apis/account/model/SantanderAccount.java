package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model class for Santander Account
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderAccount {

    @JsonProperty("displayNumber")
    private String displayNumber;
    @JsonProperty("accountId")
    private String accountId;
    @JsonProperty("accountIdType")
    private String accountIdType;
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("mainItem")
    private boolean mainItem;
    @JsonProperty("type")
    private String type;
    @JsonProperty("description")
    private String description;
    @JsonProperty("status")
    private String status;
    @JsonProperty("customerId")
    private String customerId;
    @JsonProperty("lastTransactionDate")
    private String lastTransactionDate;
    @JsonProperty("relatedCards")
    private List<RelatedCard> relatedCards;
    @JsonProperty("bankInformation")
    private BankInformation bankInformation;
    @JsonProperty("marketPackages")
    private List<MarkedPackageItem> marketPackages;
    @JsonProperty("costCenter")
    private String costCenter;
    @JsonProperty("mainBalance")
    private Balance mainBalance;
    @JsonProperty("availableBalance")
    private Balance availableBalance;
    @JsonProperty("pendingBalance")
    private Balance pendingBalance;
    @JsonProperty("withholdingBalance")
    private Balance withholdingBalance;
    @JsonProperty("overdraftLimit")
    private Balance overdraftLimit;
    @JsonProperty("overdraftUsed")
    private Balance overdraftUsed;
    @JsonProperty("availableWithoutAuthorized")
    private Balance availableWithoutAuthorized;
    @JsonProperty("latestTransactions")
    private List<Transaction> latestTransactions;
    @JsonProperty("transactionsListLink")
    private String transactionsListLink;
    @JsonProperty("_links")
    private AccountLinks links;

    /**
     * getter method
     * @return a string with the display number
     */
    public String getDisplayNumber() {
        return displayNumber;
    }

    /**
     * setter method
     * @param displayNumber a string with the display number
     */
    public void setDisplayNumber(String displayNumber) {
        this.displayNumber = displayNumber;
    }

    /**
     * getter method
     * @return a string with the account id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * setter method
     * @param accountId a string with the account id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    /**
     * getter method
     * @return a string with the type of account id
     */
    public String getAccountIdType() {
        return accountIdType;
    }

    /**
     * setter method
     * @param accountIdType a string with the type of account id
     */
    public void setAccountIdType(String accountIdType) {
        this.accountIdType = accountIdType;
    }

    /**
     * getter method
     * @return a string with the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * setter method
     * @param alias a string with the alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * getter method
     * @return true or false if the account is main item
     */
    public boolean isMainItem() {
        return mainItem;
    }

    /**
     * setter method
     * @param mainItem true or false if the account is main item
     */
    public void setMainItem(boolean mainItem) {
        this.mainItem = mainItem;
    }

    /**
     * getter method
     * @return a string with the type of the account
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type a string with the type of the account
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return a string with a description
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter method
     * @param description a string with a description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * @param status a string with the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

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
     * @return a string with the date of the last transaction
     */
    public String getLastTransactionDate() {
        return lastTransactionDate;
    }

    /**
     * setter method
     * @param lastTransactionDate a string with the date of the last transaction
     */
    public void setLastTransactionDate(String lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.RelatedCard
     * @return a list of related cards
     */
    public List<RelatedCard> getRelatedCards() {
        return relatedCards;
    }

    /**
     * setter method
     * @param relatedCards a list of related cards
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.RelatedCard
     */
    public void setRelatedCards(List<RelatedCard> relatedCards) {
        this.relatedCards = relatedCards;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.BankInformation
     * @return an object with the bank information
     */
    public BankInformation getBankInformation() {
        return bankInformation;
    }

    /**
     * setter method
     * @param bankInformation an object with the bank information
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.BankInformation
     */
    public void setBankInformation(BankInformation bankInformation) {
        this.bankInformation = bankInformation;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.MarkedPackageItem
     * @return a list of marked packages
     */
    public List<MarkedPackageItem> getMarketPackages() {
        return marketPackages;
    }

    /**
     * setter method
     * @param marketPackages a list of marked packages
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.MarkedPackageItem
     */
    public void setMarketPackages(List<MarkedPackageItem> marketPackages) {
        this.marketPackages = marketPackages;
    }

    /**
     * getter method
     * @return a string with the cost center
     */
    public String getCostCenter() {
        return costCenter;
    }

    /**
     * setter method
     * @param costCenter a string with the cost center
     */
    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the main balance
     */
    public Balance getMainBalance() {
        return mainBalance;
    }

    /**
     * setter method
     * @param mainBalance an object with the main balance
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setMainBalance(Balance mainBalance) {
        this.mainBalance = mainBalance;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the available balance
     */
    public Balance getAvailableBalance() {
        return availableBalance;
    }

    /**
     * setter method
     * @param availableBalance an object with the available balance
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setAvailableBalance(Balance availableBalance) {
        this.availableBalance = availableBalance;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the pending balance
     */
    public Balance getPendingBalance() {
        return pendingBalance;
    }

    /**
     * setter method
     * @param pendingBalance an object with the pending balance
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setPendingBalance(Balance pendingBalance) {
        this.pendingBalance = pendingBalance;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the holding balance
     */
    public Balance getWithholdingBalance() {
        return withholdingBalance;
    }

    /**
     * setter method
     * @param withholdingBalance an object with the holding balance
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setWithholdingBalance(Balance withholdingBalance) {
        this.withholdingBalance = withholdingBalance;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the overdraft limit
     */
    public Balance getOverdraftLimit() {
        return overdraftLimit;
    }

    /**
     * setter method
     * @param overdraftLimit an object with the overdraft limit
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setOverdraftLimit(Balance overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the overdraft used
     */
    public Balance getOverdraftUsed() {
        return overdraftUsed;
    }

    /**
     * setter method
     * @param overdraftUsed an object with the overdraft used
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setOverdraftUsed(Balance overdraftUsed) {
        this.overdraftUsed = overdraftUsed;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the available balance without authorized
     */
    public Balance getAvailableWithoutAuthorized() {
        return availableWithoutAuthorized;
    }

    /**
     * setter method
     * @param availableWithoutAuthorized an object with the available balance without authorized
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setAvailableWithoutAuthorized(Balance availableWithoutAuthorized) {
        this.availableWithoutAuthorized = availableWithoutAuthorized;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Transaction
     * @return a list with the latest transactions
     */
    public List<Transaction> getLatestTransactions() {
        return latestTransactions;
    }

    /**
     * setter method
     * @param latestTransactions list with the latest transactions
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Transaction
     */
    public void setLatestTransactions(List<Transaction> latestTransactions) {
        this.latestTransactions = latestTransactions;
    }

    /**
     * getter method
     * @return a string with the link to transactions link
     */
    public String getTransactionsListLink() {
        return transactionsListLink;
    }

    /**
     * setter method
     * @param transactionsListLink a string with the link to transactions link
     */
    public void setTransactionsListLink(String transactionsListLink) {
        this.transactionsListLink = transactionsListLink;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.AccountLinks
     * @return an object with account links
     */
    public AccountLinks getLinks() {
        return links;
    }

    /**
     * setter method
     * @param links an object with account links
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.AccountLinks
     */
    public void setLinks(AccountLinks links) {
        this.links = links;
    }
}
