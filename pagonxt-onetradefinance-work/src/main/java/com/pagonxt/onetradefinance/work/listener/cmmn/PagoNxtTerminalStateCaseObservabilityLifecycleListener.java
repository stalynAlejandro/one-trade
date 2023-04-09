package com.pagonxt.onetradefinance.work.listener.cmmn;

import com.pagonxt.onetradefinance.integrations.model.BusinessLogDto;
import com.pagonxt.onetradefinance.integrations.model.BusinessLogOperationStatus;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import com.pagonxt.onetradefinance.work.expression.common.PagoNxtBusinessLogExpressions;
import org.flowable.cmmn.api.listener.CaseInstanceLifecycleListener;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstanceState;

public class PagoNxtTerminalStateCaseObservabilityLifecycleListener
        implements CaseInstanceLifecycleListener {

    private final LoggerWrapper loggerWrapper;

    public PagoNxtTerminalStateCaseObservabilityLifecycleListener(LoggerWrapper loggerWrapper) {

        this.loggerWrapper = loggerWrapper;
    }

    @Override
    public String getSourceState() {
        return null;
    }

    @Override
    public String getTargetState() {
        return null;
    }

    @Override
    public void stateChanged(CaseInstance caseInstance, String s, String s1) {
        //Execute only if it is a case of Export Collection
        if (!caseInstance.getCaseDefinitionKey().toUpperCase().startsWith("CLE")) {
            return;
        }
        // Execute only when target state is distint of 'active'
        // because when the case become 'active', variable operation does not exist
        // and this variable is mandatory for observability
        if (CaseInstanceState.ACTIVE.equals(s1)) {
            return;
        }
        BusinessLogDto businessLogDto = new BusinessLogDto();
        BusinessLogOperationStatus status = CaseInstanceState
                .END_STATES.contains(s1) ? BusinessLogOperationStatus.END :
                BusinessLogOperationStatus.START;
        PagoNxtBusinessLogExpressions.initializeBusinessLogDto(businessLogDto,caseInstance,s1);
        businessLogDto.setOperationStatus(status.observabilityLabel);
        //Send business log to observability
        loggerWrapper.businessLog("info", businessLogDto);
    }
}
