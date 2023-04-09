package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
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
class ExportCollectionAdvanceModificationRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExportCollectionAdvanceModificationRequestDtoSerializer serializer;

    @Mock
    private RiskLineDtoSerializer riskLineDtoSerializer;
    @Mock
    private CustomerDtoSerializer customerDtoSerializer;
    @Mock
    private DocumentationDtoSerializer documentationDtoSerializer;
    @Mock
    private ExportCollectionAdvanceDtoSerializer exportCollectionAdvanceDtoSerializer;
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
        ExportCollectionAdvanceModificationRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-modification-request-dto.json").getFile(), ExportCollectionAdvanceModificationRequestDto.class);
        ExportCollectionAdvanceModificationRequest expectedResult = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-modification-request.json").getFile(), ExportCollectionAdvanceModificationRequest.class);

        RiskLine expectedRiskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto expectedRiskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        when(riskLineDtoSerializer.toModel(expectedRiskLineDto)).thenReturn(expectedRiskLine);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        TypeReference<List<Document>> documentListType = new TypeReference<>() {};
        List<Document> documentList = mapper.readValue(new ClassPathResource("data/model/list-documents.json").getFile(), documentListType);
        when(documentationDtoSerializer.mapDocumentation(any())).thenReturn(documentList);

        ExportCollectionAdvance expectedExportCollectionAdvance = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance.json").getFile(), ExportCollectionAdvance.class);
        ExportCollectionAdvanceDto expectedExportCollectionAdvanceDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-dto.json").getFile(), ExportCollectionAdvanceDto.class);
        when(exportCollectionAdvanceDtoSerializer.toModel(expectedExportCollectionAdvanceDto)).thenReturn(expectedExportCollectionAdvance);
        // When
        ExportCollectionAdvanceModificationRequest result = serializer.toModel(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionAdvanceModificationRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceModificationRequest request = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-modification-request.json").getFile(), ExportCollectionAdvanceModificationRequest.class);
        ExportCollectionAdvanceModificationRequestDto expectedResult = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-modification-request-dto.json").getFile(), ExportCollectionAdvanceModificationRequestDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        RiskLine expectedRiskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto expectedRiskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        when(riskLineDtoSerializer.toDto(expectedRiskLine)).thenReturn(expectedRiskLineDto);

        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        when(documentationDtoSerializer.mapDocumentationDto(any())).thenReturn(expectedDocumentationDto);

        ExportCollectionAdvance expectedExportCollectionAdvance = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance.json").getFile(), ExportCollectionAdvance.class);
        ExportCollectionAdvanceDto expectedExportCollectionAdvanceDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-dto.json").getFile(), ExportCollectionAdvanceDto.class);
        when(exportCollectionAdvanceDtoSerializer.toDto(expectedExportCollectionAdvance)).thenReturn(expectedExportCollectionAdvanceDto);
        // When
        ExportCollectionAdvanceModificationRequestDto result = serializer.toDto(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given

        // When
        ExportCollectionAdvanceModificationRequestDto result = serializer.toDto(null);

        // Then
        assertNull(result, "Result should be null");
    }
}
