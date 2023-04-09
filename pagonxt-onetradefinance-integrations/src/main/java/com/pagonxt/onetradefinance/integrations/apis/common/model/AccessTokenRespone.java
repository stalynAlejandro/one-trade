package com.pagonxt.onetradefinance.integrations.apis.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Access Token
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenRespone {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("scope")
    private String scope;

    /**
     * getter method
     * @return a string with the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * setter method
     * @param accessToken a string with the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * getter method
     * @return a string with the token type
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * setter method
     * @param tokenType a string with the token type
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * getter method
     * @return a long value with the expiration time
     */
    public long getExpiresIn() {
        return expiresIn;
    }

    /**
     * setter method
     * @param expiresIn a long value with the expiration time
     */
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * getter method
     * @return a string with the token scope
     */
    public String getScope() {
        return scope;
    }

    /**
     * setter method
     * @param scope a string with the token scope
     */
    public void setScope(String scope) {
        this.scope = scope;
    }
}
