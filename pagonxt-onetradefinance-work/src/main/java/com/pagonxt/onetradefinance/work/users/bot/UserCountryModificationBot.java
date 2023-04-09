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
public class UserCountryModificationBot implements BotService {

    private static final String BOT_KEY = "user-country-modification-bot";
    private static final String BOT_NAME = "User country modification bot";
    private static final String BOT_DESCRIPTION = "Adds a country list to a user's identityInfo";
    private static final String RESPONSE_MESSAGE = "addedToCountries";

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
    public UserCountryModificationBot(PlatformIdentityService platformIdentityService,
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
        List<String> selectedCountriesInPayload = (List<String>) payload.get(COUNTRIES);
        List<Map<String, Object>> userGroupsAndRoles = getUserGroupsAndRoles(payload);
        recalculateFlowableGroups.recalculateGroups(userId, userGroupsAndRoles, selectedCountriesInPayload);
        updateUserIdentityInfoCountries(userId, selectedCountriesInPayload);
        return botUtils.buildBotActionResult(RESPONSE_MESSAGE);
    }

    /**
     * Method to get groups and roles of users
     * @param selectedUser  : selected user
     * @return a list with groups and roles
     */
    private List<Map<String, Object>> getUserGroupsAndRoles(Map<String, Object> selectedUser) {
        List<Map<String, Object>> groupsAndRoles = botUtils.getIdentityInfo(selectedUser, GROUPS_AND_ROLES);
        if (groupsAndRoles.isEmpty()) {
            return Collections.emptyList();
        }
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> grAndRolList = (List<Map<String, Object>>) groupsAndRoles.get(0).get(VALUE);
        return grAndRolList;
    }

    /**
     * Method to update user identity info
     * @param userId                     : a string object with the user id
     * @param selectedCountriesInPayload : a list of selected countries in payload
     */
    private void updateUserIdentityInfoCountries(String userId, List<String> selectedCountriesInPayload) {
        JsonNode userGroupsNode = objectMapper.valueToTree(selectedCountriesInPayload);
        platformIdentityService.createPlatformIdentityInfoBuilder()
                .userId(userId)
                .info(COUNTRIES, userGroupsNode)
                .save();
    }
}
