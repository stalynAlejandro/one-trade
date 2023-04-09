package com.pagonxt.onetradefinance.logger;
/**
 * class for PagoNxt observability exceptions
 * @author -
 * @version jdk-11.0.13
 * @see java.lang.RuntimeException
 * @since jdk-11.0.13
 */
public class ObservabilityException extends RuntimeException{
    /**
     * constructor method
     * @param message : a string with a message
     */
    public ObservabilityException(String message) {
        super(message);
    }
}
