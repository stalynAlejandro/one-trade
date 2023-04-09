package com.pagonxt.onetradefinance.external.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ConfigurationProperties(prefix = "pagonxt.jasper")
public class JasperConfigurationProperties {

    /**
     * Class attrbute
     */
    private String resourcePath;

    /**
     * getter method
     * @return the resource path
     */
    public String getResourcePath() {
        return resourcePath;
    }

    /**
     * setter method
     * @param resourcePath a string with the resource path
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}
