package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.integrations.service.CalendarService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * service class for calendar expressions
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.CalendarService
 * @since jdk-11.0.13
 */
@Service
public class CalendarExpressions {

    //class attribute
    private final CalendarService calendarService;
    //TODO: Reemplazar por carga de propiedad de la aplicación
    private static final String TIMEZONE = "Europe/Madrid";

    /**
     * constructor method
     * @param calendarService : a CalendarService object
     */
    public CalendarExpressions(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * Method to get the next workday
     * @see java.time.LocalDate
     * @return a LocalDate object with the next workday
     */
    public LocalDate nextWorkDay() {
        return calendarService.nextWorkDay();
    }

    /**
     * class method
     * @param days : an integer value
     * @return a DateTime object
     */
    public org.joda.time.DateTime nextWorkDay(int days){
        java.time.LocalDateTime nowLocalDateTime = java.time.LocalDateTime.now();
        Duration nextDayDuration = calendarService.nextWorkDay(days);
        java.time.LocalDateTime nextDayLocalDateTime = nowLocalDateTime.plusSeconds(nextDayDuration.getSeconds());
        return new org.joda.time.LocalDateTime(
                nextDayLocalDateTime.toInstant(this.getOffset(nextDayLocalDateTime)).toEpochMilli()
        ).toDateTime();
    }

    /**
     * Method to get the default zoneId of the system
     * @see java.time.ZoneId
     * @return a ZoneId object with the default zoneId of the system
     */
    public ZoneId getSystemDefaultZoneId(){
        return ZoneId.systemDefault();
    }

    /**
     * Method to get a time-zone offset
     * @param localDateTime a LocalDateTime object with local date time
     * @return a ZoneOffSet object
     */
    private ZoneOffset getOffset(java.time.LocalDateTime localDateTime){
        return ZoneId.of(TIMEZONE).getRules().getOffset(localDateTime);
    }
}
