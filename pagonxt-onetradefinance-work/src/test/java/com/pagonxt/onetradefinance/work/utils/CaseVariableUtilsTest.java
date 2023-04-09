package com.pagonxt.onetradefinance.work.utils;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.model.Case;
import com.pagonxt.onetradefinance.work.service.model.Variable;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class CaseVariableUtilsTest {

    @Test
    void givenStringField_whenGetString_returnsValue() {
        // Given
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn("name");
        when(variable.getType()).thenReturn("string");
        when(variable.getTextValue()).thenReturn("value");
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of(variable));

        // When
        String result = CaseVariableUtils.getString(instance, "name");

        // Then
        assertEquals("value", result, "Returned value should match expected result");
    }

    @Test
    void givenNoField_whenGetString_returnsNull() {
        // Given
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        String result = CaseVariableUtils.getString(instance, "name");

        // Then
        assertNull(result, "Returned value should be null");
    }
    @Test
    void givenStringFieldOrDefault_whenGetString_returnsValue() {
        // Given
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn("name");
        when(variable.getType()).thenReturn("string");
        when(variable.getTextValue()).thenReturn("value");
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of(variable));

        // When
        String result = CaseVariableUtils.getStringOrDefault(instance, "name", "defaultString");

        // Then
        assertEquals("value", result, "Returned value should match expected result");
    }
    @Test
    void givenNoField_whenGetStringOrDefault_returnsDefault() {
        // Given
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        String result = CaseVariableUtils.getStringOrDefault(instance, "name", "defaultString");

        // Then
        assertEquals("defaultString", result, "Returned value should match default value");
    }
    @Test
    void givenDoubleField_whenGetDouble_returnsValue() {
        // Given
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn("name");
        when(variable.getType()).thenReturn("double");
        when(variable.getRawValue()).thenReturn("1.5");
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of(variable));

        // When
        Double result = CaseVariableUtils.getDouble(instance, "name");

        // Then
        assertEquals(1.5, result, "Returned value should match expected result");
    }

    @Test
    void givenNoField_whenGetDouble_returnsNull() {
        // Given
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Double result = CaseVariableUtils.getDouble(instance, "name");

        // Then
        assertNull(result, "Returned value should be null");
    }

    @Test
    void givenIntegerField_whenGetInteger_returnsValue() {
        // Given
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn("name");
        when(variable.getType()).thenReturn("integer");
        when(variable.getRawValue()).thenReturn("12");
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of(variable));

        // When
        Integer result = CaseVariableUtils.getInteger(instance, "name");

        // Then
        assertEquals(12, result, "Returned value should match expected result");
    }

    @Test
    void givenNoField_whenGetInteger_returnsNull() {
        // Given
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Integer result = CaseVariableUtils.getInteger(instance, "name");

        // Then
        assertNull(result, "Returned value should be null");
    }

    @Test
    void givenDateField_whenGetDate_returnsValue() throws Exception {
        // Given
        Variable variable = mock(Variable.class);
        when(variable.getName()).thenReturn("name");
        when(variable.getType()).thenReturn("date");
        String dateString = "20220727T085825";
        when(variable.getRawValue()).thenReturn(dateString);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        Date date = simpleDateFormat.parse(dateString);
        when(variable.getDateValue()).thenReturn(date);
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of(variable));

        // When
        Date result = CaseVariableUtils.getDate(instance, "name");

        // Then
        assertEquals(date, result, "Returned value should match expected result");
    }

    @Test
    void givenNoField_whenGetDate_returnsNull() {
        // Given
        Case instance = mock(Case.class);
        when(instance.getVariables()).thenReturn(List.of());

        // When
        Date result = CaseVariableUtils.getDate(instance, "name");

        // Then
        assertNull(result, "Returned value should be null");
    }

}
