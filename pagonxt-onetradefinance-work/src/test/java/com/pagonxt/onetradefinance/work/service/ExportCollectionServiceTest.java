package com.pagonxt.onetradefinance.work.service;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionServiceTest {

    @InjectMocks
    private ExportCollectionService exportCollectionService;
    @Mock
    private DataObjectRuntimeService dataObjectRuntimeService;
    @Mock
    private ExportCollectionMapper exportCollectionMapper;

    @Test
    void getExportCollections_whenPassingQueryCollectionTypeRequest_thenFilterByProductAndApprovedStatus() {
        // Given
        ExportCollectionQuery query = new ExportCollectionQuery();
        User user = new User("userId", "userName", "userType");
        query.setRequester(user);
        query.setCollectionType("request");
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey("CLE_DO003")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("searchExportCollectionByCustomerCodeAndStatusOperation")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("customerCode",  null)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("status",  "ACTIVE")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(new ArrayList<>());
        // When
        exportCollectionService.getExportCollections(query);
        // Then
        verify(dataObjectRuntimeService, times(1)).createDataObjectInstanceQuery();
        verify(dataObjectInstanceVariableContainerQuery, times(1)).definitionKey("CLE_DO003");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).operation("searchExportCollectionByCustomerCodeAndStatusOperation");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("customerCode",  null);
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("status",  "ACTIVE");
    }

    @Test
    void getExportCollections_whenPassingQueryCollectionTypeRequestWithPersonNumber_thenFilterByPersonNumber() {
        // Given
        ExportCollectionQuery query = new ExportCollectionQuery();
        User user = new User("userId", "userName", "userType");
        query.setRequester(user);
        query.setCustomerPersonNumber("BUC-1234567");
        query.setCollectionType("request");
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey("CLE_DO003")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("searchExportCollectionByCustomerCodeAndStatusOperation")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("customerCode",  "BUC-1234567")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("status",  "ACTIVE")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(new ArrayList<>());
        // When
        exportCollectionService.getExportCollections(query);
        // Then
        verify(dataObjectRuntimeService, times(1)).createDataObjectInstanceQuery();
        verify(dataObjectInstanceVariableContainerQuery, times(1)).definitionKey("CLE_DO003");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).operation("searchExportCollectionByCustomerCodeAndStatusOperation");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("customerCode",  "BUC-1234567");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("status",  "ACTIVE");
    }

    @Test
    void getExportCollections_whenPassingQueryCollectionTypeRequestReturnsValue_thenMapExportCollection() {
        // Given
        ExportCollectionQuery query = new ExportCollectionQuery();
        User user = new User("userId", "userName", "userType");
        query.setRequester(user);
        query.setCustomerPersonNumber("BUC-1234567");
        query.setCollectionType("request");
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey("CLE_DO003")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("searchExportCollectionByCustomerCodeAndStatusOperation")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("customerCode",  "BUC-1234567")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("status",  "ACTIVE")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(new ArrayList<>());

        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));
        // When
        exportCollectionService.getExportCollections(query);
        // Then
        verify(dataObjectRuntimeService, times(1)).createDataObjectInstanceQuery();
        verify(dataObjectInstanceVariableContainerQuery, times(1)).definitionKey("CLE_DO003");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).operation("searchExportCollectionByCustomerCodeAndStatusOperation");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("customerCode",  "BUC-1234567");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("status",  "ACTIVE");
        verify(exportCollectionMapper, times(1)).mapDataObjectInstanceVariableContainerToExportCollection(dataObjectInstanceVariableContainer);
    }

    @Test
    void getExportCollections_whenPassingQueryCollectionTypeAdvance_thenFilterByProductAndApprovedStatus() {
        // Given
        ExportCollectionQuery query = new ExportCollectionQuery();
        User user = new User("userId", "userName", "userType");
        query.setRequester(user);
        query.setCollectionType("advance");
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey("CLE_DO005")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("searchExportCollectionAdvanceByCustomerCodeAndStatusOperation")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("customerCode",  null)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("status",  "ACTIVE")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(new ArrayList<>());
        // When
        exportCollectionService.getExportCollections(query);
        // Then
        verify(dataObjectRuntimeService, times(1)).createDataObjectInstanceQuery();
        verify(dataObjectInstanceVariableContainerQuery, times(1)).definitionKey("CLE_DO005");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).operation("searchExportCollectionAdvanceByCustomerCodeAndStatusOperation");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("customerCode",  null);
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("status",  "ACTIVE");
    }

    @Test
    void getExportCollections_whenPassingQueryCollectionTypeAdvanceWithPersonNumber_thenFilterByPersonNumber() {
        // Given
        ExportCollectionQuery query = new ExportCollectionQuery();
        User user = new User("userId", "userName", "userType");
        query.setRequester(user);
        query.setCustomerPersonNumber("BUC-1234567");
        query.setCollectionType("advance");
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey("CLE_DO005")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("searchExportCollectionAdvanceByCustomerCodeAndStatusOperation")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("customerCode",  "BUC-1234567")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("status",  "ACTIVE")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(new ArrayList<>());
        // When
        exportCollectionService.getExportCollections(query);
        // Then
        verify(dataObjectRuntimeService, times(1)).createDataObjectInstanceQuery();
        verify(dataObjectInstanceVariableContainerQuery, times(1)).definitionKey("CLE_DO005");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).operation("searchExportCollectionAdvanceByCustomerCodeAndStatusOperation");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("customerCode",  "BUC-1234567");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("status",  "ACTIVE");
    }

    @Test
    void getExportCollections_whenPassingQueryCollectionTypeAdvanceReturnsValue_thenMapExportCollectionAdvance() {
        // Given
        ExportCollectionQuery query = new ExportCollectionQuery();
        User user = new User("userId", "userName", "userType");
        query.setRequester(user);
        query.setCustomerPersonNumber("BUC-1234567");
        query.setCollectionType("advance");
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey("CLE_DO005")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation("searchExportCollectionAdvanceByCustomerCodeAndStatusOperation")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("customerCode",  "BUC-1234567")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value("status",  "ACTIVE")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(new ArrayList<>());

        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));
        // When
        exportCollectionService.getExportCollections(query);
        // Then
        verify(dataObjectRuntimeService, times(1)).createDataObjectInstanceQuery();
        verify(dataObjectInstanceVariableContainerQuery, times(1)).definitionKey("CLE_DO005");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).operation("searchExportCollectionAdvanceByCustomerCodeAndStatusOperation");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("customerCode",  "BUC-1234567");
        verify(dataObjectInstanceVariableContainerQuery, times(1)).value("status",  "ACTIVE");
        verify(exportCollectionMapper, times(1)).mapDataObjectInstanceVariableContainerToExportCollectionAdvance(dataObjectInstanceVariableContainer);
    }

    @Test
    void getExportCollections_whenPassingQueryCollectionTypeUnknown_thenFilterByProductAndApprovedStatus() {
        // Given
        ExportCollectionQuery query = new ExportCollectionQuery();
        User user = new User("userId", "userName", "userType");
        query.setRequester(user);
        query.setCollectionType("unknown");
        // When
        Object result = exportCollectionService.getExportCollections(query);
        // Then
        verify(dataObjectRuntimeService, never()).createDataObjectInstanceQuery();
        assertTrue(result instanceof List);
        assertEquals(0, ((List<?>) result).size());
    }
}
