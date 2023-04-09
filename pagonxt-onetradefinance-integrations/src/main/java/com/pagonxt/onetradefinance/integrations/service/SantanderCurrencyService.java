package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.repository.CurrencyRepository;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;

import java.util.List;

/**
 * Service Class to find currency information from the repository.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class SantanderCurrencyService implements CurrencyService {

    /**
     * Repository to get currency information.
     */
    private final CurrencyRepository currencyRepository;

    /**
     * List of currencies saved in memory.
     */
    private List<CurrencyDAO> currencies = null;

    /**
     * Constructor class.
     *
     * @param currencyRepository : the currency repository
     */
    public SantanderCurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Method to get the list of currencies.
     *
     * @param product   : the product
     * @param event     : the event
     *
     * @return the list of currencies
     */
    @Override
    public List<CurrencyDAO> getCurrencyList(String product, String event) {
        if(currencies != null) {
            return currencies;
        }
        try{
            currencies = currencyRepository.findAll();
        } catch(Exception e) {
            throw new ServiceException("Connection with repository failed", "errorRepository");
        }
        return currencies;
    }
}
