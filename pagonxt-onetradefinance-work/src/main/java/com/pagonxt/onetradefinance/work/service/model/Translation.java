package com.pagonxt.onetradefinance.work.service.model;

/**
 * Model class for translations
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Translation {

    //class attributes
    private String id;

    private String key;

    private String locale;

    private String value;

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
     * @return : a string with the key
     */
    public String getKey() {
        return key;
    }

    /**
     * setter method
     * @param key : a string with the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * getter method
     * @return : a string with the locale value
     */
    public String getLocale() {
        return locale;
    }

    /**
     * setter method
     * @param locale : a string with the locale value
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * getter method
     * @return : a string with the value
     */
    public String getValue() {
        return value;
    }

    /**
     * setter method
     * @param value : a string with the value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Translation{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", locale='" + locale + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
