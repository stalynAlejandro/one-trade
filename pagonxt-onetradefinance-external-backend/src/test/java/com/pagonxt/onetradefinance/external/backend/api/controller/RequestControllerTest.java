package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class RequestControllerTest {

    @InjectMocks
    RequestController requestController;
    @Mock
    OfficeInfoService officeInfoService;
    @Mock
    UserInfoService userInfoService;

    @Test
    void getUserInfo_ok_returnUserInfo() {
        // Given
        UserInfo expectedUserInfo = new UserInfo();
        when(userInfoService.getUserInfo()).thenReturn(expectedUserInfo);
        // When
        UserInfo result = requestController.getUserInfo();
        // Then
        assertEquals(expectedUserInfo, result);
    }

    @Test
    void injectRequestInfo_ok_setRequesterInfoFields() {
        // Given
        PagoNxtRequest request = new PagoNxtRequest();
        request.setOffice("1234");
        UserInfo expectedUserInfo = new UserInfo();
        expectedUserInfo.setCountry("ES");
        when(userInfoService.getUserInfo()).thenReturn(expectedUserInfo);
        when(officeInfoService.getMiddleOffice("1234")).thenReturn("8911");
        // When
        requestController.injectRequestInfo(request);
        //Then
        assertEquals(expectedUserInfo, request.getRequester());
        assertEquals("ES", request.getCountry());
        assertEquals("8911", request.getMiddleOffice());
    }

    @ParameterizedTest
    @CsvSource(value = {"null, taskId",
            "complete-info-complete, null",
            "complete-info-cancel, null",
            "unknown, taskId"}
            , nullValues={"null"})
    void putSwitch_missingParam_thenThrowInvalidRequestException(String type, String taskId) {
        IRequestController controller = mock(IRequestController.class);
        CommonRequestDto requestDto = new CommonRequestDto();
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () ->
                requestController.putSwitch(controller, requestDto, type, taskId));
        assertEquals("paramError", exception.getKey());
    }

    @ParameterizedTest
    @CsvSource(value = {"confirm, null",
            "complete-info-complete, taskId"}
            , nullValues={"null"})
    void putSwitch_missingBody_thenThrowInvalidRequestException(String type, String taskId) {
        IRequestController controller = mock(IRequestController.class);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () ->
                requestController.putSwitch(controller, null, type, taskId));
        assertEquals("payloadError", exception.getKey());
        assertEquals("The body of the request cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {"null, caseId, taskId",
            "draft, null, taskId",
            "complete-info, caseId, null",
            "details, null, taskId",
            "unknown, caseId, taskId"}
            , nullValues={"null"})
    void getSwitch_missingParam_thenThrowInvalidRequestException(String type, String caseId, String taskId) {
        IRequestController controller = mock(IRequestController.class);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () ->
                requestController.getSwitch(controller, type, caseId, taskId, false));
        assertEquals("paramError", exception.getKey());
    }

    @Test
    void confirm_ok_throwUnsupportedOperationException() {
        CommonRequestDto commonRequestDto = new CommonRequestDto();
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                requestController.confirm(commonRequestDto));
        assertEquals("There is no implementation for this operation", exception.getMessage());
    }

    @Test
    void completeCompleteInfo_ok_throwUnsupportedOperationException() {
        CommonRequestDto commonRequestDto = new CommonRequestDto();
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                requestController.completeCompleteInfo("taskId", commonRequestDto));
        assertEquals("There is no implementation for this operation", exception.getMessage());
    }

    @Test
    void cancelCompleteInfo_ok_throwUnsupportedOperationException() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                requestController.cancelCompleteInfo("taskId"));
        assertEquals("There is no implementation for this operation", exception.getMessage());
    }

    @Test
    void getDraft_ok_throwUnsupportedOperationException() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                requestController.getDraft("caseId"));
        assertEquals("There is no implementation for this operation", exception.getMessage());
    }

    @Test
    void getCompleteInfo_ok_throwUnsupportedOperationException() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                requestController.getCompleteInfo("taskId", false));
        assertEquals("There is no implementation for this operation", exception.getMessage());
    }

    @Test
    void getCaseDetails_ok_throwUnsupportedOperationException() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () ->
                requestController.getCaseDetails("caseId"));
        assertEquals("There is no implementation for this operation", exception.getMessage());
    }
}
