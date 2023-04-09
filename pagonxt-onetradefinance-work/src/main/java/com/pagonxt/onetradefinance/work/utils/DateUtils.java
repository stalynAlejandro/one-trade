package com.pagonxt.onetradefinance.work.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class with date utils
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class DateUtils {

    public static final ZoneId ZONE_ID = ZoneId.of("Europe/Madrid");

    /**
     * empty constructor method
     */
    private DateUtils() {
    }

    /**
     * class method to get the start of day
     * @param date : a Date object with date
     * @return a Date object
     */
    public static Date getStartOfDay(Date date, String timeZone) {
        return fixDate(date, 0, 0, 0, 0, timeZone);
    }

    /**
     * class method to get the end of day
     * @param date : a Date object with date
     * @return a Date object
     */
    public static Date getEndOfDay(Date date, String timeZone) {
        return fixDate(date, 23, 59, 59, 999, timeZone);
    }

    /**
     * Fixes a date with a given hours, minutes, seconds, milliseconds and timeZone
     * @param date : the date to fix
     * @param hours : the new hours
     * @param minutes : the new minutes
     * @param seconds : the new seconds
     * @param milliseconds : the new milliseconds
     * @param timeZone : the timeZone
     * @return a Date object
     */
    private static Date fixDate(Date date, int hours, int minutes, int seconds, int milliseconds, String timeZone) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timeZone));
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milliseconds);
        return new Date(calendar.getTimeInMillis());
    }

    /**
     * class method to pass a local date to date
     * @param localDate : a LocalDate object with date
     * @return a Date object
     */
    public static Date localDateToDate(LocalDate localDate) {
        if(localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZONE_ID).toInstant());
    }

    /**
     * class method to pass a date to local date
     * @param date : a Date object with date
     * @return a LocalDate object
     */
    public static LocalDate dateToLocalDate(Date date) {
        if(date == null) {
            return null;
        }
        return date.toInstant().atZone(ZONE_ID).toLocalDate();
    }
}
