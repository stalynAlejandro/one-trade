package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class ExportCollectionAdvanceRequestStdSerializerTest {

    private static final String TIMEZONE = "Europe/Madrid";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    @Spy
    private static DateFormatProperties dateFormatProperties;

    private static ExportCollectionAdvanceRequestStdSerializer exportCollectionAdvanceRequestStdSerializer;

    private final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setTimeZone(TimeZone.getTimeZone(TIMEZONE));

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone(TIMEZONE);
        dateFormatProperties.setDateFormat(DATE_FORMAT);
        exportCollectionAdvanceRequestStdSerializer = new ExportCollectionAdvanceRequestStdSerializer(dateFormatProperties);
    }

    @Test
    void whenSerializingExportCollectionAdvanceRequest_ok_returnsValidDates() throws IOException {
        // Given
        Writer writer = new StringWriter();
        JsonGenerator jsonGenerator = mapper.createGenerator(writer);
        ExportCollectionAdvanceRequest request = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance-request-not-serialized-dates.json").getFile(), ExportCollectionAdvanceRequest.class);
        // When
        exportCollectionAdvanceRequestStdSerializer.serialize(request, jsonGenerator, mapper.getSerializerProvider());
        jsonGenerator.flush();
        String result = writer.toString();
        // Then
        JsonNode resultJsonNode = mapper.readTree(result);
        String uploadedDateDocument = resultJsonNode.get("documents").get(0).get("uploadedDate").textValue();
        assertEquals("2022-05-27T06:54:53.741+02:00", uploadedDateDocument);
        String creationDate = resultJsonNode.get("exportCollection").get("creationDate").textValue();
        assertEquals("2022-05-30T18:14:53.741+02:00", creationDate);
        String useDate = resultJsonNode.get("exchangeInsurances").get(0).get("useDate").textValue();
        assertEquals("2022-08-01", useDate);
        String exchangeInsuranceUseDate = resultJsonNode.get("exchangeInsuranceUseDate").textValue();
        assertEquals("2022-08-06", exchangeInsuranceUseDate);
        String expiration = resultJsonNode.get("expiration").textValue();
        assertEquals("2022-08-05", expiration);
        String riskLineExpires = resultJsonNode.get("riskLine").get("expires").textValue();
        assertEquals("2020-06-06", riskLineExpires);
    }
}
