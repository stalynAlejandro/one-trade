package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.CountryHolidayResponse;

/**
 * Service Interface to find country holiday information.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public interface CountryHolidayService {

    /**
     * Method to get the list of country holidays.
     *
     * @param country   : the country
     *
     * @return the list of holidays
     */
    CountryHolidayResponse getCountryHolidayResponse(String country);
}
