package com.pagonxt.onetradefinance.integrations.model.exception;

/**
 * class for handling exceptions regarding the expired token
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class ExpiredTokenException extends ServiceException {

    /**
     * constructor method
     */
    public ExpiredTokenException() {
        super("The token has expired", "errorToken");
    }
}
