package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pagonxt.onetradefinance.integrations.mapper.CountryHolidayMapper;
import com.pagonxt.onetradefinance.integrations.model.CountryHoliday;
import com.pagonxt.onetradefinance.integrations.model.CountryHolidayResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.repository.CountryHolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Class to find country holidays information from the repository.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class SantanderCountryHolidayService implements CountryHolidayService {

    private static final Logger LOG = LoggerFactory.getLogger(SantanderCountryHolidayService.class);

    /**
     * Repository to get country holiday information.
     */
    private final CountryHolidayRepository countryHolidayRepository;

    private final CountryHolidayMapper countryHolidayMapper;

    private CountryHolidayResponse backupCountryHolidayResponse = null;

    /**
     * Constructor class.
     *
     * @param countryHolidayRepository  : the country holiday repository
     * @param countryHolidayMapper      : the country holiday mapper
     */
    public SantanderCountryHolidayService(CountryHolidayRepository countryHolidayRepository, CountryHolidayMapper countryHolidayMapper) {
        this.countryHolidayRepository = countryHolidayRepository;
        this.countryHolidayMapper = countryHolidayMapper;
    }

    /**
     * Method to get the list of holidays.
     *
     * @param country   : the country
     *
     * @return the list of holidays
     */
    @Override
    @Cacheable(cacheNames="countryHoliday", unless="#result.errorDB")
    public CountryHolidayResponse getCountryHolidayResponse(String country) {
        CountryHolidayResponse countryHolidayResponse;
        try {
            if (backupCountryHolidayResponse == null) {
                backupCountryHolidayResponse = countryHolidayMapper.toModel(countryHolidayRepository.findAll());
            }
            countryHolidayResponse = countryHolidayMapper.toModel(countryHolidayRepository.findByCountry(country));
            countryHolidayResponse.setErrorDB(false);
        } catch(Exception e) {
            LOG.error("Connection with repository failed");
            countryHolidayResponse = getCountryHolidayResponseFromMemory(country);
        }
        return countryHolidayResponse;
    }

    private CountryHolidayResponse getCountryHolidayResponseFromMemory(String country) {
        List<CountryHoliday> countryHolidayList;
        if (backupCountryHolidayResponse != null) {
            countryHolidayList = backupCountryHolidayResponse.getCountryHolidayList().
                    stream().filter(x -> country.equals(x.getCountry())).collect(Collectors.toList());
        } else  {
            LOG.error("Country holiday list does not exist in memory");
            countryHolidayList = getCountryHolidayResponseFromFile();
        }
        CountryHolidayResponse countryHolidayResponse = new CountryHolidayResponse();
        countryHolidayResponse.setCountryHolidayList(countryHolidayList);
        countryHolidayResponse.setErrorDB(true);
        return countryHolidayResponse;
    }

    private List<CountryHoliday> getCountryHolidayResponseFromFile() {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("holiday-data/holiday.json")) {
            JavaTimeModule module = new JavaTimeModule();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(module);
            return objectMapper.readValue(resourceStream, new TypeReference<>(){});
        } catch (Exception e) {
            throw new ServiceException("Error mapping holidays", "errorSantanderCountryHolidayService", e);
        }
    }

}
