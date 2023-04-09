package com.pagonxt.onetradefinance.work.api.work;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.core.common.rest.response.ResponseEntityHelper;
import com.flowable.core.idm.api.PlatformIdentityService;
import com.flowable.core.idm.api.PlatformUser;
import com.flowable.core.idm.api.PlatformUserQuery;
import com.flowable.core.idm.api.UserDefinitionService;
import com.flowable.idm.dto.PlatformUserResponse;
import com.flowable.idm.rest.service.api.IdmRestResponseFactory;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.security.UserSecurityService;
import com.pagonxt.onetradefinance.work.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlatformIdentityService platformIdentityService;

    @MockBean
    ResponseEntityHelper responseEntityHelper;

    @MockBean
    UserSecurityService userSecurityService;

    @MockBean
    UserDefinitionService userDefinitionService;

    @MockBean
    IdmRestResponseFactory idmRestResponseFactory;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getUsersByFilter_isOk() throws Exception {
        // Given
        PlatformUserQuery userQuery = mock(PlatformUserQuery.class);
        doReturn(userQuery).when(platformIdentityService).createPlatformUserQuery();

        List<PlatformUser> platformUserList = new ArrayList<>();
        doReturn(platformUserList).when(userQuery).list();
        doReturn(platformUserList).when(userService).getUsersByUserProfile(any(), any());
        List<PlatformUserResponse> platformUserResponseList = new ArrayList<>();
        doReturn(platformUserResponseList).when(idmRestResponseFactory).createUserResponseList(platformUserList, true);

        // When
        this.mockMvc.perform(get("/users")
                        .param("login", "admin")
                        .param("displayName", "displayName")
                        .param("email", "email")
                        .param("status", "status")
                        .param("userProfile", "userProfile"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userQuery).userId("admin");
        verify(userQuery).userDisplayNameLikeIgnoreCase("displayName");
        verify(userQuery).userEmailLike("email");
        verify(userQuery).state("status");
    }


    @Test
    void getUsersByFilter_whenNullParams_isOk() throws Exception {
        // Given
        PlatformUserQuery userQuery = mock(PlatformUserQuery.class);
        doReturn(userQuery).when(platformIdentityService).createPlatformUserQuery();

        List<PlatformUser> platformUserList = new ArrayList<>();
        doReturn(platformUserList).when(userQuery).list();
        doReturn(platformUserList).when(userService).getUsersByUserProfile(any(), any());
        List<PlatformUserResponse> platformUserResponseList = new ArrayList<>();
        doReturn(platformUserResponseList).when(idmRestResponseFactory).createUserResponseList(platformUserList, true);

        // When
        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userQuery, never()).userId(any());
        verify(userQuery, never()).userDisplayNameLikeIgnoreCase(any());
        verify(userQuery, never()).userEmailLike(any());
        verify(userQuery, never()).state(any());
    }

    @Test
    void getUsersAvailable_returnsList() throws Exception {
        // Given
        String groups = "grpRes_ESP_XXX_User";
        PlatformUserQuery platformUserQueryPos = mock(PlatformUserQuery.class);
        List<PlatformUser> possibleList = new ArrayList<>();
        PlatformUser platformUser = mock(PlatformUser.class);
        doReturn("userId").when(platformUser).getId();
        doReturn("ACTIVE").when(platformUser).getState();
        possibleList.add(platformUser);
        doReturn(platformUserQueryPos).when(platformIdentityService).createPlatformUserQuery();
        doReturn(platformUserQueryPos).when(platformUserQueryPos).memberOfGroup(any());
        doReturn(possibleList).when(platformUserQueryPos).list();

        // When
        MvcResult result = this.mockMvc.perform(get("/users/available-by-group/{groups}", groups))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        assertThat(result.getResponse().getContentAsString()).isNotNull();
    }
}
