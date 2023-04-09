package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Class to find currency information from the mocked data.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class MockedCurrencyService implements CurrencyService {

    /**
     * Logger object for class logs.
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockedCurrencyService.class);

    /**
     * List of currencies saved in memory.
     */
    private final List<CurrencyDAO> currencies = new ArrayList<>();

    /**
     * Constructor class.
     *
     * @param mapper : the mapper
     */
    public MockedCurrencyService(ObjectMapper mapper) {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock-data/currencies.json")) {
            currencies.addAll(mapper.readValue(resourceStream, new TypeReference<>() {}));
            LOG.debug("Mock currencies repository initialized with {} items", currencies.size());
        } catch (Exception e) {
            LOG.warn("Error reading currencies file, mock repository will be empty", e);
        }
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
        return currencies;
    }
}
