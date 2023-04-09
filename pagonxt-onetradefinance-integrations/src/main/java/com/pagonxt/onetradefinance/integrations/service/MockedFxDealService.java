package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get mocked exchange insurances
 * @author -
 * @version jdk-11.0.13
 * @see FxDealService
 * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
 * @since jdk-11.0.13
 */
public class MockedFxDealService implements FxDealService {


    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockedFxDealService.class);

    private final List<ExchangeInsurance> fxDeals = new ArrayList<>();

    /**
     * constructor method
     */
    public MockedFxDealService() {
        ObjectMapper mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock-data/exchange-insurances.json")) {
            fxDeals.addAll(mapper.readValue(resourceStream, new TypeReference<>() {}));
            LOG.debug("Mock customer repository initialized with {} items", fxDeals.size());
        } catch (Exception e) {
            LOG.warn("Error reading customer file, mock repository will be empty", e);
        }
    }

    /**
     * This method allows searching fx deals
     * @param search a FxDealQuery object
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery
     * @return a list of exchange insurances
     */
    public List<ExchangeInsurance> searchFxDeals(FxDealQuery search) {
        return fxDeals;
    }
}
