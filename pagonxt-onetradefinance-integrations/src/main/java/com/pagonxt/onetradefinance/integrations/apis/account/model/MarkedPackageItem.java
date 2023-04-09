package com.pagonxt.onetradefinance.integrations.apis.account.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Marked Package
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarkedPackageItem {

    @JsonProperty("packageType")
    private String packageType;
    @JsonProperty("packageId")
    private String packageId;

    /**
     * getter method
     * @return a string with the package type
     */
    public String getPackageType() {
        return packageType;
    }

    /**
     * setter method
     * @param packageType a string with the package type
     */
    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    /**
     * getter method
     * @return a string with the package id
     */
    public String getPackageId() {
        return packageId;
    }

    /**
     * setter method
     * @param packageId a string with the package id
     */
    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
}
