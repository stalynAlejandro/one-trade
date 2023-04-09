package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.integrations.service.CalendarService;
import com.pagonxt.onetradefinance.integrations.service.CountryHolidayService;
import com.pagonxt.onetradefinance.work.common.CaseCommonConstants;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstanceQuery;
import org.flowable.common.engine.impl.runtime.Clock;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.*;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class SlaExpressionsTest {

    @Mock
    CmmnRuntimeService cmmnRuntimeService;

    @Mock
    ProcessEngineConfiguration processEngineConfiguration;

    @Mock
    HistoryService historyService;
    @Mock
    CountryHolidayService countryHolidayService;
    @Mock
    CalendarService calendarService;

    @InjectMocks
    SlaExpressions slaExpressions;

    @ParameterizedTest
    @CsvSource({
        "false, 13:00, 150, 08:00, 16:30, 2022-05-16T11:15:30, 2022-05-16T16:30:00, 2022, 5, 16, true",  // no priority, before cut-off
        "false, 13:00, 150, 08:00, 16:30, 2022-05-16T13:15:30, 2022-05-17T16:30:00, 2022, 5, 16, true",  // no priority, after cut-off
        "false, 13:00, 150, 08:00, 16:30, 2022-05-21T11:15:30, 2022-05-24T16:30:00, 2022, 5, 23, false",  // no priority, no workday
        "false, 13:00, 150, 08:00, 16:30, 2022-05-22T13:15:30, 2022-05-24T16:30:00, 2022, 5, 23, false",  // no priority, no workday
        "true,  13:00, 150, 08:00, 16:30, 2022-05-16T11:15:30, 2022-05-16T13:45:30, 2022, 5, 16, true",  // priority, before cut-off
        "true,  13:00, 150, 08:00, 16:30, 2022-05-16T13:15:30, 2022-05-17T10:30:00, 2022, 5, 16, true",  // priority, after cut-off
        "true,  13:00, 150, 08:00, 16:30, 2022-05-28T11:50:30, 2022-05-30T10:30:00, 2022, 5, 30, false",  // priority, no workday
        "true,  13:00, 150, 08:00, 16:30, 2022-08-13T14:30:00, 2022-08-16T10:30:00, 2022, 8, 16, false"   // priority, no workday
    })
    void calculateSla(boolean priority, String cutOff, long resolutionTime, String startSla, String endSla, String now,
                      String expectedSla, int year, int month, int day, boolean isWorkDay) {
        if (!isWorkDay) {
            when(calendarService.nextWorkDay(any())).thenReturn(LocalDate.of(year, month, day));
        }
        when(calendarService.isWorkDay(any())).thenReturn(isWorkDay);
        initCalculateSla(priority, cutOff, resolutionTime, startSla, endSla, parseInstant(now));
        Instant sla = slaExpressions.calculateSla("caseId");
        assertEquals(parseInstant(expectedSla), sla);
    }

    @ParameterizedTest
    @CsvSource({
        "false, 13:00, 150, 08:00, 16:30, 2022-05-16T11:15:30, 2022-05-16T16:30:00, 2022, 5, 16, true",  // no priority after onHold, before cut-off
        "false, 13:00, 150, 08:00, 16:30, 2022-05-16T13:15:30, 2022-05-17T16:30:00, 2022, 5, 16, true",  // no priority after onHold, after cut-off
        "false, 13:00, 150, 08:00, 16:30, 2022-05-21T13:15:30, 2022-05-24T16:30:00, 2022, 5, 23, false",  // no priority after onHold, no workday
        "true,  13:00, 150, 08:00, 16:30, 2022-05-16T11:15:30, 2022-05-16T13:45:30, 2022, 5, 16, true",  // priority after onHold, before cut-off
        "true,  13:00, 150, 08:00, 16:30, 2022-05-16T13:15:30, 2022-05-17T10:30:00, 2022, 5, 16, true",  // priority after onHold, after cut-off
        "true,  13:00, 150, 08:00, 16:30, 2022-08-13T13:15:30, 2022-08-16T10:30:00, 2022, 8, 16, false"   // priority after onHold, no workday
    })
    void recalculateSla(boolean onHoldPriority, String cutOff, long resolutionTime, String startSla, String endSla,
                        String now, String expectedSla, int year, int month, int day, boolean isWorkDay) {
        if (!isWorkDay) {
            when(calendarService.nextWorkDay(any())).thenReturn(LocalDate.of(year, month, day));
        }
        when(calendarService.isWorkDay(any())).thenReturn(isWorkDay);
        initRecalculateSla(onHoldPriority, cutOff, resolutionTime, startSla, endSla, parseInstant(now));
        Instant sla = slaExpressions.recalculateSla("caseId");
        assertEquals(parseInstant(expectedSla), sla);
    }

    @ParameterizedTest
    @CsvSource({
        "2022-05-16T11:15:30, 2022-05-16T14:45:55, 08:00, 16:30, 210",
        "2022-05-16T11:15:30, 2022-05-16T18:20:00, 08:00, 16:30, 314",
        "2022-05-16T18:32:00, 2022-05-17T07:55:55, 08:00, 16:30, 0",
        "2022-05-16T18:32:30, 2022-05-17T09:15:00, 08:00, 16:30, 75",
        "2022-05-16T11:30:30, 2022-05-17T11:30:29, 08:00, 16:30, 509"
    })
    void getMinutesOnHold(String startOnHold, String endOnHold, String startSla, String endSla, long expectedMinutes) {
        when(calendarService.isWorkDay(any())).thenReturn(true);
        initGetMinutesOnHold(Date.from(parseInstant(startOnHold)), Date.from(parseInstant(endOnHold)), startSla, endSla);
        long minutes = slaExpressions.getMinutesOnHold("caseId", "taskId");
        assertEquals(expectedMinutes, minutes);
    }
    @ParameterizedTest
    @CsvSource({
            "2022-05-16T15:30:30, 2022-05-18T09:15:00, 08:00, 16:30, 644, true",
            "2022-05-13T10:00:00, 2022-05-17T12:00:00, 08:00, 16:30, 1140, true"
    })
    void getMinutesOnHoldSeveralDaysWorkingDay(String startOnHold, String endOnHold, String startSla, String endSla, long expectedMinutes) {
        when(calendarService.isWorkDay(any())).thenReturn(true);
        when(calendarService.workDaysBetweenDates(any(), any())).thenReturn(1L);
        initGetMinutesOnHold(Date.from(parseInstant(startOnHold)), Date.from(parseInstant(endOnHold)), startSla, endSla);
        long minutes = slaExpressions.getMinutesOnHold("caseId", "taskId");
        assertEquals(expectedMinutes, minutes);
    }

    private Instant parseInstant(String date) {
        LocalDateTime ldt = LocalDateTime.parse(date);
        return ZonedDateTime.of(ldt, ZoneId.of("CET")).toInstant();
    }

    private void initCalculateSla(boolean priority, String cutOff, long resolutionTime, String startSla, String endSla, Instant currentTime) {
        Clock clock = mock(Clock.class);
        when(processEngineConfiguration.getClock()).thenReturn(clock);
        Date date = mock(Date.class);
        when(clock.getCurrentTime()).thenReturn(date);
        when(date.toInstant()).thenReturn(currentTime);

        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseVariables()).thenReturn(variablesCalculateSla(priority, cutOff, resolutionTime, startSla, endSla));
        CaseInstanceQuery query = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(query);
        when(query.caseInstanceId(anyString())).thenReturn(query);
        when(query.includeCaseVariables()).thenReturn(query);
        when(query.singleResult()).thenReturn(caseInstance);
    }

    private void initRecalculateSla(boolean onHoldPriority, String cutOff, long resolutionTime, String startSla, String endSla, Instant currentTime) {
        Clock clock = mock(Clock.class);
        when(processEngineConfiguration.getClock()).thenReturn(clock);
        Date date = mock(Date.class);
        when(clock.getCurrentTime()).thenReturn(date);
        when(date.toInstant()).thenReturn(currentTime);

        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseVariables()).thenReturn(variablesRecalculateSla(onHoldPriority, cutOff, resolutionTime, startSla, endSla));

        CaseInstanceQuery query = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(query);
        when(query.caseInstanceId(anyString())).thenReturn(query);
        when(query.includeCaseVariables()).thenReturn(query);
        when(query.singleResult()).thenReturn(caseInstance);
    }

    private void initGetMinutesOnHold(Date onHoldStart, Date onHoldEnd, String startSla, String endSla) {

        HistoricTaskInstance historicTaskInstance = mock(HistoricTaskInstance.class);
        when(historicTaskInstance.getCreateTime()).thenReturn(onHoldStart);
        when(historicTaskInstance.getEndTime()).thenReturn(onHoldEnd);

        HistoricTaskInstanceQuery historicTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(historicTaskInstanceQuery);
        when(historicTaskInstanceQuery.taskId(anyString())).thenReturn(historicTaskInstanceQuery);
        when(historicTaskInstanceQuery.singleResult()).thenReturn(historicTaskInstance);

        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseVariables()).thenReturn(variablesGetMinutesOnHold(startSla, endSla));

        CaseInstanceQuery query = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(query);
        when(query.caseInstanceId(anyString())).thenReturn(query);
        when(query.includeCaseVariables()).thenReturn(query);
        when(query.singleResult()).thenReturn(caseInstance);
    }

    private Map<String, Object> variablesCalculateSla(boolean priority, String cutOff, long resolutionTime, String startSla, String endSla) {
        return Map.of(
            CaseCommonConstants.SLA_PRIORITY, priority,
            CaseCommonConstants.SLA_RESOLUTION_TIME, (double) resolutionTime,
            CaseCommonConstants.SLA_START_TIME, startSla,
            CaseCommonConstants.SLA_END_TIME, endSla,
            CaseCommonConstants.SLA_CUTOFF, cutOff
        );
    }

    private Map<String, Object> variablesRecalculateSla(boolean onHoldAsPriority, String cutOff, long resolutionTime, String startSla, String endSla) {
        return Map.of(
            CaseCommonConstants.SLA_END_ONHOLD_AS_PRIORITY, onHoldAsPriority,
            CaseCommonConstants.SLA_RESOLUTION_TIME, (double) resolutionTime,
            CaseCommonConstants.SLA_START_TIME, startSla,
            CaseCommonConstants.SLA_END_TIME, endSla,
            CaseCommonConstants.SLA_CUTOFF, cutOff
        );
    }

    private Map<String, Object> variablesGetMinutesOnHold(String startSla, String endSla) {
        return Map.of(
            CaseCommonConstants.SLA_START_TIME, startSla,
            CaseCommonConstants.SLA_END_TIME, endSla
        );
    }

}