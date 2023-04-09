package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class RiskLineDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private RiskLineDtoSerializer serializer;

    @Spy
    private static DateFormatProperties dateFormatProperties;

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        dateFormatProperties.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(df);
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        RiskLine expectedRiskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto riskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        // When
        RiskLine result = serializer.toModel(riskLineDto);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedRiskLine);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNewRiskLine() {
        // Given
        RiskLine expectedResult = new RiskLine();
        // When
        RiskLine result = serializer.toModel(null);
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void toModel_wrongDateFormat_throwsDateFormatException() throws Exception {
        // Given
        RiskLineDto riskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        riskLineDto.setExpires("wrong-date-format");
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(riskLineDto));

        // Then
        assertEquals("Unable to parse date", exception.getMessage(), "Should throw exception with valid message");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        RiskLine riskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto expectedRiskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        // When
        RiskLineDto result = serializer.toDto(riskLine);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedRiskLineDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        RiskLineDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }
}
