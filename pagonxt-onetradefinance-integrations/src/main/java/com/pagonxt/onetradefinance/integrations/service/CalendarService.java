package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.CountryHoliday;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to obtain work days (Santander calendar)
 * @author -
 * @version jdk-11.0.13
 * @see CalendarService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class CalendarService {

    /**
     * Repository to get holiday information.
     */
    private final CountryHolidayService countryHolidayService;

    /**
     * constructor method
     * @param countryHolidayService an CountryHolidayService object, that provides functionality for consulting holidays
     */
    public CalendarService(CountryHolidayService countryHolidayService) {
        this.countryHolidayService = countryHolidayService;
    }

    /**
     * This method allows to obtain the next workday
     * @return a LocalDate object with the next work day
     */

    public LocalDate nextWorkDay() {
        return nextWorkDay(LocalDate.now());
    }

    /**
     * This method allows to obtain the next workday
     * @param date a LocalDate object with a date
     * @return a LocalDate object with the next work day
     */

    public LocalDate nextWorkDay(LocalDate date) {
        LocalDate nextWorkday = LocalDate.from(date);
        while(!isWorkDay(nextWorkday)) {
            nextWorkday = nextWorkday.plusDays(1);
        }
        return nextWorkday;
    }

    /**
     * This method allows to obtain the next workday
     * @param days an integer value with number of days
     * @return a Duration object with the amount of time
     */

    public Duration nextWorkDay(int days) {
        LocalDate now = LocalDate.now();
        LocalDate nextWorkday = LocalDate.now();
        for (int i = 0; i < days; i++) {
            nextWorkday = nextWorkday.plusDays(1);
            nextWorkday = this.nextWorkDay(nextWorkday);
        }
        return Duration.between(now.atStartOfDay(),nextWorkday.atStartOfDay());
    }

    /**
     * This method allows checking if a date is a work day
     * @param date a LocalDate object with a date
     * @return true or false if the date is a work day
     */

    public boolean isWorkDay(LocalDate date) {
        return isNotWeekend(date) && isNotSantanderHoliday(date);
    }

    /**
     * This method allows to calculate the number of work days between two dates
     * @param start a LocalDate object with the start date
     * @param end a LocalDate object with the end date
     * @return a long value with the number of work days between two dates
     */

    public long workDaysBetweenDates(LocalDate start, LocalDate end) {
        return start.datesUntil(end)
                .filter(date -> isNotWeekend(date) && isNotSantanderHoliday(date))
                .count();
    }

    /**
     * This method allows checking if a date belongs to a weekend day
     * @param date a LocalDate object with a date
     * @return true if the day of week is a week day, false otherwise
     */
    private boolean isNotWeekend(LocalDate date) {
        return !(date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                date.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

    /**
     * This method allows checking if a date is a holiday
     * @param date a LocalDate object with a date
     * @return true if the date is not a holiday, false otherwise
     */
    private boolean isNotSantanderHoliday(LocalDate date) {
        List<LocalDate> holidays = countryHolidayService.getCountryHolidayResponse("ES").getCountryHolidayList().stream()
                .map(CountryHoliday::getHolidayDate).collect(Collectors.toList());
        return !holidays.contains(date);
    }

}
