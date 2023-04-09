package com.pagonxt.onetradefinance.integrations.apis.fxdeal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pagonxt.onetradefinance.integrations.apis.common.model.Links;

import java.util.List;

/**
 * Model class for the responses about fx deals
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SantanderFxDealResponse {

    @JsonProperty("fxDealList")
    private List<FxDeal> fxDealList;

    @JsonProperty("_links")
    private Links links;

    /**
     * getter method
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDeal
     * @return a list of fx deals
     */
    public List<FxDeal> getFxDealList() {
        return fxDealList;
    }

    /**
     * setter method
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDeal
     * @param fxDealList a list of fx deals
     */
    public void setFxDealList(List<FxDeal> fxDealList) {
        this.fxDealList = fxDealList;
    }

    /**
     * getter method
     * @return a links object
     */
    public Links getLinks() {
        return links;
    }

    /**
     * setter method
     * @param links a links object
     */
    public void setLinks(Links links) {
        this.links = links;
    }
}
