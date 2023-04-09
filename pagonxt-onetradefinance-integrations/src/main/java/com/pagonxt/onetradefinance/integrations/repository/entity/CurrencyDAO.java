package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity Class for currency.
 * Includes some attributes and getters and setters.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Entity
@Table(name = "hgr_03rfi_currencies")
public class CurrencyDAO {

        @Id
        @Column(name = "currency", unique=true, nullable=false)
        private String currency;

        /**
         * getter method
         * @return the currency Id
         */
        public String getCurrency() {
                return currency;
        }

        /**
         * setter method
         * @param currency a string with currency Id
         */
        public void setCurrency(String currency) {
                this.currency = currency;
        }
}
