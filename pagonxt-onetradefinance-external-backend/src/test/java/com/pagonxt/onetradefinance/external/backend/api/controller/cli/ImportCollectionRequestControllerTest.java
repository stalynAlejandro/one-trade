package com.pagonxt.onetradefinance.external.backend.api.controller.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoTradeRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.ImportCollectionRequestDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeRequestService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
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

@ControllerTest(ImportCollectionRequestController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class ImportCollectionRequestControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/import-collection-request";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserInfoService userInfoService;
    @MockBean
    OfficeInfoService officeInfoService;
    @MockBean
    TradeRequestService tradeRequestService;
    @MockBean
    ImportCollectionRequestDtoSerializer serializer;
    @MockBean
    CompleteInfoTradeRequestDtoSerializer completeInfoTradeRequestDtoSerializer;

    @Test
    void saveImportCollectionRequestDraft_ok_returnsOk() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(tradeRequestService.createOrUpdateTradeRequestDraft(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
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
    void saveImportCollectionRequestDraft_ok_returnsControllerResponse() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(tradeRequestService.createOrUpdateTradeRequestDraft(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
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
        assertEquals("saveImportCollectionRequestDraft", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void saveImportCollectionRequestDraft_wrongDateFormat_returnsError() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DateFormatException("wrong date")).when(tradeRequestService)
                .createOrUpdateTradeRequestDraft(any());

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
    void saveImportCollectionRequestDraft_error_returnsError() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DeliveryException("Import Collection Request", null)).when(tradeRequestService)
                .createOrUpdateTradeRequestDraft(any());

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
        assertEquals("Error sending Import Collection Request to Flowable Work", error.get("message").asText());

    }

    @Test
    void confirmImportCollectionRequest_ok_returnsOk() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(tradeRequestService.confirmTradeRequest(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
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
    void confirmImportCollectionRequest_ok_returnsControllerResponse() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(tradeRequestService.confirmTradeRequest(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
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
        assertEquals("confirm", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void confirmImportCollectionRequest_wrongDateFormat_returnsError() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DateFormatException("wrong date")).when(tradeRequestService).confirmTradeRequest(any());

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
    void confirmImportCollectionRequest_error_returnsError() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        when(userInfoService.getUserInfo()).thenReturn(new UserInfo(user));
        doThrow(new DeliveryException("Import Collection Request", null)).when(tradeRequestService)
                .confirmTradeRequest(any());

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
        assertEquals("Error sending Import Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void completeCompleteInfo_ok_returnsControllerResponse() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
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
        verify(tradeRequestService).completeCompleteInfoTradeRequest(request, "taskId1");
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("completeCompleteInfo", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void completeCompleteInfo_error_returnsError() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(serializer.toModel(any())).thenReturn(request);
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);
        doThrow(new DeliveryException("Complete Info Import Collection Request", null)).when(tradeRequestService)
                .completeCompleteInfoTradeRequest(request, "taskId1");
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
        assertEquals("Error sending Complete Info Import Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void cancelCompleteInfo_ok_returnsControllerResponse() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);

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
        verify(tradeRequestService).cancelTradeExternalTask("taskId1", userInfo);
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText());
        assertEquals("cancelCompleteInfo", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void cancelCompleteInfo_error_returnsError() throws Exception {
        // Given
        User user = new User("office", "Office", "OFFICE");
        UserInfo userInfo = new UserInfo(user);
        when(userInfoService.getUserInfo()).thenReturn(userInfo);

        doThrow(new DeliveryException("Complete Info Import Collection Request", null)).when(tradeRequestService)
                .cancelTradeExternalTask("taskId1", userInfo);
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
        assertEquals("Error sending Complete Info Import Collection Request to Flowable Work", error.get("message").asText());
    }

    @Test
    void getImportCollectionRequestDraft_ok_returnsControllerResponse() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(tradeRequestService.getTradeRequest(eq("idtest"), any())).thenReturn(request);
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
        assertEquals("getDraft", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getImportCollectionRequestDraft_error_returnsError() throws Exception {
        // Given
        when(tradeRequestService.getTradeRequest(eq("idtest"), any()))
                .thenThrow(new DeliveryException("Import Collection Request", null));
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
        assertEquals("Error sending Import Collection Request to Flowable Work", error.get("message").asText());
    }


    @Test
    void getTradeRequestDetails_ok_returnsControllerResponse() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        when(tradeRequestService.getTradeRequest(eq("requestId1"), any())).thenReturn(request);
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
        assertEquals("getCaseDetails", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getTradeRequestDetails_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Petition Request Details", null)).when(tradeRequestService)
                .getTradeRequest(eq("requestId1"), any());
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

    @Test
    void getCompleteInfo_ok_returnsControllerResponse() throws Exception {
        // Given
        TradeExternalTaskRequest request = new TradeExternalTaskRequest();
        ControllerResponse result = ControllerResponse.success("something", request);
        when(tradeRequestService.getTradeExternalTaskRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
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
        assertEquals("getCompleteInfo", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfo_warning_returnsControllerResponse() throws Exception {
        // Given
        TradeExternalTaskRequest request = new TradeExternalTaskRequest();
        ControllerResponse result = ControllerResponse.warning("taskWasRecentlyEdited");
        result.setEntity(request);
        when(tradeRequestService.getTradeExternalTaskRequest(eq("taskId1"), eq(false), any())).thenReturn(result);
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .param("type", "complete-info")
                        .param("task_id", "taskId1")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andReturn();
        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("warning", resultJsonNode.get("type").asText());
        assertEquals("taskWasRecentlyEdited", resultJsonNode.get("key").asText());
        assertNotNull(resultJsonNode.get("entity"));
    }

    @Test
    void getCompleteInfo_error_returnsError() throws Exception {
        // Given
        doThrow(new DeliveryException("Complete Info Import Collection Request", null)).when(tradeRequestService)
                .getTradeExternalTaskRequest(eq("taskId1"), eq(false), any());
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
        assertEquals("Error sending Complete Info Import Collection Request to Flowable Work", error.get("message").asText());
    }
}
