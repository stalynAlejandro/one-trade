package com.pagonxt.onetradefinance.integrations.util;

/**
 * Class with some utilities for logs
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class LogUtils {

    /**
     * empty constructor method
     */
    private LogUtils() {
    }

    /**
     * class method to sanitize logs
     * @param log a string with th log
     * @return a string with the log
     */
    public static String sanitizeLog(String log) {
        return log == null ? null : log.replaceAll("[\n\r\t]", "_");
    }


    /**
     * class method to sanitize logs
     * @param logObject an object with the log
     * @return a string with the log
     */
    public static String sanitizeLog(Object logObject) {
        return logObject == null ? null : logObject.toString().replaceAll("[\n\r\t]", "_");
    }
}
