package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.MyRequestsService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(MyRequestsController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class MyRequestsControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/retrieve-my-requests";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MyRequestsService myRequestsService;

    @Test
    void getMyRequests_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .with(httpBasic("user", "user"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getMyRequests_ok_returnsMyRequestsList() throws Exception {
        // Given
        MyRequestsList myRequestsList = new MyRequestsList();
        when(myRequestsService.getMyRequests(any(), any())).thenReturn(myRequestsList);
        // When
        MvcResult result = mockMvc.perform(post(URL_TEMPLATE)
                .with(httpBasic("user", "user"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(result.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(myRequestsList);
        assertEquals(expectedJsonNode, resultJsonNode);
    }

    @Test
    void getFilters_ok_returnsOk() throws Exception {
        // Given

        // When and Then
        mockMvc.perform(get(URL_TEMPLATE + "/filters")
                        .with(httpBasic("user", "user"))
                        .with(csrf()))
                        .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getFilters_ok_returnsFiltersDefinition() throws Exception {
        // Given
        // When
        MvcResult result = mockMvc.perform(get(URL_TEMPLATE + "/filters")
                        .with(httpBasic("user", "user"))
                        .with(csrf()))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(result.getResponse().getContentAsString());
        assertEquals("text", resultJsonNode.get("customerName").get("type").textValue());
        assertEquals("Export Collection", resultJsonNode.get("productId").get("options").get(0).get("description").textValue());
    }
}
