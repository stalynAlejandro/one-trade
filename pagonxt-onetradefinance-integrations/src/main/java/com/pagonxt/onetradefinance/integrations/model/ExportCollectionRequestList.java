package com.pagonxt.onetradefinance.integrations.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class that creates an object with an array of requests of export collections
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionRequest
 * @since jdk-11.0.13
 */
public class ExportCollectionRequestList extends ArrayList<ExportCollectionRequest> {

    /**
     * constructor method
     * @param exportCollectionRequestList a list of requests of export collections
     */
    public ExportCollectionRequestList(List<ExportCollectionRequest> exportCollectionRequestList) {
        super(exportCollectionRequestList);
    }
}
