package com.pagonxt.onetradefinance.work.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.intents.Intent;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;

import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class BotUtilsTest {

    private static final String TIMEZONE = "Europe/Madrid";

    @Spy
    ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setTimeZone(TimeZone.getTimeZone(TIMEZONE));

    @InjectMocks
    BotUtils botUtils;

    @Test
    void buildBotActionResult_ok_returnsValidValue() {
        // Given
        String message = "message";
        JsonNode expectedMessageMap = mapper.valueToTree(Map.of(message, Boolean.TRUE));

        // When
        BaseBotActionResult result = botUtils.buildBotActionResult(message);

        // Then
        assertEquals(expectedMessageMap, result.getPayloadNode(), "Result should have a valid payload");
        assertEquals(Intent.NOOP, result.getIntent(), "Result should have a valid intent");
    }

    @Test
    void getIdentityInfo_ok_returnsValidData() {
        // Given
        String category = "category";
        Map<String, Object> identityInfo = Map.of("name", category, "someProperty", "someValue");
        Map<String, Object> anotherIdentityInfo = Map.of("name", "someOtherCategory", "someProperty", "someValue");
        Map<String, Object> payload = Map.of("identityInfo", List.of(identityInfo, anotherIdentityInfo), "somethingElse", new Object());
        List<Map<String, Object>> expectedResult = List.of(identityInfo);

        // When
        List<Map<String, Object>> result = botUtils.getIdentityInfo(payload, category);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }
}
