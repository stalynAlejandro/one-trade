package com.pagonxt.onetradefinance.integrations.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Configuration
@ConfigurationProperties(prefix = "one-trade.mapper")
public class DateFormatProperties implements Serializable {


    /**
     * Class attributes
     * The timeZone used to serialize and deserialize dto dates
     * The date format used to serialize and deserialize dto dates
     */
    private String timeZone;
    private String dateFormat;

    /**
     * class method
     * This method calls getDateFormat method
     *
     * @return a DateFormat object
     * @see java.text.DateFormat
     */
    public DateFormat getDateFormatInstance() {
        return getDateFormatInstance(dateFormat);
    }

    /**
     * class method
     *
     * @param dateFormat a string with a dateFormat
     * @return a DateFormat object
     * @see java.text.DateFormat
     */
    public DateFormat getDateFormatInstance(String dateFormat) {
        DateFormat dateTimeFormat = new SimpleDateFormat(dateFormat);
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return dateTimeFormat;
    }

    /**
     * Getter method for field timeZone
     *
     * @return value of timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Setter method for field timeZone
     *
     * @param timeZone : value of timeZone
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    /**
     * Getter method for field dateFormat
     *
     * @return value of dateFormat
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Setter method for field dateFormat
     *
     * @param dateFormat : value of dateFormat
     */
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}


