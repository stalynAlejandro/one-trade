package com.pagonxt.onetradefinance.integrations.model;

import java.util.Map;

/**
 * Model class for translations
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Translation {

    private String keyName;

    private Map<String, String> translations;

    /**
     * getter method
     * @return a string with the key name
     */
    public String getKeyName() {
        return keyName;
    }

    /**
     * setter method
     * @param keyName a string with the key name
     */
    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    /**
     * getter method
     * @return a data structure for translations
     */
    public Map<String, String> getTranslations() {
        return translations;
    }

    /**
     * setter method
     * @param translations a data structure for translations
     */
    public void setTranslations(Map<String, String> translations) {
        this.translations = translations;
    }
}
