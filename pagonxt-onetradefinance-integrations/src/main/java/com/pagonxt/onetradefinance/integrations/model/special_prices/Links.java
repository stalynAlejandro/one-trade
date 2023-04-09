package com.pagonxt.onetradefinance.integrations.model.special_prices;

import java.util.Objects;

/**
 * Model class for Links
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Links {

    /**
     * Link to the first page of the trade price list.
     * The exact content of the page depends on the input parameters provided in the request,
     * such as the offset.
     */
    private String first;

    /**
     * Link to the previous page of the trade price list.
     * The exact content of the page depends on the input parameters provided in the request,
     * such as the offset.
     */
    private String prev;

    /**
     * Link to the next page of the trade price list.
     * The exact content of the page depends on the input parameters provided in the request,
     * such as the offset.
     */
    private String next;

    /**
     * Link to the last page of the trade price list.
     * The exact content of the page depends on the input parameters provided in the request,
     * such as the offset.
     */
    private String last;

    /**
     * getter method
     * @return a string with the Link to the first page of the trade price list.
     */
    public String getFirst() {
        return first;
    }

    /**
     * setter method
     * @param first a string with the Link to the first page of the trade price list.
     */
    public void setFirst(String first) {
        this.first = first;
    }

    /**
     * getter method
     * @return a string with the Link to the previous page of the trade price list.
     */
    public String getPrev() {
        return prev;
    }

    /**
     * setter method
     * @param prev a string with the Link to the previous page of the trade price list.
     */
    public void setPrev(String prev) {
        this.prev = prev;
    }

    /**
     * getter method
     * @return a string with the Link to the next page of the trade price list.
     */
    public String getNext() {
        return next;
    }

    /**
     * setter method
     * @param next a string with the Link to the next page of the trade price list.
     */
    public void setNext(String next) {
        this.next = next;
    }

    /**
     * getter method
     * @return a string with the Link to the last page of the trade price list.
     */
    public String getLast() {
        return last;
    }

    /**
     * setter method
     * @param last a string with the Link to the last page of the trade price list.
     */
    public void setLast(String last) {
        this.last = last;
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
        Links links = (Links) o;
        return Objects.equals(first, links.first) &&
                Objects.equals(prev, links.prev) &&
                Objects.equals(next, links.next) &&
                Objects.equals(last, links.last);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(first, prev, next, last);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Links{" +
                "first='" + first + '\'' +
                ", prev='" + prev + '\'' +
                ", next='" + next + '\'' +
                ", last='" + last + '\'' +
                '}';
    }
}
