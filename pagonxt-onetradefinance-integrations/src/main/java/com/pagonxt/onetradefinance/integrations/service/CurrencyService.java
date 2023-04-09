package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;

import java.util.List;

/**
 * Service Interface to find currency information.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public interface CurrencyService {

    /**
     * Method to get the list of currencies.
     *
     * @param product   : the product
     * @param event     : the event
     *
     * @return the list of currencies
     */
    List<CurrencyDAO> getCurrencyList(String product, String event);
}
