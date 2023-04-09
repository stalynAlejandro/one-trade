package com.pagonxt.onetradefinance.integrations.configuration;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.service.TokenGeneratorService;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class AppAuthenticationJWTFilterTest {

    private static final String jwtSecret = "242a1b16cf2ba9a6a09715782c1bb675162355f1ade92ce169fb583e2b47aeea";
    private static final Key key = getKey();
    private static final AppAuthenticationJWTFilter filter = new AppAuthenticationJWTFilter(key);
    private static final TokenGeneratorService tokenGeneratorService = new TokenGeneratorService(key);

    @Test
    void givenRequestWithoutHeader_whenDoFilterInternal_thenContextRemainsEmpty() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        SecurityContextHolder.clearContext();

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        assertNull(SecurityContextHolder.getContext().getAuthentication(), "Authentication should be null");
    }

    @Test
    void givenRequestWithValidHeader_whenDoFilterInternal_thenContextCreated() throws ServletException, IOException {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("internalAppHeader", tokenGeneratorService.generateJWTToken("username"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        SecurityContextHolder.clearContext();

        // When
        filter.doFilterInternal(request, response, filterChain);

        // Then
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(),
                "Authentication should not be null");
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof AppAuthentication,
                "Authentication should be of type AppAuthentication");
        assertEquals("username",
                ((AppAuthentication) (SecurityContextHolder.getContext().getAuthentication())).getUserName(),
                "Authentication should have a valid username");
    }

    @Test
    void givenRequestWithInvalidHeader_whenDoFilterInternal_thenServiceExceptionThrown() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("internalAppHeader", "InvalidJwtToken");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = Mockito.mock(FilterChain.class);
        SecurityContextHolder.clearContext();

        // When
        ServiceException thrown = Assertions.assertThrows(ServiceException.class,
                () -> filter.doFilterInternal(request, response, filterChain),
                "Should throw ServiceException");

        // Then
        assertEquals("Invalid jwt token", thrown.getMessage(),
                "Exception should have a valid message");
        assertEquals("errorInternalCommunication", thrown.getKey(),
                "Exception should have a valid key");
    }

    private static Key getKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
                SignatureAlgorithm.HS256.getJcaName());
    }
}
