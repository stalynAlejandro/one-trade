package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionRequestService;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionRequestController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ExportCollectionRequestControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/export-collection-request";

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
    ExportCollectionRequestService exportCollectionRequestService;

    @MockBean
    ExportCollectionRequestDtoSerializer serializer;

    @MockBean
    CompleteInfoExportCollectionRequestDtoSerializer completeInfoSrializer;

    @Test
    void saveExportCollectionRequestDraft_ok_returnsOk() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(exportCollectionRequestService.createOrUpdateExportCollectionRequestDraft(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        when(serializer.toModel(any())).thenReturn(request);

        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void saveExportCollectionRequestDraft_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(exportCollectionRequestService.createOrUpdateExportCollectionRequestDraft(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        when(serializer.toModel(any())).thenReturn(request);

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("saveExportCollectionRequestDraft", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void saveExportCollectionRequestDraft_wrongDateFormat_returnsError() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DateFormatException("wrong date")).when(exportCollectionRequestService)
                .createOrUpdateExportCollectionRequestDraft(any());

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                .with(httpBasic("office", "office"))
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Unable to parse date", error.get("message").asText());
    }
    @Test
    void saveExportCollectionRequestDraft_error_returnsError() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DeliveryException("Export Collection Request", null)).when(exportCollectionRequestService)
                .createOrUpdateExportCollectionRequestDraft(any());

        // When
        MvcResult mvcResult = mockMvc.perform(post(URL_TEMPLATE)
                .with(httpBasic("office", "office"))
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Export Collection Request to Flowable Work", error.get("message").asText());

    }

    @Test
    void confirmExportCollectionRequest_ok_returnsOk() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(exportCollectionRequestService.confirmExportCollectionRequest(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        when(serializer.toModel(any())).thenReturn(request);

        // When and then
        mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "confirm")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void confirmExportCollectionRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(exportCollectionRequestService.confirmExportCollectionRequest(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        when(serializer.toModel(any())).thenReturn(request);

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                        .param("type", "confirm")
                        .with(httpBasic("office", "office"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("confirmExportCollectionRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void confirmExportCollectionRequest_wrongDateFormat_returnsError() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DateFormatException("wrong date")).when(exportCollectionRequestService).confirmExportCollectionRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                .param("type", "confirm")
                .with(httpBasic("office", "office"))
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Unable to parse date", error.get("message").asText());

    }

    @Test
    void confirmExportCollectionRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DeliveryException("Export Collection Request", null)).when(exportCollectionRequestService)
                .confirmExportCollectionRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put(URL_TEMPLATE)
                .param("type", "confirm")
                .with(httpBasic("office", "office"))
                .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")).andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Export Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getExportCollectionRequestDraft_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        when(exportCollectionRequestService.getExportCollectionRequestDraft(eq("idtest"), any())).thenReturn(request);
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "draft")
                        .param("case_id", "idtest")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("findExportCollectionRequestDraft", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getExportCollectionRequestDraft_error_returnsError() throws Exception {
        // Given
        when(exportCollectionRequestService.getExportCollectionRequestDraft(eq("idtest"), any()))
                .thenThrow(new DeliveryException("Export Collection Request", null));
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "draft")
                        .param("case_id", "idtest")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode error = resultJsonNode.get("errors").get(0);
        assertEquals("error", error.get("level").asText());
        assertEquals("Error sending Export Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getCompleteInfoExportCollectionRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        CompleteInfoExportCollectionRequest request = new CompleteInfoExportCollectionRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionRequestService.getCompleteInfoExportCollectionRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
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
        assertEquals("getCompleteInfoExportCollectionRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfoExportCollectionRequest_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionRequestService)
                .getCompleteInfoExportCollectionRequest(eq("taskId1"), eq(false), any());
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
    void completeCompleteInfoExportCollectionRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
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
        verify(exportCollectionRequestService).completeCompleteInfoExportCollectionRequest(request, "taskId1");
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void completeCompleteInfoExportCollectionRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(serializer.toModel(any())).thenReturn(request);
        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionRequestService)
                .completeCompleteInfoExportCollectionRequest(request, "taskId1");
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
    void cancelCompleteInfoExportCollectionRequest_ok_returnsControllerResponse() throws Exception {
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
        verify(exportCollectionRequestService).cancelCompleteInfoExportCollectionRequest("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfoExportCollectionRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void cancelCompleteInfoExportCollectionRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionRequestService)
                .cancelCompleteInfoExportCollectionRequest("taskId1", userInfo);
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
        CompleteInfoExportCollectionRequest request = new CompleteInfoExportCollectionRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionRequestService.getPetitionRequestDetails(eq("requestId1"), any())).thenReturn(result);
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
        doThrow(new DeliveryException("Petition Request Details", null)).when(exportCollectionRequestService)
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
