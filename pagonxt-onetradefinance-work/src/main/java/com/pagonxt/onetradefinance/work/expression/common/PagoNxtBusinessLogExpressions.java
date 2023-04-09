package com.pagonxt.onetradefinance.work.expression.common;

import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.BusinessLogDto;
import com.pagonxt.onetradefinance.integrations.model.BusinessLogOperationStatus;
import com.pagonxt.onetradefinance.integrations.model.exception.ObservabilityException;
import com.pagonxt.onetradefinance.logger.LoggerWrapper;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * service class for Observability logs
 *
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.dataobject.engine.DataObjectEngine
 * @since jdk-11.0.13
 */
@Service
public class PagoNxtBusinessLogExpressions {
    private final LoggerWrapper loggerWrapper;
    private final CmmnRuntimeService cmmnRuntimeService;

    public PagoNxtBusinessLogExpressions(LoggerWrapper loggerWrapper, CmmnRuntimeService cmmnRuntimeService) {
        this.loggerWrapper = loggerWrapper;
        this.cmmnRuntimeService = cmmnRuntimeService;
    }

    /**
     * Expression for add a business log in Observability system
     *
     * @param planItemInstance a {@link DelegatePlanItemInstance}
     */
    public void addBusinessLogByPlanInstance(DelegatePlanItemInstance planItemInstance) {
        String caseInstanceId = planItemInstance.getCaseInstanceId();
        CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceQuery()
                .includeCaseVariables().caseInstanceId(caseInstanceId).singleResult();
        BusinessLogDto businessLogDto = new BusinessLogDto();
        initializeBusinessLogDto(businessLogDto, caseInstance, planItemInstance.getName() );
        loggerWrapper.businessLog("info", businessLogDto);

    }

    /**
     * @param businessLogDto : a {@link BusinessLogDto} object
     * @param caseInstance   : a CaseIntance object
     * @param stepName       : a String with the step name
     */

    public static void initializeBusinessLogDto(BusinessLogDto businessLogDto,
                                                CaseInstance caseInstance,
                                                String stepName) {
        Map<String, Object> caseVariablesMap = caseInstance.getCaseVariables();
        DataObjectInstanceVariableContainerImpl operation = (DataObjectInstanceVariableContainerImpl) caseVariablesMap
                .get(FieldConstants.OPERATION);
        //Operation is mandatory for observability
        if (operation == null || operation.getData().isEmpty()) {
            throw new ObservabilityException("Case variable 'operation' is mandatory for observability");
        }
        String displayedStatus = (String) operation.getData().get(FieldConstants.DISPLAYED_STATUS);
        if (displayedStatus == null ){
            throw new ObservabilityException("Operation without displayed status");
        }
        BusinessLogOperationStatus operationStatusEnum = BusinessLogOperationStatus
                .valueOfOperationDisplayedStatus(displayedStatus);
        if (operationStatusEnum == null){
            throw new ObservabilityException("Invalid operation displayed status");
        }
        //Mandatory field for observability
        businessLogDto.setStepName(stepName);
        //custom field for observability
        businessLogDto.setCaseId(caseInstance.getId());
        //custom field for observability
        businessLogDto.setCaseCode((String) operation.getData().get("code"));
        //custom field for observability
        businessLogDto.setDetails(operation.getData());
        //Mandatory field for observability. The product in observability must be the name of the application
        businessLogDto.setProduct("Trade");
        //Mandatory field for observability.
        // The subProduct in observability must be the type of product (CLE, CLI, ...)
        businessLogDto.setSubproduct((String) caseVariablesMap.get(FieldConstants.PRODUCT));
        //custom field for observability
        businessLogDto.setEvent((String) caseVariablesMap.get(FieldConstants.EVENT));
        //Mandatory field for observability
        //Mandatory field for observability
        businessLogDto.setOperationStatus(operationStatusEnum.observabilityLabel);
    }
}
