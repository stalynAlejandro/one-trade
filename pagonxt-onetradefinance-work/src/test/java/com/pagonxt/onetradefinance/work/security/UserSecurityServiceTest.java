package com.pagonxt.onetradefinance.work.security;

import com.flowable.core.idm.api.PlatformIdentityService;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.provider.UserIdProvider;
import org.flowable.idm.api.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class UserSecurityServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private PlatformIdentityService platformIdentityService;

    @Mock
    private UserIdProvider userIdProvider;

    @InjectMocks
    private UserSecurityService userSecurityService;

    @ParameterizedTest
    @MethodSource("canManageUsers_ok_arguments")
    void privateCanManageUsers_ok_returnsValidResponse(String userId, List<Group> userGroups, Boolean expectedResponse) {
        // Given
        when(platformIdentityService.createGroupQuery().groupMember(eq(userId)).list()).thenReturn(userGroups);

        // When
        boolean result = Boolean.TRUE.equals(ReflectionTestUtils.invokeMethod(userSecurityService, "canManageUsers", userId));

        // Then
        assertEquals(expectedResponse, result, "Result should match expected value");
    }

    @Test
    void checkCanManageUsers_ok_invokesUserIdProvider() {
        // Given
        when(userIdProvider.getCurrentUserId()).thenReturn("admin");
        List<Group> userGroups = List.of(group("flowableAdministrator"));
        when(platformIdentityService.createGroupQuery().groupMember(eq("admin")).list()).thenReturn(userGroups);

        // When
        userSecurityService.checkCanManageUsers();

        // Then
        verify(userIdProvider).getCurrentUserId();
    }

    @Test
    void checkCanManageUsers_unauthorized_throwsSecurityException() {
        // Given
        when(userIdProvider.getCurrentUserId()).thenReturn("nonAdmin");
        List<Group> userGroups = List.of(group("user"));
        when(platformIdentityService.createGroupQuery().groupMember(eq("nonAdmin")).list()).thenReturn(userGroups);

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> userSecurityService.checkCanManageUsers(),
                "Should throw SecurityException");

        // Then
        assertEquals("User nonAdmin has no permission to manage users", thrown.getMessage(), "Thrown exception should have a valid message");
    }

    @Test
    void canManageUsers_ok_returnsValidResponse() {
        // Given
        when(userIdProvider.getCurrentUserId()).thenReturn("admin");
        List<Group> userGroups = List.of(group("flowableAdministrator"));
        when(platformIdentityService.createGroupQuery().groupMember(eq("admin")).list()).thenReturn(userGroups);

        // When
        boolean result = userSecurityService.canManageUsers();

        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void canManageUsers_unauthorized_returnsValidResponse() {
        // Given
        when(userIdProvider.getCurrentUserId()).thenReturn("user");
        List<Group> userGroups = List.of(group("nonAdmin"));
        when(platformIdentityService.createGroupQuery().groupMember(eq("user")).list()).thenReturn(userGroups);

        // When
        boolean result = userSecurityService.canManageUsers();

        // Then
        assertFalse(result, "Result should be false");
    }

    @Test
    void userCanManageUsers_ok_returnsValidResponse() {
        // Given
        List<Group> userGroups = List.of(group("flowableAdministrator"));
        when(platformIdentityService.createGroupQuery().groupMember(eq("admin")).list()).thenReturn(userGroups);

        // When
        boolean result = userSecurityService.canManageUsers("admin");

        // Then
        assertTrue(result, "Result should be true");
    }

    @Test
    void userCanManageUsers_unauthorized_returnsValidResponse() {
        // Given
        List<Group> userGroups = List.of(group("nonAdmin"));
        when(platformIdentityService.createGroupQuery().groupMember(eq("user")).list()).thenReturn(userGroups);

        // When
        boolean result = userSecurityService.canManageUsers("user");

        // Then
        assertFalse(result, "Result should be false");
    }

    private static Stream<Arguments> canManageUsers_ok_arguments() {
        return Stream.of(
                Arguments.of("Marta", List.of(group("user"), group("userManagement")), true),
                Arguments.of("Elena", List.of(group("flowableAdministrator")), true),
                Arguments.of("Borja", List.of(group("somethingElse")), false),
                Arguments.of("Concha", List.of(), false)
        );
    }

    private static Group group(String name) {
        Group result = mock(Group.class);
        when(result.getId()).thenReturn(name);
        return result;
    }
}
