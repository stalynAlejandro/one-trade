package com.pagonxt.onetradefinance.integrations.util;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * Class with some utilities for configuration
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ConfigurationUtils {

    /**
     * empty constructor method
     */
    private ConfigurationUtils(){}

    /**
     * class method fot http request
     * @param timeout an integer value with the timeout
     * @return a HttpComponentsClientHttpRequestFactory object
     */
    public static HttpComponentsClientHttpRequestFactory generateFactory(int timeout) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeout);
        clientHttpRequestFactory.setConnectionRequestTimeout(timeout);
        clientHttpRequestFactory.setReadTimeout(timeout);
        return clientHttpRequestFactory;
    }
}
