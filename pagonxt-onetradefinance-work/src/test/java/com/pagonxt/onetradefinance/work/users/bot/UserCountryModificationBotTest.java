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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

@UnitTest
class UserCountryModificationBotTest {

    private static final String TEST_USER_ID = "user_test";
    private static final String TEST_COUNTRY = "country.test";

    private static final String TEST_PAYLOAD_COUNTRIES = "countries";
    private static final String TEST_PAYLOAD_ID = "id";
    private static final String TEST_PAYLOAD_IDENTITY_INFO = "identityInfo";

    @Spy
    ObjectMapper objectMapper;

    @Mock
    PlatformIdentityService platformIdentityService;

    @Mock
    @SuppressWarnings("unused") // It is actually used
    RecalculateFlowableGroupsService recalculateFlowableGroups;

    @Mock
    UserSecurityService userSecurityService;

    @Mock
    BotUtils botUtils;

    @InjectMocks
    UserCountryModificationBot userCountryModificationBot;

    @Test
    void invokeBot_groupsAndRolesDontExists_addsCountries() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String userId = TEST_USER_ID;
        ArrayList<String> countries = new ArrayList<>(List.of(TEST_COUNTRY));
        ArrayList<Map<String, Object>> identityInfo = new ArrayList<>();

        Map<String, Object> payload = Map.of(TEST_PAYLOAD_COUNTRIES, countries,
                TEST_PAYLOAD_ID, userId,
                TEST_PAYLOAD_IDENTITY_INFO, identityInfo);

        JsonNode usersCountries = objectMapper.valueToTree(countries);
        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(userId);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(TEST_PAYLOAD_COUNTRIES, usersCountries);
        doReturn(null).when(platformIdentityInfoBuilder).save();

        when(botUtils.getIdentityInfo(any(), any())).thenReturn(identityInfo);

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        userCountryModificationBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(platformIdentityInfoBuilder).info(TEST_PAYLOAD_COUNTRIES, usersCountries);
    }

    @Test
    void invokeBot_groupsAndRolesIsEmpty_addsCountries() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String userId = TEST_USER_ID;
        ArrayList<String> countries = new ArrayList<>(List.of(TEST_COUNTRY));
        Map<String, Object> mapIdentity = Map.of("value", new ArrayList<String>(),
                "name", "groupsAndRoles");
        ArrayList<Map<String, Object>> identityInfo = new ArrayList<>();
        identityInfo.add(mapIdentity);

        Map<String, Object> payload = new HashMap<>();
        payload.put(TEST_PAYLOAD_COUNTRIES, countries);
        payload.put(TEST_PAYLOAD_ID, userId);
        payload.put(TEST_PAYLOAD_IDENTITY_INFO, identityInfo);

        JsonNode usersCountries = objectMapper.valueToTree(countries);
        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(userId);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(TEST_PAYLOAD_COUNTRIES, usersCountries);
        doReturn(null).when(platformIdentityInfoBuilder).save();

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        userCountryModificationBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(platformIdentityInfoBuilder).info(TEST_PAYLOAD_COUNTRIES, usersCountries);
    }

    @Test
    void invokeBot_groupsAndRolesAssigned_addsCountries() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        String userId = TEST_USER_ID;
        ArrayList<String> countries = new ArrayList<>(List.of(TEST_COUNTRY));
        Map<String, Object> groupsAndRoles = Map.of("group", "CLE_REQUEST_BOI_01",
                "role", "user");
        ArrayList<Map<String, Object>> groupsRolesValue = new ArrayList<>();
        groupsRolesValue.add(groupsAndRoles);
        Map<String, Object> mapIdentity = Map.of("value", groupsRolesValue,
                "name", "groupsAndRoles");
        ArrayList<Map<String, Object>> identityInfo = new ArrayList<>();
        identityInfo.add(mapIdentity);

        Map<String, Object> payload = new HashMap<>();
        payload.put(TEST_PAYLOAD_COUNTRIES, countries);
        payload.put(TEST_PAYLOAD_ID, userId);
        payload.put(TEST_PAYLOAD_IDENTITY_INFO, identityInfo);

        JsonNode usersCountries = objectMapper.valueToTree(countries);
        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(userId);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(TEST_PAYLOAD_COUNTRIES, usersCountries);
        doReturn(null).when(platformIdentityInfoBuilder).save();

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        userCountryModificationBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(platformIdentityInfoBuilder).info(TEST_PAYLOAD_COUNTRIES, usersCountries);
    }
}
