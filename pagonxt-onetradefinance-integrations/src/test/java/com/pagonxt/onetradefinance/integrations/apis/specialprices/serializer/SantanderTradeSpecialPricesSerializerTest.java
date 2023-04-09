package com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.Concept;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SantanderTradeSpecialPricesResponse;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.service.PeriodicityService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@UnitTest
class SantanderTradeSpecialPricesSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Mock
    PeriodicityService periodicityService;
    @InjectMocks
    SantanderTradeSpecialPricesSerializer santanderTradeSpecialPricesSerializer;

    @Test
    void toModel_entryIsNull_returnsEmptyList() {
        // Given & When
        List<TradeSpecialPrices> result = santanderTradeSpecialPricesSerializer.toModel((SantanderTradeSpecialPricesResponse) null, "es_es");
        // Then
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void toModel_isOk_returnsList() throws IOException {
        // Given
        SantanderTradeSpecialPricesResponse santanderTradeSpecialPricesResponse = mapper.readValue(new ClassPathResource("api-data/santander/special-prices-response.json").getFile(), SantanderTradeSpecialPricesResponse.class);
        List<TradeSpecialPrices> tradeSpecialPrices = mapper.readValue(new ClassPathResource("api-data/model/special-prices.json").getFile(), List.class);
        doReturn("99999 D").when(periodicityService).getConceptPeriodicityTranslated(any(), eq("es_es"));
        // When
        List<TradeSpecialPrices> result = santanderTradeSpecialPricesSerializer.toModel(santanderTradeSpecialPricesResponse, "es_es");
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(tradeSpecialPrices);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_entryIsNull_returnEmptyTradeSpecialPrices() {
        // Given & When
        TradeSpecialPrices result = santanderTradeSpecialPricesSerializer.toModel((Concept) null, "es_es");
        // Then
        assertEquals(new TradeSpecialPrices(), result);
    }

    @Test
    void toModel_isOk_returnTradeSpecialPrices() throws IOException {
        // Given
        Concept concept = mapper.readValue(new ClassPathResource("api-data/santander/special-price.json").getFile(), Concept.class);
        TradeSpecialPrices specialPrice = mapper.readValue(new ClassPathResource("api-data/model/special-price.json").getFile(), TradeSpecialPrices.class);
        doReturn("99999 D").when(periodicityService).getConceptPeriodicityTranslated(any(), eq("es_es"));
        // When
        TradeSpecialPrices result = santanderTradeSpecialPricesSerializer.toModel(concept, "es_es");
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(specialPrice);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }
}