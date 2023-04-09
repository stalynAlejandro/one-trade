package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.TaskService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Class to unassign tasks (work)
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.TaskService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.flowable.action.api.bot.BotService
 * @since jdk-11.0.13
 */
@Component
public class UnassignTaskBot implements BotService {

    private static final String BOT_KEY = "unassign-task-bot";
    private static final String BOT_NAME = "Unassign task bot";
    private static final String BOT_DESCRIPTION = "Unassign the task";

    //Class attributes
    protected final TaskService taskService;
    protected final ObjectMapper objectMapper;

    /**
     * Constructor method
     * @param objectMapper  : an ObjectMapper object
     * @param taskService   : a TaskService object.
     */
    public UnassignTaskBot(ObjectMapper objectMapper,
                          TaskService taskService) {
        this.taskService = taskService;
        this.objectMapper = objectMapper;
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

        unclaimTask(taskId);

        ObjectNode responsePayload = objectMapper.createObjectNode();

        return new BaseBotActionResult(responsePayload,Intent.NOOP);
    }

    /**
     * Method to unclaim a task
     * @param taskId : a string with the task id
     */
    private void unclaimTask(String taskId) {
        String currentAssignee = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult()
                .getAssignee();

        if(currentAssignee == null) {

            throw new FlowableException(String.format("Task: %s has no assignee", taskId));
        }

        taskService.unclaim(taskId);
    }
}
