package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
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
class ExportCollectionModificationRequestStdSerializerTest {

    private static final String TIMEZONE = "Europe/Madrid";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static ExportCollectionModificationRequestStdSerializer exportCollectionModificationRequestStdSerializer;

    private static ObjectMapper mapper;

    @Spy
    private static DateFormatProperties dateFormatProperties;

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone(TIMEZONE);
        dateFormatProperties.setDateFormat(DATE_FORMAT);
        DateFormat dateTimeFormat = dateFormatProperties.getDateFormatInstance();
        mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .setDateFormat(dateTimeFormat)
                .setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        exportCollectionModificationRequestStdSerializer = new ExportCollectionModificationRequestStdSerializer(dateFormatProperties);
    }

    @Test
    void whenSerializingExportCollectionModificationRequest_ok_returnsValidDates() throws IOException {
        // Given
        Writer writer = new StringWriter();
        JsonGenerator jsonGenerator = mapper.createGenerator(writer);
        ExportCollectionModificationRequest request = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-modification-request-not-serialized-dates.json").getFile(), ExportCollectionModificationRequest.class);
        // When
        exportCollectionModificationRequestStdSerializer.serialize(request, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String result = writer.toString();
        // Then
        JsonNode resultJsonNode = mapper.readTree(result);
        String exportCollectionCreationDate = resultJsonNode.get("exportCollection").get("creationDate").asText();
        assertEquals("2022-05-30T18:14:53.741+02:00", exportCollectionCreationDate);
        String uploadedDateDocument = resultJsonNode.get("documents").get(0).get("uploadedDate").asText();
        assertEquals("2022-05-27T06:54:53.741+02:00", uploadedDateDocument);
    }
}
