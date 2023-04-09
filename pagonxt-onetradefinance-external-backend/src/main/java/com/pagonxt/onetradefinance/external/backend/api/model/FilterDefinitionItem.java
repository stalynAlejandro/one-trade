package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * This class defines a filter definition item object
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class FilterDefinitionItem {

    //class attributes
    private String description;

    private String code;

    /**
     * Empty Class constructor
     */
    public FilterDefinitionItem() {
    }

    /**
     * Class constructor
     * @param code filter definition item code
     * @param description filter definition item description
     */
    public FilterDefinitionItem(String code, String description) {
        this.description = description;
        this.code = code;
    }

    /**
     * getter method
     * @return the description of the filter definition
     */
    public String getDescription() {
        return description;
    }

    /**
     * setter method
     * @param description a string with the description of the filter definition
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getter method
     * @return the filter definition id
     */
    public String getCode() {
        return code;
    }

    /**
     * setter methos
     * @param code a string with the filter definition id
     */
    public void setCode(String code) {
        this.code = code;
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
        FilterDefinitionItem that = (FilterDefinitionItem) o;
        return Objects.equals(description, that.description) && Objects.equals(code, that.code);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, code);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "FilterDefinitionItem{" +
                "description='" + description + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
