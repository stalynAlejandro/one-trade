package com.pagonxt.onetradefinance.integrations.model.exception;

import java.io.Serializable;
import java.util.List;

/**
 * class for handling exceptions
 * This class provides generic information that is used by the rest of the classes in this package.
 * @author -
 * @version jdk-11.0.13
 * @see java.lang.RuntimeException
 * @see java.io.Serializable
 * @since jdk-11.0.13
 */
public class ServiceException extends RuntimeException {

    private final String key;
    private final List<String> arguments;
    private final Serializable entity;

    /**
     * constructor method
     * @param message :a string with the error message
     * @param key     :a string with the key
     */
    public ServiceException(String message, String key) {
        this(message, key, null, null, (String[]) null);
    }

    /**
     * constructor method
     * @param message   :a string with the error message
     * @param key       :a string with the key
     * @param arguments :a string with one or more arguments
     */
    public ServiceException(String message, String key, String... arguments) {
        this(message, key, null, null, arguments);
    }

    /**
     * constructor method
     * @param message   :a string with the error message
     * @param key       :a string with the key
     * @param cause     :a Throwable object with the cause
     */
    public ServiceException(String message, String key, Throwable cause) {
        this(message, key, cause, null, (String[]) null);
    }

    /**
     * constructor method
     * @param message   :a string with the error message
     * @param key       :a string with the key
     * @param cause     :a Throwable object with the cause
     * @param entity    :a Serializable object
     * @param arguments :a string with one or more arguments
     */
    public ServiceException(String message, String key, Throwable cause, Serializable entity, String... arguments) {
        super(message);
        this.key = key;
        this.entity = entity;
        if (cause != null) {
            initCause(cause);
        }
        this.arguments = arguments == null ? List.of() : List.of(arguments);
    }

    /**
     * getter method
     * @return a string with the key
     */
    public String getKey() {
        return key;
    }

    /**
     * getter method
     * @return a list with arguments
     */
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * getter method
     * @return a Serializable object with entity
     */
    public Serializable getEntity() {
        return entity;
    }

}
