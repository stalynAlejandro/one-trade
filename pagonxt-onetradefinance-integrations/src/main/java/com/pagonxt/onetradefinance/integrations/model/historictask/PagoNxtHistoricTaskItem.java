package com.pagonxt.onetradefinance.integrations.model.historictask;

import java.util.Date;
import java.util.Objects;

/**
 * Model class for get data about every item of the list of historic tasks
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class PagoNxtHistoricTaskItem {

    private String rowId;
    private Date startDate;
    private Date endDate;
    private String taskName;
    private String userName;

    /**
     * getter method
     * @return a string with the row id of the item
     */
    public String getRowId() {
        return rowId;
    }

    /**
     * setter method
     * @param rowId a string with the row id of the item
     */
    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    /**
     * getter method
     * @return a Date object with the start date of the task
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * setter method
     * @param startDate a Date object with the start date of the task
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * getter method
     * @return a Date object with the end date of the task
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * setter method
     * @param endDate a Date object with the end date of the task
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * getter method
     * @return a string with the task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * setter method
     * @param taskName a string with the task name
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * getter method
     * @return a string with the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * setter method
     * @param userName a string with the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
        PagoNxtHistoricTaskItem that = (PagoNxtHistoricTaskItem) o;
        return Objects.equals(rowId, that.rowId) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(taskName, that.taskName) &&
                Objects.equals(userName, that.userName);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(rowId, startDate, endDate, taskName, userName);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "PagoNxtHistoricTaskItem{" +
                "rowId='" + rowId + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", taskName='" + taskName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
