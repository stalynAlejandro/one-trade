package com.pagonxt.onetradefinance.integrations.model;

import java.util.Objects;

/**
 * Model class to create an object with info about offices
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class OfficeInfo {
    private String country;
    private String office;
    private String address;
    private String place;

    /**
     * getter method
     * @return a string with the office country
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter method
     * @param country a string with the office country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getter method
     * @return a string with the office
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the office
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return a string with the office address
     */
    public String getAddress() {
        return address;
    }

    /**
     * setter method
     * @param address a string with the office address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * getter method
     * @return a string with the office place
     */
    public String getPlace() {
        return place;
    }

    /**
     * setter method
     * @param place a string with the office place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            {return true;}
        if (o == null || getClass() != o.getClass())
            {return false;}
        OfficeInfo that = (OfficeInfo) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(office, that.office) &&
                Objects.equals(address, that.address) &&
                Objects.equals(place, that.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, office, address, place);
    }
}
