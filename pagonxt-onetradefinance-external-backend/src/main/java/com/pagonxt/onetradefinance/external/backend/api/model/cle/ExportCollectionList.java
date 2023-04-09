package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates an object with a list of export collections(DTO)
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionDto
 * @since jdk-11.0.13
 */
public class ExportCollectionList extends ArrayList<ExportCollectionDto> {

    /**
     * Class constructor
     * @param exportCollections list of export collections(DTO)
     */
    public ExportCollectionList(List<ExportCollectionDto> exportCollections) {
        super(exportCollections);
    }
}
