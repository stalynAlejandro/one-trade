package com.pagonxt.onetradefinance.integrations.apis.common.security.configuration;

import com.pagonxt.onetradefinance.integrations.apis.common.model.JWTRequestBody;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "one-trade.integrations.jwt")
public class JWTProperties {
    private String url;
    private String clientId;
    private int timeout;
    private String sub;
    private String localUid;
    private String localRealm;
    private String iss;
    private String country;

    public JWTRequestBody getJWTRequestBody() {
        return new JWTRequestBody(sub, localUid, localRealm, iss, country);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getLocalUid() {
        return localUid;
    }

    public void setLocalUid(String localUid) {
        this.localUid = localUid;
    }

    public String getLocalRealm() {
        return localRealm;
    }

    public void setLocalRealm(String localRealm) {
        this.localRealm = localRealm;
    }

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
