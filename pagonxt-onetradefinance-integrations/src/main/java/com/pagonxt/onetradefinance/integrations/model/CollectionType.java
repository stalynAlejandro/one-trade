package com.pagonxt.onetradefinance.integrations.model;

import java.util.List;
import java.util.Objects;

/**
 * Model class for collection types
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CollectionType {

    private String product;

    private String id;

    private String name;

    private String key;

    private List<String> currency;

    /**
     * empty constructor method
     */
    public CollectionType() {}

    /**
     * constructor method
     * @param product   : a string with the product ("CLE","CLI",...)
     * @param id        : a string with the id of the collection type
     * @param name      : a string with the name of the collection type
     * @param key       : a string with the key of the collection type
     * @param currency  : a list with currencies
     */
    public CollectionType(String product, String id, String name, String key, List<String> currency){
        this.product = product;
        this.id = id;
        this.name = name;
        this.key = key;
        this.currency = currency;
    }

    /**
     * getter method
     * @return a string with the product ("CLE","CLI",...)
     */
    public String getProduct() {
        return product;
    }

    /**
     * setter method
     * @param product a string with the product ("CLE","CLI",...)
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * getter method
     * @return a string with the id of the collection type
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id a string with the id of the collection type
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return a string with the name of the collection type
     */
    public String getName() {
        return name;
    }

    /**
     * setter method
     * @param name a string with the name of the collection type
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter method
     * @return a string with the key of the collection type
     */
    public String getKey() {
        return key;
    }

    /**
     * setter method
     * @param key a string with the key of the collection type
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * getter method
     * @return a list with currencies
     */
    public List<String> getCurrency() {
        return currency;
    }

    /**
     * setter method
     * @param currency a list with currencies
     */
    public void setCurrency(List<String> currency) {
        this.currency = currency;
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
        if (!(o instanceof CollectionType)) {
            return false;
        }
        CollectionType that = (CollectionType) o;
        return Objects.equals(getProduct(), that.getProduct()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getKey(), that.getKey()) &&
                Objects.equals(getCurrency(), that.getCurrency());
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getId(), getName(), getKey());
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "CollectionType{" +
                "product='" + product + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", key=" + key +
                ", currency=" + currency +
                '}';
    }
}
