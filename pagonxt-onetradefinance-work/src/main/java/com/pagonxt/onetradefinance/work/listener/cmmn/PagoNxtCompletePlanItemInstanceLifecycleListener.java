package com.pagonxt.onetradefinance.work.listener.cmmn;

import com.pagonxt.onetradefinance.integrations.model.BusinessLogDto;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import com.pagonxt.onetradefinance.work.expression.common.PagoNxtBusinessLogExpressions;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.listener.PlanItemInstanceLifecycleListener;
import org.flowable.cmmn.api.runtime.CaseInstance;

import java.util.Locale;

public class PagoNxtCompletePlanItemInstanceLifecycleListener implements PlanItemInstanceLifecycleListener {
    private final LoggerWrapper loggerWrapper;
    private final CmmnRuntimeService cmmnRuntimeService;

    public PagoNxtCompletePlanItemInstanceLifecycleListener(LoggerWrapper loggerWrapper,
                                                            CmmnRuntimeService cmmnRuntimeService) {
        this.loggerWrapper = loggerWrapper;
        this.cmmnRuntimeService = cmmnRuntimeService;
    }

    @Override
    public String getSourceState() {
        return null;
    }

    @Override
    public String getTargetState() {
        return "completed";
    }

    @Override
    public void stateChanged(DelegatePlanItemInstance delegatePlanItemInstance, String oldState, String newState) {
        //For retrieve case variables and case definition key case id is needed
        String caseInstanceId = delegatePlanItemInstance.getCaseInstanceId();
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables()
                .caseInstanceId(caseInstanceId).singleResult();
        String caseDefinitionKey = caseInstance.getCaseDefinitionKey();
        //IMPORTANT: Filter by case definition key
        if (!caseDefinitionKey.toUpperCase(Locale.ENGLISH).startsWith("CLE")) {
            return;
        }
        BusinessLogDto businessLogDto = new BusinessLogDto();
        PagoNxtBusinessLogExpressions
                .initializeBusinessLogDto(businessLogDto,caseInstance,delegatePlanItemInstance.getName());
        loggerWrapper.businessLog("info", businessLogDto);
    }
}

