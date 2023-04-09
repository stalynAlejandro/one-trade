package com.pagonxt.onetradefinance.integrations.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * model class for validation errors
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see java.io.Serializable
 * @since jdk-11.0.13
 */
public class ValidationError implements Serializable {

    private String parentFieldName;
    private String fieldName;
    private String violation;
    private String limit;

    /**
     * constructor method
     * @param parentFieldName   : a string with the parent field name
     * @param fieldName         : a string with the field name
     * @param violation         : a string with the error message
     * @param limit             : a string with the limit
     */
    public ValidationError(String parentFieldName, String fieldName, String violation, String limit) {
        this.parentFieldName = parentFieldName;
        this.fieldName = fieldName;
        this.violation = violation;
        this.limit = limit;
    }

    /**
     * constructor method
     * @param fieldName         : a string with the field name
     * @param violation         : a string with the error message
     * @param limit             : a string with the limit
     */
    public ValidationError(String fieldName, String violation, String limit) {
        this.fieldName = fieldName;
        this.violation = violation;
        this.limit = limit;
    }

    /**
     * getter method
     * @return a string with the parent field name
     */
    public String getParentFieldName() {
        return parentFieldName;
    }

    /**
     * setter method
     * @param parentFieldName a string with the parent field name
     */
    public void setParentFieldName(String parentFieldName) {
        this.parentFieldName = parentFieldName;
    }

    /**
     * getter method
     * @return a string with the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * setter method
     * @param fieldName a string with the field name
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * getter method
     * @return a string with the error message
     */
    public String getViolation() {
        return violation;
    }

    /**
     * setter method
     * @param errorCause a string with the error message
     */
    public void setViolation(String errorCause) {
        this.violation = errorCause;
    }

    /**
     * getter method
     * @return a string with the limit
     */
    public String getLimit() {
        return limit;
    }

    /**
     * setter method
     * @param limit a string with the limit
     */
    public void setLimit(String limit) {
        this.limit = limit;
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
        ValidationError that = (ValidationError) o;
        return Objects.equals(parentFieldName, that.parentFieldName) &&
                Objects.equals(fieldName, that.fieldName) &&
                Objects.equals(violation, that.violation) &&
                Objects.equals(limit, that.limit);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(parentFieldName, fieldName, violation, limit);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ValidationError{" +
                "parentFieldName='" + parentFieldName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", violation='" + violation + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }
}
