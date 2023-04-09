package com.pagonxt.onetradefinance.work.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.intents.Intent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.IDENTITY_INFO;
import static com.pagonxt.onetradefinance.work.common.UserManagementConstants.IDENTITY_INFO_NAME;

/**
 * service clas with bot utils
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class BotUtils {

    //class attribute
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     * @param objectMapper : an ObjectMapper object
     */
    public BotUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Method to build a bot action result
     * @param message : a string objec with message
     * @see com.flowable.action.api.bot.BotActionResult
     * @return a BaseBotActionResult object
     */
    public BaseBotActionResult buildBotActionResult(String message) {
        Map<String, Object> botResultMap = new HashMap<>();
        botResultMap.put(message, true);
        JsonNode botResult = objectMapper.valueToTree(botResultMap);

        BaseBotActionResult baseBotActionResult;
        baseBotActionResult = new BaseBotActionResult(botResult, Intent.NOOP);

        return baseBotActionResult;
    }

    /**
     * Method to get identity info
     * @param payload   : the payload
     * @param category  : a string object with the category
     * @return a list with identity info
     */
    public List<Map<String, Object>> getIdentityInfo(Map<String, Object> payload, String category) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> identityInfo = (List<Map<String, Object>>) payload.get(IDENTITY_INFO);
        return identityInfo.stream()
                .filter(identity -> identity.get(IDENTITY_INFO_NAME).toString().equals(category))
                .collect(Collectors.toList());
    }
}
