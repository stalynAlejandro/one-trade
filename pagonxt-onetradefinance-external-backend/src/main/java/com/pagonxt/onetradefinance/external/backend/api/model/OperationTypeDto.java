package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for operation types
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class OperationTypeDto {

    //class attributes
    private String id;

    private String label;

    private String product;

    private String key;

    /**
     * getter method
     * @return the id of the operation type
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id a string with the id of the operation type
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return the label of the operation type(name)
     */
    public String getLabel() {
        return label;
    }

    /**
     * setter method
     * @param label a string with the label of the operation type(name)
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * getter method
     * @return the product of the operation type("CLE",...)
     */
    public String getProduct() {
        return product;
    }

    /**
     * setter method
     * @param product a string with the product of the operation type("CLE",...)
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * getter method
     * @return the key of the operation type
     */
    public String getKey() {
        return key;
    }

    /**
     * setter method
     * @param key a string with the key of the operation type
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Class constructor
     * @param id operation type id ("1",...)
     * @param label operation type label
     * @param product operation type product("CLE",...)
     * @param key operation type key
     */
    public OperationTypeDto(String id, String label, String product, String key) {
        this.id = id;
        this.label = label;
        this.product = product;
        this.key = key;
    }

    /**
     * Empty Class constructor
     */
    public OperationTypeDto() {
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
        if (!(o instanceof CollectionTypeDto)) {
            return false;
        }
        CollectionTypeDto that = (CollectionTypeDto) o;
        return Objects.equals(getId(), that.getId())
                && Objects.equals(getLabel(), that.getLabel())
                && Objects.equals(getProduct(), that.getProduct())
                && Objects.equals(getKey(), that.getKey());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLabel(), getProduct(), getKey());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "CollectionTypeDto{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", product='" + product + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
