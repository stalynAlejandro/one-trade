package com.pagonxt.onetradefinance.integrations.model.exception;

/**
 * class for handling exceptions regarding integrations
 * @author -
 * @version jdk-11.0.13
 * @see ServiceException
 * @since jdk-11.0.13
 */
public class ForbiddenException extends ServiceException {

    /**
     * constructor class
     */
    public ForbiddenException() {
        super("Not enough privileges to perform the action", "errorForbidden");
    }
}
