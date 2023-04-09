package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.intents.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for tasks
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
public abstract class TaskBot {

    //class attribute
    private final ObjectMapper objectMapper;

    /**
     * constructor method
     * @param objectMapper : an ObjectMapper object
     */
    protected TaskBot(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Method class
     * @param caseId : a string with the case id
     * @param taskId : a string with the task id
     * @see com.flowable.action.api.bot.BotActionResult
     * @return a BaseBotActionResult
     */
    protected BaseBotActionResult buildBotActionResult(String caseId, String taskId) {
        Map<String, Object> botResultMap = new HashMap<>();
        botResultMap.put("caseId", caseId);
        botResultMap.put("taskId", taskId);
        JsonNode botResult = objectMapper.valueToTree(botResultMap);

        BaseBotActionResult baseBotActionResult;
        baseBotActionResult = new BaseBotActionResult(botResult, Intent.NOOP);

        return baseBotActionResult;
    }
}
