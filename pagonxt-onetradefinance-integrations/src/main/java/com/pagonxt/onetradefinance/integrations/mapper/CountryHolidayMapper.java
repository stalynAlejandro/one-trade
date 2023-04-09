package com.pagonxt.onetradefinance.integrations.mapper;

import com.pagonxt.onetradefinance.integrations.model.CountryHoliday;
import com.pagonxt.onetradefinance.integrations.model.CountryHolidayResponse;
import com.pagonxt.onetradefinance.integrations.repository.entity.CountryHolidayDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CountryHolidayMapper {

    public CountryHolidayResponse toModel(List<CountryHolidayDAO> countryHolidayDAOList) {
        CountryHolidayResponse countryHolidayResponse = new CountryHolidayResponse();
        if(countryHolidayDAOList == null) {
            return null;
        }
        List<CountryHoliday> countryHolidayList =
                countryHolidayDAOList.stream().map(this::toModel).collect(Collectors.toList());
        countryHolidayResponse.setCountryHolidayList(countryHolidayList);
        return  countryHolidayResponse;
    }

    public CountryHoliday toModel(CountryHolidayDAO countryHolidayDAO) {
        if(countryHolidayDAO == null) {
            return new CountryHoliday();
        }
        CountryHoliday countryHoliday = new CountryHoliday();
        countryHoliday.setHolidayDate(countryHolidayDAO.getHolidayDate());
        countryHoliday.setCountry(countryHolidayDAO.getCountry());
        return countryHoliday;
    }
}
