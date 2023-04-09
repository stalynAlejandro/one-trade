package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(DocumentController.class)
class DocumentControllerTest {

    public static final String URL_TEMPLATE = "/backend/document/get/doc-1";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    DocumentService documentService;

    @Test
    void getExportCollections_ok_returnsOk() throws Exception {
        // Given


        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"user\":{\"userId\":\"admin\",\"userType\":\"OFFICE\",\"userDisplayedName\":null}}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void findDocument_ok_returnsValidData() throws Exception {
        // Given
        Document document = new Document();
        when(documentService.findDocument(any(), eq("doc-1"))).thenReturn(document);
        ControllerResponse expectedResult = ControllerResponse.success("findDocument", document);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user\":{\"userId\":\"admin\",\"userType\":\"OFFICE\",\"userDisplayedName\":null}}")).andReturn();

        // Then
        verify(documentService, times(1)).findDocument(any(), eq("doc-1"));
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
