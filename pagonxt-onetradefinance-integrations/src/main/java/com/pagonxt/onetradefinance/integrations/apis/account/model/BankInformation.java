package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Bank Information
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankInformation {

    @JsonProperty("localBankId")
    private String localBankId;
    @JsonProperty("country")
    private Country country;

    /**
     * getter method
     * @return a String with the id of local bank
     */
    public String getLocalBankId() {
        return localBankId;
    }

    /**
     * setter method
     * @param localBankId a String with the id of local bank
     */
    public void setLocalBankId(String localBankId) {
        this.localBankId = localBankId;
    }

    /**
     * getter method
     * @see  com.pagonxt.onetradefinance.integrations.apis.account.model.Country
     * @return a Country object with the country of the bank
     */
    public Country getCountry() {
        return country;
    }

    /**
     * setter method
     * @see  com.pagonxt.onetradefinance.integrations.apis.account.model.Country
     * @param country a Country object with the country of the bank
     */
    public void setCountry(Country country) {
        this.country = country;
    }
}
