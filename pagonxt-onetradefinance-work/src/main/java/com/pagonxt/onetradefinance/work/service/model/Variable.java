package com.pagonxt.onetradefinance.work.service.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Date;

/**
 * Model class for variables
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Variable {

    //class attributes
    private String id;

    private String name;

    private String type;

    private String scopeId;

    private String scopeType;

    private String scopeDefinitionId;

    private String scopeDefinitionKey;

    private String scopeHierarchyType;

    private String rawValue;

    private String textValue;

    private String textValueKeyword;

    private Boolean booleanValue;

    private Double numberValue;

    private JsonNode jsonValue;

    private Date dateValue;

    /**
     * getter method
     * @return : a string with the id
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id : a string with the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return : a string with the name
     */
    public String getName() {
        return name;
    }

    /**
     * setter method
     * @param name : a string with the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getter method
     * @return : a string with the type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type : a string with the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return : a string with the scope id
     */
    public String getScopeId() {
        return scopeId;
    }

    /**
     * setter method
     * @param scopeId : a string with the scope id
     */
    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    /**
     * getter method
     * @return : a string with the scope type
     */
    public String getScopeType() {
        return scopeType;
    }

    /**
     * setter method
     * @param scopeType : a string with the scope type
     */
    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    /**
     * getter method
     * @return : a string with the scope definition id
     */
    public String getScopeDefinitionId() {
        return scopeDefinitionId;
    }

    /**
     * setter method
     * @param scopeDefinitionId : a string with the scope definition id
     */
    public void setScopeDefinitionId(String scopeDefinitionId) {
        this.scopeDefinitionId = scopeDefinitionId;
    }

    /**
     * getter method
     * @return : a string with the scope definition key
     */
    public String getScopeDefinitionKey() {
        return scopeDefinitionKey;
    }

    /**
     * setter method
     * @param scopeDefinitionKey : a string with the scope definition key
     */
    public void setScopeDefinitionKey(String scopeDefinitionKey) {
        this.scopeDefinitionKey = scopeDefinitionKey;
    }

    /**
     * getter method
     * @return : a string with the scope hierarchy type
     */
    public String getScopeHierarchyType() {
        return scopeHierarchyType;
    }

    /**
     * setter method
     * @param scopeHierarchyType : a string with the scope hierarchy type
     */
    public void setScopeHierarchyType(String scopeHierarchyType) {
        this.scopeHierarchyType = scopeHierarchyType;
    }

    /**
     * getter method
     * @return : a string with the raw value
     */
    public String getRawValue() {
        return rawValue;
    }

    /**
     * setter method
     * @param rawValue : a string with the raw value
     */
    public void setRawValue(String rawValue) {
        this.rawValue = rawValue;
    }

    /**
     * getter method
     * @return : a string with the text value
     */
    public String getTextValue() {
        return textValue;
    }

    /**
     * setter method
     * @param textValue : a string with the text value
     */
    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    /**
     * getter method
     * @return : a string with the text value keyword
     */
    public String getTextValueKeyword() {
        return textValueKeyword;
    }

    /**
     * setter method
     * @param textValueKeyword : a string with the text value keyword
     */
    public void setTextValueKeyword(String textValueKeyword) {
        this.textValueKeyword = textValueKeyword;
    }

    /**
     * getter method
     * @return : a boolean value
     */
    public Boolean getBooleanValue() {
        return booleanValue;
    }

    /**
     * setter method
     * @param booleanValue : a boolean value
     */
    public void setBooleanValue(Boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    /**
     * getter method
     * @return : a Double object with the number value
     */
    public Double getNumberValue() {
        return numberValue;
    }

    /**
     * setter method
     * @param numberValue : a Double object with the number value
     */
    public void setNumberValue(Double numberValue) {
        this.numberValue = numberValue;
    }

    /**
     * getter method
     * @return : a JsonNode object with the Json value
     */
    public JsonNode getJsonValue() {
        return jsonValue;
    }

    /**
     * setter method
     * @param jsonValue : a JsonNode object with the Json value
     */
    public void setJsonValue(JsonNode jsonValue) {
        this.jsonValue = jsonValue;
    }

    /**
     * getter method
     * @return : a Date object with the date value
     */
    public Date getDateValue() {
        return dateValue;
    }

    /**
     * setter method
     * @param dateValue : a Date object with the date value
     */
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Variable{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", scopeId='" + scopeId + '\'' +
                ", scopeType='" + scopeType + '\'' +
                ", scopeDefinitionId='" + scopeDefinitionId + '\'' +
                ", scopeDefinitionKey='" + scopeDefinitionKey + '\'' +
                ", scopeHierarchyType='" + scopeHierarchyType + '\'' +
                ", rawValue='" + rawValue + '\'' +
                ", textValue='" + textValue + '\'' +
                ", textValueKeyword='" + textValueKeyword + '\'' +
                ", booleanValue=" + booleanValue +
                ", numberValue=" + numberValue +
                ", jsonValue=" + jsonValue +
                ", dateValue=" + dateValue +
                '}';
    }
}
