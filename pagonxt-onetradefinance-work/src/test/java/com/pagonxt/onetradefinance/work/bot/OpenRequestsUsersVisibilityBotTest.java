package com.pagonxt.onetradefinance.work.bot;

import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@UnitTest
class OpenRequestsUsersVisibilityBotTest {

    @InjectMocks
    OpenRequestsUsersVisibilityBot openRequestsUsersVisibilityBot;

    @Test
    void invokeBot_isOk_returnsProductAndEvents() {
        // Given
        HistoricActionInstance actionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        Map<String, Object> payload = new HashMap<>();
        payload.put("userResolutionGroups", List.of("PRODUCT_EVENT_XX"));

        // When
        BotActionResult botActionResult = openRequestsUsersVisibilityBot.invokeBot(actionInstance, actionDefinition, payload);

        // Then
        assertEquals("EVENT", botActionResult.getPayloadNode().get("productsAndEvents").get("events").get(0).asText());
        assertEquals("PRODUCT", botActionResult.getPayloadNode().get("productsAndEvents").get("products").get(0).asText());
    }
}