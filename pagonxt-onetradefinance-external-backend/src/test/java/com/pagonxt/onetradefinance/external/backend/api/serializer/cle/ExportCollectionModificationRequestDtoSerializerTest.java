package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionModificationRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExportCollectionModificationRequestDtoSerializer serializer;

    @Mock
    private CustomerDtoSerializer customerDtoSerializer;

    @Mock
    private CustomerService customerService;

    @Mock
    private ExportCollectionDtoSerializer exportCollectionDtoSerializer;

    @Mock
    private DocumentationDtoSerializer documentationDtoSerializer;

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionModificationRequest expectedExportCollectionModificationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-modification-request.json").getFile(), ExportCollectionModificationRequest.class);
        ExportCollectionModificationRequestDto exportCollectionModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-modification-request-dto.json").getFile(), ExportCollectionModificationRequestDto.class);
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);
        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toModel(expectedExportCollectionDto)).thenReturn(expectedExportCollection);
        TypeReference<List<Document>> documentListType = new TypeReference<>() {};
        List<Document> documentList = mapper.readValue(new ClassPathResource("data/model/list-documents.json").getFile(), documentListType);
        when(documentationDtoSerializer.mapDocumentation(any())).thenReturn(documentList);
        // When
        ExportCollectionModificationRequest result = serializer.toModel(exportCollectionModificationRequestDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExportCollectionModificationRequest);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNewExportCollection() {
        // Given
        // When
        ExportCollectionModificationRequest result = serializer.toModel(null);
        // Then
        assertNull(result);
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionModificationRequest exportCollectionModificationRequest = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-modification-request.json").getFile(), ExportCollectionModificationRequest.class);
        ExportCollectionModificationRequestDto expectedExportCollectionModificationRequestDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-modification-request-dto.json").getFile(), ExportCollectionModificationRequestDto.class);
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toDto(expectedExportCollection)).thenReturn(expectedExportCollectionDto);
        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        when(documentationDtoSerializer.mapDocumentationDto(any())).thenReturn(expectedDocumentationDto);

        // When
        ExportCollectionModificationRequestDto result = serializer.toDto(exportCollectionModificationRequest);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExportCollectionModificationRequestDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionModificationRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }
}
