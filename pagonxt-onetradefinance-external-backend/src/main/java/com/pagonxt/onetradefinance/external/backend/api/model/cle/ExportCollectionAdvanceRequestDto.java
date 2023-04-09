package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;

import java.util.Objects;

/**
 * DTO Class for export collection advance request
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceRequestDetailsDto
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceRequestDto extends CommonRequestDto {

    //Class Attributes
    private ExportCollectionAdvanceRequestDetailsDto request;

    private Integer savedStep;

    private String contractReference;

    /**
     * getter method
     * @return an ExportCollectionAdvanceRequestDetailsDto (with request data)
     */
    public ExportCollectionAdvanceRequestDetailsDto getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request an ExportCollectionAdvanceRequestDetailsDto (with request data)
     */
    public void setRequest(ExportCollectionAdvanceRequestDetailsDto request) {
        this.request = request;
    }

    /**
     * getter method
     * @return get the saved step from external app (The request is starting in external app)
     */
    public Integer getSavedStep() {
        return savedStep;
    }

    /**
     * setter method
     * @param savedStep a integer value with  the saved step from external app (The request is starting in external app)
     */
    public void setSavedStep(Integer savedStep) {
        this.savedStep = savedStep;
    }

    /**
     * getter method
     * @return the contract reference
     */
    public String getContractReference() {
        return contractReference;
    }

    /**
     * getter method
     * @param contractReference a string with the contract reference
     */
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
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
        ExportCollectionAdvanceRequestDto that = (ExportCollectionAdvanceRequestDto) o;
        return Objects.equals(request, that.request) &&
                Objects.equals(savedStep, that.savedStep) &&
                Objects.equals(contractReference, that.contractReference);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(request, savedStep, contractReference);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionAdvanceRequestDto{" +
                ", request=" + request +
                ", savedStep=" + savedStep +
                ", contractReference='" + contractReference +
                '}';
    }
}
