package com.pagonxt.onetradefinance.integrations.apis.specialprices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 *  Model class for the concept of special prices
 *  Include class attributes, getters and setters
 *  @author -
 *  @version jdk-11.0.13
 *  @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Concept {

    /**
     * 'Settlement concept (fee associated with the product) ID.
     * You can use this parameter to retrieve a specific settlement concept.
     */
    @JsonProperty("conceptId")
    private String conceptId;

    /**
     * Currency name
     */
    @JsonProperty("conceptName")
    private String conceptName;

    /**
     * Data structure containing special price details
     */
    @JsonProperty("specialPrice")
    private SpecialPrice specialPrice;

    /**
     * Data structure containing standard price details
     */
    @JsonProperty("standardPrice")
    private StandardPrice standardPrice;

    /**
     * getter method
     * @return a string with the concept id
     */
    public String getConceptId() {
        return conceptId;
    }

    /**
     * setter method
     * @param conceptId a string with the concept id
     */
    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    /**
     * getter method
     * @return a string with the concept name
     */
    public String getConceptName() {
        return conceptName;
    }

    /**
     * setter method
     * @param conceptName a string with the concept name
     */
    public void setConceptName(String conceptName) {
        this.conceptName = conceptName;
    }

    /**
     * getter method
     * @return a StandardPrice object
     */
    public StandardPrice getStandardPrice() {
        return standardPrice;
    }


    /**
     * setter method
     * @param standardPrice a StandardPrice object
     */
    public void setStandardPrice(StandardPrice standardPrice) {
        this.standardPrice = standardPrice;
    }

    /**
     * getter method
     * @return a SpecialPrice object
     */
    public SpecialPrice getSpecialPrice() {
        return specialPrice;
    }

    /**
     * setter method
     * @param specialPrice a SpecialPrice object
     */
    public void setSpecialPrice(SpecialPrice specialPrice) {
        this.specialPrice = specialPrice;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Concept concept = (Concept) o;
        return Objects.equals(conceptId, concept.conceptId) &&
                Objects.equals(conceptName, concept.conceptName) &&
                Objects.equals(standardPrice, concept.standardPrice) &&
                Objects.equals(specialPrice, concept.specialPrice);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(conceptId, conceptName, standardPrice, specialPrice);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Concepts{" +
                "conceptId='" + conceptId + '\'' +
                ", conceptName='" + conceptName + '\'' +
                ", standardPrice=" + standardPrice +
                ", specialPrice=" + specialPrice +
                '}';
    }
}
