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
@ConfigurationProperties(prefix = "pagonxt.price-chart")
public class SecurityConfigurationAuthorProperties {

    /**
     * Class attribute
     */
    private String author;

    /**
     * getter method
     * @return the Author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * setter method
     * @param author a String with the autor
     */
    public void setAuthor(String author) {
        this.author = author;
    }
}
