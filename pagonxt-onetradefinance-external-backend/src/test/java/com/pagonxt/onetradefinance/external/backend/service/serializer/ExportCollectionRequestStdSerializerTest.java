package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class ExportCollectionRequestStdSerializerTest {

    private static final String TIMEZONE = "Europe/Madrid";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static ExportCollectionRequestStdSerializer exportCollectionRequestStdSerializer;

    @Spy
    private static DateFormatProperties dateFormatProperties;

    private final static ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setTimeZone(TimeZone.getTimeZone(TIMEZONE));

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setDateFormat(DATE_FORMAT);
        dateFormatProperties.setTimeZone(TIMEZONE);
        DateFormat df = dateFormatProperties.getDateFormatInstance();
        mapper.setDateFormat(df);
        exportCollectionRequestStdSerializer = new ExportCollectionRequestStdSerializer(dateFormatProperties);
    }

    @Test
    void whenSerializingExportCollectionRequest_ok_returnsValidDates() throws IOException {
        // Given
        Writer writer = new StringWriter();
        JsonGenerator jsonGenerator = mapper.createGenerator(writer);
        ExportCollectionRequest request = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-request-not-serialized-dates.json").getFile(), ExportCollectionRequest.class);
        // When
        exportCollectionRequestStdSerializer.serialize(request, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String result = writer.toString();
        // Then
        JsonNode resultJsonNode = mapper.readTree(result);
        String creationDate = resultJsonNode.get("creationDate").textValue();
        assertEquals("2022-05-27T06:59:53.741+02:00", creationDate);
        String advanceRiskLineDueDate = resultJsonNode.get("advanceRiskLine").get("expires").textValue();
        assertEquals("2020-06-06", advanceRiskLineDueDate);
        String advanceDueDate = resultJsonNode.get("advanceExpiration").textValue();
        assertEquals("2023-08-07", advanceDueDate);
        String uploadedDateDocument = resultJsonNode.get("documents").get(0).get("uploadedDate").textValue();
        assertEquals("2022-05-27T06:54:53.741+02:00", uploadedDateDocument);
        String slaEnd = resultJsonNode.get("slaEnd").textValue();
        assertEquals("2022-05-25T07:41:33.741+02:00", slaEnd);
    }
}
