package com.pagonxt.onetradefinance.work.api.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.service.DirectoryService;
import com.pagonxt.onetradefinance.work.api.model.RecipientsResponse;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(DirectoryController.class)
class DirectoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DirectoryService directoryService;

    @Test
    void getRecipientsForNotification() throws Exception {
        //given
        String someString = "someString";
        doReturn(someString).when(directoryService).getRecipientsForNotification(any(), any());

        //when and then
        MvcResult mvcResult = mockMvc.perform(get("/integrations/directory/recipients"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode resultJsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = objectMapper.valueToTree(new RecipientsResponse(someString));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
