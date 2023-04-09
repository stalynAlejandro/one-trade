package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExportCollectionRequestDtoSerializer serializer;

    @Mock
    private RiskLineDtoSerializer riskLineDtoSerializer;
    @Mock
    private AccountDtoSerializer accountDtoSerializer;
    @Mock
    private CustomerDtoSerializer customerDtoSerializer;
    @Mock
    private DocumentationDtoSerializer documentationDtoSerializer;
    @Mock
    private AccountService accountService;
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
        ExportCollectionRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-request-dto.json").getFile(), ExportCollectionRequestDto.class);
        ExportCollectionRequest expectedResult = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-request.json").getFile(), ExportCollectionRequest.class);

        RiskLine expectedRiskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto expectedRiskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        when(riskLineDtoSerializer.toModel(expectedRiskLineDto)).thenReturn(expectedRiskLine);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        Account expectedAccount = mapper.readValue(new ClassPathResource("./data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        when(accountDtoSerializer.toModel(expectedAccountDto)).thenReturn(expectedAccount);

        TypeReference<List<Document>> documentListType = new TypeReference<>() {};
        List<Document> documentList = mapper.readValue(new ClassPathResource("data/model/list-documents.json").getFile(), documentListType);
        when(documentationDtoSerializer.mapDocumentation(any())).thenReturn(documentList);
        // When
        ExportCollectionRequest result = serializer.toModel(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }
    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionRequest result = serializer.toModel(null);

        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toModel_wrongSlaEndDateFormat_throwsDateFormatException() throws Exception {
        // Given
        ExportCollectionRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-request-dto.json").getFile(), ExportCollectionRequestDto.class);
        request.setSlaEnd("wrong-date-format");
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(request));
        // Then
        assertEquals("Unable to parse date", exception.getMessage(), "Should throw exception with valid message");
    }

    @Test
    void toModel_wrongAdvanceExpirationDateFormat_throwsDateFormatException() throws Exception {
        // Given
        ExportCollectionRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-request-dto.json").getFile(), ExportCollectionRequestDto.class);
        request.getAdvance().setAdvanceExpiration("wrong-date-format");
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(request));
        // Then
        assertEquals("Unable to parse date", exception.getMessage(), "Should throw exception with valid message");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionRequest request = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-request.json").getFile(), ExportCollectionRequest.class);
        ExportCollectionRequestDto expectedResult = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-request-dto.json").getFile(), ExportCollectionRequestDto.class);

        Account expectedAccount = mapper.readValue(new ClassPathResource("data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        when(accountService.getAccountById("account-id-1")).thenReturn(expectedAccount);
        when(accountDtoSerializer.toDto(expectedAccount)).thenReturn(expectedAccountDto);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        RiskLine expectedRiskLine = mapper.readValue(new ClassPathResource("data/model/riskLine.json").getFile(), RiskLine.class);
        RiskLineDto expectedRiskLineDto = mapper.readValue(new ClassPathResource("data/dto/riskLine-dto.json").getFile(), RiskLineDto.class);
        when(riskLineDtoSerializer.toDto(expectedRiskLine)).thenReturn(expectedRiskLineDto);

        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        when(documentationDtoSerializer.mapDocumentationDto(any())).thenReturn(expectedDocumentationDto);
        // When
        ExportCollectionRequestDto result = serializer.toDto(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }
}
