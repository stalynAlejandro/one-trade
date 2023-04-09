package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.ExchangeInsuranceDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.utils.QueryUtils;
import com.pagonxt.onetradefinance.integrations.service.FxDealService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller with the endpoints to search exchange insurances from the external app
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.utils.QueryUtils
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.ExchangeInsuranceDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.FxDealService
 * @since jdk-11.0.13
 */
@RestController
@Validated
@RequestMapping("${controller.path}/fx-deals")
public class FxDealController {

    private static final Logger LOG = LoggerFactory.getLogger(FxDealController.class);

    private final FxDealService fxDealService;
    private final ExchangeInsuranceDtoSerializer serializer;
    private final QueryUtils queryUtils;

    /**
     * Fx Deals constructor
     *
     * @param fxDealService      Service that provides necessary functionality to this controller
     * @param serializer         Component for the conversion and adaptation of the data structure
     * @param queryUtils         get some info to build a FxDealQuery object
     */
    public FxDealController(FxDealService fxDealService, ExchangeInsuranceDtoSerializer serializer,
                            QueryUtils queryUtils) {
        this.fxDealService = fxDealService;
        this.serializer = serializer;
        this.queryUtils = queryUtils;
    }

    /**
     * This method get some information and return a list of exchange insurances
     * @param customerId a string with the customer id
     * @param currencyBuy a String with the currency to buy
     * @param currencySell a String with the currency to sell
     * @param buy a boolean value to indicate if it is a purchase
     * @param amount a double value with the amount
     * @return a list of exchange insurances
     */
    @GetMapping
    @Secured("ROLE_USER")
    public ExchangeInsuranceResponse getExchangeInsurances(@RequestParam (name = "customer_id") String customerId,
                                                           @RequestParam (name = "currency_buy") String currencyBuy,
                                                           @RequestParam (name = "currency_sell") String currencySell,
                                                           @RequestParam boolean buy,
                                                           @RequestParam Double amount) {
        List<ExchangeInsuranceDto> exchangeInsurances = fxDealService.searchFxDeals(
                        queryUtils.generateFxDealQuery(customerId, currencyBuy, currencySell, buy, amount))
                .stream().map(serializer::toDto).collect(Collectors.toList());
        ExchangeInsuranceResponse result = new ExchangeInsuranceResponse(exchangeInsurances);
        LOG.debug("getExchangeInsurances() returned: {}", result);
        return result;
    }
}
