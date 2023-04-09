package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.Objects;

/**
 * This class represents an api request for a list.
 * Includes a generic filters attribute, as well as pagination and sorting attributes.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.Filters
 * @since jdk-11.0.13
 */
public class ListRequest {

    public static final int SORT_ASCENDING = 1;

    public static final int SORT_DESCENDING = -1;

    //class attributes
    private Filters filters;

    private String sortField;

    private int sortOrder = 0;

    private Integer fromPage;

    private Integer pageSize;

    /**
     * getter method
     * @return a Filters object
     */
    public Filters getFilters() {
        return filters;
    }

    /**
     * setter method
     * @param filters a Filters object
     */
    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    /**
     * getter method
     * @return the sort field
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * setter method
     * @param sortField the sort field
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * getter method
     * @return the sort order
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * setter method
     * @param sortOrder the sort order
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * getter method
     * @return the page which the list starts
     */
    public Integer getFromPage() {
        return fromPage;
    }

    /**
     * setter methos
     * @param fromPage an Integer value with the page which the list starts
     */
    public void setFromPage(Integer fromPage) {
        this.fromPage = fromPage;
    }

    /**
     * getter method
     * @return the page size (number of visible elements in the list)
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * setter method
     * @param pageSize an integer value with the page size (number of visible elements in the list)
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
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
        ListRequest that = (ListRequest) o;
        return sortOrder == that.sortOrder &&
                Objects.equals(filters, that.filters) &&
                Objects.equals(sortField, that.sortField) &&
                Objects.equals(fromPage, that.fromPage) &&
                Objects.equals(pageSize, that.pageSize);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(filters, sortField, sortOrder, fromPage, pageSize);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ListRequest{" +
                ", filters=" + filters +
                ", sortField='" + sortField + '\'' +
                ", sortOrder=" + sortOrder +
                ", fromPage=" + fromPage +
                ", pageSize=" + pageSize +
                '}';
    }
}
