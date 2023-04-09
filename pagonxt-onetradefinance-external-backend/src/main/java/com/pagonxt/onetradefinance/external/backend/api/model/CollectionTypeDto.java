package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for collection types
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CollectionTypeDto {

    //class attributes
    private String id;

    private String label;

    private String product;

    private String key;

    /**
     * getter method
     * @return the collection type id
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id a string with the collection type id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return the collection type label(or name)
     */
    public String getLabel() {
        return label;
    }

    /**
     * setter method
     * @param label a string with the collection type label(or name)
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * getter method
     * @return the collection type product
     */
    public String getProduct() {
        return product;
    }

    /**
     * setter method
     * @param product a string with the collection type product
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * getter method
     * @return the collection type key
     */
    public String getKey() {
        return key;
    }

    /**
     * setter method
     * @param key a string with the collection type key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Class constructor
     * @param id collection type id
     * @param label collection type label
     * @param product collection type product
     * @param key collection type key
     */
    public CollectionTypeDto(String id, String label, String product, String key) {
        this.id = id;
        this.label = label;
        this.product = product;
        this.key = key;
    }

    /**
     * empty class constructor
     */
    public CollectionTypeDto() {
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
