package com.pagonxt.onetradefinance.work.service.exception;

import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;

/**
 * Class with some methods to handle mapping exceptions
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class MappingException extends ServiceException {

    /**
     * constructor method
     * @param message   : a string with the message
     * @param key       : a string with the key
     */
    public MappingException(String message, String key) {
        super(message, key);
    }

    /**
     * constructor method
     * @param message   : a string with the message
     * @param key       : a string with the key
     * @param arguments : one or more arguments
     */
    public MappingException(String message, String key, String... arguments) {
        super(message, key, arguments);
    }

    /**
     * constructor method
     * @param message   : a string with the message
     * @param key       : a string with the key
     * @param cause     : a Throwable object with cause
     */
    public MappingException(String message, String key, Throwable cause) {
        super(message, key, cause);
    }

    /**
     * constructor method
     * @param message   : a string with the message
     * @param key       : a string with the key
     * @param cause     : a Throwable object with cause
     * @param arguments : one or more arguments
     */
    public MappingException(String message, String key, Throwable cause, String... arguments) {
        super(message, key, cause, null, arguments);
    }
}
