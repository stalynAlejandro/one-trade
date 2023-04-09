package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pagonxt.onetradefinance.integrations.model.CountryHoliday;
import com.pagonxt.onetradefinance.integrations.model.CountryHolidayResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MockedCountryHolidayService implements CountryHolidayService {
    private static final Logger log = LoggerFactory.getLogger(MockedCountryHolidayService.class);
    private final List<CountryHoliday> mockedHolidays = new ArrayList<>();

    public MockedCountryHolidayService(){
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("holiday-data/holiday.json")) {
            JavaTimeModule module = new JavaTimeModule();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(module);
            CountryHoliday[] holidays = objectMapper.readValue(resourceStream, CountryHoliday[].class);
            mockedHolidays.addAll(Arrays.asList(holidays));
            log.debug("Holidays repository initialized with {} items", mockedHolidays.size());
        } catch (Exception e) {
            throw new ServiceException("Error mapping holidays", "errorMockedCountryHolidayService", e);
        }
    }
    @Override
    public CountryHolidayResponse getCountryHolidayResponse(String country) {
        CountryHolidayResponse countryHolidayResponse = new CountryHolidayResponse();
        List<CountryHoliday> countryHolidayList =
                mockedHolidays.stream().filter(x -> country.equals(x.getCountry())).collect(Collectors.toList());
        countryHolidayResponse.setCountryHolidayList(countryHolidayList);
        countryHolidayResponse.setErrorDB(false);
        return countryHolidayResponse;
    }
}
