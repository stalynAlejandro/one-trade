package com.pagonxt.onetradefinance.integrations.model;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class TradeSpecialPricesQueryBuilderTest {

    @Test
    void build_ok_returnTradeSpecialPricesQuery(){
        // Given and When
        TradeSpecialPricesQuery tradeSpecialPricesQuery = TradeSpecialPricesQuery.TradeSpecialPricesQueryBuilder
                .tradeSpecialPricesQuery().agreementId("AI").productId("PI").conceptId("CI").customerId("CI")
                .country("ES").currencyCode("CC").amount(1d).queryDate("20221110").branchId("BI")
                .term(2d).termType("TT").build();
        // Then
        assertEquals("AI", tradeSpecialPricesQuery.getAgreementId());
        assertEquals("PI", tradeSpecialPricesQuery.getProductId());
        assertEquals("CI", tradeSpecialPricesQuery.getConceptId());
        assertEquals("CI", tradeSpecialPricesQuery.getCustomerId());
        assertEquals("ES", tradeSpecialPricesQuery.getCountry());
        assertEquals("CC", tradeSpecialPricesQuery.getCurrencyCode());
        assertEquals(1d, tradeSpecialPricesQuery.getAmount());
        assertEquals("20221110", tradeSpecialPricesQuery.getQueryDate());
        assertEquals("BI", tradeSpecialPricesQuery.getBranchId());
        assertEquals(2d, tradeSpecialPricesQuery.getTerm());
        assertEquals("TT", tradeSpecialPricesQuery.getTermType());
    }

}
