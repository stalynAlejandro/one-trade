package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Class to view tasks
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
@SuppressWarnings("unchecked")
public class ViewTaskBot extends TaskBot implements BotService {

    private static final String BOT_KEY = "view-task-bot";
    private static final String BOT_NAME = "View task bot";
    private static final String BOT_DESCRIPTION = "Navigates to the task";

    /**
     * Constructor method
     * @param objectMapper  : an ObjectMapper object
     */
    public ViewTaskBot(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    /**
     * class method
     * @return a string with the value of BOT_KEY
     */
    @Override
    public String getKey() {
        return BOT_KEY;
    }

    /**
     * class method
     * @return a string with the value of BOT_NAME
     */
    @Override
    public String getName() {
        return BOT_NAME;
    }

    /**
     * class method
     * @return a string with the value of BOT_DESCRIPTION
     */
    @Override
    public String getDescription() {
        return BOT_DESCRIPTION;
    }

    /**
     * class method
     * @param actionInstance   : an HistoricActionInstance object
     * @param actionDefinition : an ActionDefinition object
     * @param payload          : payload
     * @see com.flowable.action.api.history.HistoricActionInstance
     * @see com.flowable.action.api.repository.ActionDefinition
     * @return a BotActionResult object
     */
    @Override
    public BotActionResult invokeBot(HistoricActionInstance actionInstance,
                                     ActionDefinition actionDefinition,
                                     Map<String, Object> payload) {

        String taskId = actionInstance.getScopeId();
        String caseId = getCaseId(payload);

        return buildBotActionResult(caseId, taskId);
    }

    /**
     * Method to get the case id
     * @param payload : the payload
     * @return a string with the case id
     */
    private String getCaseId(Map<String, Object> payload) {

        return (String) ((Map<String, Object>) payload.get("item")).get("rootScopeId");
    }

}
