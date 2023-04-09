package com.pagonxt.onetradefinance.work.service.model.elastic;

/**
 * model class with tools for elastic responses
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class Total {

    //class attributes
    private Integer value;

    private String relation;

    /**
     * getter method
     * @return : an Integer with the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * setter method
     * @param value : an Integer with the value
     */
    public void setValue(Integer value) {
        this.value = value;
    }

    /**
     * getter method
     * @return : a String with the relation value
     */
    public String getRelation() {
        return relation;
    }

    /**
     * setter method
     * @param relation : a String with the relation value
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "Total{" +
                "value=" + value +
                ", relation='" + relation + '\'' +
                '}';
    }
}
