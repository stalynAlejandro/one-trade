package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.work.config.TestUtils;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class CaseQueryComposerTest {

    static ObjectMapper objectMapper;

    static DateFormatProperties dateFormatProperties;

    static CaseQueryComposer caseQueryComposer;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        caseQueryComposer = new CaseQueryComposer(dateFormatProperties, objectMapper);
    }

    @Test
    void composeGetCaseByCodeQuery_ok_returnsValidQuery() {
        // Given
        String expectedResult = TestUtils.getRawFile("/data/caseQuery/code-case-query.txt");

        // When
        String result = caseQueryComposer.composeGetCaseByCodeQuery("code");

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void composeGetCaseByIdQuery_ok_returnsValidQuery() {
        // Given
        String expectedResult = TestUtils.getRawFile("/data/caseQuery/id-case-query.txt");

        // When
        String result = caseQueryComposer.composeGetCaseByIdQuery("id");

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }
}
