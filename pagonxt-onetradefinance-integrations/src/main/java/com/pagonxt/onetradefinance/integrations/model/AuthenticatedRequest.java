package com.pagonxt.onetradefinance.integrations.model;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Model class for authenticated requests
 * Include class attributes, constructors, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see UserInfo
 * @since jdk-11.0.13
 */
public class AuthenticatedRequest {

    @NotNull
    private UserInfo requester;

    /**
     * Empty constructor method
     */
    public AuthenticatedRequest() {
    }

    /**
     * constructor method
     * @param requester a UserInfo object with the requester
     */
    public AuthenticatedRequest(UserInfo requester) {
        this.requester = requester;
    }

    /**
     * getter method
     * @return a UserInfo object with the requester
     */
    public UserInfo getRequester() {
        return requester;
    }

    /**
     * setter method
     * @param requester a UserInfo object with the requester
     */
    public void setRequester(UserInfo requester) {
        this.requester = requester;
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
        AuthenticatedRequest that = (AuthenticatedRequest) o;
        return Objects.equals(requester, that.requester);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(requester);
    }
}
