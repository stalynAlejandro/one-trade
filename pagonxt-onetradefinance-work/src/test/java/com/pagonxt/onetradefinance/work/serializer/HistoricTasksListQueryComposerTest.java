package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class HistoricTasksListQueryComposerTest {

    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Europe/Madrid");

    @Spy
    private final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setTimeZone(TIME_ZONE);

    @Mock
    DateFormatProperties dateFormatProperties;

    @InjectMocks
    private HistoricTasksListQueryComposer historicTasksListQueryComposer;

    @Test
    void givenHistoricTasksQuery_compose_returnsValidResponse() {
        // Given
        HistoricTasksQuery query = new HistoricTasksQuery();
        query.setCode("CLE-TEST");
        query.setSortField("endDate");
        query.setSortOrder(1);
        query.setFromPage(3);
        query.setPageSize(5);
        String expectedResult = getRawFile("/data/historicTasks/request.txt");

        // When
        String result = historicTasksListQueryComposer.compose(query);
        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenDefaultHistoricTasksQuery_compose_returnsValidResponse() {
        // Given
        HistoricTasksQuery query = new HistoricTasksQuery();
        query.setCode("CLE-TEST");
        String expectedResult = getRawFile("/data/historicTasks/default-request.txt");

        // When
        String result = historicTasksListQueryComposer.compose(query);
        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    private String getRawFile(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            assert resource != null;
            return Files.readString(Paths.get(resource.toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(String.format("Error getting template '%s'", fileName), e);
        }
    }
}
