package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * model class to add more info about users
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see User
 * @since jdk-11.0.13
 */
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private User user;

    private String country;

    private String middleOffice;

    @Size(max = 10)
    private String office;

    private String mail;

    private String locale;

    /**
     * empty constructor method
     */
    public UserInfo() {
    }

    /**
     * constructor method
     * @param user a User object with the user
     */
    public UserInfo(User user) {
        this.user = user;
    }

    /**
     * getter method
     * @return a User object with the user
     */
    public User getUser() {
        return user;
    }

    /**
     * setter method
     * @param user a User object with the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * getter method
     * @return a string with the user country
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
     * @return a string with the middle office of the user
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
     * @return a string with the user office
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the user office
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return a string with the user email
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
     * getter method
     * @return a string with the locale value
     */
    public String getLocale() {
        return locale;
    }

    /**
     * setter method
     * @param locale a string with the locale value
     */
    public void setLocale(String locale) {
        this.locale = locale;
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
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(user, userInfo.user) &&
                Objects.equals(country, userInfo.country) &&
                Objects.equals(middleOffice, userInfo.middleOffice) &&
                Objects.equals(office, userInfo.office) &&
                Objects.equals(mail, userInfo.mail) &&
                Objects.equals(locale, userInfo.locale);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(user, country, middleOffice, office, mail, locale);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "UserInfo{" +
                "user=" + user +
                ", country='" + country + '\'' +
                ", middleOffice='" + middleOffice + '\'' +
                ", office='" + office + '\'' +
                ", mail='" + mail + '\'' +
                ", locale='" + locale + '\'' +
                '}';
    }
}
