package com.pagonxt.onetradefinance.integrations.configuration;

import com.pagonxt.onetradefinance.integrations.apis.common.ApiErrorHandler;
import com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.ApiSecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.pagonxt.onetradefinance.integrations.constants.ApiConstants.X_SANTANDER_CLIENT_ID;
import static com.pagonxt.onetradefinance.integrations.util.ConfigurationUtils.generateFactory;
import static org.springframework.http.HttpHeaders.ACCEPT;

/**
 * Configuration class to create beans for integration with Santander risk line (Api call)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.apis.common.ApiErrorHandler
 * @see com.pagonxt.onetradefinance.integrations.apis.common.security.configuration.ApiSecurityProperties
 * @see com.pagonxt.onetradefinance.integrations.configuration.IntegrationTradeSpecialPricesProperties
 * @since jdk-11.0.13
 */
@Configuration
public class IntegrationTradeSpecialPricesConfiguration {

    private final ApiErrorHandler apiErrorHandler;

    private final ApiSecurityProperties apiSecurityProperties;

    private final IntegrationTradeSpecialPricesProperties integrationTradeSpecialPricesProperties;


    /**
     * Constructor method
     * @param apiErrorHandler                           : ApiErrorHandler object
     * @param apiSecurityProperties                     : ApiSecurityProperties object
     * @param integrationTradeSpecialPricesProperties   : Properties for this class
     */
    public IntegrationTradeSpecialPricesConfiguration
                                    (ApiErrorHandler apiErrorHandler, ApiSecurityProperties apiSecurityProperties,
                                     IntegrationTradeSpecialPricesProperties integrationTradeSpecialPricesProperties) {
        this.apiErrorHandler = apiErrorHandler;
        this.apiSecurityProperties = apiSecurityProperties;
        this.integrationTradeSpecialPricesProperties = integrationTradeSpecialPricesProperties;
    }

    /**
     * Class method to generate a bean used in api calls
     * In this case returns a RestTemplate object, a Synchronous client to perform HTTP requests,
     * exposing a simple, template method API over underlying HTTP client libraries such as the
     * DK HttpURLConnection, Apache HttpComponents, and others.
     * @see org.springframework.web.client.RestTemplate
     * @return a RestTemplate object
     */
    @Bean
    public RestTemplate restTemplateTradeSpecialPrices() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                generateFactory(integrationTradeSpecialPricesProperties.getTimeout());
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        restTemplate.setErrorHandler(apiErrorHandler);
        return restTemplate;
    }

    /**
     * Class method to generate a bean used in api calls
     * In this case returns a HttpHeaders object, a data structure representing HTTP request
     * or response headers, mapping String header names to a list of String values,
     * also offering accessors for common application-level data types
     * @see org.springframework.http.HttpHeaders
     * @return an HttpHeaders object
     */
    @Bean
    public HttpHeaders httpHeaderTradeSpecialPrices() {
        HttpHeaders entityHeaders = new HttpHeaders();
        entityHeaders.add(X_SANTANDER_CLIENT_ID, apiSecurityProperties.getClientId());
        entityHeaders.setContentType(MediaType.APPLICATION_JSON);
        entityHeaders.add(ACCEPT, "application/json");
        return entityHeaders;
    }
}
