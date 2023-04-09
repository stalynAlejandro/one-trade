package com.pagonxt.onetradefinance.external.backend.configuration;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class ConcurrencyConfigurationPropertiesTest {

    @Test
    void setThreshold_ok_setsValidThreshold() {
        // Given
        ConcurrencyConfigurationProperties properties = new ConcurrencyConfigurationProperties();

        // When
        properties.setThreshold("PT15M");

        // Then
        assertEquals(Duration.ofMinutes(15), properties.getThreshold(), "Properties should have a valid threshold");
    }

    @Test
    void setThreshold_invalidString_throwsDateTimeParseException() {
        // Given
        ConcurrencyConfigurationProperties properties = new ConcurrencyConfigurationProperties();

        // When
        assertThrows(DateTimeParseException.class,
                () -> properties.setThreshold("invalid"),
                "Should throw DateTimeParseException");
    }

    @Test
    void setThreshold_null_throwsNullPointerException() {
        // Given
        ConcurrencyConfigurationProperties properties = new ConcurrencyConfigurationProperties();

        // When
        assertThrows(NullPointerException.class,
                () -> properties.setThreshold(null),
                "Should throw NullPointerException");
    }
}
