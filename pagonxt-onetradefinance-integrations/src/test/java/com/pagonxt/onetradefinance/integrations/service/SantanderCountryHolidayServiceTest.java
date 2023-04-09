package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.mapper.CountryHolidayMapper;
import com.pagonxt.onetradefinance.integrations.model.CountryHoliday;
import com.pagonxt.onetradefinance.integrations.model.CountryHolidayResponse;
import com.pagonxt.onetradefinance.integrations.repository.CountryHolidayRepository;
import com.pagonxt.onetradefinance.integrations.repository.entity.CountryHolidayDAO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
class SantanderCountryHolidayServiceTest {

    @InjectMocks
    private SantanderCountryHolidayService santanderCountryHolidayService;
    @Mock
    private CountryHolidayRepository countryHolidayRepository;
    @Mock
    private CountryHolidayMapper countryHolidayMapper;

    @Test
    void getCountryHolidayResponse_ok_callRepository() {
        // Given
        List<CountryHolidayDAO> countryHolidayDAOList = List.of(new CountryHolidayDAO());
        CountryHolidayResponse countryHolidayResponse = new CountryHolidayResponse();
        when(countryHolidayRepository.findAll()).thenReturn(countryHolidayDAOList);
        when(countryHolidayRepository.findByCountry("ES")).thenReturn(countryHolidayDAOList);
        when(countryHolidayMapper.toModel(countryHolidayDAOList)).thenReturn(countryHolidayResponse);
        // When
        CountryHolidayResponse result = santanderCountryHolidayService.getCountryHolidayResponse("ES");
        // Then
        verify(countryHolidayRepository, times(1)).findAll();
        verify(countryHolidayRepository, times(1)).findByCountry("ES");
        assertFalse(result.isErrorDB());
    }

    @Test
    void getCountryHolidayResponse_okWithBackup_callRepository() {
        // Given
        List<CountryHolidayDAO> countryHolidayDAOList = List.of(new CountryHolidayDAO());
        CountryHolidayResponse countryHolidayResponse = new CountryHolidayResponse();
        when(countryHolidayRepository.findByCountry("ES")).thenReturn(countryHolidayDAOList);
        when(countryHolidayMapper.toModel(countryHolidayDAOList)).thenReturn(countryHolidayResponse);
        ReflectionTestUtils.setField(santanderCountryHolidayService, "backupCountryHolidayResponse", countryHolidayResponse);
        // When
        CountryHolidayResponse result = santanderCountryHolidayService.getCountryHolidayResponse("ES");
        // Then
        verify(countryHolidayRepository, never()).findAll();
        verify(countryHolidayRepository, times(1)).findByCountry("ES");
        assertFalse(result.isErrorDB());
    }

    @Test
    void getCountryHolidayResponse_koDB_getBackup() {
        // Given
        CountryHolidayResponse countryHolidayResponseBackup = new CountryHolidayResponse();
        CountryHoliday countryHoliday = new CountryHoliday();
        countryHoliday.setCountry("ES");
        countryHoliday.setHolidayDate(LocalDate.of(2023,1,10));
        countryHolidayResponseBackup.setCountryHolidayList(List.of(countryHoliday));
        ReflectionTestUtils.setField(santanderCountryHolidayService, "backupCountryHolidayResponse", countryHolidayResponseBackup);
        when(countryHolidayRepository.findByCountry("ES")).thenThrow(NullPointerException.class);
        // When
        CountryHolidayResponse result = santanderCountryHolidayService.getCountryHolidayResponse("ES");
        // Then
        verify(countryHolidayRepository, never()).findAll();
        verify(countryHolidayRepository, times(1)).findByCountry("ES");
        assertEquals(1, result.getCountryHolidayList().size());
    }

    @Test
    void getCountryHolidayResponse_koDBAndNoBackUp_getBackupFromFile() {
        // Given
        when(countryHolidayRepository.findAll()).thenThrow(NullPointerException.class);
        // When
        CountryHolidayResponse result = santanderCountryHolidayService.getCountryHolidayResponse("ES");
        // Then
        verify(countryHolidayRepository, times(1)).findAll();
        assertEquals(10, result.getCountryHolidayList().size());
    }
}
