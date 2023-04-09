package com.pagonxt.onetradefinance.integrations.model;

import java.util.Objects;

/**
 * Model class for operation types
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class OperationType {

    private String product;

    private String id;

    private String name;

    private String key;

    /**
     * empty constructor method
     */
    public OperationType() {}

    /**
     * constructor method
     * @param product   : a string with the product ("CLE", "CLI",...)
     * @param id        : a string with the id
     * @param name      : a string with the name
     * @param key       : a string with the key
     */
    public OperationType(String product, String id, String name, String key) {
        this.product = product;
        this.id = id;
        this.name = name;
        this.key = key;
    }

    /**
     * getter method
     * @return a string with the product ("CLE", "CLI",...)
     */
    public String getProduct() {
        return product;
    }

    /**
     * setter method
     * @param product a string with the product ("CLE", "CLI",...)
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * getter method
     * @return a string with the id
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id a string with the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return a string with the name
     */
    public String getName() {
        return name;
    }

    /**
     * setter method
     * @param name a string with the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter method
     * @return a string with the key
     */
    public String getKey() {
        return key;
    }

    /**
     * setter method
     * @param key a string with the key
     */
    public void setKey(String key) {
        this.key = key;
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
        OperationType that = (OperationType) o;
        return Objects.equals(product, that.product) &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(key, that.key);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(product, id, name, key);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "OperationType{" +
                "product='" + product + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
