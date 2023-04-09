package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Related Card
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedCard {

    @JsonProperty("displayNumber")
    private String displayNumber;
    @JsonProperty("cardLink")
    private String cardLink;

    /**
     * getter method
     * @return a string with the display number
     */
    public String getDisplayNumber() {
        return displayNumber;
    }

    /**
     * setter method
     * @param displayNumber a string with the display number
     */
    public void setDisplayNumber(String displayNumber) {
        this.displayNumber = displayNumber;
    }

    /**
     * getter method
     * @return a string with the card link
     */
    public String getCardLink() {
        return cardLink;
    }

    /**
     * setter method
     * @param cardLink a string with the card link
     */
    public void setCardLink(String cardLink) {
        this.cardLink = cardLink;
    }
}
