package com.pagonxt.onetradefinance.integrations.model.exception;

/**
 * class for handling exceptions regarding integrations
 * @author -
 * @version jdk-11.0.13
 * @see ServiceException
 * @since jdk-11.0.13
 */
public class TimeoutException extends ServiceException {

    /**
     * constructor class
     */
    public TimeoutException() {
        super("The server has not received a response in tume", "errorTimeout");
    }
}
