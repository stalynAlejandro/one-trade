package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.User;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides a method to get offices
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.User
 * @since jdk-11.0.13
 */

public interface OfficeService {

    /**
     * This method allows to obtain the requester office
     * @param requester a User object with the requester
     * @return a string with the requester office
     */
    String getRequesterOffice(User requester);

    /**
     * This method allows to obtain the middle office of the requester
     * @param requester a User object with the requester
     * @return a string with the middle office of the requeste
     */
    String getRequesterMiddleOffice(User requester);

}
