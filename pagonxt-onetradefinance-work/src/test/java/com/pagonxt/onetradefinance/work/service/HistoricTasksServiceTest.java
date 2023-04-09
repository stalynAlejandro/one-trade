package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.indexing.SearchService;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import com.pagonxt.onetradefinance.integrations.model.historictask.PagoNxtHistoricTaskItem;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.serializer.HistoricTasksListQueryComposer;
import com.pagonxt.onetradefinance.work.serializer.PagoNxtHistoricTaskItemSerializer;
import com.pagonxt.onetradefinance.work.service.model.Task;
import com.pagonxt.onetradefinance.work.service.model.elastic.ElasticResponse;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hit;
import com.pagonxt.onetradefinance.work.service.model.elastic.Hits;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class HistoricTasksServiceTest {

    @InjectMocks
    HistoricTasksService historicTasksService;
    @Mock
    HistoricTasksListQueryComposer historicTasksListQueryComposer;
    @Mock
    PagoNxtHistoricTaskItemSerializer pagoNxtHistoricTaskItemSerializer;
    @Mock
    CaseSecurityService caseSecurityService;
    @Mock
    SearchService searchService;
    @Mock
    ObjectMapper objectMapper;
    @Spy
    ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();

    @Test
    void getHistoricTasks_ok_invokesSearchService() throws Exception {
        // Given
        HistoricTasksQuery query = new HistoricTasksQuery();
        UserInfo userInfo = new UserInfo();
        query.setUserInfo(userInfo);
        query.setCode("CLE-TEST");
        query.setLocale("es_es");

        String searchQuery = "searchQueryMock";
        JsonNode jsonNode = mock(JsonNode.class);
        JsonParser jsonParser = mock(JsonParser.class);
        ElasticResponse<Task> elasticResponse = mock(ElasticResponse.class);
        Task task = new Task();
        task.setId("idTest");
        Hit<Task> hit = new Hit<>();
        hit.setSource(task);
        Hits<Task> hits = new Hits<>();
        hits.setHitList(List.of(hit));
        when(elasticResponse.getHits()).thenReturn(hits);
        when(elasticResponse.getSize()).thenReturn(1L);

        PagoNxtHistoricTaskItem expectedTask = new PagoNxtHistoricTaskItem();
        expectedTask.setRowId("testId");

        when(historicTasksListQueryComposer.compose(any())).thenReturn(searchQuery);
        when(searchService.query("tasks", searchQuery)).thenReturn(jsonNode);
        when(objectMapper.treeAsTokens(jsonNode)).thenReturn(jsonParser);
        when(objectMapper.readValue(eq(jsonParser), any(TypeReference.class))).thenReturn(elasticResponse);
        when(pagoNxtHistoricTaskItemSerializer.serialize(task, "es_es")).thenReturn(expectedTask);
        elasticsearchProperties.setLogResponseTimes(true);

        // When
        HistoricTasksList result = historicTasksService.getHistoricTasks(query);
        // Then
        assertEquals(1, result.getTotal());
        assertEquals("testId", result.getData().get(0).getRowId());
        verify(caseSecurityService).checkRead(userInfo, "CLE-TEST");
        verify(historicTasksListQueryComposer).compose(query);
        verify(searchService).query(any(), any());
        verify(pagoNxtHistoricTaskItemSerializer, times(1)).serialize(task, "es_es");
        elasticsearchProperties.setLogResponseTimes(false);
    }

    @Test
    void getHistoricTask_whenMappingError_thenThrowServiceException() throws Exception {
        // Given
        HistoricTasksQuery query = new HistoricTasksQuery();
        UserInfo userInfo = new UserInfo();
        query.setUserInfo(userInfo);

        String searchQuery = "searchQueryMock";
        JsonNode jsonNode = mock(JsonNode.class);
        JsonParser jsonParser = mock(JsonParser.class);
        when(historicTasksListQueryComposer.compose(any())).thenReturn(searchQuery);
        when(searchService.query("tasks", searchQuery)).thenReturn(jsonNode);
        when(objectMapper.treeAsTokens(jsonNode)).thenReturn(jsonParser);
        when(objectMapper.readValue(eq(jsonParser), any(TypeReference.class))).thenThrow(new IOException());
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> historicTasksService.getHistoricTasks(query));
        // Then
        assertEquals("getHistoricTasks", exception.getKey());
        assertEquals("Error listing historic tasks", exception.getMessage());
    }
}
