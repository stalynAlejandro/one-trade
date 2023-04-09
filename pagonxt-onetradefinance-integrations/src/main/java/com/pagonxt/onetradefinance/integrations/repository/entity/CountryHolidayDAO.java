package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Entity Class for currency.
 * Includes some attributes and getters and setters.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Entity
@Table(name = "hgr_03rfi_country_holidays")
public class CountryHolidayDAO {

        @Id
        @Column(name = "holiday_date", unique=true, nullable=false)
        private LocalDate holidayDate;

        @Column(name = "country_iso2", nullable=false)
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
