package com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealDetails;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.SantanderFxDealResponse;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class SantanderFxDealSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static SantanderFxDealSerializer serializer;

    @BeforeAll
    public static void setup() {
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        serializer = new SantanderFxDealSerializer(dateFormatProperties);
    }

    @Test
    void toModelResponse_ok_returnsValidModel() throws Exception {
        // Given
        TypeReference<List<ExchangeInsurance>> exchangeInsuranceType = new TypeReference<>() {
        };
        SantanderFxDealResponse santanderFxDealResponse = mapper.readValue(new ClassPathResource("api-data/santander/fx-deal-response.json").getFile(), SantanderFxDealResponse.class);
        List<ExchangeInsurance> expectedExchangeInsurances = mapper.readValue(new ClassPathResource("api-data/model/exchange-insurances.json").getFile(), exchangeInsuranceType);
        // When
        List<ExchangeInsurance> result = serializer.toModel(santanderFxDealResponse);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExchangeInsurances);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModelResponse_whenNull_thenReturnsEmptyList() {
        // Given and When
        List<ExchangeInsurance> result = serializer.toModel((SantanderFxDealResponse) null);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void toModelResponse_whenNullList_thenReturnsEmptyList() {
        // Given and When
        List<ExchangeInsurance> result = serializer.toModel(new SantanderFxDealResponse());
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        FxDealDetails fxDealDetails = mapper.readValue(new ClassPathResource("api-data/santander/fx-deal.json").getFile(), FxDealDetails.class);
        ExchangeInsurance expectedExchangeInsurance = mapper.readValue(new ClassPathResource("api-data/model/exchange-insurance.json").getFile(), ExchangeInsurance.class);
        // When
        ExchangeInsurance result = serializer.toModel(fxDealDetails);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExchangeInsurance);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenWrongDate_returnsDateFormatException() throws Exception {
        // Given
        FxDealDetails fxDealDetails = mapper.readValue(new ClassPathResource("api-data/santander/fx-deal.json").getFile(), FxDealDetails.class);
        fxDealDetails.setFxDealUseDate("unparsable");
        // When
        ServiceException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(fxDealDetails));
        // Then
        assertEquals("Unable to parse date", exception.getMessage());
        assertEquals("unableToParseDate", exception.getKey());
    }

    @Test
    void toModel_whenNull_thenReturnsNewObject() {
        // Given
        ExchangeInsurance exchangeInsurance = new ExchangeInsurance();
        // When
        ExchangeInsurance result = serializer.toModel((FxDealDetails) null);
        // Then
        assertEquals(exchangeInsurance, result);
    }
}
