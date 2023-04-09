package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class CurrencyDtoSerializerTest {

    @InjectMocks
    private CurrencyDtoSerializer serializer;

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        CurrencyDAO currencyDAO1 = new CurrencyDAO();
        currencyDAO1.setCurrency("EUR");
        CurrencyDto expectedResult = new CurrencyDto();
        expectedResult.setId("EUR");
        expectedResult.setCurrency("EUR");
        // When
        CurrencyDto result = serializer.toDto(currencyDAO1);
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given and When
        CurrencyDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }

    @Test
    void toDto_whenBlankCurrency_returnsNull() {
        // Given
        CurrencyDAO currencyDAO1 = new CurrencyDAO();
        currencyDAO1.setCurrency(" ");
        // When
        CurrencyDto result = serializer.toDto(currencyDAO1);
        // Then
        assertNull(result);
    }
}
