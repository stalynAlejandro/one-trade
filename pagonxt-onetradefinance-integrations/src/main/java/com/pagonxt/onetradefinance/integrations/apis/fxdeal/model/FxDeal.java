package com.pagonxt.onetradefinance.integrations.apis.fxdeal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for fx deals
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FxDeal {

    @JsonProperty("_fxDealDetailsLink")
    private FxDealDetails fxDealDetails;

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealDetails
     * @return a FXDealDetails object with the details of FX Deal
     */
    public FxDealDetails getFxDealDetails() {
        return fxDealDetails;
    }

    /**
     * setter method
     * @param fxDealDetails a FXDealDetails object with the details of FX Deal
     */
    public void setFxDealDetails(FxDealDetails fxDealDetails) {
        this.fxDealDetails = fxDealDetails;
    }
}
