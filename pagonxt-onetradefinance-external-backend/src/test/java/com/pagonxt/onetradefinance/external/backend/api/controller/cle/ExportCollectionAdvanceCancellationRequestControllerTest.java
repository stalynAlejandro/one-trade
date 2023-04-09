package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceCancellationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionAdvanceCancellationRequestService;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionAdvanceCancellationRequestController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ExportCollectionAdvanceCancellationRequestControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/export-collection-advance-cancellation-request";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @MockBean
    UserInfoService userInfoService;

    @MockBean
    OfficeInfoService officeInfoService;

    @MockBean
    ExportCollectionAdvanceCancellationRequestService exportCollectionAdvanceCancellationRequestService;

    @MockBean
    ExportCollectionAdvanceCancellationRequestDtoSerializer serializer;

    @MockBean
    CompleteInfoExportCollectionAdvanceCancellationRequestDtoSerializer completeInfoSerializer;

    @Test
    void confirmExportCollectionAdvanceCancellationRequest_ok_returnsOk() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        when(exportCollectionAdvanceCancellationRequestService.confirmExportCollectionAdvanceCancellationRequest(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(serializer.toModel(any())).thenReturn(request);

        // When and then
        mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "confirm")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requester\":{}}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void confirmExportCollectionAdvanceCancellationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        when(exportCollectionAdvanceCancellationRequestService.confirmExportCollectionAdvanceCancellationRequest(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(serializer.toModel(any())).thenReturn(request);

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "confirm")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"requester\":{}}"))
                .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("confirmExportCollectionAdvanceCancellationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void confirmExportCollectionAdvanceCancellationRequest_wrongDateFormat_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DateFormatException("wrong date")).when(exportCollectionAdvanceCancellationRequestService)
                .confirmExportCollectionAdvanceCancellationRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                .param("type", "confirm")
                .with(httpBasic("office", "office"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"requester\":{}}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Unable to parse date", error.get("message").asText());
    }

    @Test
    void confirmExportCollectionAdvanceCancellationRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DeliveryException("Export Collection Advance Cancellation Request", null)).when(exportCollectionAdvanceCancellationRequestService)
                .confirmExportCollectionAdvanceCancellationRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                .param("type", "confirm")
                .with(httpBasic("office", "office"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"requester\":{}}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Export Collection Advance Cancellation Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionAdvanceCancellationRequestService.getCompleteInfoExportCollectionAdvanceCancellationRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "complete-info")
                        .param("task_id", "taskId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("getCompleteInfoExportCollectionAdvanceCancellationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceCancellationRequest_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Complete Info Export Collection Advance Cancellation Request", null)).when(exportCollectionAdvanceCancellationRequestService)
                .getCompleteInfoExportCollectionAdvanceCancellationRequest(eq("taskId1"), eq(false), any());
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "complete-info")
                        .param("task_id", "taskId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Complete Info Export Collection Advance Cancellation Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(serializer.toModel(any())).thenReturn(request);
        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "complete-info-complete")
                        .param("task_id", "taskId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        verify(exportCollectionAdvanceCancellationRequestService).completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, "taskId1");
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionAdvanceCancellationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceCancellationRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(serializer.toModel(any())).thenReturn(request);
        doThrow(new DeliveryException("Complete Info Export Collection Advance Cancellation Request", null)).when(exportCollectionAdvanceCancellationRequestService)
                .completeCompleteInfoExportCollectionAdvanceCancellationRequest(request, "taskId1");
        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "complete-info-complete")
                        .param("task_id", "taskId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Complete Info Export Collection Advance Cancellation Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void cancelCompleteInfoExportCollectionAdvanceCancellationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "complete-info-cancel")
                        .param("task_id", "taskId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        verify(exportCollectionAdvanceCancellationRequestService).cancelCompleteInfoExportCollectionAdvanceCancellationRequest("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfoExportCollectionAdvanceCancellationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void cancelCompleteInfoExportCollectionAdvanceCancellationRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        doThrow(new DeliveryException("Complete Info Export Collection Advance Cancellation Request", null)).when(exportCollectionAdvanceCancellationRequestService)
                .cancelCompleteInfoExportCollectionAdvanceCancellationRequest("taskId1", userInfo);
        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "complete-info-cancel")
                        .param("task_id", "taskId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Complete Info Export Collection Advance Cancellation Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getPetitionRequestDetails_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        CompleteInfoExportCollectionAdvanceCancellationRequest request = new CompleteInfoExportCollectionAdvanceCancellationRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        doReturn(result).when(exportCollectionAdvanceCancellationRequestService).getPetitionRequestDetails(eq("requestId1"), any());
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "details")
                        .param("case_id", "requestId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("getPetitionRequestDetails", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getPetitionRequestDetails_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Petition Request Details", null)).when(exportCollectionAdvanceCancellationRequestService)
                .getPetitionRequestDetails(eq("requestId1"), any());
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "details")
                        .param("case_id", "requestId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Petition Request Details to Flowable Work", error.get("message").asText());
    }
}
