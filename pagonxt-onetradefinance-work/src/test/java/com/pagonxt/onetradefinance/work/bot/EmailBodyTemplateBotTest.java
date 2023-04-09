package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.template.api.TemplateService;
import com.flowable.template.api.processor.TemplateProcessingResult;
import com.flowable.template.engine.TemplateEngine;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.expression.common.LanguageCodeExpressions;
import com.pagonxt.onetradefinance.work.expression.common.MasterDataExpressions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@UnitTest
class EmailBodyTemplateBotTest {

    @Mock
    ObjectMapper objectMapper;

    @Mock
    TemplateEngine templateEngine;

    @Mock
    MasterDataExpressions masterDataExpressions;

    @Mock
    LanguageCodeExpressions languageCodeExpressions;

    @InjectMocks
    EmailBodyTemplateBot emailBodyTemplateBot;

    @Test
    void invokeBot() {
        // Given
        HistoricActionInstance actionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        Map<String, Object> payload = new HashMap<>();
        payload.put("country", "country");
        payload.put("product", "product");

        String languageCode = "es_es";
        doReturn(languageCode).when(languageCodeExpressions).getByCountry(any());

        String masterDataProductName = "masterDataName";
        doReturn(masterDataProductName).when(masterDataExpressions).getInstanceName(any(), any(), any());

        String templateTest = "Template Test";
        TemplateProcessingResult templateProcessingResult = mock(TemplateProcessingResult.class);
        doReturn(templateTest).when(templateProcessingResult).getProcessedContent();
        TemplateService templateService = mock(TemplateService.class);
        doReturn(templateService).when(templateEngine).getTemplateService();
        doReturn(templateProcessingResult).when(templateService).processTemplate(any(), any(), any());

        ObjectNode objectNode = mock(ObjectNode.class);
        doReturn(objectNode).when(objectMapper).createObjectNode();

        // When
        BotActionResult botActionResult = emailBodyTemplateBot.invokeBot(actionInstance, actionDefinition, payload);

        // Then
        Map<String, Object> variant = Map.of("notificationType", "commonAdHocNotification",
                "languageCode", languageCode);
        verify(languageCodeExpressions).getByCountry((String) payload.get("country"));
        verify(masterDataExpressions).getInstanceName("md-product-type", (String) payload.get("product"), languageCode);
    }
}
