package com.pagonxt.onetradefinance.work.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Model class for Elasticsearch configuration
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ConfigurationProperties(prefix = "one-trade.elastic")
public class ElasticsearchProperties {

    private boolean logResponseTimes = false;
    private boolean loadCertificates = false;
    private String keystore;
    private String keystorePass;
    private String truststore;

    /**
     * getter method
     *
     * @return whether to log the elasticsearch response times
     */
    public boolean isLogResponseTimes() {
        return logResponseTimes;
    }

    /**
     * setter method
     *
     * @param logResponseTimes whether to log the elasticsearch response times
     */
    public void setLogResponseTimes(boolean logResponseTimes) {
        this.logResponseTimes = logResponseTimes;
    }

    /**
     * getter method
     *
     * @return whether to load the elasticsearch certificates
     */
    public boolean isLoadCertificates() {
        return loadCertificates;
    }

    /**
     * setter method
     *
     * @param loadCertificates whether to load the elasticsearch certificates
     */
    public void setLoadCertificates(boolean loadCertificates) {
        this.loadCertificates = loadCertificates;
    }

    /**
     * Getter method for field keystore
     *
     * @return value of keystore
     */
    public String getKeystore() {
        return keystore;
    }

    /**
     * Setter method for field keystore
     *
     * @param keystore : value of keystore
     */
    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    /**
     * Getter method for field keyStorePass
     *
     * @return value of keyStorePass
     */
    public String getKeystorePass() {
        return keystorePass;
    }

    /**
     * Setter method for field keyStorePass
     *
     * @param keystorePass : value of keyStorePass
     */
    public void setKeystorePass(String keystorePass) {
        this.keystorePass = keystorePass;
    }

    /**
     * Getter method for field truststore
     *
     * @return value of truststore
     */
    public String getTruststore() {
        return truststore;
    }

    /**
     * Setter method for field truststore
     *
     * @param truststore : value of truststore
     */
    public void setTruststore(String truststore) {
        this.truststore = truststore;
    }
}
