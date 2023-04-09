package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectModificationBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.TaskUtils;
import com.pagonxt.onetradefinance.work.validation.ExportCollectionValidation;
import org.flowable.cmmn.api.CmmnHistoryService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.history.HistoricCaseInstance;
import org.flowable.cmmn.api.history.HistoricCaseInstanceQuery;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.CaseInstanceBuilder;
import org.flowable.cmmn.api.runtime.CaseInstanceQuery;
import org.flowable.cmmn.engine.CmmnEngineConfiguration;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.flowable.common.engine.impl.interceptor.CommandExecutor;
import org.flowable.job.service.JobServiceConfiguration;
import org.flowable.job.service.impl.TimerJobServiceImpl;
import org.flowable.job.service.impl.persistence.entity.TimerJobEntityManagerImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionRequestServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnRuntimeService cmmnRuntimeService;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnEngineConfiguration cmmnEngineConfiguration;
    @Mock
    PagoNxtRequestUtils pagoNxtRequestUtils;
    @Mock
    TaskUtils taskUtils;
    @Mock(answer = RETURNS_DEEP_STUBS)
    DataObjectRuntimeService dataObjectRuntimeService;
    @Mock
    ExportCollectionValidation exportCollectionValidation;
    @Mock
    ObjectMapper mapper;
    @Mock
    CaseSecurityService caseSecurityService;
    @Mock
    CmmnHistoryService cmmnHistoryService;

    @InjectMocks
    ExportCollectionRequestService exportCollectionRequestService;

    @Test
    void createExportCollectionRequest_caseCodeExists_throwsInvalidRequestException() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        doReturn("XX").when(exportCollectionRequest).getCode();
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionRequestService.createExportCollectionRequest(exportCollectionRequest),
                "Expected exception message when code is blank");

        // Then
        assertEquals("In a draft creation, the field 'code' cannot be informed.", exception.getMessage(), "Expected exception message when code is blank");
    }
    @Test
    void createExportCollectionRequest_generateVariableOperation_throwsInvalidRequestException() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        loadNoContentVariables(exportCollectionRequest);
        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey("CLE_DO001").operation(any())).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionRequestService.createExportCollectionRequest(exportCollectionRequest),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for create a operation in flow with code null is missing", exception.getMessage(), "Expected exception message when request has missing values");

    }
    @Test
    void createExportCollectionRequest_updateVariableOperation_throwsInvalidRequestException() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        loadNoContentVariables(exportCollectionRequest);
        doReturn("XX").when(exportCollectionRequest).getCode();
        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class);
        when(dataObjectRuntimeService.createDataObjectModificationBuilder().definitionKey(any()).operation(any())).thenReturn(dataObjectModificationBuilderMock);
        when(dataObjectModificationBuilderMock.modify()).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionRequestService.updateExportCollectionRequest(exportCollectionRequest),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for update a operation in flow with code XX is missing", exception.getMessage(), "Expected exception message when request has missing values");

    }
    @Test
    void createExportCollectionRequest_isOk_returnsExportCollectionRequest() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        CaseInstanceBuilder caseInstanceBuilder = mock(CaseInstanceBuilder.class);
        doReturn(caseInstanceBuilder).when(cmmnRuntimeService).createCaseInstanceBuilder();
        doReturn(caseInstanceBuilder).when(caseInstanceBuilder).caseDefinitionKey(any());
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstance).when(caseInstanceBuilder).start();
        doReturn("id").when(caseInstance).getId();
        String code = "Code_XX";
        Map<String, Object> map = Map.of("exportCollectionRequestCaseSeq", code,
                "product", "Export_XX",
                "event", "CLE_XX");
        doReturn(map).when(caseInstance).getCaseVariables();
        loadNoContentVariables(exportCollectionRequest);
        doReturn(5).when(exportCollectionRequest).getSavedStep();
        doReturn("country").when(exportCollectionRequest).getCountry();

        long milliseconds = System.currentTimeMillis();
        Date date = new Date(milliseconds +  86400000);
        doReturn(date).when(exportCollectionRequest).getAdvanceExpiration();
        doReturn(1.1).when(exportCollectionRequest).getAdvanceAmount();
        doReturn(10.1).when(exportCollectionRequest).getAmount();

        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class, RETURNS_DEEP_STUBS);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey("CLE_DO001")).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        // When
        exportCollectionRequestService.createExportCollectionRequest(exportCollectionRequest);
        // Then
        verify(cmmnRuntimeService).setVariable(caseInstance.getId(), "requestSavedStep", 5);
        verify(cmmnRuntimeService).setVariable(caseInstance.getId(), "country", "country");
        verify(pagoNxtRequestUtils).updateDraftDocumentsOnCaseInstance(exportCollectionRequest, caseInstance);
        verify(exportCollectionValidation).validateDraft(exportCollectionRequest);
    }

    @Test
    void updateExportCollectionRequest_requestWithoutCode_throwsInvalidRequestException() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);

        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionRequestService.updateExportCollectionRequest(exportCollectionRequest),
                "Expected exception message when code is blank");

        // Then
        assertEquals("Mandatory data is missing: code", exception.getMessage(), "Expected exception message when code is blank");
    }

    @Test
    void updateExportCollectionRequest_caseInstanceNotFound_throwsResourceNotFoundException() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        doReturn("XX").when(exportCollectionRequest).getCode();
        when(cmmnRuntimeService.createCaseInstanceQuery()
                .includeCaseVariables()
                .caseDefinitionKey(any())
                .variableValueEquals(any(), any())
                .singleResult())
                .thenReturn(null);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> exportCollectionRequestService.updateExportCollectionRequest(exportCollectionRequest),
                "Expected exception message when code is blank");

        // Then
        assertEquals("No case found with code XX", exception.getMessage(), "Expected exception message when code is blank");
    }

    @Test
    void updateExportCollectionRequest_isOk_returnsExportCollectionRequest() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        doReturn("XX").when(exportCollectionRequest).getCode();

        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).caseDefinitionKey(any());
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        doReturn("id1").when(caseInstance).getId();

        JobServiceConfiguration jobServiceConfiguration = mock(JobServiceConfiguration.class);
        TimerJobServiceImpl timerJobServiceImpl = mock(TimerJobServiceImpl.class);
        TimerJobEntityManagerImpl timerJobEntityManagerImpl = mock(TimerJobEntityManagerImpl.class);

        loadNoContentVariables(exportCollectionRequest);
        doReturn(5).when(exportCollectionRequest).getSavedStep();

        CommandExecutor commandExecutor = mock(CommandExecutor.class);

        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class, RETURNS_DEEP_STUBS);
        when(dataObjectRuntimeService.createDataObjectModificationBuilder()).thenReturn(dataObjectModificationBuilderMock);
        // When
        exportCollectionRequestService.updateExportCollectionRequest(exportCollectionRequest);

        // Then
        verify(cmmnRuntimeService).setVariable(caseInstance.getId(), "requestSavedStep", 5);
        verify(pagoNxtRequestUtils).updateDraftDocumentsOnCaseInstance(exportCollectionRequest, caseInstance);
        verify(exportCollectionValidation).validateDraft(exportCollectionRequest);
        verify(caseSecurityService).checkEdit(any(), any(CaseInstance.class));
    }

    @Test
    void confirmExportCollectionRequest_caseCodeIsBlank_throwsInvalidRequestException() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);

        // When
            Exception exception = assertThrows(InvalidRequestException.class, () -> exportCollectionRequestService.confirmExportCollectionRequest(exportCollectionRequest), "Expected exception message when code is blank");

        // Then
        assertEquals("Mandatory data is missing: code", exception.getMessage(), "Expected exception message when code is blank");
    }

    @Test
    void confirmExportCollectionRequest_caseInstanceIsNull_throwsResourceNotFoundException() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        doReturn("XX").when(exportCollectionRequest).getCode();

        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).caseDefinitionKey(any());
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(null).when(caseInstanceQuery).singleResult();

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionRequestService.confirmExportCollectionRequest(exportCollectionRequest), "Expected exception message when case instance is null");

        // Then
        assertEquals("No case found with code XX", exception.getMessage(), "Expected exception message when case instance is null");
    }

    @Test
    void confirmExportCollectionRequest_returnsExportCollectionRequest() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        doReturn("XX").when(exportCollectionRequest).getCode();
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        doReturn(userInfo).when(exportCollectionRequest).getRequester();

        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).caseDefinitionKey(any());
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(caseInstance).when(caseInstanceQuery).singleResult();
        doReturn("id1").when(caseInstance).getId();
        // When
        exportCollectionRequestService.confirmExportCollectionRequest(exportCollectionRequest);
        // Then
        verify(taskUtils).completeTaskDraft(eq(caseInstance), any());
        verify(exportCollectionValidation).validateConfirm(exportCollectionRequest);
        verify(caseSecurityService).checkEdit(any(), any(CaseInstance.class));
        verify(pagoNxtRequestUtils).setOperationDocumentsFromRequestDocuments(caseInstance.getId(), "OFFICE");
    }

    @Test
    void getExportCollectionRequestByCode_caseInstanceNotFound_throwsResourceNotFoundException() {
        // Given
        String code = "XX";
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).caseDefinitionKey(any());
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), eq(code));
        doReturn(null).when(caseInstanceQuery).singleResult();

        // When
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> exportCollectionRequestService.getExportCollectionRequestByCode(code, null),
                "Expected exception message when code is null");

        // Then
        assertEquals("No case found with code XX", exception.getMessage(), "Expected exception message when code is null");
    }

    @Test
    void getExportCollectionRequestByCode_mapResultFails_throwsMappingException() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()
                .includeCaseVariables()
                .caseDefinitionKey(any())
                .variableValueEquals(any(), any())
                .singleResult())
                .thenReturn(caseInstance);

        // When
        Exception thrown = assertThrows(MappingException.class,
                () -> exportCollectionRequestService.getExportCollectionRequestByCode("code", null),
                "Should throw MappingException");

        // Then
        assertEquals("Error mapping request", thrown.getMessage(), "Exception should contain valid message");
    }

    @Test
    void getExportCollectionRequestByCode_ok_returnsValidData() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class, Answers.RETURNS_DEEP_STUBS);
        when(operation.getLocalDate(any())).thenReturn(LocalDate.now());
        when(operation.getString(any())).thenReturn(null);
        Map<String, Object> caseVariables = Map.of(
                "exportCollectionRequestCaseSeq", "code",
                "product", "product",
                "event", "event",
                "operation", operation
        );
        doReturn(caseVariables).when(caseInstance).getCaseVariables();
        when(cmmnRuntimeService.createCaseInstanceQuery()
                .includeCaseVariables()
                .caseDefinitionKey(any())
                .variableValueEquals(any(), any())
                .singleResult())
                .thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        ExportCollectionRequest result = exportCollectionRequestService.getExportCollectionRequestByCode("code", userInfo);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals("code", result.getCode(), "Result should have a valid code");
    }

    @Test
    void getCompleteInfoExportCollectionRequest_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("operationCode", "CLE-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        TaskRepresentation taskRepresentation = new TaskRepresentation();
        when(taskUtils.getTask("TSK-123", false)).thenReturn(taskRepresentation);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C001")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionRequestCaseSeq", "CLE-123")).thenReturn(caseInstanceQuery);
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionRequestService.getCompleteInfoExportCollectionRequest("TSK-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(pagoNxtRequestUtils).mapCompleteInfoPagoNxtRequest(any(), eq(caseVariables), eq(taskRepresentation), eq(taskVariables));
    }

    @Test
    void getCompleteInfoExportCollectionRequest_whenNullCaseInstance_thenThrowResourceNotFoundException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("returnReason", "returnReason1");
        taskVariables.put("returnComment", "returnComment11");
        taskVariables.put("operationCode", "CLE-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C001")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionRequestCaseSeq", "CLE-123")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.singleResult()).thenReturn(null);
        UserInfo userInfo = new UserInfo();
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionRequestService.getCompleteInfoExportCollectionRequest("TSK-123", userInfo));
        // Then
        assertEquals("No case found with code CLE-123", exception.getMessage());
        assertEquals("errorCaseNotFound", exception.getKey());
    }

    @Test
    void completeCompleteInfoExportCollectionRequest_ok_thenInvokeCompleteTaskCompleteInfo() {
        // Given
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        loadNoContentVariables(exportCollectionRequest);
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        doReturn(userInfo).when(exportCollectionRequest).getRequester();
        // When
        exportCollectionRequestService.completeCompleteInfoExportCollectionRequest(exportCollectionRequest, "TSK-123");
        // Then
        verify(taskUtils).setCompleteInfoTaskVariables(eq("TSK-123"), any(), any());
    }

    @Test
    void getPetitionRequestDetails_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        when(cmmnHistoryService.createHistoricCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C001")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionRequestCaseSeq", "CLE-123")).thenReturn(caseInstanceQuery);
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionRequestService.getPetitionRequestDetails("CLE-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(pagoNxtRequestUtils).mapDetailedPagoNxtRequest(any(), eq(caseVariables));
    }

    private void loadNoContentVariables(ExportCollectionRequest exportCollectionRequest) {
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionRequest).getCustomer();
        Account account = mock(Account.class);
        doReturn(account).when(exportCollectionRequest).getNominalAccount();
        doReturn(account).when(exportCollectionRequest).getCommissionAccount();
        RiskLine riskLine = mock(RiskLine.class);
        doReturn(riskLine).when(exportCollectionRequest).getAdvanceRiskLine();
        doReturn("exportElectronicManagement").when(exportCollectionRequest).getCollectionType();
    }
}
