package com.pagonxt.onetradefinance.integrations.validation.mock;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class JWTValidationMockConfigurationTest {

    @InjectMocks
    JWTValidationMockConfiguration jwtValidationMockConfiguration;

    @Test
    void jwtValidation_ok_returnMockBean() {
        // When
        JWTValidation result = jwtValidationMockConfiguration.jwtValidation();
        // Then
        assertTrue(result instanceof JWTValidationMockImpl);
    }
}
