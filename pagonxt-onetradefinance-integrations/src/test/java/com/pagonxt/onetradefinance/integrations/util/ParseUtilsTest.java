package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class ParseUtilsTest {

    @ParameterizedTest
    @MethodSource("parseBooleanToYesNoArguments")
    void parseBooleanToYesNo_returnsValidAnswer(Object testObject, String expectedResult) {
        // Given

        // When
        String result = ParseUtils.parseBooleanToYesNo(testObject);

        // Then
        assertEquals(expectedResult, result, "Result should match expected result");
    }

    @ParameterizedTest
    @MethodSource("parseObjectToDoubleArguments")
    void parseObjectToDouble_returnsValidAnswer(Object testObject, Double expectedResult) {
        // Given

        // When
        Double result = ParseUtils.parseObjectToDouble(testObject);

        // Then
        assertEquals(expectedResult, result, "Result should match expected result");
    }

    @Test
    void parseDoubleToString() {
        // Given
        Double d = 34.12645;
        // When
        String result = ParseUtils.parseDoubleToString(d);
        // Then
        assertEquals("34.13", result);
    }


    @ParameterizedTest
    @CsvSource(value = {
            "es_es, '2 389 434,13'",
            "en_us, '2,389,434.13'",
            "unknown, '2,389,434.13'",
            "null, '2,389,434.13'"}
            , nullValues = {"null"})
    void parseDoubleToString_whenLocale_returnFormattedNumber(String locale, String formattedNumber) {
        // Given
        Double d = 2389434.12645;
        // When
        String result = ParseUtils.parseDoubleToString(d, locale);
        // Then
        assertEquals(formattedNumber, result);
    }

    @Test
    void parseDoubleToString_whenNullValue_returnNull() {
        // Given, When and Then
        assertNull(ParseUtils.parseDoubleToString(null, "es_es"));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "ES, es_es",
            "US, en_us",
            "unknown, es_es",
            "null, null"}
            , nullValues = {"null"})
    void getLocaleFromCountry_whenCountry_returnFormattedNumber(String country, String locale) {
        // Given, When and Then
        assertEquals(locale, ParseUtils.getLocaleFromCountry(country));
    }

    @Test
    void parseKey_ok_returnKey() {
        // Given, When and Then
        assertEquals("test_1", ParseUtils.parseKey("Test 1"));
    }

    @Test
    void parseKey_whenNull_returnNull() {
        // Given, When and Then
        assertNull(ParseUtils.parseKey(null));
    }

    private static Stream<Arguments> parseBooleanToYesNoArguments() {
        return Stream.of(
                Arguments.of(null, "no"),
                Arguments.of(Boolean.TRUE, "yes"),
                Arguments.of(Boolean.FALSE, "no"),
                Arguments.of(new Object(), "no")
        );
    }

    private static Stream<Arguments> parseObjectToDoubleArguments() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(12.34D, 12.34d),
                Arguments.of("12.34", 12.34d),
                Arguments.of(1234, 1234d),
                Arguments.of(new Object(), null)
        );
    }
}
