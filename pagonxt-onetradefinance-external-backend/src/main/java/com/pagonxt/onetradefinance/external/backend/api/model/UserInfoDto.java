package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * DTO Class for user info
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class UserInfoDto {

    //claas attributes
    private String userId;

    private String userDisplayedName;

    private String userType;

    private String country;

    private String middleOffice;

    private String office;

    private String mail;

    /**
     * getter method
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * setter method
     * @param userId a string with the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * getter method
     * @return the displayed name of the user
     */
    public String getUserDisplayedName() {
        return userDisplayedName;
    }

    /**
     * setter method
     * @param userDisplayedName a string with  the displayed name of the user
     */
    public void setUserDisplayedName(String userDisplayedName) {
        this.userDisplayedName = userDisplayedName;
    }

    /**
     * getter method
     * @return the user type
     */
    public String getUserType() {
        return userType;
    }

    /**
     * setter method
     * @param userType a string with the user type
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * getter method
     * @return the user country
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter method
     * @param country a string with the user country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getter method
     * @return the middle office of the user
     */
    public String getMiddleOffice() {
        return middleOffice;
    }

    /**
     * setter method
     * @param middleOffice a string with the middle office of the user
     */
    public void setMiddleOffice(String middleOffice) {
        this.middleOffice = middleOffice;
    }

    /**
     * getter method
     * @return the user office
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the office of the user
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return the user email
     */
    public String getMail() {
        return mail;
    }

    /**
     * setter method
     * @param mail a string with the user email
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfoDto that = (UserInfoDto) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(userDisplayedName, that.userDisplayedName) &&
                Objects.equals(userType, that.userType) &&
                Objects.equals(country, that.country) &&
                Objects.equals(middleOffice, that.middleOffice) &&
                Objects.equals(office, that.office) && Objects.equals(mail, that.mail);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, userDisplayedName, userType, country, middleOffice, office, mail);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "UserInfoDto{" +
                "userId='" + userId + '\'' +
                ", userDisplayedName='" + userDisplayedName + '\'' +
                ", userType='" + userType + '\'' +
                ", country='" + country + '\'' +
                ", middleOffice='" + middleOffice + '\'' +
                ", office='" + office + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
