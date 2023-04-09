package com.pagonxt.onetradefinance.integrations.configuration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

/**
 * Custom class to create an authentication.
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class AppAuthentication implements Authentication {

    /**
     * The username
     */
    private String userName;

    /**
     * Constructor.
     *
     * @param userName : the username
     */
    public AppAuthentication(String userName) {
        this.userName = userName;
    }

    /**
     * Getter method for field userName
     *
     * @return value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method for field userName
     *
     * @param userName : value of userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Method to get the authorities granted to the principal.
     *
     * @return null
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    /**
     * Method to get The credentials that prove the principal is correct.
     *
     * @return null
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * Method to get the additional details about the authentication request.
     *
     * @return null
     */
    @Override
    public Object getDetails() {
        return null;
    }

    /**
     * Method to get the identity of the principal being authenticated.
     *
     * @return the principal
     */
    @Override
    public Object getPrincipal() {
        return (Principal) this::getUserName;
    }

    /**
     * Method to verify if the token has been authenticated.
     *
     * @return true
     */
    @Override
    public boolean isAuthenticated() {
        return true;
    }

    /**
     * Method to set the authentication (Does nothing).
     *
     * @param isAuthenticated <code>true</code> if the token should be trusted (which may
     *                        result in an exception) or <code>false</code> if the token should not be trusted
     * @throws IllegalArgumentException : the exception
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // Empty method
    }

    /**
     * Method that returns the name of this principal
     *
     * @return value of userName
     */
    @Override
    public String getName() {
        return getUserName();
    }
}
