package com.pagonxt.onetradefinance.work.service.model.elastic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * model class with tools for elastic responses
 * @param <T> : generic collection
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Hit<T> {

    //class attributes
    @JsonProperty("_index")
    private String index;

    @JsonProperty("_type")
    private String type;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_score")
    private Double score;

    @JsonProperty("_source")
    private T source;

    /**
     * getter method
     * @return : a string with the index value
     */
    public String getIndex() {
        return index;
    }

    /**
     * setter method
     * @param index : a string with the index value
     */
    public void setIndex(String index) {
        this.index = index;
    }

    /**
     * getter method
     * @return : a string with the type value
     */
    public String getType() {
        return type;
    }

    /**
     * setter method
     * @param type : a string with the type value
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getter method
     * @return : a string with the id value
     */
    public String getId() {
        return id;
    }

    /**
     * setter method
     * @param id : a string with the id value
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * getter method
     * @return : a Double object with the score value
     */
    public Double getScore() {
        return score;
    }

    /**
     * setter method
     * @param score : a Double object with the score value
     */
    public void setScore(Double score) {
        this.score = score;
    }

    /**
     * getter method
     * @return : the source
     */
    public T getSource() {
        return source;
    }

    /**
     * setter method
     * @param source : the source
     */
    public void setSource(T source) {
        this.source = source;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Hit{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", score=" + score +
                ", source=" + source +
                '}';
    }
}
