package com.pagonxt.onetradefinance.integrations.apis.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for JWT Request Body
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JWTRequestBody {

    @JsonProperty("sub")
    private String sub;
    @JsonProperty("local_uid")
    private String localUid;
    @JsonProperty("local_realm")
    private String localRealm;
    @JsonProperty("iss")
    private String iss;
    @JsonProperty("country")
    private String country;

    /**
     * Class constructor
     * @param sub a string with sub
     * @param localUid a string with localUid
     * @param localRealm a string with localRealm
     * @param iss a string with iss
     * @param country a string with country
     */
    public JWTRequestBody(String sub, String localUid, String localRealm, String iss, String country) {
        this.sub = sub;
        this.localUid = localUid;
        this.localRealm = localRealm;
        this.iss = iss;
        this.country = country;
    }

    /**
     * getter method
     * @return a string with sub
     */
    public String getSub() {
        return sub;
    }

    /**
     * setter method
     * @param sub a string with sub
     */
    public void setSub(String sub) {
        this.sub = sub;
    }

    /**
     * getter method
     * @return a string with local Uid
     */
    public String getLocalUid() {
        return localUid;
    }

    /**
     * setter method
     * @param localUid a string with local Uid
     */
    public void setLocalUid(String localUid) {
        this.localUid = localUid;
    }

    /**
     * getter method
     * @return a string with local realm
     */
    public String getLocalRealm() {
        return localRealm;
    }

    /**
     * setter method
     * @param localRealm a string with local realm
     */
    public void setLocalRealm(String localRealm) {
        this.localRealm = localRealm;
    }

    /**
     * getter method
     * @return a string with Iss
     */
    public String getIss() {
        return iss;
    }

    /**
     * setter method
     * @param iss a string with iss
     */
    public void setIss(String iss) {
        this.iss = iss;
    }

    /**
     * getter method
     * @return a string with the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter method
     * @param country a string with the country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
