package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@UnitTest
class AssignTaskBotTest {

    @Mock
    TaskService taskService;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    AssignTaskBot assignTaskBot;

    @Test
    void invokeBot() {
        // Given
        HistoricActionInstance actionInstance = mock(HistoricActionInstance.class);
        String taskId = "taskId";
        doReturn(taskId).when(actionInstance).getScopeId();

        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String userId = "userId";
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> assignedUser = new HashMap<>();
        assignedUser.put("id", userId);
        payload.put("assignedUser", assignedUser);

        TaskQuery taskQuery = mock(TaskQuery.class);
        Task task = mock(Task.class);
        String assignee = "assignee";
        doReturn(taskQuery).when(taskService).createTaskQuery();
        doReturn(taskQuery).when(taskQuery).taskId(taskId);
        doReturn(task).when(taskQuery).singleResult();
        doReturn(assignee).when(task).getAssignee();

        ObjectNode objectNode = mock(ObjectNode.class);
        doReturn(objectNode).when(objectMapper).createObjectNode();

        // When
        assignTaskBot.invokeBot(actionInstance, actionDefinition, payload);

        // Then
        verify(taskService).claim(taskId, userId);
    }

    @Test
    void getKey_ok_returnString() {
        // Given, When and Then
        assertEquals("assign-task-bot", assignTaskBot.getKey());
    }

    @Test
    void getName_ok_returnString() {
        // Given, When and Then
        assertEquals("Assign task bot", assignTaskBot.getName());
    }

    @Test
    void getDescription_ok_returnString() {
        // Given, When and Then
        assertEquals("Assign the task", assignTaskBot.getDescription());
    }
}
