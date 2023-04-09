package com.pagonxt.onetradefinance.integrations.repository;

import com.pagonxt.onetradefinance.integrations.repository.entity.CountryHolidayDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository Class to access the table holidays.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Repository
public interface CountryHolidayRepository extends JpaRepository<CountryHolidayDAO, String> {

    List<CountryHolidayDAO> findByCountry(String country);
}