package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

@UnitTest
class ImportCollectionModificationRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ImportCollectionModificationRequestDtoSerializer serializer;

    @Mock
    private CustomerDtoSerializer customerDtoSerializer;
    @Mock
    private CustomerService customerService;
    @Mock
    private ImportCollectionDtoSerializer importCollectionDtoSerializer;
    @Mock
    private DocumentationDtoSerializer documentationDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        TradeRequest expectedTradeRequest = mapper.readValue(new ClassPathResource("data/model/trade/import-collection-modification-trade-request-2.json").getFile(), TradeRequest.class);
        ImportCollectionModificationRequestDto importCollectionModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-modification-request-dto.json").getFile(), ImportCollectionModificationRequestDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        TypeReference<List<Document>> documentListType = new TypeReference<>() {};
        List<Document> documentList = mapper.readValue(new ClassPathResource("data/model/list-documents.json").getFile(), documentListType);
        when(documentationDtoSerializer.mapDocumentation(any())).thenReturn(documentList);
        // When
        TradeRequest result = serializer.toModel(importCollectionModificationRequestDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedTradeRequest);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When and Then
        assertNull(serializer.toModel(null));
    }

    @Test
    void toModel_whenNullFields_thenReturnsNullFields() {
        // Given
        ImportCollectionModificationRequestDto request = new ImportCollectionModificationRequestDto();
        // When
        TradeRequest result = serializer.toModel(request);
        // Then
        assertTrue(result.getDocuments().isEmpty(), "List should be empty");
        assertNull(result.getCustomer(), "Field should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        TradeRequest tradeRequest = mapper.readValue(new ClassPathResource("data/model/trade/import-collection-modification-trade-request-1.json").getFile(), TradeRequest.class);
        ImportCollectionModificationRequestDto expectedImportCollectionModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-modification-request-dto.json").getFile(), ImportCollectionModificationRequestDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);

        ImportCollectionDto expectedImportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-dto.json").getFile(), ImportCollectionDto.class);
        when(importCollectionDtoSerializer.toDto(anyMap())).thenReturn(expectedImportCollectionDto);

        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        when(documentationDtoSerializer.mapDocumentationTradeDto(any())).thenReturn(expectedDocumentationDto);
        // When
        ImportCollectionModificationRequestDto result = serializer.toDto(tradeRequest);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedImportCollectionModificationRequestDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When and Then
        assertNull(serializer.toDto(null));
    }
}
