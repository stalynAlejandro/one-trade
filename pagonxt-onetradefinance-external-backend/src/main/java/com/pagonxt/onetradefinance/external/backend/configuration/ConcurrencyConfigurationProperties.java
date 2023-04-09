package com.pagonxt.onetradefinance.external.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @see java.time.Duration
 * @since jdk-11.0.13
 */
@Configuration
@ConfigurationProperties(prefix = "pagonxt.concurrency-monitor")
public class ConcurrencyConfigurationProperties {

    /**
     * class attribute
     */
    private Duration threshold;

    /**
     * getter method
     * @return a Duration object with the threshold
     */
    public Duration getThreshold() {
        return threshold;
    }

    /**
     * setter method.
     * @param threshold a String with the treshold
     * Duration.parse accepts a representation on ISO-8601 format:
     * https://docs.oracle.com/en/java/javase/12/docs/api/java.base/java/time/Duration.html#parse
     *                  (java.lang.CharSequence)
     */
    public void setThreshold(String threshold) {
        this.threshold = Duration.parse(threshold);
    }
}
