package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * Model class for customers
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Customer {

    @NotNull
    private String customerId;

    private String countryISO;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 50)
    private String taxId;

    @NotNull
    private String office;

    @NotNull
    @Size(max = 50)
    private String personNumber;

    @NotNull
    private String segment;

    //TODO se puede añadir la anotación @email para comprobar que el campo está correcto.
    // Ver si se podrá acceder al email del usuario.
    private String email;

    /**
     * empty constructor method
     */
    public Customer() {}

    /**
     * constructor method
     * @param customerId    :a string with the customer id
     * @param countryISO    :a string with the country code (alpha-2)
     * @param name          :a string with the customer name
     * @param taxId         :a string with the tax id of the customer
     * @param office        :a string with the customer office
     * @param personNumber  :a string with the person number of the customer
     * @param segment       :a string with the customer segment
     * @param email         :a string with the customer email
     */
    public Customer(String customerId,
                    String countryISO,
                    String name,
                    String taxId,
                    String office,
                    String personNumber,
                    String segment,
                    String email) {
        this.customerId = customerId;
        this.countryISO = countryISO;
        this.name = name;
        this.taxId = taxId;
        this.office = office;
        this.personNumber = personNumber;
        this.segment = segment;
        this.email = email;
    }

    /**
     * getter method
     * @return a string with the customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * setter method
     * @param customerId a string with the customer id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Getter method for field countryISO
     *
     * @return value of countryISO
     */
    public String getCountryISO() {
        return countryISO;
    }

    /**
     * Setter method for field countryISO
     *
     * @param countryISO : value of countryISO
     */
    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
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
     * @return a string with the customer segment
     */
    public String getSegment() {
        return segment;
    }

    /**
     * setter method
     * @param segment a string with the customer segment
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /**
     * getter method
     * @return a string with the customer email
     */
    public String getEmail() {
        return email;
    }

    /**
     * setter method
     * @param email a string with the customer email
     */
    public void setEmail(String email) {
        this.email = email;
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
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId) &&
                Objects.equals(countryISO, customer.countryISO) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(taxId, customer.taxId) &&
                Objects.equals(office, customer.office) &&
                Objects.equals(personNumber, customer.personNumber) &&
                Objects.equals(segment, customer.segment) &&
                Objects.equals(email, customer.email);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(customerId, countryISO, name, taxId, office, personNumber, segment, email);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", countryISO='" + countryISO + '\'' +
                ", name='" + name + '\'' +
                ", taxId='" + taxId + '\'' +
                ", office='" + office + '\'' +
                ", personNumber='" + personNumber + '\'' +
                ", segment='" + segment + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
