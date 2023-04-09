package com.pagonxt.onetradefinance.work.users.bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.core.idm.api.PlatformIdentityInfoBuilder;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.users.service.RecalculateFlowableGroupsService;
import com.pagonxt.onetradefinance.work.utils.BotUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@UnitTest
class UserGroupModificationBotTest {
    private static final String TEST_USER_ID = "user_test";
    private static final String TEST_GROUPS_ROLES = "groupsRoles.test";

    private static final String TEST_PAYLOAD_GROUPS_ROLES = "groupsAndRoles";
    private static final String TEST_PAYLOAD_ID = "id";
    private static final String TEST_PAYLOAD_IDENTITY_INFO = "identityInfo";

    @Spy
    ObjectMapper objectMapper;

    @Mock
    PlatformIdentityService platformIdentityService;

    @Mock
    @SuppressWarnings("unused")
    RecalculateFlowableGroupsService recalculateFlowableGroups;

    @Mock
    UserSecurityService userSecurityService;

    @Mock
    BotUtils botUtils;

    @InjectMocks
    UserGroupModificationBot userGroupModificationBot;

    @Test
    void invokeBot_countriesVarDoesntExist_addsGroupsAndRoles() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String userId = TEST_USER_ID;
        ArrayList<String> groupsAndRoles = new ArrayList<>(List.of(TEST_GROUPS_ROLES));
        ArrayList<?> identityInfo = new ArrayList<>();

        Map<String, Object> payload = Map.of(TEST_PAYLOAD_GROUPS_ROLES, groupsAndRoles,
                TEST_PAYLOAD_ID, userId,
                TEST_PAYLOAD_IDENTITY_INFO, identityInfo);

        JsonNode usersGroups = objectMapper.valueToTree(groupsAndRoles);
        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(userId);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(TEST_PAYLOAD_GROUPS_ROLES, usersGroups);
        doReturn(null).when(platformIdentityInfoBuilder).save();

        when(botUtils.getIdentityInfo(any(), any())).thenReturn(List.of());

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        userGroupModificationBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(platformIdentityInfoBuilder).info(TEST_PAYLOAD_GROUPS_ROLES, usersGroups);
    }

    @Test
    void invokeBot_countriesIsEmpty_addsGroupsAndRoles() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String userId = TEST_USER_ID;
        ArrayList<String> groupsAndRoles = new ArrayList<>(List.of(TEST_GROUPS_ROLES));
        Map<String, Object> mapIdentity = Map.of("value", new ArrayList<>(),
                "name", "countries");
        ArrayList<Map<String, Object>> identityInfo = new ArrayList<>();
        identityInfo.add(mapIdentity);

        Map<String, Object> payload = Map.of(TEST_PAYLOAD_GROUPS_ROLES, groupsAndRoles,
                TEST_PAYLOAD_ID, userId,
                TEST_PAYLOAD_IDENTITY_INFO, identityInfo);

        JsonNode usersGroups = objectMapper.valueToTree(groupsAndRoles);
        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(userId);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(TEST_PAYLOAD_GROUPS_ROLES, usersGroups);
        doReturn(null).when(platformIdentityInfoBuilder).save();

        when(botUtils.getIdentityInfo(any(), any())).thenReturn(List.of(mapIdentity));

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        userGroupModificationBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(platformIdentityInfoBuilder).info(TEST_PAYLOAD_GROUPS_ROLES, usersGroups);
    }

    @Test
    void invokeBot_countriesAssigned_addsGroupsAndRoles() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String userId = TEST_USER_ID;
        ArrayList<String> groupsAndRoles = new ArrayList<>(List.of(TEST_GROUPS_ROLES));
        ArrayList<String> countriesValue = new ArrayList<>();
        countriesValue.add("ESP");
        Map<String, Object> mapIdentity = Map.of("value", countriesValue,
                "name", "countries");
        ArrayList<Map<String, Object>> identityInfo = new ArrayList<>();
        identityInfo.add(mapIdentity);

        Map<String, Object> payload = Map.of(TEST_PAYLOAD_GROUPS_ROLES, groupsAndRoles,
                TEST_PAYLOAD_ID, userId,
                TEST_PAYLOAD_IDENTITY_INFO, identityInfo);

        JsonNode usersGroups = objectMapper.valueToTree(groupsAndRoles);
        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(userId);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(TEST_PAYLOAD_GROUPS_ROLES, usersGroups);
        doReturn(null).when(platformIdentityInfoBuilder).save();

        when(botUtils.getIdentityInfo(any(), any())).thenReturn(List.of(mapIdentity));

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        userGroupModificationBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(platformIdentityInfoBuilder).info(TEST_PAYLOAD_GROUPS_ROLES, usersGroups);
    }
}
