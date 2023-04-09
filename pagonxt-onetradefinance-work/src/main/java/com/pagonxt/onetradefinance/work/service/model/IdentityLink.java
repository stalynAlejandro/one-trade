package com.pagonxt.onetradefinance.work.service.model;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for identity links, in flowable
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class IdentityLink {

    //class attributes
    @JsonProperty("identityLink")
    private String link;

    private String type;

    private String userId;

    private String groupId;

    /**
     * getter method
     * @return : a string with the link
     */
    public String getLink() {
        return link;
    }

    /**
     * setter method
     * @param link : a string with the link
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * getter method
     * @return : a string with the type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type : a string with the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return : a string with the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setter method
     * @param userId : a string with the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * getter method
     * @return : a string with the group id
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * setter method
     * @param groupId : a string with the group id
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "IdentityLink{" +
                "identityLink='" + link + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", groupId='" + groupId + '\'' +
                '}';
    }
}
