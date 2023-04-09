package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for files
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class FileDto {

    //class attributes
    private String id;

    private String name;

    private String uploadedDate;

    private String documentType;

    /**
     * The file contents, encoded in base64. The field is excluded from equals and hashCode, ad has a custom toString.
     */
    private String data;

    /**
     * getter method
     * @return the file id
     */
    public String getId() {
        return id;
    }

    /**
     * setter  method
     * @param id a string with the file id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return the file name
     */
    public String getName() {
        return name;
    }

    /**
     * setter  method
     * @param name a string with the file name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter method
     * @return the uploaded date of the file
     */
    public String getUploadedDate() {
        return uploadedDate;
    }

    /**
     * setter  method
     * @param uploadedDate a string with the uploaded date of the file
     */
    public void setUploadedDate(String uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    /**
     * getter method
     * @return the file type
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * setter  method
     * @param documentType a string with the file type
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * getter method
     * @return the file data
     */
    public String getData() {
        return data;
    }

    /**
     * setter  method
     * @param data a string with the file data
     */
    public void setData(String data) {
        this.data = data;
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
        FileDto fileDto = (FileDto) o;
        return Objects.equals(id, fileDto.id) &&
                Objects.equals(name, fileDto.name) &&
                Objects.equals(uploadedDate, fileDto.uploadedDate) &&
                Objects.equals(documentType, fileDto.documentType);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, uploadedDate, documentType);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "FileDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", uploadedDate=" + uploadedDate +
                ", documentType='" + documentType + '\'' +
                ", data? " + (data != null) +
                ", data.length=" + (data == null ? 0 : data.length()) +
                '}';
    }
}
