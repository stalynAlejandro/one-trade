package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Transactions
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Transaction {
    @JsonProperty("transactionId")
    private String transactionId;
    @JsonProperty("creationDate")
    private String creationDate;
    @JsonProperty("processedDate")
    private String processedDate;
    @JsonProperty("accountingDate")
    private String accountingDate;
    @JsonProperty("description")
    private String description;
    @JsonProperty("transactionType")
    private String transactionType;
    @JsonProperty("transactionCategory")
    private String transactionCategory;
    @JsonProperty("amount")
    private Balance amount;
    @JsonProperty("transactionDetailsLink")
    private String transactionDetailsLink;

    /**
     * getter method
     * @return a string with the transaction id
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * setter method
     * @param transactionId a string with the transaction id
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * getter method
     * @return a string with the creation date of the transaction
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * setter method
     * @param creationDate a string with the creation date of the transaction
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * getter method
     * @return a string with the processed date of the transaction
     */
    public String getProcessedDate() {
        return processedDate;
    }

    /**
     * setter method
     * @param processedDate a string with the processed date of the transaction
     */
    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

    /**
     * getter method
     * @return a string with the accounting date of the transaction
     */
    public String getAccountingDate() {
        return accountingDate;
    }

    /**
     * setter method
     * @param accountingDate a string with the accounting date of the transaction
     */
    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }

    /**
     * getter method
     * @return a string with the description of the transaction
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter method
     * @param description a string with the description of the transaction
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getter method
     * @return a string with the transaction type
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * setter method
     * @param transactionType a string with the transaction type
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * getter method
     * @return a string with the transaction category
     */
    public String getTransactionCategory() {
        return transactionCategory;
    }

    /**
     * setter method
     * @param transactionCategory a string with the transaction category
     */
    public void setTransactionCategory(String transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     * @return an object with the transaction amount
     */
    public Balance getAmount() {
        return amount;
    }

    /**
     * setter method
     * @param amount an object with the transaction amount
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.Balance
     */
    public void setAmount(Balance amount) {
        this.amount = amount;
    }

    /**
     * getter method
     * @return a string with the link of the transaction details
     */
    public String getTransactionDetailsLink() {
        return transactionDetailsLink;
    }

    /**
     * setter method
     * @param transactionDetailsLink a string with the link of the transaction details
     */
    public void setTransactionDetailsLink(String transactionDetailsLink) {
        this.transactionDetailsLink = transactionDetailsLink;
    }
}
