package com.pagonxt.onetradefinance.integrations.apis.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class for Links
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Links {

    @JsonProperty("_first")
    private String first;
    @JsonProperty("_prev")
    private String prev;
    @JsonProperty("_next")
    private String next;
    @JsonProperty("_last")
    private String last;

    /**
     * getter method
     * @return a string with the first element
     */
    public String getFirst() {
        return first;
    }

    /**
     * setter method
     * @param first a string with the first element
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * getter method
     * @return a string with the previous element
     */
    public String getPrev() {
        return prev;
    }

    /**
     * setter method
     * @param prev a string with the previous element
     */
    public void setPrev(String prev) {
        this.prev = prev;
    }

    /**
     * getter method
     * @return a string with the next element
     */
    public String getNext() {
        return next;
    }

    /**
     * setter method
     * @param next a string with the next element
     */
    public void setNext(String next) {
        this.next = next;
    }

    /**
     * getter method
     * @return a string with the last element
     */
    public String getLast() {
        return last;
    }

    /**
     * setter method
     * @param last a string with the last element
     */
    public void setLast(String last) {
        this.last = last;
    }
}
