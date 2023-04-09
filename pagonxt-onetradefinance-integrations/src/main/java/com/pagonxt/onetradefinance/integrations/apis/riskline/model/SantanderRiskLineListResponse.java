package com.pagonxt.onetradefinance.integrations.apis.riskline.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagonxt.onetradefinance.integrations.apis.common.model.Links;

import java.util.List;
import java.util.Objects;

/**
 * Model class for responses about the risk lines
 * Response to a request to retrieve a list of risk line contracts for a customer
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderRiskLineListResponse {
    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("riskLines")
    private List<RiskLineList> riskLines;

    @JsonProperty("links")
    private Links links;

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
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineList
     * @return a list of risk lines
     */
    public List<RiskLineList> getRiskLines() {
        return riskLines;
    }

    /**
     * setter method
     * @param riskLines a list of risk lines
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineList
     */
    public void setRiskLines(List<RiskLineList> riskLines) {
        this.riskLines = riskLines;
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
     * @see com.pagonxt.onetradefinance.integrations.apis.common.model.Links
     * @param links a Links object
     */
    public void setLinks(Links links) {
        this.links = links;
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
        if (!(o instanceof SantanderRiskLineListResponse)) {
            return false;
        }
        SantanderRiskLineListResponse that = (SantanderRiskLineListResponse) o;
        return Objects.equals(getCustomerId(), that.getCustomerId()) &&
                Objects.equals(getRiskLines(), that.getRiskLines()) &&
                Objects.equals(getLinks(), that.getLinks());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCustomerId(), getRiskLines(), getLinks());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SantanderRiskLine{" +
                "customerId='" + customerId + '\'' +
                ", riskLines=" + riskLines +
                ", links=" + links +
                '}';
    }
}

