package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class create an object with a list of collection types(DTO)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.CollectionTypeDto
 * @since jdk-11.0.13
 */
public class CollectionTypeSearchResponse extends ArrayList<CollectionTypeDto> {

    /**
     * Class constructor
     * @param collectionTypes list of collection types(dto)
     */
    public CollectionTypeSearchResponse(List<CollectionTypeDto> collectionTypes) {
        super(collectionTypes);
    }
}
