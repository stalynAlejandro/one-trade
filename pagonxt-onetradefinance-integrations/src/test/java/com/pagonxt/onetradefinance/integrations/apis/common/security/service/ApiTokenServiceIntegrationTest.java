package com.pagonxt.onetradefinance.integrations.apis.common.security.service;

import com.pagonxt.onetradefinance.integrations.apis.common.model.AccessTokenRespone;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.ApiSecurityProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration
class ApiTokenServiceIntegrationTest {

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        ApiSecurityProperties apiSecurityProperties() {
            return mock(ApiSecurityProperties.class);
        }

        @Bean
        HttpHeaders httpHeadersToken() {
            return spy(HttpHeaders.class);
        }

        @Bean
        RestTemplate restTemplateToken() {
            return mock(RestTemplate.class);
        }

        @Bean
        ApiTokenService apiTokenService(ApiSecurityProperties apiSecurityProperties,
                                        HttpHeaders httpHeadersToken,
                                        RestTemplate restTemplateToken) {
            return new ApiTokenService(apiSecurityProperties, httpHeadersToken, restTemplateToken);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("apiToken");
        }
    }

    @Autowired
    ApiTokenService apiTokenService;
    @Autowired
    ApiSecurityProperties apiSecurityProperties;
    @Autowired
    RestTemplate restTemplateToken;

    @Test
    void getAccessToken_callTwice_callRestTemplateOnce() {
        // Given
        when(apiSecurityProperties.getUrl()).thenReturn("testUrl");
        when(apiSecurityProperties.getClientId()).thenReturn("testId");
        when(apiSecurityProperties.getCsec()).thenReturn("testSecret");
        AccessTokenRespone token = new AccessTokenRespone();
        token.setAccessToken("testToken");
        ResponseEntity<AccessTokenRespone> response = new ResponseEntity<>(token, HttpStatus.OK);
        when(restTemplateToken.exchange(eq("testUrl"),
                eq(HttpMethod.POST), any(), eq(AccessTokenRespone.class))).thenReturn(response);
        // When
        apiTokenService.getAccessToken("testJWTToken", "testScope");
        apiTokenService.getAccessToken("testJWTToken", "testScope");
        // Then
        verify(restTemplateToken, times(1)).exchange(eq("testUrl"),
                eq(HttpMethod.POST), any(), eq(AccessTokenRespone.class));
    }

    @Test
    void evictAccessToken_ok_cacheRecordIsDeleted() {
        // Given
        when(apiSecurityProperties.getUrl()).thenReturn("testUrl");
        when(apiSecurityProperties.getClientId()).thenReturn("testId");
        when(apiSecurityProperties.getCsec()).thenReturn("testSecret");
        AccessTokenRespone token = new AccessTokenRespone();
        token.setAccessToken("testToken");
        ResponseEntity<AccessTokenRespone> response = new ResponseEntity<>(token, HttpStatus.OK);
        when(restTemplateToken.exchange(eq("testUrl"),
                eq(HttpMethod.POST), any(), eq(AccessTokenRespone.class))).thenReturn(response);
        // When
        apiTokenService.getAccessToken("testJWTToken", "testScope");
        apiTokenService.evictAccessToken("testJWTToken", "testScope");
        apiTokenService.getAccessToken("testJWTToken", "testScope");
        // Then
        verify(restTemplateToken, times(2)).exchange(eq("testUrl"),
                eq(HttpMethod.POST), any(), eq(AccessTokenRespone.class));
    }

}
