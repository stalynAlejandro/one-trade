package com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.CompleteInfoPagoNxtRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceRequestDto;

import java.util.Objects;

/**
 * DTO Class for Export Collection Advance Request(CLE-C003), when we have a complete info task in Flowable Work
 * A complete info task requires some data of the external application (see user pending tasks)
 * extends of complete info Pagonxt request to obtain data like return reason, comments...
 * Includes an attribute, getter and setter, equals method, hashCode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequestDto
 * @see ExportCollectionAdvanceRequestDto
 * @since jdk-11.0.13
 */
public class CompleteInfoExportCollectionAdvanceRequestDto extends CompleteInfoPagoNxtRequestDto {

    /**
     * class attributes
     */
    private ExportCollectionAdvanceRequestDto request;

    /**
     * Getter Method
     * @return an ExportCollectionAdvanceRequestDto object
     */
    public ExportCollectionAdvanceRequestDto getRequest() {
        return request;
    }

    /**
     * Setter Method
     * @param request ExportCollectionAdvanceRequestDto object
     */
    public void setRequest(ExportCollectionAdvanceRequestDto request) {
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
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)){
            return false;
        }
        CompleteInfoExportCollectionAdvanceRequestDto that = (CompleteInfoExportCollectionAdvanceRequestDto) o;
        return Objects.equals(request, that.request);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), request);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "CompleteInfoExportCollectionAdvanceRequestDto{" +
                "request=" + request +
                "} " + super.toString();
    }
}
