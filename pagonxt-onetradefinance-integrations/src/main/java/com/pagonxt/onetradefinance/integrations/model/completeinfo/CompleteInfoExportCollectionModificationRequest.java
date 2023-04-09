package com.pagonxt.onetradefinance.integrations.model.completeinfo;

import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;

/**
 * Model class for a modification request of export collection
 * In this case when a complete information task is active
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequest
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest
 * @since jdk-11.0.13
 * @since jdk-11.0.13
 * @since jdk-11.0
 * @since jdk-11.0
 */
public class CompleteInfoExportCollectionModificationRequest extends CompleteInfoPagoNxtRequest {

    private ExportCollectionModificationRequest request;

    /**
     * getter method
     * @return a request for a modification request of export collection.
     * This object includes all the data of the request.
     */
    public ExportCollectionModificationRequest getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request a request for a modification request of export collection.
     * This object includes all the data of the request.
     */
    public void setRequest(ExportCollectionModificationRequest request) {
        this.request = request;
    }

}
