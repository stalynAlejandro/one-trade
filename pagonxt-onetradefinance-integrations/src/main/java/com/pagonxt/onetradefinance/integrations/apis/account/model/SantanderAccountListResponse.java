package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagonxt.onetradefinance.integrations.apis.common.model.Links;

import java.util.List;

/**
 * Model class for Santander accounts list
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderAccountListResponse {

    @JsonProperty("accountsDataList")
    List<SantanderAccount> accountsDataList;
    @JsonProperty("_links")
    Links links;

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount
     * @return a list of Santander accounts
     */
    public List<SantanderAccount> getAccountsDataList() {
        return accountsDataList;
    }

    /**
     * setter method
     * @param accountsDataList a list of Santander accounts
     * @see com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount
     */
    public void setAccountsDataList(List<SantanderAccount> accountsDataList) {
        this.accountsDataList = accountsDataList;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.Links
     * @return a Links object
     */
    public Links getLinks() {
        return links;
    }

    /**
     * setter method
     * @param links a Links object
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.Links
     */
    public void setLinks(Links links) {
        this.links = links;
    }
}
