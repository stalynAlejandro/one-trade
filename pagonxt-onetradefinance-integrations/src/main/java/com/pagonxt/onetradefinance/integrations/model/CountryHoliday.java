package com.pagonxt.onetradefinance.integrations.model;

import java.time.LocalDate;

public class CountryHoliday {

    private LocalDate holidayDate;
    private String country;

    /**
     * Getter method for field holidayDate
     *
     * @return value of holidayDate
     */
    public LocalDate getHolidayDate() {
        return holidayDate;
    }

    /**
     * Setter method for field holidayDate
     *
     * @param holidayDate : value of holidayDate
     */
    public void setHolidayDate(LocalDate holidayDate) {
        this.holidayDate = holidayDate;
    }

    /**
     * Getter method for field country
     *
     * @return value of country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter method for field country
     *
     * @param country : value of country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
