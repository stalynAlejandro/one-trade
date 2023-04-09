package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.ExchangeInsuranceDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionAdvanceRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExportCollectionAdvanceRequestDtoSerializer serializer;

    @Mock
    private RiskLineDtoSerializer riskLineDtoSerializer;
    @Mock
    private CustomerDtoSerializer customerDtoSerializer;
    @Mock
    private DocumentationDtoSerializer documentationDtoSerializer;
    @Mock
    private ExchangeInsuranceDtoSerializer exchangeInsuranceDtoSerializer;
    @Mock
    private ExportCollectionDtoSerializer exportCollectionDtoSerializer;
    @Mock
    private CustomerService customerService;

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
        ExportCollectionAdvanceRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-request-dto.json").getFile(), ExportCollectionAdvanceRequestDto.class);
        ExportCollectionAdvanceRequest expectedResult = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-request.json").getFile(), ExportCollectionAdvanceRequest.class);

        RiskLine expectedRiskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto expectedRiskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        when(riskLineDtoSerializer.toModel(expectedRiskLineDto)).thenReturn(expectedRiskLine);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        TypeReference<List<Document>> documentListType = new TypeReference<>() {};
        List<Document> documentList = mapper.readValue(new ClassPathResource("data/model/list-documents.json").getFile(), documentListType);
        when(documentationDtoSerializer.mapDocumentation(any())).thenReturn(documentList);

        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toModel(expectedExportCollectionDto)).thenReturn(expectedExportCollection);

        ExchangeInsurance exchangeInsurance = mapper.readValue(new ClassPathResource("data/model/exchange-insurance.json").getFile(), ExchangeInsurance.class);
        when(exchangeInsuranceDtoSerializer.toModel(any())).thenReturn(exchangeInsurance);
        // When
        ExportCollectionAdvanceRequest result = serializer.toModel(request);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionAdvanceRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-request.json").getFile(), ExportCollectionAdvanceRequest.class);
        ExportCollectionAdvanceRequestDto expectedResult = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-request-dto.json").getFile(), ExportCollectionAdvanceRequestDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        RiskLine expectedRiskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto expectedRiskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        when(riskLineDtoSerializer.toDto(expectedRiskLine)).thenReturn(expectedRiskLineDto);

        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        when(documentationDtoSerializer.mapDocumentationDto(any())).thenReturn(expectedDocumentationDto);

        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toDto(expectedExportCollection)).thenReturn(expectedExportCollectionDto);

        ExchangeInsuranceDto exchangeInsuranceDto = mapper.readValue(new ClassPathResource("data/dto/exchange-insurance-dto.json").getFile(), ExchangeInsuranceDto.class);
        when(exchangeInsuranceDtoSerializer.toDto(any())).thenReturn(exchangeInsuranceDto);

        // When
        ExportCollectionAdvanceRequestDto result = serializer.toDto(request);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given

        // When
        ExportCollectionAdvanceRequestDto result = serializer.toDto(null);

        // Then
        assertNull(result, "Result should be null");
    }
}
