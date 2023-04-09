package com.pagonxt.onetradefinance.integrations.model.completeinfo;

import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;

/**
 * Model class for an advance request of export collection.
 * In this case when a complete information task is active
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequest
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
 * @since jdk-11.0.13
 */
public class CompleteInfoExportCollectionAdvanceRequest extends CompleteInfoPagoNxtRequest {

    private ExportCollectionAdvanceRequest request;

    /**
     * getter method
     * @return a request for an advance request of export collection.
     * This object includes all the data of the request.
     */
    public ExportCollectionAdvanceRequest getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request a request for an advance request of export collection.
     * This object includes all the data of the request.
     */
    public void setRequest(ExportCollectionAdvanceRequest request) {
        this.request = request;
    }


}
