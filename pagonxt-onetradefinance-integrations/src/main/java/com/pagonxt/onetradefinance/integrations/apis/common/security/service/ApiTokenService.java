package com.pagonxt.onetradefinance.integrations.apis.common.security.service;

import com.pagonxt.onetradefinance.integrations.apis.common.model.AccessTokenRespone;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.ApiSecurityProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiTokenService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(ApiTokenService.class);

    private final ApiSecurityProperties apiSecurityProperties;
    private final HttpHeaders httpHeadersToken;
    private final RestTemplate restTemplateToken;

    public ApiTokenService(ApiSecurityProperties apiSecurityProperties,
                           HttpHeaders httpHeadersToken,
                           RestTemplate restTemplateToken) {
        this.apiSecurityProperties = apiSecurityProperties;
        this.httpHeadersToken = httpHeadersToken;
        this.restTemplateToken = restTemplateToken;
    }

    @Cacheable("apiToken")
    public String getAccessToken(String jwtToken, String scope) {
        MultiValueMap<String, String> requestBody = generateRequestBody(scope, jwtToken);
        httpHeadersToken.setBasicAuth(apiSecurityProperties.getClientId(), apiSecurityProperties.getCsec());
        HttpEntity<?> requestEntity = new HttpEntity<>(requestBody, httpHeadersToken);
        return extractResult(restTemplateToken.exchange(apiSecurityProperties.getUrl(),
                    HttpMethod.POST, requestEntity, AccessTokenRespone.class));
    }

    @CacheEvict("apiToken")
    public void evictAccessToken(String jwtToken, String scope) {
        LOG.debug("Evicting access token for jwtToken [{}] and scope [{}]", jwtToken, scope);
    }

    private MultiValueMap<String, String> generateRequestBody(String scope, String jwtToken) {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer");
        requestBody.add("scope", scope);
        // TODO : This token will be provided by the client
        requestBody.add("assertion", jwtToken);
        requestBody.add("country", apiSecurityProperties.getCountry());
        return requestBody;
    }

    private String extractResult(ResponseEntity<AccessTokenRespone> response) {
        AccessTokenRespone result = response.getBody();
        if (result == null) {
            throw new IntegrationException();
        }
        LOG.debug("Getting new access token: [{}]", result.getAccessToken());
        return result.getAccessToken();
    }
}
