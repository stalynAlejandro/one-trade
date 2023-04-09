package com.pagonxt.onetradefinance.integrations.model.document;

import org.apache.logging.log4j.util.Strings;

import java.util.Date;
import java.util.Objects;

/**
 * Model class for documents
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Document {

    private String documentId;

    private String filename;

    private String mimeType;

    private Date uploadedDate;

    private String documentType;

    /**
     * Document data encoded in base64.
     * Data is omitted from equals and hashcode,
     * and has a custom toString representation.
     */
    private String data;

    /**
     * Empty method constructor
     */
    public Document() {
    }

    /**
     * method constructor
     * @param documentId    :a string with the document id
     * @param filename      :a string with the file name
     * @param mimeType      :a string with the mime type (document format)
     * @param uploadedDate  :a Date object with the uploaded date
     * @param documentType  :a string with the document type
     * @param data          :a string with the document data
     */
    public Document(String documentId,
                    String filename,
                    String mimeType,
                    Date uploadedDate,
                    String documentType,
                    String data) {
        this.documentId = documentId;
        this.filename = filename;
        this.mimeType = mimeType;
        this.uploadedDate = uploadedDate;
        this.documentType = documentType;
        this.data = data;
    }

    /**
     * getter method
     * @return a string with the document id
     */
    public String getDocumentId() {
        return documentId;
    }

    /**
     * setter method
     * @param documentId a string with the document id
     */
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    /**
     * getter method
     * @return a string with the file name
     */
    public String getFilename() {
        return filename;
    }

    /**
     * setter method
     * @param filename a string with the file name
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * getter method
     * @return a string with the mime type (document format)
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * setter method
     * @param mimeType a string with the mime type (document format)
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * getter method
     * @return a string with the document data
     */
    public String getData() {
        return data;
    }

    /**
     * setter method
     * @param data a string with the document data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * getter method
     * @return a Date object with the uploaded date
     */
    public Date getUploadedDate() {
        return uploadedDate;
    }

    /**
     * setter method
     * @param uploadedDate a Date object with the uploaded date
     */
    public void setUploadedDate(Date uploadedDate) {
        this.uploadedDate = uploadedDate;
    }

    /**
     * getter method
     * @return a string with the document type
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * setter method
     * @param documentType a string with the document type
     */
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
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
        Document document = (Document) o;
        return Objects.equals(documentId, document.documentId) &&
                Objects.equals(filename, document.filename) &&
                Objects.equals(mimeType, document.mimeType) &&
                Objects.equals(uploadedDate, document.uploadedDate) &&
                Objects.equals(documentType, document.documentType);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(documentId, filename, mimeType, uploadedDate, documentType);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Document{" +
                "id='" + documentId + '\'' +
                ", filename='" + filename + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", data? " + (Strings.isNotBlank(data)) +
                ", data.length=" + (data == null ? 0 : data.length()) +
                ", uploadedDate=" + uploadedDate +
                ", documentType='" + documentType + '\'' +
                '}';
    }
}
