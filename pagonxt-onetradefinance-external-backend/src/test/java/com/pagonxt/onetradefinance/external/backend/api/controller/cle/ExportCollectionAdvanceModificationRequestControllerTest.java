package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionAdvanceModificationRequestService;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceModificationRequest;
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

@ControllerTest(ExportCollectionAdvanceModificationRequestController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ExportCollectionAdvanceModificationRequestControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/export-collection-advance-modification-request";

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
    ExportCollectionAdvanceModificationRequestService exportCollectionAdvanceModificationRequestService;

    @MockBean
    ExportCollectionAdvanceModificationRequestDtoSerializer serializer;

    @MockBean
    CompleteInfoExportCollectionAdvanceModificationRequestDtoSerializer completeInfoSerializer;

    @Test
    void confirmExportCollectionAdvanceModificationRequest_ok_returnsOk() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        when(exportCollectionAdvanceModificationRequestService.confirmExportCollectionAdvanceModificationRequest(any())).thenReturn(request);
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
    void confirmExportCollectionAdvanceModificationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        when(exportCollectionAdvanceModificationRequestService.confirmExportCollectionAdvanceModificationRequest(any())).thenReturn(request);
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
        assertEquals("confirmExportCollectionAdvanceModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void confirmExportCollectionAdvanceModificationRequest_wrongDateFormat_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DateFormatException("wrong date")).when(exportCollectionAdvanceModificationRequestService)
                .confirmExportCollectionAdvanceModificationRequest(any());

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
    void confirmExportCollectionAdvanceModificationRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DeliveryException("Export Collection Advance Modification Request", null)).when(exportCollectionAdvanceModificationRequestService)
                .confirmExportCollectionAdvanceModificationRequest(any());

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
        assertEquals("Error sending Export Collection Advance Modification Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        CompleteInfoExportCollectionAdvanceModificationRequest request = new CompleteInfoExportCollectionAdvanceModificationRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionAdvanceModificationRequestService.getCompleteInfoExportCollectionAdvanceModificationRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
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
        assertEquals("getCompleteInfoExportCollectionAdvanceModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceModificationRequest_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Complete Info Export Collection Advance Modification Request", null)).when(exportCollectionAdvanceModificationRequestService)
                .getCompleteInfoExportCollectionAdvanceModificationRequest(eq("taskId1"), eq(false), any());
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
        assertEquals("Error sending Complete Info Export Collection Advance Modification Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
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
        verify(exportCollectionAdvanceModificationRequestService).completeCompleteInfoExportCollectionAdvanceModificationRequest(request, "taskId1");
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionAdvanceModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceModificationRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(serializer.toModel(any())).thenReturn(request);
        doThrow(new DeliveryException("Complete Info Export Collection Advance Modification Request", null)).when(exportCollectionAdvanceModificationRequestService)
                .completeCompleteInfoExportCollectionAdvanceModificationRequest(request, "taskId1");
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
        assertEquals("Error sending Complete Info Export Collection Advance Modification Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void cancelCompleteInfoExportCollectionAdvanceModificationRequest_ok_returnsControllerResponse() throws Exception {
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
        verify(exportCollectionAdvanceModificationRequestService).cancelCompleteInfoExportCollectionAdvanceModificationRequest("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfoExportCollectionAdvanceModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void cancelCompleteInfoExportCollectionAdvanceModificationRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        doThrow(new DeliveryException("Complete Info Export Collection Advance Modification Request", null)).when(exportCollectionAdvanceModificationRequestService)
                .cancelCompleteInfoExportCollectionAdvanceModificationRequest("taskId1", userInfo);
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
        assertEquals("Error sending Complete Info Export Collection Advance Modification Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getPetitionRequestDetails_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        CompleteInfoExportCollectionAdvanceModificationRequest request = new CompleteInfoExportCollectionAdvanceModificationRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionAdvanceModificationRequestService.getPetitionRequestDetails("requestId1", userInfo)).thenReturn(result);
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "details")
                        .param("case_id", "requestId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
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
        doThrow(new DeliveryException("Petition Request Details", null)).when(exportCollectionAdvanceModificationRequestService)
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
