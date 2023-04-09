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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionModificationRequestServiceTest {

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
    ExportCollectionModificationRequestService exportCollectionModificationRequestService;

    @Test
    void createExportCollectionModificationRequest_ok_returnsValidData() {
        // Given
        ExportCollectionModificationRequest request = getRequest();
        CaseInstance caseInstance = mock(CaseInstance.class, RETURNS_DEEP_STUBS);
        when(caseInstance.getId()).thenReturn("CLE-001");
        when(caseInstance.getCaseVariables().get("product")).thenReturn("CLE");
        when(caseInstance.getCaseVariables().get("event")).thenReturn("MODIFICATION");
        when(caseInstance.getCaseVariables().get("exportCollectionModificationCaseSeq")).thenReturn("001");
        when(cmmnRuntimeService.createCaseInstanceBuilder().caseDefinitionKey("CLE_C002").start()).thenReturn(caseInstance);
        String expectedCode = "001";
        String expectedProduct = "CLE";
        String expectedEvent = "MODIFICATION";

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class, RETURNS_DEEP_STUBS);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey("CLE_DO002")).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainerMock = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenReturn(dataObjectInstanceVariableContainerMock);

        // When
        ExportCollectionModificationRequest result = exportCollectionModificationRequestService.createExportCollectionModificationRequest(request);

        // Then
        verify(pagoNxtRequestUtils, times(1)).updateDraftDocumentsOnCaseInstance(request, caseInstance);
        verify(pagoNxtRequestUtils, times(1)).setOperationDocumentsFromRequestDocuments("CLE-001", "userType");
        verify(exportCollectionValidation).validateConfirm(request);

        assertEquals(expectedCode, result.getCode(), "Result should match pattern");
        assertEquals(expectedProduct, result.getProduct(), "Product should match pattern");
        assertEquals(expectedEvent, result.getEvent(), "Event should match pattern");
    }
    @Test
    void ExportCollectionModificationRequest_generateVariableOperation_throwsInvalidRequestException() {
        // Given
        ExportCollectionModificationRequest request = mock(ExportCollectionModificationRequest.class, RETURNS_DEEP_STUBS);

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey(any()).operation(any())).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionModificationRequestService.createExportCollectionModificationRequest(request),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for create a operation in flow with code null is missing", exception.getMessage(), "Expected exception message when request has missing values");

    }
    @Test
    void getCompleteInfoExportCollectionModificationRequest_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("operationCode", "CLE-MOD-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        TaskRepresentation taskRepresentation = new TaskRepresentation();
        when(taskUtils.getTask("TSK-123", false)).thenReturn(taskRepresentation);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C002")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionModificationCaseSeq", "CLE-MOD-123")).thenReturn(caseInstanceQuery);
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionModificationRequestService.getCompleteInfoExportCollectionModificationRequest("TSK-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollection(any());
        verify(caseSecurityService).checkReadTask(any(), eq("TSK-123"));
        verify(pagoNxtRequestUtils).mapCompleteInfoPagoNxtRequest(any(), eq(caseVariables), eq(taskRepresentation), eq(taskVariables));
    }

    @Test
    void getCompleteInfoExportCollectionModificationRequest_whenErrorMapping_thenThrowResourceNotFoundException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("returnReason", "returnReason1");
        taskVariables.put("returnComment", "returnComment11");
        taskVariables.put("operationCode", "CLE-MOD-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C002")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionModificationCaseSeq", "CLE-MOD-123")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.singleResult()).thenReturn(null);
        UserInfo userInfo = new UserInfo();
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionModificationRequestService.getCompleteInfoExportCollectionModificationRequest("TSK-123", userInfo));
        // Then
        assertEquals("No case found with code CLE-MOD-123", exception.getMessage());
        assertEquals("errorCaseNotFound", exception.getKey());
    }

    @Test
    void completeCompleteInfoExportCollectionModificationRequest_ok_thenInvokeCompleteTaskCompleteInfo() {
        // Given
        ExportCollectionModificationRequest exportCollectionModificationRequest = mockRequest();
        // When
        exportCollectionModificationRequestService.completeCompleteInfoExportCollectionModificationRequest(exportCollectionModificationRequest, "TSK-123");
        // Then
        verify(taskUtils).setCompleteInfoTaskVariables(eq("TSK-123"), any(), any());
        verify(exportCollectionUtils).findDataObjectByIdAndModel(any(), eq("CLE_DO003"));
        verify(caseSecurityService).checkEditTask(any(), eq("TSK-123"));
    }

    @Test
    void getPetitionRequestDetails_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        when(cmmnHistoryService.createHistoricCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C002")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionModificationCaseSeq", "CLE-MOD-123")).thenReturn(caseInstanceQuery);
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionModificationRequestService.getPetitionRequestDetails("CLE-MOD-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollection(any());
        verify(caseSecurityService).checkRead(any(), eq("CLE-MOD-123"));
        verify(pagoNxtRequestUtils).mapDetailedPagoNxtRequest(any(), eq(caseVariables));
    }

    private ExportCollectionModificationRequest getRequest() {
        ExportCollectionModificationRequest result = new ExportCollectionModificationRequest();
        result.setRequester(new UserInfo(new User("userId", "userDisplayName", "userType")));
        result.setCustomer(new Customer("customerId", "ES", null, "taxId", null, "personNumber", "segment", null));
        result.setExportCollection(new ExportCollection("code", null, null, null, null, 12.34D, "eur", null));
        when(exportCollectionUtils.findDataObjectByIdAndModel(any(), eq("CLE_DO003"))).thenReturn(null);
        return result;
    }

    private ExportCollectionModificationRequest mockRequest() {
        ExportCollectionModificationRequest request = mock(ExportCollectionModificationRequest.class);
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        doReturn(userInfo).when(request).getRequester();
        Customer customer = mock(Customer.class);
        doReturn(customer).when(request).getCustomer();
        ExportCollection exportCollection = mock(ExportCollection.class);
        doReturn(exportCollection).when(request).getExportCollection();
        return request;
    }
}
