package com.pagonxt.onetradefinance.integrations.validation.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

/**
 * Configuration class that provides properties to the bean used in the validation of the JWT token.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ConfigurationProperties(prefix = "santander.token-validation")
public class JWTValidationProperties {

    /**
     * useV2 property
     */
    private boolean useV2;

    /**
     * Url property
     */
    private @NotNull URI url;

    /**
     * Audiences property
     */
    private List<String> audiences;

    /**
     * Issuers property
     */
    private List<String> issuers;

    /**
     * URL location, with file:// scheme.
     */
    private String trustStore;

    /**
     * The store password of the given trust store.
     */
    private char[] trustStorePassword;

    /**
     * One of the SSLContext algorithms
     */
    private String protocol;

    /**
     * Getter method for field useV2
     *
     * @return value of useV2
     */
    public boolean isUseV2() {
        return useV2;
    }
    /**
     * Setter method for field useV2
     *
     * @param useV2 : value of useV2
     */
    public void setUseV2(boolean useV2) {
        this.useV2 = useV2;
    }

    /**
     * Getter method for field url
     *
     * @return value of url
     */
    public URI getUrl() {
        return url;
    }

    /**
     * Setter method for field url
     *
     * @param url : value of url
     */
    public void setUrl(URI url) {
        this.url = url;
    }

    /**
     * Getter method for field audiences
     *
     * @return value of audiences
     */
    public List<String> getAudiences() {
        return audiences;
    }

    /**
     * Setter method for field audiences
     *
     * @param audiences : value of audiences
     */
    public void setAudiences(List<String> audiences) {
        this.audiences = audiences;
    }

    /**
     * Getter method for field issuers
     *
     * @return value of issuers
     */
    public List<String> getIssuers() {
        return issuers;
    }

    /**
     * Setter method for field issuers
     *
     * @param issuers : value of issuers
     */
    public void setIssuers(List<String> issuers) {
        this.issuers = issuers;
    }

    /**
     * Getter method for field trustStore
     *
     * @return value of trustStore
     */
    public String getTrustStore() {
        return trustStore;
    }

    /**
     * Setter method for field trustStore
     *
     * @param trustStore : value of trustStore
     */
    public void setTrustStore(String trustStore) {
        this.trustStore = trustStore;
    }

    /**
     * Getter method for field trustStorePassword
     *
     * @return value of trustStorePassword
     */
    public char[] getTrustStorePassword() {
        return trustStorePassword;
    }

    /**
     * Setter method for field trustStorePassword
     *
     * @param trustStorePassword : value of trustStorePassword
     */
    public void setTrustStorePassword(char[] trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    /**
     * Getter method for field protocol
     *
     * @return value of protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Setter method for field protocol
     *
     * @param protocol : value of protocol
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
