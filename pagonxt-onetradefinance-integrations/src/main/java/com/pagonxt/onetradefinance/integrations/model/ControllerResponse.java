package com.pagonxt.onetradefinance.integrations.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;

import java.util.List;

/**
 * Model class for responses of controllers actions
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonPropertyOrder({"type", "key", "message", "arguments", "entity"})
public class ControllerResponse {

    private static final String SUCCESS = "success";
    private static final String WARNING = "warning";
    private static final String ERROR = "error";

    /**
     * Valid values: success, warning, error
     */
    private String type;

    private String key;

    private String message;

    private List<String> arguments;

    private Object entity;

    /**
     * empty constructor method
     */
    public ControllerResponse() {
    }

    /**
     * constructor
     * @param type      : a string with the type
     * @param key       : a string with the key
     * @param message   : a string with the message
     * @param entity    : an object with the entity
     * @param arguments : a string with arguments
     */
    public ControllerResponse(String type, String key, String message, Object entity, String... arguments) {
        this.type = type;
        this.key = key;
        this.message = message;
        this.entity = entity;
        this.arguments = arguments == null ? List.of() : List.of(arguments);
    }

    /**
     * class method to build a success response
     * @param key       : a string with the key
     * @param entity    : an object with the entity
     * @param arguments : a string with arguments
     * @return a success response
     */
    public static ControllerResponse success(String key, Object entity, String... arguments) {
        return new ControllerResponse(SUCCESS, key, null, entity, arguments);
    }

    /**
     * class method to build a success response
     * @param key       : a string with the key
     * @param entity    : an object with the entity
     * @return a success response
     */
    public static ControllerResponse success(String key, Object entity) {
        return new ControllerResponse(SUCCESS, key, null, entity);
    }

    /**
     * class method to build a warning response
     * @param key       : a string with the key
     * @param arguments : a string with arguments
     * @return a warning response
     */
    public static ControllerResponse warning(String key, String... arguments) {
        return new ControllerResponse(WARNING, key, null, null, arguments);
    }

    /**
     * class method to build an error response
     * @param key       : a string with the key
     * @param arguments : a string with arguments
     * @return an error response
     */
    public static ControllerResponse error(String key, String... arguments) {
        return new ControllerResponse(ERROR, key, null, null, arguments);
    }

    /**
     * class method to build an error response
     * @param message : a string with the error message
     * @return an error response
     */
    public static ControllerResponse error(String message) {
        return new ControllerResponse(ERROR, null, message, null);
    }

    /**
     * class method to build an error response
     * @param serviceException : a ServiceException object
     * @return an error response
     */
    public static ControllerResponse error(ServiceException serviceException) {
        String[] arguments = serviceException.getArguments().toArray(String[]::new);
        return new ControllerResponse(ERROR, serviceException.getKey(),
                serviceException.getMessage(), serviceException.getEntity(), arguments);
    }

    /**
     * getter method
     * @return a string with the type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type a string with the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return a string with the key
     */
    public String getKey() {
        return key;
    }

    /**
     * setter method
     * @param key a string with the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * getter method
     * @return a string with the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * setter method
     * @param message a string with the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * getter method
     * @return a list with arguments
     */
    public List<String> getArguments() {
        return arguments;
    }

    /**
     * setter method
     * @param arguments a list with arguments
     */
    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    /**
     * getter method
     * @return an object with the entity
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * setter method
     * @param entity an object with the entity
     */
    public void setEntity(Object entity) {
        this.entity = entity;
    }

}
