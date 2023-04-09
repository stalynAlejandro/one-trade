package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.OperationType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class OperationTypeStdSerializerTest {

    private static final String TIMEZONE = "Europe/Madrid";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setTimeZone(TimeZone.getTimeZone(TIMEZONE));

    @BeforeAll
    static void setup() {
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setDateFormat(DATE_FORMAT);
        dateFormatProperties.setTimeZone(TIMEZONE);
        mapper.setDateFormat(dateFormatProperties.getDateFormatInstance());
    }

    private final OperationTypeStdSerializer operationTypeStdSerializer = new OperationTypeStdSerializer();

    @Test
    void whenSerializingDocument_ok_returnsValidData() throws IOException {
        // Given
        Writer writer = new StringWriter();
        JsonGenerator jsonGenerator = mapper.createGenerator(writer);
        OperationType operationType = mapper.readValue(new ClassPathResource("data/model/operationType.json").getFile(), OperationType.class);
        JsonNode expectedJsonNode = mapper.readTree(new ClassPathResource("data/model/serialized-operationType.json").getFile());

        // When
        operationTypeStdSerializer.serialize(operationType, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String result = writer.toString();

        // Then
        JsonNode resultJsonNode = mapper.readTree(result);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }
}
