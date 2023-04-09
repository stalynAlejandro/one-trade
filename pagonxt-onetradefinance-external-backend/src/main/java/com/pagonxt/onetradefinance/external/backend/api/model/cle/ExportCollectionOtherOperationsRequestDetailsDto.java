package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import java.util.Objects;

/**
 * DTO Class for export collection other operation request details
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionDto
 * @since jdk-11.0.13
 */
public class ExportCollectionOtherOperationsRequestDetailsDto {

    //Class attributes
    private ExportCollectionDto exportCollection;
    private String operationType;
    private String office;
    private String comments;

    /**
     * getter method
     * @return a ExportCollectionDto object(export collection referenced)
     */
    public ExportCollectionDto getExportCollection() {
        return exportCollection;
    }

    /**
     * setter method
     * @param exportCollection  a ExportCollectionDto object(export collection referenced)
     */
    public void setExportCollection(ExportCollectionDto exportCollection) {
        this.exportCollection = exportCollection;
    }

    /**
     * getter method
     * @return the operation type of the request
     */
    public String getOperationType() {
        return operationType;
    }

    /**
     * setter method
     * @param operationType a string with the operation type of the request
     */
    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    /**
     * getter method
     * @return the request office
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the request office
     */
    public void setOffice(String office) {this.office = office;}

    /**
     * getter method
     * @return the request comments
     */
    public String getComments() {return comments;}

    /**
     * setter method
     * @param comments a string with request comments
     */
    public void setComments(String comments) {this.comments = comments;}

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
        ExportCollectionOtherOperationsRequestDetailsDto that = (ExportCollectionOtherOperationsRequestDetailsDto) o;
        return Objects.equals(exportCollection, that.exportCollection) &&
                Objects.equals(operationType, that.operationType) &&
                Objects.equals(office, that.office) &&
                Objects.equals(comments, that.comments);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(exportCollection, operationType, office, comments);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionOtherOperationsRequestDetailsDto{" +
                "exportCollection=" + exportCollection +
                ", operationType=" + operationType +
                ", office='" + office + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
