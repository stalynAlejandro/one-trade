package com.pagonxt.onetradefinance.integrations.apis.riskline.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * Model class for responses about the risk lines
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderRiskLineResponse extends RiskLineList {

    @JsonProperty("customerId")
    private String customerId;

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
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SantanderRiskLineResponse)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SantanderRiskLineResponse that = (SantanderRiskLineResponse) o;
        return Objects.equals(getCustomerId(), that.getCustomerId());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCustomerId());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SantanderRiskLineResponse{" +
                "customerId='" + customerId + '\'' +
                "} " + super.toString();
    }
}
