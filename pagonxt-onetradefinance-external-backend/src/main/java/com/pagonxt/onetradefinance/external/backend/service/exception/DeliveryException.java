package com.pagonxt.onetradefinance.external.backend.service.exception;

import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;

/**
 * Class for error handling
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @see java.lang.Throwable
 * @since jdk-11.0.13
 */
public class DeliveryException extends ServiceException {

    /**
     * Class constructor
     * @param content a string with the error message
     * @param cause error or exception
     */
    public DeliveryException(String content, Throwable cause) {
        super(String.format("Error sending %s to Flowable Work", content), "errorSendingToFlowable", cause);
    }
}
