package com.pagonxt.onetradefinance.external.backend.utils;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class QueryUtilsTest {

    @InjectMocks
    private QueryUtils queryUtils;
    @Mock
    DateFormatProperties dateFormatProperties;

    @Test
    void generateFxDealQuery_whenBuy_returnValue() {
        // Given, When
        FxDealQuery result = queryUtils.generateFxDealQuery("customerTest", "EUR", "GBP", true, 120.32d);
        // Then
        assertEquals("customerTest", result.getCustomerId());
        assertEquals("EUR", result.getBuyCurrency());
        assertEquals("GBP", result.getSellCurrency());
        assertEquals(120.32d, result.getBalanceFxDealAmount());
        assertEquals("EUR", result.getBalanceFxDealCurrency());
        assertEquals("BUY", result.getDirection());
    }

    @Test
    void generateFxDealQuery_whenSell_returnValue() {
        // Given, When
        FxDealQuery result = queryUtils.generateFxDealQuery("customerTest", "EUR", "GBP", false, 120.32d);
        // Then
        assertEquals("customerTest", result.getCustomerId());
        assertEquals("EUR", result.getBuyCurrency());
        assertEquals("GBP", result.getSellCurrency());
        assertEquals(120.32d, result.getBalanceFxDealAmount());
        assertEquals("GBP", result.getBalanceFxDealCurrency());
        assertEquals("SELL", result.getDirection());
    }

    @Test
    void generateRiskLineQuery_ok_returnValue() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ReflectionTestUtils.setField(queryUtils, "dateFormat", dateFormat, DateFormat.class);
        String today = dateFormat.format(new Date());
        // When
        RiskLineQuery result = queryUtils.generateRiskLineQuery("customerTest", "CLE", "2022-10-28", 120.32d, "EUR");
        // Then
        assertEquals("customerTest", result.getCustomerId());
        assertEquals("206601", result.getProductId());
        assertEquals("2022-10-28", result.getExpirationDate());
        assertEquals(120.32d, result.getOperationAmount());
        assertEquals("EUR", result.getOperationCurrency());
        assertTrue(result.isActive());
        assertEquals(today, result.getValueDateOperation());
    }

    @Test
    void generateRiskLineQuery_withNullValues_returnValue() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ReflectionTestUtils.setField(queryUtils, "dateFormat", dateFormat, DateFormat.class);
        String today = dateFormat.format(new Date());
        // When
        RiskLineQuery result = queryUtils.generateRiskLineQuery("customerTest", "CLE", null, null, null);
        // Then
        assertEquals("customerTest", result.getCustomerId());
        assertEquals("206601", result.getProductId());
        assertEquals(today, result.getExpirationDate());
        assertEquals(0, result.getOperationAmount());
        assertEquals("EUR", result.getOperationCurrency());
        assertTrue(result.isActive());
        assertEquals(today, result.getValueDateOperation());
    }

}
