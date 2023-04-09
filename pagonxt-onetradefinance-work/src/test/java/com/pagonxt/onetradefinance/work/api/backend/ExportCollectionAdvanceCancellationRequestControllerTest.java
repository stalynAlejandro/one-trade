package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionAdvanceCancellationRequestService;
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

@ControllerTest(ExportCollectionAdvanceCancellationRequestController.class)
class ExportCollectionAdvanceCancellationRequestControllerTest {

    public static final String URL_TEMPLATE = "/backend/export-collection-advance-cancellation-request";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExportCollectionAdvanceCancellationRequestService exportCollectionAdvanceCancellationRequestService;

    @Test
    void createExportCollectionAdvanceCancellationRequest_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void createExportCollectionAdvanceCancellationRequest_ok_returnsValidData() throws Exception {
        // Given
        ExportCollectionAdvance exportCollectionAdvance = new ExportCollectionAdvance("001", new Customer(), new Date(1653927293741L), new Date(1653927293741L), "ref1", 1.23, "EUR", new ExportCollection(), new Date());
        ExportCollectionAdvanceCancellationRequest exportCollectionAdvanceCancellationRequest = new ExportCollectionAdvanceCancellationRequest();
        exportCollectionAdvanceCancellationRequest.setExportCollectionAdvance(exportCollectionAdvance);
        exportCollectionAdvanceCancellationRequest.setCustomer(new Customer());
        when(exportCollectionAdvanceCancellationRequestService.createExportCollectionAdvanceCancellationRequest(any())).thenReturn(exportCollectionAdvanceCancellationRequest);
        ControllerResponse expectedResult = ControllerResponse.success("exportCollectionAdvanceCancellationRequestCreated", exportCollectionAdvanceCancellationRequest);

        // When
        MvcResult mvcResult =
                mockMvc.perform(post(URL_TEMPLATE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                        .andReturn();

        // Then
        verify(exportCollectionAdvanceCancellationRequestService, times(1)).createExportCollectionAdvanceCancellationRequest(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE + "/complete-info/get/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnsValidData() throws Exception {
        // Given
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        when(exportCollectionAdvanceCancellationRequestService.getCompleteInfoExportCollectionAdvanceCancellationRequest(eq("TSK-123"), any())).thenReturn(request);
        ControllerResponse expectedResult = ControllerResponse.success("getCompleteInfoExportCollectionAdvanceCancellationRequest", request);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE + "/complete-info/get/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(exportCollectionAdvanceCancellationRequestService, times(1)).getCompleteInfoExportCollectionAdvanceCancellationRequest(eq("TSK-123"), any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(put(URL_TEMPLATE + "/complete-info/complete/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnsValidData() throws Exception {
        // Given
        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE + "/complete-info/complete/TSK-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        verify(exportCollectionAdvanceCancellationRequestService, times(1)).completeCompleteInfoExportCollectionAdvanceCancellationRequest(any(), eq("TSK-123"));
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionAdvanceCancellationRequest", resultJsonNode.get("key").asText());
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
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        when(exportCollectionAdvanceCancellationRequestService.getPetitionRequestDetails(eq("requestId"), any())).thenReturn(request);
        ControllerResponse expectedResult = ControllerResponse.success("getPetitionRequestDetails", request);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE + "/details/requestId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(exportCollectionAdvanceCancellationRequestService, times(1)).getPetitionRequestDetails(eq("requestId"), any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
