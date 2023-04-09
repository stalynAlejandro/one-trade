package com.pagonxt.onetradefinance.work.users.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.UserStates;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.utils.BotUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Map;

import static com.pagonxt.onetradefinance.work.users.bot.ActivateDeactivateUserBot.PAYLOAD_STATE;
import static org.mockito.Mockito.*;

@UnitTest
class ActivateDeactivateUserBotTest {

    @Mock
    ObjectMapper objectMapper;

    @Mock
    PlatformIdentityService platformIdentityService;

    @Mock
    UserSecurityService userSecurityService;

    @Mock
    BotUtils botUtils;

    @InjectMocks
    ActivateDeactivateUserBot activateDeactivateUserBot;

    @Test
    void invokeBot_userStateInactive_userStateToActive() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);
        Map<String, Object> selectedUser = Map.of("id", "test.user",
                PAYLOAD_STATE, UserStates.INACTIVE);
        Map<String, Object> payload = Map.of("selectedUser", selectedUser);
        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        activateDeactivateUserBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        verify(platformIdentityService).setUserState("test.user", UserStates.ACTIVE);
    }
}
