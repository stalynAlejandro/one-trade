package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.apis.fxdeal.FxDealGateway;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer.SantanderFxDealSerializer;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;

import java.util.List;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get exchange insurances from Santander
 * @author -
 * @version jdk-11.0.13
 * @see FxDealService
 * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
 * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.FxDealGateway
 * @see  com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer.SantanderFxDealSerializer
 * @since jdk-11.0.13
 */
public class SantanderFxDealService implements FxDealService {

    private final FxDealGateway fxDealGateway;

    private final SantanderFxDealSerializer santanderFxDealSerializer;

    /**
     * constructor method
     * @param fxDealGateway             : a FxDealGateway object
     * @param santanderFxDealSerializer : a SantanderFxDealSerializer object
     */
    public SantanderFxDealService(FxDealGateway fxDealGateway, SantanderFxDealSerializer santanderFxDealSerializer) {
        this.fxDealGateway = fxDealGateway;
        this.santanderFxDealSerializer = santanderFxDealSerializer;
    }

    /**
     * This method allows searching fx deals
     * @param search a FxDealQuery object
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery
     * @return a list of exchange insurances
     */
    @Override
    public List<ExchangeInsurance> searchFxDeals(FxDealQuery search) {
        return santanderFxDealSerializer.toModel(fxDealGateway.searchFxDeals(search));
    }
}
