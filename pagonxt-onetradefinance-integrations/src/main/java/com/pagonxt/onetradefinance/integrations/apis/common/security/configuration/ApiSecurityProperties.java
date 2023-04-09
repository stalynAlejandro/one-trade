package com.pagonxt.onetradefinance.integrations.apis.common.security.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "one-trade.integrations.api.token")
public class ApiSecurityProperties {

    private String clientId;
    private String csec;
    private String url;
    private String country;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCsec() {
        return csec;
    }

    public void setCsec(String csec) {
        this.csec = csec;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
