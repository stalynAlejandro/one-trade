package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.integrations.model.tasks.PagoNxtTaskItem;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.model.Task;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

@UnitTest
class PagoNxtTaskItemSerializerTest {

    private static final String TIMEZONE = "Europe/Madrid";

    private final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setTimeZone(TimeZone.getTimeZone(TIMEZONE));

    @Test
    void givenCase_whenSerialize_returnsValidObject() throws IOException {
        // Given
        PagoNxtTaskItemSerializer pagoNxtTaskItemSerializer = Mockito.mock(PagoNxtTaskItemSerializer.class, CALLS_REAL_METHODS);
        Mockito.when(pagoNxtTaskItemSerializer.getUUID()).thenReturn("4d4d8ed7-4369-4b95-aaef-8a0fc22e92e2");
        Task instance = mapper.readValue(new ClassPathResource("data/model/task.json").getFile(), Task.class);
        PagoNxtTaskItem expectedResult = mapper.readValue(new ClassPathResource("data/model/pagoNxtTaskItem.json").getFile(), PagoNxtTaskItem.class);

        // When
        PagoNxtTaskItem result = pagoNxtTaskItemSerializer.serialize(instance);

        // Then
        assertEquals(expectedResult, result, "Returned value should match template");
    }

    @Test
    void givenNoCase_whenSerialize_returnsNull() {
        // Given

        // When
        PagoNxtTaskItem result = new PagoNxtTaskItemSerializer().serialize(null);

        // Then
        assertNull(result, "Returned value should be null");
    }
}
