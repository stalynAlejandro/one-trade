package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import org.flowable.engine.TaskService;
import org.springframework.stereotype.Component;

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
@Component
public class AssignTaskBot implements BotService {

    private static final String BOT_KEY = "assign-task-bot";
    private static final String BOT_NAME = "Assign task bot";
    private static final String BOT_DESCRIPTION = "Assign the task";

    private static final String PAYLOAD_ASSIGNED_USER = "assignedUser";
    private static final String ASSIGNED_USER_ID = "id";

    //Class attributes
    protected final TaskService taskService;
    protected final ObjectMapper objectMapper;

    /**
     * Constructor method
     * @param objectMapper  : an ObjectMapper object
     * @param taskService   : a TaskService object.
     */
    public AssignTaskBot(ObjectMapper objectMapper,
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

        String assignedUserId = (String) ((Map<String, Object>) payload.get(PAYLOAD_ASSIGNED_USER)).get(ASSIGNED_USER_ID);

        assignTaskToSelectedUser(assignedUserId, taskId);

        ObjectNode responsePayload = objectMapper.createObjectNode();

        return new BaseBotActionResult(responsePayload, Intent.NOOP);
    }

    /**
     * This method allows assigning a task to a selected user
     * @param assignedUserId : a string with the user id (user to assign task)
     * @param taskId         : a string with task id
     */
    private void assignTaskToSelectedUser(String assignedUserId, String taskId) {
        String currentAssignee = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult()
                .getAssignee();

        if(currentAssignee != null) {

            taskService.unclaim(taskId);
        }

        taskService.claim(taskId, assignedUserId);
    }
}
