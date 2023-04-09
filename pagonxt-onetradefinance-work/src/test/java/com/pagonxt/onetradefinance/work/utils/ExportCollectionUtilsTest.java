package com.pagonxt.onetradefinance.work.utils;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionUtilsTest {

    @Mock
    private DataObjectRuntimeService dataObjectRuntimeService;

    @InjectMocks
    ExportCollectionUtils exportCollectionUtils;

    @Test
    void getExportCollection_whenPassingValidCodeAndDataObject_returnDataObject() {
        // Given
        DataObjectInstanceVariableContainerQuery queryMock = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(queryMock);
        when(queryMock.definitionKey("CLE_DO003")).thenReturn(queryMock);
        when(queryMock.operation("findById")).thenReturn(queryMock);
        when(queryMock.value("lookupId",  "code1")).thenReturn(queryMock);
        DataObjectInstanceVariableContainer expectedExportCollection = mock(DataObjectInstanceVariableContainer.class);
        when(queryMock.singleResult()).thenReturn(expectedExportCollection);
        // When
        DataObjectInstanceVariableContainer result = exportCollectionUtils.findDataObjectByIdAndModel("code1", "CLE_DO003");
        // Then
        assertEquals(expectedExportCollection, result);
    }

    @Test
    void getExportCollection_whenPassingInvalidCodeAndDataObject_throwResourceNotFoundException() {
        // Given
        DataObjectInstanceVariableContainerQuery queryMock = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(queryMock);
        when(queryMock.definitionKey("CLE_DO003")).thenReturn(queryMock);
        when(queryMock.operation("findById")).thenReturn(queryMock);
        when(queryMock.value("lookupId",  "invalidCode")).thenReturn(queryMock);
        when(queryMock.singleResult()).thenReturn(null);
        // When
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> exportCollectionUtils.findDataObjectByIdAndModel("invalidCode", "CLE_DO003"));
        // Then
        assertEquals("No collection found with code invalidCode", exception.getMessage());
    }
}
