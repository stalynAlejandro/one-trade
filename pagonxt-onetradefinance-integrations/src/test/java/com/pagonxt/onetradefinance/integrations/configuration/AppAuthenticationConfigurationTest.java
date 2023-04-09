package com.pagonxt.onetradefinance.integrations.configuration;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;

@UnitTest
class AppAuthenticationConfigurationTest {

    private static final String jwtSecret = "242a1b16cf2ba9a6a09715782c1bb675162355f1ade92ce169fb583e2b47aeea";

    @Test
    void hmacKey_ok_returnsValidKey() {
        // Given
        AppAuthenticationConfiguration appAuthenticationConfiguration = new AppAuthenticationConfiguration();
        ReflectionTestUtils.setField(appAuthenticationConfiguration, "jwtSecret", jwtSecret);

        // When
        Key result = appAuthenticationConfiguration.hmacKey();

        // Then
        Assertions.assertEquals("HmacSHA256", result.getAlgorithm(),
                "Key should have a valid algorithm");
        Assertions.assertEquals("RAW", result.getFormat(), "Key should have a valid format");
    }
}
