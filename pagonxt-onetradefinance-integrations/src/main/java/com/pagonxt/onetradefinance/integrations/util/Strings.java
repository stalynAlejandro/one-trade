package com.pagonxt.onetradefinance.integrations.util;

/**
 * Class with some utilities to work with strings
 * String utils, so we don't depend on org.apache.logging.log4j.util.Strings for this.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Strings {

    /**
     * empty constructor method
     */
    private Strings() {
    }

    /**
     * class method to check if a string is not blank (it has characters)
     * @param string a string to check
     * @return true or false if a string is not blank
     */
    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }

    /**
     * class method to check if a string is blank (it hasn't characters)
     * @param string a string to check
     * @return true or false if a string is blank
     */
    public static boolean isBlank(String string) {
        if (string == null || string.isEmpty()) {
            return true;
        }
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (!Character.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    public static String emptyIfNull(String string) {
        return string == null ? "" : string;
    }
}
