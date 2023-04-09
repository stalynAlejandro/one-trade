package com.pagonxt.onetradefinance.external.backend.api.model;

/**
 * DTO Class for common data of the different events(CLE-...)
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto
 * @since jdk-11.0.13
 */
public class CommonRequestDto {

    //class attributes
    private String code;

    private String displayedStatus;

    private CustomerDto customer;

    private DocumentationDto documentation;

    /**
     * getter method
     * @return the request code
     */
    public String getCode() {
        return code;
    }

    /**
     * setter method
     * @param code a string with request code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * getter method
     * @return the displayed status of the request
     */
    public String getDisplayedStatus() {
        return displayedStatus;
    }

    /**
     * setter method
     * @param displayedStatus the displayed status of the request
     */
    public void setDisplayedStatus(String displayedStatus) {
        this.displayedStatus = displayedStatus;
    }

    /**
     * getter method
     * @return a CustomerDto object with the request customer
     */
    public CustomerDto getCustomer() {
        return customer;
    }

    /**
     * setter method
     * @param customer a CustomerDto object with the request customer
     */
    public void setCustomer(CustomerDto customer) {
        this.customer = customer;
    }

    /**
     * getter method
     * @return a DocumentationDto object with the request documents
     */
    public DocumentationDto getDocumentation() {
        return documentation;
    }

    /**
     * setter method
     * @param documentation a DocumentationDto object with the request documents
     */
    public void setDocumentation(DocumentationDto documentation) {
        this.documentation = documentation;
    }
}
