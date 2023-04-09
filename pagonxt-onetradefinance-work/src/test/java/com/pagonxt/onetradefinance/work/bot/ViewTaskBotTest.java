package com.pagonxt.onetradefinance.work.bot;

import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.pagonxt.onetradefinance.work.config.BaseUnitTest;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@UnitTest
class ViewTaskBotTest extends BaseUnitTest {

    @InjectMocks
    ViewTaskBot viewTaskBot;

    @Test
    void invokeBot_ok_returnsCaseAndTaskPayload() {
        //given
        String caseId = "CAS-XXX";
        String taskId = "TSK-XXX";

        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        doReturn(taskId).when(historicActionInstance).getScopeId();

        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> item = new HashMap<>();
        item.put("rootScopeId", caseId);
        payload.put("item", item);

        //when
        BotActionResult botActionResult = viewTaskBot.invokeBot(historicActionInstance, actionDefinition, payload);

        //then
        assertEquals(caseId, botActionResult.getPayloadNode().get("caseId").asText());
        assertEquals(taskId, botActionResult.getPayloadNode().get("taskId").asText());
    }

}