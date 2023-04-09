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
@ConfigurationProperties(prefix = "one-trade.integrations.office")
public class IntegrationOfficeProperties {

    /**
     * Use a mock office service instead of a real integration.
     * Useful for development. For production, it should be set to false
     * and proper configuration for the integration should be provided.
     */
    private boolean mockEnabled = true;

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
}
