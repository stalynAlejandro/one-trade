package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Class with some utilities to work with api calls
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties
 * @since jdk-11.0.13
 */
@Component
public class ApiParamsUtils {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final SimpleDateFormat dateFormat;

    /**
     * constructor method
     * @param dateFormatProperties a DateFormatProperties object, util to set TimeZone
     */
    public ApiParamsUtils(DateFormatProperties dateFormatProperties) {
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(dateFormatProperties.getTimeZone()));
    }

    /**
     * Class method to inject a query param
     * @param url an UriComponentsBuilder object
     * @param param a string with the param
     * @param value a Date object with the value
     * @see org.springframework.web.util.UriComponentsBuilder
     */
    public void injectQueryParam(UriComponentsBuilder url, String param, Date value) {
        if (value != null) {
            url.queryParam(param, dateFormat.format(value));
        }
    }

    /**
     * Class method to inject a query param
     * @param url an UriComponentsBuilder object
     * @param param a string with the param
     * @param value a double value
     * @see org.springframework.web.util.UriComponentsBuilder
     */
    public void injectQueryParam(UriComponentsBuilder url, String param, Double value) {
        if (value != null) {
            url.queryParam(param, String.format(Locale.ROOT, "%.2f", value));
        }
    }

    /**
     * Class method to inject a query param
     * @param url an UriComponentsBuilder object
     * @param param a string with the param
     * @param value an integer value
     * @see org.springframework.web.util.UriComponentsBuilder
     */
    public void injectQueryParam(UriComponentsBuilder url, String param, Integer value) {
        if (value != null) {
            url.queryParam(param, value.toString());
        }
    }

    /**
     * Class method to inject a query param
     * @param url an UriComponentsBuilder object
     * @param param a string with the param
     * @param value a string with a value
     * @see org.springframework.web.util.UriComponentsBuilder
     */
    public void injectQueryParam(UriComponentsBuilder url, String param, String value) {
        if (value != null) {
            url.queryParam(param, value);
        }
    }
}
