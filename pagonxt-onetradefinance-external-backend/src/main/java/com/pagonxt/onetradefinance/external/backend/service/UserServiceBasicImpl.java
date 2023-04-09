package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_BACKOFFICE;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_CUSTOMER;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_MIDDLE_OFFICE;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_OFFICE;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see UserServiceBasicImpl
 * @see com.pagonxt.onetradefinance.integrations.service.DirectoryService
 * @see com.pagonxt.onetradefinance.integrations.service.OfficeService
 * @since jdk-11.0.13
 */
@Service
@Profile("!apigee")
public class UserServiceBasicImpl implements UserService {

    private static final String SPAIN = "ES";

    /**
     * Map collection with the different types of user
     */
    private static final Map<String, String> ORIGINS_BY_ROLE_MAP = Map.of(
            "ROLE_BACKOFFICE", USER_TYPE_BACKOFFICE,
            "ROLE_MIDDLE_OFFICE", USER_TYPE_MIDDLE_OFFICE,
            "ROLE_OFFICE", USER_TYPE_OFFICE,
            "ROLE_CUSTOMER", USER_TYPE_CUSTOMER
    );

    /**
     * This method returns the country of the current user
     * @return a country (Spain)
     */
    public String getCurrentUserCountry() {
        return SPAIN;
    }

    /**
     * This method returns the current user
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @return a User object with the user data
     */
    public User getCurrentUser() {
        /*
         * NOTES:
         * - This information should be stored on the session during user login.
         * - The user's displayed name should be retrieved from a valid source,
         *     like the JWT token for SAML auth, or from Google for OIDC auth.
         *     This displayedName capitalizing the login is a provisional solution.
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String displayedName = StringUtils.capitalize(authentication.getName());
        return new User(authentication.getName(), displayedName, getUserOrigin(authentication));
    }

    /**
     * This method gets the user origin
     * @param user Authentication object
     * @see org.springframework.security.core.Authentication
     * @return the user role
     */
    private String getUserOrigin(Authentication user) {
        String userRole = getUserRoles(user).stream()
                .filter(ORIGINS_BY_ROLE_MAP::containsKey)
                .findFirst()
                .orElseThrow(() -> new ServiceException("User has no valid roles", "getUserOrigin"));
        return ORIGINS_BY_ROLE_MAP.get(userRole);
    }

    /**
     * This method returns a roles list of the user
     * @param user Authentication object
     * @see org.springframework.security.core.Authentication
     * @return a roles list of the user
     */
    private List<String> getUserRoles(Authentication user) {
        return user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}
