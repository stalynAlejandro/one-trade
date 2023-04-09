package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class LogUtilsTest {

    @Test
    void sanitizeString_ok_returnsSanitizedString() {
        // Given
        String expectedResult = "asd_qwe_uio_ghj";

        // When
        String result = LogUtils.sanitizeLog("asd\nqwe\ruio\tghj");

        // Then
        assertEquals(expectedResult, result, "Result should match pattern");
    }

    @Test
    void sanitizeString_null_returnsNull() {
        // When
        String result = LogUtils.sanitizeLog((String) null);

        // Then
        assertNull(result, "Result should be null");
    }

    @Test
    void sanitizeObject_ok_returnsSanitizedString() {
        // Given
        String expectedResult = "[asd_qwe_uio_ghj]";

        // When
        String result = LogUtils.sanitizeLog(List.of("asd\nqwe\ruio\tghj"));

        // Then
        assertEquals(expectedResult, result, "Result should match pattern");

    }

    @Test
    void sanitizeObject_null_returnsNull() {
        // When
        String result = LogUtils.sanitizeLog((Object) null);

        // Then
        assertNull(result, "Result should be null");
    }
}
