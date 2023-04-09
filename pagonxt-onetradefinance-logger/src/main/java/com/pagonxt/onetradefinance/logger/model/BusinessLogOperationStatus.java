package com.pagonxt.onetradefinance.logger.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum BusinessLogOperationStatus {
    START("Start", "DRAFT"),
    IN_PROGRESS("In Progress", "IN_PROGRESS"),
    ON_HOLD("On hold", "AWAITING_INSTRUCTIONS"),
    END("End", "ISSUED");
    public final String observabilityLabel;
    public final String operationDisplayedStatus;
    BusinessLogOperationStatus(String observabilityLabel, String operationDisplayedStatus){
        this.observabilityLabel = observabilityLabel;
        this.operationDisplayedStatus = operationDisplayedStatus;
    }
    public static BusinessLogOperationStatus valueOfOperationDisplayedStatus(String operationDisplayedStatus) {
        for (BusinessLogOperationStatus businessLogOperationStatus : values()) {
            if (businessLogOperationStatus.operationDisplayedStatus.equals(operationDisplayedStatus)) {
                return businessLogOperationStatus;
            }
        }
        return null;
    }
}
