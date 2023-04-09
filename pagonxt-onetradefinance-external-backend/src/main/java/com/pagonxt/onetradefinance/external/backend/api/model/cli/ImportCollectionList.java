package com.pagonxt.onetradefinance.external.backend.api.model.cli;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates an object with a list of export collections(DTO)
 * @author -
 * @version jdk-11.0.13
 * @see ImportCollectionDto
 * @since jdk-11.0.13
 */
public class ImportCollectionList extends ArrayList<ImportCollectionDto> {

    /**
     * Class constructor
     * @param importCollections list of import collections(DTO)
     */
    public ImportCollectionList(List<ImportCollectionDto> importCollections) {
        super(importCollections);
    }
}
