package com.pagonxt.onetradefinance.integrations.apis.common.security.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.common.model.JWTRequestBody;
import com.pagonxt.onetradefinance.integrations.apis.common.model.TokenResponse;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.JWTProperties;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.GOSTokenGenericUserService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Component
@Profile("!apigee-apis")
public class JWTServiceGenericUserImpl implements JWTService {

    private static final Logger log = LoggerFactory.getLogger(JWTServiceGenericUserImpl.class);

    private final JWTProperties jwtProperties;
    private final HttpHeaders httpHeadersJWT;
    private final RestTemplate restTemplateJWT;
    private final GOSTokenGenericUserService gosTokenGenericUserService;
    ObjectMapper mapper = new ObjectMapper();
    private String token;

    public JWTServiceGenericUserImpl(JWTProperties jwtProperties,
                                     HttpHeaders httpHeadersJWT,
                                     RestTemplate restTemplateJWT,
                                     GOSTokenGenericUserService gosTokenGenericUserService) {
        this.jwtProperties = jwtProperties;
        this.httpHeadersJWT = httpHeadersJWT;
        this.restTemplateJWT = restTemplateJWT;
        this.gosTokenGenericUserService = gosTokenGenericUserService;
    }

    public String getToken() {
        if (requireNewToken()) {
            return getNewToken();
        }
        return token;
    }

    private String getNewToken() {
        String url = jwtProperties.getUrl();
        httpHeadersJWT.setBearerAuth(gosTokenGenericUserService.getAccessToken());
        JWTRequestBody requestBody = jwtProperties.getJWTRequestBody();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, httpHeadersJWT);
        try {
            extractResult(restTemplateJWT.exchange(url, HttpMethod.POST, requestEntity, TokenResponse.class));
            return token;
        } catch (ExpiredTokenException e) {
            token = null;
            return retryGetNewToken();
        }
    }

    private String retryGetNewToken() {
        String url = jwtProperties.getUrl();
        httpHeadersJWT.setBearerAuth(gosTokenGenericUserService.getNewAccessToken());
        JWTRequestBody requestBody = jwtProperties.getJWTRequestBody();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, httpHeadersJWT);
        extractResult(restTemplateJWT.exchange(url, HttpMethod.POST, requestEntity, TokenResponse.class));
        return token;
    }

    private boolean requireNewToken() {
        if(token == null) {
            return true;
        }
        String[] tokenChunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(tokenChunks[1]));
        try {
            JsonNode actualObj = mapper.readTree(payload);
            long exp = actualObj.get("exp").asLong();
            return exp * 1000 <= System.currentTimeMillis();
        } catch (JsonProcessingException e) {
            log.error("Error parsing jwt token");
            return true;
        }
    }

    private void extractResult(ResponseEntity<TokenResponse> response) {
        TokenResponse result = response.getBody();
        if (result == null) {
            throw new IntegrationException();
        }
        token = result.getToken();
        log.debug("Getting new generic user token jwt : {}", token);
    }
}
