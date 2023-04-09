package com.pagonxt.onetradefinance.integrations.model.exception;

import java.io.Serializable;

/**
 * class for handling exceptions regarding invalid requests
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class InvalidRequestException extends ServiceException {

    /**
     * constructor method
     * @param message :a string with error message
     * @param key     :a string with the key
     */
    public InvalidRequestException(String message, String key) {
        super(message, key);
    }

    /**
     * constructor method
     * @param message :a string with error message
     * @param key     :a string with the key
     * @param entity  :a Serializable object
     */
    public InvalidRequestException(String message, String key, Serializable entity) {
        super(message, key, null, entity);
    }

    /**
     * constructor method
     * @param paramName :a string with the param name
     */
    public InvalidRequestException(String paramName) {
        super(String.format("Param %s must be informed", paramName), "paramError");
    }

    public InvalidRequestException(String message, String key, Throwable cause) {
        super(message, key, cause, null);
    }
}
