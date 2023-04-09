package com.pagonxt.onetradefinance.integrations.validation.impl;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@UnitTest
class JWTValidationConfigurationTest {

    @InjectMocks
    JWTValidationConfiguration jwtValidationConfiguration;
    @Mock
    JWTValidationProperties jwtValidationProperties;

    @Test
    void jwtValidation_ok_returnBean() throws Exception {
        // Given
        when(jwtValidationProperties.isUseV2()).thenReturn(false);
        when(jwtValidationProperties.getUrl()).thenReturn(
                new URI("https://pkm-estructural-seguridad-dev.apps.ocp02.gts.dev.weu1.azure.paas.cloudcenter.corp"));
        when(jwtValidationProperties.getAudiences()).thenReturn(List.of("aud1", "aud2"));
        when(jwtValidationProperties.getIssuers()).thenReturn(List.of("iss1", "iss2"));
        // When
        JWTValidation result = jwtValidationConfiguration.jwtValidation();
        // Then
        assertTrue(result instanceof JWTValidationImpl);
    }
}
