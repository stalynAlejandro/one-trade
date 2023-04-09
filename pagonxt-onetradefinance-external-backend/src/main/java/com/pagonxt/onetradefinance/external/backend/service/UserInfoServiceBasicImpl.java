package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.service.DirectoryService;
import com.pagonxt.onetradefinance.integrations.service.OfficeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Service class that provides the user information.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.UserService
 * @see com.pagonxt.onetradefinance.integrations.service.DirectoryService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@Service
@Profile("!apigee")
public class UserInfoServiceBasicImpl implements UserInfoService {

    /**
     * Class attributes
     */
    public final UserService userService;
    public final DirectoryService directoryService;
    public final OfficeService officeService;

    /**
     * Class constructor
     * @param userService UserService object
     * @param directoryService DirectoryService object
     * @param officeService OfficeService object
     */
    public UserInfoServiceBasicImpl(UserService userService, DirectoryService directoryService, OfficeService officeService) {
        this.userService = userService;
        this.directoryService = directoryService;
        this.officeService = officeService;
    }

    /**
     * Class method to get User Info
     * @return UserInfo object
     */
    public UserInfo getUserInfo() {
        UserInfo userInfo = new UserInfo();
        User user = userService.getCurrentUser();
        if (user == null) {
            throw new ServiceException("Error retrieving the current user", "getUserInfoError");
        }
        userInfo.setUser(user);
        userInfo.setCountry(userService.getCurrentUserCountry());
        userInfo.setOffice(officeService.getRequesterOffice(user));
        userInfo.setMiddleOffice(officeService.getRequesterMiddleOffice(user));
        userInfo.setMail(directoryService.getUserEmail(user));
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
