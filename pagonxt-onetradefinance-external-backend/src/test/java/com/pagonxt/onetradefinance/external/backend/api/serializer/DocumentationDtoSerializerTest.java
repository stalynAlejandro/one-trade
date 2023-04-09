package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class DocumentationDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private DocumentationDtoSerializer serializer;

    @Spy
    private static DateFormatProperties dateFormatProperties;

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        dateFormatProperties.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    @Test
    void mapDocumentationDto_ok_returnsDocumentationDto() throws IOException {
        // Given
        PagoNxtRequest pagoNxtRequest = mapper.readValue(new ClassPathResource("data/model/pagonxt-request.json").getFile(), PagoNxtRequest.class);
        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        // When
        DocumentationDto result = serializer.mapDocumentationDto(pagoNxtRequest);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedDocumentationDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void mapDocumentationDto_whenNullDocumentation_thenReturnsEmptyArrayList() throws IOException {
        // Given
        PagoNxtRequest pagoNxtRequest = mapper.readValue(new ClassPathResource("data/model/pagonxt-request.json").getFile(), PagoNxtRequest.class);
        pagoNxtRequest.setDocuments(null);
        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        expectedDocumentationDto.setFiles(new ArrayList<>());
        // When
        DocumentationDto result = serializer.mapDocumentationDto(pagoNxtRequest);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedDocumentationDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void mapDocumentationDto_whenNullUploadedDate_thenReturnsNullUploadedDate() throws IOException {
        // Given
        PagoNxtRequest pagoNxtRequest = mapper.readValue(new ClassPathResource("data/model/pagonxt-request.json").getFile(), PagoNxtRequest.class);
        pagoNxtRequest.getDocuments().get(0).setUploadedDate(null);
        // When
        DocumentationDto result = serializer.mapDocumentationDto(pagoNxtRequest);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        assertTrue(resultJsonNode.get("files").get(0).get("uploadedDate").isNull());
    }

    @Test
    void mapDocumentation_ok_mapsDocumentation() throws IOException {
        // Given
        DocumentationDto documentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto-2.json").getFile(), DocumentationDto.class);
        Document expectedDocument = mapper.readValue(new ClassPathResource("data/model/document.json").getFile(), Document.class);
        // When
        List<Document> result = serializer.mapDocumentation(documentationDto);
        // Then
        JsonNode expectedJsonNode = mapper.valueToTree(expectedDocument);
        JsonNode resultJsonNode = mapper.valueToTree(result.get(0));
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void mapDocumentation_whenNullDocumentation_thenReturnsEmptyArrayDocumentation() throws IOException {
        // Given
        DocumentationDto documentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto-2.json").getFile(), DocumentationDto.class);
        documentationDto.setFiles(null);
        // When
        List<Document> documents = serializer.mapDocumentation(documentationDto);
        // Then
        assertEquals(0, documents.size());
    }

    @Test
    void mapDocumentation_whenNullUploadedDate_thenReturnsNullUploadedDate() throws IOException {
        // Given
        DocumentationDto documentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto-2.json").getFile(), DocumentationDto.class);
        documentationDto.getFiles().get(0).setUploadedDate(null);
        // When
        List<Document> documents = serializer.mapDocumentation(documentationDto);
        // Then
        assertNull(documents.get(0).getUploadedDate());
    }

    @Test
    void mapDocumentation_whenWrongDateFormat_throwDateFormatException() throws IOException {
        // Given
        DocumentationDto documentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        documentationDto.getFiles().get(0).setUploadedDate("wrong-date-format");
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.mapDocumentation(documentationDto));
        // Then
        assertEquals("Unable to parse date", exception.getMessage(), "Should throw exception with valid message");
    }

    @Test
    void mapDocumentationTradeDto_ok_returnsDocumentationDto() throws IOException {
        // Given
        TradeRequest tradeRequest = mapper.readValue(new ClassPathResource("data/model/pagonxt-request.json").getFile(), TradeRequest.class);
        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        // When
        DocumentationDto result = serializer.mapDocumentationTradeDto(tradeRequest);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedDocumentationDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void mapDocumentationTradeDto_whenNullDocumentation_thenReturnsEmptyArrayList() throws IOException {
        // Given
        TradeRequest tradeRequest = mapper.readValue(new ClassPathResource("data/model/pagonxt-request.json").getFile(), TradeRequest.class);
        tradeRequest.setDocuments(null);
        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        expectedDocumentationDto.setFiles(new ArrayList<>());
        // When
        DocumentationDto result = serializer.mapDocumentationTradeDto(tradeRequest);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedDocumentationDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void mapDocumentationTradeDto_whenNullUploadedDate_thenReturnsNullUploadedDate() throws IOException {
        // Given
        TradeRequest tradeRequest = mapper.readValue(new ClassPathResource("data/model/pagonxt-request.json").getFile(), TradeRequest.class);
        tradeRequest.getDocuments().get(0).setUploadedDate(null);
        // When
        DocumentationDto result = serializer.mapDocumentationTradeDto(tradeRequest);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        assertTrue(resultJsonNode.get("files").get(0).get("uploadedDate").isNull());
    }
}
