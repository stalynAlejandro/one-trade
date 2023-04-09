package com.pagonxt.onetradefinance.work.utils;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.TimeZone;

@UnitTest
class DateUtilsTest {

    private static final String TIME_ZONE_STRING = "Europe/Madrid";
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone(TIME_ZONE_STRING);
    public static final String DATE_FORMAT = "yyyyMMdd-HHmmssSSSZ";

    @Test
    void givenValidData_getStartOfDay_returnsFixedDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TIME_ZONE);
        // Given
        Date testDate = dateFormat.parse("20220719-135325123+0200");
        Date expectedDate = dateFormat.parse("20220719-000000000+0200");

        // When
        Date result = DateUtils.getStartOfDay(testDate, TIME_ZONE_STRING);

        // Then
        Assertions.assertEquals(expectedDate, result, "Result should match template");
    }

    @Test
    void givenValidData_getEndOfDay_returnsFixedDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TIME_ZONE);
        // Given
        Date testDate = dateFormat.parse("20220719-135325456+0200");
        Date expectedDate = dateFormat.parse("20220719-235959999+0200");

        // When
        Date result = DateUtils.getEndOfDay(testDate, TIME_ZONE_STRING);

        // Then
        Assertions.assertEquals(expectedDate, result, "Result should match template");
    }

    @Test
    void localDateToDate_whenLocalDate_returnsDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TIME_ZONE);
        // Given
        Date expectedDate = dateFormat.parse("20220826-000000000+0200");
        LocalDate localDate = LocalDate.of(2022, 8, 26);
        // When
        Date result = DateUtils.localDateToDate(localDate);

        // Then
        Assertions.assertEquals(expectedDate, result);
    }


    @Test
    void localDateToDate_whenNull_returnsNull() {
        // Given, When and Then
        Assertions.assertNull(DateUtils.localDateToDate(null));
    }

    @Test
    void dateToLocalDate_whenLocalDate_returnsDate() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TIME_ZONE);
        // Given
        LocalDate expectedLocalDate = LocalDate.of(2022, 8, 26);
        Date date = dateFormat.parse("20220826-000000000+0200");
        // When
        LocalDate result = DateUtils.dateToLocalDate(date);

        // Then
        Assertions.assertEquals(expectedLocalDate, result);
    }

    @Test
    void dateToLocalDate_whenNull_returnsNull() {
        // Given, When and Then
        Assertions.assertNull(DateUtils.dateToLocalDate(null));
    }
}
