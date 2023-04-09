package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.DocumentService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.exception.FlowableServerException;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(DocumentController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class DocumentControllerTest {

    public static final String URL_DOCUMENT = "/api-tradeflow/documents/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserInfoService infoService;

    @MockBean
    DocumentService documentService;

    @Test
    void findDocument_ok_returnsOk() throws Exception {
        // Given
        String documentId = "documentId";
        Document foundDocument = new Document();
        foundDocument.setMimeType("application/pdf");
        when(documentService.findDocument(eq(documentId), any())).thenReturn(foundDocument);

        // When and then
        mockMvc.perform(get(URL_DOCUMENT + documentId)
                        .with(httpBasic("office", "office"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void findDocument_ok_returnsControllerResponse() throws Exception {
        // Given
        String documentId = "documentId";
        Document foundDocument = new Document();
        foundDocument.setDocumentId(documentId);
        foundDocument.setMimeType("application/pdf");
        foundDocument.setFilename("filename1.pdf");
        when(documentService.findDocument(eq(documentId), any())).thenReturn(foundDocument);

        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_DOCUMENT + documentId)
                        .with(httpBasic("office", "office"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        assertEquals("application/pdf", mvcResult.getResponse().getContentType());
        assertEquals("attachment; filename=filename1.pdf", mvcResult.getResponse().getHeader("Content-Disposition"));
        assertNotNull(mvcResult.getResponse().getOutputStream());
    }

    @Test
    void findDocument_FlowableError_returnsControllerResponseError() throws Exception {
        // Given
        String documentId = "documentId";
        doThrow(new FlowableServerException("Test Error")).when(documentService).findDocument(eq(documentId), any());

        // When and then
        MvcResult mvcResult = mockMvc.perform(get(URL_DOCUMENT + documentId)
                        .with(httpBasic("office", "office"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Test Error", error.get("message").asText());
    }
}
