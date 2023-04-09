package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class MockedCurrencyServiceTest {

    MockedCurrencyService mockedCurrencyService = new MockedCurrencyService(new ObjectMapper());

    @Test
    void getCurrencyList_ok_returnList() {
        // Given and When
        List<CurrencyDAO> result = mockedCurrencyService.getCurrencyList("productTest", "eventTest");
        // Then
        assertEquals(180, result.size());
    }
}
