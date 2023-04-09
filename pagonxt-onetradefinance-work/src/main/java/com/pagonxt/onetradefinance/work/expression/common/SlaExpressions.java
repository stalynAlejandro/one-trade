package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.integrations.service.CalendarService;
import com.pagonxt.onetradefinance.work.common.CaseCommonConstants;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.engine.HistoryService;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

/**
 * class for SLA expressions
 * @author -
 * @version jdk-11.0.13
 * @see org.flowable.engine.ProcessEngineConfiguration
 * @see org.flowable.cmmn.api.CmmnRuntimeService
 * @see org.flowable.engine.HistoryService
 * @see com.pagonxt.onetradefinance.integrations.service.CalendarService
 * @since jdk-11.0.13
 */
@Component
public class SlaExpressions {

    //TODO: Move to ConfigurationProperties similar to IntegrationCalendarProperties
    @Value("${one-trade.sla.timezone:CET}")
    private String timezone = "CET";

    //Class attributes
    private final ProcessEngineConfiguration processEngineConfiguration;
    private final CmmnRuntimeService cmmnRuntimeService;
    private final HistoryService historyService;
    private final CalendarService calendarService;

    /**
     * Constructor method
     * @param processEngineConfiguration : a ProcessEngineConfiguration object
     * @param cmmnRuntimeService         : a CmmnRuntimeService object
     * @param historyService             : a HistoryService object
     * @param calendarService            : a CalendarService object
     */
    public SlaExpressions(ProcessEngineConfiguration processEngineConfiguration,
                          CmmnRuntimeService cmmnRuntimeService,
                          HistoryService historyService,
                          CalendarService calendarService) {
        this.processEngineConfiguration = processEngineConfiguration;
        this.cmmnRuntimeService = cmmnRuntimeService;
        this.historyService = historyService;
        this.calendarService = calendarService;
    }

    /**
     * Method to calculate SLA
     * @param caseId : a string with the case id
     * @see java.time.Instant
     * @return an Instant object with the SLA
     */
    public Instant calculateSla(String caseId) {
        CaseInstance caseInstance = getCaseInstanceWithVariables(caseId);
        Map<String, Object> caseVariables = caseInstance.getCaseVariables();
        Instant now = now();

        Instant sla;

        String startSlaTime = (String) caseVariables.get(CaseCommonConstants.SLA_START_TIME);
        boolean isPriority = (Boolean) caseVariables.get(CaseCommonConstants.SLA_PRIORITY);
        String cutOff = (String) caseVariables.get(CaseCommonConstants.SLA_CUTOFF);

        if (isPriority) {
            Double resolutionTimeDouble = (Double) caseVariables.get(CaseCommonConstants.SLA_RESOLUTION_TIME);
            long resolutionTime = resolutionTimeDouble.longValue();
            sla = calculatePrioritySla(now, cutOff, resolutionTime, startSlaTime);
        } else {
            String endSlaTime = (String) caseVariables.get(CaseCommonConstants.SLA_END_TIME);
            sla = calculateNormalSla(now, cutOff, startSlaTime, endSlaTime);
        }
        return sla;
    }

    /**
     * Method to calculate normal SLA
     * @param now : an Instant object
     * @param cutOff : a string with the cutOff
     * @param startSlaTime: a string with the start time of the SLA
     * @param endSlaTime : a string with the end time of the SLA
     * @see java.time.Instant
     * @return an Instant object with the SLA
     */
    private Instant calculateNormalSla(Instant now, String cutOff, String startSlaTime, String endSlaTime) {
        Instant sla;

        Instant initialTime = now;
        if (!calendarService.isWorkDay(LocalDate.ofInstant(now, ZoneId.of(timezone)))) {
            initialTime = OffsetDateTime
                .ofInstant(now, ZoneId.of(timezone))
                .with(calendarService.nextWorkDay(LocalDate.ofInstant(now, ZoneId.of(timezone))))
                .with(LocalTime.parse(startSlaTime))
                .toInstant();
        }

        Instant todayCutOffTime = cutOffTime(now, cutOff);

        if (initialTime.isBefore(todayCutOffTime)) {
            sla = OffsetDateTime
                .ofInstant(initialTime, ZoneId.of(timezone))
                .with(LocalTime.parse(endSlaTime))
                .toInstant();
        } else {
            sla = OffsetDateTime
                .ofInstant(initialTime, ZoneId.of(timezone))
                .plusDays(1)
                .with(LocalTime.parse(endSlaTime))
                .toInstant();
        }
        return sla;
    }

    /**
     * Method to calculate the priority of SLA
     * @param now : an Instant object
     * @param cutOffTime : a string with the cutOff time
     * @param resolutionTime: a long value with the resolution time
     * @param startSlaTime : a string with the start time of the SLA
     * @see java.time.Instant
     * @return an Instant object with the SLA
     */
    private Instant calculatePrioritySla(Instant now, String cutOffTime, Long resolutionTime, String startSlaTime) {
        Instant sla;

        Instant initialTime = now;
        if (!calendarService.isWorkDay(LocalDate.ofInstant(now, ZoneId.of(timezone)))) {
            initialTime = OffsetDateTime
                .ofInstant(now, ZoneId.of(timezone))
                .with(calendarService.nextWorkDay(LocalDate.ofInstant(now, ZoneId.of(timezone))))
                .with(LocalTime.parse(startSlaTime))
                .toInstant();
        }

        Instant todayCutOffTime = cutOffTime(initialTime, cutOffTime);

        if (initialTime.isBefore(todayCutOffTime)) {
            sla = initialTime.plusSeconds(resolutionTime * 60);
        } else {
            sla = OffsetDateTime
                .ofInstant(initialTime, ZoneId.of(timezone))
                .plusDays(1)
                .with(LocalTime.parse(startSlaTime))
                .plusMinutes(resolutionTime)
                .toInstant();
        }
        return sla;
    }

    /**
     * Method to get the cutOff time
     * @param now       : an Instant object
     * @param cutOff    : a string with the cutOff
     * @return an Instant object with the cutOff time
     */
    private Instant cutOffTime(Instant now, String cutOff) {
        return OffsetDateTime
            .ofInstant(now, ZoneId.of(timezone))
            .with(LocalTime.parse(cutOff))
            .toInstant();
    }

    /**
     * Method to get An instantaneous point on the time-line
     * @return an Instant object
     */
    private Instant now() {
        return processEngineConfiguration.getClock().getCurrentTime().toInstant();
    }

    private CaseInstance getCaseInstanceWithVariables(String caseId) {
        return cmmnRuntimeService.createCaseInstanceQuery()
            .caseInstanceId(caseId)
            .includeCaseVariables()
            .singleResult();
    }

    /**
     * Method to set the start on hold
     * parser expressions
     * @param caseId
     */
    public void setStartOnHold(String caseId) {
        cmmnRuntimeService.setVariable(caseId, "onHoldStart", new Date());
        cmmnRuntimeService.removeVariable(caseId, "onHoldEnd");
    }

    /**
     * Class method to update the minutes on hold
     * @param caseId : a string with case id
     */
    public void updateMinutesOnHold(String caseId) {
        Date onHoldEnd = new Date();
        cmmnRuntimeService.setVariable(caseId, "onHoldEnd", onHoldEnd);

        Date onHoldStart = (Date) cmmnRuntimeService.getVariable(caseId, "onHoldStart");
        Long minutesOnnHold = (Long) cmmnRuntimeService.getVariable(caseId, "minutesOnnHold");
        minutesOnnHold = minutesOnnHold == null ? 0 : minutesOnnHold;

        long additionalMinutes = calculateMinutesOnHold(
            onHoldStart.toInstant().atZone(ZoneId.of(timezone)),
            onHoldEnd.toInstant().atZone(ZoneId.of(timezone)),
            (String) cmmnRuntimeService.getVariable(caseId, CaseCommonConstants.SLA_START_TIME),
            (String) cmmnRuntimeService.getVariable(caseId, CaseCommonConstants.SLA_END_TIME)
        );

        cmmnRuntimeService.setVariable(caseId, "minutesOnHold", minutesOnnHold + additionalMinutes);
        cmmnRuntimeService.setVariable(caseId, CaseCommonConstants.SLA_END, recalculateSla(caseId));
    }


    /**
     * Method to recalculate SLA
     * @param caseId : a string with the case id
     * @return : an Instant object with the SLA
     */
    Instant recalculateSla(String caseId) {
        CaseInstance caseInstance = getCaseInstanceWithVariables(caseId);
        Map<String, Object> caseVariables = caseInstance.getCaseVariables();
        Instant now = now();

        Instant sla;

        // Since it's after on hold, consider priority based on onHold property
        String startSlaTime = (String) caseVariables.get(CaseCommonConstants.SLA_START_TIME);
        boolean isPriorityOnHold = (Boolean) caseVariables.get(CaseCommonConstants.SLA_END_ONHOLD_AS_PRIORITY);
        String cutOff = (String) caseVariables.get(CaseCommonConstants.SLA_CUTOFF);

        if (isPriorityOnHold) {
            Double resolutionTimeDouble = (Double) caseVariables.get(CaseCommonConstants.SLA_RESOLUTION_TIME);
            long resolutionTime = resolutionTimeDouble.longValue();
            sla = calculatePrioritySla(now, cutOff, resolutionTime, startSlaTime);
        } else {
            String endSlaTime = (String) caseVariables.get(CaseCommonConstants.SLA_END_TIME);
            sla = calculateNormalSla(now, cutOff, startSlaTime, endSlaTime);
        }
        return sla;
    }

    /**
     * Class to get the minutes on hold
     * @param caseId        : a string with the case id
     * @param onHoldTaskId  : a string with task id on hold
     * @return a long value minutes
     */
    long getMinutesOnHold(String caseId, String onHoldTaskId) {
        HistoricTaskInstance historicTask = historyService
                .createHistoricTaskInstanceQuery().taskId(onHoldTaskId).singleResult();

        Date createTime = historicTask.getCreateTime();
        Date endTime = historicTask.getEndTime();

        CaseInstance caseInstance = getCaseInstanceWithVariables(caseId);
        Map<String, Object> caseVariables = caseInstance.getCaseVariables();

        String startSlaTime = (String) caseVariables.get(CaseCommonConstants.SLA_START_TIME);
        String endSlaTime = (String) caseVariables.get(CaseCommonConstants.SLA_END_TIME);

        return calculateMinutesOnHold(
            createTime.toInstant().atZone(ZoneId.of(timezone)),
            endTime.toInstant().atZone(ZoneId.of(timezone)),
            startSlaTime, endSlaTime);
    }

    /**
     * Method to calculate the minutes on hold
     * @param onHoldStartDate : a ZonedDateTime object with the start date of the on hold
     * @param onHoldEndDate   : a ZonedDateTime object with the end date of the on hold
     * @param startSlaTime    : a string with start time of sla
     * @param endSlaTime      : a string with end time of sla
     * @see java.time.ZonedDateTime
     * @return a long value with the minutes
     */
    private long calculateMinutesOnHold(ZonedDateTime onHoldStartDate,
                                        ZonedDateTime onHoldEndDate, String startSlaTime, String endSlaTime) {
        ZonedDateTime endOfFirstDay = onHoldStartDate.with(LocalTime.MAX);
        ZonedDateTime startOfSecondDay = onHoldStartDate.with(LocalTime.MIN).plusDays(1);
        ZonedDateTime startOfLastDay = onHoldEndDate.with(LocalTime.MIN);

        if (startOfSecondDay.isAfter(onHoldEndDate)) {
            return minutesOnHoldSameDay(onHoldStartDate, onHoldEndDate, startSlaTime, endSlaTime);
        }

        return minutesOnHoldSameDay(onHoldStartDate, endOfFirstDay, startSlaTime, endSlaTime) +
            minutesOnHoldMultipleDays(startOfSecondDay, startOfLastDay, startSlaTime, endSlaTime) +
            minutesOnHoldSameDay(startOfLastDay, onHoldEndDate, startSlaTime, endSlaTime);
    }

    /**
     * Method to calculate the minutes on hold on the same day
     * @param startTime     : a ZonedDateTime object with the start time
     * @param endTime       : a ZonedDateTime object with the end time
     * @param startSlaTime  : a string with start time of sla
     * @param endSlaTime     : a string with end time of sla
     * @see java.time.ZonedDateTime
     * @return a long value with the minutes
     */
    private long minutesOnHoldSameDay(ZonedDateTime startTime,
                                      ZonedDateTime endTime, String startSlaTime, String endSlaTime) {
        ZonedDateTime startSla = startTime.with(LocalTime.parse(startSlaTime));
        ZonedDateTime endSla = endTime.with(LocalTime.parse(endSlaTime));

        if (calendarService.isWorkDay(startSla.toLocalDate())) {
            if (startSla.isBefore(startTime)) {
                if (endTime.isBefore(endSla)) {
                    return Math.max(ChronoUnit.MINUTES.between(startTime, endTime), 0);
                }
                return Math.max(ChronoUnit.MINUTES.between(startTime, endSla), 0);
            } else {
                if (endTime.isBefore(endSla)) {
                    return Math.max(ChronoUnit.MINUTES.between(startSla, endTime), 0);
                }
                return Math.max(ChronoUnit.MINUTES.between(startSla, endSla), 0);
            }
        }

        return 0;
    }

    /**
     * Method to calculate the minutes on hold on the multiple days
     * @param startTime     : a ZonedDateTime object with the start time
     * @param endTime       : a ZonedDateTime object with the end time
     * @param startSlaTime  : a string with start time of sla
     * @param endSlaTime     : a string with end time of sla
     * @see java.time.ZonedDateTime
     * @return a long value with the minutes
     */
    private long minutesOnHoldMultipleDays(ZonedDateTime startTime,
                                           ZonedDateTime endTime, String startSlaTime, String endSlaTime) {
        long numberOfDays = calendarService.workDaysBetweenDates(startTime.toLocalDate(), endTime.toLocalDate());
        return numberOfDays * ChronoUnit.MINUTES.between(LocalTime.parse(startSlaTime), LocalTime.parse(endSlaTime));
    }
}
