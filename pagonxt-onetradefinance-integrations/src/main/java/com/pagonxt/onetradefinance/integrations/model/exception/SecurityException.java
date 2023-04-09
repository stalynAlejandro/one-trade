package com.pagonxt.onetradefinance.integrations.model.exception;

import java.util.List;

/**
 * class for handling exceptions regarding security
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.exception.ServiceException
 * @since jdk-11.0.13
 */
public class SecurityException extends ServiceException {

    /**
     * constructor method
     * @param user          :a string with the user id
     * @param resourceId    :a string with the resource id
     * @param resourceType  :a string with the resource type
     */
    public SecurityException(String user, String resourceId, String resourceType) {
        super(String.format("User %s has no access to resource %s of type %s", user, resourceId, resourceType),
                "securityViolation",
                List.of(user, resourceId, resourceType).toArray(new String[]{}));
    }

    /**
     * constructor method
     * @param user   :a string with the user id
     * @param action :a string with th action
     */
    public SecurityException(String user, String action) {
        super(String.format("User %s has no permission to %s", user, action),
                "securityViolation",
                List.of(user, action).toArray(new String[]{}));
    }
}
