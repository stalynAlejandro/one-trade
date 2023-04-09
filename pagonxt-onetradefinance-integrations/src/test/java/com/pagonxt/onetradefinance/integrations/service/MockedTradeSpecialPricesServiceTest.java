package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class MockedTradeSpecialPricesServiceTest {

    ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    MockedTradeSpecialPricesService tradeSpecialPricesService;

    @Test
    void getSpecialPrices() throws IOException {
        // Given
        TypeReference<List<TradeSpecialPrices>> tradeSpecialPrices = new TypeReference<>() {};
        List<TradeSpecialPrices> specialPricesList = mapper.readValue(new ClassPathResource("mock-data/special-prices.json").getFile(), tradeSpecialPrices);
        // When
        List<TradeSpecialPrices> result = tradeSpecialPricesService.getSpecialPrices(new TradeSpecialPricesQuery(), "es_es");
        // Then
        assertEquals(specialPricesList, result);
    }
}