package com.pagonxt.onetradefinance.integrations.apis.riskline.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount;

import java.util.Objects;


 /**
 *  Model class for risk lines
 *  Response to a request to retrieve a risk line contracts for a customer
 *  Include class attributes, getters and setters
 *  @author -
 *  @version jdk-11.0.13
 *  @since jdk-11.0.13
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskLineList {

    @JsonProperty("riskLineId")
    private String riskLineId;

    @JsonProperty("product")
    private String product;

    @JsonProperty("lineAmount")
    private CurrencyAmount lineAmount;

    @JsonProperty("availableAmount")
    private CurrencyAmount availableAmount;

    @JsonProperty("usedAmount")
    private CurrencyAmount usedAmount;

    @JsonProperty("openingDate")
    private String openingDate;

    @JsonProperty("maturityDate")
    private String maturityDate;

    @JsonProperty("riskLineStatus")
    private String riskLineStatus;

    /**
     * getter method
     * @return a string with the id of the risk line
    */
    public String getRiskLineId() {
        return riskLineId;
    }

     /**
      * setter method
      * @param riskLineId a string with the id of the risk line
      */
    public void setRiskLineId(String riskLineId) {
        this.riskLineId = riskLineId;
    }

     /**
      * getter method
      * @return a string with the product
      */
    public String getProduct() {
        return product;
    }

     /**
      * setter method
      * @param product a string with the product
      */
    public void setProduct(String product) {
        this.product = product;
    }

     /**
      * getter method
      * @return a string with the line amount
      */
    public CurrencyAmount getLineAmount() {
        return lineAmount;
    }

     /**
      * setter method
      * @param lineAmount a string with the line amount
      */
    public void setLineAmount(CurrencyAmount lineAmount) {
        this.lineAmount = lineAmount;
    }

     /**
      * getter method
      * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
      * @return a CurrencyAmount object with the available amount
      */
    public CurrencyAmount getAvailableAmount() {
        return availableAmount;
    }

     /**
      * setter method
      * @param availableAmount a CurrencyAmount object with the available amount
      * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
      */
    public void setAvailableAmount(CurrencyAmount availableAmount) {
        this.availableAmount = availableAmount;
    }

     /**
      * getter method
      * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
      * @return a CurrencyAmount object with the used amount
      */
    public CurrencyAmount getUsedAmount() {
        return usedAmount;
    }

     /**
      * setter method
      * @param usedAmount a CurrencyAmount object with the used amount
      * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
      */
    public void setUsedAmount(CurrencyAmount usedAmount) {
        this.usedAmount = usedAmount;
    }

     /**
      * getter method
      * @return a string with the opening date
      */
    public String getOpeningDate() {
        return openingDate;
    }

     /**
      * setter method
      * @param openingDate a string with the opening date
      * @see com.pagonxt.onetradefinance.integrations.apis.common.model.CurrencyAmount
      */
    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

     /**
      * getter method
      * @return a string with the maturity date
      */
    public String getMaturityDate() {
        return maturityDate;
    }

     /**
      * setter method
      * @param maturityDate a string with the maturity date
      */
    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

     /**
      * getter method
      * @return a string with the status of the risk line
      */
    public String getRiskLineStatus() {
        return riskLineStatus;
    }

     /**
      * setter method
      * @param riskLineStatus a string with the status of the risk line
      */
    public void setRiskLineStatus(String riskLineStatus) {
        this.riskLineStatus = riskLineStatus;
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
        if (!(o instanceof RiskLineList)) {
            return false;
        }
        RiskLineList riskLineList = (RiskLineList) o;
        return Objects.equals(getRiskLineId(), riskLineList.getRiskLineId()) &&
                Objects.equals(getProduct(), riskLineList.getProduct()) &&
                Objects.equals(getLineAmount(), riskLineList.getLineAmount()) &&
                Objects.equals(getAvailableAmount(), riskLineList.getAvailableAmount()) &&
                Objects.equals(getUsedAmount(), riskLineList.getUsedAmount()) &&
                Objects.equals(getOpeningDate(), riskLineList.getOpeningDate()) &&
                Objects.equals(getMaturityDate(), riskLineList.getMaturityDate()) &&
                Objects.equals(getRiskLineStatus(), riskLineList.getRiskLineStatus());
    }

     /**
      * hashCode method
      * @return a hash value of the sequence of input values
      */
    @Override
    public int hashCode() {
        return Objects.hash(getRiskLineId(),
                            getProduct(),
                            getLineAmount(),
                            getAvailableAmount(),
                            getUsedAmount(),
                            getOpeningDate(),
                            getMaturityDate(),
                            getRiskLineStatus());
    }

     /**
      * toString method
      * @return a String with class attributes and its values
      */
    @Override
    public String toString() {
        return "RiskLine{" +
                "riskLineId='" + riskLineId + '\'' +
                ", product='" + product + '\'' +
                ", lineAmount=" + lineAmount +
                ", availableAmount=" + availableAmount +
                ", usedAmount=" + usedAmount +
                ", openingDate='" + openingDate + '\'' +
                ", maturityDate='" + maturityDate + '\'' +
                ", riskLineStatus='" + riskLineStatus + '\'' +
                '}';
    }
}
