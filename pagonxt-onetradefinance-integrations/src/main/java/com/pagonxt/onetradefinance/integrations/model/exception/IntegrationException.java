package com.pagonxt.onetradefinance.integrations.model.exception;

/**
 * class for handling exceptions regarding integrations
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class IntegrationException extends ServiceException {

    /**
     * constructor class
     */
    public IntegrationException() {
        super("Error connecting with external API", "errorIntegration");
    }
}
