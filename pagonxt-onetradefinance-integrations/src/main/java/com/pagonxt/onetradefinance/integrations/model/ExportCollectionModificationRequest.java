package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;

/**
 * Model class for modifications of export collections
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see Customer
 * @see ExportCollection
 * @see PagoNxtRequest
 * @since jdk-11.0.13
 */
public class ExportCollectionModificationRequest extends PagoNxtRequest {

    @NotNull
    private Customer customer;

    @NotNull
    private ExportCollection exportCollection;

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
}
