package com.pagonxt.onetradefinance.integrations.apis.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Model class for Santander errors responses in api operations(query,...)
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderErrorResponse {
    @JsonProperty("errors")
    List<SantanderError> errors;

    /**
     * getter method
     * @return a list of Santander errors
     */
    public List<SantanderError> getErrors() {
        return errors;
    }
}
