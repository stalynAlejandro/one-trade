package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get operation types
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.OperationType
 * @since jdk-11.0.13
 */
@Service
public class OperationTypeService {

    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(OperationTypeService.class);

    private List<OperationType> operationTypes;

    /**
     * constructor method
     */
    public OperationTypeService(){
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("operation-type-data/operation-types.json")) {
            operationTypes = new ArrayList<>();
            operationTypes.addAll(new ObjectMapper().readValue(resourceStream, new TypeReference<>() {}));
        } catch (Exception e) {
            LOG.warn("Error reading collection types file", e);
        }

    }

    /**
     * This method allows to obtain a list of operation types through a product id
     * @param productId a string with the product id
     * @return a list of operation types
     */
    public List<OperationType> getOperationTypeByProduct(String productId){
        return operationTypes.stream()
                .filter(collectionType -> collectionType.getProduct().equals(productId))
                .collect(Collectors.toList());
    }

    /**
     * This method allows checking if an operation type exists through a key
     * @param key a string with a key
     * @return true or false if the operation type exists
     */
    public boolean exists(String key){
        return operationTypes.stream()
                .anyMatch(collectionType -> collectionType.getKey().equals(key));
    }

}
