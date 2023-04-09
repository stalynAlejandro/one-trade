package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@UnitTest
class ApiParamsUtilsTest {

    private static ApiParamsUtils apiParamsUtils;

    @BeforeAll
    public static void setup() {
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        apiParamsUtils = new ApiParamsUtils(dateFormatProperties);
    }

    @Test
    void injectQueryParam_valueIsNull_doesNothing() {
        // Given
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString("testUrl/special-prices/");
        // When
        apiParamsUtils.injectQueryParam(urlBuilder, "param", (String) null);
        // Then
        int size = urlBuilder.build().getQueryParams().size();
        assertTrue("Query Params should be 0", size == 0);
    }

    @Test
    void injectQueryParam_valueIsDate_addsQueryParam() {
        // Given
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString("testUrl/special-prices/");
        // When
        apiParamsUtils.injectQueryParam(urlBuilder, "param", new Date());
        // Then
        int size = urlBuilder.build().getQueryParams().size();
        assertTrue("Query Params should be greater than 0", size > 0);
    }

    @Test
    void injectQueryParam_valueIsDouble_addsQueryParam() {
        // Given
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString("testUrl/special-prices/");
        // When
        apiParamsUtils.injectQueryParam(urlBuilder, "param", 4.56D);
        // Then
        int size = urlBuilder.build().getQueryParams().size();
        assertTrue("Query Params should be greater than 0", size > 0);
    }

    @Test
    void injectQueryParam_valueIsInteger_addsQueryParam() {
        // Given
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString("testUrl/special-prices/");
        // When
        apiParamsUtils.injectQueryParam(urlBuilder, "param", 3);
        // Then
        int size = urlBuilder.build().getQueryParams().size();
        assertTrue("Query Params should be greater than 0", size > 0);
    }
}
