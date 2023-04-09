package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Country
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;

    /**
     * getter method
     * @return a string with the country code
     */
    public String getCode() {
        return code;
    }

    /**
     * setter method
     * @param code string with the country code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * getter method
     * @return a string with the country name
     */
    public String getName() {
        return name;
    }

    /**
     * setter method
     * @param name string with the country name
     */
    public void setName(String name) {
        this.name = name;
    }
}
