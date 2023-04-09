package com.pagonxt.onetradefinance.work.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerBuilder;
import com.flowable.dataobject.api.runtime.DataObjectModificationBuilder;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.flowable.platform.service.task.TaskRepresentation;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.CaseSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.MappingException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import com.pagonxt.onetradefinance.work.service.mapper.ExchangeInsuranceFlowableMapper;
import com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper;
import com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable;
import com.pagonxt.onetradefinance.work.utils.ExportCollectionUtils;
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
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@UnitTest
class ExportCollectionAdvanceRequestServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnRuntimeService cmmnRuntimeService;
    @Mock
    ExportCollectionUtils exportCollectionUtils;
    @Mock
    ExchangeInsuranceFlowableMapper exchangeInsuranceFlowableMapper;
    @Mock
    TaskUtils taskUtils;
    @Mock
    ExportCollectionValidation exportCollectionValidation;
    @Mock
    PagoNxtRequestUtils pagoNxtRequestUtils;
    @Mock
    CaseCommonVariablesHelper caseCommonVariablesHelper;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    DataObjectRuntimeService dataObjectRuntimeService;
    @Mock
    CaseSecurityService caseSecurityService;
    @Mock
    CmmnHistoryService cmmnHistoryService;

    @Spy
    ExportCollectionMapper exportCollectionMapper = new ExportCollectionMapper();

    @InjectMocks
    ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService;

    @Test
    void createExportCollectionAdvanceRequest_ok_createsCaseAndUpdatesRequestAndDocuments() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        when(cmmnRuntimeService.createCaseInstanceBuilder().caseDefinitionKey(any()).variables(any()).start())
                .thenReturn(caseInstance);
        ExportCollectionAdvanceRequest request = mock(ExportCollectionAdvanceRequest.class, Answers.RETURNS_DEEP_STUBS);
        when(request.getRequester().getUser()).thenReturn(new User());
        when(request.getCustomer()).thenReturn(new Customer());
        when(request.getRiskLine()).thenReturn(new RiskLine());
        when(request.getExpiration()).thenReturn(new Date());

        DataObjectInstanceVariableContainerBuilder builder = mock(DataObjectInstanceVariableContainerBuilder.class);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey(any()).operation(any())).thenReturn(builder);
        DataObjectInstanceVariableContainer container = mock(DataObjectInstanceVariableContainer.class);
        when(builder.create()).thenReturn(container);

        // When
        exportCollectionAdvanceRequestService.createExportCollectionAdvanceRequest(request);

        // Then
        verify(pagoNxtRequestUtils).updateDraftDocumentsOnCaseInstance(any(), any());
        verify(exportCollectionValidation).validateDraft(request);
    }

    @Test
    void createExportCollectionAdvanceRequest_requestCodeIsNotBlank_throwsInvalidRequestException() {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        doReturn("code").when(exportCollectionAdvanceRequest).getCode();

        // When
        Exception exception = assertThrows(InvalidRequestException.class, () -> exportCollectionAdvanceRequestService.createExportCollectionAdvanceRequest(exportCollectionAdvanceRequest), "Expected exception message when code is not blank");

        // Then
        assertEquals("In a draft creation, the field 'code' cannot be informed.", exception.getMessage(), "Expected exception message when code is not blank");
    }

    @Test
    void createExportCollectionAdvanceRequest_noCaseFound_throwsResourceNotFoundException() throws Exception {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        ExportCollection exportCollection = mock(ExportCollection.class);
        doReturn(exportCollection).when(exportCollectionAdvanceRequest).getExportCollection();
        doReturn("exportCollectionCode").when(exportCollection).getCode();

        CaseInstanceBuilder caseInstanceBuilder = mock(CaseInstanceBuilder.class);
        doReturn(caseInstanceBuilder).when(cmmnRuntimeService).createCaseInstanceBuilder();
        doReturn(caseInstanceBuilder).when(caseInstanceBuilder).caseDefinitionKey("CLE_C003");
        CaseInstance caseInstanceMock = mock(CaseInstance.class);
        doReturn(caseInstanceMock).when(caseInstanceBuilder).start();
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionAdvanceRequest).getCustomer();
        RiskLine riskLine = mock(RiskLine.class);
        doReturn(riskLine).when(exportCollectionAdvanceRequest).getRiskLine();
        doThrow(ResourceNotFoundException.class).when(exportCollectionUtils).findDataObjectByIdAndModel("exportCollectionCode", "CLE_DO003");
        List<ExchangeInsuranceFlowable> exchangeInsuranceFlowableList = List.of();
        when(exchangeInsuranceFlowableMapper.toFlowable(anyList())).thenReturn(exchangeInsuranceFlowableList);
        when(caseCommonVariablesHelper.getJsonNode(any())).thenReturn(null);
        // When and Then
        assertThrows(ResourceNotFoundException.class, () -> exportCollectionAdvanceRequestService.createExportCollectionAdvanceRequest(exportCollectionAdvanceRequest), "Expected exception when no case found with the indicated code");
    }

    @Test
    void createExportCollectionAdvanceRequest_notGettingExportCollection_throwsServiceException() {
        // Given
        CaseInstanceBuilder caseInstanceBuilder = mock(CaseInstanceBuilder.class);
        doReturn(caseInstanceBuilder).when(cmmnRuntimeService).createCaseInstanceBuilder();
        doReturn(caseInstanceBuilder).when(caseInstanceBuilder).caseDefinitionKey("CLE_C003");

        CaseInstance caseInstanceMock = mock(CaseInstance.class);
        when(caseInstanceBuilder.start()).thenReturn(caseInstanceMock);
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionAdvanceRequest).getCustomer();
        RiskLine riskLine = mock(RiskLine.class);
        doReturn(riskLine).when(exportCollectionAdvanceRequest).getRiskLine();
        ExportCollection exportCollection = mock(ExportCollection.class);
        doReturn(exportCollection).when(exportCollectionAdvanceRequest).getExportCollection();
        doReturn("exportCollectionCode").when(exportCollection).getCode();
        doThrow(ServiceException.class).when(exportCollectionUtils).findDataObjectByIdAndModel("exportCollectionCode", "CLE_DO003");
        // When and then
        assertThrows(ServiceException.class,
                () -> exportCollectionAdvanceRequestService.createExportCollectionAdvanceRequest(exportCollectionAdvanceRequest),
                "Expected exception when sending selected export collection");
    }

    @Test
    void updateExportCollectionAdvanceRequest_ok_updatesVariablesAndDocuments() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn("id").when(caseInstance).getId();
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables()
                .caseDefinitionKey(any()).variableValueEquals(any(), any())
                .singleResult())
                .thenReturn(caseInstance);
        ExportCollectionAdvanceRequest request = mock(ExportCollectionAdvanceRequest.class, Answers.RETURNS_DEEP_STUBS);
        when(request.getRequester().getUser()).thenReturn(new User());
        when(request.getCode()).thenReturn("code");
        when(request.getCustomer()).thenReturn(new Customer());
        when(request.getRiskLine()).thenReturn(new RiskLine());
        when(request.getExpiration()).thenReturn(new Date());

        // When
        exportCollectionAdvanceRequestService.updateExportCollectionAdvanceRequest(request);

        // Then
        verify(pagoNxtRequestUtils).updateDraftDocumentsOnCaseInstance(request, caseInstance);
        verify(exportCollectionValidation).validateDraft(request);
        verify(caseSecurityService).checkEdit(any(), eq(caseInstance));
    }

    @Test
    void updateExportCollectionAdvanceRequest_requestCodeIsBlank_throwsInvalidRequestException() {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);

        // When
        Exception exception = assertThrows(InvalidRequestException.class, () -> exportCollectionAdvanceRequestService.updateExportCollectionAdvanceRequest(exportCollectionAdvanceRequest), "Expected exception when code is missing");

        // Then
        assertEquals("Mandatory data is missing: code", exception.getMessage(), "Expected exception when code is missing");
    }

    @Test
    void updateExportCollectionAdvanceRequest_caseInstanceNotFound_throwsResourceNotFoundException() {
        // Given
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables()
                .caseDefinitionKey(any()).variableValueEquals(any(), any())
                .singleResult())
                .thenReturn(null);
        ExportCollectionAdvanceRequest request = mock(ExportCollectionAdvanceRequest.class);
        when(request.getCode()).thenReturn("code");

        // When
        Exception thrown = assertThrows(ResourceNotFoundException.class,
                () -> exportCollectionAdvanceRequestService.updateExportCollectionAdvanceRequest(request),
                "Should throw ResourceNotFoundException");

        // Then
        assertEquals("No case found with code code", thrown.getMessage(), "Exception thrown should have a valid message");
    }
    @Test
    void ExportCollectionAdvanceRequest_generateVariableOperation_throwsInvalidRequestException() {
        // Given
        ExportCollectionAdvanceRequest request = mock(ExportCollectionAdvanceRequest.class, Answers.RETURNS_DEEP_STUBS);
        when(request.getRequester().getUser()).thenReturn(new User());
        when(request.getCustomer()).thenReturn(new Customer());
        when(request.getRiskLine()).thenReturn(new RiskLine());
        when(request.getExpiration()).thenReturn(new Date());
        DataObjectInstanceVariableContainerBuilder dataObjectInstanceVariableContainerBuilderMock = mock(DataObjectInstanceVariableContainerBuilder.class);
        when(dataObjectRuntimeService.createDataObjectValueInstanceBuilderByDefinitionKey(any()).operation(any())).thenReturn(dataObjectInstanceVariableContainerBuilderMock);
        when(dataObjectInstanceVariableContainerBuilderMock.create()).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionAdvanceRequestService.createExportCollectionAdvanceRequest(request),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for create a operation in flow with code null is missing", exception.getMessage(), "Expected exception message when request has missing values");

    }
    @Test
    void createExportCollectionAdvanceRequest_updateVariableOperation_throwsInvalidRequestException() {
        // Given
        ExportCollectionAdvanceRequest request = mock(ExportCollectionAdvanceRequest.class, Answers.RETURNS_DEEP_STUBS);
        when(request.getRequester().getUser()).thenReturn(new User());
        when(request.getCustomer()).thenReturn(new Customer());
        when(request.getRiskLine()).thenReturn(new RiskLine());
        when(request.getExpiration()).thenReturn(new Date());
        doReturn("XX").when(request).getCode();
        DataObjectModificationBuilder dataObjectModificationBuilderMock = mock(DataObjectModificationBuilder.class);
        when(dataObjectRuntimeService.createDataObjectModificationBuilder().definitionKey(any()).operation(any())).thenReturn(dataObjectModificationBuilderMock);
        when(dataObjectModificationBuilderMock.modify()).thenThrow(FlowableIllegalArgumentException.class);
        // When
        Exception exception = assertThrows(InvalidRequestException.class,
                () -> exportCollectionAdvanceRequestService.updateExportCollectionAdvanceRequest(request),
                "Expected exception message when request has missing values");

        // Then
        assertEquals("Mandatory data for update a operation in flow with code XX is missing", exception.getMessage(), "Expected exception message when request has missing values");

    }
    @Test
    void confirmExportCollectionAdvanceRequest_ok_returnsValidObject() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        doReturn("id").when(caseInstance).getId();
        when(cmmnRuntimeService.createCaseInstanceQuery().includeCaseVariables()
                .caseDefinitionKey(any()).variableValueEquals(any(), any())
                .singleResult())
                .thenReturn(caseInstance);
        ExportCollectionAdvanceRequest request = mock(ExportCollectionAdvanceRequest.class, Answers.RETURNS_DEEP_STUBS);
        User user = new User();
        when(request.getRequester().getUser()).thenReturn(user);
        when(request.getCode()).thenReturn("code");

        // When
        exportCollectionAdvanceRequestService.confirmExportCollectionAdvanceRequest(request);

        // Then
        verify(taskUtils).completeTaskDraft(eq(caseInstance), any());
        verify(exportCollectionValidation).validateConfirm(request);
    }

    @Test
    void confirmExportCollectionAdvanceRequest_requestCodeIsBlank_throwsInvalidRequestException() {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);

        // When
        Exception exception = assertThrows(InvalidRequestException.class, () -> exportCollectionAdvanceRequestService.confirmExportCollectionAdvanceRequest(exportCollectionAdvanceRequest), "Expected exception when code is missing");

        // Then
        assertEquals("Mandatory data is missing: code", exception.getMessage(), "Expected exception when code is missing");
    }

    @Test
    void confirmExportCollectionAdvanceRequest_noDraftFound_throwsResourceNotFoundException() {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        doReturn("code").when(exportCollectionAdvanceRequest).getCode();

        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).caseDefinitionKey(any());
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), any());
        doReturn(null).when(caseInstanceQuery).singleResult();

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionAdvanceRequestService.confirmExportCollectionAdvanceRequest(exportCollectionAdvanceRequest), "Expected exception when no draft is found with indicated code");

        // Then
        assertEquals("No case found with code code", exception.getMessage(), "Expected exception when no draft is found with indicated code");
    }

    @Test
    void getExportCollectionAdvanceRequestByCode_ok_returnsValidObject() {
        // Given
        DataObjectInstanceVariableContainerImpl operation = mock(DataObjectInstanceVariableContainerImpl.class);
        when(operation.getString(any())).thenReturn(null);
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariables = Map.of(
                "exportCollectionAdvanceRequestCaseSeq", "code",
                "product", "product",
                "event", "event",
                "requestAdvanceExchangeInsurance", "[{}]",
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
        ExportCollectionAdvanceRequest result = exportCollectionAdvanceRequestService.getExportCollectionAdvanceRequestByCode("code", userInfo);

        // Then
        assertNotNull(result, "Result should not be null");
        assertEquals("code", result.getCode(), "Result should have a valid code");
    }

    @Test
    void getExportCollectionAdvanceRequestByCode_noDraftFound_throwsResourceNotFoundException() {
        // Given
        String code = "XX";
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        doReturn(caseInstanceQuery).when(cmmnRuntimeService).createCaseInstanceQuery();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).includeCaseVariables();
        doReturn(caseInstanceQuery).when(caseInstanceQuery).caseDefinitionKey(any());
        doReturn(caseInstanceQuery).when(caseInstanceQuery).variableValueEquals(any(), eq(code));
        doReturn(null).when(caseInstanceQuery).singleResult();
        UserInfo userInfo = mock(UserInfo.class);

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> exportCollectionAdvanceRequestService.getExportCollectionAdvanceRequestByCode(code, userInfo),
                "Expected exception message when code is null");

        // Then
        assertEquals("No case found with code XX", exception.getMessage(), "Expected exception when no draft is found with indicated code");
    }

    @Test
    void getExportCollectionAdvanceRequestByCode_mapToExportCollectionRequestFails_throwsMappingException() {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariables = Map.of(
                "exportCollectionAdvanceRequestCaseSeq", "code",
                "product", "product",
                "event", "event"
        );
        doReturn(caseVariables).when(caseInstance).getCaseVariables();
        when(cmmnRuntimeService.createCaseInstanceQuery()
                .includeCaseVariables()
                .caseDefinitionKey(any())
                .variableValueEquals(any(), any())
                .singleResult())
                .thenReturn(caseInstance);
        UserInfo userInfo = mock(UserInfo.class);

        // When
        Exception thrown = assertThrows(MappingException.class,
                () -> exportCollectionAdvanceRequestService.getExportCollectionAdvanceRequestByCode("code", userInfo),
                "Should throw MappingException");

        // THen
        assertEquals("Error mapping request", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceRequest_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("operationCode", "CLE-ADV-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        TaskRepresentation taskRepresentation = new TaskRepresentation();
        when(taskUtils.getTask("TSK-123", false)).thenReturn(taskRepresentation);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C003")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceRequestCaseSeq", "CLE-ADV-123")).thenReturn(caseInstanceQuery);
        CaseInstance caseInstance = mock(CaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionAdvanceRequestService.getCompleteInfoExportCollectionAdvanceRequest("TSK-123", userInfo);
        // Then
        verify(exchangeInsuranceFlowableMapper).toModel(any(DataObjectInstanceVariableContainerImpl.class));
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollection(any());
        verify(pagoNxtRequestUtils).mapCompleteInfoPagoNxtRequest(any(), eq(caseVariables), eq(taskRepresentation), eq(taskVariables));
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceRequest_whenErrorMapping_thenThrowResourceNotFoundException() {
        // Given
        Map<String, Object> taskVariables = new HashMap<>();
        taskVariables.put("returnReason", "returnReason1");
        taskVariables.put("returnComment", "returnComment11");
        taskVariables.put("operationCode", "CLE-ADV-123");
        when(taskUtils.getTaskVariablesCompleteInfo("TSK-123")).thenReturn(taskVariables);
        CaseInstanceQuery caseInstanceQuery = mock(CaseInstanceQuery.class);
        when(cmmnRuntimeService.createCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C003")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceRequestCaseSeq", "CLE-ADV-123")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.singleResult()).thenReturn(null);
        UserInfo userInfo = new UserInfo();
        // When
        ServiceException exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionAdvanceRequestService.getCompleteInfoExportCollectionAdvanceRequest("TSK-123", userInfo));
        // Then
        assertEquals("No case found with code CLE-ADV-123", exception.getMessage());
        assertEquals("errorCaseNotFound", exception.getKey());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceRequest_ok_thenInvokeCompleteTaskCompleteInfo() {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mockRequest();
        // When
        exportCollectionAdvanceRequestService.completeCompleteInfoExportCollectionAdvanceRequest(exportCollectionAdvanceRequest, "TSK-123");
        // Then
        verify(taskUtils).setCompleteInfoTaskVariables(eq("TSK-123"), any(), any());
        verify(exportCollectionUtils).findDataObjectByIdAndModel(any(), eq("CLE_DO003"));
    }

    private ExportCollectionAdvanceRequest mockRequest() {
        ExportCollectionAdvanceRequest request = mock(ExportCollectionAdvanceRequest.class);
        User user = mock(User.class);
        UserInfo userInfo = new UserInfo(user);
        doReturn(userInfo).when(request).getRequester();
        Customer customer = mock(Customer.class);
        doReturn(customer).when(request).getCustomer();
        ExportCollection exportCollection = mock(ExportCollection.class);
        doReturn(exportCollection).when(request).getExportCollection();
        RiskLine riskLine = mock(RiskLine.class);
        doReturn(riskLine).when(request).getRiskLine();
        return request;
    }

    @Test
    void getPetitionRequestDetails_ok_returnCompleteInfoExportCollectionRequest() throws JsonProcessingException {
        // Given
        HistoricCaseInstanceQuery caseInstanceQuery = mock(HistoricCaseInstanceQuery.class);
        when(cmmnHistoryService.createHistoricCaseInstanceQuery()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.includeCaseVariables()).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.caseDefinitionKey("CLE_C003")).thenReturn(caseInstanceQuery);
        when(caseInstanceQuery.variableValueEquals("exportCollectionAdvanceRequestCaseSeq", "CLE-ADV-123")).thenReturn(caseInstanceQuery);
        HistoricCaseInstance caseInstance = mock(HistoricCaseInstance.class);
        Map<String, Object> caseVariables = new HashMap<>();
        caseVariables.put("operation", mock(DataObjectInstanceVariableContainerImpl.class));
        when(caseInstance.getCaseVariables()).thenReturn(caseVariables);
        when(caseInstanceQuery.singleResult()).thenReturn(caseInstance);
        User user = new User("userTest", "UserTest", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        // When
        exportCollectionAdvanceRequestService.getPetitionRequestDetails("CLE-ADV-123", userInfo);
        // Then
        verify(pagoNxtRequestUtils).setCommonVariablesFromMapVariables(any(), eq(caseVariables), eq("OFFICE"));
        verify(exportCollectionMapper).mapDataObjectInstanceVariableContainerToExportCollection(any());
        verify(caseSecurityService).checkRead(eq(userInfo), eq("CLE-ADV-123"));
        verify(pagoNxtRequestUtils).mapDetailedPagoNxtRequest(any(), eq(caseVariables));
    }
}
