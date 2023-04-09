package com.pagonxt.onetradefinance.integrations.model.exception;

/**
 * class for handling exceptions regarding integrations
 * @author -
 * @version jdk-11.0.13
 * @see ServiceException
 * @since jdk-11.0.13
 */
public class ServiceUnavailableException extends ServiceException {

    /**
     * constructor class
     */
    public ServiceUnavailableException() {
        super("The service is unavailable", "errorServiceUnavailable");
    }
}
