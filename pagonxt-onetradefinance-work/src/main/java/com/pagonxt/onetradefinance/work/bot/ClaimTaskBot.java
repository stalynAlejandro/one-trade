package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import org.flowable.engine.TaskService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Class to assign tasks (work)
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.TaskService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.flowable.action.api.bot.BotService
 * @since jdk-11.0.13
 */
@Service
@SuppressWarnings("unchecked")
public class ClaimTaskBot extends TaskBot implements BotService {

    private static final String BOT_KEY = "claim-task-bot";
    private static final String BOT_NAME = "Claims task bot";
    private static final String BOT_DESCRIPTION = "Claims the task, and navigates to it";

    //Class attribute
    private final TaskService taskService;

    /**
     * Constructor method
     * @param objectMapper  : an ObjectMapper object
     * @param taskService   : a TaskService object.
     */
    public ClaimTaskBot(ObjectMapper objectMapper,
                        TaskService taskService) {
        super(objectMapper);
        this.taskService = taskService;
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
        claimTaskByActionExecutor(actionInstance, taskId);

        String caseId = getCaseId(payload);

        return buildBotActionResult(caseId, taskId);
    }

    /**
     * This method allows users to claim a task
     * @param actionInstance : an HistoricActionInstance object
     * @param taskId         : a string task id
     * @see com.flowable.action.api.history.HistoricActionInstance
     */
    private void claimTaskByActionExecutor(HistoricActionInstance actionInstance, String taskId) {
        taskService.claim(taskId, actionInstance.getExecutedBy());
    }

    /**
     * This method allows to get the case id
     * @param payload : payload
     * @return a string with the case id
     */
    private String getCaseId(Map<String, Object> payload) {

        return (String) ((Map<String, Object>) payload.get("item")).get("rootScopeId");
    }


}
