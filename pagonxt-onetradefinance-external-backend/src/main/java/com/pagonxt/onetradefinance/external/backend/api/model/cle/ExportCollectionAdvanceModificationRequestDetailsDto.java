package com.pagonxt.onetradefinance.external.backend.api.model.cle;


import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;

import java.util.Objects;

/**
 * DTO Class for export collection advance modification request details
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceModificationRequestDetailsDto {

    //Class constructor
    private ExportCollectionAdvanceDto exportCollectionAdvance;
    private RiskLineDto riskLine;
    private String office;
    private String comments;

    /**
     * getter method
     * @return ExportCollectionAdvanceDto object
     */
    public ExportCollectionAdvanceDto getExportCollectionAdvance() {
        return exportCollectionAdvance;
    }

    /**
     * setter method
     * @param exportCollectionAdvance ExportCollectionAdvanceDto object
     */
    public void setExportCollectionAdvance(ExportCollectionAdvanceDto exportCollectionAdvance) {
        this.exportCollectionAdvance = exportCollectionAdvance;
    }

    /**
     * getter method
     * @return a RiskLineDto object with the risk line
     */
    public RiskLineDto getRiskLine() {
        return riskLine;
    }

    /**
     * setter method
     * @param riskLine a RiskLineDto object with the risk line
     */
    public void setRiskLine(RiskLineDto riskLine) {
        this.riskLine = riskLine;
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
     * @param comments a string with request comments
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
        ExportCollectionAdvanceModificationRequestDetailsDto that =
                (ExportCollectionAdvanceModificationRequestDetailsDto) o;
        return Objects.equals(exportCollectionAdvance, that.exportCollectionAdvance) &&
                Objects.equals(riskLine, that.riskLine) &&
                Objects.equals(office, that.office) &&
                Objects.equals(comments, that.comments);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(exportCollectionAdvance, riskLine, office, comments);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionAdvanceModificationRequestDetailsDto{" +
                "exportCollectionAdvance=" + exportCollectionAdvance +
                ", riskLine=" + riskLine +
                ", office='" + office + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
