package com.pagonxt.onetradefinance.work.service;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class RecipientsServiceTest {

    private static final String EXPECTED_OFFICE_EMAIL = "office123@test.com";
    private static final String EXPECTED_MIDDLE_OFFICE_EMAIL = "midlle.office.456@test.com";
    private static final String DEFINITION_KEY = "PGN_DO001";
    private static final String FIND_BY_ID = "findById";
    private static final String OFFICE = "officeCode";
    private static final String EMAIL = "email";

    @InjectMocks
    RecipientsService recipientsService;

    @Mock
    DataObjectRuntimeService dataObjectRuntimeService;


    @Test
    void testGetOfficeEmail_returnOfficeEmail() {

        //Given
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainer.getString(EMAIL)).thenReturn(EXPECTED_OFFICE_EMAIL);

        //When
        String result = recipientsService.getOfficeEmail("1235");

        //Then
        assertEquals(EXPECTED_OFFICE_EMAIL,result);
    }

    @Test
    void testGetOfficeEmail_returnNull() {

        //Given
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((null));

        //When
        String result = recipientsService.getOfficeEmail("1235");

        //Then
        assertEquals("",result);
    }

    @Test
    void testGetMiddleOfficeEmail_returnValidMiddleOfficeEmail() {
        //Given
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((dataObjectInstanceVariableContainer));
        when(dataObjectInstanceVariableContainer.getString(EMAIL)).thenReturn(EXPECTED_MIDDLE_OFFICE_EMAIL);

        //When
        String result = recipientsService.getMiddleOfficeEmail("1235");

        //Then
        assertEquals(EXPECTED_MIDDLE_OFFICE_EMAIL,result);
    }

    @Test
    void testGetMiddleOfficeEmail_returnNull() {
        //Given
        DataObjectInstanceVariableContainerQuery dataObjectInstanceVariableContainerQuery = mock(DataObjectInstanceVariableContainerQuery.class);
        when(dataObjectRuntimeService.createDataObjectInstanceQuery()).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.definitionKey(DEFINITION_KEY)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.operation(FIND_BY_ID)).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.value(OFFICE,  "1235")).thenReturn(dataObjectInstanceVariableContainerQuery);
        when(dataObjectInstanceVariableContainerQuery.singleResult()).thenReturn((null));

        //When
        String result = recipientsService.getMiddleOfficeEmail("1235");

        //Then
        //Then
        assertEquals("",result);
    }

}