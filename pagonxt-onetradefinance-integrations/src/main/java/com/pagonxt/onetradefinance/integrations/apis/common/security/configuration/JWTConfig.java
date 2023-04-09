package com.pagonxt.onetradefinance.integrations.apis.common.security.configuration;

import com.pagonxt.onetradefinance.integrations.apis.common.security.TokenErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.pagonxt.onetradefinance.integrations.constants.ApiConstants.KEEP_ALIVE;
import static com.pagonxt.onetradefinance.integrations.constants.ApiConstants.X_SANTANDER_CLIENT_ID;
import static com.pagonxt.onetradefinance.integrations.util.ConfigurationUtils.generateFactory;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.CONNECTION;

@Configuration
public class JWTConfig {

    private final JWTProperties jwtProperties;
    private final TokenErrorHandler tokenErrorHandler;

    public JWTConfig(JWTProperties jwtProperties, TokenErrorHandler tokenErrorHandler) {
        this.jwtProperties = jwtProperties;
        this.tokenErrorHandler = tokenErrorHandler;
    }

    @Bean
    public RestTemplate restTemplateJWT() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = generateFactory(jwtProperties.getTimeout());
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.setErrorHandler(tokenErrorHandler);
        return restTemplate;
    }

    @Bean
    public HttpHeaders httpHeadersJWT() {
        HttpHeaders entityHeaders = new HttpHeaders();
        entityHeaders.setContentType(MediaType.APPLICATION_JSON);
        entityHeaders.add(ACCEPT, "application/json");
        entityHeaders.add(CONNECTION, KEEP_ALIVE);
        entityHeaders.add(X_SANTANDER_CLIENT_ID, jwtProperties.getClientId());
        return entityHeaders;
    }
}
