package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagonxt.onetradefinance.integrations.apis.common.model.Links;

import java.util.List;
import java.util.Objects;

/**
 * Model class for the responses of the special prices
 * Response to a request to retrieve the special and standard prices for a trade
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderTradeSpecialPricesResponse {

    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("currencyName")
    private String currencyName;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("conceptsList")
    private List<Concept> concepts;

    @JsonProperty("_links")
    private Links links;

    /**
     * getter method
     * @return a string with the currency code
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * setter method
     * @param currencyCode a string with the currency code
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * getter method
     * @return a string with the currency name
     */
    public String getCurrencyName() {
        return currencyName;
    }

    /**
     * setter method
     * @param currencyName a string with the currency name
     */
    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    /**
     * getter method
     * @return a string with the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * setter method
     * @param productName a string with the product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.Concept
     * @return a list of concepts
     */
    public List<Concept> getConcepts() {
        return concepts;
    }

    /**
     * setter method
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.Concept
     * @param concepts a list of concepts
     */
    public void setConcepts(List<Concept> concepts) {
        this.concepts = concepts;
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
        if (!(o instanceof SantanderTradeSpecialPricesResponse)) {
            return false;
        }
        SantanderTradeSpecialPricesResponse that = (SantanderTradeSpecialPricesResponse) o;
        return Objects.equals(getCurrencyCode(), that.getCurrencyCode()) &&
                Objects.equals(getCurrencyName(), that.getCurrencyName()) &&
                Objects.equals(getProductName(), that.getProductName()) &&
                Objects.equals(getConcepts(), that.getConcepts()) &&
                Objects.equals(getLinks(), that.getLinks());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCurrencyCode(), getCurrencyName(), getProductName(), getConcepts(), getLinks());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "SantanderTradeSpecialPricesResponse{" +
                "currencyCode='" + currencyCode + '\'' +
                ", currencyName='" + currencyName + '\'' +
                ", productName='" + productName + '\'' +
                ", conceptsList=" + concepts +
                ", links=" + links +
                '}';
    }
}
