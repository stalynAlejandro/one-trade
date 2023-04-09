package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExportCollectionDtoSerializer serializer;

    @Mock
    private CustomerDtoSerializer customerDtoSerializer;

    @Mock
    private CustomerService customerService;

    @Mock
    private AccountDtoSerializer accountDtoSerializer;

    @Mock
    private AccountService accountService;

    @Spy
    private static DateFormatProperties dateFormatProperties;

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        dateFormatProperties.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto exportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        Account expectedAccount = mapper.readValue(new ClassPathResource("./data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        when(accountDtoSerializer.toModel(expectedAccountDto)).thenReturn(expectedAccount);
        // When
        ExportCollection result = serializer.toModel(exportCollectionDto);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExportCollection);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNewExportCollection() {
        // Given
        ExportCollection expectedResult = new ExportCollection();
        // When
        ExportCollection result = serializer.toModel(null);
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void toModel_wrongDateFormat_throwsDateFormatException() throws Exception {
        // Given
        ExportCollectionDto exportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        exportCollectionDto.setCreationDate("non parsable date");
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(exportCollectionDto));

        // Then
        assertEquals("Unable to parse date", exception.getMessage(), "Should throw exception with valid message");
    }

    @Test
    void toModel_wrongApprovalDateFormat_throwsDateFormatException() throws Exception {
        // Given
        ExportCollectionDto exportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        exportCollectionDto.setApprovalDate("non parsable date");
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(exportCollectionDto));

        // Then
        assertEquals("Unable to parse date", exception.getMessage(), "Should throw exception with valid message");
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollection exportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        Account expectedAccount = mapper.readValue(new ClassPathResource("./data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        when(accountService.getAccountById("account-id-1")).thenReturn(expectedAccount);
        when(accountDtoSerializer.toDto(expectedAccount)).thenReturn(expectedAccountDto);
        // When
        ExportCollectionDto result = serializer.toDto(exportCollection);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExportCollectionDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }
}
