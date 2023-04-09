package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flowable.indexing.SearchService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.serializer.MyTasksListQueryComposer;
import com.pagonxt.onetradefinance.work.serializer.PagoNxtTaskItemSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class MyTasksServiceTest {

    @Spy
    ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();

    @Mock
    SearchService searchService;

    @Mock
    MyTasksListQueryComposer myTasksListQueryComposer;

    @Spy
    PagoNxtTaskItemSerializer pagoNxtTaskItemSerializer = new PagoNxtTaskItemSerializer();

    @Spy
    ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @InjectMocks
    MyTasksService myTasksService;

    @Test
    void getMyTasks_ok_invokesSearchService() throws Exception {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        when(myTasksListQueryComposer.compose(any())).thenReturn("{}");
        JsonNode emptySearchResponse = mapper.readTree(new ClassPathResource("data/model/searchServiceEmptyResponse.json").getFile());
        when(searchService.query(any(), any())).thenReturn(emptySearchResponse);
        elasticsearchProperties.setLogResponseTimes(true);

        // When
        myTasksService.getMyTasks(query);

        // Then
        verify(searchService).query(any(), any());
        elasticsearchProperties.setLogResponseTimes(false);
    }

    @Test
    void getMyTasks_error_throwsServiceException() throws Exception {
        // Given
        MyTasksQuery query = new MyTasksQuery();
        Mockito.doThrow(new RuntimeException("error")).when(myTasksListQueryComposer).compose(any());

        // When
        ServiceException thrown = Assertions.assertThrows(ServiceException.class,
                () -> myTasksService.getMyTasks(query),
                "Should throw ServiceException");

        // Then
        Assertions.assertEquals("Error listing my tasks", thrown.getMessage(),
                "Exception thrown should have a valid message");
        Assertions.assertEquals("getMyTasks", thrown.getKey(),
                "Exception thrown should have a valid key");
    }
}
