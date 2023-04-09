package com.pagonxt.onetradefinance.integrations.model;

import java.util.Objects;

/**
 * Model class for queries about export collections
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see User
 * @since jdk-11.0.13
 */
public class ExportCollectionQuery {

    private User requester;

    private String customerPersonNumber;

    private String collectionType;

    /**
     * getter method
     * @return a User object with the requester
     */
    public User getRequester() {
        return requester;
    }

    /**
     * setter method
     * @param requester a User object with the requester
     */
    public void setRequester(User requester) {
        this.requester = requester;
    }

    /**
     * getter method
     * @return a string with the person number of the customer
     */
    public String getCustomerPersonNumber() {
        return customerPersonNumber;
    }

    /**
     * setter method
     * @param customerPersonNumber a string with the person number of the customer
     */
    public void setCustomerPersonNumber(String customerPersonNumber) {
        this.customerPersonNumber = customerPersonNumber;
    }

    /**
     * getter method
     * @return a string with the collection type
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * setter method
     * @param collectionType a string with the collection type
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
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
        ExportCollectionQuery that = (ExportCollectionQuery) o;
        return Objects.equals(requester, that.requester) &&
                Objects.equals(customerPersonNumber, that.customerPersonNumber) &&
                Objects.equals(collectionType, that.collectionType);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(requester, customerPersonNumber, collectionType);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionQuery{" +
                "requester=" + requester +
                ", customerPersonNumber='" + customerPersonNumber + '\'' +
                ", collectionType='" + collectionType + '\'' +
                '}';
    }
}
