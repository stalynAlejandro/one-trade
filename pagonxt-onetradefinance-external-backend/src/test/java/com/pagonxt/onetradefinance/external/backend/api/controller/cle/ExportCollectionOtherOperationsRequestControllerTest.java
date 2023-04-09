package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionOtherOperationsRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionOtherOperationsRequestService;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionOtherOperationsRequest;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionOtherOperationsRequestController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ExportCollectionOtherOperationsRequestControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/export-collection-other-operations-request";

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
    ExportCollectionOtherOperationsRequestService exportCollectionOtherOperationsRequestService;

    @MockBean
    ExportCollectionOtherOperationsRequestDtoSerializer serializer;

    @MockBean
    CompleteInfoExportCollectionOtherOperationsRequestDtoSerializer completeInfoSerializer;

    @Test
    void confirmExportCollectionOtherOperationsRequest_ok_returnsOk() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        when(exportCollectionOtherOperationsRequestService.confirmExportCollectionOtherOperationsRequest(any())).thenReturn(request);
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
    void confirmExportCollectionOtherOperationsRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        when(exportCollectionOtherOperationsRequestService.confirmExportCollectionOtherOperationsRequest(any())).thenReturn(request);
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
        assertEquals("confirmExportCollectionOtherOperationsRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void confirmExportCollectionOtherOperationsRequest_wrongDateFormat_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DateFormatException("wrong date")).when(exportCollectionOtherOperationsRequestService)
                .confirmExportCollectionOtherOperationsRequest(any());

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
    void confirmExportCollectionOtherOperationsRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DeliveryException("Export Collection Other Operations Request", null)).when(exportCollectionOtherOperationsRequestService)
                .confirmExportCollectionOtherOperationsRequest(any());

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
        assertEquals("Error sending Export Collection Other Operations Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getCompleteInfoExportCollectionOtherOperationsRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        CompleteInfoExportCollectionOtherOperationsRequest request = new CompleteInfoExportCollectionOtherOperationsRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionOtherOperationsRequestService.getCompleteInfoExportCollectionOtherOperationsRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
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
        assertEquals("getCompleteInfoExportCollectionOtherOperationsRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfoExportCollectionOtherOperationsRequest_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Complete Info Export Collection Other Operations Request", null)).when(exportCollectionOtherOperationsRequestService)
                .getCompleteInfoExportCollectionOtherOperationsRequest(eq("taskId1"), eq(false), any());
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
        assertEquals("Error sending Complete Info Export Collection Other Operations Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void completeCompleteInfoExportCollectionOtherOperationsRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
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
        verify(exportCollectionOtherOperationsRequestService).completeCompleteInfoExportCollectionOtherOperationsRequest(request, "taskId1");
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionOtherOperationsRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void completeCompleteInfoExportCollectionOtherOperationsRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(serializer.toModel(any())).thenReturn(request);
        doThrow(new DeliveryException("Complete Info Export Collection Other Operations Request", null)).when(exportCollectionOtherOperationsRequestService)
                .completeCompleteInfoExportCollectionOtherOperationsRequest(request, "taskId1");
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
        assertEquals("Error sending Complete Info Export Collection Other Operations Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void cancelCompleteInfoExportCollectionOtherOperationsRequest_ok_returnsControllerResponse() throws Exception {
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
        verify(exportCollectionOtherOperationsRequestService).cancelCompleteInfoExportCollectionOtherOperationsRequest("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfoExportCollectionOtherOperationsRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void cancelCompleteInfoExportCollectionOtherOperationsRequest_error_returnsError() throws Exception {
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
        verify(exportCollectionOtherOperationsRequestService).cancelCompleteInfoExportCollectionOtherOperationsRequest("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfoExportCollectionOtherOperationsRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getPetitionRequestDetails_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        CompleteInfoExportCollectionOtherOperationsRequest request = new CompleteInfoExportCollectionOtherOperationsRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        doReturn(result).when(exportCollectionOtherOperationsRequestService).getPetitionRequestDetails(eq("requestId1"), any());
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
        doThrow(new DeliveryException("Petition Request Details", null)).when(exportCollectionOtherOperationsRequestService)
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

