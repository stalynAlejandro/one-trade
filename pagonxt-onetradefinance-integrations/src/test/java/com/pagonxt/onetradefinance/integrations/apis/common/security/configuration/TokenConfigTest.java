package com.pagonxt.onetradefinance.integrations.apis.common.security.configuration;

import com.pagonxt.onetradefinance.integrations.apis.common.security.TokenErrorHandler;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@UnitTest
class TokenConfigTest {

    @InjectMocks
    JWTConfig jwtConfig;
    @Mock
    JWTProperties jwtProperties;
    @Mock
    TokenErrorHandler tokenErrorHandler;

    @Test
    void restTemplateJWT_ok_returnRestTemplate() {
        // Given
        when(jwtProperties.getTimeout()).thenReturn(100);
        // When
        RestTemplate result = jwtConfig.restTemplateJWT();
        // Then
        assertEquals(tokenErrorHandler, result.getErrorHandler());
    }

    @Test
    void httpHeadersJWT_ok_returnHttpHeaders() {
        // Given
        when(jwtProperties.getClientId()).thenReturn("clientTest");
        // When
        HttpHeaders result = jwtConfig.httpHeadersJWT();
        // Then
        List<String> headerClient = result.get("X-Santander-Client-Id");
        assertNotNull(headerClient);
        assertEquals(1, headerClient.size());
        assertEquals("clientTest", headerClient.get(0));
        assertEquals(MediaType.APPLICATION_JSON, result.getContentType());
        assertEquals(1, result.getAccept().size());
        assertEquals(MediaType.APPLICATION_JSON, result.getAccept().get(0));
        assertEquals(1, result.getConnection().size());
        assertEquals("keep-alive", result.getConnection().get(0));
    }
}
