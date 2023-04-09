package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionAdvanceRequestController.class)
class ExportCollectionAdvanceRequestControllerTest {

    public static final String URL_TEMPLATE = "/backend/export-collection-advance-request";

    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.USE_LONG_FOR_INTS, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    @MockBean
    ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService;

    @Test
    void createExportCollectionAdvanceRequest_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void createExportCollectionAdvanceRequest_ok_returnsValidData() throws Exception {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        doReturn("createdAdvanceCase").when(exportCollectionAdvanceRequest).getCode();
        doReturn(exportCollectionAdvanceRequest).when(exportCollectionAdvanceRequestService).createExportCollectionAdvanceRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        assertThat(mvcResult.getResponse().getContentAsString()).contains("createdAdvanceCase");
    }

    @Test
    void updateExportCollectionAdvanceRequest_ok_returnsOk() throws Exception {
        // Given

        // When and Then
        mockMvc.perform(put(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void updateExportCollectionAdvanceRequest_ok_returnsValidData() throws Exception {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        doReturn("normal").when(exportCollectionAdvanceRequest).getPriority();
        doReturn(exportCollectionAdvanceRequest).when(exportCollectionAdvanceRequestService).updateExportCollectionAdvanceRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        assertThat(mvcResult.getResponse().getContentAsString()).contains("normal");
    }

    @Test
    void confirmExportCollectionAdvanceRequest_ok_isOk() throws Exception {
        // Given

        // When and Then
        mockMvc.perform(put(URL_TEMPLATE.concat("/confirm"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void confirmExportCollectionAdvanceRequest_ok_returnsValidData() throws Exception {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        doReturn(exportCollectionAdvanceRequest).when(exportCollectionAdvanceRequestService).confirmExportCollectionAdvanceRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE.concat("/confirm"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        assertThat(mvcResult).isNotNull();
    }

    @Test
    void getExportCollectionAdvanceRequest_isOk_returnsOk() throws Exception {
        // Given

        // When and Then
        mockMvc.perform(post(URL_TEMPLATE.concat("/get/code"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getExportCollectionAdvanceRequest_isOk_returnsValidData() throws Exception {
        // Given
        ExportCollectionAdvanceRequest exportCollectionAdvanceRequest = mock(ExportCollectionAdvanceRequest.class);
        doReturn(exportCollectionAdvanceRequest).when(exportCollectionAdvanceRequestService).confirmExportCollectionAdvanceRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE.concat("/get/code"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();

        // Then
        assertThat(mvcResult).isNotNull();
    }


    @Test
    void getCompleteInfoExportCollectionAdvanceRequest_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE + "/complete-info/get/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceRequest_ok_returnsValidData() throws Exception {
        // Given
        CompleteInfoExportCollectionAdvanceRequest request = new CompleteInfoExportCollectionAdvanceRequest();
        when(exportCollectionAdvanceRequestService.getCompleteInfoExportCollectionAdvanceRequest(eq("TSK-123"), any())).thenReturn(request);
        ControllerResponse expectedResult = ControllerResponse.success("getCompleteInfoExportCollectionAdvanceRequest", request);
        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE + "/complete-info/get/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(exportCollectionAdvanceRequestService, times(1)).getCompleteInfoExportCollectionAdvanceRequest(eq("TSK-123"), any());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode resultJsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = objectMapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceRequest_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(put(URL_TEMPLATE + "/complete-info/complete/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceRequest_ok_returnsValidData() throws Exception {
        // Given
        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE + "/complete-info/complete/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        verify(exportCollectionAdvanceRequestService, times(1)).completeCompleteInfoExportCollectionAdvanceRequest(any(), eq("TSK-123"));
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionAdvanceRequest", resultJsonNode.get("key").asText());
    }

    @Test
    void getPetitionRequestDetails_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE + "/details/requestId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getPetitionRequestDetails_ok_returnsValidData() throws Exception {
        // Given
        CompleteInfoExportCollectionAdvanceRequest request = new CompleteInfoExportCollectionAdvanceRequest();
        when(exportCollectionAdvanceRequestService.getPetitionRequestDetails(eq("requestId"), any())).thenReturn(request);
        ControllerResponse expectedResult = ControllerResponse.success("getPetitionRequestDetails", request);
        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE + "/details/requestId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(exportCollectionAdvanceRequestService, times(1)).getPetitionRequestDetails(eq("requestId"), any());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode resultJsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = objectMapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
