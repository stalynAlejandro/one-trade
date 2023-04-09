package com.pagonxt.onetradefinance.integrations.model.exception;
/**
 * class for PagoNxt observability exceptions
 * @author -
 * @version jdk-11.0.13
 * @see RuntimeException
 * @since jdk-11.0.13
 */
public class ObservabilityException extends RuntimeException {
    /**
     * constructor method
     * @param message : a string with a message
     */
    public ObservabilityException(String message) {
        super(message);
    }
}
