package com.pagonxt.onetradefinance.integrations.apis.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Santander errors in api operations(query,...)
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderError {
    @JsonProperty("code")
    private String code;

    @JsonProperty("message")
    private String message;

    @JsonProperty("level")
    private String level;

    @JsonProperty("description")
    private String description;

    /**
     * getter method
     * @return a string with error code
     */
    public String getCode() {
        return code;
    }

    /**
     * getter method
     * @return a string with error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * getter method
     * @return a string with error level
     */
    public String getLevel() {
        return level;
    }

    /**
     * getter method
     * @return a string with error description
     */
    public String getDescription() {
        return description;
    }
}
