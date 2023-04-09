package com.pagonxt.onetradefinance.integrations.apis.account.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount;
import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccountListResponse;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Account;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class SantanderAccountSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private SantanderAccountSerializer serializer;

    @Test
    void toModelList_ok_returnsValidModel() throws Exception {
        // Given
        SantanderAccountListResponse santanderAccountListResponse = mapper.readValue(new ClassPathResource("api-data/santander/account-list-response.json").getFile(), SantanderAccountListResponse.class);
        List<Account> expectedAccounts = mapper.readValue(new ClassPathResource("api-data/model/accounts.json").getFile(), List.class);
        // When
        List<Account> result = serializer.toModel(santanderAccountListResponse);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedAccounts);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModelList_whenNull_thenReturnsEmptyList() {
        // Given and When
        List<Account> result = serializer.toModel((SantanderAccountListResponse) null);
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void toModelList_whenNullList_thenReturnsEmptyList() {
        // Given and When
        List<Account> result = serializer.toModel(new SantanderAccountListResponse());
        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        SantanderAccount santanderAccount = mapper.readValue(new ClassPathResource("api-data/santander/account-response.json").getFile(), SantanderAccount.class);
        Account expectedAccount = mapper.readValue(new ClassPathResource("api-data/model/account.json").getFile(), Account.class);
        // When
        Account result = serializer.toModel(santanderAccount);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedAccount);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenMainBalanceNull_returnsCurrencyNull() throws Exception {
        // Given
        SantanderAccount santanderAccount = mapper.readValue(new ClassPathResource("api-data/santander/account-response.json").getFile(), SantanderAccount.class);
        santanderAccount.setMainBalance(null);
        // When
        Account result = serializer.toModel(santanderAccount);
        // Then
        assertNull(result.getCurrency());
    }

    @Test
    void toModel_whenNull_thenReturnsNewObject() {
        // Given
        Account expectedResult = new Account();
        // When
        Account result = serializer.toModel((SantanderAccount) null);
        // Then
        assertEquals(expectedResult, result);
    }
}
