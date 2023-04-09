package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.integrations.model.User;

/**
 * Service interface that provides the user information.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public interface UserService {

    String getCurrentUserCountry();

    User getCurrentUser();
}
