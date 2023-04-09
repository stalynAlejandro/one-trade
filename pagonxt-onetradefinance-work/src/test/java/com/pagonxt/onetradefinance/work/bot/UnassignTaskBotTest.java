package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class UnassignTaskBotTest {

    @Mock
    TaskService taskService;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    UnassignTaskBot unassignTaskBot;

    @Test
    void invokeBot_unAssignTask_invokesUnClaim() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);
        String taskId = "task.id";
        doReturn(taskId).when(historicActionInstance).getScopeId();

        Map<String, Object> payload = new HashMap<>();

        TaskQuery taskQuery = mock(TaskQuery.class);
        Task task = mock(Task.class);
        doReturn(taskQuery).when(taskService).createTaskQuery();
        doReturn(taskQuery).when(taskQuery).taskId(any());
        doReturn(task).when(taskQuery).singleResult();
        doReturn("assignee").when(task).getAssignee();

        // When
        unassignTaskBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(taskService).unclaim(taskId);
    }


    @Test
    void invokeBot_currentAssigneeIsNull_thenThrowFlowableException() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);
        String taskId = "task.id";
        doReturn(taskId).when(historicActionInstance).getScopeId();

        Map<String, Object> payload = new HashMap<>();

        TaskQuery taskQuery = mock(TaskQuery.class);
        Task task = mock(Task.class);
        doReturn(taskQuery).when(taskService).createTaskQuery();
        doReturn(taskQuery).when(taskQuery).taskId(any());
        doReturn(task).when(taskQuery).singleResult();
        doReturn(null).when(task).getAssignee();

        // When
        FlowableException exception = assertThrows(FlowableException.class, () -> unassignTaskBot.invokeBot(historicActionInstance, actionDefinition, payload));

        // Then
        assertEquals("Task: task.id has no assignee", exception.getMessage());
        verify(taskService, never()).unclaim(taskId);
    }


    @Test
    void getKey_ok_returnString() {
        // Given, When and Then
        assertEquals("unassign-task-bot", unassignTaskBot.getKey());
    }

    @Test
    void getName_ok_returnString() {
        // Given, When and Then
        assertEquals("Unassign task bot", unassignTaskBot.getName());
    }

    @Test
    void getDescription_ok_returnString() {
        // Given, When and Then
        assertEquals("Unassign the task", unassignTaskBot.getDescription());
    }
}
