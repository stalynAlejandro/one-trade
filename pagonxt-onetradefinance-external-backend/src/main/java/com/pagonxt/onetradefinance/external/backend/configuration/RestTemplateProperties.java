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
@ConfigurationProperties(prefix = "rest-template")
public class RestTemplateProperties {

    /**
     * Connection timeout.
     */
    private Integer timeout;

    /**
     * Flowable work root.
     */
    private String flowableWorkUrl;

    /**
     * Credentials external App.
     */
    private String user;
    private String password;

    /**
     * getter method
     * @return timeout
     */
    public Integer getTimeout() {
        return timeout;
    }

    /**
     * setter method
     * @param timeout a integer value with timeout
     */
    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    /**
     * getter method
     * @return the URL of flowable work
     */
    public String getFlowableWorkUrl() {
        return flowableWorkUrl;
    }

    /**
     * setter method
     * @param flowableWorkUrl a string with the URL of flowable work
     */
    public void setFlowableWorkUrl(String flowableWorkUrl) {
        this.flowableWorkUrl = flowableWorkUrl;
    }

    /**
     * getter method
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * setter method
     * @param user a string with the user id
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Getter method for field password
     *
     * @return value of password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter method for field password
     *
     * @param password : value of password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
