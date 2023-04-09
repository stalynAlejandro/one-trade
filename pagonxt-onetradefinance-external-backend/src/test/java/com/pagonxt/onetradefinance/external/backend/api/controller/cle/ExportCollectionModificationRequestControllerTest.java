package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionModificationRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionModificationRequestService;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionModificationRequest;
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

@ControllerTest(ExportCollectionModificationRequestController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ExportCollectionModificationRequestControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/export-collection-modification-request";

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
    ExportCollectionModificationRequestService exportCollectionModificationRequestService;

    @MockBean
    ExportCollectionModificationRequestDtoSerializer serializer;

    @MockBean
    CompleteInfoExportCollectionModificationRequestDtoSerializer completeInfoSerializer;

    @Test
    void confirmExportCollectionModificationRequest_ok_returnsOk() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        when(exportCollectionModificationRequestService.confirmExportCollectionModificationRequest(any())).thenReturn(request);
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
    void confirmExportCollectionModificationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        when(exportCollectionModificationRequestService.confirmExportCollectionModificationRequest(any())).thenReturn(request);
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
        assertEquals("confirmExportCollectionModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void confirmExportCollectionModificationRequest_wrongDateFormat_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DateFormatException("wrong date")).when(exportCollectionModificationRequestService)
                .confirmExportCollectionModificationRequest(any());

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
    void confirmExportCollectionModificationRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        doThrow(new DeliveryException("Export Collection Modification Request", null)).when(exportCollectionModificationRequestService)
                .confirmExportCollectionModificationRequest(any());

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
        assertEquals("Error sending Export Collection Modification Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getCompleteInfoExportCollectionModificationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        CompleteInfoExportCollectionModificationRequest request = new CompleteInfoExportCollectionModificationRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionModificationRequestService.getCompleteInfoExportCollectionModificationRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
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
        assertEquals("getCompleteInfoExportCollectionModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfoExportCollectionModificationRequest_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionModificationRequestService)
                .getCompleteInfoExportCollectionModificationRequest(eq("taskId1"), eq(false), any());
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
        assertEquals("Error sending Complete Info Export Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void completeCompleteInfoExportCollectionModificationRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
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
        verify(exportCollectionModificationRequestService).completeCompleteInfoExportCollectionModificationRequest(request, "taskId1");
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void completeCompleteInfoExportCollectionModificationRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(serializer.toModel(any())).thenReturn(request);
        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionModificationRequestService)
                .completeCompleteInfoExportCollectionModificationRequest(request, "taskId1");
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
        assertEquals("Error sending Complete Info Export Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void cancelCompleteInfoExportCollectionModificationRequest_ok_returnsControllerResponse() throws Exception {
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
        verify(exportCollectionModificationRequestService).cancelCompleteInfoExportCollectionModificationRequest("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfoExportCollectionModificationRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void cancelCompleteInfoExportCollectionModificationRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionModificationRequestService)
                .cancelCompleteInfoExportCollectionModificationRequest("taskId1", userInfo);
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
        assertEquals("Error sending Complete Info Export Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getPetitionRequestDetails_ok_returnsControllerResponse() throws Exception {
        // Given
        CompleteInfoExportCollectionModificationRequest request = new CompleteInfoExportCollectionModificationRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionModificationRequestService.getPetitionRequestDetails(eq("requestId"), any())).thenReturn(result);
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "details")
                        .param("case_id", "requestId")
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
        doThrow(new DeliveryException("Petition Request Details", null)).when(exportCollectionModificationRequestService)
                .getPetitionRequestDetails(eq("requestId"), any());
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "details")
                        .param("case_id", "requestId")
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
