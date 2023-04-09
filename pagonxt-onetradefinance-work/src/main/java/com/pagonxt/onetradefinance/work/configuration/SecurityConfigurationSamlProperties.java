package com.pagonxt.onetradefinance.work.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Model class for Security Configuration SAML
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@ConfigurationProperties(prefix = "pagonxt.saml2")
public class SecurityConfigurationSamlProperties {

    private String idpMetadataLocation;

    private String spEntityId;

    private String spRegistrationId;

    /**
     * getter method
     * @return a string with the id of metadata location
     */
    public String getIdpMetadataLocation() {
        return idpMetadataLocation;
    }

    /**
     * setter method
     * @param idpMetadataLocation a string with the id of metadata location
     */
    public void setIdpMetadataLocation(String idpMetadataLocation) {
        this.idpMetadataLocation = idpMetadataLocation;
    }

    /**
     * getter method
     * @return a string with the id of SP entity
     */
    public String getSpEntityId() {
        return spEntityId;
    }

    /**
     * setter method
     * @param spEntityId a string with the id of SP entity
     */
    public void setSpEntityId(String spEntityId) {
        this.spEntityId = spEntityId;
    }

    /**
     * getter method
     * @return a string with the id of SP registration
     */
    public String getSpRegistrationId() {
        return spRegistrationId;
    }

    /**
     * setter method
     * @param spRegistrationId a string with the id of SP registration
     */
    public void setSpRegistrationId(String spRegistrationId) {
        this.spRegistrationId = spRegistrationId;
    }
}
