package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.apache.http.client.config.RequestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@UnitTest
class ConfigurationUtilsTest {

    @Test
    void generateFactory_ok_returnsHttpComponentsClientHttpRequestFactory() {
        // Given
        int timeout = 100;
        // When
        HttpComponentsClientHttpRequestFactory result = ConfigurationUtils.generateFactory(timeout);
        // Then
        Field requestConfigField = ReflectionUtils.findField(HttpComponentsClientHttpRequestFactory.class, "requestConfig", RequestConfig.class);
        assertNotNull(requestConfigField);
        requestConfigField.setAccessible(true);
        RequestConfig requestConfig = (RequestConfig) ReflectionUtils.getField(requestConfigField, result);
        assertNotNull(requestConfig);
        assertEquals(timeout, requestConfig.getConnectTimeout());
        assertEquals(timeout, requestConfig.getConnectionRequestTimeout());
        assertEquals(timeout, requestConfig.getSocketTimeout());
    }
}
