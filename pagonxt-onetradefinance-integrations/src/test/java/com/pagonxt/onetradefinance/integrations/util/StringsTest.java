package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class StringsTest {

    @ParameterizedTest
    @MethodSource("returnsValidResponseArguments")
    void isBlank_ok_returnsValidResponse(String testString, boolean expectedResult) {
        // Given

        // When
        boolean result = Strings.isBlank(testString);

        // Then
        assertEquals(expectedResult, result, "Result should match expected result");
    }

    @ParameterizedTest
    @CsvSource(value = {"str, str",
            "null, ''"},
            nullValues = {"null"})
    void emptyIfNull_ok_returnString(String input, String expectedOutput) {
        assertEquals(expectedOutput, Strings.emptyIfNull(input));
    }

    private static Stream<Arguments> returnsValidResponseArguments() {
        return Stream.of(
                Arguments.of(null, true),
                Arguments.of("", true),
                Arguments.of(" ", true),
                Arguments.of("Hi", false)
        );
    }
}
