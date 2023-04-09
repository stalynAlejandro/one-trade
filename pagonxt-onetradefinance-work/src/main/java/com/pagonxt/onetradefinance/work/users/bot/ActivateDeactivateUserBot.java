package com.pagonxt.onetradefinance.work.users.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.UserStates;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.utils.BotUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.PAYLOAD_SELECTED_USER;
import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.USER_ID;

/**
 * Class to activate and deactivate user bot
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.security.UserSecurityService
 * @see com.flowable.core.idm.api.PlatformIdentityService
 * @see com.pagonxt.onetradefinance.work.utils.BotUtils
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Component
public class ActivateDeactivateUserBot implements BotService {

    private static final String BOT_KEY = "activate-deactivate-user-bot";
    private static final String BOT_NAME = "Activate/deactivate user bot";
    private static final String BOT_DESCRIPTION = "Activates/deactivates selected user";

    public static final String PAYLOAD_STATE = "state";

    private static final String RESPONSE_PAYLOAD = "stateUserModified";

    //class attributes
    private final PlatformIdentityService platformIdentityService;
    private final UserSecurityService userSecurityService;
    private final BotUtils botUtils;
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     * @param platformIdentityService   : a PlatformIdentityService object
     * @param userSecurityService       : a UserSecurityService object
     * @param botUtils                  : a BotUtils object
     * @param objectMapper              : a ObjectMapper object
     */
    public ActivateDeactivateUserBot(PlatformIdentityService platformIdentityService,
                                     UserSecurityService userSecurityService,
                                     BotUtils botUtils,
                                     ObjectMapper objectMapper) {
        this.platformIdentityService = platformIdentityService;
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
        @SuppressWarnings("unchecked")
        Map<String, Object> selectedUser = (Map<String, Object>) payload.get(PAYLOAD_SELECTED_USER);
        changeUserState(selectedUser);
        return botUtils.buildBotActionResult(RESPONSE_PAYLOAD);
    }

    /**
     * Method to change user state
     * @param selectedUser  : the selected user
     */
    private void changeUserState(Map<String, Object> selectedUser) {
        String userId = (String) selectedUser.get(USER_ID);

        String newState = UserStates.ACTIVE.equals(selectedUser
                .get(PAYLOAD_STATE)) ? UserStates.INACTIVE : UserStates.ACTIVE;

        this.platformIdentityService.setUserState(userId, newState);
    }
}
