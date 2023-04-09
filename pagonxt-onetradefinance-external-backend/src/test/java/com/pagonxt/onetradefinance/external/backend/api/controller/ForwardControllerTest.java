package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ForwardController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ForwardControllerTest {

    public static final String URL_TEMPLATE = "/app/xyz";

    @Autowired
    MockMvc mockMvc;

    @Test
    void get_ok_returnsOk() throws Exception {
        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }
}
