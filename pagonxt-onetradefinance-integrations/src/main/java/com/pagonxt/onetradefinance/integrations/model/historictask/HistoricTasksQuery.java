package com.pagonxt.onetradefinance.integrations.model.historictask;

import com.pagonxt.onetradefinance.integrations.model.UserInfo;

import java.util.Objects;

/**
 * Model class to get info for queries about historic tasks
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
 * @since jdk-11.0.13
 */
public class HistoricTasksQuery {
    private UserInfo userInfo;
    private String code;
    private String sortField;
    private int sortOrder = 0;
    private Integer fromPage;
    private Integer pageSize;
    private String locale;

    /**
     * getter method
     * @return UserInfo object with info about user
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * setter method
     * @param userInfo UserInfo object with info about user
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * getter method
     * @return a string with the task code
     */
    public String getCode() {
        return code;
    }

    /**
     * setter method
     * @param code a string with the task code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * getter method
     * @return a string with sort field
     */
    public String getSortField() {
        return sortField;
    }

    /**
     * setter method
     * @param sortField a string with sort field
     */
    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    /**
     * getter method
     * @return a string with sort order
     */
    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * setter method
     * @param sortOrder a string with sort order
     */
    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * getter method
     * @return an integer value with page where to start the list
     */
    public Integer getFromPage() {
        return fromPage;
    }

    /**
     * setter method
     * @param fromPage an integer value with page where to start the list
     */
    public void setFromPage(Integer fromPage) {
        this.fromPage = fromPage;
    }

    /**
     * getter methdod
     * @return an integer value with the numbers of elements to show in the list
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * setter method
     * @param pageSize an integer value with the numbers of elements to show in the list
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * getter method
     * @return a string with locale value
     */
    public String getLocale() {
        return locale;
    }

    /**
     * setter method
     * @param locale a string with locale value
     */
    public void setLocale(String locale) {
        this.locale = locale;
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
        HistoricTasksQuery that = (HistoricTasksQuery) o;
        return sortOrder == that.sortOrder &&
                Objects.equals(userInfo, that.userInfo) &&
                Objects.equals(code, that.code) &&
                Objects.equals(sortField, that.sortField) &&
                Objects.equals(fromPage, that.fromPage) &&
                Objects.equals(pageSize, that.pageSize) &&
                Objects.equals(locale, that.locale);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(userInfo, code, sortField, sortOrder, fromPage, pageSize, locale);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "HistoricTasksQuery{" +
                "userInfo=" + userInfo +
                ", code='" + code + '\'' +
                ", sortField='" + sortField + '\'' +
                ", sortOrder=" + sortOrder +
                ", fromPage=" + fromPage +
                ", pageSize=" + pageSize +
                ", locale='" + locale + '\'' +
                '}';
    }
}
