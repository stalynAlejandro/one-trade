package com.pagonxt.onetradefinance.integrations.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Class with some utilities to parse data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ParseUtils {

    private static final String DECIMAL_FORMAT = "###,##0.00";

    /**
     * empty constructor method
     */
    private ParseUtils(){}

    /**
     * class method to parse a Boolean object to yes or not
     * @param value an object with the value
     * @return a string with yes or no
     */
    public static String parseBooleanToYesNo(Object value) {
        if(value instanceof Boolean) {
            return (boolean) value ? "yes" : "no";
        }
        return "no";
    }

    /**
     * class method to parse to Boolean
     * @param value a string with the value
     * @return a boolean value
     */
    public static boolean parseYesNoToBoolean(String value) {
        return "yes".equals(value);
    }

    /**
     * class method to parse an object to double
     * @param value  an object with the value
     * @return a Double object
     */
    public static Double parseObjectToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Double) {
            return (Double) value;
        }
        if (value instanceof String) {
            return Double.parseDouble((String) value);
        }
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        }
        return null;
    }

    /**
     * class method to parse a double value to string
     * @param d a double value
     * @return a string with the value
     */
    public static String parseDoubleToString(Double d) {
        return String.format(Locale.ROOT, "%.2f", d);
    }

    /**
     * class method to parse a double value to string
     * @param d a double value
     * @param locale a string with locale value
     * @return a string with the value
     */
    public static String parseDoubleToString(Double d, String locale) {
        if(d == null) {
            return null;
        }
        DecimalFormat formatter = new DecimalFormat(DECIMAL_FORMAT);
        DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ROOT);
        if(locale != null) {
            switch(locale) {
                case "en_us" :
                    formatSymbols.setGroupingSeparator(',');
                    formatSymbols.setDecimalSeparator('.');
                    break;
                case "es_es":
                    formatSymbols.setGroupingSeparator(' ');
                    formatSymbols.setDecimalSeparator(',');
                    break;
                default:
                    break;
            }
        }
        formatter.setDecimalFormatSymbols(formatSymbols);
        return formatter.format(d);
    }

    /**
     * class method to get locale value from country
     * @param country a string with the country
     * @return a String with locale value
     */
    public static String getLocaleFromCountry(String country) {
        if(country == null) {
            return null;
        }
        switch(country) {
            case "US" :
                return "en_us";
            case "ES":
            default:
                return "es_es";
        }
    }

    /**
     * class method
     * @param unformattedKey a string with an unformatted key
     * @return a string with a formatted key
     */
    public static String parseKey(String unformattedKey) {
        if (unformattedKey == null) {
            return null;
        }
        return unformattedKey.replace(" ", "_").toLowerCase();
    }
}
