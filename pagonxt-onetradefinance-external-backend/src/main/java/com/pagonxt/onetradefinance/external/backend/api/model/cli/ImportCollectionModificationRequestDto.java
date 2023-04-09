package com.pagonxt.onetradefinance.external.backend.api.model.cli;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;

import java.util.Objects;

/**
 * DTO class for import collection modification request
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ImportCollectionDto
 * @see CommonRequestDto
 * @since jdk-11.0.13
 */
public class ImportCollectionModificationRequestDto extends CommonRequestDto {

    //Class attributes
    private ImportCollectionDto importCollection;

    private String office;

    private String comments;

    /**
     * getter method
     * @return a ImportCollectionDto object (import collection reference)
     */
    public ImportCollectionDto getImportCollection() {
        return importCollection;
    }


    /**
     * setter method
     * @param importCollection a ImportCollectionDto object (import collection reference)
     */
    public void setImportCollection(ImportCollectionDto importCollection) {
        this.importCollection = importCollection;
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
        ImportCollectionModificationRequestDto that = (ImportCollectionModificationRequestDto) o;
        return Objects.equals(importCollection, that.importCollection) &&
                Objects.equals(office, that.office) &&
                Objects.equals(comments, that.comments);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(importCollection, office, comments);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ImportCollectionModificationRequestDto{" +
                ", importCollection=" + importCollection +
                ", office='" + office + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
