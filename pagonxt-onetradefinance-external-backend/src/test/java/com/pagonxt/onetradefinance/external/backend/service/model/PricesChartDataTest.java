package com.pagonxt.onetradefinance.external.backend.service.model;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class PricesChartDataTest {

    @Test
    void commissionConstructor_ok_returnCommission() {
        TradeSpecialPrices tradeSpecialPrices = new TradeSpecialPrices();
        tradeSpecialPrices.setConceptName("concept1");
        tradeSpecialPrices.setAmount("value1");
        tradeSpecialPrices.setPercentage("percentage1");
        tradeSpecialPrices.setPeriodicity("periodicity1");
        // When
        PricesChartData.Commission result = new PricesChartData.Commission(tradeSpecialPrices);
        // Then
        assertEquals("concept1", result.getConcept());
        assertEquals("value1", result.getValue());
        assertEquals("percentage1", result.getPercentage());
        assertEquals("periodicity1", result.getPeriodicity());
    }

    @Test
    void commissionConstructor_nullValues_returnCommission() {
        TradeSpecialPrices tradeSpecialPrices = new TradeSpecialPrices();
        tradeSpecialPrices.setConceptName("concept1");
        tradeSpecialPrices.setAmount("value1");
        tradeSpecialPrices.setPercentage(null);
        tradeSpecialPrices.setPeriodicity(null);
        // When
        PricesChartData.Commission result = new PricesChartData.Commission(tradeSpecialPrices);
        // Then
        assertEquals("concept1", result.getConcept());
        assertEquals("value1", result.getValue());
        assertNull(result.getPercentage());
        assertNull(result.getPeriodicity());
    }

}
