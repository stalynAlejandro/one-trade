package com.pagonxt.onetradefinance.work.service.model.elastic;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * model class for elastic responses
 * @param <T> : generic collection
 * @author -
 * @version jdk-11.0.13
 * @see Shards
 * @see Hits
 * @since jdk-11.0.13
 */
public class ElasticResponse<T> {

    //class attributes
    private Integer took;

    @JsonProperty("timed_out")
    private Boolean timedOut;

    @JsonProperty("_shards")
    private Shards shards;

    private Hits<T> hits;

    /**
     * getter method
     * @return a Long object with the size value
     */
    public Long getSize() {
        return getHits().getTotal().getValue().longValue();
    }

    /**
     * getter method
     * @return an Integer object with took value
     */
    public Integer getTook() {
        return took;
    }

    /**
     * setter method
     * @param took : an Integer object with took value
     */
    public void setTook(Integer took) {
        this.took = took;
    }

    /**
     * getter method
     * @return a boolean value (if timed out or not)
     */
    public Boolean getTimedOut() {
        return timedOut;
    }

    /**
     * setter method
     * @param timedOut : a boolean value (if timed out or not)
     */
    public void setTimedOut(Boolean timedOut) {
        this.timedOut = timedOut;
    }

    /**
     * getter method
     * @see Shards
     * @return a Shards object
     */
    public Shards getShards() {
        return shards;
    }

    /**
     * setter method
     * @param shards :a Shards object
     * @see Shards
     */
    public void setShards(Shards shards) {
        this.shards = shards;
    }

    /**
     * getter method
     * @see Hits
     * @return a Hits object
     */
    public Hits<T> getHits() {
        return hits;
    }

    /**
     * setter method
     * @param hits a Hits object
     */
    public void setHits(Hits<T> hits) {
        this.hits = hits;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ElasticResponse{" +
                "took=" + took +
                ", timedOut=" + timedOut +
                ", shards=" + shards +
                ", hits=" + hits +
                '}';
    }
}
