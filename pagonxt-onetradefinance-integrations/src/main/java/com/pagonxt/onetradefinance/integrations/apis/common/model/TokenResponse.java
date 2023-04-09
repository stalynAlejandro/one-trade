package com.pagonxt.onetradefinance.integrations.apis.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for token responses
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

    @JsonProperty("token")
    private String token;

    /**
     * getter method
     * @return a string with token
     */
    public String getToken() {
        return token;
    }

    /**
     * setter method
     * @param token a string with token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
