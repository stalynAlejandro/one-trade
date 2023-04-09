package com.pagonxt.onetradefinance.integrations.model.completeinfo;

import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;

/**
 * Model class for other operations request of an export collection
 * In this case when a complete information task is active
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see CompleteInfoPagoNxtRequest
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
 * @since jdk-11.0.13
 * @since jdk-11.0.13
 * @since jdk-11.0
 * @since jdk-11.0
 */
public class CompleteInfoExportCollectionOtherOperationsRequest extends CompleteInfoPagoNxtRequest {

    private ExportCollectionOtherOperationsRequest request;

    /**
     * getter method
     * @return a request for other operations request of export collection.
     * This object includes all the data of the request.
     */
    public ExportCollectionOtherOperationsRequest getRequest() {return request;}

    /**
     * setter method
     * @param request a request for other operations request of export collection.
     * This object includes all the data of the request.
     */
    public void setRequest(ExportCollectionOtherOperationsRequest request) {this.request = request;}
}
