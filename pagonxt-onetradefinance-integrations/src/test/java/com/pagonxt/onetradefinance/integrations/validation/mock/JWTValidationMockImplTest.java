package com.pagonxt.onetradefinance.integrations.validation.mock;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

@UnitTest
class JWTValidationMockImplTest {

    @InjectMocks
    JWTValidationMockImpl jwtValidationMockImpl;

    @Test
    void validate_ok_returnDecodedJWT() {
        // When
        DecodedJWT result = jwtValidationMockImpl.verify("test");
        // Then
        Assertions.assertEquals("J000104892", result.getClaim("sub").asString());
        Assertions.assertEquals("", result.getClaim("country").asString());
    }

}
