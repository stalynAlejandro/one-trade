package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.account.AccountGateway;
import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount;
import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccountListResponse;
import com.pagonxt.onetradefinance.integrations.apis.account.serializer.SantanderAccountSerializer;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Account;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@UnitTest
class SantanderAccountServiceTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private SantanderAccountService santanderAccountService;
    @Mock
    private AccountGateway accountGateway;
    @Mock
    private SantanderAccountSerializer santanderAccountSerializer;

    @Test
    void getCustomerAccounts_ok_callAccountGateway() throws IOException {
        // Given
        SantanderAccountListResponse santanderAccountListResponse = mapper.readValue(new ClassPathResource("api-data/santander/account-list-response.json").getFile(), SantanderAccountListResponse.class);
        List<Account> expectedAccounts = mapper.readValue(new ClassPathResource("api-data/model/accounts.json").getFile(), List.class);
        when(accountGateway.getCustomerAccounts("customerId")).thenReturn(santanderAccountListResponse);
        when(santanderAccountSerializer.toModel(santanderAccountListResponse)).thenReturn(expectedAccounts);
        // When
        List<Account> result = santanderAccountService.getCustomerAccounts("customerId");
        // Then
        assertEquals(expectedAccounts, result);
    }

    @Test
    void testGetAccountById() throws IOException {
        // Given
        SantanderAccount santanderAccount = mapper.readValue(new ClassPathResource("api-data/santander/account-response.json").getFile(), SantanderAccount.class);
        Account expectedAccount = mapper.readValue(new ClassPathResource("api-data/model/account.json").getFile(), Account.class);
        when(accountGateway.getAccountById("accountId")).thenReturn(santanderAccount);
        when(santanderAccountSerializer.toModel(santanderAccount)).thenReturn(expectedAccount);
        // When
        Account result = santanderAccountService.getAccountById("accountId");
        // Then
        assertEquals(expectedAccount, result);
    }
}
