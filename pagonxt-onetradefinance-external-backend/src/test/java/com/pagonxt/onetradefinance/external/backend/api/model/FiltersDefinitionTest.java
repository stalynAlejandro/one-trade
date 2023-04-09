package com.pagonxt.onetradefinance.external.backend.api.model;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class FiltersDefinitionTest {

    @Test
    void toString_ok_returnString() {
        //Given
        FilterDefinition filterDefinition = new FilterDefinition();
        filterDefinition.setType("typeTest");
        FiltersDefinition filtersDefinition = new FiltersDefinition();
        filtersDefinition.put("code", filterDefinition);
        // When
        String result = filtersDefinition.toString();
        // Then
        assertEquals("FiltersDefinition{} {code=FilterDefinition{type='typeTest', options=null}}", result);
    }

}
