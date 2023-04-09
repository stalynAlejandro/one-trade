package com.pagonxt.onetradefinance.integrations.configuration;

import com.pagonxt.onetradefinance.integrations.apis.common.ApiErrorHandler;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.ApiSecurityProperties;
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
class IntegrationTradeSpecialPricesConfigurationTest {

    @InjectMocks
    IntegrationTradeSpecialPricesConfiguration integrationTradeSpecialPricesConfiguration;
    @Mock
    ApiErrorHandler apiErrorHandler;
    @Mock
    ApiSecurityProperties apiSecurityProperties;
    @Mock
    IntegrationTradeSpecialPricesProperties integrationTradeSpecialPricesProperties;

    @Test
    void restTemplateTradeSpecialPrices_ok_returnRestTemplate() {
        // Given
        when(integrationTradeSpecialPricesProperties.getTimeout()).thenReturn(100);
        // When
        RestTemplate result = integrationTradeSpecialPricesConfiguration.restTemplateTradeSpecialPrices();
        // Then
        assertEquals(apiErrorHandler, result.getErrorHandler());
    }

    @Test
    void httpHeadersTradeSpecialPrices_ok_returnHttpHeaders() {
        // Given
        when(apiSecurityProperties.getClientId()).thenReturn("clientTest");
        // When
        HttpHeaders result = integrationTradeSpecialPricesConfiguration.httpHeaderTradeSpecialPrices();
        // Then
        List<String> headerClient = result.get("X-Santander-Client-Id");
        assertNotNull(headerClient);
        assertEquals(1, headerClient.size());
        assertEquals("clientTest", headerClient.get(0));
        assertEquals(MediaType.APPLICATION_JSON, result.getContentType());
        assertEquals(1, result.getAccept().size());
        assertEquals(MediaType.APPLICATION_JSON, result.getAccept().get(0));
    }
}
