package com.pagonxt.onetradefinance.logger.model;

import java.util.Map;

/**
 *
 */

public class BusinessLogDto {

    private String uniqueGTSPayment;
    private String product;
    private String subproduct;
    private String event;
    private String stepName;
    private String operationStatus;
    private String caseCode;
    private String caseId;
    private Map<String, Object> details;

    public String getUniqueGTSPayment() {
        return uniqueGTSPayment;
    }

    public void setUniqueGTSPayment(String uniqueGTSPayment) {
        this.uniqueGTSPayment = uniqueGTSPayment;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSubproduct() {
        return subproduct;
    }

    public void setSubproduct(String subproduct) {
        this.subproduct = subproduct;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    @Override
    public String toString() {
        return "BusinessLogDto{" +
                "uniqueGTSPayment='" + uniqueGTSPayment + '\'' +
                ", product='" + product + '\'' +
                ", subproduct='" + subproduct + '\'' +
                ", event='" + event + '\'' +
                ", stepName='" + stepName + '\'' +
                ", operationStatus=" + operationStatus +
                ", caseCode='" + caseCode + '\'' +
                ", caseId='" + caseId + '\'' +
                ", details=" + details +
                '}';
    }
}
