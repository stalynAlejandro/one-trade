package com.pagonxt.onetradefinance.work.api.model;

/**
 * Model class for responses
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ContentStringResponse {

    //class attribute
    private final String content;

    /**
     * constructor method
     * @param content : a string with the content
     */
    public ContentStringResponse(String content) {
        this.content = content;
    }

    /**
     * getter method
     * @return a string with the content
     */
    public String getContent() {
        return content;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ContentStringResponse{" +
                "content='" + content + '\'' +
                '}';
    }
}
