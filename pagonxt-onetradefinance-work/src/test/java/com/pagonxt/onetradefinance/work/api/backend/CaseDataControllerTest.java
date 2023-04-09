package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.CaseDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(CaseDataController.class)
class CaseDataControllerTest {

    public static final String URL_TEMPLATE = "/backend/case-data";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CaseDataService caseDataService;

    @Test
    void findComments_ok_returnsOk() throws Exception {
        // Given, when and then
        mockMvc.perform(post(URL_TEMPLATE + "/get/CLE-TEST/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\":{\"userId\":\"admin\",\"userType\":\"OFFICE\",\"userDisplayedName\":null}}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void findComments_ok_returnsValidData() throws Exception {
        // Given
        Comment comment = new Comment();
        comment.setTaskName("Case 123");
        List<Comment> comments = List.of(comment);
        when(caseDataService.findComments(any(), eq("CLE-TEST"))).thenReturn(comments);
        ControllerResponse expectedResult = ControllerResponse.success("findComments", comments);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE + "/get/CLE-TEST/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user\":{\"userId\":\"admin\",\"userType\":\"OFFICE\",\"userDisplayedName\":null}}")).andReturn();

        // Then
        verify(caseDataService, times(1)).findComments(any(), eq("CLE-TEST"));
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertEquals(expectedJsonNode, resultJsonNode);
    }
}
