package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.integrations.service.CalendarService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@UnitTest
class CalendarExpressionsTest {

    @Mock
    CalendarService calendarService;

    @InjectMocks
    CalendarExpressions calendarExpressions;

    @Test
    void nextWorkDay_ok_returnsCorrectlDateTime() {
        java.time.LocalDateTime currentLocalDateTime = java.time.LocalDateTime.parse("2022-01-01T10:11:12.123");
        java.time.LocalDateTime expectedLocalDateTime = java.time.LocalDateTime.parse("2022-01-03T10:11:12.123");
        ZoneOffset zoneOffSet = ZoneId.of("Europe/Madrid").getRules().getOffset(expectedLocalDateTime);
        org.joda.time.DateTime expectedResult = new org.joda.time.LocalDateTime(
                expectedLocalDateTime.toInstant(zoneOffSet).toEpochMilli()).toDateTime();
        try (MockedStatic<java.time.LocalDateTime> mockedLocalDateTime = Mockito.mockStatic(java.time.LocalDateTime.class)){
            mockedLocalDateTime.when(java.time.LocalDateTime::now).thenReturn(currentLocalDateTime);
            when(calendarService.nextWorkDay(1)).thenReturn(Duration.ofDays(2));
            //when
            org.joda.time.DateTime result = calendarExpressions.nextWorkDay(1);
            //then
            assertEquals(expectedResult,result);
        }
    }

    @Test
    void testNextWorkDay() {
        // Given
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        when(calendarService.nextWorkDay()).thenReturn(tomorrow);

        // When
        LocalDate result = calendarExpressions.nextWorkDay();

        // Then
        assertEquals(result.plusDays(-1).getDayOfMonth(), LocalDate.now().getDayOfMonth());
    }
}
