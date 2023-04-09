package com.pagonxt.onetradefinance.integrations.model.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model class to generate a list of tasks
 * Include class attributes, constructors, getters and setter
 * @author -
 * @version jdk-11.0.13
 * @see PagoNxtTaskItem
 * @since jdk-11.0.13
 */
public class MyTasksList {

    private List<PagoNxtTaskItem> data = new ArrayList<>();

    private Long total;

    /**
     * Empty constructor method
     */
    public MyTasksList() {
    }

    /**
     * constructor method
     * @param data a list of items with data about tasks
     * @param total a long value with the number of items
     */
    public MyTasksList(List<PagoNxtTaskItem> data, Long total) {
        this.data = data;
        this.total = total;
    }

    /**
     * getter method
     * @return a list of items with data about tasks
     */
    public List<PagoNxtTaskItem> getData() {
        return data;
    }

    /**
     * setter method
     * @param data a list of items with data about tasks
     */
    public void setData(List<PagoNxtTaskItem> data) {
        this.data = data;
    }

    /**
     * getter method
     * @return a long value with the number of elements
     */
    public Long getTotal() {
        return total;
    }

    /**
     * setter method
     * @param total  a long value with the number of elements
     */
    public void setTotal(Long total) {
        this.total = total;
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
        MyTasksList that = (MyTasksList) o;
        return Objects.equals(data, that.data) && Objects.equals(total, that.total);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(data, total);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "MyTasksList{" +
                "data=" + data +
                ", total=" + total +
                '}';
    }
}
