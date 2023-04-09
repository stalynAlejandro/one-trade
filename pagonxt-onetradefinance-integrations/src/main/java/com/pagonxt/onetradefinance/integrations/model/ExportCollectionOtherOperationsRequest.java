package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Model class for other operations of export collections
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see Customer
 * @see ExportCollection
 * @see PagoNxtRequest
 * @since jdk-11.0.13
 */
public class ExportCollectionOtherOperationsRequest extends PagoNxtRequest {

    @NotNull
    private Customer customer;

    @NotNull
    private ExportCollection exportCollection;

    @NotNull
    private String operationType;

    public ExportCollectionOtherOperationsRequest() {}

    /**
     * constructor method
     * @param customer          : a Customer object with the customer
     * @param exportCollection  : an ExportCollection object with the data of request
     * @param operationType     : a string with the operation type
     */
    public ExportCollectionOtherOperationsRequest(Customer customer,
                                                  ExportCollection exportCollection,
                                                  String operationType) {
        this.customer = customer;
        this.exportCollection = exportCollection;
        this.operationType = operationType;
    }

    /**
     * getter method
     * @return a Customer object with the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * setter method
     * @param customer a Customer object with the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * getter method
     * @return an ExportCollection object with the data of request
     */
    public ExportCollection getExportCollection() {
        return exportCollection;
    }

    /**
     * setter method
     * @param exportCollection an ExportCollection object with the data of request
     */
    public void setExportCollection(ExportCollection exportCollection) {
        this.exportCollection = exportCollection;
    }

    /**
     * getter method
     * @return a string with the operation type
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * setter method
     * @param operationType a string with the operation type
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
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
        ExportCollectionOtherOperationsRequest that = (ExportCollectionOtherOperationsRequest) o;
        return Objects.equals(customer, that.customer) &&
                Objects.equals(exportCollection, that.exportCollection) &&
                Objects.equals(operationType, that.operationType);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(customer, exportCollection, operationType);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionOtherOperationsRequest{" +
                "customer=" + customer +
                ", exportCollection=" + exportCollection +
                ", operationType=" + operationType +
                '}';
    }
}
