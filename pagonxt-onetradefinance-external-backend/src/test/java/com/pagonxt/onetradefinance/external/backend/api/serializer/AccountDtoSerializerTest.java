package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Account;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class AccountDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private AccountDtoSerializer serializer;

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        Account account = mapper.readValue(new ClassPathResource("data/model/account.json").getFile(), Account.class);
        AccountDto expectedAccountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        // When
        AccountDto result = serializer.toDto(account);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedAccountDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        AccountDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        Account expectedAccount = mapper.readValue(new ClassPathResource("data/model/account.json").getFile(), Account.class);
        AccountDto accountDto = mapper.readValue(new ClassPathResource("data/dto/account-dto.json").getFile(), AccountDto.class);
        // When
        Account result = serializer.toModel(accountDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedAccount);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNull() {
        // Given
        Account expectedResult = new Account();
        // When
        Account result = serializer.toModel(null);
        // Then
        assertEquals(expectedResult, result);
    }
}
