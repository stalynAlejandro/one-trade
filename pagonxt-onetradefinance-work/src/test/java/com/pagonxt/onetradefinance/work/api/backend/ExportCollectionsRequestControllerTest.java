package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionRequestService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionRequestController.class)
class ExportCollectionsRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExportCollectionRequestService exportCollectionRequestService;

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnRuntimeService cmmnRuntimeService;

    @Test
    void createExportCollectionRequest_returns200() throws Exception {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class, Answers.RETURNS_DEEP_STUBS);
        when(caseInstance.getCaseVariables().get("exportCollectionRequestCaseSeq")).thenReturn("CLE-001");
        when(cmmnRuntimeService
                .createCaseInstanceBuilder()
                .caseDefinitionKey(any())
                .variables(any())
                .start()).thenReturn(caseInstance);

        // When and then
        mockMvc.perform(post("/backend/export-collection-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void createExportCollectionRequest_returnsValidResponse() throws Exception {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class, Answers.RETURNS_DEEP_STUBS);
        when(caseInstance.getCaseVariables().get("exportCollectionRequestCaseSeq")).thenReturn("CLE-001");
        when(cmmnRuntimeService
                .createCaseInstanceBuilder()
                .caseDefinitionKey(any())
                .variables(any())
                .start()).thenReturn(caseInstance);
        JsonNode expectedJsonNode = mapper.valueToTree(ControllerResponse.success("exportCollectionRequestCreated", null));

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/export-collection-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void updateExportCollectionRequest_returns200() throws Exception {
        // Given

        // When and then
        mockMvc.perform(put("/backend/export-collection-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void updateExportCollectionRequest_returnsValidResponse() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        doReturn(request).when(exportCollectionRequestService).updateExportCollectionRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put("/backend/export-collection-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("updateExportCollectionRequest", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode expectedJsonNode = mapper.valueToTree(request);
        assertEquals(expectedJsonNode, resultJsonNode.get("entity"), "Returned entity should match pattern");
    }

    @Test
    void confirmExportCollectionRequest_returns200() throws Exception {
        // Given

        // When and then
        mockMvc.perform(put("/backend/export-collection-request/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void confirmExportCollectionRequest_returnsValidResponse() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        doReturn(request).when(exportCollectionRequestService).confirmExportCollectionRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put("/backend/export-collection-request/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("confirmExportCollectionRequest", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode expectedJsonNode = mapper.valueToTree(request);
        assertEquals(expectedJsonNode, resultJsonNode.get("entity"), "Returned entity should match pattern");
    }

    @Test
    void getExportCollectionRequest_returns200() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post("/backend/export-collection-request/get/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getExportCollectionRequest_returnsValidResponse() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setCode("1");
        doReturn(request).when(exportCollectionRequestService).getExportCollectionRequestByCode(any(), any());

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/export-collection-request/get/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("getExportCollectionRequest", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode expectedJsonNode = mapper.valueToTree(request);
        assertEquals(expectedJsonNode, resultJsonNode.get("entity"), "Returned entity should match pattern");
    }

    @Test
    void deleteExportCollectionRequest_returns500() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post("/backend/export-collection-request/delete/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().is5xxServerError()).andReturn();
    }

    @Test
    void getPetitionRequestDetails_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post("/backend/export-collection-request/details/requestId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getPetitionRequestDetails_ok_returnsValidData() throws Exception {
        // Given
        CompleteInfoExportCollectionRequest request = new CompleteInfoExportCollectionRequest();
        when(exportCollectionRequestService.getPetitionRequestDetails(eq("requestId"), any())).thenReturn(request);
        ControllerResponse expectedResult = ControllerResponse.success("getPetitionRequestDetails", request);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/export-collection-request/details/requestId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(exportCollectionRequestService, times(1)).getPetitionRequestDetails(eq("requestId"), any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
