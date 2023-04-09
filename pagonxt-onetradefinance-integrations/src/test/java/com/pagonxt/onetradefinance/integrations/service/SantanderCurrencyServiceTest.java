package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.repository.CurrencyRepository;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class SantanderCurrencyServiceTest {

    @InjectMocks
    private SantanderCurrencyService santanderCurrencyService;
    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    void getCurrencyList_ok_callRepository() {
        // Given
        List<CurrencyDAO> expectedResult = List.of(new CurrencyDAO());
        when(currencyRepository.findAll()).thenReturn(expectedResult);
        // When
        List<CurrencyDAO> result = santanderCurrencyService.getCurrencyList("productTest", "eventTest");
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void getCurrencyList_callMethodTwice_callRepositoryOnce() {
        // Given
        List<CurrencyDAO> expectedResult = List.of(new CurrencyDAO());
        when(currencyRepository.findAll()).thenReturn(expectedResult);
        // When
        List<CurrencyDAO> result1 = santanderCurrencyService.getCurrencyList("productTest1", "eventTest1");
        List<CurrencyDAO> result2 = santanderCurrencyService.getCurrencyList("productTest2", "eventTest2");
        // Then
        verify(currencyRepository, times(1)).findAll();
        assertEquals(expectedResult, result1);
        assertEquals(expectedResult, result2);
    }

    @Test
    void getCurrencyList_repositoryFail_thenThrowServiceException() {
        // Given
        when(currencyRepository.findAll()).thenThrow(NullPointerException.class);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> santanderCurrencyService.getCurrencyList("productTest", "eventTest"));
        // Then
        assertEquals("errorRepository", exception.getKey());
        assertEquals("Connection with repository failed", exception.getMessage());
    }
}
