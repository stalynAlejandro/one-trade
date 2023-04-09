package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Account Links
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountLinks {

    @JsonProperty("accountDetailsLink")
    private String accountDetailsLink;
    @JsonProperty("transactionsListLink")
    private String transactionsListLink;

    /**
     * getter method
     * @return a String with the account details link
     */
    public String getAccountDetailsLink() {
        return accountDetailsLink;
    }

    /**
     * setter method
     * @param accountDetailsLink a String with the account details link
     */
    public void setAccountDetailsLink(String accountDetailsLink) {
        this.accountDetailsLink = accountDetailsLink;
    }

    /**
     * getter method
     * @return a String with the transactions list link
     */
    public String getTransactionsListLink() {
        return transactionsListLink;
    }

    /**
     * setter method
     * @param transactionsListLink a String with the transactions list link
     */
    public void setTransactionsListLink(String transactionsListLink) {
        this.transactionsListLink = transactionsListLink;
    }
}
