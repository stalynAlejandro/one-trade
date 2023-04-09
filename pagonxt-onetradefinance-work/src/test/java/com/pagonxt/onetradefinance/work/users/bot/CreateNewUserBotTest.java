package com.pagonxt.onetradefinance.work.users.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.core.idm.api.*;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.users.utils.UserCreationMessageUtils;
import org.flowable.idm.api.UserQuery;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.work.users.bot.CreateNewUserBot.PAYLOAD_LOGIN_NAME;
import static com.pagonxt.onetradefinance.work.users.bot.CreateNewUserBot.RESPONSE_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@UnitTest
class CreateNewUserBotTest {

    @Mock
    ObjectMapper objectMapper;

    @Mock
    PlatformIdentityService platformIdentityService;

    @Mock
    UserCreationMessageUtils userCreationMessageUtils;

    @Mock
    UserSecurityService userSecurityService;

    @InjectMocks
    CreateNewUserBot createNewUserBot;

    @Test
    void invokeBot_loginNameInUse_responsePayloadErrorMessage() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        Map<String, Object> payload = new HashMap<>();
        payload.put(PAYLOAD_LOGIN_NAME, "test_login");

        UserQuery userQuery = mock(UserQuery.class);
        doReturn(userQuery).when(platformIdentityService).createUserQuery();
        doReturn(userQuery).when(userQuery).userId((String) payload.get(PAYLOAD_LOGIN_NAME));
        doReturn(1L).when(userQuery).count();

        Response response = mock(Response.class);
        response.setResponse("messageEN", "loginFail");
        response.setResponse("messageES", "falloLogin");
        response.setSuccess(false);
        doReturn(response).when(userCreationMessageUtils).allLanguagesLoginMsgError((String) payload.get(PAYLOAD_LOGIN_NAME));

        ObjectNode objectNode = mock(ObjectNode.class);
        doReturn(objectNode).when(objectMapper).createObjectNode();

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        BotActionResult botActionResult = createNewUserBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        ObjectNode responsePayload = objectMapper.createObjectNode();
        UserCreationMessageUtils userCreationMessageUtils = new UserCreationMessageUtils();
        userCreationMessageUtils.allLanguagesLoginMsgError((String) payload.get(PAYLOAD_LOGIN_NAME));
        responsePayload.set(RESPONSE_MESSAGE, objectMapper.valueToTree(userCreationMessageUtils));
        BaseBotActionResult expectedResult = new BaseBotActionResult(responsePayload, Intent.NOOP);

        assertThat(botActionResult.getPayloadNode()).isEqualTo(expectedResult.getPayloadNode());
    }

    @Test
    void invokeBot_createUser_responsePayloadSuccessfulMessage() {
        // Given
        HistoricActionInstance historicActionInstance = mock(HistoricActionInstance.class);
        ActionDefinition actionDefinition = mock(ActionDefinition.class);

        Map<String, Object> payload = Map.of(PAYLOAD_LOGIN_NAME, "test_login",
                "displayName", "displayName",
                "firstName", "firstName",
                "lastName", "lastName",
                "email", "email",
                "userProfile", "userManagement");

        UserQuery userQuery = mock(UserQuery.class);
        doReturn(userQuery).when(platformIdentityService).createUserQuery();
        doReturn(userQuery).when(userQuery).userId((String) payload.get(PAYLOAD_LOGIN_NAME));
        doReturn(0L).when(userQuery).count();

        PlatformUserBuilder platformUserBuilder = mock(PlatformUserBuilder.class);

        doReturn(platformUserBuilder).when(platformIdentityService).createNewUserBuilder((String) payload.get(PAYLOAD_LOGIN_NAME));
        doReturn(platformUserBuilder).when(platformUserBuilder).displayName("displayName");
        doReturn(platformUserBuilder).when(platformUserBuilder).firstName("firstName");
        doReturn(platformUserBuilder).when(platformUserBuilder).lastName("lastName");
        doReturn(platformUserBuilder).when(platformUserBuilder).email("email");
        doReturn(platformUserBuilder).when(platformUserBuilder).theme("Santander");
        doReturn(platformUserBuilder).when(platformUserBuilder).userDefinitionKey("user-oneTradeFinance");
        PlatformUser platformUser = mock(PlatformUser.class);
        doReturn("newUserId").when(platformUser).getId();
        doReturn(platformUser).when(platformUserBuilder).save();

        PlatformIdentityInfoBuilder platformIdentityInfoBuilder = mock(PlatformIdentityInfoBuilder.class);
        PlatformIdentityInfo platformIdentityInfos = mock(PlatformIdentityInfo.class);
        List<PlatformIdentityInfo> platformIdentityInfoList = List.of(platformIdentityInfos);
        doReturn(platformIdentityInfoBuilder).when(platformIdentityService).createPlatformIdentityInfoBuilder();
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).userId(any());
        doReturn(platformIdentityInfoBuilder).when(platformIdentityInfoBuilder).info(any(), any());
        doReturn(platformIdentityInfoList).when(platformIdentityInfoBuilder).save();

        PlatformGroupQuery platformGroupQuery = mock(PlatformGroupQuery.class);
        PlatformGroup platformGroup = mock(PlatformGroup.class);
        doReturn(platformGroupQuery).when(platformIdentityService).createPlatformGroupQuery();
        doReturn(platformGroupQuery).when(platformGroupQuery).groupId(any());
        doReturn(platformGroup).when(platformGroupQuery).singleResult();

        Response response = mock(Response.class);
        response.setResponse("messageEN", "userCreated");
        response.setResponse("messageES", "usuarioCreado");
        response.setSuccess(true);
        doReturn(response).when(userCreationMessageUtils).userCreated();

        ObjectNode objectNode = mock(ObjectNode.class);
        doReturn(objectNode).when(objectMapper).createObjectNode();

        doReturn(true).when(userSecurityService).canManageUsers();

        // When
        BotActionResult botActionResult = createNewUserBot.invokeBot(historicActionInstance, actionDefinition, payload);

        // Then
        ObjectNode responsePayload = objectMapper.createObjectNode();
        UserCreationMessageUtils userCreationMessageUtils = new UserCreationMessageUtils();
        userCreationMessageUtils.userCreated();
        responsePayload.set(RESPONSE_MESSAGE, objectMapper.valueToTree(userCreationMessageUtils));
        BaseBotActionResult expectedResult = new BaseBotActionResult(responsePayload, Intent.NOOP);

        assertThat(botActionResult.getPayloadNode()).isEqualTo(expectedResult.getPayloadNode());
    }
}
