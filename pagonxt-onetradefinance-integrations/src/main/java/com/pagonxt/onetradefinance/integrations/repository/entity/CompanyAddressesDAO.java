package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.*;

/**
 * Entity Class for company addresses.
 * Includes some attributes and getters and setters.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Entity
@Table(name = "aci_01cip_company_addresses")
public class CompanyAddressesDAO {
    @EmbeddedId
    private CompanyAddressesId companyAddressesId;

    @Column(name = "department")
    private String department;

    @Column(name = "subdepartment")
    private String subdepartment;

    @Column(name = "street")
    private String street;

    @Column(name = "street_building")
    private String streetBuilding;

    @Column(name = "building")
    private String building;

    @Column(name = "floor")
    private String floor;

    @Column(name = "district")
    private String district;

    @Column(name = "post_code")
    private String postCode;

    @Column(name = "post_office_box")
    private String postOfficeBox;

    @Column(name = "location")
    private String location;

    @Column(name = "town")
    private String town;

    @Column(name = "province")
    private String province;

    @Column(name = "county")
    private String county;

    @Column(name = "region")
    private String region;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    public CompanyAddressesDAO() {
        // Empty constructor
    }

    /**
     * Getter method for field companyAddressesId
     *
     * @return value of companyAddressesId
     */
    public CompanyAddressesId getCompanyAddressesId() {
        return companyAddressesId;
    }

    /**
     * Setter method for field companyAddressesId
     *
     * @param companyAddressesId : value of companyAddressesId
     */
    public void setCompanyAddressesId(CompanyAddressesId companyAddressesId) {
        this.companyAddressesId = companyAddressesId;
    }

    /**
     * Getter method for field department
     *
     * @return value of department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Setter method for field department
     *
     * @param department : value of department
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Getter method for field subdepartment
     *
     * @return value of subdepartment
     */
    public String getSubdepartment() {
        return subdepartment;
    }

    /**
     * Setter method for field subdepartment
     *
     * @param subdepartment : value of subdepartment
     */
    public void setSubdepartment(String subdepartment) {
        this.subdepartment = subdepartment;
    }

    /**
     * Getter method for field street
     *
     * @return value of street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Setter method for field street
     *
     * @param street : value of street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Getter method for field streetBuilding
     *
     * @return value of streetBuilding
     */
    public String getStreetBuilding() {
        return streetBuilding;
    }

    /**
     * Setter method for field streetBuilding
     *
     * @param streetBuilding : value of streetBuilding
     */
    public void setStreetBuilding(String streetBuilding) {
        this.streetBuilding = streetBuilding;
    }

    /**
     * Getter method for field building
     *
     * @return value of building
     */
    public String getBuilding() {
        return building;
    }

    /**
     * Setter method for field building
     *
     * @param building : value of building
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * Getter method for field floor
     *
     * @return value of floor
     */
    public String getFloor() {
        return floor;
    }

    /**
     * Setter method for field floor
     *
     * @param floor : value of floor
     */
    public void setFloor(String floor) {
        this.floor = floor;
    }

    /**
     * Getter method for field district
     *
     * @return value of district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * Setter method for field district
     *
     * @param district : value of district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * Getter method for field postCode
     *
     * @return value of postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * Setter method for field postCode
     *
     * @param postCode : value of postCode
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * Getter method for field postOfficeBox
     *
     * @return value of postOfficeBox
     */
    public String getPostOfficeBox() {
        return postOfficeBox;
    }

    /**
     * Setter method for field postOfficeBox
     *
     * @param postOfficeBox : value of postOfficeBox
     */
    public void setPostOfficeBox(String postOfficeBox) {
        this.postOfficeBox = postOfficeBox;
    }

    /**
     * Getter method for field location
     *
     * @return value of location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter method for field location
     *
     * @param location : value of location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter method for field town
     *
     * @return value of town
     */
    public String getTown() {
        return town;
    }

    /**
     * Setter method for field town
     *
     * @param town : value of town
     */
    public void setTown(String town) {
        this.town = town;
    }

    /**
     * Getter method for field province
     *
     * @return value of province
     */
    public String getProvince() {
        return province;
    }

    /**
     * Setter method for field province
     *
     * @param province : value of province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * Getter method for field county
     *
     * @return value of county
     */
    public String getCounty() {
        return county;
    }

    /**
     * Setter method for field county
     *
     * @param county : value of county
     */
    public void setCounty(String county) {
        this.county = county;
    }

    /**
     * Getter method for field region
     *
     * @return value of region
     */
    public String getRegion() {
        return region;
    }

    /**
     * Setter method for field region
     *
     * @param region : value of region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Getter method for field state
     *
     * @return value of state
     */
    public String getState() {
        return state;
    }

    /**
     * Setter method for field state
     *
     * @param state : value of state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter method for field country
     *
     * @return value of country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter method for field country
     *
     * @param country : value of country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
