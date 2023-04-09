package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;

import java.util.List;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides a method to get a list of exchange insurances
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
 * @since jdk-11.0.13
 */
public interface FxDealService {

    /**
     * This method allows searching fx deals
     * @param search a FxDealQuery object
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery
     * @return a list of exchange insurances
     */
    List<ExchangeInsurance> searchFxDeals(FxDealQuery search);
}
