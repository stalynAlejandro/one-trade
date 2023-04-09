package com.pagonxt.onetradefinance.work.listener.cmmn;

import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.exception.ObservabilityException;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class PagoNxtTerminalStateCaseObservabilityLifecycleListenerTest {
    @Mock
    LoggerWrapper loggerWrapper;
    @InjectMocks
    PagoNxtTerminalStateCaseObservabilityLifecycleListener pagoNxtTerminalStateCaseObservabilityLifecycleListener;


    @Test
    void stateChanged_noFilterMatch_noLoggerCalled() {
        //given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseDefinitionKey()).thenReturn("CASE_KEY");
        pagoNxtTerminalStateCaseObservabilityLifecycleListener.stateChanged(caseInstance, "oldStage", "newStage");
        //then
        verify(loggerWrapper, times(0)).businessLog(any(), any());
    }

    @Test
    void stateChanged_filterMatch_and_noOperationFound_throwsException() throws ObservabilityException {
        //given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseDefinitionKey()).thenReturn("CLE_C001");
        //when and then
        assertThrows(ObservabilityException.class,
                () -> pagoNxtTerminalStateCaseObservabilityLifecycleListener
                        .stateChanged(caseInstance, "oldState", "newState"));
    }

    @Test
    void stateChanged_OperationFound_and_FilterMach_and_operationStatusIncorrect_throwsException()
            throws ObservabilityException {
        //given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(caseInstance.getCaseDefinitionKey()).thenReturn("CLE_C001");
        Map<String, Object> caseVariablesMap = new HashMap<>();
        DataObjectInstanceVariableContainerImpl operationMocked = mock(DataObjectInstanceVariableContainerImpl.class);
        Map<String, Object> operationMap = new HashMap<>();
        operationMap.put("code", "caseCode");
        operationMap.put(FieldConstants.DISPLAYED_STATUS, "INVALID_STATUS");
        when(operationMocked.getData()).thenReturn(operationMap);
        caseVariablesMap.put("operation", operationMocked);
        when(caseInstance.getCaseVariables()).thenReturn(caseVariablesMap);
        //when and then
        assertThrows(ObservabilityException.class,
                () -> pagoNxtTerminalStateCaseObservabilityLifecycleListener
                        .stateChanged(caseInstance, "oldState", "newState"));
    }
    @Test
    void stateChanged_OperationFound_and_FilterMach_and_operationStatusCorrect_OK(){
        //given
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
        //when
        pagoNxtTerminalStateCaseObservabilityLifecycleListener.stateChanged(caseInstance,"oldState","newState");
        //then
        verify(loggerWrapper,times(1)).businessLog(any(),any());

    }
}
