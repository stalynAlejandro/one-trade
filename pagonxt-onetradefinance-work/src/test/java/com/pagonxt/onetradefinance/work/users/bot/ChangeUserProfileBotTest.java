package com.pagonxt.onetradefinance.work.users.bot;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.core.idm.api.PlatformIdentityInfo;
import com.flowable.core.idm.api.PlatformIdentityInfoBuilder;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.users.service.RecalculateFlowableGroupsService;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@UnitTest
class ChangeUserProfileBotTest {

    @Mock
    PlatformIdentityService platformIdentityService;

    @Mock
    RecalculateFlowableGroupsService recalculateFlowableGroupsService;

    @Mock
    UserSecurityService userSecurityService;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    ChangeUserProfileBot changeUserProfileBot;

    @ParameterizedTest
    @ValueSource(strings = {"User Management", "Back Office"})
    void invokerBot_withTwoUsersProfiles_createsAndDeletesMembership(String userProfile) {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        Map<String, Object> selectedUser = Map.of("id", "test.user");
        Map<String, Object> userProfiles = Map.of("value", userProfile);
        Map<String, Object> payload = Map.of("selectedUser", selectedUser,
                "selectedUserProfile", userProfiles);

        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        PlatformIdentityInfo platformIdentityInfos = mock(PlatformIdentityInfo.class);
        List<PlatformIdentityInfo> platformIdentityInfoList = List.of(platformIdentityInfos);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(any());
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(any(), any());
        doReturn(platformIdentityInfoList).when(platformIdentityInfoBuilder).save();


        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        BotActionResult botActionResult = changeUserProfileBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        Map<String, Object> botResultMap = Map.of("stateUserModified", true);
        JsonNode botResult = objectMapper.valueToTree(botResultMap);
        BaseBotActionResult expectedResult = new BaseBotActionResult(botResult, Intent.NOOP);

        assertThat(botActionResult.getPayloadNode()).isEqualTo(expectedResult.getPayloadNode());
    }

}
