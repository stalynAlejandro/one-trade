package com.pagonxt.onetradefinance.integrations.apis.common.security.service.impl;

import com.pagonxt.onetradefinance.integrations.apis.common.model.JWTRequestBody;
import com.pagonxt.onetradefinance.integrations.apis.common.model.TokenResponse;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.JWTProperties;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.GOSTokenGenericUserService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.impl.JWTServiceGenericUserImpl;
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
class JWTServiceGenericUserImplTest {

    @InjectMocks
    JWTServiceGenericUserImpl jwtService;
    @Mock
    JWTProperties jwtProperties;
    @Spy
    HttpHeaders httpHeadersJWT;
    @Mock
    RestTemplate restTemplateJWT;
    @Mock
    GOSTokenGenericUserService gosTokenGenericUserService;

    @Test
    void getAccessToken_ok_returnAccessToken() {
        // Given
        when(jwtProperties.getUrl()).thenReturn("testUrl");
        JWTRequestBody jwtRequestBody = mock(JWTRequestBody.class);
        when(jwtProperties.getJWTRequestBody()).thenReturn(jwtRequestBody);
        TokenResponse token = new TokenResponse();
        token.setToken("testToken");
        ResponseEntity<TokenResponse> response = new ResponseEntity<>(token, HttpStatus.OK);
        when(restTemplateJWT.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenReturn(response);
        // When
        String result = jwtService.getToken();
        // Then
        assertEquals("testToken", result);
    }

    @Test
    void getAccessToken_tokenSaved_callApiOnce() {
        // Given
        when(jwtProperties.getUrl()).thenReturn("testUrl");
        JWTRequestBody jwtRequestBody = mock(JWTRequestBody.class);
        when(jwtProperties.getJWTRequestBody()).thenReturn(jwtRequestBody);
        TokenResponse validToken = new TokenResponse();
        validToken.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhYmNkMTIzIiwiZXhwIjo5OTk5OTk5OTk5fQ." +
                "NKgS1BimtYcUbnUZobjkuKQS3OqS908UsKV4nV_v4cg");
        ResponseEntity<TokenResponse> response = new ResponseEntity<>(validToken, HttpStatus.OK);
        when(restTemplateJWT.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenReturn(response);
        // When
        jwtService.getToken();
        jwtService.getToken();
        // Then
        verify(restTemplateJWT, times(1)).exchange(
                eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class));
    }

    @Test
    void getAccessToken_tokenExpired_callApiTwice() {
        // Given
        when(jwtProperties.getUrl()).thenReturn("testUrl");
        JWTRequestBody jwtRequestBody = mock(JWTRequestBody.class);
        when(jwtProperties.getJWTRequestBody()).thenReturn(jwtRequestBody);
        TokenResponse expiredToken = new TokenResponse();
        expiredToken.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJhYmNkMTIzIiwiZXhwIjoxNjQ2NjM1NjExfQ." +
                "xdXyWun9MUskp3uEwCqZ-XVrEAi1g1y0NTxwKz21Vk4");
        ResponseEntity<TokenResponse> response = new ResponseEntity<>(expiredToken, HttpStatus.OK);
        when(restTemplateJWT.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenReturn(response);
        // When
        jwtService.getToken();
        jwtService.getToken();
        // Then
        verify(restTemplateJWT, times(2)).exchange(
                eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class));
    }

    @Test
    void getAccessToken_tokenError_callApiTwice() {
        // Given
        when(jwtProperties.getUrl()).thenReturn("testUrl");
        JWTRequestBody jwtRequestBody = mock(JWTRequestBody.class);
        when(jwtProperties.getJWTRequestBody()).thenReturn(jwtRequestBody);
        TokenResponse errorToken = new TokenResponse();
        errorToken.setToken("ERROR.eW91ciBmaWxlIGNvbnRlbnRz.ERROR");
        ResponseEntity<TokenResponse> response = new ResponseEntity<>(errorToken, HttpStatus.OK);
        when(restTemplateJWT.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenReturn(response);
        // When
        jwtService.getToken();
        jwtService.getToken();
        // Then
        verify(restTemplateJWT, times(2)).exchange(
                eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class));
    }

    @Test
    void getAccessToken_responseNoContent_thenThrowIntegrationException() {
        // Given
        when(jwtProperties.getUrl()).thenReturn("testUrl");
        JWTRequestBody jwtRequestBody = mock(JWTRequestBody.class);
        when(jwtProperties.getJWTRequestBody()).thenReturn(jwtRequestBody);
        ResponseEntity<TokenResponse> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        when(restTemplateJWT.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenReturn(response);
        // When
        ServiceException exception = assertThrows(IntegrationException.class, () -> jwtService.getToken());
        // Then
        assertEquals("errorIntegration", exception.getKey());
        assertEquals("Error connecting with external API", exception.getMessage());
    }

    @Test
    void getAccessToken_whenExpiredTokenException_thenGetNewAccessToken() {
        // Given
        when(jwtProperties.getUrl()).thenReturn("testUrl");
        JWTRequestBody jwtRequestBody = mock(JWTRequestBody.class);
        when(jwtProperties.getJWTRequestBody()).thenReturn(jwtRequestBody);
        TokenResponse token = new TokenResponse();
        token.setToken("testToken");
        ResponseEntity<TokenResponse> response = new ResponseEntity<>(token, HttpStatus.OK);
        when(restTemplateJWT.exchange(eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class)))
                .thenThrow(ExpiredTokenException.class).thenReturn(response);
        // When
        String result = jwtService.getToken();
        // Then
        verify(gosTokenGenericUserService, times(1)).getNewAccessToken();
        verify(restTemplateJWT, times(2)).exchange(
                eq("testUrl"), eq(HttpMethod.POST), any(), eq(TokenResponse.class));
        assertEquals("testToken", result);
    }
}
