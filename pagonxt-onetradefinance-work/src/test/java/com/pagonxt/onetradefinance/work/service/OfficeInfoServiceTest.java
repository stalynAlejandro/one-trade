package com.pagonxt.onetradefinance.work.service;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
class OfficeInfoServiceTest {

    private static final String DEFINITION_KEY = "PGN_DO001";
    private static final String FIND_BY_ID = "findById";
    private static final String OFFICE = "officeCode";
    private static final String FIND_MIDDLE_OFFICE = "findMiddleOffice";
    private static final String MIDDLE_OFFICE = "middleOfficeCode";

    @InjectMocks
    private OfficeInfoService officeInfoService;
    @Mock
    private DataObjectRuntimeService dataObjectRuntimeService;

    @Test
    void isValidOffice_officeIsOfficeAndNotMiddleOffice_returnTrue() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_MIDDLE_OFFICE)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(MIDDLE_OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(Collections.emptyList());
        when(dataObjectInstanceVariableContainerQuery.singleResult().getLocalDate("deregistrationDate")).thenReturn(null);

        boolean result = officeInfoService.isValidOffice("1235");

        assertTrue(result);

    }

    @Test
    void isValidOffice_officeIsOfficeAndNotMiddleOfficeButOfficeIsInactive_returnFalse() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_MIDDLE_OFFICE)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(MIDDLE_OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(Collections.emptyList());
        when(dataObjectInstanceVariableContainerQuery.singleResult().getLocalDate("deregistrationDate")).thenReturn(LocalDate.ofEpochDay(22-2-2022));

        boolean result = officeInfoService.isValidOffice("1235");

        assertFalse(result);

    }

    @Test
    void isValidOffice_officeIsOfficeAndMiddleOffice_returnFalse() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_MIDDLE_OFFICE)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(MIDDLE_OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));

        boolean result = officeInfoService.isValidOffice("1235");

        assertFalse(result);

    }

    @Test
    void isValidOffice_officeIsNotOffice_returnFalse() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((null));

        boolean result = officeInfoService.isValidOffice("1235");

        assertFalse(result);

    }

    @Test
    void isValidMiddleOffice_officeIsMiddleOffice_returnTrue() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_MIDDLE_OFFICE)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(MIDDLE_OFFICE,  "8911")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));

        boolean result = officeInfoService.isValidMiddleOffice("8911");

        assertTrue(result);
    }

    @Test
    void isValidMiddleOffice_officeIsNotMiddleOffice_returnFalse() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_MIDDLE_OFFICE)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(MIDDLE_OFFICE,  "8911")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(Collections.emptyList());

        boolean result = officeInfoService.isValidMiddleOffice("8911");

        assertFalse(result);
    }

    @Test
    void getMiddleOffice_officeExistsAndIsActive() {

        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainer.getString("middleOfficeCode")).thenReturn("8911");
        when(dataObjectInstanceVariableContainerQuery.singleResult().getLocalDate("deregistrationDate")).thenReturn(null);

        String result = officeInfoService.getMiddleOffice("1235");

        assertEquals("8911",result);

    }

    @Test
    void getMiddleOffice_officeExistsButIsNotActive() {

        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainerQuery.singleResult().getLocalDate("deregistrationDate")).thenReturn(LocalDate.ofEpochDay(22-2-2022));

        String result = officeInfoService.getMiddleOffice("1235");

        assertEquals("none",result);

    }

    @Test
    void getMiddleOffice_officeNotExists() {

        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((null));

        String result = officeInfoService.getMiddleOffice("1235");

        assertEquals("none",result);

    }

    @Test
    void getOfficeInfo_officeExists_returnOfficeInfoObject() {

        OfficeInfo officeInfo = new OfficeInfo();
        officeInfo.setCountry("ES");
        officeInfo.setOffice("1235");
        officeInfo.setAddress("mockAddress");
        officeInfo.setPlace("Valencia");

        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainer.getString("country")).thenReturn("ES");
        when(dataObjectInstanceVariableContainer.getString("office")).thenReturn("1235");
        when(dataObjectInstanceVariableContainer.getString("address")).thenReturn("mockAddress");
        when(dataObjectInstanceVariableContainer.getString("place")).thenReturn("Valencia");
        when(dataObjectInstanceVariableContainerQuery.singleResult().getLocalDate("deregistrationDate")).thenReturn(null);

        OfficeInfo result = officeInfoService.getOfficeInfo("1235");

        assertEquals(officeInfo, result);

    }

    @Test
    void getOfficeInfo_officeNotExists_returnOfficeInfoObjectWithNullAttributes() {

        OfficeInfo officeInfo = new OfficeInfo();
        officeInfo.setCountry("ES");
        officeInfo.setOffice("1235");
        officeInfo.setAddress("mockAddress");
        officeInfo.setPlace("Valencia");

        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((null));

        OfficeInfo result = officeInfoService.getOfficeInfo("1235");

        assertNotEquals(officeInfo,result);

    }

    @Test
    void getOfficeInfo_officeExistsButIsNotActive_returnOfficeInfoObjectWithNullAttributes() {

        OfficeInfo officeInfo = new OfficeInfo();
        officeInfo.setCountry("ES");
        officeInfo.setOffice("1235");
        officeInfo.setAddress("mockAddress");
        officeInfo.setPlace("Valencia");

        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainerQuery.singleResult().getLocalDate("deregistrationDate")).thenReturn(LocalDate.ofEpochDay(22-2-2022));

        OfficeInfo result = officeInfoService.getOfficeInfo("1235");

        assertNotEquals(officeInfo,result);

    }

    @Test
    void getOffices_officeExist_returnNotEmptyList() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_MIDDLE_OFFICE)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(MIDDLE_OFFICE,  "8911")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(List.of(dataObjectInstanceVariableContainer));

        List<String> result = officeInfoService.getOffices("8911");

        assertFalse(result.isEmpty());
    }

    @Test
    void getOffices_officeNotExist_returnEmptyList() {
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_MIDDLE_OFFICE)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(MIDDLE_OFFICE,  "8911")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.list()).thenReturn(Collections.emptyList());

        List<String> result = officeInfoService.getOffices("8911");

        assertTrue(result.isEmpty());
    }

}
