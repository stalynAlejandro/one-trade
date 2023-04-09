package com.pagonxt.onetradefinance.integrations.model.completeinfo;

import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;

/**
 * Model class for an advance cancellation request of export collection.
 * In this case when a complete information task is active
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequest
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
 * @since jdk-11.0.13
 */
public class CompleteInfoExportCollectionAdvanceCancellationRequest extends CompleteInfoPagoNxtRequest {

    private ExportCollectionAdvanceCancellationRequest request;

    /**
     * getter method
     * @return a request for an advance cancellation request of export collection.
     * This object includes all the data of the request.
     */
    public ExportCollectionAdvanceCancellationRequest getRequest() {
        return request;
    }

    /**
     * setter method
     * @param request a request for an advance cancellation request of export collection.
     * This object includes all the data of the request.
     */
    public void setRequest(ExportCollectionAdvanceCancellationRequest request) {
        this.request = request;
    }

}
