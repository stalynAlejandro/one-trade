package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.AdvanceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.OperationDetailsDto;

import java.util.Objects;

/**
 * DTO Class for export collection request
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.AdvanceDto
 * @see OperationDetailsDto
 * @since jdk-11.0.13
 */
public class ExportCollectionRequestDto extends CommonRequestDto {

    //Class attributes
    private String contractReference;

    private String slaEnd;

    private Integer savedStep;

    private AdvanceDto advance;

    private OperationDetailsDto operationDetails;

    /**
     * getter method
     * @return The contract reference
     */
    public String getContractReference() {
        return contractReference;
    }

    /**
     * setter method
     * @param contractReference a string with the contract reference
     */
    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    /**
     * getter method
     * @return the SLA end data
     */
    public String getSlaEnd() {
        return slaEnd;
    }

    /**
     * setter method
     * @param slaEnd a string with the SLA end data
     */
    public void setSlaEnd(String slaEnd) {
        this.slaEnd = slaEnd;
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
     * @return an AdvanceDto object with the advance of the request
     */
    public AdvanceDto getAdvance() {
        return advance;
    }

    /**
     * setter method
     * @param advance an AdvanceDto object with the advance of the request
     */
    public void setAdvance(AdvanceDto advance) {
        this.advance = advance;
    }

    /**
     * getter method
     * @return an OperationDetailsDto object with the details of the request
     */
    public OperationDetailsDto getOperationDetails() {
        return operationDetails;
    }

    /**
     * setter method
     * @param operationDetails an OperationDetailsDto object with the details of the request
     */
    public void setOperationDetails(OperationDetailsDto operationDetails) {
        this.operationDetails = operationDetails;
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
        ExportCollectionRequestDto that = (ExportCollectionRequestDto) o;
        return Objects.equals(contractReference, that.contractReference) &&
                Objects.equals(slaEnd, that.slaEnd) &&
                Objects.equals(savedStep, that.savedStep) &&
                Objects.equals(advance, that.advance) &&
                Objects.equals(operationDetails, that.operationDetails);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(contractReference, slaEnd, savedStep, advance, operationDetails);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionRequestDto{" +
                ", contractReference='" + contractReference + '\'' +
                ", slaEnd='" + slaEnd + '\'' +
                ", savedStep=" + savedStep +
                ", advance=" + advance +
                ", operationDetails=" + operationDetails +
                '}';
    }
}
