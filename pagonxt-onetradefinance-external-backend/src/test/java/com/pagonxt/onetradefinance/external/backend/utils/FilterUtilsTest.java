package com.pagonxt.onetradefinance.external.backend.utils;

import com.pagonxt.onetradefinance.external.backend.api.model.Filter;
import com.pagonxt.onetradefinance.external.backend.api.model.Filters;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class FilterUtilsTest {

    private final Filters filters = new Filters();

    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Test
    void getString_whenString_returnValue() {
        // Given
        Filter filter = new Filter();
        filter.setValue("codeTest");
        filters.put("code", filter);
        // When
        String result = FilterUtils.getString(filters, "code");
        // Then
        assertEquals("codeTest", result);
    }

    @Test
    void getString_whenFilterNotFound_returnNull() {
        // When
        String result = FilterUtils.getString(filters, "Unknown");
        // Then
        assertNull(result);
    }

    @Test
    void getString_whenStringHasNoText_returnNull() {
        Filter filter = new Filter();
        filter.setValue("");
        filters.put("code", filter);
        // When
        String result = FilterUtils.getString(filters, "code");
        // Then
        assertNull(result);
    }

    @Test
    void getStringList_whenString_returnValue() {
        // Given
        Filter filter = new Filter();
        List<String> expectedList = List.of("product1", "product2");
        filter.setValues(expectedList);
        filters.put("productId", filter);
        // When
        List<String> result = FilterUtils.getStringList(filters, "productId");
        // Then
        assertEquals(expectedList, result);
    }

    @Test
    void getStringList_whenFilterNotFound_returnEmptyList() {
        // When
        List<String> result = FilterUtils.getStringList(filters, "Unknown");
        // Then
        assertEquals(0, result.size());
    }

    @Test
    void getStringList_whenListIsEmpty_returnEmptyList() {
        // Given
        Filter filter = new Filter();
        filter.setValues(new ArrayList<>());
        filters.put("productId", filter);
        // When
        List<String> result = FilterUtils.getStringList(filters, "productId");
        // Then
        assertEquals(0, result.size());
    }

    @Test
    void getStringList_whenListIsNull_returnEmptyList() {
        // Given
        Filter filter = new Filter();
        filter.setValues(null);
        filters.put("productId", filter);
        // When
        List<String> result = FilterUtils.getStringList(filters, "productId");
        // Then
        assertEquals(0, result.size());
    }

    @Test
    void getJSONDate_whenValidDate_returnValue() throws ParseException {
        // Given
        Filter filter = new Filter();
        filter.setValue("2022-08-15T22:00:00.000Z");
        Date date = dateTimeFormat.parse("2022-08-15T22:00:00.000Z");
        filters.put("fromDate", filter);
        // When
        Date result = FilterUtils.getJSONDate(filters, "fromDate");
        // Then
        assertEquals(date, result);
    }

    @Test
    void getJSONDate_whenInvalidDate_throwDateFormatException() {
        // Given
        Filter filter = new Filter();
        filter.setValue("2022-12-15");
        filters.put("fromDate", filter);
        // When and then
        assertThrows(DateFormatException.class,
                () -> FilterUtils.getJSONDate(filters, "fromDate"));
    }

    @Test
    void getJSONDate_whenFilterNotFound_returnNull() {
        // When
        Date result = FilterUtils.getJSONDate(filters, "Unknown");
        // Then
        assertNull(result);
    }

    @Test
    void getJSONDate_whenValueIsEmpty_returnNull() {
        Filter filter = new Filter();
        filter.setValue("");
        filters.put("fromDate", filter);
        // When
        Date result = FilterUtils.getJSONDate(filters, "fromDate");
        // Then
        assertNull(result);
    }

    @Test
    void getJSONDate_whenValueIsNull_returnNull() {
        Filter filter = new Filter();
        filter.setValue(null);
        filters.put("fromDate", filter);
        // When
        Date result = FilterUtils.getJSONDate(filters, "fromDate");
        // Then
        assertNull(result);
    }

    @Test
    void getDouble_whenDouble_returnValue() {
        // Given
        Filter filter = new Filter();
        filter.setValue("234.56");
        filters.put("fromAmount", filter);
        // When
        Double result = FilterUtils.getDouble(filters, "fromAmount");
        // Then
        assertEquals(234.56d, result);
    }

    @Test
    void getDouble_whenInvalidDouble_throwNumberFormatException() {
        // Given
        Filter filter = new Filter();
        filter.setValue("234,56");
        filters.put("fromAmount", filter);
        // When and then
        assertThrows(NumberFormatException.class,
                () -> FilterUtils.getDouble(filters, "fromAmount"));
    }

    @Test
    void getDouble_whenFilterNotFound_returnNull() {
        // When
        Double result = FilterUtils.getDouble(filters, "Unknown");
        // Then
        assertNull(result);
    }

    @Test
    void getDouble_whenValueIsEmpty_returnNull() {
        Filter filter = new Filter();
        filter.setValue("");
        filters.put("fromAmount", filter);
        // When
        Double result = FilterUtils.getDouble(filters, "fromAmount");
        // Then
        assertNull(result);
    }

    @Test
    void getDouble_whenValueIsNull_returnNull() {
        Filter filter = new Filter();
        filter.setValue(null);
        filters.put("fromAmount", filter);
        // When
        Double result = FilterUtils.getDouble(filters, "fromAmount");
        // Then
        assertNull(result);
    }
}
