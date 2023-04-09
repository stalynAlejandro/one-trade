package com.pagonxt.onetradefinance.work.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that provides properties to the configuration of the azure blob storage
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ConfigurationProperties(prefix = "santander.azure.blob")
public class AzureBlobContainerProperties {

    private boolean enabled;
    private String endpoint;
    private String containerName;
    private String clientId;
    private String clientSecret;
    private String tenantId;

    /**
     * Checks whether the azure blob storage customization is enabled.
     *
     * @return value of enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Setter method for field enabled
     *
     * @param enabled : value of enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Getter method for field endpoint
     *
     * @return value of endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Setter method for field endpoint
     *
     * @param endpoint : value of endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Getter method for field containerName
     *
     * @return value of containerName
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     * Setter method for field containerName
     *
     * @param containerName : value of containerName
     */
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    /**
     * Getter method for field clientId
     *
     * @return value of clientId
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * Setter method for field clientId
     *
     * @param clientId : value of clientId
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * Getter method for field clientSecret
     *
     * @return value of clientSecret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Setter method for field clientSecret
     *
     * @param clientSecret : value of clientSecret
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * Getter method for field tenantId
     *
     * @return value of tenantId
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * Setter method for field tenantId
     *
     * @param tenantId : value of tenantId
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
