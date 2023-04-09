package com.pagonxt.onetradefinance.integrations.model;

import java.util.List;

public class CountryHolidayResponse {

    private List<CountryHoliday> countryHolidayList;
    private boolean errorDB;

    /**
     * Getter method for field countryHolidayList
     *
     * @return value of countryHolidayList
     */
    public List<CountryHoliday> getCountryHolidayList() {
        return countryHolidayList;
    }

    /**
     * Setter method for field countryHolidayList
     *
     * @param countryHolidayList : value of countryHolidayList
     */
    public void setCountryHolidayList(List<CountryHoliday> countryHolidayList) {
        this.countryHolidayList = countryHolidayList;
    }

    /**
     * Getter method for field errorDB
     *
     * @return value of errorDB
     */
    public boolean isErrorDB() {
        return errorDB;
    }

    /**
     * Setter method for field errorDB
     *
     * @param errorDB : value of errorDB
     */
    public void setErrorDB(boolean errorDB) {
        this.errorDB = errorDB;
    }
}
