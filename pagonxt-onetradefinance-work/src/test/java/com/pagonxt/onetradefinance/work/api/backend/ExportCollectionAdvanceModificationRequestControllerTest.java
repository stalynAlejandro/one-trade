package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceModificationRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionAdvanceModificationRequestController.class)
class ExportCollectionAdvanceModificationRequestControllerTest {

    public static final String URL_TEMPLATE = "/backend/export-collection-advance-modification-request";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExportCollectionAdvanceModificationRequestService exportCollectionAdvanceModificationRequestService;

    @Test
    void createExportCollectionAdvanceModificationRequest_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void createExportCollectionAdvanceModificationRequest_ok_returnsValidData() throws Exception {
        // Given
        ExportCollectionAdvance exportCollectionAdvance = new ExportCollectionAdvance("001", new Customer(), new Date(1653927293741L), new Date(1653927293741L), "ref1", 1.23, "EUR", new ExportCollection(), new Date());
        ExportCollectionAdvanceModificationRequest exportCollectionAdvanceModificationRequest = new ExportCollectionAdvanceModificationRequest();
        exportCollectionAdvanceModificationRequest.setExportCollectionAdvance(exportCollectionAdvance);
        exportCollectionAdvanceModificationRequest.setCustomer(new Customer());
        when(exportCollectionAdvanceModificationRequestService.createExportCollectionAdvanceModificationRequest(any())).thenReturn(exportCollectionAdvanceModificationRequest);
        ControllerResponse expectedResult = ControllerResponse.success("exportCollectionAdvanceModificationRequestCreated", exportCollectionAdvanceModificationRequest);

        // When
        MvcResult mvcResult =
                mockMvc.perform(post(URL_TEMPLATE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                        .andReturn();

        // Then
        verify(exportCollectionAdvanceModificationRequestService, times(1)).createExportCollectionAdvanceModificationRequest(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE + "/complete-info/get/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnsValidData() throws Exception {
        // Given
        CompleteInfoExportCollectionAdvanceModificationRequest request = new CompleteInfoExportCollectionAdvanceModificationRequest();
        when(exportCollectionAdvanceModificationRequestService.getCompleteInfoExportCollectionAdvanceModificationRequest(eq("TSK-123"), any())).thenReturn(request);
        ControllerResponse expectedResult = ControllerResponse.success("getCompleteInfoExportCollectionAdvanceModificationRequest", request);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE + "/complete-info/get/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(exportCollectionAdvanceModificationRequestService, times(1)).getCompleteInfoExportCollectionAdvanceModificationRequest(eq("TSK-123"), any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(put(URL_TEMPLATE + "/complete-info/complete/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnsValidData() throws Exception {
        // Given
        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE + "/complete-info/complete/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        verify(exportCollectionAdvanceModificationRequestService, times(1)).completeCompleteInfoExportCollectionAdvanceModificationRequest(any(), eq("TSK-123"));
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionAdvanceModificationRequest", resultJsonNode.get("key").asText());
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
        CompleteInfoExportCollectionAdvanceModificationRequest request = new CompleteInfoExportCollectionAdvanceModificationRequest();
        when(exportCollectionAdvanceModificationRequestService.getPetitionRequestDetails(eq("requestId"), any())).thenReturn(request);
        ControllerResponse expectedResult = ControllerResponse.success("getPetitionRequestDetails", request);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE + "/details/requestId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(exportCollectionAdvanceModificationRequestService, times(1)).getPetitionRequestDetails(eq("requestId"), any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
