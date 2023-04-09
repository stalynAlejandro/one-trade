package com.pagonxt.onetradefinance.external.backend.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@UnitTest
class SerializerUtilsTest {

    @Mock
    private JsonGenerator jsonGenerator;

    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Test
    void writeStringFieldOrNull_whenStringValue_invokeWriteStringField() throws IOException {
        // When
        SerializerUtils.writeStringFieldOrNull(jsonGenerator, "var1", "value1");
        // Then
        verify(jsonGenerator, times(1)).writeStringField("var1", "value1");
    }

    @Test
    void writeNumberFieldOrNull_whenIntegerValue_invokeWriteNumberField() throws IOException {
        // When
        SerializerUtils.writeNumberFieldOrNull(jsonGenerator, "var1", 1);
        // Then
        verify(jsonGenerator, times(1)).writeNumberField("var1", 1);
    }

    @Test
    void writeNumberFieldOrNull_whenDoubleValue_invokeWriteNumberField() throws IOException {
        // When
        SerializerUtils.writeNumberFieldOrNull(jsonGenerator, "var1", 1d);
        // Then
        verify(jsonGenerator, times(1)).writeNumberField("var1", 1d);
    }

    @Test
    void writeDateFieldOrNull_whenDateValue_invokeWriteStringField() throws IOException {
        // Given
        Date date = new Date();
        // When
        SerializerUtils.writeDateFieldOrNull(jsonGenerator, "var1", date, dateTimeFormat);
        // Then
        verify(jsonGenerator, times(1)).writeStringField(eq("var1"), any());
    }

    @Test
    void writeStringFieldOrNull_whenNullStringValue_invokeWriteNullField() throws IOException {
        // When
        SerializerUtils.writeStringFieldOrNull(jsonGenerator, "var1", null);
        // Then
        verify(jsonGenerator, times(1)).writeNullField("var1");
    }

    @Test
    void writeNumberFieldOrNull_whenNullIntegerValue_invokeWriteNullField() throws IOException {
        // Given
        Integer value = null;
        // When
        SerializerUtils.writeNumberFieldOrNull(jsonGenerator, "var1", value);
        // Then
        verify(jsonGenerator, times(1)).writeNullField("var1");
    }


    @Test
    void writeNumberFieldOrNull_whenNullDoubleValue_invokeWriteNullField() throws IOException {
        // Given
        Double value = null;
        // When
        SerializerUtils.writeNumberFieldOrNull(jsonGenerator, "var1", value);
        // Then
        verify(jsonGenerator, times(1)).writeNullField("var1");
    }

    @Test
    void writeDateFieldOrNull_whenNullDateValue_invokeWriteNullField() throws IOException {
        // When
        SerializerUtils.writeDateFieldOrNull(jsonGenerator, "var1", null, dateTimeFormat);
        // Then
        verify(jsonGenerator, times(1)).writeNullField("var1");
    }

}
