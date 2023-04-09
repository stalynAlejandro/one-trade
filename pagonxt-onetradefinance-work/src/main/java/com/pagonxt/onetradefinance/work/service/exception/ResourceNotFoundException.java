package com.pagonxt.onetradefinance.work.service.exception;

import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;

/**
 * Class with some methods to handle exceptions when a resource is not found
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class ResourceNotFoundException extends ServiceException {

    /**
     * constructor method
     * @param message   : a string with the message
     * @param key       : a string with the key
     */
    public ResourceNotFoundException(String message, String key) {
        super(message, key);
    }
    public ResourceNotFoundException(String message, String key, Throwable cause) {
        super(message, key, cause, null);
    }
}
