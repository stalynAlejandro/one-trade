package com.pagonxt.onetradefinance.work.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class ExchangeInsuranceFlowableMapperTest {

    private final DateFormat dateTimeFormat;

    @InjectMocks
    private ExchangeInsuranceFlowableMapper exchangeInsuranceFlowableMapper;

    ExchangeInsuranceFlowableMapperTest() {
        dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    void toModel_whenPassingDataObject_returnListExchangeInsurance() throws JsonProcessingException {
        // Given
        String id = "id1";
        String type = "type1";
        Date useDate = new Date();
        String useDateString = dateTimeFormat.format(useDate);
        Double buyAmount = 12.34d;
        Double sellAmount = 15.89d;
        Double exchangeRate = 1.235d;
        Double amountToUse = 5.12d;
        String buyCurrency = "GBP";
        String sellCurrency = "EUR";
        List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList = new ArrayList<>();
        ExchangeInsuranceFlowable exchangeInsuranceFlowable = new ExchangeInsuranceFlowable();
        exchangeInsuranceFlowable.setId(id);
        exchangeInsuranceFlowable.setType(type);
        exchangeInsuranceFlowable.setUseDate(useDateString);
        exchangeInsuranceFlowable.setBuyAvailableAmount(buyAmount);
        exchangeInsuranceFlowable.setSellAvailableAmount(sellAmount);
        exchangeInsuranceFlowable.setExchangeRate(exchangeRate);
        exchangeInsuranceFlowable.setAmountToUse(amountToUse);
        exchangeInsuranceFlowableList.add(exchangeInsuranceFlowable);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainer.getVariable("exchangeInsuranceDetails")).thenReturn(exchangeInsuranceFlowableList);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceBuyCurrency")).thenReturn(buyCurrency);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceSellCurrency")).thenReturn(sellCurrency);
        // When
        List<ExchangeInsurance> result = exchangeInsuranceFlowableMapper.toModel(dataObjectInstanceVariableContainer);
        // Then
        assertEquals(1, result.size());
        ExchangeInsurance exchangeInsurance = result.get(0);
        assertEquals(id, exchangeInsurance.getExchangeInsuranceId());
        assertEquals(type, exchangeInsurance.getType());
        assertEquals(useDate, exchangeInsurance.getUseDate());
        assertEquals(buyAmount, exchangeInsurance.getBuyAmount());
        assertEquals(sellAmount, exchangeInsurance.getSellAmount());
        assertEquals(exchangeRate, exchangeInsurance.getExchangeRate());
        assertEquals(amountToUse, exchangeInsurance.getUseAmount());
        assertEquals(buyCurrency, exchangeInsurance.getBuyCurrency());
        assertEquals(sellCurrency, exchangeInsurance.getSellCurrency());
    }

    @Test
    void toModel_whenPassingNullUseDate_returnListExchangeInsuranceWithNullUseDate() throws JsonProcessingException {
        // Given
        String id = "id1";
        String type = "type1";
        Double buyAmount = 12.34d;
        Double sellAmount = 15.89d;
        Double exchangeRate = 1.235d;
        Double amountToUse = 5.12d;
        String buyCurrency = "GBP";
        String sellCurrency = "EUR";
        List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList = new ArrayList<>();
        ExchangeInsuranceFlowable exchangeInsuranceFlowable = new ExchangeInsuranceFlowable();
        exchangeInsuranceFlowable.setId(id);
        exchangeInsuranceFlowable.setType(type);
        exchangeInsuranceFlowable.setUseDate(null);
        exchangeInsuranceFlowable.setBuyAvailableAmount(buyAmount);
        exchangeInsuranceFlowable.setSellAvailableAmount(sellAmount);
        exchangeInsuranceFlowable.setExchangeRate(exchangeRate);
        exchangeInsuranceFlowable.setAmountToUse(amountToUse);
        exchangeInsuranceFlowableList.add(exchangeInsuranceFlowable);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainer.getVariable("exchangeInsuranceDetails")).thenReturn(exchangeInsuranceFlowableList);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceBuyCurrency")).thenReturn(buyCurrency);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceSellCurrency")).thenReturn(sellCurrency);
        // When
        List<ExchangeInsurance> result = exchangeInsuranceFlowableMapper.toModel(dataObjectInstanceVariableContainer);
        // Then
        assertEquals(1, result.size());
        ExchangeInsurance exchangeInsurance = result.get(0);
        assertNull(exchangeInsurance.getUseDate());
    }

    @Test
    void toModel_whenPassingUnparseableDate_thenThrowException() {
        // Given
        String id = "id1";
        String type = "type1";
        String useDateString = "parseError";
        Double buyAmount = 12.34d;
        Double sellAmount = 15.89d;
        Double exchangeRate = 1.235d;
        Double amountToUse = 5.12d;
        String buyCurrency = "GBP";
        String sellCurrency = "EUR";
        List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList = new ArrayList<>();
        ExchangeInsuranceFlowable exchangeInsuranceFlowable = new ExchangeInsuranceFlowable();
        exchangeInsuranceFlowable.setId(id);
        exchangeInsuranceFlowable.setType(type);
        exchangeInsuranceFlowable.setUseDate(useDateString);
        exchangeInsuranceFlowable.setBuyAvailableAmount(buyAmount);
        exchangeInsuranceFlowable.setSellAvailableAmount(sellAmount);
        exchangeInsuranceFlowable.setExchangeRate(exchangeRate);
        exchangeInsuranceFlowable.setAmountToUse(amountToUse);
        exchangeInsuranceFlowableList.add(exchangeInsuranceFlowable);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainer.getVariable("exchangeInsuranceDetails")).thenReturn(exchangeInsuranceFlowableList);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceBuyCurrency")).thenReturn(buyCurrency);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceSellCurrency")).thenReturn(sellCurrency);
        // When
        Exception exception = assertThrows(DateFormatException.class, () -> exchangeInsuranceFlowableMapper.toModel(dataObjectInstanceVariableContainer));
        // Then
        assertEquals("Unable to parse date", exception.getMessage());
    }

    @Test
    void toModel_whenPassingNullDataObject_returnEmptyList() throws JsonProcessingException {
        // Given
        // When
        List<ExchangeInsurance> result = exchangeInsuranceFlowableMapper.toModel((DataObjectInstanceVariableContainer) null);
        // Then
        assertEquals(0, result.size());
    }

    @Test
    void toModel_whenPassingNullExchangeInsuranceDetails_returnEmptyList() throws JsonProcessingException {
        // Given
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainer.getVariable("exchangeInsuranceDetails")).thenReturn(null);
        // When
        List<ExchangeInsurance> result = exchangeInsuranceFlowableMapper.toModel(dataObjectInstanceVariableContainer);
        // Then
        assertEquals(0, result.size());
    }

    @Test
    void toModel_whenPassingExchangeInsuranceDetailsWithNullElements_returnEmptyList() throws JsonProcessingException {
        // Given
        String buyCurrency = "GBP";
        String sellCurrency = "EUR";
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList = new ArrayList<>();
        exchangeInsuranceFlowableList.add(null);
        when(dataObjectInstanceVariableContainer.getVariable("exchangeInsuranceDetails")).thenReturn(exchangeInsuranceFlowableList);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceBuyCurrency")).thenReturn(buyCurrency);
        when(dataObjectInstanceVariableContainer.getString("exchangeInsuranceSellCurrency")).thenReturn(sellCurrency);
        // When
        List<ExchangeInsurance> result = exchangeInsuranceFlowableMapper.toModel(dataObjectInstanceVariableContainer);
        // Then
        assertEquals(0, result.size());
    }

    @Test
    void toFlowable_whenPassingExchangeInsurances_returnListExchangeInsuranceFlowable() {
        // Given
        String id = "id1";
        String type = "type1";
        Date useDate = new Date();
        String useDateString = dateTimeFormat.format(useDate);
        Double buyAmount = 12.34d;
        Double sellAmount = 15.89d;
        Double exchangeRate = 1.235d;
        Double amountToUse = 5.12d;
        List<ExchangeInsurance> exchangeInsurances = new ArrayList<>();
        ExchangeInsurance exchangeInsurance = new ExchangeInsurance();
        exchangeInsurance.setExchangeInsuranceId(id);
        exchangeInsurance.setType(type);
        exchangeInsurance.setUseDate(useDate);
        exchangeInsurance.setBuyAmount(buyAmount);
        exchangeInsurance.setSellAmount(sellAmount);
        exchangeInsurance.setExchangeRate(exchangeRate);
        exchangeInsurance.setUseAmount(amountToUse);
        exchangeInsurances.add(exchangeInsurance);
        // When
        List<ExchangeInsuranceFlowable> result = exchangeInsuranceFlowableMapper.toFlowable(exchangeInsurances);
        // Then
        assertEquals(1, result.size());
        ExchangeInsuranceFlowable exchangeInsuranceFlowable = result.get(0);
        assertEquals(id, exchangeInsuranceFlowable.getId());
        assertEquals(type, exchangeInsuranceFlowable.getType());
        assertEquals(useDateString, exchangeInsuranceFlowable.getUseDate());
        assertEquals(buyAmount, exchangeInsuranceFlowable.getBuyAvailableAmount());
        assertEquals(sellAmount, exchangeInsuranceFlowable.getSellAvailableAmount());
        assertEquals(exchangeRate, exchangeInsuranceFlowable.getExchangeRate());
        assertEquals(amountToUse, exchangeInsuranceFlowable.getAmountToUse());
    }

    @Test
    void toFlowable_whenPassingExchangeInsurancesWithNullUseDate_returnListExchangeInsuranceFlowableWithNullUseDate() {
        // Given
        String id = "id1";
        String type = "type1";
        Double buyAmount = 12.34d;
        Double sellAmount = 15.89d;
        Double exchangeRate = 1.235d;
        Double amountToUse = 5.12d;
        List<ExchangeInsurance> exchangeInsurances = new ArrayList<>();
        ExchangeInsurance exchangeInsurance = new ExchangeInsurance();
        exchangeInsurance.setExchangeInsuranceId(id);
        exchangeInsurance.setType(type);
        exchangeInsurance.setUseDate(null);
        exchangeInsurance.setBuyAmount(buyAmount);
        exchangeInsurance.setSellAmount(sellAmount);
        exchangeInsurance.setExchangeRate(exchangeRate);
        exchangeInsurance.setUseAmount(amountToUse);
        exchangeInsurances.add(exchangeInsurance);
        // When
        List<ExchangeInsuranceFlowable> result = exchangeInsuranceFlowableMapper.toFlowable(exchangeInsurances);
        // Then
        assertEquals(1, result.size());
        ExchangeInsuranceFlowable exchangeInsuranceFlowable = result.get(0);
        assertEquals(id, exchangeInsuranceFlowable.getId());
        assertEquals(type, exchangeInsuranceFlowable.getType());
        assertNull(exchangeInsuranceFlowable.getUseDate());
        assertEquals(buyAmount, exchangeInsuranceFlowable.getBuyAvailableAmount());
        assertEquals(sellAmount, exchangeInsuranceFlowable.getSellAvailableAmount());
        assertEquals(exchangeRate, exchangeInsuranceFlowable.getExchangeRate());
        assertEquals(amountToUse, exchangeInsuranceFlowable.getAmountToUse());
    }

    @Test
    void toFlowable_whenPassingNullExchangeInsurances_returnEmptyList() {
        // Given
        // When
        List<ExchangeInsuranceFlowable> result = exchangeInsuranceFlowableMapper.toFlowable((List<ExchangeInsurance>) null);
        // Then
        assertEquals(0, result.size());
    }

    @Test
    void toModel_whenPassingExchangeInsurancesWithNullElements_returnEmptyList() {
        // Given
        List<ExchangeInsurance> exchangeInsurances = new ArrayList<>();
        exchangeInsurances.add(null);
        // When
        List<ExchangeInsuranceFlowable> result = exchangeInsuranceFlowableMapper.toFlowable(exchangeInsurances);
        // Then
        assertEquals(0, result.size());
    }
}
