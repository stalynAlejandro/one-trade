package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
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
class ImportCollectionRequestDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ImportCollectionRequestDtoSerializer serializer;

    @Mock
    private CustomerDtoSerializer customerDtoSerializer;
    @Mock
    private AccountDtoSerializer accountDtoSerializer;
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
        ImportCollectionRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-request-dto.json").getFile(), ImportCollectionRequestDto.class);
        TradeRequest expectedResult = mapper.readValue(new ClassPathResource("data/model/trade/import-collection-trade-request.json").getFile(), TradeRequest.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        TypeReference<List<Document>> documentListType = new TypeReference<>() {};
        List<Document> documentList = mapper.readValue(new ClassPathResource("data/model/list-documents.json").getFile(), documentListType);
        when(documentationDtoSerializer.mapDocumentation(any())).thenReturn(documentList);
        // When
        TradeRequest result = serializer.toModel(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        // When
        TradeRequest result = serializer.toModel(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toModel_whenNullFields_thenReturnsNullFields() {
        // Given
        ImportCollectionRequestDto request = new ImportCollectionRequestDto();
        // When
        TradeRequest result = serializer.toModel(request);
        // Then
        assertTrue(result.getDocuments().isEmpty(), "List should be empty");
        assertNull(result.getCustomer(), "Field should be null");
    }

    @Test
    void toModel_wrongSlaEndDateFormat_throwsDateFormatException() throws Exception {
        // Given
        ImportCollectionRequestDto request = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-request-dto.json").getFile(), ImportCollectionRequestDto.class);
        request.setSlaEnd("wrong-date-format");
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(request));
        // Then
        assertEquals("Unable to parse date", exception.getMessage(), "Should throw exception with valid message");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        TradeRequest request = mapper.readValue(new ClassPathResource("data/model/trade/import-collection-trade-request.json").getFile(), TradeRequest.class);
        ImportCollectionRequestDto expectedResult = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-request-dto.json").getFile(), ImportCollectionRequestDto.class);

        Account expectedAccount = mapper.readValue(new ClassPathResource("data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        when(accountService.getAccountById("account-id-1")).thenReturn(expectedAccount);
        when(accountDtoSerializer.toDto(expectedAccount)).thenReturn(expectedAccountDto);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        DocumentationDto expectedDocumentationDto = mapper.readValue(new ClassPathResource("data/dto/documentation-dto.json").getFile(), DocumentationDto.class);
        when(documentationDtoSerializer.mapDocumentationTradeDto(any())).thenReturn(expectedDocumentationDto);
        // When
        ImportCollectionRequestDto result = serializer.toDto(request);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        ImportCollectionRequestDto result = serializer.toDto(null);
        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void toDto_whenNullFields_thenReturnsNullFields() {
        // Given
        TradeRequest tradeRequest = new TradeRequest();
        when(documentationDtoSerializer.mapDocumentationTradeDto(any())).thenReturn(new DocumentationDto());
        // When
        ImportCollectionRequestDto result = serializer.toDto(tradeRequest);
        // Then
        assertTrue(result.getDocumentation().getFiles().isEmpty(), "List should be empty");
        assertNull(result.getCustomer(), "Field should be null");
    }
}
