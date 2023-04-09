package com.pagonxt.onetradefinance.integrations.apis.riskline.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineList;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineResponse;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class SantanderRiskLineResponseSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private SantanderRiskLineSerializer serializer;

    @Test
    void toModelList_ok_returnsValidModel() throws Exception {
        // Given
        SantanderRiskLineListResponse santanderRiskLineResponse = mapper.readValue(new ClassPathResource("api-data/santander/risk-line-response.json").getFile(), SantanderRiskLineListResponse.class);
        List<RiskLine> expectedRiskLines = mapper.readValue(new ClassPathResource("api-data/model/risk-line-list.json").getFile(), List.class);

        // When
        List<RiskLine> result = serializer.toModel(santanderRiskLineResponse);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedRiskLines);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModelList_whenNull_thenReturnsEmptyList() {
        // Given and When
        List<com.pagonxt.onetradefinance.integrations.model.RiskLine> result = serializer.toModel((SantanderRiskLineListResponse) null);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void toModelList_whenNullList_thenReturnsEmptyList() {
        // Given and When
        List<com.pagonxt.onetradefinance.integrations.model.RiskLine> result = serializer.toModel(new SantanderRiskLineListResponse());
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        RiskLineList santanderRiskLineResponse = mapper.readValue(new ClassPathResource("api-data/santander/risk-line-byId-response.json").getFile(), RiskLineList.class);
        RiskLine expectedRiskLines = mapper.readValue(new ClassPathResource("api-data/model/risk-line.json").getFile(), RiskLine.class);
        // When
        RiskLine result = serializer.toModel(santanderRiskLineResponse);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedRiskLines);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsEmptyList() {
        // Given and When
        RiskLine result = serializer.toModel((RiskLineList) null);
        // Then
        assertNull(result.getRiskLineId());
    }

    @Test
    void toModel2_ok_returnsValidModel() throws Exception {
        // Given
        SantanderRiskLineResponse santanderRiskLineResponse = mapper.readValue(new ClassPathResource("api-data/santander/risk-line-byId-response.json").getFile(), SantanderRiskLineResponse.class);
        RiskLine expectedRiskLines = mapper.readValue(new ClassPathResource("api-data/model/risk-line-customer.json").getFile(), RiskLine.class);
        // When
        RiskLine result = serializer.toModel(santanderRiskLineResponse);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedRiskLines);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel2_whenNull_thenReturnsEmptyList() {
        // Given and When
        RiskLine result = serializer.toModel((SantanderRiskLineResponse) null);
        // Then
        assertNull(result.getRiskLineId());
    }
}