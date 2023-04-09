package com.pagonxt.onetradefinance.work.api.work;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.PlatformUser;
import com.flowable.core.idm.api.PlatformUserQuery;
import com.flowable.core.idm.api.UserDefinitionService;
import com.flowable.idm.dto.PlatformUserResponse;
import com.flowable.idm.rest.service.api.IdmRestResponseFactory;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Controller class for users
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.core.idm.api.PlatformIdentityService
 * @see com.flowable.core.idm.api.UserDefinitionService
 * @see com.pagonxt.onetradefinance.work.security.UserSecurityService
 * @see com.flowable.idm.rest.service.api.IdmRestResponseFactory
 * @see com.pagonxt.onetradefinance.work.service.UserService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final String RESPONSE_ID = "id";
    private static final String RESPONSE_NAME = "name";

    protected final PlatformIdentityService platformIdentityService;

    private final UserSecurityService userSecurityService;

    protected IdmRestResponseFactory idmRestResponseFactory;

    protected final UserService userService;

    /**
     * constructor method
     * @param platformIdentityService   : a PlatformIdentityService object
     * @param objectMapper              : an ObjectMapper object
     * @param userSecurityService       : an UserSecurityService object
     * @param userDefinitionService     : an UserDefinitionService object
     * @param userService               : an UserService object
     */
    public UserController(PlatformIdentityService platformIdentityService,
                          ObjectMapper objectMapper,
                          UserSecurityService userSecurityService,
                          UserDefinitionService userDefinitionService,
                          UserService userService) {
        this.platformIdentityService = platformIdentityService;
        this.userSecurityService = userSecurityService;
        this.userService = userService;
        this.idmRestResponseFactory = new IdmRestResponseFactory(objectMapper, userDefinitionService);
    }

    /**
     * This method allows getting users by filter
     * @param login         : a string with the user login
     * @param displayName   : a string with the user displayName
     * @param email         : a string with the user email
     * @param status        : a string with the user status
     * @param userProfile   : a string with the user profile
     * @return a ResponseEntity object
     */
    @GetMapping
    public ResponseEntity<List<PlatformUserResponse>> getUsersByFilter(
            @RequestParam(required = false) String login, @RequestParam(required = false) String displayName,
            @RequestParam(required = false) String email, @RequestParam(required = false) String status,
            @RequestParam(required = false) String userProfile) {

        userSecurityService.checkCanManageUsers();
        PlatformUserQuery userQuery = platformIdentityService.createPlatformUserQuery();
        if (StringUtils.hasText(login)) {
            userQuery.userId(login);
        }
        if (StringUtils.hasText(displayName)) {
            userQuery.userDisplayNameLikeIgnoreCase(displayName);
        }
        if (StringUtils.hasText(email)) {
            userQuery.userEmailLike(email);
        }
        if (StringUtils.hasText(status)) {
            userQuery.state(status);
        }
        List<PlatformUser> usersList = this.userService.getUsersByUserProfile(userProfile, userQuery);
        List<PlatformUserResponse> platformUserResponse = this.idmRestResponseFactory
                .createUserResponseList(usersList, true);
        return ResponseEntity.ok(platformUserResponse);
    }

    /**
     * This method allows to get available users
     * @param groups : a string with groups
     * @return a ResponseEntity object
     */
    @GetMapping("/available-by-group/{groups}")
    public ResponseEntity<List<Map<String, String>>> getUsersAvailable(@PathVariable String groups) {
        userSecurityService.checkCanManageUsers();
        return ok(toMapOfUserNames(userService.getAvailableUsers(groups)));
    }

    /**
     * This method allows mapping usernames
     * @param users : a list of users
     * @return a list with a users collection
     */
    private List<Map<String, String>> toMapOfUserNames(List<PlatformUser> users) {
        return users.parallelStream()
                .map(user -> Map.of(RESPONSE_ID, user.getId(), RESPONSE_NAME, user.getDisplayName()))
                .collect(Collectors.toList());
    }
}
