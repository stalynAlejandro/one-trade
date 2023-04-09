package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;

import java.util.Objects;

/**
 * DTO Class for export collection other operations request
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto
 * @see ExportCollectionOtherOperationsRequestDetailsDto
 * @since jdk-11.0.13
 */
public class ExportCollectionOtherOperationsRequestDto extends CommonRequestDto {

    //Class attributes
    private ExportCollectionOtherOperationsRequestDetailsDto request;

    /**
     * getter method
     * @return a ExportCollectionOtherOperationsRequestDetailsDto object(request details)
     */
    public ExportCollectionOtherOperationsRequestDetailsDto getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request a ExportCollectionOtherOperationsRequestDetailsDto object(request details)
     */
    public void setRequest(ExportCollectionOtherOperationsRequestDetailsDto request) {
        this.request = request;
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
        ExportCollectionOtherOperationsRequestDto that = (ExportCollectionOtherOperationsRequestDto) o;
        return Objects.equals(request, that.request);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(request);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionOtherOperationsRequestDto{" + "request=" + request + "} " + super.toString();
    }
}
