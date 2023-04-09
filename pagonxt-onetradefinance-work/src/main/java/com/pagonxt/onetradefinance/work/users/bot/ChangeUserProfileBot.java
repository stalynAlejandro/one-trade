package com.pagonxt.onetradefinance.work.users.bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.users.service.RecalculateFlowableGroupsService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static com.pagonxt.onetradefinance.work.common.GroupRoleCommonConstants.*;
import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.*;
/**
 * Class to activate and deactivate user bot
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.security.UserSecurityService
 * @see com.flowable.core.idm.api.PlatformIdentityService
 * @see com.pagonxt.onetradefinance.work.users.service.RecalculateFlowableGroupsService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Component
public class ChangeUserProfileBot implements BotService {

    private static final String BOT_KEY = "change-user-profile-bot";
    private static final String BOT_NAME = "Change user profile bot";
    private static final String BOT_DESCRIPTION = "Changes user profile bot";

    //class attributes
    private final PlatformIdentityService platformIdentityService;
    private final RecalculateFlowableGroupsService recalculateFlowableGroupsService;
    private final UserSecurityService userSecurityService;
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     * @param platformIdentityService           : a PlatformIdentityService object
     * @param userSecurityService               : a UserSecurityService object
     * @param recalculateFlowableGroupsService  : a RecalculateFlowableGroupsService object
     * @param objectMapper                      : a ObjectMapper object
     */
    public ChangeUserProfileBot(PlatformIdentityService platformIdentityService,
                                RecalculateFlowableGroupsService recalculateFlowableGroupsService,
                                UserSecurityService userSecurityService,
                                ObjectMapper objectMapper) {
        this.platformIdentityService = platformIdentityService;
        this.recalculateFlowableGroupsService = recalculateFlowableGroupsService;
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
        String userId = (String) ((Map<String, Object>) payload.get(PAYLOAD_SELECTED_USER)).get(USER_ID);

        String userProfile = (String) ((Map<String, Object>) payload.get(PAYLOAD_SELECTED_USER_PROFILE)).get(VALUE);

        ObjectNode responsePayload = objectMapper.createObjectNode();

        if (!userSecurityService.canManageUsers()) {
            return new BaseBotActionResult(responsePayload, Intent.ERROR_MESSAGE);
        }

        if(userProfile.equals(BACK_OFFICE)) {

            changeUserIdentityInfoProfileAndCreateMembership(userId, USER_MANAGEMENT_GROUP_NAME);

            recalculateFlowableGroupsService.recalculateGroups(userId, null, null);

            deleteGroupsAndCountriesIdentityInfo(userId);
        } else {

            changeUserIdentityInfoProfileAndCreateMembership(userId, BACK_OFFICE);
        }

        return new BaseBotActionResult(responsePayload, Intent.NOOP);
    }

    /**
     * class method
     * @param userId : a string object with the user id
     */
    private void deleteGroupsAndCountriesIdentityInfo(String userId) {
        for(String info : Arrays.asList(GROUPS_AND_ROLES, COUNTRIES)) {
            JsonNode userGroupsNode = objectMapper.valueToTree(Collections.emptyList());

            platformIdentityService.createPlatformIdentityInfoBuilder()
                    .userId(userId)
                    .info(info, userGroupsNode)
                    .save();
        }
    }

    /**
     * class method
     * @param userId            : a string object with the user id
     * @param newUserProfile    : a string with the new user profile
     */
    private void changeUserIdentityInfoProfileAndCreateMembership(String userId, String newUserProfile) {
        platformIdentityService.createPlatformIdentityInfoBuilder()
                .userId(userId)
                .info(USER_PROFILE, newUserProfile)
                .save();

        if(newUserProfile.equals(USER_MANAGEMENT_GROUP_NAME)) {

            platformIdentityService.createMembership(userId, USER_MANAGEMENT_GROUP_ID);
        } else {

            platformIdentityService.deleteMembership(userId, USER_MANAGEMENT_GROUP_ID);
        }
    }
}
