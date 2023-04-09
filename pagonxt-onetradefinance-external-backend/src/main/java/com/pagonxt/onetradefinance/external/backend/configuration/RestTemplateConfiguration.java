package com.pagonxt.onetradefinance.external.backend.configuration;

import com.pagonxt.onetradefinance.external.backend.service.handler.ControllerResponseErrorHandler;
import com.pagonxt.onetradefinance.integrations.service.TokenGeneratorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.configuration.RestTemplateProperties
 * @see com.pagonxt.onetradefinance.external.backend.service.handler.ControllerResponseErrorHandler
 * @since jdk-11.0.13
 */
@Configuration
public class RestTemplateConfiguration {

    public static final String APPLICATION_JSON = "application/json";
    /**
     * Class attributes
     */
    private final RestTemplateProperties restTemplateProperties;

    private final ControllerResponseErrorHandler controllerResponseErrorHandler;

    private final TokenGeneratorService tokenGeneratorService;

    /**
     * Class constructor
     *
     * @param restTemplateProperties         Thw RestTemplate properties object
     * @param controllerResponseErrorHandler The error handler for the controller response
     * @param tokenGeneratorService          The token generator service
     */
    public RestTemplateConfiguration(RestTemplateProperties restTemplateProperties,
                                     ControllerResponseErrorHandler controllerResponseErrorHandler,
                                     TokenGeneratorService tokenGeneratorService) {
        this.restTemplateProperties = restTemplateProperties;
        this.controllerResponseErrorHandler = controllerResponseErrorHandler;
        this.tokenGeneratorService = tokenGeneratorService;
    }

    /**
     * Class method
     * @return a RestTemplate object with a result
     * @see org.springframework.web.client.RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        int timeout = restTemplateProperties.getTimeout();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
        clientHttpRequestFactory.setReadTimeout(timeout);

        RestTemplate result = new RestTemplate(clientHttpRequestFactory);
        result.setErrorHandler(controllerResponseErrorHandler);
        return result;
    }

    /**
     * Class methods
     * @return a HttpHeaders object
     * @see org.springframework.http.HttpHeaders
     */
    @Bean
    public HttpHeaders httpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(restTemplateProperties.getUser(), restTemplateProperties.getPassword());
        headers.add("internalAppHeader",
                tokenGeneratorService.generateJWTToken(restTemplateProperties.getUser()));
        headers.add("Content-Type", APPLICATION_JSON);
        headers.add("Accept", APPLICATION_JSON);
        return headers;
    }
}
