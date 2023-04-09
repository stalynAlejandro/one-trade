package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.apis.specialprices.TradeSpecialPricesGateway;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer.SantanderTradeSpecialPricesSerializer;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;

import java.util.List;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get trade special prices from Santander
 * @author -
 * @version jdk-11.0.13
 * @see TradeSpecialPricesService
 * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices
 * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.TradeSpecialPricesGateway
 * @see  com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer.SantanderTradeSpecialPricesSerializer
 * @since jdk-11.0.13
 */
public class SantanderTradeSpecialPricesService implements TradeSpecialPricesService {

    private final TradeSpecialPricesGateway tradeSpecialPricesGateway;

    private final SantanderTradeSpecialPricesSerializer santanderTradeSpecialPricesSerializer;

    /**
     * constructor method
     * @param tradeSpecialPricesGateway             : a TradeSpecialPricesGateway object
     * @param santanderTradeSpecialPricesSerializer : a SantanderTradeSpecialPricesSerializer object
     */
    public SantanderTradeSpecialPricesService
            (TradeSpecialPricesGateway tradeSpecialPricesGateway,
             SantanderTradeSpecialPricesSerializer santanderTradeSpecialPricesSerializer) {
        this.tradeSpecialPricesGateway = tradeSpecialPricesGateway;
        this.santanderTradeSpecialPricesSerializer = santanderTradeSpecialPricesSerializer;
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
        return santanderTradeSpecialPricesSerializer.toModel(tradeSpecialPricesGateway.getSpecialPrices(query), locale);
    }
}
