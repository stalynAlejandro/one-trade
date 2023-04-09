package com.pagonxt.onetradefinance.work.service;

import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.PlatformUser;
import com.flowable.core.idm.api.PlatformUserQuery;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * service class for users
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.security.UserSecurityService
 * @see com.flowable.core.idm.api.PlatformIdentityService
 * @since jdk-11.0.13
 */
@Service
public class UserService {

    private static final String USER_STATUS_ACTIVE = "ACTIVE";

    //class attributes
    private final UserSecurityService userSecurityService;
    private final PlatformIdentityService platformIdentityService;

    /**
     * constructor method
     * @param userSecurityService       : a UserSecurityService object
     * @param platformIdentityService   : a PlatformIdentityService object
     */
    public UserService(UserSecurityService userSecurityService,
                       PlatformIdentityService platformIdentityService) {
        this.userSecurityService = userSecurityService;
        this.platformIdentityService = platformIdentityService;
    }

    /**
     * Method to get a list of users by user profile
     * @param userProfile   : a string object with the user profile
     * @param userQuery     : a PlatformUserQuery object
     * @see com.flowable.core.idm.api.PlatformUserQuery
     * @see com.flowable.core.idm.api.PlatformUser
     * @return a list of users
     */
    public List<PlatformUser> getUsersByUserProfile(String userProfile, PlatformUserQuery userQuery) {
        userSecurityService.checkCanManageUsers();
        List<PlatformUser> usersList = userQuery.list();
        if (userProfile != null && !usersList.isEmpty()) {
            usersList = usersList.stream()
                    .filter(user -> userHasProfile(user, userProfile))
                    .collect(Collectors.toList());
        }
        return usersList;
    }

    /**
     * Method to get available users
     * @param groups : a string object with the groups
     * @see com.flowable.core.idm.api.PlatformUser
     * @return a list of users
     */
    public List<PlatformUser> getAvailableUsers(String groups) {
        userSecurityService.checkCanManageUsers();
        List<PlatformUser> possibleUsers = platformIdentityService.createPlatformUserQuery()
                .memberOfGroup(groups)
                .list();
        return filterOutInactiveUsers(possibleUsers);
    }

    /**
     * Method to get inactive users
     * @param possibleUsers : a list of users
     * @see com.flowable.core.idm.api.PlatformUser
     * @return a list of users
     */
    private List<PlatformUser> filterOutInactiveUsers(List<PlatformUser> possibleUsers) {
        return possibleUsers.stream()
                .filter(user -> user.getState().equals(USER_STATUS_ACTIVE))
                .collect(Collectors.toList());
    }

    /**
     * Method to check if a user has profile
     * @param user          : a PlatformUser object
     * @param userProfile   : a string object with the user profile
     * @see com.flowable.core.idm.api.PlatformUser
     * @return  true or false if the user has a profile
     */
    private boolean userHasProfile(PlatformUser user, String userProfile) {
        return user.getIdentityInfo().stream()
                .anyMatch(info -> "userProfile".equals(info.getName()) && info.getValue().equals(userProfile));
    }
}
