package com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer;

import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDeal;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealDetails;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.SantanderFxDealResponse;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

/**
 * Class to serialize Santander fx deals data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class SantanderFxDealSerializer {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final DateFormat dateFormat;

    /**
     * Class constructor
     * @param dateFormatProperties DateFormatProperties object
     */
    public SantanderFxDealSerializer(DateFormatProperties dateFormatProperties) {
        dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(dateFormatProperties.getTimeZone()));
    }

    /**
     * Class method to serialize a list of exchange insurances
     * @param santanderFxDealResponse a SantanderFxDealResponse object
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.SantanderFxDealResponse
     * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
     * @return an empty list or a list of exchange insurances
     */
    public List<ExchangeInsurance> toModel(SantanderFxDealResponse santanderFxDealResponse) {
        if (santanderFxDealResponse == null || santanderFxDealResponse.getFxDealList() == null) {
            return Collections.emptyList();
        }
        return santanderFxDealResponse.getFxDealList().stream()
                .map(FxDeal::getFxDealDetails).map(this::toModel).collect(Collectors.toList());
    }

    /**
     * Class method to serialize an exchange insurance
     * @param fxDealDetails a FxDealDetails object
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealDetails
     * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
     * @return a ExchangeInsurance object
     */
    public ExchangeInsurance toModel(FxDealDetails fxDealDetails) {
        if (fxDealDetails == null) {
            return new ExchangeInsurance();
        }
        ExchangeInsurance exchangeInsurance = new ExchangeInsurance();
        exchangeInsurance.setExchangeInsuranceId(fxDealDetails.getFxDealId());
        exchangeInsurance.setType(ParseUtils.parseKey(fxDealDetails.getFxDealType()));
        if (fxDealDetails.getFxDealUseDate() != null) {
            try {
                exchangeInsurance.setUseDate(dateFormat.parse(fxDealDetails.getFxDealUseDate()));
            } catch (ParseException e) {
                throw new DateFormatException(fxDealDetails.getFxDealUseDate(), e);
            }
        }
        if (fxDealDetails.getSellAvailableAmount() != null) {
            if (fxDealDetails.getSellAvailableAmount().getAmount() != null) {
                exchangeInsurance.setSellAmount(fxDealDetails.getSellAvailableAmount().getAmount());
            }
            exchangeInsurance.setSellCurrency(fxDealDetails.getSellAvailableAmount().getCurrency());
        }
        if (fxDealDetails.getBuyAvailableAmount() != null) {
            if (fxDealDetails.getBuyAvailableAmount().getAmount() != null) {
                exchangeInsurance.setBuyAmount(fxDealDetails.getBuyAvailableAmount().getAmount());
            }
            exchangeInsurance.setBuyCurrency(fxDealDetails.getBuyAvailableAmount().getCurrency());
        }
        if (fxDealDetails.getExchangeRate() != null) {
            exchangeInsurance.setExchangeRate(fxDealDetails.getExchangeRate());
        }
        return exchangeInsurance;
    }

}
