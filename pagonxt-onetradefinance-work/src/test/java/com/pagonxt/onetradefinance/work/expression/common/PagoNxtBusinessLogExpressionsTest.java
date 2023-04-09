package com.pagonxt.onetradefinance.work.expression.common;

import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.BusinessLogDto;
import com.pagonxt.onetradefinance.integrations.model.BusinessLogOperationStatus;
import com.pagonxt.onetradefinance.integrations.model.exception.ObservabilityException;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class PagoNxtBusinessLogExpressionsTest {
    @InjectMocks
    private PagoNxtBusinessLogExpressions pagoNxtBusinessLogExpressions;
    @Mock
    private LoggerWrapper loggerWrapper;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private CmmnRuntimeService cmmnRuntimeService;
    @Test
    void addBusinessLogByPlanInstance_ok(){
        //given
        DelegatePlanItemInstance delegatePlanItemInstance = mock(DelegatePlanItemInstance.class);
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getId()).thenReturn("caseId");
        Map<String, Object> caseVariablesMap = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operationMocked = mock(DataObjectInstanceVariableContainerImpl.class);
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put("code","caseCode");
        operationMap.put(FieldConstants.DISPLAYED_STATUS,"IN_PROGRESS");
        when(operationMocked.getData()).thenReturn(operationMap);
        caseVariablesMap.put("operation", operationMocked);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariablesMap);
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables().caseInstanceId(any())
                .singleResult()).thenReturn(caseInstance);
        //when
        pagoNxtBusinessLogExpressions.addBusinessLogByPlanInstance(delegatePlanItemInstance);
        //then
        verify(loggerWrapper, times(1)).businessLog(any(), any());
    }
    @Test
    void initializeBusinessLogDto_Ok(){
        //given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getId()).thenReturn("caseId");
        Map<String, Object> caseVariablesMap = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operationMocked = mock(DataObjectInstanceVariableContainerImpl.class);
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put("code","caseCode");
        operationMap.put(FieldConstants.DISPLAYED_STATUS,"IN_PROGRESS");
        when(operationMocked.getData()).thenReturn(operationMap);
        caseVariablesMap.put("operation", operationMocked);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariablesMap);
        BusinessLogDto businessLogDto = new BusinessLogDto();
        //when
        PagoNxtBusinessLogExpressions.initializeBusinessLogDto(businessLogDto, caseInstance,"stepName");
        //then
        assertEquals(BusinessLogOperationStatus.IN_PROGRESS.observabilityLabel,businessLogDto.getOperationStatus());
        assertEquals("stepName", businessLogDto.getStepName());

    }
    @Test
    void initializeBusinessLogDto_noOperationExist() throws ObservabilityException {
        //given
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariablesMap = new HashMap<>();
        when(caseInstance.getCaseVariables()).thenReturn(caseVariablesMap);
        BusinessLogDto businessLogDto = new BusinessLogDto();
        //when and then
        Exception exception = assertThrows(ObservabilityException.class,
                () ->  PagoNxtBusinessLogExpressions
                        .initializeBusinessLogDto(businessLogDto, caseInstance,"stepName"));
        assert(exception.getMessage()).equals("Case variable 'operation' is mandatory for observability");
    }
    @Test
    void initializeBusinessLogDto_incorrectOperationStatus() throws ObservabilityException {
        //given
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariablesMap = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operationMocked = mock(DataObjectInstanceVariableContainerImpl.class);
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put("code","caseCode");
        operationMap.put(FieldConstants.DISPLAYED_STATUS,"INCORRECT_STATUS");
        when(operationMocked.getData()).thenReturn(operationMap);
        caseVariablesMap.put("operation", operationMocked);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariablesMap);
        BusinessLogDto businessLogDto = new BusinessLogDto();
        //when and then
        Exception exception = assertThrows(ObservabilityException.class,
                () ->  PagoNxtBusinessLogExpressions
                        .initializeBusinessLogDto(businessLogDto, caseInstance,"stepName"));
       assert(exception.getMessage())
                .equals("Invalid operation displayed status");
    }
}
