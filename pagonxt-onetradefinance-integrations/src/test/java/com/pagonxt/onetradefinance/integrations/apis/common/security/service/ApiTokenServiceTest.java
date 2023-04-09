package com.pagonxt.onetradefinance.integrations.apis.common.security.service;

import com.pagonxt.onetradefinance.integrations.apis.common.model.AccessTokenRespone;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.ApiSecurityProperties;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class ApiTokenServiceTest {

    @InjectMocks
    ApiTokenService apiTokenService;
    @Mock
    ApiSecurityProperties apiSecurityProperties;
    @Spy
    HttpHeaders httpHeadersToken;
    @Mock
    RestTemplate restTemplateToken;

    @Test
    void getAccessToken_ok_returnAccessToken() {
        // Given
        when(apiSecurityProperties.getUrl()).thenReturn("testUrl");
        when(apiSecurityProperties.getClientId()).thenReturn("testId");
        when(apiSecurityProperties.getCsec()).thenReturn("testSecret");
        AccessTokenRespone token = new AccessTokenRespone();
        token.setAccessToken("testToken");
        ResponseEntity<AccessTokenRespone> response = new ResponseEntity<>(token, HttpStatus.OK);
        when(restTemplateToken.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(AccessTokenRespone.class)))
                .thenReturn(response);
        // When
        String result = apiTokenService.getAccessToken("testJWTToken", "scope-test");
        // Then
        assertEquals("testToken", result);
    }

    @Test
    void getAccessToken_responseNoContent_thenThrowIntegrationException() {
        // Given
        when(apiSecurityProperties.getUrl()).thenReturn("testUrl");
        when(apiSecurityProperties.getClientId()).thenReturn("testId");
        when(apiSecurityProperties.getCsec()).thenReturn("testSecret");
        ResponseEntity<AccessTokenRespone> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        when(restTemplateToken.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(AccessTokenRespone.class)))
                .thenReturn(response);
        // When
        ServiceException exception = assertThrows(IntegrationException.class, () -> apiTokenService.getAccessToken("testJWTToken", "scope-test"));
        // Then
        assertEquals("errorIntegration", exception.getKey());
        assertEquals("Error connecting with external API", exception.getMessage());
    }
}
