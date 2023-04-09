package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.CompleteInfoTradeRequestDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class CompleteInfoTradeRequestDtoSerializerTest {

    private static final String TIMEZONE = "Europe/Madrid";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Spy
    private static DateFormatProperties dateFormatProperties;

    @InjectMocks
    private CompleteInfoTradeRequestDtoSerializer serializer;

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone(TIMEZONE);
        dateFormatProperties.setDateFormat(DATE_FORMAT);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        mapper.setDateFormat(df);
    }

    @Test
    void toDto_ok_returnsValidDto() throws IOException {
        // Given
        CompleteInfoTradeRequestDto expectedCompleteInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoTradeRequestDto.class);
        TradeExternalTaskRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), TradeExternalTaskRequest.class);
        CompleteInfoTradeRequestDto newCompleteInfoDto = new CompleteInfoTradeRequestDto();
        // When
        CompleteInfoTradeRequestDto result = serializer.toDto(completeInfo, newCompleteInfoDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedCompleteInfoDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNullDto_thenReturnsNull() throws IOException {
        // Given
        TradeExternalTaskRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), TradeExternalTaskRequest.class);
        // When
        CompleteInfoTradeRequestDto result = serializer.toDto(completeInfo, null);
        // Then
        assertNull(result);
    }

    @Test
    void toDto_whenNullModel_thenReturnsSameObject() {
        // Given
        CompleteInfoTradeRequestDto completeInfoDto = new CompleteInfoTradeRequestDto();
        // When
        CompleteInfoTradeRequestDto result = serializer.toDto(null, completeInfoDto);
        // Then
        assertEquals(completeInfoDto, result);
    }

    @Test
    void toDto_whenRequestCreationTimeNull_returnsRequestCreationTimeNull() throws IOException {
        // Given
        TradeExternalTaskRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), TradeExternalTaskRequest.class);
        completeInfo.setRequestCreationTime(null);
        CompleteInfoTradeRequestDto newCompleteInfoDto = new CompleteInfoTradeRequestDto();
        // When
        CompleteInfoTradeRequestDto result = serializer.toDto(completeInfo, newCompleteInfoDto);
        // Then
        assertNull(result.getRequestCreationTime());
    }


    @Test
    void toDto_whenTaskCreationTimeNull_returnsTaskCreationTimeNull() throws IOException {
        // Given
        TradeExternalTaskRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), TradeExternalTaskRequest.class);
        completeInfo.setTaskCreationTime(null);
        CompleteInfoTradeRequestDto newCompleteInfoDto = new CompleteInfoTradeRequestDto();
        // When
        CompleteInfoTradeRequestDto result = serializer.toDto(completeInfo, newCompleteInfoDto);
        // Then
        assertNull(result.getTaskCreationTime());
    }
}
