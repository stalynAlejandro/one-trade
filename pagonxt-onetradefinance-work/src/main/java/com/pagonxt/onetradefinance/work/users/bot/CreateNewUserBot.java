package com.pagonxt.onetradefinance.work.users.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.core.idm.api.PlatformGroup;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.PlatformUser;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.users.utils.UserCreationMessageUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.*;

/**
 * Class to create a user bot
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.security.UserSecurityService
 * @see com.flowable.core.idm.api.PlatformIdentityService
 * @see com.pagonxt.onetradefinance.work.users.utils.UserCreationMessageUtils
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Component
public class CreateNewUserBot implements BotService {

    private static final String BOT_KEY = "create-new-user-bot";
    private static final String BOT_NAME = "Create new user bot";
    private static final String BOT_DESCRIPTION = "Creates a new user";

    public static final String PAYLOAD_LOGIN_NAME = "loginName";
    private static final String PAYLOAD_DISPLAY_NAME = "displayName";
    private static final String PAYLOAD_FIRST_NAME = "firstName";
    private static final String PAYLOAD_LAST_NAME = "lastName";
    private static final String PAYLOAD_EMAIL = "email";

    public static final String RESPONSE_MESSAGE = "message";

    private static final String FLOWABLE_USER_GROUP = "flowableUser";
    private static final String ONE_TRADE_FINANCE_USER_GROUP = "oneTradeFinanceUser";
    private static final String ONE_TRADE_FINANCE_USER_DEFINITION = "user-oneTradeFinance";
    private static final String SANTANDER_THEME = "Santander";

    //class attributes
    private final PlatformIdentityService platformIdentityService;
    private final UserCreationMessageUtils userCreationMessageUtils;
    private final UserSecurityService userSecurityService;
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     * @param platformIdentityService   : a PlatformIdentityService object
     * @param userSecurityService       : a UserSecurityService object
     * @param userCreationMessageUtils  : a UserCreationMessageUtils object
     * @param objectMapper              : a ObjectMapper object
     */
    public CreateNewUserBot(PlatformIdentityService platformIdentityService,
                            UserCreationMessageUtils userCreationMessageUtils,
                            UserSecurityService userSecurityService,
                            ObjectMapper objectMapper) {
        this.platformIdentityService = platformIdentityService;
        this.userCreationMessageUtils = userCreationMessageUtils;
        this.userSecurityService = userSecurityService;
        this.objectMapper = objectMapper;
    }

    /**
     * method to get the bot key
     * @return a string object with the key
     */
    @Override
    public String getKey() {
        return BOT_KEY;
    }

    /**
     * method to get the bot name
     * @return a string object with the name
     */
    @Override
    public String getName() {
        return BOT_NAME;
    }

    /**
     * method to get the bot description
     * @return a string object with the description
     */
    @Override
    public String getDescription() {
        return BOT_DESCRIPTION;
    }

    /**
     * Method to invoke bot
     * @param actionInstance    : a HistoricActionInstance object
     * @param actionDefinition  : an ActionDefinition object
     * @param payload           : the payload
     * @see com.flowable.action.api.history.HistoricActionInstance
     * @see com.flowable.action.api.repository.ActionDefinition
     * @see com.flowable.action.api.bot.BotActionResult
     * @return a BotActionResult object
     */
    @Override
    public BotActionResult invokeBot(HistoricActionInstance actionInstance,
                                     ActionDefinition actionDefinition,
                                     Map<String, Object> payload) {
        if (!userSecurityService.canManageUsers()) {
            ObjectNode responsePayload = objectMapper.createObjectNode();
            return new BaseBotActionResult(responsePayload, Intent.ERROR_MESSAGE);
        }

        if ((this.platformIdentityService.createUserQuery()
                .userId((String) payload.get(PAYLOAD_LOGIN_NAME))).count() > 0L) {
            ObjectNode responsePayload = objectMapper.createObjectNode();
            Response loginMsg = userCreationMessageUtils.allLanguagesLoginMsgError((String) payload
                    .get(PAYLOAD_LOGIN_NAME));
            responsePayload.set(RESPONSE_MESSAGE, objectMapper.valueToTree(loginMsg));
            return new BaseBotActionResult(responsePayload, Intent.NOOP);
        } else {
            PlatformUser newUser = platformIdentityService
                    .createNewUserBuilder((String) payload.get(PAYLOAD_LOGIN_NAME))
                    .displayName((String) payload.get(PAYLOAD_DISPLAY_NAME))
                    .firstName((String) payload.get(PAYLOAD_FIRST_NAME))
                    .lastName((String) payload.get(PAYLOAD_LAST_NAME))
                    .email((String) payload.get(PAYLOAD_EMAIL))
                    .theme(SANTANDER_THEME)
                    .userDefinitionKey(ONE_TRADE_FINANCE_USER_DEFINITION)
                    .save();
            if (!newUser.getId().isEmpty()) {
                platformIdentityService.createMembership(newUser.getId(), FLOWABLE_USER_GROUP);
                platformIdentityService.createMembership(newUser.getId(), ONE_TRADE_FINANCE_USER_GROUP);
                assignUserProfileIdentityInfo(newUser.getId(), (String) payload.get(USER_PROFILE), newUser);
            }
            ObjectNode responsePayload = objectMapper.createObjectNode();
            Response successCreation = userCreationMessageUtils.userCreated();
            responsePayload.set(RESPONSE_MESSAGE, objectMapper.valueToTree(successCreation));
            return new BaseBotActionResult(responsePayload, Intent.NOOP);
        }
    }

    /**
     * Method to assign a user profile
     * @param userId        : a string with the user id
     * @param userProfile   : a string with the user profile
     * @param newUser       : a Platform user object with the new user
     * @see com.flowable.core.idm.api.PlatformUser
     */
    private void assignUserProfileIdentityInfo(String userId, String userProfile, PlatformUser newUser) {
        if (userProfile.equals(USER_MANAGEMENT_GROUP_ID)) {
            createIdentityInfo(userId, USER_MANAGEMENT_GROUP_NAME);
            assignUserManagementMembership(newUser);
        } else {
            createIdentityInfo(userId, BACK_OFFICE);
        }
    }

    /**
     * Method to assign a user management membership
     * @param newUser : a Platform user object with the new user
     * @see com.flowable.core.idm.api.PlatformUser
     */
    private void assignUserManagementMembership(PlatformUser newUser) {
        createUserManagementFlowableGroupIfNotExists();
        platformIdentityService.createMembership(newUser.getId(), USER_MANAGEMENT_GROUP_ID);
    }

    /**
     * Method to create identity info
     * @param userId                    : a string with the user id
     * @param userManagementGroupName   : a string with the user management group name

     */
    private void createIdentityInfo(String userId, String userManagementGroupName) {
        platformIdentityService.createPlatformIdentityInfoBuilder()
                .userId(userId)
                .info(USER_PROFILE, userManagementGroupName)
                .save();
    }

    /**
     * Method to create a User Management group if not exists
     */
    private void createUserManagementFlowableGroupIfNotExists() {
        PlatformGroup platformGroup = platformIdentityService.createPlatformGroupQuery()
                .groupId(USER_MANAGEMENT_GROUP_ID).singleResult();
        if (platformGroup == null) {
            platformIdentityService.createNewGroupBuilder(USER_MANAGEMENT_GROUP_ID)
                    .name(USER_MANAGEMENT_GROUP_NAME)
                    .save();
        }
    }
}
