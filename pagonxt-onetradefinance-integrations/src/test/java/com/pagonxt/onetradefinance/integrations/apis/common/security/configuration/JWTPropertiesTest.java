package com.pagonxt.onetradefinance.integrations.apis.common.security.configuration;

import com.pagonxt.onetradefinance.integrations.apis.common.model.JWTRequestBody;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class JWTPropertiesTest {

    @Test
    void getJWTRequestBody_ok_returnJWTRequestBody() {
        // Given
        JWTProperties jwtProperties = new JWTProperties();
        jwtProperties.setSub("sub");
        jwtProperties.setLocalUid("LUID");
        jwtProperties.setLocalRealm("LR");
        jwtProperties.setIss("iss");
        jwtProperties.setCountry("ES");
        // When
        JWTRequestBody result = jwtProperties.getJWTRequestBody();
        // Then
        assertEquals("sub", result.getSub());
        assertEquals("LUID", result.getLocalUid());
        assertEquals("LR", result.getLocalRealm());
        assertEquals("iss", result.getIss());
        assertEquals("ES", result.getCountry());
    }
}
