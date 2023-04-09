package com.pagonxt.onetradefinance.external.backend.configuration;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@UnitTest
class TokenInterceptorTest {
    @Mock
    JWTValidation jwtValidation;
    @Mock
    OfficeInfoService officeInfoService;

    @Test
    void preHandle_officeUser_returnTrue() {
        // Given
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtValidation, officeInfoService, false);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        mockRequest(request, "testSubOffice", "ES1234");
        when(officeInfoService.isValidOffice("1234")).thenReturn(true);
        // When
        boolean result = tokenInterceptor.preHandle(request, mock(HttpServletResponse.class), mock(Object.class));
        // Then
        assertTrue(result);
        ArgumentCaptor<UserInfo> argumentCaptor = ArgumentCaptor.forClass(UserInfo.class);
        verify(session).setAttribute(eq("userInfo"), argumentCaptor.capture());
        UserInfo userInfoResult = argumentCaptor.getValue();
        assertEquals("testSubOffice", userInfoResult.getUser().getUserId());
        assertEquals("testSubOffice", userInfoResult.getUser().getUserDisplayedName());
        assertEquals("OFFICE", userInfoResult.getUser().getUserType());
        assertEquals("ES", userInfoResult.getCountry());
        assertEquals("1234", userInfoResult.getOffice());
        assertNull(userInfoResult.getMiddleOffice());
    }

    @Test
    void preHandle_middleOfficeUser_returnTrue() {
        // Given
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtValidation, officeInfoService, false);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        mockRequest(request,"testSubMiddleOffice", "ES8911");
        when(officeInfoService.isValidOffice("8911")).thenReturn(false);
        when(officeInfoService.isValidMiddleOffice("8911")).thenReturn(true);
        // When
        boolean result = tokenInterceptor.preHandle(request, mock(HttpServletResponse.class), mock(Object.class));
        // Then
        assertTrue(result);
        ArgumentCaptor<UserInfo> argumentCaptor = ArgumentCaptor.forClass(UserInfo.class);
        verify(session).setAttribute(eq("userInfo"), argumentCaptor.capture());
        UserInfo userInfoResult = argumentCaptor.getValue();
        assertEquals("testSubMiddleOffice", userInfoResult.getUser().getUserId());
        assertEquals("testSubMiddleOffice", userInfoResult.getUser().getUserDisplayedName());
        assertEquals("MO", userInfoResult.getUser().getUserType());
        assertEquals("ES", userInfoResult.getCountry());
        assertNull(userInfoResult.getOffice());
        assertEquals("8911", userInfoResult.getMiddleOffice());
    }

    @Test
    void preHandle_backOfficeUser_returnTrue() {
        // Given
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtValidation, officeInfoService, true);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        mockRequest(request, "testSubBackOffice", "ESBSCHESMM");
        // When
        boolean result = tokenInterceptor.preHandle(request, mock(HttpServletResponse.class), mock(Object.class));
        // Then
        assertTrue(result);
        ArgumentCaptor<UserInfo> argumentCaptor = ArgumentCaptor.forClass(UserInfo.class);
        verify(session).setAttribute(eq("userInfo"), argumentCaptor.capture());
        UserInfo userInfoResult = argumentCaptor.getValue();
        assertEquals("testSubBackOffice", userInfoResult.getUser().getUserId());
        assertEquals("testSubBackOffice", userInfoResult.getUser().getUserDisplayedName());
        assertEquals("BO", userInfoResult.getUser().getUserType());
        assertEquals("ES", userInfoResult.getCountry());
        assertNull(userInfoResult.getOffice());
        assertNull(userInfoResult.getMiddleOffice());
    }

    @Test
    void preHandle_blankUserName_thenThrowServiceException() {
        // Given
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtValidation, officeInfoService, false);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testJWTToken");
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(jwtValidation.verify("Bearer testJWTToken")).thenReturn(decodedJWT);
        Claim claimSub = mock(Claim.class);
        when(claimSub.asString()).thenReturn("");
        when(decodedJWT.getClaim("sub")).thenReturn(claimSub);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () ->
                tokenInterceptor.preHandle(request, mock(HttpServletResponse.class), mock(Object.class)));
        // Then
        assertEquals("errorToken", exception.getKey());
        assertEquals("The authorization header is not correct", exception.getMessage());
    }

    @Test
    void preHandle_incorrectCountry_thenThrowServiceException() {
        // Given
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtValidation, officeInfoService, false);
        HttpServletRequest request = mock(HttpServletRequest.class);
        mockRequest(request, "testSub", "E");
        // When
        ServiceException exception = assertThrows(ServiceException.class, () ->
                tokenInterceptor.preHandle(request, mock(HttpServletResponse.class), mock(Object.class)));
        // Then
        assertEquals("errorToken", exception.getKey());
        assertEquals("The authorization header is not correct", exception.getMessage());
    }

    @Test
    void preHandle_incorrectOffice_returnTrue() {
        // Given
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtValidation, officeInfoService, false);
        HttpServletRequest request = mock(HttpServletRequest.class);
        mockRequest(request,"testSubMiddleOffice", "ES9999");
        when(officeInfoService.isValidOffice("9999")).thenReturn(false);
        when(officeInfoService.isValidMiddleOffice("9999")).thenReturn(false);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () ->
                tokenInterceptor.preHandle(request, mock(HttpServletResponse.class), mock(Object.class)));
        // Then
        assertEquals("errorToken", exception.getKey());
        assertEquals("The authorization header is not correct", exception.getMessage());
    }

    private void mockRequest(HttpServletRequest request, String sub, String country) {
        when(request.getHeader("Authorization")).thenReturn("Bearer testJWTToken");
        DecodedJWT decodedJWT = mock(DecodedJWT.class);
        when(jwtValidation.verify("Bearer testJWTToken")).thenReturn(decodedJWT);
        Claim claimSub = mock(Claim.class);
        when(claimSub.asString()).thenReturn(sub);
        when(decodedJWT.getClaim("sub")).thenReturn(claimSub);
        if(country != null) {
            Claim claimCountry = mock(Claim.class);
            when(claimCountry.asString()).thenReturn(country);
            when(decodedJWT.getClaim("country")).thenReturn(claimCountry);
        }
    }

}
