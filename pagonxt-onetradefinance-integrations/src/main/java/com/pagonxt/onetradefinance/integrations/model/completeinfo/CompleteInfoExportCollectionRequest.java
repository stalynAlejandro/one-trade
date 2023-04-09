package com.pagonxt.onetradefinance.integrations.model.completeinfo;

import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;

/**
 * Model class for a request of export collection
 * In this case when a complete information task is active
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequest
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
 * @since jdk-11.0.13
 * @since jdk-11.0.13
 * @since jdk-11.0
 * @since jdk-11.0
 */
public class CompleteInfoExportCollectionRequest extends CompleteInfoPagoNxtRequest {

    private ExportCollectionRequest request;

    /**
     * getter method
     * @return a request for a request of export collection.
     * This object includes all the data of the request.
     */
    public ExportCollectionRequest getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request a request for a request of export collection.
     * This object includes all the data of the request.
     */
    public void setRequest(ExportCollectionRequest request) {
        this.request = request;
    }
}
