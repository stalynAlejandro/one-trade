package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get mocked trade special prices
 * @author -
 * @version jdk-11.0.13
 * @see TradeSpecialPricesService
 * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices
 * @since jdk-11.0.13
 */
public class MockedTradeSpecialPricesService implements TradeSpecialPricesService {

    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockedTradeSpecialPricesService.class);

    private final List<TradeSpecialPrices> specialPrices = new ArrayList<>();

    /**
     * constructor method
     */
    public MockedTradeSpecialPricesService() {
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock-data/special-prices.json")) {
            this.specialPrices.addAll(mapper.readValue(resourceStream, new TypeReference<>() {}));
            LOG.debug("Mock special prices repository initialized");
        } catch (Exception e) {
            LOG.warn("Error reading special prices file, mock repository will be empty", e);
        }
    }

    /**
     * This method allows to obtain a list of trade special prices
     * @param query a TradeSpecialPricesQuery object
     * @param locale a string with locale value
     * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery
     * @return a list of trade special prices
     */
    @Override
    public List<TradeSpecialPrices> getSpecialPrices(TradeSpecialPricesQuery query, String locale) {
        return specialPrices;
    }
}
