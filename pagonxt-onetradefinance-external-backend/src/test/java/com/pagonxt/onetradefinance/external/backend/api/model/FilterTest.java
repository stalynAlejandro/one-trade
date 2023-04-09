package com.pagonxt.onetradefinance.external.backend.api.model;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class FilterTest {

    @Test
    void toString_ok_returnString() {
        Filter filter = new Filter();
        filter.setValue("codeTest");
        String result = filter.toString();
        // Then
        assertEquals("Filter{type='null', value=codeTest, values=null}", result);
    }

}
