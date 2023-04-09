package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.CollectionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get collection types
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.CollectionType
 * @since jdk-11.0.13
 */
@Service
public class CollectionTypeService {

    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(CollectionTypeService.class);

    private List<CollectionType> collectionTypes;

    /**
     * constructor method
     */
    public CollectionTypeService() {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("collection-type-data/collection-types.json")) {
            collectionTypes = new ArrayList<>();
            collectionTypes.addAll(new ObjectMapper().readValue(resourceStream, new TypeReference<>() {}));
        } catch (Exception e) {
            LOG.warn("Error reading collection types file", e);
        }
    }

    /**
     * This method allows to obtain a list of collection types
     * @param productId a string with the product id
     * @param currency  a string with the currency
     * @return a list of collection types
     */
    public List<CollectionType> getCollectionType(String productId, String currency){
        return collectionTypes.stream()
                .filter(collectionType -> collectionType.getProduct().equals(productId))
                .filter(collectionType -> {
                    if(currency != null) {
                        return collectionType.getCurrency().contains(currency);
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * This method allows checking if a collection type exists through a key
     * @param key a string with a key
     * @return true or false if the collection type exists
     */
    public boolean exists(String key){
        return collectionTypes.stream()
                .anyMatch(collectionType -> collectionType.getKey().equals(key));
    }

    /**
     * This method allows to obtain a collection type id through a key
     * @param key a string with a key
     * @return a string with the id of the collection type
     */
    public String getIdByKey(String key){
        return collectionTypes.stream()
                .filter(collectionType -> collectionType.getKey().equals(key))
                .map(CollectionType::getId)
                .findFirst()
                .orElse(null);
    }
}
