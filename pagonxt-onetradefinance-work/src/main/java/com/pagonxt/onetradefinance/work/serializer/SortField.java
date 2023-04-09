package com.pagonxt.onetradefinance.work.serializer;

import java.util.Objects;

/**
 * class with some methods to sort fields
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class SortField {

    public static final String STRING = "string";
    public static final String INTEGER = "integer";
    public static final String DOUBLE = "double";
    public static final String DATE = "date";
    public static final String BOOLEAN = "boolean";
    public static final String ROOT_SCOPE = "root";

    //class attributes
    private final String fieldName;
    private final String fieldMapping;
    private final String dataType;
    private final String scopeHierarchyType;

    /**
     * constructor method
     * @param fieldName    : a string with the field name
     * @param fieldMapping : a string with the field mapping
     * @param dataType     : a string with the data type
     */
    private SortField(String fieldName, String fieldMapping, String dataType) {
        this(fieldName, fieldMapping, dataType, null);
    }

    /**
     * constructor method
     * @param fieldName          : a string with the field name
     * @param fieldMapping       : a string with the field mapping
     * @param dataType           : a string with the data type
     * @param scopeHierarchyType : a string with the scope hierarchy type
     */
    private SortField(String fieldName, String fieldMapping, String dataType, String scopeHierarchyType) {
        this.fieldName = fieldName;
        this.fieldMapping = fieldMapping;
        this.dataType = dataType;
        this.scopeHierarchyType = scopeHierarchyType;
    }

    /**
     * class method
     * @param fieldName      :a string with the field name
     * @param elasticMapping :a string with the elastic mapping
     * @return a SortField object
     */
    public static SortField ofString(String fieldName, String elasticMapping) {
        return new SortField(fieldName, elasticMapping, STRING);
    }

    /**
     * class method
     * @param fieldName      :a string with the field name
     * @param elasticMapping :a string with the elastic mapping
     * @return a SortField object
     */
    public static SortField ofInteger(String fieldName, String elasticMapping) {
        return new SortField(fieldName, elasticMapping, INTEGER);
    }

    /**
     * class method
     * @param fieldName      :a string with the field name
     * @param elasticMapping :a string with the elastic mapping
     * @return a SortField object
     */
    public static SortField ofDouble(String fieldName, String elasticMapping) {
        return new SortField(fieldName, elasticMapping, DOUBLE);
    }

    /**
     * class method
     * @param fieldName      :a string with the field name
     * @param elasticMapping :a string with the elastic mapping
     * @return a SortField object
     */
    public static SortField ofDate(String fieldName, String elasticMapping) {
        return new SortField(fieldName, elasticMapping, DATE);
    }

    /**
     * class method
     * @param fieldName      :a string with the field name
     * @param elasticMapping :a string with the elastic mapping
     * @return a SortField object
     */
    public static SortField ofBoolean(String fieldName, String elasticMapping) {
        return new SortField(fieldName, elasticMapping, BOOLEAN);
    }

    /**
     * class method
     * @param fieldName         :a string with the field name
     * @param elasticMapping    :a string with the elastic mapping
     * @param scopeHierarchyType:a string with the scope hierarchy type
     * @return a SortField object
     */
    public static SortField ofString(String fieldName, String elasticMapping, String scopeHierarchyType) {
        return new SortField(fieldName, elasticMapping, STRING, scopeHierarchyType);
    }

    /**
     * class method
     * @param fieldName         :a string with the field name
     * @param elasticMapping    :a string with the elastic mapping
     * @param scopeHierarchyType:a string with the scope hierarchy type
     * @return a SortField object
     */
    public static SortField ofInteger(String fieldName, String elasticMapping, String scopeHierarchyType) {
        return new SortField(fieldName, elasticMapping, INTEGER, scopeHierarchyType);
    }

    /**
     * class method
     * @param fieldName         :a string with the field name
     * @param elasticMapping    :a string with the elastic mapping
     * @param scopeHierarchyType:a string with the scope hierarchy type
     * @return a SortField object
     */
    public static SortField ofDouble(String fieldName, String elasticMapping, String scopeHierarchyType) {
        return new SortField(fieldName, elasticMapping, DOUBLE, scopeHierarchyType);
    }

    /**
     * class method
     * @param fieldName         :a string with the field name
     * @param elasticMapping    :a string with the elastic mapping
     * @param scopeHierarchyType:a string with the scope hierarchy type
     * @return a SortField object
     */
    public static SortField ofDate(String fieldName, String elasticMapping, String scopeHierarchyType) {
        return new SortField(fieldName, elasticMapping, DATE, scopeHierarchyType);
    }

    /**
     * class method
     * @param fieldName         :a string with the field name
     * @param elasticMapping    :a string with the elastic mapping
     * @param scopeHierarchyType:a string with the scope hierarchy type
     * @return a SortField object
     */
    public static SortField ofBoolean(String fieldName, String elasticMapping, String scopeHierarchyType) {
        return new SortField(fieldName, elasticMapping, BOOLEAN, scopeHierarchyType);
    }

    /**
     * getter method
     * @return a string with the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * getter method
     * @return a string with the field mapping
     */
    public String getFieldMapping() {
        return fieldMapping;
    }

    /**
     * getter method
     * @return a string with the data type
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * getter method
     * @return a string with the scope hierarchy type
     */
    public String getScopeHierarchyType() {
        return scopeHierarchyType;
    }

    /**
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortField sortField = (SortField) o;
        return Objects.equals(fieldName, sortField.fieldName) &&
                Objects.equals(fieldMapping, sortField.fieldMapping) &&
                Objects.equals(dataType, sortField.dataType) &&
                Objects.equals(scopeHierarchyType, sortField.scopeHierarchyType);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(fieldName, fieldMapping, dataType, scopeHierarchyType);
    }
}
