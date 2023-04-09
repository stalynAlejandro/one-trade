package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.CountryHoliday;
import com.pagonxt.onetradefinance.integrations.model.CountryHolidayResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Mock;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@UnitTest
class CalendarServiceTest {
    @InjectMocks
    private CalendarService calendarService;
    @Mock
    private CountryHolidayService countryHolidayService;
    private final CountryHolidayResponse COUNTRY_HOLIDAY_RESPONSE = generateListHolidays();

    @Test
    void nextWorkDay_ok_returnsCorrectLocalDate() {
        // Given
        when(countryHolidayService.getCountryHolidayResponse("ES")).thenReturn(COUNTRY_HOLIDAY_RESPONSE);
        LocalDate mockedToday = LocalDate.of(2022,1,1);
        LocalDate expectedNextDay = LocalDate.of(2022,1,3);
        try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(mockedToday);
            mockedLocalDate.when(()->LocalDate.from(mockedToday)).thenReturn(mockedToday);
            //when
            LocalDate result = calendarService.nextWorkDay();
            //then
            assertEquals(expectedNextDay,result);
        }
    }

    @Test
    void testNextWorkDay() {
        // Given
        when(countryHolidayService.getCountryHolidayResponse("ES")).thenReturn(COUNTRY_HOLIDAY_RESPONSE);
        LocalDate mockedToday = LocalDate.of(2022,1,1);
        LocalDate mockedNextToToday = LocalDate.of(2022,1,2);
        try (MockedStatic<LocalDate> mockedLocalDate = Mockito.mockStatic(LocalDate.class)) {
            mockedLocalDate.when(LocalDate::now).thenReturn(mockedToday);
            mockedLocalDate.when(()->LocalDate.from(mockedNextToToday)).thenReturn(mockedNextToToday);
            //when
            Duration result = calendarService.nextWorkDay(1);
            //then
            assertThat(result).isEqualTo(Duration.ofDays(2));
        }
    }

    @ParameterizedTest
    @CsvSource({
        "2022-08-12, 2022-08-12",
        "2022-08-13, 2022-08-16",
        "2022-08-14, 2022-08-16",
        "2022-08-15, 2022-08-16",
        "2022-08-16, 2022-08-16"
    })
    void nextWorkDay(String date, String nextWorkDate) {
        // Given
        when(countryHolidayService.getCountryHolidayResponse("ES")).thenReturn(COUNTRY_HOLIDAY_RESPONSE);
        LocalDate localDate = LocalDate.parse(date);
        // When
        LocalDate nextWorkDay = calendarService.nextWorkDay(localDate);
        // Then
        assertThat(nextWorkDay).isEqualTo(nextWorkDate);
    }

    @ParameterizedTest
    @CsvSource({
        "2022-08-12, true, false",
        "2022-08-13, false, true",
        "2022-08-14, false, true",
        "2022-08-15, false, false",
        "2022-08-16, true, false"
    })
    void isWorkDay(String date, boolean expectedIsWorkDay, boolean isWeekend) {
        // Given
        if (!isWeekend) {
            when(countryHolidayService.getCountryHolidayResponse("ES")).thenReturn(COUNTRY_HOLIDAY_RESPONSE);
        }
        LocalDate localDate = LocalDate.parse(date);
        // When
        boolean isWorkDay = calendarService.isWorkDay(localDate);
        // Then
        assertThat(isWorkDay).isEqualTo(expectedIsWorkDay);
    }

    @ParameterizedTest
    @CsvSource({
        "2022-08-12, 2022-08-16, 1",
        "2022-08-13, 2022-08-16, 0",
        "2022-08-15, 2022-08-22, 4"
    })
    void workDaysBetweenDays(String startDate, String endDate, long numberOfWorkDays) {
        // Given
        when(countryHolidayService.getCountryHolidayResponse("ES")).thenReturn(COUNTRY_HOLIDAY_RESPONSE);
        // When
        long workDays = calendarService.workDaysBetweenDates(LocalDate.parse(startDate), LocalDate.parse(endDate));
        // Then
        assertThat(workDays).isEqualTo(numberOfWorkDays);
    }

    private CountryHolidayResponse generateListHolidays() {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("holiday-data/holiday.json")) {
            JavaTimeModule module = new JavaTimeModule();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(module);
            List<CountryHoliday> countryHolidayList =
                    Arrays.asList(objectMapper.readValue(resourceStream, new TypeReference<>(){}));
            CountryHolidayResponse countryHolidayResponse = new CountryHolidayResponse();
            countryHolidayResponse.setCountryHolidayList(countryHolidayList);
            return countryHolidayResponse;
        } catch (Exception e) {
            throw new ServiceException("Error mapping holidays", "errorCalendarServiceTest", e);
        }
    }
}