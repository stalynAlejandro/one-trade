package com.pagonxt.onetradefinance.work.bot;

import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.pagonxt.onetradefinance.work.config.BaseUnitTest;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.engine.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@UnitTest
class ClaimTaskBotTest extends BaseUnitTest {

    @Mock
    TaskService taskService;

    @InjectMocks
    ClaimTaskBot claimTaskBot;

    @Test
    void invokeBot_ok_returnsCaseAndTaskPayload() {
        //given
        String taskId = "TSK-XXX";
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        doReturn(taskId).when(historicActionInstance).getScopeId();

        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String caseId = "CAS-XXX";
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> item = new HashMap<>();
        item.put("rootScopeId", caseId);
        payload.put("item", item);

        String userId = "some.user";
        doReturn(userId).when(historicActionInstance).getExecutedBy();

        //when
        BotActionResult botActionResult = claimTaskBot.invokeBot(historicActionInstance, actionDefinition, payload);

        //then
        assertEquals(caseId, botActionResult.getPayloadNode().get("caseId").asText());
        assertEquals(taskId, botActionResult.getPayloadNode().get("taskId").asText());
        verify(taskService).claim(eq(taskId), eq(userId));
    }
}