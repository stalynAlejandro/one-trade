package com.pagonxt.onetradefinance.integrations.apis.common.security.service.impl;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@UnitTest
class JWTServiceImplTest {

    @InjectMocks
    JWTServiceImpl jwtService;
    @Mock
    HttpServletRequest httpServletRequest;

    @Test
    void getAccessToken_ok_returnAccessToken() {
        // Given
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer testJWTToken");
        // When
        String result = jwtService.getToken();
        // Then
        assertEquals("testJWTToken", result);
    }
}
