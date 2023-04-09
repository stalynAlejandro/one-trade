package com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.CompleteInfoPagoNxtRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionModificationRequestDto;

import java.util.Objects;

/**
 * DTO Class for Export Collection Modification Request(CLE-C002), when we have a complete info task in Flowable Work
 * A complete info task requires some data of the external application (see user pending tasks)
 * extends of complete info Pagonxt request to obtain data like return reason, comments...
 * Includes an attribute, getter and setter, equals method, hashCode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequestDto
 * @see ExportCollectionModificationRequestDto
 * @since jdk-11.0.13
 */
public class CompleteInfoExportCollectionModificationRequestDto extends CompleteInfoPagoNxtRequestDto {

    /**
     * class attributes
     */
    private ExportCollectionModificationRequestDto request;

    /**
     * Getter Method
     * @return an ExportCollectionModificationRequestDto object
     */
    public ExportCollectionModificationRequestDto getRequest() {
        return request;
    }

    /**
     * Setter Method
     * @param request ExportCollectionModificationRequestDto object
     */
    public void setRequest(ExportCollectionModificationRequestDto request) {
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
        if (!super.equals(o)) {
            return false;
        }
        CompleteInfoExportCollectionModificationRequestDto that =
                (CompleteInfoExportCollectionModificationRequestDto) o;
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
        return "CompleteInfoExportCollectionModificationRequestDto{" +
                "request=" + request +
                "} " + super.toString();
    }
}
