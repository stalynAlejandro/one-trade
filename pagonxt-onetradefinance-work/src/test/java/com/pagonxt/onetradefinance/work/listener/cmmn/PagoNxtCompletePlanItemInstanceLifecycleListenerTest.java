package com.pagonxt.onetradefinance.work.listener.cmmn;

import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class PagoNxtCompletePlanItemInstanceLifecycleListenerTest {
    @InjectMocks
    PagoNxtCompletePlanItemInstanceLifecycleListener pagoNxtCompletePlanItemInstanceLifecycleListener;
    @Mock
    LoggerWrapper loggerWrapper;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnRuntimeService cmmnRuntimeService;

    @Test
    void stateChanged_noFilterMatch_noLoggerCalled(){
        //given
        DelegatePlanItemInstance delegatePlanItemInstance = mock(DelegatePlanItemInstance.class);
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseDefinitionKey()).thenReturn("CASE_KEY");
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables().caseInstanceId(any()).singleResult())
                .thenReturn(caseInstance);
        //when
        pagoNxtCompletePlanItemInstanceLifecycleListener
                .stateChanged(delegatePlanItemInstance,"oldState","newState");
        //then
        verify(loggerWrapper,times(0)).businessLog(any(),any());
    }
    @Test
    void stateChanged_filterMatch_and_noOperationFound_throwsException() throws ObservabilityException {
        //given
        DelegatePlanItemInstance delegatePlanItemInstance = mock(DelegatePlanItemInstance.class);
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseDefinitionKey()).thenReturn("CLE_C001");
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables().caseInstanceId(any()).singleResult())
                .thenReturn(caseInstance);
        when(delegatePlanItemInstance.getCaseInstanceId()).thenReturn("caseId");
        when(delegatePlanItemInstance.getName()).thenReturn("planName");
        //when and then
        assertThrows(ObservabilityException.class,
                () -> pagoNxtCompletePlanItemInstanceLifecycleListener
                        .stateChanged(delegatePlanItemInstance,"oldState","newState"));
    }
    @Test
    void stateChanged_OperationFound_and_FilterMach_and_operationStatusIncorrect_throwsException()
            throws ObservabilityException{
        //given
        DelegatePlanItemInstance delegatePlanItemInstance = mock(DelegatePlanItemInstance.class);
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseDefinitionKey()).thenReturn("CLE_C001");
        Map<String, Object> caseVariablesMap = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operationMocked = mock(DataObjectInstanceVariableContainerImpl.class);
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put("code","caseCode");
        operationMap.put(FieldConstants.DISPLAYED_STATUS,"INVALID_STATUS");
        when(operationMocked.getData()).thenReturn(operationMap);
        caseVariablesMap.put("operation", operationMocked);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariablesMap);
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables().caseInstanceId(any()).singleResult())
                .thenReturn(caseInstance);
        when(delegatePlanItemInstance.getCaseInstanceId()).thenReturn("caseId");
        when(delegatePlanItemInstance.getName()).thenReturn("planName");
        //when and then
        assertThrows(ObservabilityException.class,
                () -> pagoNxtCompletePlanItemInstanceLifecycleListener
                        .stateChanged(delegatePlanItemInstance,"oldState","newState"));

    }
    @Test
    void stateChanged_OperationFound_and_FilterMach_and_operationStatusCorrect_OK(){
        //given
        DelegatePlanItemInstance delegatePlanItemInstance = mock(DelegatePlanItemInstance.class);
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseDefinitionKey()).thenReturn("CLE_C001");
        Map<String, Object> caseVariablesMap = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operationMocked = mock(DataObjectInstanceVariableContainerImpl.class);
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put("code","caseCode");
        operationMap.put(FieldConstants.DISPLAYED_STATUS,"IN_PROGRESS");
        when(operationMocked.getData()).thenReturn(operationMap);
        caseVariablesMap.put("operation", operationMocked);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariablesMap);
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables().caseInstanceId(any()).singleResult())
                .thenReturn(caseInstance);
        when(delegatePlanItemInstance.getCaseInstanceId()).thenReturn("caseId");
        when(delegatePlanItemInstance.getName()).thenReturn("planName");
        //when
        pagoNxtCompletePlanItemInstanceLifecycleListener
                .stateChanged(delegatePlanItemInstance,"oldState","newState");
        //then
        verify(loggerWrapper,times(1)).businessLog(any(),any());

    }
}
