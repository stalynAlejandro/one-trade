package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;

import java.util.List;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides a method to get a list of trade special prices
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices
 * @since jdk-11.0.13
 */
public interface TradeSpecialPricesService {

    /**
     * This method allows to obtain a list of trade special prices
     * @param query a TradeSpecialPricesQuery object
     * @param locale a string with locale value
     * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery
     * @return a list of trade special prices
     */
    List<TradeSpecialPrices> getSpecialPrices(TradeSpecialPricesQuery query, String locale);
}
