package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO class for customers
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class CustomerDto {

    //class attributes
    private String customerId;

    private String name;

    private String taxId;

    private String office;

    private String personNumber;

    private String segment;

    private String email;

    /**
     * getter method
     * @return the customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * setter method
     * @param customerId a string with customer id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * getter method
     * @return the customer name
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
     * @return the customer taxId
     */
    public String getTaxId() {
        return taxId;
    }

    /**
     * setter method
     * @param taxId a string with customer taxId
     */
    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    /**
     * getter method
     * @return the customer office
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
     * @return the customer number(internal code)
     */
    public String getPersonNumber() {
        return personNumber;
    }

    /**
     * setter method
     * @param personNumber a string with the customer number(internal code)
     */
    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    /**
     * getter method
     * @return the customer segment
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
     * @return the customer email
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
     * Class constructor
     * @param customerId customer id
     * @param name customer name
     * @param taxId customer tax id
     * @param office customer office
     * @param personNumber customer number
     * @param segment customer segment
     * @param email customer email
     */
    public CustomerDto(String customerId, String name, String taxId, String office,
                       String personNumber, String segment, String email) {
        this.customerId = customerId;
        this.name = name;
        this.taxId = taxId;
        this.office = office;
        this.personNumber = personNumber;
        this.segment = segment;
        this.email = email;
    }

    /**
     * Empty Class Constructor
     */
    public CustomerDto(){}

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
        CustomerDto clientDto = (CustomerDto) o;
        return Objects.equals(customerId, clientDto.customerId) &&
                Objects.equals(name, clientDto.name) &&
                Objects.equals(taxId, clientDto.taxId) &&
                Objects.equals(office, clientDto.office) &&
                Objects.equals(personNumber, clientDto.personNumber) &&
                Objects.equals(segment, clientDto.segment) &&
                Objects.equals(email, clientDto.email);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, taxId, office, personNumber, segment, email);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "CustomerDto{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", taxId='" + taxId + '\'' +
                ", office=" + office +
                ", personNumber='" + personNumber + '\'' +
                ", segment='" + segment + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
