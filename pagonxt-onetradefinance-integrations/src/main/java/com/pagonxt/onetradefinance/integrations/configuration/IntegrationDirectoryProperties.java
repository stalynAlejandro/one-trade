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
@ConfigurationProperties(prefix = "one-trade.integrations.directory")
public class IntegrationDirectoryProperties {

    /**
     * Use a mock directory service instead of a real integration.
     * Useful for development. For production, it should be set to false
     * and proper configuration for the integration should be provided.
     */
    private boolean mockEnabled = true;
    private String fallbackOfficeUserEmail;
    private String fallbackMiddleOfficeEmail;
    private String fallbackOfficeEmail;

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
     *
     * @return fallback office user email
     */
    public String getFallbackOfficeUserEmail() {
        return fallbackOfficeUserEmail;
    }
    /**
     * @param fallbackOfficeUserEmail default email if no found
     */
    public void setFallbackOfficeUserEmail(String fallbackOfficeUserEmail) {
        this.fallbackOfficeUserEmail = fallbackOfficeUserEmail;
    }

    /**
     *
     * @return fallback middle office email
     */
    public String getFallbackMiddleOfficeEmail() {
        return fallbackMiddleOfficeEmail;
    }

    /**
     * @param fallbackMiddleOfficeEmail default email if no found
     */
    public void setFallbackMiddleOfficeEmail(String fallbackMiddleOfficeEmail) {
        this.fallbackMiddleOfficeEmail = fallbackMiddleOfficeEmail;
    }

    /**
     * Getter Method
     * @return a string with the fallback of office email
     */
    public String getFallbackOfficeEmail() {
        return fallbackOfficeEmail;
    }

    /**
     * Setter Method
     * @param fallbackOfficeEmail : a String with the fallback of Office email
     */
    public void setFallbackOfficeEmail(String fallbackOfficeEmail) {
        this.fallbackOfficeEmail = fallbackOfficeEmail;
    }
}
