package com.pagonxt.onetradefinance.integrations.model;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class CollectionLoanProductTest {

    @ParameterizedTest
    @CsvSource({"CLE, 206601",
                "CLI, 185601"})
    void getProductCode_whenProductId_returnCode(String productId, String expectedCode) {
        // Given, When and Then
        assertEquals(expectedCode, CollectionLoanProduct.getProductCode(productId));
    }

}
