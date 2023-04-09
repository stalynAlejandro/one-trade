package com.pagonxt.onetradefinance.work.security.provider;

/**
 * interface that provides the user id
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public interface UserIdProvider {

    /**
     * Method to get the id of the current user
     * @return a string with the id of the current user
     */
    public String getCurrentUserId();
}
