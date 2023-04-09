package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.User;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@UnitTest
class UserServiceBasicImplTest {

    private final UserService userService = new UserServiceBasicImpl();

    @Test
    void getCurrentUserCountry_ok_returnsSpain() {
        // Given
        // When
        String result = userService.getCurrentUserCountry();

        // Then
        assertEquals("ES", result, "Expected ES");
    }

    @ParameterizedTest
    @MethodSource("provideValidUserOriginArguments")
    void getRequester_validUser_returnsCorrectValue(String username, String userRole, String expectedOrigin) {
        // Given
        initContextWithUser(username, userRole);
        // When
        User result = userService.getCurrentUser();
        // Then
        assertEquals(username, result.getUserId(), "Expected the right user id based on their user name");
        assertEquals(StringUtils.capitalize(username), result.getUserDisplayedName(), "Expected capitalized username as displayedName");
        assertEquals(expectedOrigin, result.getUserType(), "Expected the right user type based on their role");
    }

    private static Stream<Arguments> provideValidUserOriginArguments() {
        return Stream.of(
                Arguments.of("backoffice_user", "ROLE_BACKOFFICE", "BO"),
                Arguments.of("middle-office_user", "ROLE_MIDDLE_OFFICE", "MO"),
                Arguments.of("office_user", "ROLE_OFFICE", "OFFICE"),
                Arguments.of("customer_user", "ROLE_CUSTOMER", "CUSTOMER")
        );
    }

    private void initContextWithUser(String username, String role) {
        initContextWithAuthorities(username, "ROLE_USER", role);
    }

    private void initContextWithAuthorities(String username, String... authorities) {
        SecurityContext securityContext = new SecurityContextImpl();
        Authentication authentication = new TestingAuthenticationToken(username, null, authorities);
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
    }
}
