package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.core.idm.api.PlatformGroup;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.PlatformUser;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class CustomSecurityScopeTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    PlatformIdentityService platformIdentityService;

    @Test
    void whenGetUserId_returnsValidValue() {
        // Given
        String name = "User";
        Authentication authentication = composeAuthentication(name);
        CustomSecurityScope customSecurityScope = new CustomSecurityScope(authentication, platformIdentityService);

        // When
        String result = customSecurityScope.getUserId();

        // Then
        assertEquals(name, result, "Should return valid data");
    }

    @Test
    void whenGetGroupKeys_returnsValidData() {
        // Given
        String name = "User";
        Authentication authentication = composeAuthentication(name);
        PlatformGroup platformGroup = mock(PlatformGroup.class);
        when(platformGroup.getKey()).thenReturn("group");
        when(platformIdentityService.createPlatformGroupQuery().groupMember(any()).list()).thenReturn(List.of(platformGroup));
        CustomSecurityScope customSecurityScope = new CustomSecurityScope(authentication, platformIdentityService);
        Set<String> expectedResult = new HashSet<>();
        expectedResult.add("group");

        // When
        Set<String> result = customSecurityScope.getGroupKeys();

        // Then
        assertEquals(expectedResult, result, "Should return valid data");
    }

    @Test
    void whenGetTenantId_returnsNull() {
        // Given
        Authentication authentication = composeAuthentication(null);
        CustomSecurityScope customSecurityScope = new CustomSecurityScope(authentication, platformIdentityService);

        // When and then
        assertNull(customSecurityScope.getTenantId(), "Should return a null value");
    }

    @Test
    void whenGetUserDefinitionKey_returnsValidValue() {
        // Given
        String name = "User";
        String key = "user";
        Authentication authentication = composeAuthentication(name);
        PlatformUser user = mock(PlatformUser.class);
        when(user.getUserDefinitionKey()).thenReturn(key);
        when(platformIdentityService.createPlatformUserQuery().userId(any()).singleResult()).thenReturn(user);
        CustomSecurityScope customSecurityScope = new CustomSecurityScope(authentication, platformIdentityService);

        // When
        String result = customSecurityScope.getUserDefinitionKey();

        // Then
        assertEquals(key, result, "Should return a valid value");
    }

    @Test
    void whenHasAuthority_returnsFalse() {
        // Given
        Authentication authentication = composeAuthentication(null);
        CustomSecurityScope customSecurityScope = new CustomSecurityScope(authentication, platformIdentityService);

        // When and then
        assertFalse(customSecurityScope.hasAuthority("any"), "Should return false");
    }

    private Authentication composeAuthentication(String name) {
        Authentication result = mock(Authentication.class);
        if (name != null) {
            when(result.getName()).thenReturn("User");
        }
        return result;
    }
}
