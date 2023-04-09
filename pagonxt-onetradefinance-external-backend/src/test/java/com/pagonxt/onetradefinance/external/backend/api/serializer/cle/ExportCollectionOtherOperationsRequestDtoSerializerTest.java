package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionOtherOperationsRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionOtherOperationsRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExportCollectionOtherOperationsRequestDtoSerializer serializer;
    @Mock
    private CustomerDtoSerializer customerDtoSerializer;
    @Mock
    private DocumentationDtoSerializer documentationDtoSerializer;
    @Mock
    private ExportCollectionDtoSerializer exportCollectionDtoSerializer;
    @Mock
    private CustomerService customerService;

    @BeforeAll
    static void setup() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(df);
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionOtherOperationsRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-other-operations-request-dto.json").getFile(), ExportCollectionOtherOperationsRequestDto.class);
        ExportCollectionOtherOperationsRequest expectedResult = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-other-operations-request.json").getFile(), ExportCollectionOtherOperationsRequest.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        TypeReference<List<Document>> documentListType = new TypeReference<>() {};
        List<Document> documentList = mapper.readValue(new ClassPathResource("data/model/list-documents.json").getFile(), documentListType);
        when(documentationDtoSerializer.mapDocumentation(any())).thenReturn(documentList);

        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toModel(expectedExportCollectionDto)).thenReturn(expectedExportCollection);
        // When
        ExportCollectionOtherOperationsRequest result = serializer.toModel(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionOtherOperationsRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionOtherOperationsRequest request = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-other-operations-request.json").getFile(), ExportCollectionOtherOperationsRequest.class);
        ExportCollectionOtherOperationsRequestDto expectedResult = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-other-operations-request-dto.json").getFile(), ExportCollectionOtherOperationsRequestDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        when(documentationDtoSerializer.mapDocumentationDto(any())).thenReturn(expectedDocumentationDto);

        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toDto(expectedExportCollection)).thenReturn(expectedExportCollectionDto);
        // When
        ExportCollectionOtherOperationsRequestDto result = serializer.toDto(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given

        // When
        ExportCollectionOtherOperationsRequestDto result = serializer.toDto(null);

        // Then
        assertNull(result, "Result should be null");
    }
}

