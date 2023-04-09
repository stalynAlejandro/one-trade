package com.pagonxt.onetradefinance.work.configuration;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class ElasticSearchPropertiesTest {

    @Test
    void testLogResponseTimes() {
        // Given
        ElasticsearchProperties properties = new ElasticsearchProperties();

        // When
        properties.setLogResponseTimes(true);

        // Then
        assertTrue(properties.isLogResponseTimes(), "isLogResponseTimes should return true");
    }
}
