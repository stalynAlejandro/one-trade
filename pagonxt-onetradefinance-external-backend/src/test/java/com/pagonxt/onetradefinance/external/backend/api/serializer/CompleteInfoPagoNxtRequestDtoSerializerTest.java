package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CompleteInfoPagoNxtRequestDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoPagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class CompleteInfoPagoNxtRequestDtoSerializerTest {

    private static final String TIMEZONE = "Europe/Madrid";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Spy
    private static DateFormatProperties dateFormatProperties;

    @InjectMocks
    private CompleteInfoPagoNxtRequestDtoSerializer serializer;

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
    void toModel_ok_returnsValidModel() throws IOException {
        // Given
        CompleteInfoPagoNxtRequest expectedCompleteInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), CompleteInfoPagoNxtRequest.class);
        CompleteInfoPagoNxtRequestDto completeInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoPagoNxtRequestDto.class);
        CompleteInfoPagoNxtRequest newCompleteInfo = new CompleteInfoPagoNxtRequest();
        // When
        CompleteInfoPagoNxtRequest result = serializer.toModel(completeInfoDto, newCompleteInfo);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedCompleteInfo);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNullModel_thenReturnsNull() throws IOException {
        // Given
        CompleteInfoPagoNxtRequestDto completeInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoPagoNxtRequestDto.class);
        // When
        CompleteInfoPagoNxtRequest result = serializer.toModel(completeInfoDto, null);
        // Then
        assertNull(result);
    }

    @Test
    void toModel_whenNullDto_thenReturnsSameObject() {
        // Given
        CompleteInfoPagoNxtRequest completeInfo = new CompleteInfoPagoNxtRequest();
        // When
        CompleteInfoPagoNxtRequest result = serializer.toModel(null, completeInfo);
        // Then
        assertEquals(completeInfo, result);
    }

    @Test
    void toModel_whenBlankRequestCreationTime_thenReturnsNullRequestCreationTime() throws IOException {
        // Given
        CompleteInfoPagoNxtRequestDto completeInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoPagoNxtRequestDto.class);
        completeInfoDto.setRequestCreationTime("");
        CompleteInfoPagoNxtRequest newCompleteInfo = new CompleteInfoPagoNxtRequest();
        // When
        CompleteInfoPagoNxtRequest result = serializer.toModel(completeInfoDto, newCompleteInfo);
        // Then
        assertNull(result.getRequestCreationTime());
    }

    @Test
    void toModel_whenNonParsableRequestCreationTime_thenThrowDateFormatException() throws IOException {
        // Given
        CompleteInfoPagoNxtRequestDto completeInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoPagoNxtRequestDto.class);
        completeInfoDto.setRequestCreationTime("nonParsable");
        CompleteInfoPagoNxtRequest newCompleteInfo = new CompleteInfoPagoNxtRequest();
        // When
        ServiceException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(completeInfoDto, newCompleteInfo));
        // Then
        assertEquals("unableToParseDate", exception.getKey());
        assertEquals("Unable to parse date", exception.getMessage());
    }

    @Test
    void toModel_whenBlankTaskCreationTime_thenReturnsNullTaskCreationTime() throws IOException {
        // Given
        CompleteInfoPagoNxtRequestDto completeInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoPagoNxtRequestDto.class);
        completeInfoDto.setTaskCreationTime("");
        CompleteInfoPagoNxtRequest newCompleteInfo = new CompleteInfoPagoNxtRequest();
        // When
        CompleteInfoPagoNxtRequest result = serializer.toModel(completeInfoDto, newCompleteInfo);
        // Then
        assertNull(result.getTaskCreationTime());
    }

    @Test
    void toModel_whenNonParsableTaskCreationTime_thenThrowDateFormatException() throws IOException {
        // Given
        CompleteInfoPagoNxtRequestDto completeInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoPagoNxtRequestDto.class);
        completeInfoDto.setTaskCreationTime("nonParsable");
        CompleteInfoPagoNxtRequest newCompleteInfo = new CompleteInfoPagoNxtRequest();
        // When
        ServiceException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(completeInfoDto, newCompleteInfo));
        // Then
        assertEquals("unableToParseDate", exception.getKey());
        assertEquals("Unable to parse date", exception.getMessage());
    }

    @Test
    void toDto_ok_returnsValidDto() throws IOException {
        // Given
        CompleteInfoPagoNxtRequestDto expectedCompleteInfoDto = mapper.readValue(new ClassPathResource("data/dto/complete-info-dto.json").getFile(), CompleteInfoPagoNxtRequestDto.class);
        CompleteInfoPagoNxtRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), CompleteInfoPagoNxtRequest.class);
        CompleteInfoPagoNxtRequestDto newCompleteInfoDto = new CompleteInfoPagoNxtRequestDto();
        // When
        CompleteInfoPagoNxtRequestDto result = serializer.toDto(completeInfo, newCompleteInfoDto);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedCompleteInfoDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNullDto_thenReturnsNull() throws IOException {
        // Given
        CompleteInfoPagoNxtRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), CompleteInfoPagoNxtRequest.class);
        // When
        CompleteInfoPagoNxtRequestDto result = serializer.toDto(completeInfo, null);
        // Then
        assertNull(result);
    }

    @Test
    void toDto_whenNullModel_thenReturnsSameObject() {
        // Given
        CompleteInfoPagoNxtRequestDto completeInfoDto = new CompleteInfoPagoNxtRequestDto();
        // When
        CompleteInfoPagoNxtRequestDto result = serializer.toDto(null, completeInfoDto);
        // Then
        assertEquals(completeInfoDto, result);
    }

    @Test
    void toDto_whenRequestCreationTimeNull_returnsRequestCreationTimeNull() throws IOException {
        // Given
        CompleteInfoPagoNxtRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), CompleteInfoPagoNxtRequest.class);
        completeInfo.setRequestCreationTime(null);
        CompleteInfoPagoNxtRequestDto newCompleteInfoDto = new CompleteInfoPagoNxtRequestDto();
        // When
        CompleteInfoPagoNxtRequestDto result = serializer.toDto(completeInfo, newCompleteInfoDto);
        // Then
        assertNull(result.getRequestCreationTime());
    }


    @Test
    void toDto_whenTaskCreationTimeNull_returnsTaskCreationTimeNull() throws IOException {
        // Given
        CompleteInfoPagoNxtRequest completeInfo = mapper.readValue(new ClassPathResource("data/model/complete-info.json").getFile(), CompleteInfoPagoNxtRequest.class);
        completeInfo.setTaskCreationTime(null);
        CompleteInfoPagoNxtRequestDto newCompleteInfoDto = new CompleteInfoPagoNxtRequestDto();
        // When
        CompleteInfoPagoNxtRequestDto result = serializer.toDto(completeInfo, newCompleteInfoDto);
        // Then
        assertNull(result.getTaskCreationTime());
    }
}
