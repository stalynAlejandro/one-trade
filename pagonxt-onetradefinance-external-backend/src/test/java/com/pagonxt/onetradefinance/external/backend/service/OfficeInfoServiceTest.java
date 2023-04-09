package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class OfficeInfoServiceTest {

    @Mock
    RestTemplateWorkService restTemplateWorkService;

    @Mock
    UserInfoService userInfoService;

    @InjectMocks
    OfficeInfoService officeInfoService;

    @Test
    void isValidOffice_whenPassingOfficeId_theReturnBooleanValue() {
        // Given
        boolean booleanValue = true;
        ControllerResponse controllerResponse = ControllerResponse.success("", true);
        when(restTemplateWorkService.isValidOffice(eq("1234"), any())).thenReturn(controllerResponse);
        // When
        Boolean response = officeInfoService.isValidOffice("1234");
        // Then
        ArgumentCaptor<AuthenticatedRequest> captor = ArgumentCaptor.forClass(AuthenticatedRequest.class);
        verify(restTemplateWorkService).isValidOffice(eq("1234"), captor.capture());
        assertEquals(booleanValue, response);
    }

    @Test
    void isValidMiddleOffice_whenPassingOfficeId_thenReturnBooleanValue() {
        // Given
        boolean booleanValue = true;
        ControllerResponse controllerResponse = ControllerResponse.success("", true);
        when(restTemplateWorkService.isValidMiddleOffice(eq("1234"), any())).thenReturn(controllerResponse);
        // When
        Boolean response = officeInfoService.isValidMiddleOffice("1234");
        // Then
        ArgumentCaptor<AuthenticatedRequest> captor = ArgumentCaptor.forClass(AuthenticatedRequest.class);
        verify(restTemplateWorkService).isValidMiddleOffice(eq("1234"), captor.capture());
        assertEquals(booleanValue, response);
    }

    @Test
    void getOfficeInfo_whenPassingOfficeId_theReturnOfficeInfoObject() {
        // Given
        OfficeInfo officeInfo = new OfficeInfo();
        officeInfo.setCountry("ES");
        officeInfo.setOffice("1235");
        officeInfo.setAddress("mockAddress");
        officeInfo.setPlace("Valencia");
        ControllerResponse controllerResponse = ControllerResponse.success("", officeInfo);
        when(restTemplateWorkService.getOfficeInfo(eq("1234"), any())).thenReturn(controllerResponse);
        // When
        OfficeInfo response = officeInfoService.getOfficeInfo("1234");
        // Then
        ArgumentCaptor<AuthenticatedRequest> captor = ArgumentCaptor.forClass(AuthenticatedRequest.class);
        verify(restTemplateWorkService).getOfficeInfo(eq("1234"), captor.capture());
        assertEquals(officeInfo, response);
    }

    @Test
    void getMiddleOffice_whenPassingOfficeId_thenReturnsString() {
        // Given
        String stringValue = "8911";
        ControllerResponse controllerResponse = ControllerResponse.success("", "8911");
        when(restTemplateWorkService.getMiddleOffice(eq("1234"), any())).thenReturn(controllerResponse);
        // When
        String response = officeInfoService.getMiddleOffice("1234");
        // Then
        ArgumentCaptor<AuthenticatedRequest> captor = ArgumentCaptor.forClass(AuthenticatedRequest.class);
        verify(restTemplateWorkService).getMiddleOffice(eq("1234"), captor.capture());
        assertEquals(stringValue, response);
    }

    @Test
    void getOffices_whenPassingOfficeId_theReturnList() {
        List<String> officeList = new ArrayList<>();
        officeList.add("1234");
        ControllerResponse controllerResponse = ControllerResponse.success("", officeList);
        when(restTemplateWorkService.getOffices(eq("1234"), any())).thenReturn(controllerResponse);
        // When
        List<String> response = officeInfoService.getOffices("1234");
        // Then
        ArgumentCaptor<AuthenticatedRequest> captor = ArgumentCaptor.forClass(AuthenticatedRequest.class);
        verify(restTemplateWorkService).getOffices(eq("1234"), captor.capture());
        assertEquals(officeList, response);
    }

    @Test
    void getOffices_whenPassingOfficeId_theReturnEmptyList() {
        List<String> officeList = new ArrayList<>();
        ControllerResponse controllerResponse = ControllerResponse.success("", officeList);
        when(restTemplateWorkService.getOffices(eq("1234"), any())).thenReturn(controllerResponse);
        // When
        List<String> response = officeInfoService.getOffices("1234");
        // Then
        ArgumentCaptor<AuthenticatedRequest> captor = ArgumentCaptor.forClass(AuthenticatedRequest.class);
        verify(restTemplateWorkService).getOffices(eq("1234"), captor.capture());
        assertEquals(officeList, response);
    }
}