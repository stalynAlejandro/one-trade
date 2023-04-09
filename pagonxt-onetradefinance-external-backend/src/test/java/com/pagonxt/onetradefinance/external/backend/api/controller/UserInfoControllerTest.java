package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.UserInfoDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.UserInfoDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(UserInfoController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class UserInfoControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/user-info";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserInfoService userInfoService;

    @MockBean
    UserInfoDtoSerializer userInfoDtoSerializer;

    @Test
    void getUserInfo_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .with(httpBasic("user", "user")))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void getUserInfo_noAuth_returnsUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getUserInfo_ok_returnsValidData() throws Exception {
        // Given
        UserInfo userInfo = new UserInfo();
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId("testId");
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userInfoDtoSerializer.toDto(userInfo)).thenReturn(userInfoDto);
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("userInfoFound", resultJsonNode.get("key").asText());
        assertEquals("testId", resultJsonNode.get("entity").get("userId").asText());
    }
}
