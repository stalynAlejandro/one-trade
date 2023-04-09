package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.integrations.model.UserInfo;

/**
 * Service class that provides the user information.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public interface UserInfoService {

    /**
     * Class method to get User Info
     * @return UserInfo object
     */
    UserInfo getUserInfo();

    /**
     * Class method to get User Info
     * @param locale locale value
     * @return UserInfo object
     */
    UserInfo getUserInfo(String locale);
}
