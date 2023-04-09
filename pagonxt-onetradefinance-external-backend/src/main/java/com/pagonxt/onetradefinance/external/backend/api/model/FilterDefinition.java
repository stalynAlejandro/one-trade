package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.List;
import java.util.Objects;

/**
 * This class defines a filter definition object
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.FilterDefinitionItem
 * @since jdk-11.0.13
 */
public class FilterDefinition {

    private String type;

    private List<FilterDefinitionItem> options;

    /**
     * Empty Class constructor
     */
    public FilterDefinition() {
    }

    /**
     * Class constructor
     * @param type filter definition type
     */
    public FilterDefinition(String type) {
        this.type = type;
    }

    /**
     * Class constructor
     * @param type filter definition type
     * @param options list of filter definition items
     */
    public FilterDefinition(String type, List<FilterDefinitionItem> options) {
        this.type = type;
        this.options = options;
    }

    /**
     * getter method
     * @return a filter type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type a string with a filter type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return a list of options(filter definitions)
     */
    public List<FilterDefinitionItem> getOptions() {
        return options;
    }

    /**
     * setter method
     * @param options a list of options(filter definitions)
     */
    public void setOptions(List<FilterDefinitionItem> options) {
        this.options = options;
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
        FilterDefinition that = (FilterDefinition) o;
        return Objects.equals(type, that.type) && Objects.equals(options, that.options);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(type, options);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "FilterDefinition{" +
                "type='" + type + '\'' +
                ", options=" + options +
                '}';
    }
}
