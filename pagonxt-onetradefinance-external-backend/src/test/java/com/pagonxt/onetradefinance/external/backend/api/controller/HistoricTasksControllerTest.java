package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.HistoricTasksService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(HistoricTasksController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class HistoricTasksControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/retrieve-historic-tasks";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserInfoService userInfoService;

    @MockBean
    private HistoricTasksService historicTasksService;

    @Test
    void getHistoricTasks_ok_returnsOk() throws Exception {
        // Given, When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .param("case_id", "CLE-TEST")
                        .param("locale", "es_es")
                        .with(httpBasic("user", "user"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getHistoricTasks_ok_returnsHistoricTasksList() throws Exception {
        // Given
        HistoricTasksList historicTasksList = new HistoricTasksList();
        UserInfo userInfo = new UserInfo();
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(historicTasksService.getHistoricTasks(any(), eq("CLE-TEST"), eq("es_es"), eq(userInfo))).thenReturn(historicTasksList);
        // When
        MvcResult result = mockMvc.perform(post(URL_TEMPLATE)
                .param("case_id", "CLE-TEST")
                .param("locale", "es_es")
                .with(httpBasic("user", "user"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(result.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(historicTasksList);
        assertEquals(expectedJsonNode, resultJsonNode);
    }
}
