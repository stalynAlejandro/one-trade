package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Model class for advance modifications of export collections
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see Customer
 * @see ExportCollectionAdvance
 * @see RiskLine
 * @see PagoNxtRequest
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceModificationRequest extends PagoNxtRequest {

    @NotNull
    private Customer customer;
    @NotNull
    private ExportCollectionAdvance exportCollectionAdvance;
    @NotNull
    private RiskLine riskLine;

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
     * @return an ExportCollectionAdvance object with the data of request
     */
    public ExportCollectionAdvance getExportCollectionAdvance() {
        return exportCollectionAdvance;
    }

    /**
     * setter method
     * @param exportCollectionAdvance an ExportCollectionAdvance object with the data of request
     */
    public void setExportCollectionAdvance(ExportCollectionAdvance exportCollectionAdvance) {
        this.exportCollectionAdvance = exportCollectionAdvance;
    }

    /**
     * getter method
     * @return a RiskLine object with the risk lines
     */
    public RiskLine getRiskLine() {
        return riskLine;
    }

    /**
     * setter method
     * @param riskLine a RiskLine object with the risk lines
     */
    public void setRiskLine(RiskLine riskLine) {
        this.riskLine = riskLine;
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
        if (!super.equals(o)) {
            return false;
        }
        ExportCollectionAdvanceModificationRequest that = (ExportCollectionAdvanceModificationRequest) o;
        return Objects.equals(customer, that.customer) &&
                Objects.equals(exportCollectionAdvance, that.exportCollectionAdvance) &&
                Objects.equals(riskLine, that.riskLine);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customer, exportCollectionAdvance, riskLine);
    }
}
