package com.pagonxt.onetradefinance.integrations.model.completeinfo;

import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;

/**
 * Model class for an advance modification request of export collection.
 * In this case when a complete information task is active
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequest
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest
 * @since jdk-11.0.13
 */
public class CompleteInfoExportCollectionAdvanceModificationRequest extends CompleteInfoPagoNxtRequest {

    private ExportCollectionAdvanceModificationRequest request;

    /**
     * getter method
     * @return a request for an advance modification request of export collection.
     * This object includes all the data of the request.
     */
    public ExportCollectionAdvanceModificationRequest getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request a request for an advance modification request of export collection.
     * This object includes all the data of the request.
     */
    public void setRequest(ExportCollectionAdvanceModificationRequest request) {
        this.request = request;
    }

}
