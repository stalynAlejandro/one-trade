package com.pagonxt.onetradefinance.external.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class UserInfoServiceJWTImplTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    UserInfoServiceJWTImpl userInfoServiceJWTImpl;

    @Mock
    HttpServletRequest httpServletRequest;

    @Test
    void getUserInfo_whenUserInfoFound_thenReturnUserInfo() throws IOException {
        // Given
        UserInfo expectedUserInfo = mapper.readValue(new ClassPathResource("data/model/user-info.json").getFile(), UserInfo.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("userInfo")).thenReturn(expectedUserInfo);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        // When
        UserInfo result = userInfoServiceJWTImpl.getUserInfo();
        // Then
        assertEquals(expectedUserInfo, result);
    }

    @Test
    void getUserInfo_whenUserInfoNotFound_thenReturnUserInfo() {
        // Given
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("userInfo")).thenReturn(null);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> userInfoServiceJWTImpl.getUserInfo());
        // Then
        assertEquals("Error retrieving the user info", exception.getMessage());
        assertEquals("getUserInfoError", exception.getKey());
    }

    @Test
    void getUserInfoWithLocale_whenUserInfoFound_thenReturnUserInfo() throws IOException {
        // Given
        UserInfo expectedUserInfo = mapper.readValue(new ClassPathResource("data/model/user-info.json").getFile(), UserInfo.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("userInfo")).thenReturn(expectedUserInfo);
        when(httpServletRequest.getSession()).thenReturn(httpSession);
        // When
        UserInfo result = userInfoServiceJWTImpl.getUserInfo("es_es");
        // Then
        assertEquals("es_es", result.getLocale());
    }

}
