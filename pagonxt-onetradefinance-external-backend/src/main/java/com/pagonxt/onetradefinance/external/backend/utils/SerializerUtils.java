package com.pagonxt.onetradefinance.external.backend.utils;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

public class SerializerUtils {

    private SerializerUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static void writeStringFieldOrNull(JsonGenerator generator, String fieldName, String value) throws IOException {
        if (value == null) {
            generator.writeNullField(fieldName);
        } else {
            generator.writeStringField(fieldName, value);
        }
    }

    public static void writeNumberFieldOrNull(JsonGenerator generator, String fieldName, Integer value) throws IOException {
        if (value == null) {
            generator.writeNullField(fieldName);
        } else {
            generator.writeNumberField(fieldName, value);
        }
    }

    public static void writeNumberFieldOrNull(JsonGenerator generator, String fieldName, Double value) throws IOException {
        if (value == null) {
            generator.writeNullField(fieldName);
        } else {
            generator.writeNumberField(fieldName, value);
        }
    }

    public static void writeDateFieldOrNull(JsonGenerator generator, String fieldName, Date value, DateFormat dateFormat) throws IOException {
        if (value == null) {
            generator.writeNullField(fieldName);
        } else {
            String stringValue = dateFormat.format(value);
            generator.writeStringField(fieldName, stringValue);
        }
    }
}
