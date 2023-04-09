package com.pagonxt.onetradefinance.external.backend.api.model.cle.completeinfo;

import com.pagonxt.onetradefinance.external.backend.api.model.CompleteInfoPagoNxtRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceCancellationRequestDto;

import java.util.Objects;

/**
 * DTO Class for Export Collection Advance Cancellation Request(CLE-C005),
 * when we have a complete info task in Flowable Work
 * A complete info task requires some data of the external application (see user pending tasks)
 * extends of complete info Pagonxt request to obtain data like return reason, comments...
 * Includes:attribute,getter and setter,equals method,hashCode method,toString method
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequestDto
 * @see ExportCollectionAdvanceCancellationRequestDto
 * @since jdk-11.0.13
 */
public class CompleteInfoExportCollectionAdvanceCancellationRequestDto extends CompleteInfoPagoNxtRequestDto {

    /**
     * class attributes
     */
    private ExportCollectionAdvanceCancellationRequestDto request;

    /**
     * Getter Method
     * @return an ExportCollectionAdvanceCancellationRequestDto object
     */
    public ExportCollectionAdvanceCancellationRequestDto getRequest() {
        return request;
    }

    /**
     * Setter Method
     * @param request ExportCollectionAdvanceCancellationRequestDto object
     */
    public void setRequest(ExportCollectionAdvanceCancellationRequestDto request) {
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
        CompleteInfoExportCollectionAdvanceCancellationRequestDto that =
                (CompleteInfoExportCollectionAdvanceCancellationRequestDto) o;
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
