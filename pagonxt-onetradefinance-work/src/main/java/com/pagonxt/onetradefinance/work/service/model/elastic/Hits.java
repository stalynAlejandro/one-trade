package com.pagonxt.onetradefinance.work.service.model.elastic;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * model class with tools for elastic responses
 * @param <T> : generic collection
 * @author -
 * @version jdk-11.0.13
 * @see Total
 * @see Hit
 * @since jdk-11.0.13
 */
public class Hits<T> {

    //class attributes
    private Total total;

    @JsonProperty("max_score")
    private Double maxScore;

    @JsonProperty("hits")
    private List<Hit<T>> hitList;

    /**
     * getter method
     * @return : a Total object with total value
     */
    public Total getTotal() {
        return total;
    }

    /**
     * setter method
     * @param total : a Total object with total value
     */
    public void setTotal(Total total) {
        this.total = total;
    }

    /**
     * getter method
     * @return : a Double object with the max excore value
     */
    public Double getMaxScore() {
        return maxScore;
    }

    /**
     * setter method
     * @param maxScore : a Double object with the max excore value
     */
    public void setMaxScore(Double maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * getter method
     * @return : a list of hits
     */
    public List<Hit<T>> getHitList() {
        return hitList;
    }

    /**
     * setter method
     * @param hitList : a list of hits
     */
    public void setHitList(List<Hit<T>> hitList) {
        this.hitList = hitList;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Hits{" +
                "total=" + total +
                ", maxScore=" + maxScore +
                ", hits=" + hitList +
                '}';
    }
}
