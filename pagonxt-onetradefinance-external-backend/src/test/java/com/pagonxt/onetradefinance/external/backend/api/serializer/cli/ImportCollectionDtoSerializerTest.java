package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@UnitTest
class ImportCollectionDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ImportCollectionDtoSerializer serializer;

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
    void toDto_whenPassingTradeContract_returnsValidDto() throws Exception {
        // Given
        TradeContract tradeContract = mapper.readValue(new ClassPathResource("data/model/trade/import-collection-trade-contract.json").getFile(), TradeContract.class);
        ImportCollectionDto expectedImportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-dto.json").getFile(), ImportCollectionDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        Account expectedAccount = mapper.readValue(new ClassPathResource("./data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        when(accountService.getAccountById("account-id-1")).thenReturn(expectedAccount);
        when(accountDtoSerializer.toDto(expectedAccount)).thenReturn(expectedAccountDto);
        // When
        ImportCollectionDto result = serializer.toDto(tradeContract);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedImportCollectionDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenTradeContractNull_thenReturnsNullImportCollectionDto() {
        // Given
        // When
        ImportCollectionDto result = serializer.toDto((TradeContract) null);
        // Then
        assertNull(result);
    }

    @Test
    void toDto_whenTradeContractEmpty_thenReturnsEmptyImportCollectionDto() {
        // Given
        TradeContract tradeContract = new TradeContract();
        // When
        ImportCollectionDto result = serializer.toDto(tradeContract);
        // Then
        assertThat(result).isEqualTo(new ImportCollectionDto());
    }

    @Test
    void toDto_whenPassingMap_returnsValidDto() throws Exception {
        // Given
        @SuppressWarnings("unchecked")
        Map<String, Object> tradeContract = mapper.readValue(new ClassPathResource("data/model/trade/import-collection-trade-contract-map.json").getFile(), Map.class);
        ImportCollectionDto expectedImportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cli/import-collection-dto.json").getFile(), ImportCollectionDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        Account expectedAccount = mapper.readValue(new ClassPathResource("./data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        when(accountService.getAccountById("account-id-1")).thenReturn(expectedAccount);
        when(accountDtoSerializer.toDto(expectedAccount)).thenReturn(expectedAccountDto);
        // When
        ImportCollectionDto result = serializer.toDto(tradeContract);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedImportCollectionDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenMapNull_thenReturnsNullImportCollectionDto() {
        // Given
        // When
        ImportCollectionDto result = serializer.toDto((Map<String, Object>) null);
        // Then
        assertNull(result);
    }

    @Test
    void toDto_whenMapEmpty_thenReturnsEmptyImportCollectionDto() {
        // Given
        Map<String, Object> importCollectionMap = new HashMap<>();
        // When
        ImportCollectionDto result = serializer.toDto(importCollectionMap);
        // Then
        assertThat(result).isEqualTo(new ImportCollectionDto());
    }

}
