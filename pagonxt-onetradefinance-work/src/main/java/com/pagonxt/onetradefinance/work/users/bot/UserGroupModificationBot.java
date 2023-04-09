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
import com.pagonxt.onetradefinance.work.utils.BotUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.work.common.GroupRoleCommonConstants.*;
import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.USER_ID;

/**
 * Class to create a user country modification
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.security.UserSecurityService
 * @see com.flowable.core.idm.api.PlatformIdentityService
 * @see com.pagonxt.onetradefinance.work.users.service.RecalculateFlowableGroupsService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.pagonxt.onetradefinance.work.utils.BotUtils
 * @since jdk-11.0.13
 */
@Component
public class UserGroupModificationBot implements BotService {

    private static final String BOT_KEY = "user-group-modification-bot";
    private static final String BOT_NAME = "User group modification bot";
    private static final String BOT_DESCRIPTION = "Adds a list of groups and roles to a user identityInfo";

    public static final String RESPONSE_MESSAGE = "addedToGroupAndRoles";

    //class attributes
    private final PlatformIdentityService platformIdentityService;
    private final RecalculateFlowableGroupsService recalculateFlowableGroups;
    private final UserSecurityService userSecurityService;
    private final BotUtils botUtils;
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     * @param platformIdentityService   : a PlatformIdentityService object
     * @param userSecurityService       : a UserSecurityService object
     * @param recalculateFlowableGroups : a RecalculateFlowableGroupsService object
     * @param objectMapper              : a ObjectMapper object
     * @param botUtils                  : a BotUtils object
     */
    public UserGroupModificationBot(PlatformIdentityService platformIdentityService,
                                    RecalculateFlowableGroupsService recalculateFlowableGroups,
                                    UserSecurityService userSecurityService,
                                    BotUtils botUtils,
                                    ObjectMapper objectMapper) {
        this.platformIdentityService = platformIdentityService;
        this.recalculateFlowableGroups = recalculateFlowableGroups;
        this.userSecurityService = userSecurityService;
        this.botUtils = botUtils;
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
        String userId = (String) payload.get(USER_ID);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> groupsAndRoles = (List<Map<String, Object>>) payload.get(GROUPS_AND_ROLES);
        List<String> userCountries = getUserCountries(payload);
        recalculateFlowableGroups.recalculateGroups(userId, groupsAndRoles, userCountries);
        updateUserIdentityInfoCountries(userId, groupsAndRoles);
        return botUtils.buildBotActionResult(RESPONSE_MESSAGE);
    }

    /**
     * Method to get the user countries
     * @param payload : the payload
     * @return  a list of countries
     */
    private List<String> getUserCountries(Map<String, Object> payload) {
        List<Map<String, Object>> countries = botUtils.getIdentityInfo(payload, COUNTRIES);
        if (countries.isEmpty()) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        List<String> countryValueList = (List<String>) countries.get(0).get(VALUE);
        return countryValueList;
    }

    /**
     * Method to update user identity info
     * @param userId         : a string object with the user id
     * @param groupsAndRoles : a list with groups and roles
     */
    private void updateUserIdentityInfoCountries(String userId, List<Map<String, Object>> groupsAndRoles) {
        JsonNode userGroupsNode = objectMapper.valueToTree(groupsAndRoles);
        platformIdentityService.createPlatformIdentityInfoBuilder()
                .userId(userId)
                .info(GROUPS_AND_ROLES, userGroupsNode)
                .save();
    }
}
