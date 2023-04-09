package com.pagonxt.onetradefinance.integrations.apis.account;

import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount;
import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccountListResponse;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationAccountProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class AccountGatewayTest {

    @InjectMocks
    private AccountGateway accountGateway;
    @Mock
    private IntegrationAccountProperties integrationAccountProperties;
    @Spy
    private HttpHeaders httpHeaders;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiTokenService apiTokenService;
    @Mock
    private JWTService jwtService;

    @Test
    void getCustomerAccounts_ok_thenInvokeRestTemplate() {
        // Given
        when(integrationAccountProperties.getUrl()).thenReturn("testUrl/v2/accounts");
        when(integrationAccountProperties.getLimit()).thenReturn(100);
        when(jwtService.getToken()).thenReturn("testJWTToken");
        SantanderAccountListResponse body = new SantanderAccountListResponse();
        ResponseEntity<SantanderAccountListResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderAccountListResponse.class))).thenReturn(response);
        // When
        accountGateway.getCustomerAccounts("customerIdTest");
        // Then
        verify(restTemplate, times(1)).exchange(
                eq("testUrl/v2/accounts?customer_id=customerIdTest&account_id_type=IBA&status=Open&_limit=100"),
                eq(HttpMethod.GET), any(), eq(SantanderAccountListResponse.class));
        verify(apiTokenService, times(1)).getAccessToken("testJWTToken", "acclist.read acclistcid.read");
    }

    @Test
    void getCustomerAccounts_whenTokenExpired_thenInvokeRestTemplateTwice() {
        // Given
        when(integrationAccountProperties.getUrl()).thenReturn("testUrl/v2/accounts");
        when(integrationAccountProperties.getLimit()).thenReturn(100);
        when(jwtService.getToken()).thenReturn("testJWTToken");
        SantanderAccountListResponse body = new SantanderAccountListResponse();
        ResponseEntity<SantanderAccountListResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderAccountListResponse.class)))
                .thenThrow(ExpiredTokenException.class)
                .thenReturn(response);
        // When
        accountGateway.getCustomerAccounts("customerIdTest");
        // Then
        verify(restTemplate, times(2)).exchange(
                eq("testUrl/v2/accounts?customer_id=customerIdTest&account_id_type=IBA&status=Open&_limit=100"),
                eq(HttpMethod.GET), any(), eq(SantanderAccountListResponse.class));
        verify(apiTokenService, times(2)).getAccessToken("testJWTToken", "acclist.read acclistcid.read");
    }

    @Test
    void getCustomerAccounts_whenNoContent_thenReturnNull() {
        // Given
        when(integrationAccountProperties.getUrl()).thenReturn("testUrl/v2/accounts");
        when(integrationAccountProperties.getLimit()).thenReturn(100);
        when(jwtService.getToken()).thenReturn("testJWTToken");
        ResponseEntity<SantanderAccountListResponse> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderAccountListResponse.class))).thenReturn(response);
        // When and Then
        SantanderAccountListResponse result = accountGateway.getCustomerAccounts("customerIdTest");
        // Then
        assertNull(result);
    }

    @Test
    void getCustomerAccounts_whenBodyNull_thenThrowIntegrationException() {
        // Given
        when(integrationAccountProperties.getUrl()).thenReturn("testUrl/v2/accounts");
        when(integrationAccountProperties.getLimit()).thenReturn(100);
        when(jwtService.getToken()).thenReturn("testJWTToken");
        ResponseEntity<SantanderAccountListResponse> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderAccountListResponse.class))).thenReturn(response);
        // When and Then
        assertThrows(IntegrationException.class, () -> accountGateway.getCustomerAccounts("customerIdTest"));
    }

    @Test
    void getAccountById_ok_thenInvokeRestTemplate() {
        // Given
        when(integrationAccountProperties.getUrl()).thenReturn("testUrl/v2/accounts");
        when(jwtService.getToken()).thenReturn("testJWTToken");
        SantanderAccount body = new SantanderAccount();
        ResponseEntity<SantanderAccount> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderAccount.class))).thenReturn(response);
        // When
        accountGateway.getAccountById("accountIdTest");
        // Then
        verify(restTemplate, times(1)).exchange(eq("testUrl/v2/accounts/accountIdTest"),
                eq(HttpMethod.GET), any(), eq(SantanderAccount.class));
        verify(apiTokenService, times(1)).getAccessToken("testJWTToken", "accdet.read accdetcid.read");
    }

    @Test
    void getAccountById_whenTokenExpired_thenInvokeRestTemplateTwice() {
        // Given
        when(integrationAccountProperties.getUrl()).thenReturn("testUrl/v2/accounts");
        when(jwtService.getToken()).thenReturn("testJWTToken");
        SantanderAccount body = new SantanderAccount();
        ResponseEntity<SantanderAccount> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderAccount.class)))
                .thenThrow(ExpiredTokenException.class)
                .thenReturn(response);
        // When
        accountGateway.getAccountById("accountIdTest");
        // Then
        verify(restTemplate, times(2)).exchange(
                eq("testUrl/v2/accounts/accountIdTest"), eq(HttpMethod.GET), any(), eq(SantanderAccount.class));
        verify(apiTokenService, times(2)).getAccessToken("testJWTToken", "accdet.read accdetcid.read");
    }

    @Test
    void getAccountById_whenBodyNull_thenThrowIntegrationException() {
        // Given
        when(integrationAccountProperties.getUrl()).thenReturn("testUrl/v2/accounts");
        when(jwtService.getToken()).thenReturn("testJWTToken");
        ResponseEntity<SantanderAccount> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderAccount.class))).thenReturn(response);
        // When and Then
        assertThrows(IntegrationException.class, () -> accountGateway.getAccountById("accountIdTest"));
    }
}
