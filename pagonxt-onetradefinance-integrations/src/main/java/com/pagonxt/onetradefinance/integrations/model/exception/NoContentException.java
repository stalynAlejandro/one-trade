package com.pagonxt.onetradefinance.integrations.model.exception;

/**
 * class for handling exceptions regarding integrations
 * @author -
 * @version jdk-11.0.13
 * @see ServiceException
 * @since jdk-11.0.13
 */
public class NoContentException extends ServiceException {

    /**
     * constructor class
     */
    public NoContentException() {
        super("No content received", "errorNoCOntent");
    }
}
