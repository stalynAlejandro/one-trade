package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;

import java.util.Objects;

/**
 * DTO class for export collection modification request
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto
 * @since jdk-11.0.13
 */
public class ExportCollectionModificationRequestDto extends CommonRequestDto {

    //Class attributes
    private ExportCollectionDto exportCollection;

    private String office;

    private String comments;

    /**
     * getter method
     * @return a ExportCollectionDto object (export collection reference)
     */
    public ExportCollectionDto getExportCollection() {
        return exportCollection;
    }

    /**
     * setter method
     * @param exportCollection a ExportCollectionDto object (export collection reference)
     */
    public void setExportCollection(ExportCollectionDto exportCollection) {
        this.exportCollection = exportCollection;
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
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return the request comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * setter method
     * @param comments a string with the request comments
     */
    public void setComments(String comments) {
        this.comments = comments;
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
        ExportCollectionModificationRequestDto that = (ExportCollectionModificationRequestDto) o;
        return Objects.equals(exportCollection, that.exportCollection) &&
                Objects.equals(office, that.office) &&
                Objects.equals(comments, that.comments);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(exportCollection, office, comments);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionModificationRequestDto{" +
                ", exportCollection=" + exportCollection +
                ", office='" + office + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
