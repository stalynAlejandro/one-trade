package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.completeinfo.CompleteInfoExportCollectionAdvanceRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionAdvanceRequestService;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.completeinfo.CompleteInfoExportCollectionAdvanceRequest;
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

@ControllerTest(ExportCollectionAdvanceRequestController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ExportCollectionAdvanceRequestControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/export-collection-advance-request";

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
    ExportCollectionAdvanceRequestService exportCollectionAdvanceRequestService;

    @MockBean
    ExportCollectionAdvanceRequestDtoSerializer serializer;

    @MockBean
    CompleteInfoExportCollectionAdvanceRequestDtoSerializer completeInfoSerializer;

    @Test
    void saveExportCollectionAdvanceRequestDraft_ok_returnsOk() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(exportCollectionAdvanceRequestService.createOrUpdateExportCollectionAdvanceRequestDraft(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
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
    void saveExportCollectionAdvanceRequestDraft_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(exportCollectionAdvanceRequestService.createOrUpdateExportCollectionAdvanceRequestDraft(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
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
        assertEquals("saveExportCollectionAdvanceRequestDraft", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void saveExportCollectionAdvanceRequestDraft_wrongDateFormat_returnsError() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
        doThrow(new DateFormatException("wrong date")).when(exportCollectionAdvanceRequestService)
                .createOrUpdateExportCollectionAdvanceRequestDraft(any());

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
    void saveExportCollectionAdvanceRequestDraft_error_returnsError() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
        doThrow(new DeliveryException("Export Collection Request", null)).when(exportCollectionAdvanceRequestService)
                .createOrUpdateExportCollectionAdvanceRequestDraft(any());

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
    void confirmExportCollectionAdvanceRequest_ok_returnsOk() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(exportCollectionAdvanceRequestService.confirmExportCollectionAdvanceRequest(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
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
    void confirmExportCollectionAdvanceRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(exportCollectionAdvanceRequestService.confirmExportCollectionAdvanceRequest(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
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
        assertEquals("confirmExportCollectionAdvanceRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void confirmExportCollectionAdvanceRequest_wrongDateFormat_returnsError() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
        doThrow(new DateFormatException("wrong date")).when(exportCollectionAdvanceRequestService).confirmExportCollectionAdvanceRequest(any());

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
    void confirmExportCollectionAdvanceRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(serializer.toModel(any())).thenReturn(request);
        when(userService.getCurrentUserCountry()).thenReturn("ESP");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo());
        doThrow(new DeliveryException("Export Collection Advance Request", null)).when(exportCollectionAdvanceRequestService)
                .confirmExportCollectionAdvanceRequest(any());

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
        assertEquals("Error sending Export Collection Advance Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getExportCollectionAdvanceRequestDraft_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        when(exportCollectionAdvanceRequestService.getExportCollectionAdvanceRequestDraft(eq("idtest"), any())).thenReturn(request);
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
        assertEquals("findExportCollectionAdvanceRequestDraft", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getExportCollectionAdvanceRequestDraft_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Export Collection Advance Request", null)).when(exportCollectionAdvanceRequestService)
                .getExportCollectionAdvanceRequestDraft(eq("idtest"), any());
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
        assertEquals("Error sending Export Collection Advance Request to Flowable Work", error.get("message").asText());
    }


    @Test
    void getCompleteInfoExportCollectionAdvanceRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        CompleteInfoExportCollectionAdvanceRequest request = new CompleteInfoExportCollectionAdvanceRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionAdvanceRequestService.getCompleteInfoExportCollectionAdvanceRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
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
        assertEquals("getCompleteInfoExportCollectionAdvanceRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfoExportCollectionAdvanceRequest_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionAdvanceRequestService)
                .getCompleteInfoExportCollectionAdvanceRequest(eq("taskId1"), eq(false), any());
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
    void completeCompleteInfoExportCollectionAdvanceRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
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
        verify(exportCollectionAdvanceRequestService).completeCompleteInfoExportCollectionAdvanceRequest(request, "taskId1");
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfoExportCollectionAdvanceRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void completeCompleteInfoExportCollectionAdvanceRequest_error_returnsError() throws Exception {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        userInfo.setCountry("ES");
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");
        when(serializer.toModel(any())).thenReturn(request);
        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionAdvanceRequestService)
                .completeCompleteInfoExportCollectionAdvanceRequest(request, "taskId1");
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
    void cancelCompleteInfoExportCollectionAdvanceRequest_ok_returnsControllerResponse() throws Exception {
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
        verify(exportCollectionAdvanceRequestService).cancelCompleteInfoExportCollectionAdvanceRequest("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfoExportCollectionAdvanceRequest", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void cancelCompleteInfoExportCollectionAdvanceRequest_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        when(userService.getCurrentUserCountry()).thenReturn("ES");

        doThrow(new DeliveryException("Complete Info Export Collection Request", null)).when(exportCollectionAdvanceRequestService)
                .cancelCompleteInfoExportCollectionAdvanceRequest("taskId1", userInfo);
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
        CompleteInfoExportCollectionAdvanceRequest request = new CompleteInfoExportCollectionAdvanceRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(exportCollectionAdvanceRequestService.getPetitionRequestDetails(eq("requestId1"), any())).thenReturn(result);
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
        doThrow(new DeliveryException("Petition Request Details", null)).when(exportCollectionAdvanceRequestService)
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
