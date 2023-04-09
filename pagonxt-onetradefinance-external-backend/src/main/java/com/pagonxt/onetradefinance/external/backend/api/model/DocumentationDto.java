package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO Class for documentation
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.FileDto
 * @since jdk-11.0.13
 */
public class DocumentationDto {

    //Class attributes
    private List<FileDto> files = new ArrayList<>();

    private String priority;

    private boolean clientAcceptance;

    /**
     * getter method
     * @return a FileDto object with a list of files
     */
    public List<FileDto> getFiles() {
        return files;
    }

    /**
     * setter method
     * @param files a FileDto object with a list of files
     */
    public void setFiles(List<FileDto> files) {
        this.files = files;
    }

    /**
     * getter method
     * @return the request priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * setter method
     * @param priority a string with the request priority
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * getter method
     * @return true or false, if client has accepted the prices chart
     */
    public boolean isClientAcceptance() {
        return clientAcceptance;
    }

    /**
     * setter method
     * @param clientAcceptance true or false, if client has accepted the prices chart
     */
    public void setClientAcceptance(boolean clientAcceptance) {
        this.clientAcceptance = clientAcceptance;
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
        DocumentationDto that = (DocumentationDto) o;
        return clientAcceptance == that.clientAcceptance &&
                Objects.equals(files, that.files) &&
                Objects.equals(priority, that.priority);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(files, priority, clientAcceptance);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "DocumentationDto{" +
                "files=" + files +
                ", priority='" + priority + '\'' +
                ", clientAcceptance=" + clientAcceptance +
                '}';
    }
}
