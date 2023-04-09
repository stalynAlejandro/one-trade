package com.pagonxt.onetradefinance.integrations.model.exception;

/**
 * class for handling exceptions regarding the date format
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class DateFormatException extends ServiceException {

    private static final String MESSAGE = "Unable to parse date";
    private static final String ID = "unableToParseDate";

    /**
     * This method exceptions about date format
     * @param date a string with the date
     * @param cause exception cause
     */
    public DateFormatException(String date, Throwable cause) {
        super(MESSAGE, ID, date);
        this.initCause(cause);
    }

    /**
     * This method exceptions about date format
     * @param date a string with the date
     */
    public DateFormatException(String date) {
        super(MESSAGE, ID, date);
    }
}
