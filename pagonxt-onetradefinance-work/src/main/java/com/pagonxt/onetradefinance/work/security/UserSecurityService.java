package com.pagonxt.onetradefinance.work.security;

import com.flowable.core.idm.api.PlatformIdentityService;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.work.security.provider.UserIdProvider;
import org.flowable.idm.api.Group;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.USER_MANAGEMENT_GROUP_ID;

/**
 * Service class to provide some methods to check the security of users
 * @author -
 * @version jdk-11.0.13
 * @see  com.flowable.core.idm.api.PlatformIdentityService
 * @see com.pagonxt.onetradefinance.work.security.provider.UserIdProvider
 * @since jdk-11.0.13
 */
@Service
public class UserSecurityService {

    private static final String FLOWABLE_ADMINISTRATOR_GROUP = "flowableAdministrator";
    private static final String ADMINISTRATOR_RESOLUTION_GROUP_ROLE = "_Administrator";
    private static final String SUPERVISOR_RESOLUTION_GROUP_ROLE = "_Supervisor";

    private static final Set<String> ACCEPTED_GROUPS = Set.of(FLOWABLE_ADMINISTRATOR_GROUP, USER_MANAGEMENT_GROUP_ID);

    //Class attributes
    private final PlatformIdentityService platformIdentityService;
    private final UserIdProvider userIdProvider;

    /**
     * constructor method
     * @param platformIdentityService   : a PlatformIdentityService object
     * @param userIdProvider            : a UserIdProvider object
     */
    public UserSecurityService(PlatformIdentityService platformIdentityService,
                               UserIdProvider userIdProvider) {
        this.platformIdentityService = platformIdentityService;
        this.userIdProvider = userIdProvider;
    }

    /**
     * Method to check can manage users
     */
    public void checkCanManageUsers() {
        String currentUserId = getCurrentUserId();
        if (!canManageUsers(currentUserId)) {
            throw new SecurityException(currentUserId, "manage users");
        }
    }

    /**
     * Method to check can manage users
     * @return a boolean value
     */
    public boolean canManageUsers() {
        return canManageUsers(getCurrentUserId());
    }

    /**
     * Method to check can manage users
     * @param userId : a string with the user id
     * @return a boolean value
     */
    public boolean canManageUsers(String userId) {
        return platformIdentityService.createGroupQuery()
                .groupMember(userId)
                .list()
                .stream()
                .map(Group::getId)
                .anyMatch(groupId -> ACCEPTED_GROUPS.contains(groupId) ||
                        groupId.contains(ADMINISTRATOR_RESOLUTION_GROUP_ROLE) ||
                        groupId.contains(SUPERVISOR_RESOLUTION_GROUP_ROLE));
    }

    /**
     * Method to get the id of the current user
     * @return a string with the id
     */
    private String getCurrentUserId() {
        return userIdProvider.getCurrentUserId();
    }
}
