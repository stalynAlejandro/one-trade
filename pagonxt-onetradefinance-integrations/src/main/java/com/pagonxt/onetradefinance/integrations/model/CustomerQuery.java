package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * Model class for queries about customers
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CustomerQuery implements Serializable {

    private static final long serialVersionUID = -3309655774567779148L;

    @Size (max = 50)
    private String name;

    @Size (max = 50)
    private String taxId;

    @Size (max = 50)
    private String personNumber;

    @Size (max = 10)
    private String office;

    /**
     * constructor method
     * @param name          : a string with the customer name
     * @param taxId         : a string with the tax id of the customer
     * @param personNumber  : a string with the person number of the customer
     * @param office        : a string with the customer office
     */
    public CustomerQuery(String name, String taxId, String personNumber, String office) {
        this.name = name;
        this.taxId = taxId;
        this.personNumber = personNumber;
        this.office = office;
    }

    /**
     * getter method
     * @return a string with the customer name
     */
    public String getName() {
        return name;
    }

    /**
     * setter method
     * @param name a string with the customer name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter method
     * @return a string with the tax id of the customer
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * setter method
     * @param taxId a string with the tax id of the customer
     */
    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    /**
     * getter method
     * @return a string with the person number of the customer
     */
    public String getPersonNumber() {
        return personNumber;
    }

    /**
     * setter method
     * @param personNumber a string with the person number of the customer
     */
    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    /**
     * getter method
     * @return a string with the customer office
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the customer office
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ClientQuery{" +
                "name='" + name + '\'' +
                ", taxId='" + taxId + '\'' +
                ", personNumber='" + personNumber + '\'' +
                ", office=" + office +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerQuery that = (CustomerQuery) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(taxId, that.taxId) &&
                Objects.equals(personNumber, that.personNumber) &&
                Objects.equals(office, that.office);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, taxId, personNumber, office);
    }
}
