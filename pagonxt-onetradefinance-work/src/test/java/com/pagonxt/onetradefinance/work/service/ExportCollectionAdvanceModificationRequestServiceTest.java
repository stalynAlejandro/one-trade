package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper;
import com.pagonxt.onetradefinance.work.utils.ExportCollectionUtils;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import com.pagonxt.onetradefinance.work.validation.ExportCollectionValidation;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.history.HistoricCaseInstance;
import org.flowable.cmmn.api.history.HistoricCaseInstanceQuery;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstanceQuery;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionAdvanceModificationRequestServiceTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    CmmnRuntimeService cmmnRuntimeService;
    @Mock(answer = RETURNS_DEEP_STUBS)
    DataObjectRuntimeService dataObjectRuntimeService;
    @Mock
    PagoNxtRequestUtils pagoNxtRequestUtils;
    @Mock
    ExportCollectionUtils exportCollectionUtils;
    @Mock
    TaskUtils taskUtils;
    @Mock
    ExportCollectionMapper exportCollectionMapper;
    @Mock
    ExportCollectionValidation exportCollectionValidation;
    @Mock
    CaseSecurityService caseSecurityService;
    @Mock
    CmmnHistoryService cmmnHistoryService;

    @InjectMocks
    ExportCollectionAdvanceModificationRequestService exportCollectionModificationRequestService;

    @Test
    void createExportCollectionAdvanceModificationRequest_ok_returnsValidData() {
        // Given
        ExportCollectionAdvanceModificationRequest request = getRequest();
        CaseInstance caseInstance = mock(CaseInstance.class, RETURNS_DEEP_STUBS);
        when(caseInstance.getId()).thenReturn("CLE-ADV-MOD-001");
        when(caseInstance.getCaseVariables().get("product")).thenReturn("CLE-ADV-MOD");
        when(caseInstance.getCaseVariables().get("event")).thenReturn("ADVANCE_MODIFICATION");
        when(caseInstance.getCaseVariables().get("exportCollectionAdvanceModificationCaseSeq")).thenReturn("001");
        when(cmmnRuntimeService.createCaseInstanceBuilder().caseDefinitionKey("CLE_C004").start()).thenReturn(caseInstance);
        String expectedCode = "001";
        String expectedProduct = "CLE-ADV-MOD";
        String expectedEvent = "ADVANCE_MODIFICATION";

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class, RETURNS_DEEP_STUBS);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey("CLE_DO006")).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainerMock = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenReturn(dataObjectInstanceVariableContainerMock);

        // When
        ExportCollectionAdvanceModificationRequest result = exportCollectionModificationRequestService.createExportCollectionAdvanceModificationRequest(request);

        // Then
        verify(pagoNxtRequestUtils, times(1)).updateDraftDocumentsOnCaseInstance(request, caseInstance);
        verify(pagoNxtRequestUtils, times(1)).setOperationDocumentsFromRequestDocuments("CLE-ADV-MOD-001", "userType");
        verify(exportCollectionValidation).validateConfirm(request);

        assertEquals(expectedCode, result.getCode(), "Result should match pattern");
        assertEquals(expectedProduct, result.getProduct(), "Product should match pattern");
        assertEquals(expectedEvent, result.getEvent(), "Event should match pattern");
    }
    @Test
    void ExportCollectionAdvanceModificationRequest_generateVariableOperation_throwsInvalidRequestException() {
        // Given
        ExportCollectionAdvanceModificationRequest request = mock(ExportCollectionAdvanceModificationRequest.class, RETURNS_DEEP_STUBS);

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey(any()).operation(any())).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionModificationRequestService.createExportCollectionAdvanceModificationRequest(request),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for create a operation in flow with code null is missing", exception.getMessage(), "Expected exception message when request has missing values");

    }
    @Test
    void getCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("operationCode", "CLE-ADV-MOD-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        TaskRepresentation taskRepresentation = new TaskRepresentation();
        when(taskUtils.getTask("TSK-123", false)).thenReturn(taskRepresentation);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C004")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceModificationCaseSeq", "CLE-ADV-MOD-123")).thenReturn(caseInstanceQuery);
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionModificationRequestService.getCompleteInfoExportCollectionAdvanceModificationRequest("TSK-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollectionAdvance(any());
        verify(caseSecurityService).checkReadTask(any(), eq("TSK-123"));
        verify(pagoNxtRequestUtils).mapCompleteInfoPagoNxtRequest(any(), eq(caseVariables), eq(taskRepresentation), eq(taskVariables));
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceModificationRequest_whenErrorMapping_thenThrowResourceNotFoundException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("returnReason", "returnReason1");
        taskVariables.put("returnComment", "returnComment11");
        taskVariables.put("operationCode", "CLE-ADV-MOD-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C004")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceModificationCaseSeq", "CLE-ADV-MOD-123")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.singleResult()).thenReturn(null);
        UserInfo userInfo = new UserInfo();
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionModificationRequestService.getCompleteInfoExportCollectionAdvanceModificationRequest("TSK-123", userInfo));
        // Then
        assertEquals("No case found with code CLE-ADV-MOD-123", exception.getMessage());
        assertEquals("errorCaseNotFound", exception.getKey());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceModificationRequest_ok_thenInvokeCompleteTaskCompleteInfo() {
        // Given
        ExportCollectionAdvanceModificationRequest exportCollectionModificationRequest = mockRequest();
        // When
        exportCollectionModificationRequestService.completeCompleteInfoExportCollectionAdvanceModificationRequest(exportCollectionModificationRequest, "TSK-123");
        // Then
        verify(taskUtils).setCompleteInfoTaskVariables(eq("TSK-123"), any(), any());
        verify(exportCollectionUtils).findDataObjectByIdAndModel(any(), eq("CLE_DO005"));
        verify(caseSecurityService).checkEditTask(any(), eq("TSK-123"));
    }

    @Test
    void getPetitionRequestDetails_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        when(cmmnHistoryService.createHistoricCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C004")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceModificationCaseSeq", "CLE-ADV-MOD-123")).thenReturn(caseInstanceQuery);
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionModificationRequestService.getPetitionRequestDetails("CLE-ADV-MOD-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollectionAdvance(any());
        verify(caseSecurityService).checkRead(userInfo, "CLE-ADV-MOD-123");
        verify(pagoNxtRequestUtils).mapDetailedPagoNxtRequest(any(), eq(caseVariables));
    }

    private ExportCollectionAdvanceModificationRequest getRequest() {
        ExportCollectionAdvanceModificationRequest result = new ExportCollectionAdvanceModificationRequest();
        result.setRequester(new UserInfo(new User("userId", "userDisplayName", "userType")));
        result.setCustomer(new Customer("customerId", "ES", null, "taxId", null, "personNumber", "segment", null));
        result.setExportCollectionAdvance(new ExportCollectionAdvance("code", null, null, null, null, 12.34D, "eur", null, new Date()));
        result.setRiskLine(new RiskLine("riskLineId"));
        when(exportCollectionUtils.findDataObjectByIdAndModel(any(), eq("CLE_DO005"))).thenReturn(null);
        return result;
    }

    private ExportCollectionAdvanceModificationRequest mockRequest() {
        ExportCollectionAdvanceModificationRequest request = mock(ExportCollectionAdvanceModificationRequest.class);
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        doReturn(userInfo).when(request).getRequester();
        Customer customer = mock(Customer.class);
        doReturn(customer).when(request).getCustomer();
        ExportCollectionAdvance exportCollectionAdvance = mock(ExportCollectionAdvance.class);
        doReturn(exportCollectionAdvance).when(request).getExportCollectionAdvance();
        RiskLine riskLine = mock(RiskLine.class);
        doReturn(riskLine).when(request).getRiskLine();
        return request;
    }
}
