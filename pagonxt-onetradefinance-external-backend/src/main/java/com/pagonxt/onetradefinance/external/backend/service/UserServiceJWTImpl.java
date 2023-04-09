package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see UserServiceJWTImpl
 * @see com.pagonxt.onetradefinance.integrations.service.DirectoryService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@Service
@Profile("apigee")
public class UserServiceJWTImpl implements UserService {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceJWTImpl.class);

    private static final String SPAIN = "ES";

    private final UserInfoService userInfoService;

    public UserServiceJWTImpl(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * This method returns the country of the current user
     * @return a country (Spain)
     */
    public String getCurrentUserCountry() {
        return SPAIN;
    }

    /**
     * This method returns the current user
     * @see User
     * @return a User object with the user data
     */
    public User getCurrentUser() {
        UserInfo userInfo = userInfoService.getUserInfo();
        LOG.info("UserInfo: {}", userInfo);
        return new User(userInfo.getUser().getUserId(), userInfo.getUser().getUserDisplayedName(), userInfo.getUser().getUserType());
    }

}
