package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.flowable.indexing.SearchService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.configuration.ElasticsearchProperties;
import com.pagonxt.onetradefinance.work.serializer.MyRequestsListQueryComposer;
import com.pagonxt.onetradefinance.work.serializer.PagoNxtRequestItemSerializer;
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
class MyRequestsServiceTest {

    @Spy
    ElasticsearchProperties elasticsearchProperties = new ElasticsearchProperties();

    @Mock
    SearchService searchService;

    @Mock
    MyRequestsListQueryComposer myRequestsListQueryComposer;

    @Spy
    PagoNxtRequestItemSerializer pagoNxtRequestItemSerializer = new PagoNxtRequestItemSerializer();

    @Spy
    ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @InjectMocks
    MyRequestsService myRequestsService;

    @Test
    void getMyRequests_ok_invokesSearchService() throws Exception {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        when(myRequestsListQueryComposer.compose(any())).thenReturn("{}");
        JsonNode emptySearchResponse = mapper.readTree(new ClassPathResource("data/model/searchServiceEmptyResponse.json").getFile());
        when(searchService.query(any(), any())).thenReturn(emptySearchResponse);
        elasticsearchProperties.setLogResponseTimes(true);

        // When
        myRequestsService.getMyRequests(query);

        // Then
        verify(searchService).query(any(), any());
        elasticsearchProperties.setLogResponseTimes(false);
    }

    @Test
    void getMyRequests_exception_throwsServiceException() {
        // Given
        MyRequestsQuery query = new MyRequestsQuery();
        Mockito.doThrow(new RuntimeException("error")).when(myRequestsListQueryComposer).compose(any());

        // When
        ServiceException thrown = Assertions.assertThrows(ServiceException.class,
                () -> myRequestsService.getMyRequests(query),
                "Should throw ServiceException");

        // Then
        Assertions.assertEquals("Error listing my requests", thrown.getMessage(),
                "Exception thrown should have a valid message");
        Assertions.assertEquals("getMyRequests", thrown.getKey(),
                "Exception thrown should have a valid key");
    }
}
