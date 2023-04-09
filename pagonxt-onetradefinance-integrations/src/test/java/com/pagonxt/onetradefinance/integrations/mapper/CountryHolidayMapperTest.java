package com.pagonxt.onetradefinance.integrations.mapper;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.CountryHoliday;
import com.pagonxt.onetradefinance.integrations.model.CountryHolidayResponse;
import com.pagonxt.onetradefinance.integrations.repository.entity.CountryHolidayDAO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class CountryHolidayMapperTest {

    @InjectMocks
    private CountryHolidayMapper countryHolidayMapper;

    @Test
    void toModel_ok_returnModel() {
        // Given
        CountryHolidayDAO countryHolidayDAO = new CountryHolidayDAO();
        countryHolidayDAO.setCountry("ES");
        LocalDate localDate = LocalDate.of(2023,1,10);
        countryHolidayDAO.setHolidayDate(localDate);
        // When
        CountryHoliday result = countryHolidayMapper.toModel(countryHolidayDAO);
        // Then
        assertEquals("ES", result.getCountry());
        assertEquals(localDate, result.getHolidayDate());
    }

    @Test
    void toModel_whenNull_returnNewModel() {
        // Given and When
        CountryHoliday result = countryHolidayMapper.toModel((CountryHolidayDAO) null);
        // Then
        assertNull(result.getCountry());
        assertNull(result.getHolidayDate());
    }

    @Test
    void toModel_whenList_returnResponse() {
        // Given
        CountryHolidayDAO countryHolidayDAO = new CountryHolidayDAO();
        countryHolidayDAO.setCountry("ES");
        LocalDate localDate = LocalDate.of(2023,1,10);
        countryHolidayDAO.setHolidayDate(localDate);
        List<CountryHolidayDAO> countryHolidayDAOList = List.of(countryHolidayDAO);
        // When
        CountryHolidayResponse result = countryHolidayMapper.toModel(countryHolidayDAOList);
        // Then
        CountryHoliday countryHoliday = result.getCountryHolidayList().get(0);
        assertEquals("ES", countryHoliday.getCountry());
        assertEquals(localDate, countryHoliday.getHolidayDate());
    }

    @Test
    void toModel_whenNullList_returnNullResponse() {
        // Given and When
        CountryHolidayResponse result = countryHolidayMapper.toModel((List<CountryHolidayDAO>) null);
        // Then
        assertNull(result);
    }
}
