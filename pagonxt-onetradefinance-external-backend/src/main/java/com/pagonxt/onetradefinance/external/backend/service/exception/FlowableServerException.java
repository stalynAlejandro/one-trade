package com.pagonxt.onetradefinance.external.backend.service.exception;

import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;

/**
 * Class for error handling of flowable server
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class FlowableServerException extends ServiceException {

    /**
     * Class variables
     */
    private static final String ID = "flowableServerError";
    public static final String UNKNOWN_ERROR = "Unknown Error";

    /**
     * class constructor
     * @param message a string with error message
     */
    public FlowableServerException(String message) {
        super(message, ID);
    }

    /**
     * Class constructor, without parameters
     */
    public FlowableServerException() {
        super(UNKNOWN_ERROR, ID);
    }
}
