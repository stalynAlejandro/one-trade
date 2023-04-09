package com.pagonxt.onetradefinance.external.backend.api.model;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class FiltersTest {

    @Test
    void toString_ok_returnString() {
        Filters filters = new Filters();
        Filter filter = new Filter();
        filter.setValue("codeTest");
        filters.put("code", filter);
        String result = filters.toString();
        // Then
        assertEquals("Filters{} {code=Filter{type='null', value=codeTest, values=null}}", result);
    }

}
