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
class ExportCollectionAdvanceCancellationRequestServiceTest {

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
    ExportCollectionAdvanceCancellationRequestService exportCollectionCancellationRequestService;

    @Test
    void createExportCollectionAdvanceCancellationRequest_ok_returnsValidData() {
        // Given
        ExportCollectionAdvanceCancellationRequest request = getRequest();
        CaseInstance caseInstance = mock(CaseInstance.class, RETURNS_DEEP_STUBS);
        when(caseInstance.getId()).thenReturn("CLE-ADV-CAN-001");
        when(caseInstance.getCaseVariables().get("product")).thenReturn("CLE-ADV-CAN");
        when(caseInstance.getCaseVariables().get("event")).thenReturn("ADVANCE_CANCELLATION");
        when(caseInstance.getCaseVariables().get("exportCollectionAdvanceCancellationCaseSeq")).thenReturn("001");
        when(cmmnRuntimeService.createCaseInstanceBuilder().caseDefinitionKey("CLE_C005").start()).thenReturn(caseInstance);
        String expectedCode = "001";
        String expectedProduct = "CLE-ADV-CAN";
        String expectedEvent = "ADVANCE_CANCELLATION";

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class, RETURNS_DEEP_STUBS);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey("CLE_DO007")).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainerMock = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenReturn(dataObjectInstanceVariableContainerMock);

        // When
        ExportCollectionAdvanceCancellationRequest result = exportCollectionCancellationRequestService.createExportCollectionAdvanceCancellationRequest(request);

        // Then
        verify(pagoNxtRequestUtils, times(1)).updateDraftDocumentsOnCaseInstance(request, caseInstance);
        verify(pagoNxtRequestUtils, times(1)).setOperationDocumentsFromRequestDocuments("CLE-ADV-CAN-001", "userType");
        verify(exportCollectionValidation).validateConfirm(request);

        assertEquals(expectedCode, result.getCode(), "Result should match pattern");
        assertEquals(expectedProduct, result.getProduct(), "Product should match pattern");
        assertEquals(expectedEvent, result.getEvent(), "Event should match pattern");
    }
    @Test
    void ExportCollectionAdvanceCancellationRequest_generateVariableOperation_throwsInvalidRequestException() {
        // Given
        ExportCollectionAdvanceCancellationRequest request = mock(ExportCollectionAdvanceCancellationRequest.class, RETURNS_DEEP_STUBS);

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey(any()).operation(any())).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionCancellationRequestService.createExportCollectionAdvanceCancellationRequest(request),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for create a operation in flow with code null is missing", exception.getMessage(), "Expected exception message when request has missing values");

    }
    @Test
    void getCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("operationCode", "CLE-ADV-CAN-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        TaskRepresentation taskRepresentation = new TaskRepresentation();
        when(taskUtils.getTask("TSK-123", false)).thenReturn(taskRepresentation);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C005")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceCancellationCaseSeq", "CLE-ADV-CAN-123")).thenReturn(caseInstanceQuery);
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionCancellationRequestService.getCompleteInfoExportCollectionAdvanceCancellationRequest("TSK-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollectionAdvance(any());
        verify(caseSecurityService).checkReadTask(any(), eq("TSK-123"));
        verify(pagoNxtRequestUtils).mapCompleteInfoPagoNxtRequest(any(), eq(caseVariables), eq(taskRepresentation), eq(taskVariables));
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceCancellationRequest_whenErrorMapping_thenThrowResourceNotFoundException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("returnReason", "returnReason1");
        taskVariables.put("returnComment", "returnComment11");
        taskVariables.put("operationCode", "CLE-ADV-CAN-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C005")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceCancellationCaseSeq", "CLE-ADV-CAN-123")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.singleResult()).thenReturn(null);
        UserInfo userInfo = new UserInfo();
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionCancellationRequestService.getCompleteInfoExportCollectionAdvanceCancellationRequest("TSK-123", userInfo));
        // Then
        assertEquals("No case found with code CLE-ADV-CAN-123", exception.getMessage());
        assertEquals("errorCaseNotFound", exception.getKey());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceCancellationRequest_ok_thenInvokeCompleteTaskCompleteInfo() {
        // Given
        ExportCollectionAdvanceCancellationRequest exportCollectionCancellationRequest = mockRequest();
        // When
        exportCollectionCancellationRequestService.completeCompleteInfoExportCollectionAdvanceCancellationRequest(exportCollectionCancellationRequest, "TSK-123");
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
        when(caseInstanceQuery.caseDefinitionKey("CLE_C005")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceCancellationCaseSeq", "CLE-ADV-CAN-123")).thenReturn(caseInstanceQuery);
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionCancellationRequestService.getPetitionRequestDetails("CLE-ADV-CAN-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollectionAdvance(any());
        verify(caseSecurityService).checkRead(userInfo, "CLE-ADV-CAN-123");
        verify(pagoNxtRequestUtils).mapDetailedPagoNxtRequest(any(), eq(caseVariables));
    }

    private ExportCollectionAdvanceCancellationRequest getRequest() {
        ExportCollectionAdvanceCancellationRequest result = new ExportCollectionAdvanceCancellationRequest();
        result.setRequester(new UserInfo(new User("userId", "userDisplayName", "userType")));
        result.setCustomer(new Customer("customerId", "ES", null, "taxId", null, "personNumber", "segment", null));
        result.setExportCollectionAdvance(new ExportCollectionAdvance("code", null, null, null, null, 12.34D, "eur", null, new Date()));
        when(exportCollectionUtils.findDataObjectByIdAndModel(any(), eq("CLE_DO005"))).thenReturn(null);
        return result;
    }

    private ExportCollectionAdvanceCancellationRequest mockRequest() {
        ExportCollectionAdvanceCancellationRequest request = mock(ExportCollectionAdvanceCancellationRequest.class);
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        doReturn(userInfo).when(request).getRequester();
        Customer customer = mock(Customer.class);
        doReturn(customer).when(request).getCustomer();
        ExportCollectionAdvance exportCollectionAdvance = mock(ExportCollectionAdvance.class);
        doReturn(exportCollectionAdvance).when(request).getExportCollectionAdvance();
        return request;
    }
}
