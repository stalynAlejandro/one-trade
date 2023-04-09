package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class MockedFxDealServiceTest {

    ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    MockedFxDealService mockedFxDealService = new MockedFxDealService();

    @Test
    void testSearchClients() throws IOException {
        // Given
        TypeReference<List<ExchangeInsurance>> exchangeInsuranceType = new TypeReference<>() {};
        List<ExchangeInsurance> expectedExchangeInsurances = mapper.readValue(new ClassPathResource("mock-data/exchange-insurances.json").getFile(), exchangeInsuranceType);
        // When
        List<ExchangeInsurance> result = mockedFxDealService.searchFxDeals(new FxDealQuery());
        // Then
        assertEquals(expectedExchangeInsurances, result);
    }
}
