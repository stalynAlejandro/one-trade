package com.pagonxt.onetradefinance.integrations.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * model class for users
 * Include class attributes, constructor, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId;

    private String userDisplayedName;

    private String userType;

    /**
     * empty constructor method
     */
    public User() {
    }

    /**
     * constructor method
     * @param userId            : a string with the user id
     * @param userDisplayedName : a string with the displayed name of the user
     * @param userType          : a string with the user type
     */
    public User(String userId, String userDisplayedName, String userType) {
        this.userId = userId;
        this.userDisplayedName = userDisplayedName;
        this.userType = userType;
    }

    /**
     * getter method
     * @return a string with the user id
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
     * @return a string with the displayed name of the user
     */
    public String getUserDisplayedName() {
        return userDisplayedName;
    }

    /**
     * setter method
     * @param userDisplayedName a string with the displayed name of the user
     */
    public void setUserDisplayedName(String userDisplayedName) {
        this.userDisplayedName = userDisplayedName;
    }

    /**
     * getter method
     * @return a string with the user type
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
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(userDisplayedName, user.userDisplayedName) &&
                Objects.equals(userType, user.userType);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId, userDisplayedName, userType);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userDisplayedName='" + userDisplayedName + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
