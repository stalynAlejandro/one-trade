package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.FiltersDefinition;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.MyTasksService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(MyTasksController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class MyTasksControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/retrieve-my-tasks";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MyTasksService myTasksService;

    @Test
    void getMyTasks_ok_returnsOk() throws Exception {
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
    void getMyTasks_ok_returnsMyTasksList() throws Exception {
        // Given
        MyTasksList myTasksList = new MyTasksList();
        when(myTasksService.getMyTasks(any(), any(), any())).thenReturn(myTasksList);
        // When
        MvcResult result = mockMvc.perform(post(URL_TEMPLATE)
                .with(httpBasic("user", "user"))
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(result.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(myTasksList);
        assertEquals(expectedJsonNode, resultJsonNode);
    }

    @Test
    void getFilters_ok_returnsOk() throws Exception {
        // Given
        when(userService.getCurrentUser()).thenReturn(new User(null, null, "BO"));

        // When and Then
        mockMvc.perform(get(URL_TEMPLATE + "/filters")
                        .with(httpBasic("user", "user"))
                        .with(csrf()))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getFilters_ok_returnsFiltersDefinition() throws Exception {
        // Given
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("filters/my-tasks.json");
        FiltersDefinition filters = mapper.readValue(stream, new TypeReference<>() {
        });
        when(myTasksService.getFilters("BO")).thenReturn(filters);
        when(userService.getCurrentUser()).thenReturn(new User("user", "User", "BO"));

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
