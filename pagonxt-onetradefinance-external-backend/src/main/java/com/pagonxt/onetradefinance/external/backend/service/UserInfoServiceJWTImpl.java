package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.service.DirectoryService;
import com.pagonxt.onetradefinance.integrations.service.OfficeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * Service class that provides the user information.
 * @author -
 * @version jdk-11.0.13
 * @see UserService
 * @see DirectoryService
 * @see OfficeService
 * @since jdk-11.0.13
 */
@Service
@Profile("apigee")
public class UserInfoServiceJWTImpl implements UserInfoService {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserInfoServiceJWTImpl.class);

    private final HttpServletRequest httpServletRequest;

    public UserInfoServiceJWTImpl(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * Class method to get User Info
     * @return UserInfo object
     */
    public UserInfo getUserInfo() {
        UserInfo userInfo = (UserInfo) httpServletRequest.getSession().getAttribute("userInfo");
        if (userInfo == null) {
            LOG.error("Error retrieving the user info");
            throw new ServiceException("Error retrieving the user info", "getUserInfoError");
        }
        return userInfo;
    }

    /**
     * Class method to get User Info
     * @param locale locale value
     * @return UserInfo object
     */
    public UserInfo getUserInfo(String locale) {
        UserInfo userInfo = getUserInfo();
        userInfo.setLocale(locale);
        return userInfo;
    }
}
