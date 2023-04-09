package com.pagonxt.onetradefinance.integrations.apis.common.security.service;

import com.pagonxt.onetradefinance.integrations.apis.common.model.AccessTokenRespone;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.GOSTokenProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Component
public class GOSTokenGenericUserService {

    private static final Logger log = LoggerFactory.getLogger(GOSTokenGenericUserService.class);

    private final GOSTokenProperties gosTokenProperties;
    private final HttpHeaders httpHeadersToken;
    private final RestTemplate restTemplateToken;

    private String accessToken;
    private Instant expires;

    public GOSTokenGenericUserService(GOSTokenProperties gosTokenProperties,
                                      HttpHeaders httpHeadersToken,
                                      RestTemplate restTemplateToken) {
        this.gosTokenProperties = gosTokenProperties;
        this.httpHeadersToken = httpHeadersToken;
        this.restTemplateToken = restTemplateToken;
    }

    public String getAccessToken() {
        if (requireNewAccessToken()) {
            return getNewAccessToken();
        }
        return accessToken;
    }

    public String getNewAccessToken() {
        String url = gosTokenProperties.getUrl();
        MultiValueMap<String, String> requestBody = generateRequestBody();
        httpHeadersToken.setBasicAuth(gosTokenProperties.getClientId(), gosTokenProperties.getClientSecret());
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, httpHeadersToken);
        extractResult(restTemplateToken.exchange(url, HttpMethod.POST, requestEntity, AccessTokenRespone.class));
        return accessToken;
    }

    private boolean requireNewAccessToken() {
        if(accessToken == null || expires == null) {
            return true;
        }
        return Instant.now().isAfter(expires);
    }

    private MultiValueMap<String, String> generateRequestBody() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("scope", "basic");
        return requestBody;
    }

    private void extractResult(ResponseEntity<AccessTokenRespone> response) {
        AccessTokenRespone result = response.getBody();
        if (result == null) {
            throw new IntegrationException();
        }
        expires = Instant.now().plusSeconds(result.getExpiresIn() - 1);
        accessToken = result.getAccessToken();
        log.debug("Getting new generic user token GOS : {}", accessToken);
    }
}
