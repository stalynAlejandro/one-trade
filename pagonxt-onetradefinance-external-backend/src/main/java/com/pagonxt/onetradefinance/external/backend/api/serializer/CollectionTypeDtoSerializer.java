package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.CollectionTypeDto;
import com.pagonxt.onetradefinance.integrations.model.CollectionType;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert DTO's into entities and viceversa, for Collection types
 * In this case, there is only a method to convert from model(entity) to dto
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class CollectionTypeDtoSerializer {

    /**
     * Method to convert a collectionType object to a collectionTypeDto object
     * @param collectionType collectionType object
     * @return collectionTypeDTO
     */
    public CollectionTypeDto toDto(CollectionType collectionType) {
        if(collectionType == null) {
            return null;
        }
        CollectionTypeDto collectionTypeDto = new CollectionTypeDto();
        collectionTypeDto.setId(collectionType.getId());
        collectionTypeDto.setLabel(collectionType.getName());
        collectionTypeDto.setProduct(collectionType.getProduct());
        collectionTypeDto.setKey(collectionType.getKey());
        return collectionTypeDto;
    }
}
