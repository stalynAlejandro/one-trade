package com.pagonxt.onetradefinance.integrations.apis.common.security.configuration;

import com.pagonxt.onetradefinance.integrations.apis.common.security.TokenErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.pagonxt.onetradefinance.integrations.constants.ApiConstants.KEEP_ALIVE;
import static com.pagonxt.onetradefinance.integrations.util.ConfigurationUtils.generateFactory;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONNECTION;

@Configuration
public class TokenConfig {

    private final GOSTokenProperties gosTokenProperties;
    private final TokenErrorHandler tokenErrorHandler;

    public TokenConfig(GOSTokenProperties gosTokenProperties, TokenErrorHandler tokenErrorHandler) {
        this.gosTokenProperties = gosTokenProperties;
        this.tokenErrorHandler = tokenErrorHandler;
    }

    @Bean
    public RestTemplate restTemplateToken() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = generateFactory(gosTokenProperties.getTimeout());
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.setErrorHandler(tokenErrorHandler);
        return restTemplate;
    }

    @Bean
    public HttpHeaders httpHeadersToken() {
        HttpHeaders entityHeaders = new HttpHeaders();
        entityHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        entityHeaders.add(ACCEPT, "application/json");
        entityHeaders.add(CONNECTION, KEEP_ALIVE);
        return entityHeaders;
    }
}
