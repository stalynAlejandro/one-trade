package com.pagonxt.onetradefinance.work.service.model;

import java.util.Objects;

public class Office {

    private String country;
    private String officeId;
    private String address;
    private String place;
    private String email;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        Office office = (Office) o;
        return Objects.equals(country, office.country)
                && Objects.equals(officeId, office.officeId)
                && Objects.equals(address, office.address)
                && Objects.equals(place, office.place)
                && Objects.equals(email, office.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, officeId, address, place, email);
    }

    @Override
    public String toString() {
        return "Office{" +
                "country='" + country + '\'' +
                ", officeId='" + officeId + '\'' +
                ", address='" + address + '\'' +
                ", place='" + place + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
