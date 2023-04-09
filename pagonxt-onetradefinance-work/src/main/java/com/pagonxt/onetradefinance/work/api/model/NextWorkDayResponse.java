package com.pagonxt.onetradefinance.work.api.model;

import java.time.LocalDate;

/**
 * Model class for responses
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class NextWorkDayResponse {

    //class attribute
    private LocalDate nextWorkDay;

    /**
     * constructor method
     * @param nextWorkDay : a LocalDate object with the next workday
     */
    public NextWorkDayResponse(LocalDate nextWorkDay) {
        this.nextWorkDay = nextWorkDay;
    }

    /**
     * getter method
     * @return a LocalDate object with the next workday
     */
    public LocalDate getNextWorkDay() {
        return nextWorkDay;
    }

    /**
     * setter method
     * @param nextWorkDay a LocalDate object with the next workday
     */
    public void setNextWorkDay(LocalDate nextWorkDay) {
        this.nextWorkDay = nextWorkDay;
    }
}
