package com.pagonxt.onetradefinance.integrations.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that provides properties to the beans used in the integration with the Santander api
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ConfigurationProperties(prefix = "one-trade.integrations.fx-deal")
public class IntegrationFxDealProperties {

    /**
     * Use a mock fxDeal service instead of a real integration.
     * Useful for development. For production, it should be set to false
     * and proper configuration for the integration should be provided.
     */
    private boolean mockEnabled;
    private String url;
    private int timeout;
    private int limit;

    /**
     * getter method
     * @return true or false if we use mock data
     */
    public boolean isMockEnabled() {
        return mockEnabled;
    }

    /**
     * setter method
     * @param mockEnabled true or false to enable or disable mock data
     */
    public void setMockEnabled(boolean mockEnabled) {
        this.mockEnabled = mockEnabled;
    }

    /**
     * getter method
     * @return a string with the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * setter method
     * @param url a string with the url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * getter method
     * @return an integer value with the timeout
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * setter method
     * @param timeout an integer value with the timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    /**
     * getter method
     * @return an integer value with the time limit
     */
    public int getLimit() {
        return limit;
    }

    /**
     * setter method
     * @param limit an integer value with the time limit
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }
}
