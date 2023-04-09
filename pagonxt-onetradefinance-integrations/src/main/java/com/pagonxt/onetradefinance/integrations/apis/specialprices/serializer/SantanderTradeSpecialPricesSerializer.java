package com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer;

import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.Concept;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SantanderTradeSpecialPricesResponse;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SpecialTier;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.StandardTier;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.service.PeriodicityService;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class to serialize Santander special prices data
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.PeriodicityService
 * @since jdk-11.0.13
 */
@Component
public class SantanderTradeSpecialPricesSerializer {

    private final PeriodicityService periodicityService;

    /**
     * Class constructor
     * @param periodicityService PeriodicityService object
     */
    public SantanderTradeSpecialPricesSerializer(PeriodicityService periodicityService) {
        this.periodicityService = periodicityService;
    }

    /**
     * Class method to serialize a list of special prices
     * @param santanderTradeSpecialPricesResponse SantanderTradeSpecialPricesResponse object
     * @param locale a string with locale value
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SantanderTradeSpecialPricesResponse
     * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices
     * @return an empty list or a list of special prices
     */
    public List<TradeSpecialPrices> toModel(SantanderTradeSpecialPricesResponse santanderTradeSpecialPricesResponse,
                                            String locale) {
        if(santanderTradeSpecialPricesResponse == null) {
            return Collections.emptyList();
        }

        return santanderTradeSpecialPricesResponse.getConcepts().stream()
                .map(c -> toModel(c, locale))
                .collect(Collectors.toList());
    }

    /**
     * Class method to serialize a trade special price
     * @param concept a Concept object
     * @param locale a string with local value
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.Concept
     * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices
     * @return a TradeSpecialPrices object
     */
    public TradeSpecialPrices toModel(Concept concept, String locale) {
        if(concept == null) {
            return new TradeSpecialPrices();
        }

        TradeSpecialPrices specialPrices = new TradeSpecialPrices();
        specialPrices.setConceptName(concept.getConceptName());
        specialPrices.setPeriodicity(periodicityService
                .getConceptPeriodicityTranslated(concept.getConceptId(),locale));
        getData(specialPrices, concept, locale);
        return specialPrices;
    }

    /**
     * Class method to get necessary data from special prices
     * @param specialPrices TradeSpecialPrices object
     * @param concept a Concept object
     * @param locale a string with locale value
     * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.Concept
     */
    private void getData(TradeSpecialPrices specialPrices, Concept concept, String locale) {
        Double percentage = null;
        Double amount = null;
        String currencyCode = null;
        if(concept.getSpecialPrice() != null) {
            SpecialTier specialTier = concept.getSpecialPrice().getSpecialTier().stream()
                    .findFirst()
                    .orElse(new SpecialTier());
            if(specialTier.getSpecialFixedRatePrice() != null) {
                percentage = specialTier.getSpecialFixedRatePrice().getSpecialRate();
                amount = specialTier.getSpecialFixedRatePrice().getSpecialMinimumAmount();
                currencyCode = specialTier.getSpecialFixedRatePrice().getCurrencyCode();
            } else if(specialTier.getSpecialFixedAmountPrice() != null) {
                amount = specialTier.getSpecialFixedAmountPrice().getSpecialFixedAmount();
                currencyCode = specialTier.getSpecialFixedAmountPrice().getCurrencyCode();
            }
        } else if(concept.getStandardPrice() != null) {
            StandardTier standardTier = concept.getStandardPrice().getStandardTier().stream()
                    .findFirst()
                    .orElse(new StandardTier());
            if(standardTier.getStandardFixedRatePrice() != null) {
                percentage = standardTier.getStandardFixedRatePrice().getStandardRate();
                amount = standardTier.getStandardFixedRatePrice().getStandardMinimumAmount();
                currencyCode = standardTier.getStandardFixedRatePrice().getCurrencyCode();
                specialPrices.setCurrencyCode(standardTier.getStandardFixedRatePrice().getCurrencyCode());
            } else if(standardTier.getStandardFixedAmountPrice() != null) {
                amount = standardTier.getStandardFixedAmountPrice().getStandardFixedAmount();
                currencyCode = standardTier.getStandardFixedAmountPrice().getCurrencyCode();
            }
        }
        specialPrices.setPercentage(ParseUtils.parseDoubleToString(percentage, locale));
        specialPrices.setAmount(ParseUtils.parseDoubleToString(amount, locale));
        specialPrices.setCurrencyCode(currencyCode);
    }
}
