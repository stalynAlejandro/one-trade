package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.TradeSpecialPricesGateway;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SantanderTradeSpecialPricesResponse;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer.SantanderTradeSpecialPricesSerializer;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

@UnitTest
class SantanderTradeSpecialPricesServiceTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private TradeSpecialPricesGateway tradeSpecialPricesGateway;

    @Mock
    private SantanderTradeSpecialPricesSerializer santanderTradeSpecialPricesSerializer;

    @InjectMocks
    private SantanderTradeSpecialPricesService santanderTradeSpecialPricesService;

    @Test
    void getSpecialPrices_isOk_returnsList() throws IOException {
        // Given
        SantanderTradeSpecialPricesResponse santanderTradeSpecialPricesResponse = mapper.readValue(new ClassPathResource("api-data/santander/special-prices-response.json").getFile(), SantanderTradeSpecialPricesResponse.class);
        List<TradeSpecialPrices> tradeSpecialPrices = mapper.readValue(new ClassPathResource("api-data/model/special-prices.json").getFile(), List.class);
        TradeSpecialPricesQuery query = new TradeSpecialPricesQuery();
        doReturn(santanderTradeSpecialPricesResponse).when(tradeSpecialPricesGateway).getSpecialPrices(query);
        doReturn(tradeSpecialPrices).when(santanderTradeSpecialPricesSerializer).toModel(santanderTradeSpecialPricesResponse, "es_es");
        // When
        List<TradeSpecialPrices> result = santanderTradeSpecialPricesService.getSpecialPrices(query, "es_es");
        // Then
        assertEquals(tradeSpecialPrices, result);
    }
}