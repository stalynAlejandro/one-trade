package com.pagonxt.onetradefinance.external.backend.api.model;

import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * This class defines a filter object
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Filter {

    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    //class attributes
    private String type;

    private String value;

    private List<String> values;

    /**
     * getter method
     * @return the filter type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type a string with the filter type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return the filter value
     */
    public String getValue() {
        return value;
    }

    /**
     * setter method
     * @param value a string with the filter value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * getter method
     * @return a list of filter values
     */
    public List<String> getValues() {
        return values;
    }

    /**
     * setter method
     * @param values a list of filter values
     */
    public void setValues(List<String> values) {
        this.values = values;
    }

    /**
     * class method
     * @return a string object
     */
    public String getString() {
        if(value == null || value.isEmpty()) {
            return null;
        }
        return value;
    }

    /**
     * class method
     * @return a Double object
     */
    public Double getDouble() {
        if(value == null || value.isEmpty()) {
            return null;
        }
        return Double.parseDouble(value);
    }

    /**
     * class method
     * @return a Integer object
     */
    public Integer getInt() {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Integer.parseInt(value);
    }

    /**
     * class method
     * @return a Date object
     */
    public Date getJSONDate() {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return dateTimeFormat.parse(value);
        } catch (ParseException e) {
            throw new DateFormatException(value, e);
        }
    }

    /**
     * class method
     * @return a list of String Objects
     */
    public List<String> getStringList() {
        return getValues();
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Filter{" +
                "type='" + type + '\'' +
                ", value=" + value +
                ", values=" + values +
                '}';
    }
}
