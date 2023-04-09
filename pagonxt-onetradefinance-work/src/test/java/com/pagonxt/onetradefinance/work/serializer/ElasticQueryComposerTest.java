package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class ElasticQueryComposerTest {

    private static final String TIMEZONE = "Europe/Madrid";

    private final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .setTimeZone(TimeZone.getTimeZone(TIMEZONE));

    @Test
    void givenText_whenContains_returnsEscapedText() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "contains", "text");

        // Then
        assertEquals("*text*", result, "Result should match template");
    }

    @Test
    void givenNullText_whenContains_returnsEscapedNullText() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "contains", (String) null);

        // Then
        assertEquals("*null*", result, "Result should match template");
    }

    @Test
    void givenPlaintText_whenEscape_returnsJsonEscapedText() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "escape", "text");

        // Then
        assertEquals("\"text\"", result, "Result should match template");
    }

    @Test
    void givenTextWithQuotes_whenEscape_returnsJsonEscapedText() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "escape", "text\"with\"quotes");

        // Then
        assertEquals("\"text\\\"with\\\"quotes\"", result, "Result should match template");
    }

    @Test
    void givenNullText_whenEscape_returnsNullText() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "escape", (String) null);

        // Then
        assertEquals("null", result, "Result should match template");
    }

    @Test
    void givenJsonProcessingException_whenEscape_throwsServiceException() throws Exception {
        // Given
        ObjectMapper exceptionThrowingObjectMapper = mock(ObjectMapper.class);
        when(exceptionThrowingObjectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        ReflectionTestUtils.setField(elasticQueryComposer, "objectMapper", exceptionThrowingObjectMapper);

        // When
        ServiceException exceptionThrown = Assertions.assertThrows(ServiceException.class,
                () -> ReflectionTestUtils.invokeMethod(elasticQueryComposer, "escape", "text")
        );

        // Then
        assertEquals("Error serializing json string 'text'", exceptionThrown.getMessage(),
                "Exception message should match template");
    }

    @Test
    void givenValidTemplate_whenLoadTemplate_templateIsLoaded() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        ReflectionTestUtils.invokeMethod(elasticQueryComposer, "loadTemplate", "elastic-empty-query");

        // Then
        @SuppressWarnings("unchecked")
        Map<String, String> loadedTemplates = (Map<String, String>) ReflectionTestUtils.getField(elasticQueryComposer, "templates");
        assert loadedTemplates != null;
        assertNotNull(loadedTemplates.get("elastic-empty-query"), "Template should be loaded");
    }

    @Test
    void givenWrongTemplateName_whenLoadTemplate_templateIsNotLoaded() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        ReflectionTestUtils.invokeMethod(elasticQueryComposer, "loadTemplate", "invalid-template");

        // Then
        @SuppressWarnings("unchecked")
        Map<String, String> loadedTemplates = (Map<String, String>) ReflectionTestUtils.getField(elasticQueryComposer, "templates");
        assert loadedTemplates != null;
        assertNull(loadedTemplates.get("invalid-template"), "Template should not be available");
    }

    @Test
    void loadTemplate_whenIOException_throwsServiceException() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        ReflectionTestUtils.invokeMethod(elasticQueryComposer, "loadTemplate", "invalid-template");

        // Then
        @SuppressWarnings("unchecked")
        Map<String, String> loadedTemplates = (Map<String, String>) ReflectionTestUtils.getField(elasticQueryComposer, "templates");
        assert loadedTemplates != null;
        assertNull(loadedTemplates.get("invalid-template"), "Template should not be available");
    }

    @Test
    void givenValidTemplate_whenGetTemplate_returnsTemplateContents() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        String expectedResult = getRawFile("/data/elastic/elastic-empty-query.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "getTemplate", "elastic-empty-query");

        // Then
        assertEquals(expectedResult, result, "Loaded template should match template");
    }

    @Test
    void givenWrongTemplateName_whenGetTemplate_returnsNull() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "getTemplate", "invalid-template");

        // Then
        assertNull(result, "Should return null");
    }

    @Test
    void givenListOfConditions_whenGetOrCondition_returnsValidString() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        List<String> conditions = List.of("\"condition1\"", "\"condition2\"", "\"condition3\"");
        String expectedResult = getRawFile("/data/elastic/valid-or-string.txt");

        // When
        String result = elasticQueryComposer.getOrCondition(conditions);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenEmptyListOfConditions_whenGetOrCondition_returnsEmptyOrString() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        List<String> conditions = List.of();
        String expectedResult = getRawFile("/data/elastic/empty-or-string.txt");

        // When
        String result = elasticQueryComposer.getOrCondition(conditions);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @ParameterizedTest
    @MethodSource("getConditionMethod_argumentProvider")
    void givenField_getConditionMethod_returnsValidString(String conditionMethodName, String expectedResultTemplate, String fieldName, String scopeHierarchyType, Object argument) {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        String expectedResult = getRawFile(String.format("/data/elastic/%s.txt", expectedResultTemplate));

        // When
        List<Object> argumentList = new ArrayList<>();
        argumentList.add(fieldName);
        if (scopeHierarchyType != null) {
            argumentList.add(scopeHierarchyType);
        }
        if (argument != null) {
            argumentList.add(argument);
        }
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, conditionMethodName, argumentList.toArray());

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidPropertyField_composeSort_returnsValidString() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        String expectedResult = getRawFile("/data/elastic/valid-property-compose-sort.txt");

        // When
        SortField sortField = SortField.ofString("field", "field-mapping");
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidStringVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofString("variable", "variables.variable");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-string-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, -1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidIntegerNumberVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofInteger("variable", "variables.variable");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-integer-number-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidDoubleNumberVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofDouble("variable", "variables.variable");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-double-number-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidDateVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofDate("variable", "variables.variable");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-date-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, -1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidBooleanVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofBoolean("variable", "variables.variable");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-boolean-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidScopedStringVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofString("variable", "variables.variable", "root");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-scoped-string-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, -1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidScopedIntegerNumberVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofInteger("variable", "variables.variable", "root");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-scoped-integer-number-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidScopedDoubleNumberVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofDouble("variable", "variables.variable", "root");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-scoped-double-number-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidScopedDateVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofDate("variable", "variables.variable", "root");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-scoped-date-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, -1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidScopedBooleanVariable_composeSort_returnsValidString() {
        // Given
        SortField sortField = SortField.ofBoolean("variable", "variables.variable", "root");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-scoped-boolean-variable-compose-sort.txt");

        // When
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeSort", sortField, 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidConditions_composeQuery_returnsValidString() {
        // Given
        SortField sortField = SortField.ofString("field", "field-mapping");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-compose-query.txt");

        // When
        List<String> conditions = List.of("\"condition1\"", "\"condition2\"", "\"condition3\"");
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeQuery", conditions, 0, 5, "field", 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenNoConditions_composeQuery_returnsValidString() {
        // Given
        SortField sortField = SortField.ofString("field", "field-mapping");
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(List.of(sortField));
        String expectedResult = getRawFile("/data/elastic/valid-compose-query-no-conditions.txt");

        // When
        List<String> conditions = List.of();
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeQuery", conditions, 0, 5, "field", 1);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidConditions_composeQueryWithCustomSort_returnsValidString() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        String expectedResult = getRawFile("/data/elastic/valid-compose-query-custom-sort.txt");

        // When
        List<String> conditions = List.of("\"condition1\"", "\"condition2\"", "\"condition3\"");
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeQuery", conditions, 0, 5, "customSort");

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenNoConditions_composeQueryWithCustomSort_returnsValidString() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        String expectedResult = getRawFile("/data/elastic/valid-compose-query-no-conditions-custom-sort.txt");

        // When
        List<String> conditions = List.of();
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeQuery", conditions, 0, 5, "customSort");

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenValidConditions_composeQueryWithoutSort_returnsValidString() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        String expectedResult = getRawFile("/data/elastic/valid-compose-query-without-sort.txt");

        // When
        List<String> conditions = List.of("\"condition1\"", "\"condition2\"", "\"condition3\"");
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeQuery", conditions, 0, 5);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    @Test
    void givenNoConditions_composeQueryWithoutSort_returnsValidString() {
        // Given
        ElasticQueryComposer elasticQueryComposer = getMockedElasticQueryComposer(null);
        String expectedResult = getRawFile("/data/elastic/valid-compose-query-without-sort-without-conditions.txt");

        // When
        List<String> conditions = List.of();
        String result = ReflectionTestUtils.invokeMethod(elasticQueryComposer, "composeQuery", conditions, 0, 5);

        // Then
        assertEquals(expectedResult, result, "Result should match template");
    }

    private ElasticQueryComposer getMockedElasticQueryComposer(List<SortField> sortFields) {
        ElasticQueryComposer result = mock(ElasticQueryComposer.class, Mockito.CALLS_REAL_METHODS);
        ReflectionTestUtils.setField(result, "templates", new HashMap<String, String>());
        Map<String, SortField> sortFieldsMap = (sortFields == null) ? null :
                sortFields.stream().collect(Collectors.toMap(SortField::getFieldName, sortField -> sortField));
        ReflectionTestUtils.setField(result, "sortFields", sortFieldsMap);
        ReflectionTestUtils.setField(result, "objectMapper", mapper);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSZ");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        ReflectionTestUtils.setField(result, "basicDateFormat", simpleDateFormat);
        return result;
    }

    private String getRawFile(String fileName) {
        try {
            URL resource = getClass().getResource(fileName);
            assert resource != null;
            return Files.readString(Paths.get(resource.toURI()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(String.format("Error getting template '%s'", fileName), e);
        }
    }

    private static Stream<Arguments> getConditionMethod_argumentProvider() throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmssZ");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Madrid"));
        Date testDate = simpleDateFormat.parse("20220719-135300+0000");
        return Stream.of(

                // Properties:

                Arguments.of("getPropertyExistsCondition", "valid-property-exists", "field", null, null),
                Arguments.of("getPropertyExistsCondition", "null-property-exists", null, null, null),

                Arguments.of("getPropertyDoesNotExistCondition", "valid-property-does-not-exist", "field", null, null),
                Arguments.of("getPropertyDoesNotExistCondition", "null-property-does-not-exist", null, null, null),

                Arguments.of("getPropertyHasExactTextCondition", "valid-property-has-exact-text", "field", null, "exact-text"),
                Arguments.of("getPropertyHasExactTextCondition", "null-property-has-exact-text", null, null, "exact-text"),

                Arguments.of("getPropertyContainsTextCondition", "valid-property-contains-text", "field", null, "some-text"),
                Arguments.of("getPropertyContainsTextCondition", "null-property-contains-text", null, null, "some-text"),

                Arguments.of("getPropertyHasNumberCondition", "valid-property-has-number-int", "field", null, 12),
                Arguments.of("getPropertyHasNumberCondition", "null-property-has-number-int", null, null, 12),
                Arguments.of("getPropertyHasNumberCondition", "valid-property-has-number-double", "field", null, 12.34D),
                Arguments.of("getPropertyHasNumberCondition", "null-property-has-number-double", null, null, 12.34D),
                Arguments.of("getPropertyHasNumberCondition", "valid-property-has-number-double", "field", null, 12.34F),
                Arguments.of("getPropertyHasNumberCondition", "null-property-has-number-double", null, null, 12.34F),

                Arguments.of("getPropertyGTCondition", "valid-property-gt-int", "field", null, 12),
                Arguments.of("getPropertyGTCondition", "null-property-gt-int", null, null, 12),
                Arguments.of("getPropertyGTCondition", "valid-property-gt-double", "field", null, 12.34D),
                Arguments.of("getPropertyGTCondition", "null-property-gt-double", null, null, 12.34D),
                Arguments.of("getPropertyGTCondition", "valid-property-gt-double", "field", null, 12.34F),
                Arguments.of("getPropertyGTCondition", "null-property-gt-double", null, null, 12.34F),
                Arguments.of("getPropertyGTCondition", "valid-property-gt-date", "field", null, testDate),
                Arguments.of("getPropertyGTCondition", "null-property-gt-date", null, null, testDate),

                Arguments.of("getPropertyGTECondition", "valid-property-gte-int", "field", null, 12),
                Arguments.of("getPropertyGTECondition", "null-property-gte-int", null, null, 12),
                Arguments.of("getPropertyGTECondition", "valid-property-gte-double", "field", null, 12.34D),
                Arguments.of("getPropertyGTECondition", "null-property-gte-double", null, null, 12.34D),
                Arguments.of("getPropertyGTECondition", "valid-property-gte-double", "field", null, 12.34F),
                Arguments.of("getPropertyGTECondition", "null-property-gte-double", null, null, 12.34F),
                Arguments.of("getPropertyGTECondition", "valid-property-gte-date", "field", null, testDate),
                Arguments.of("getPropertyGTECondition", "null-property-gte-date", null, null, testDate),

                Arguments.of("getPropertyLTCondition", "valid-property-lt-int", "field", null, 12),
                Arguments.of("getPropertyLTCondition", "null-property-lt-int", null, null, 12),
                Arguments.of("getPropertyLTCondition", "valid-property-lt-double", "field", null, 12.34D),
                Arguments.of("getPropertyLTCondition", "null-property-lt-double", null, null, 12.34D),
                Arguments.of("getPropertyLTCondition", "valid-property-lt-double", "field", null, 12.34F),
                Arguments.of("getPropertyLTCondition", "null-property-lt-double", null, null, 12.34F),
                Arguments.of("getPropertyLTCondition", "valid-property-lt-date", "field", null, testDate),
                Arguments.of("getPropertyLTCondition", "null-property-lt-date", null, null, testDate),

                Arguments.of("getPropertyLTECondition", "valid-property-lte-int", "field", null, 12),
                Arguments.of("getPropertyLTECondition", "null-property-lte-int", null, null, 12),
                Arguments.of("getPropertyLTECondition", "valid-property-lte-double", "field", null, 12.34D),
                Arguments.of("getPropertyLTECondition", "null-property-lte-double", null, null, 12.34D),
                Arguments.of("getPropertyLTECondition", "valid-property-lte-double", "field", null, 12.34F),
                Arguments.of("getPropertyLTECondition", "null-property-lte-double", null, null, 12.34F),
                Arguments.of("getPropertyLTECondition", "valid-property-lte-date", "field", null, testDate),
                Arguments.of("getPropertyLTECondition", "null-property-lte-date", null, null, testDate),

                // Variables:

                Arguments.of("getVariableExistsCondition", "valid-variable-exists", "var", null, null),
                Arguments.of("getVariableExistsCondition", "null-variable-exists", null, null, null),

                Arguments.of("getVariableDoesNotExistCondition", "valid-variable-does-not-exist", "var", null, null),
                Arguments.of("getVariableDoesNotExistCondition", "null-variable-does-not-exist", null, null, null),

                Arguments.of("getVariableHasExactTextCondition", "valid-variable-has-exact-text", "var", null, "exact-text"),
                Arguments.of("getVariableHasExactTextCondition", "null-variable-has-exact-text", null, null, "exact-text"),

                Arguments.of("getVariableContainsTextCondition", "valid-variable-contains-text", "var", null, "some-text"),
                Arguments.of("getVariableContainsTextCondition", "null-variable-contains-text", null, null, "some-text"),

                Arguments.of("getVariableHasNumberCondition", "valid-variable-has-number-int", "var", null, 12),
                Arguments.of("getVariableHasNumberCondition", "null-variable-has-number-int", null, null, 12),
                Arguments.of("getVariableHasNumberCondition", "valid-variable-has-number-double", "var", null, 12.34D),
                Arguments.of("getVariableHasNumberCondition", "null-variable-has-number-double", null, null, 12.34D),
                Arguments.of("getVariableHasNumberCondition", "valid-variable-has-number-double", "var", null, 12.34F),
                Arguments.of("getVariableHasNumberCondition", "null-variable-has-number-double", null, null, 12.34F),

                Arguments.of("getVariableGTCondition", "valid-variable-gt-int", "var", null, 12),
                Arguments.of("getVariableGTCondition", "null-variable-gt-int", null, null, 12),
                Arguments.of("getVariableGTCondition", "valid-variable-gt-double", "var", null, 12.34D),
                Arguments.of("getVariableGTCondition", "null-variable-gt-double", null, null, 12.34D),
                Arguments.of("getVariableGTCondition", "valid-variable-gt-double", "var", null, 12.34F),
                Arguments.of("getVariableGTCondition", "null-variable-gt-double", null, null, 12.34F),
                Arguments.of("getVariableGTCondition", "valid-variable-gt-date", "var", null, testDate),
                Arguments.of("getVariableGTCondition", "null-variable-gt-date", null, null, testDate),

                Arguments.of("getVariableGTECondition", "valid-variable-gte-int", "var", null, 12),
                Arguments.of("getVariableGTECondition", "null-variable-gte-int", null, null, 12),
                Arguments.of("getVariableGTECondition", "valid-variable-gte-double", "var", null, 12.34D),
                Arguments.of("getVariableGTECondition", "null-variable-gte-double", null, null, 12.34D),
                Arguments.of("getVariableGTECondition", "valid-variable-gte-double", "var", null, 12.34F),
                Arguments.of("getVariableGTECondition", "null-variable-gte-double", null, null, 12.34F),
                Arguments.of("getVariableGTECondition", "valid-variable-gte-date", "var", null, testDate),
                Arguments.of("getVariableGTECondition", "null-variable-gte-date", null, null, testDate),

                Arguments.of("getVariableLTCondition", "valid-variable-lt-int", "var", null, 12),
                Arguments.of("getVariableLTCondition", "null-variable-lt-int", null, null, 12),
                Arguments.of("getVariableLTCondition", "valid-variable-lt-double", "var", null, 12.34D),
                Arguments.of("getVariableLTCondition", "null-variable-lt-double", null, null, 12.34D),
                Arguments.of("getVariableLTCondition", "valid-variable-lt-double", "var", null, 12.34F),
                Arguments.of("getVariableLTCondition", "null-variable-lt-double", null, null, 12.34F),
                Arguments.of("getVariableLTCondition", "valid-variable-lt-date", "var", null, testDate),
                Arguments.of("getVariableLTCondition", "null-variable-lt-date", null, null, testDate),

                Arguments.of("getVariableLTECondition", "valid-variable-lte-int", "var", null, 12),
                Arguments.of("getVariableLTECondition", "null-variable-lte-int", null, null, 12),
                Arguments.of("getVariableLTECondition", "valid-variable-lte-double", "var", null, 12.34D),
                Arguments.of("getVariableLTECondition", "null-variable-lte-double", null, null, 12.34D),
                Arguments.of("getVariableLTECondition", "valid-variable-lte-double", "var", null, 12.34F),
                Arguments.of("getVariableLTECondition", "null-variable-lte-double", null, null, 12.34F),
                Arguments.of("getVariableLTECondition", "valid-variable-lte-date", "var", null, testDate),
                Arguments.of("getVariableLTECondition", "null-variable-lte-date", null, null, testDate),

                // Scoped variables:

                Arguments.of("getVariableExistsCondition", "valid-scoped-variable-exists", "var", "root", null),
                Arguments.of("getVariableExistsCondition", "null-scoped-variable-exists", null, "root", null),

                Arguments.of("getVariableDoesNotExistCondition", "valid-scoped-variable-does-not-exist", "var", "root", null),
                Arguments.of("getVariableDoesNotExistCondition", "null-scoped-variable-does-not-exist", null, "root", null),

                Arguments.of("getVariableHasExactTextCondition", "valid-scoped-variable-has-exact-text", "var", "root", "exact-text"),
                Arguments.of("getVariableHasExactTextCondition", "null-scoped-variable-has-exact-text", null, "root", "exact-text"),

                Arguments.of("getVariableContainsTextCondition", "valid-scoped-variable-contains-text", "var", "root", "some-text"),
                Arguments.of("getVariableContainsTextCondition", "null-scoped-variable-contains-text", null, "root", "some-text"),

                Arguments.of("getVariableHasNumberCondition", "valid-scoped-variable-has-number-int", "var", "root", 12),
                Arguments.of("getVariableHasNumberCondition", "null-scoped-variable-has-number-int", null, "root", 12),
                Arguments.of("getVariableHasNumberCondition", "valid-scoped-variable-has-number-double", "var", "root", 12.34D),
                Arguments.of("getVariableHasNumberCondition", "null-scoped-variable-has-number-double", null, "root", 12.34D),
                Arguments.of("getVariableHasNumberCondition", "valid-scoped-variable-has-number-double", "var", "root", 12.34F),
                Arguments.of("getVariableHasNumberCondition", "null-scoped-variable-has-number-double", null, "root", 12.34F),

                Arguments.of("getVariableGTCondition", "valid-scoped-variable-gt-int", "var", "root", 12),
                Arguments.of("getVariableGTCondition", "null-scoped-variable-gt-int", null, "root", 12),
                Arguments.of("getVariableGTCondition", "valid-scoped-variable-gt-double", "var", "root", 12.34D),
                Arguments.of("getVariableGTCondition", "null-scoped-variable-gt-double", null, "root", 12.34D),
                Arguments.of("getVariableGTCondition", "valid-scoped-variable-gt-double", "var", "root", 12.34F),
                Arguments.of("getVariableGTCondition", "null-scoped-variable-gt-double", null, "root", 12.34F),
                Arguments.of("getVariableGTCondition", "valid-scoped-variable-gt-date", "var", "root", testDate),
                Arguments.of("getVariableGTCondition", "null-scoped-variable-gt-date", null, "root", testDate),

                Arguments.of("getVariableGTECondition", "valid-scoped-variable-gte-int", "var", "root", 12),
                Arguments.of("getVariableGTECondition", "null-scoped-variable-gte-int", null, "root", 12),
                Arguments.of("getVariableGTECondition", "valid-scoped-variable-gte-double", "var", "root", 12.34D),
                Arguments.of("getVariableGTECondition", "null-scoped-variable-gte-double", null, "root", 12.34D),
                Arguments.of("getVariableGTECondition", "valid-scoped-variable-gte-double", "var", "root", 12.34F),
                Arguments.of("getVariableGTECondition", "null-scoped-variable-gte-double", null, "root", 12.34F),
                Arguments.of("getVariableGTECondition", "valid-scoped-variable-gte-date", "var", "root", testDate),
                Arguments.of("getVariableGTECondition", "null-scoped-variable-gte-date", null, "root", testDate),

                Arguments.of("getVariableLTCondition", "valid-scoped-variable-lt-int", "var", "root", 12),
                Arguments.of("getVariableLTCondition", "null-scoped-variable-lt-int", null, "root", 12),
                Arguments.of("getVariableLTCondition", "valid-scoped-variable-lt-double", "var", "root", 12.34D),
                Arguments.of("getVariableLTCondition", "null-scoped-variable-lt-double", null, "root", 12.34D),
                Arguments.of("getVariableLTCondition", "valid-scoped-variable-lt-double", "var", "root", 12.34F),
                Arguments.of("getVariableLTCondition", "null-scoped-variable-lt-double", null, "root", 12.34F),
                Arguments.of("getVariableLTCondition", "valid-scoped-variable-lt-date", "var", "root", testDate),
                Arguments.of("getVariableLTCondition", "null-scoped-variable-lt-date", null, "root", testDate),

                Arguments.of("getVariableLTECondition", "valid-scoped-variable-lte-int", "var", "root", 12),
                Arguments.of("getVariableLTECondition", "null-scoped-variable-lte-int", null, "root", 12),
                Arguments.of("getVariableLTECondition", "valid-scoped-variable-lte-double", "var", "root", 12.34D),
                Arguments.of("getVariableLTECondition", "null-scoped-variable-lte-double", null, "root", 12.34D),
                Arguments.of("getVariableLTECondition", "valid-scoped-variable-lte-double", "var", "root", 12.34F),
                Arguments.of("getVariableLTECondition", "null-scoped-variable-lte-double", null, "root", 12.34F),
                Arguments.of("getVariableLTECondition", "valid-scoped-variable-lte-date", "var", "root", testDate),
                Arguments.of("getVariableLTECondition", "null-scoped-variable-lte-date", null, "root", testDate)
        );
    }
}
