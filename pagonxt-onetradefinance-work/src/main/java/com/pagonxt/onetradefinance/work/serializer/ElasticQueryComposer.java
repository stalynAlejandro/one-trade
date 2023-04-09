package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.work.serializer.SortField.*;

/**
 * class for elastic queries
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public abstract class ElasticQueryComposer {

    private static final String RESOURCE_FILE_TEMPLATE = "/templates/elastic/%s.txt";

    private static final Map<String, String> SORT_TYPE_FILENAMES = Map.of(
            STRING, "variable-sort-string",
            INTEGER, "variable-sort-int-number",
            DOUBLE, "variable-sort-double-number",
            DATE, "variable-sort-date",
            BOOLEAN, "variable-sort-boolean"
    );
    private static final Map<String, String> SCOPED_SORT_TYPE_FILENAMES = Map.of(
            STRING, "scoped-variable-sort-string",
            INTEGER, "scoped-variable-sort-int-number",
            DOUBLE, "scoped-variable-sort-double-number",
            DATE, "scoped-variable-sort-date",
            BOOLEAN, "scoped-variable-sort-boolean"
    );

    // File template names:
    private static final String ELASTIC_QUERY = "elastic-query";
    private static final String ELASTIC_EMPTY_QUERY = "elastic-empty-query";
    private static final String ELASTIC_QUERY_NO_SORT = "elastic-query-no-sort";
    private static final String ELASTIC_EMPTY_QUERY_NO_SORT = "elastic-empty-query-no-sort";
    private static final String SORT = "sort";
    private static final String OR_CONDITION = "or-condition";
    private static final String PROPERTY_EXISTS_CONDITION = "property-exists-condition";
    private static final String PROPERTY_DOES_NOT_EXIST_CONDITION = "property-does-not-exist-condition";
    private static final String PROPERTY_HAS_EXACT_TEXT_CONDITION = "property-has-exact-text-condition";
    private static final String PROPERTY_CONTAINS_TEXT_CONDITION = "property-contains-text-condition";
    private static final String PROPERTY_HAS_INT_NUMBER_CONDITION = "property-has-int-number-condition";
    private static final String PROPERTY_HAS_DOUBLE_NUMBER_CONDITION = "property-has-double-number-condition";
    private static final String PROPERTY_HAS_INT_COMPARISON_CONDITION = "property-has-int-comparison-condition";
    private static final String PROPERTY_HAS_DOUBLE_COMPARISON_CONDITION = "property-has-double-comparison-condition";
    private static final String PROPERTY_HAS_DATE_COMPARISON_CONDITION = "property-has-date-comparison-condition";
    private static final String VARIABLE_EXISTS_CONDITION = "variable-exists-condition";
    private static final String VARIABLE_DOES_NOT_EXIST_CONDITION = "variable-does-not-exist-condition";
    private static final String VARIABLE_HAS_EXACT_TEXT_CONDITION = "variable-has-exact-text-condition";
    private static final String VARIABLE_CONTAINS_TEXT_CONDITION = "variable-contains-text-condition";
    private static final String VARIABLE_HAS_INT_NUMBER_CONDITION = "variable-has-int-number-condition";
    private static final String VARIABLE_HAS_DOUBLE_NUMBER_CONDITION = "variable-has-double-number-condition";
    private static final String VARIABLE_HAS_INT_COMPARISON_CONDITION = "variable-has-int-comparison-condition";
    private static final String VARIABLE_HAS_DOUBLE_COMPARISON_CONDITION = "variable-has-double-comparison-condition";
    private static final String VARIABLE_HAS_DATE_COMPARISON_CONDITION = "variable-has-date-comparison-condition";
    private static final String SCOPED_VARIABLE_EXISTS_CONDITION = "scoped-variable-exists-condition";
    private static final String SCOPED_VARIABLE_DOES_NOT_EXIST_CONDITION = "scoped-variable-does-not-exist-condition";
    private static final String SCOPED_VARIABLE_HAS_EXACT_TEXT_CONDITION = "scoped-variable-has-exact-text-condition";
    private static final String SCOPED_VARIABLE_CONTAINS_TEXT_CONDITION = "scoped-variable-contains-text-condition";
    private static final String SCOPED_VARIABLE_HAS_INT_NUMBER_CONDITION = "scoped-variable-has-int-number-condition";
    private static final String SCOPED_VARIABLE_HAS_DOUBLE_NUMBER_CONDITION = "scoped-variable-has-double-number-condition";
    private static final String SCOPED_VARIABLE_HAS_INT_COMPARISON_CONDITION = "scoped-variable-has-int-comparison-condition";
    private static final String SCOPED_VARIABLE_HAS_DOUBLE_COMPARISON_CONDITION = "scoped-variable-has-double-comparison-condition";
    private static final String SCOPED_VARIABLE_HAS_DATE_COMPARISON_CONDITION = "scoped-variable-has-date-comparison-condition";

    private static final int DEFAULT_START_ITEM = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final DateFormat basicDateFormat;

    private final Map<String, String> templates = new HashMap<>();

    private final Map<String, SortField> sortFields = new HashMap<>();

    private final ObjectMapper objectMapper;

    /**
     * constructor method
     * @param dateFormatProperties : date format properties
     * @param objectMapper : an ObjectMapper
     */
    protected ElasticQueryComposer(DateFormatProperties dateFormatProperties, ObjectMapper objectMapper) {
        this(dateFormatProperties, objectMapper, null);
    }

    /**
     * constructor method
     * @param dateFormatProperties : date format properties
     * @param objectMapper  : an ObjectMapper
     * @param sortFields    : a list with fields to sort
     */
    protected ElasticQueryComposer(DateFormatProperties dateFormatProperties, ObjectMapper objectMapper, List<SortField> sortFields) {
        this.objectMapper = objectMapper;
        this.basicDateFormat = dateFormatProperties.getDateFormatInstance("yyyyMMdd'T'HHmmss.SSSZ");
        if (sortFields != null) {
            setSortFields(sortFields);
        }
    }

    /**
     * class method
     * @param conditions    : a list of conditions
     * @param fromPage      : FromPage
     * @param pageSize      : PageSize
     * @param sortField     : SortField
     * @param sortDirection : sortDirection
     * @return a string object
     */
    protected String composeQuery(List<String> conditions, Integer fromPage,
                                  Integer pageSize, String sortField, Integer sortDirection) {
        Integer size = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
        Integer fromItem = (fromPage == null || fromPage < 0) ? DEFAULT_START_ITEM : fromPage * size;
        String sort = composeSort(sortFields.get(sortField), sortDirection);
        return conditions.isEmpty() ?
                buildQuery(fromItem, size, sort) :
                buildQuery(conditions, fromItem, size, sort);
    }

    /**
     * class method
     * @param conditions: a list of conditions
     * @param fromPage  : FromPage
     * @param pageSize  : PageSize
     * @param customSort: sortDirection
     * @return a string object
     */
    protected String composeQuery(List<String> conditions, Integer fromPage, Integer pageSize, String customSort) {
        Integer size = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
        Integer fromItem = (fromPage == null || fromPage < 0) ? DEFAULT_START_ITEM : fromPage * size;
        return conditions.isEmpty() ?
                buildQuery(fromItem, size, customSort) :
                buildQuery(conditions, fromItem, size, customSort);
    }

    /**
     * class method
     * @param conditions: a list of conditions
     * @param fromPage  : FromPage
     * @param pageSize  : PageSize
     * @return a string object
     */
    protected String composeQuery(List<String> conditions, Integer fromPage, Integer pageSize) {
        Integer size = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
        Integer fromItem = (fromPage == null || fromPage < 0) ? DEFAULT_START_ITEM : fromPage * size;
        return conditions.isEmpty() ?
                buildQuery(fromItem, size) :
                buildQuery(conditions, fromItem, size);
    }

    /**
     * class method
     * @param from  : an integer with from value
     * @param size  : an integer with size value
     * @return a string object
     */
    private String buildQuery(Integer from, Integer size) {
        return String.format(getTemplate(ELASTIC_EMPTY_QUERY_NO_SORT),
                from, size);
    }

    /**
     * class method
     * @param conditions : list of conditions
     * @param from       : an integer with from value
     * @param size       : an integer with size value
     * @return a string object
     */
    private String buildQuery(List<String> conditions, Integer from, Integer size) {
        String conditionsString = conditions.parallelStream().collect(Collectors.joining(","));
        return String.format(getTemplate(ELASTIC_QUERY_NO_SORT),
                from, size,
                conditionsString);
    }

    /**
     * class method
     * @param from  : an integer with from value
     * @param size  : an integer with size value
     * @param sort  : a string with sort value
     * @return a string object
     */
    private String buildQuery(Integer from, Integer size, String sort) {
        return String.format(getTemplate(ELASTIC_EMPTY_QUERY),
                from, size, sort);
    }

    /**
     * class method
     * @param conditions : list of conditions
     * @param from  : an integer with from value
     * @param size  : an integer with size value
     * @param sort  : a string with sort value
     * @return a string object
     */
    private String buildQuery(List<String> conditions, Integer from, Integer size, String sort) {
        String conditionsString = conditions.parallelStream().collect(Collectors.joining(","));
        return String.format(getTemplate(ELASTIC_QUERY),
                from, size, sort,
                conditionsString);
    }

    /**
     * class method
     * @param sortField     : a SortField object
     * @param sortDirection : an integer with the sort direction
     * @return a string object
     */
    private String composeSort(SortField sortField, Integer sortDirection) {
        String orderString = (sortDirection == null || sortDirection >= 0) ? "asc" : "desc";
        String fieldMapping = sortField.getFieldMapping();
        if (fieldMapping.startsWith("variables.")) {
            String field = fieldMapping.substring(fieldMapping.indexOf('.') + 1);
            String scopeHierarchyType = sortField.getScopeHierarchyType();
            if (scopeHierarchyType == null) {
                String template = getTemplate(SORT_TYPE_FILENAMES.get(sortField.getDataType()));
                return String.format(template, orderString, field);
            } else {
                String template = getTemplate(SCOPED_SORT_TYPE_FILENAMES.get(sortField.getDataType()));
                return String.format(template, orderString, field, scopeHierarchyType);
            }
        } else {
            return String.format(getTemplate(SORT), fieldMapping, orderString);
        }
    }

    /**
     * class method
     * @param conditions : list of conditions
     * @return a string object
     */
    protected String getOrCondition(List<String> conditions) {
        String conditionsString = conditions.parallelStream().collect(Collectors.joining(","));
        return String.format(getTemplate(OR_CONDITION), conditionsString);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @return a string object
     */
    protected String getPropertyExistsCondition(String propertyName) {
        return String.format(getTemplate(PROPERTY_EXISTS_CONDITION), propertyName);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @return a string object
     */
    protected String getPropertyDoesNotExistCondition(String propertyName) {
        return String.format(getTemplate(PROPERTY_DOES_NOT_EXIST_CONDITION), propertyName);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param requiredValue: a string with required value
     * @return a string object
     */
    protected String getPropertyHasExactTextCondition(String propertyName, String requiredValue) {
        return String.format(getTemplate(PROPERTY_HAS_EXACT_TEXT_CONDITION), propertyName, escape(requiredValue));
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param requiredValue: a string with required value
     * @return a string object
     */
    protected String getPropertyContainsTextCondition(String propertyName, String requiredValue) {
        return String
                .format(getTemplate(PROPERTY_CONTAINS_TEXT_CONDITION), propertyName, escape(contains(requiredValue)));
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param requiredValue: an integer with required value
     * @return a string object
     */
    protected String getPropertyHasNumberCondition(String propertyName, Integer requiredValue) {
        return String.format(getTemplate(PROPERTY_HAS_INT_NUMBER_CONDITION), propertyName, requiredValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param requiredValue: a Double object with required value
     * @return a string object
     */
    protected String getPropertyHasNumberCondition(String propertyName, Double requiredValue) {
        return String
                .format(Locale.ROOT, getTemplate(PROPERTY_HAS_DOUBLE_NUMBER_CONDITION), propertyName, requiredValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param requiredValue: a Float object with required value
     * @return a string object
     */
    protected String getPropertyHasNumberCondition(String propertyName, Float requiredValue) {
        return String
                .format(Locale.ROOT, getTemplate(PROPERTY_HAS_DOUBLE_NUMBER_CONDITION), propertyName, requiredValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : an integer with the limit value
     * @return a string object
     */
    protected String getPropertyGTCondition(String propertyName, Integer limitValue) {
        return getPropertyHasNumberCompared(propertyName, "gt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : an integer with the limit value
     * @return a string object
     */
    protected String getPropertyGTECondition(String propertyName, Integer limitValue) {
        return getPropertyHasNumberCompared(propertyName, "gte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : an integer with the limit value
     * @return a string object
     */
    protected String getPropertyLTCondition(String propertyName, Integer limitValue) {
        return getPropertyHasNumberCompared(propertyName, "lt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : an integer with the limit value
     * @return a string object
     */
    protected String getPropertyLTECondition(String propertyName, Integer limitValue) {
        return getPropertyHasNumberCompared(propertyName, "lte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Double object with the limit value
     * @return a string object
     */
    protected String getPropertyGTCondition(String propertyName, Double limitValue) {
        return getPropertyHasNumberCompared(propertyName, "gt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Double object with the limit value
     * @return a string object
     */
    protected String getPropertyGTECondition(String propertyName, Double limitValue) {
        return getPropertyHasNumberCompared(propertyName, "gte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Double object with the limit value
     * @return a string object
     */
    protected String getPropertyLTCondition(String propertyName, Double limitValue) {
        return getPropertyHasNumberCompared(propertyName, "lt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Double object with the limit value
     * @return a string object
     */
    protected String getPropertyLTECondition(String propertyName, Double limitValue) {
        return getPropertyHasNumberCompared(propertyName, "lte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Float object with the limit value
     * @return a string object
     */
    protected String getPropertyGTCondition(String propertyName, Float limitValue) {
        return getPropertyHasNumberCompared(propertyName, "gt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Float object with the limit value
     * @return a string object
     */
    protected String getPropertyGTECondition(String propertyName, Float limitValue) {
        return getPropertyHasNumberCompared(propertyName, "gte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Float object with the limit value
     * @return a string object
     */
    protected String getPropertyLTCondition(String propertyName, Float limitValue) {
        return getPropertyHasNumberCompared(propertyName, "lt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Float object with the limit value
     * @return a string object
     */
    protected String getPropertyLTECondition(String propertyName, Float limitValue) {
        return getPropertyHasNumberCompared(propertyName, "lte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Date object with the limit value
     * @return a string object
     */
    protected String getPropertyGTCondition(String propertyName, Date limitValue) {
        return getPropertyHasDateCompared(propertyName, "gt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Date object with the limit value
     * @return a string object
     */
    protected String getPropertyGTECondition(String propertyName, Date limitValue) {
        return getPropertyHasDateCompared(propertyName, "gte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Date object with the limit value
     * @return a string object
     */
    protected String getPropertyLTCondition(String propertyName, Date limitValue) {
        return getPropertyHasDateCompared(propertyName, "lt", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param limitValue   : a Date object with the limit value
     * @return a string object
     */
    protected String getPropertyLTECondition(String propertyName, Date limitValue) {
        return getPropertyHasDateCompared(propertyName, "lte", limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param comparison   : a string with comparison value
     * @param limitValue   : an integer with the limit value
     * @return a string object
     */
    private String getPropertyHasNumberCompared(String propertyName, String comparison, Integer limitValue) {
        return String.format(getTemplate(PROPERTY_HAS_INT_COMPARISON_CONDITION), propertyName, comparison, limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param comparison   : a string with comparison value
     * @param limitValue   : a Double object with the limit value
     * @return a string object
     */
    private String getPropertyHasNumberCompared(String propertyName, String comparison, Double limitValue) {
        return String.format(Locale.ROOT,
                getTemplate(PROPERTY_HAS_DOUBLE_COMPARISON_CONDITION), propertyName, comparison, limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param comparison   : a string with comparison value
     * @param limitValue   : a Float object with the limit value
     * @return a string object
     */
    private String getPropertyHasNumberCompared(String propertyName, String comparison, Float limitValue) {
        return String.format(Locale.ROOT,
                getTemplate(PROPERTY_HAS_DOUBLE_COMPARISON_CONDITION), propertyName, comparison, limitValue);
    }

    /**
     * class method
     * @param propertyName : a string with property Name value
     * @param comparison   : a string with comparison value
     * @param date         : a Date object with the date value
     * @return a string object
     */
    private String getPropertyHasDateCompared(String propertyName, String comparison, Date date) {
        String formattedDate = basicDateFormat.format(date);
        return String
                .format(getTemplate(PROPERTY_HAS_DATE_COMPARISON_CONDITION), propertyName, comparison, formattedDate);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @return a string object
     */
    protected String getVariableExistsCondition(String variableName) {
        return String.format(getTemplate(VARIABLE_EXISTS_CONDITION), variableName);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @return a string object
     */
    protected String getVariableDoesNotExistCondition(String variableName) {
        return String.format(getTemplate(VARIABLE_DOES_NOT_EXIST_CONDITION), variableName);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param requiredValue: a string with required value
     * @return a string object
     */
    protected String getVariableHasExactTextCondition(String variableName, String requiredValue) {
        return String.format(getTemplate(VARIABLE_HAS_EXACT_TEXT_CONDITION), variableName, escape(requiredValue));
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param requiredValue: a string with required value
     * @return a string object
     */
    protected String getVariableContainsTextCondition(String variableName, String requiredValue) {
        return String
                .format(getTemplate(VARIABLE_CONTAINS_TEXT_CONDITION), variableName, escape(contains(requiredValue)));
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param requiredValue: an integer with required value
     * @return a string object
     */
    protected String getVariableHasNumberCondition(String variableName, Integer requiredValue) {
        return String.format(getTemplate(VARIABLE_HAS_INT_NUMBER_CONDITION), variableName, requiredValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param requiredValue: a Double object with required value
     * @return a string object
     */
    protected String getVariableHasNumberCondition(String variableName, Double requiredValue) {
        return String
                .format(Locale.ROOT, getTemplate(VARIABLE_HAS_DOUBLE_NUMBER_CONDITION), variableName, requiredValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param requiredValue: a Float object with required value
     * @return a string object
     */
    protected String getVariableHasNumberCondition(String variableName, Float requiredValue) {
        return String
                .format(Locale.ROOT, getTemplate(VARIABLE_HAS_DOUBLE_NUMBER_CONDITION), variableName, requiredValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : an integer with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : an integer with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : an integer with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : an integer with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Double object with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, Double limitValue) {
        return getVariableHasNumberCompared(variableName, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Double object with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, Double limitValue) {
        return getVariableHasNumberCompared(variableName, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Double object with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, Double limitValue) {
        return getVariableHasNumberCompared(variableName, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Double object with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, Double limitValue) {
        return getVariableHasNumberCompared(variableName, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Float object with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, Float limitValue) {
        return getVariableHasNumberCompared(variableName, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Float object with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, Float limitValue) {
        return getVariableHasNumberCompared(variableName, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Float object with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, Float limitValue) {
        return getVariableHasNumberCompared(variableName, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Float object with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, Float limitValue) {
        return getVariableHasNumberCompared(variableName, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Date object with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, Date limitValue) {
        return getVariableHasDateCompared(variableName, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Date object with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, Date limitValue) {
        return getVariableHasDateCompared(variableName, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Date object with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, Date limitValue) {
        return getVariableHasDateCompared(variableName, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param limitValue   : a Date object with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, Date limitValue) {
        return getVariableHasDateCompared(variableName, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param comparison   : a string with comparison value
     * @param limitValue   : an integer object with limit value
     * @return a string object
     */
    private String getVariableHasNumberCompared(String variableName, String comparison, Integer limitValue) {
        return String.format(getTemplate(VARIABLE_HAS_INT_COMPARISON_CONDITION), variableName, comparison, limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param comparison   : a string with comparison value
     * @param limitValue   : a Double object object with limit value
     * @return a string object
     */
    private String getVariableHasNumberCompared(String variableName, String comparison, Double limitValue) {
        return String.format(Locale.ROOT,
                getTemplate(VARIABLE_HAS_DOUBLE_COMPARISON_CONDITION), variableName, comparison, limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param comparison   : a string with comparison value
     * @param limitValue   : a Float object with limit value
     * @return a string object
     */
    private String getVariableHasNumberCompared(String variableName, String comparison, Float limitValue) {
        return String.format(Locale.ROOT,
                getTemplate(VARIABLE_HAS_DOUBLE_COMPARISON_CONDITION), variableName, comparison, limitValue);
    }

    /**
     * class method
     * @param variableName : a string with variable Name value
     * @param comparison   : a string with comparison value
     * @param date         : a Date object with the date value
     * @return a string object
     */
    private String getVariableHasDateCompared(String variableName, String comparison, Date date) {
        String formattedDate = basicDateFormat.format(date);
        return String
                .format(getTemplate(VARIABLE_HAS_DATE_COMPARISON_CONDITION), variableName, comparison, formattedDate);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @return a string object
     */
    protected String getVariableExistsCondition(String variableName, String scopeHierarchyType) {
        return String.format(getTemplate(SCOPED_VARIABLE_EXISTS_CONDITION), variableName, scopeHierarchyType);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @return a string object
     */
    protected String getVariableDoesNotExistCondition(String variableName, String scopeHierarchyType) {
        return String.format(getTemplate(SCOPED_VARIABLE_DOES_NOT_EXIST_CONDITION), variableName, scopeHierarchyType);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param requiredValue     : a string with required value
     * @return a string object
     */
    protected String getVariableHasExactTextCondition(String variableName,
                                                      String scopeHierarchyType,
                                                      String requiredValue) {
        return String.format(getTemplate(SCOPED_VARIABLE_HAS_EXACT_TEXT_CONDITION),
                variableName, scopeHierarchyType, escape(requiredValue));
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param requiredValue     : a string with required value
     * @return a string object
     */
    protected String getVariableContainsTextCondition(String variableName,
                                                      String scopeHierarchyType,
                                                      String requiredValue) {
        return String.format(getTemplate(SCOPED_VARIABLE_CONTAINS_TEXT_CONDITION),
                variableName, scopeHierarchyType, escape(contains(requiredValue)));
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param requiredValue     : an integer with required value
     * @return a string object
     */
    protected String getVariableHasNumberCondition(String variableName,
                                                   String scopeHierarchyType,
                                                   Integer requiredValue) {
        return String.format(getTemplate(SCOPED_VARIABLE_HAS_INT_NUMBER_CONDITION),
                variableName, scopeHierarchyType, requiredValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param requiredValue     : a Double object with required value
     * @return a string object
     */
    protected String getVariableHasNumberCondition(String variableName,
                                                   String scopeHierarchyType,
                                                   Double requiredValue) {
        return String.format(Locale.ROOT, getTemplate(SCOPED_VARIABLE_HAS_DOUBLE_NUMBER_CONDITION),
                variableName, scopeHierarchyType, requiredValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param requiredValue     : a Float object with required value
     * @return a string object
     */
    protected String getVariableHasNumberCondition(String variableName,
                                                   String scopeHierarchyType,
                                                   Float requiredValue) {
        return String.format(Locale.ROOT, getTemplate(SCOPED_VARIABLE_HAS_DOUBLE_NUMBER_CONDITION),
                variableName, scopeHierarchyType, requiredValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : an integer with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, String scopeHierarchyType, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : an integer with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, String scopeHierarchyType, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : an integer with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, String scopeHierarchyType, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : an integer with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, String scopeHierarchyType, Integer limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Double object with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, String scopeHierarchyType, Double limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Double object with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, String scopeHierarchyType, Double limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Double object with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, String scopeHierarchyType, Double limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Double object with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, String scopeHierarchyType, Double limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Float object with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, String scopeHierarchyType, Float limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Float object with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, String scopeHierarchyType, Float limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Float object with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, String scopeHierarchyType, Float limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Float object with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, String scopeHierarchyType, Float limitValue) {
        return getVariableHasNumberCompared(variableName, scopeHierarchyType, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Date object with limit value
     * @return a string object
     */
    protected String getVariableGTCondition(String variableName, String scopeHierarchyType, Date limitValue) {
        return getVariableHasDateCompared(variableName, scopeHierarchyType, "gt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Date object with limit value
     * @return a string object
     */
    protected String getVariableGTECondition(String variableName, String scopeHierarchyType, Date limitValue) {
        return getVariableHasDateCompared(variableName, scopeHierarchyType, "gte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Date object with limit value
     * @return a string object
     */
    protected String getVariableLTCondition(String variableName, String scopeHierarchyType, Date limitValue) {
        return getVariableHasDateCompared(variableName, scopeHierarchyType, "lt", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param limitValue        : a Date object with limit value
     * @return a string object
     */
    protected String getVariableLTECondition(String variableName, String scopeHierarchyType, Date limitValue) {
        return getVariableHasDateCompared(variableName, scopeHierarchyType, "lte", limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param comparison        : a string with comparison value
     * @param limitValue        : an integer with limit value
     * @return a string object
     */
    private String getVariableHasNumberCompared(String variableName,
                                                String scopeHierarchyType,
                                                String comparison,
                                                Integer limitValue) {
        return String.format(getTemplate(SCOPED_VARIABLE_HAS_INT_COMPARISON_CONDITION),
                variableName, scopeHierarchyType, comparison, limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param comparison        : a string with comparison value
     * @param limitValue        : a Double object with limit value
     * @return a string object
     */
    private String getVariableHasNumberCompared(String variableName,
                                                String scopeHierarchyType,
                                                String comparison,
                                                Double limitValue) {
        return String.format(Locale.ROOT, getTemplate(SCOPED_VARIABLE_HAS_DOUBLE_COMPARISON_CONDITION),
                variableName, scopeHierarchyType, comparison, limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param comparison        : a string with comparison value
     * @param limitValue        : a Float object with limit value
     * @return a string object
     */
    private String getVariableHasNumberCompared(String variableName,
                                                String scopeHierarchyType,
                                                String comparison,
                                                Float limitValue) {
        return String.format(Locale.ROOT, getTemplate(SCOPED_VARIABLE_HAS_DOUBLE_COMPARISON_CONDITION),
                variableName, scopeHierarchyType, comparison, limitValue);
    }

    /**
     * class method
     * @param variableName      : a string with variable Name value
     * @param scopeHierarchyType: a string with scope hierarchy type value
     * @param comparison        : a string with comparison value
     * @param date              : a Date object with date value
     * @return a string object
     */
    private String getVariableHasDateCompared(String variableName,
                                              String scopeHierarchyType,
                                              String comparison,
                                              Date date) {
        String formattedDate = basicDateFormat.format(date);
        return String.format(getTemplate(SCOPED_VARIABLE_HAS_DATE_COMPARISON_CONDITION),
                variableName, scopeHierarchyType, comparison, formattedDate);
    }

    /**
     * class method
     * @param name: a string with Name value
     * @return a string object
     */
    private String getTemplate(String name) {
        if (!templates.containsKey(name)) {
            loadTemplate(name);
        }
        return templates.get(name);
    }

    /**
     * class method
     * @param name: a string with Name value
     */
    private synchronized void loadTemplate(String name) {
        if (!templates.containsKey(name)) {
            try {
                addTemplateToMap(name, new ClassPathResource(String.format(RESOURCE_FILE_TEMPLATE, name)));
            } catch (IOException e) {
                throw new ServiceException(String.format("Error loading template '%s'", name), "loadTemplate", e);
            }
        }
    }

    /**
     * class method
     *
     * @param name:    a string with Name value
     * @param resource : a classpath resource
     */
    private void addTemplateToMap(String name, ClassPathResource resource) throws IOException {
        try {
            templates.put(name, new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            // Do not add the template to the map
        }
    }

    /**
     * class method
     *
     * @param text: a string with text
     * @return a string object
     */
    private String escape(String text) {
        try {
            return objectMapper.writeValueAsString(text);
        } catch (JsonProcessingException e) {
            throw new ServiceException(String.format("Error serializing json string '%s'", text), "escape", e);
        }
    }

    /**
     * class method
     * @param text: a string with text
     * @return a string object
     */
    private String contains(String text) {
        return "*" + text + "*";
    }

    /**
     * class method
     * @param sortFields: a list of sort fields
     */
    private void setSortFields(List<SortField> sortFields) {
        for (SortField field : sortFields) {
            this.sortFields.put(field.getFieldName(), field);
        }
    }
}
