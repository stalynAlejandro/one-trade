package com.pagonxt.onetradefinance.external.backend.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@ConfigurationProperties(prefix = "pagonxt.saml2")
public class SecurityConfigurationSamlProperties {

    /**
     * Class attributes
     */
    private String idpMetadataLocation;

    private String spEntityId;

    private String spRegistrationId;

    /**
     * getter method
     * @return a string with Idp Metadata Location
     */
    public String getIdpMetadataLocation() {
        return idpMetadataLocation;
    }

    /**
     * setter method
     * @param idpMetadataLocation a string with Idp Metadata Location
     */
    public void setIdpMetadataLocation(String idpMetadataLocation) {
        this.idpMetadataLocation = idpMetadataLocation;
    }

    /**
     * getter method
     * @return a string with SP entity id
     */
    public String getSpEntityId() {
        return spEntityId;
    }

    /**
     * setter method
     * @param spEntityId a string with SP entity id
     */
    public void setSpEntityId(String spEntityId) {
        this.spEntityId = spEntityId;
    }

    /**
     * getter method
     * @return a string with SP registration id
     */
    public String getSpRegistrationId() {
        return spRegistrationId;
    }

    /**
     * setter method
     * @param spRegistrationId a string with SP registration id
     */
    public void setSpRegistrationId(String spRegistrationId) {
        this.spRegistrationId = spRegistrationId;
    }
}
