package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;

import java.util.Objects;

/**
 * DTO Class for export collection advance modification request
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceModificationRequestDetailsDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceModificationRequestDto extends CommonRequestDto {

    //class attribute
    private ExportCollectionAdvanceModificationRequestDetailsDto request;

    /**
     * getter method
     * @return a request with an ExportCollectionAdvanceModificationRequestDetailsDto object (request data)
     */
    public ExportCollectionAdvanceModificationRequestDetailsDto getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request an ExportCollectionAdvanceModificationRequestDetailsDto object (request data)
     */
    public void setRequest(ExportCollectionAdvanceModificationRequestDetailsDto request) {
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
        ExportCollectionAdvanceModificationRequestDto that = (ExportCollectionAdvanceModificationRequestDto) o;
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
        return "ExportCollectionAdvanceRequestDto{" +
                ", request=" + request +
                '}';
    }
}
