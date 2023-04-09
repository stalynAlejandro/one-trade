package com.pagonxt.onetradefinance.work.expression.common;

/**
 * class for PagoNxt expression exceptions
 * @author -
 * @version jdk-11.0.13
 * @see java.lang.RuntimeException
 * @since jdk-11.0.13
 */
public class PagoNxtExpressionException extends RuntimeException {

    /**
     * constructor method
     * @param message : a string with a message
     */
    public PagoNxtExpressionException(String message) {
        super(message);
    }
}
