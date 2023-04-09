package com.pagonxt.onetradefinance.work.expression.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.flowable.platform.service.task.PlatformTaskService;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@UnitTest
class InternationalisationExpressionsTest {
    @InjectMocks
    private InternationalisationExpressions internationalisationExpressions;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private PlatformTaskService platformTaskService;

    @Test
    void getTaskName_WithTranslationsConfigured_With_PipeInName_ok(){
        //Given
        TaskRepresentation taskMock = new TaskRepresentation();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode translationsNode = mapper.createObjectNode();
        ObjectNode nameNode = mapper.createObjectNode();
        nameNode.set("name", new TextNode("Revisar solicitud de alta | CLE-123"));
        translationsNode.set("es_es",nameNode );
        taskMock.setName("Review request | CLE-123");
        taskMock.setTranslations(translationsNode);
        when(platformTaskService.getTask("myTaskId", true)).thenReturn(taskMock);

        //when
        String result = internationalisationExpressions.getTaskName("myTaskId", "es_es");
        //then
        assertThat(result).isEqualTo("Revisar solicitud de alta");
    }

    @Test
    void getTaskName_WithTranslationsConfigured_WithoutPipeInName_ok(){
        //Given
        TaskRepresentation taskMock = new TaskRepresentation();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode translationsNode = mapper.createObjectNode();
        ObjectNode nameNode = mapper.createObjectNode();
        nameNode.set("name", new TextNode("Revisar solicitud de alta"));
        translationsNode.set("es_es",nameNode );
        taskMock.setName("Review request | CLE-123");
        taskMock.setTranslations(translationsNode);
        when(platformTaskService.getTask("myTaskId", true)).thenReturn(taskMock);

        //when
        String result = internationalisationExpressions.getTaskName("myTaskId", "es_es");
        //then
        assertThat(result).isEqualTo("Revisar solicitud de alta");
    }

    @Test
    void getTaskName_WithoutTranslationsConfigured_ok(){
        //Given
        TaskRepresentation taskMock = new TaskRepresentation();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode translationsNode = mapper.createObjectNode();

        taskMock.setName("Review request | CLE-123");
        taskMock.setTranslations(translationsNode);
        when(platformTaskService.getTask("myTaskId", true)).thenReturn(taskMock);

        //when
        String result = internationalisationExpressions.getTaskName("myTaskId", "es_es");
        //then
        assertThat(result).isEqualTo("Review request");
    }
}
