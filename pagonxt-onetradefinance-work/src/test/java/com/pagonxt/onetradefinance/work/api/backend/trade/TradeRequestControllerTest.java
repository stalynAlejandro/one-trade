package com.pagonxt.onetradefinance.work.api.backend.trade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.TaskOperationService;
import com.pagonxt.onetradefinance.work.service.trade.TradeRequestService;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(TradeRequestController.class)
class TradeRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TradeRequestService tradeRequestService;

    @MockBean
    TaskOperationService taskOperationService;

    @MockBean(answer = Answers.RETURNS_DEEP_STUBS)
    CmmnRuntimeService cmmnRuntimeService;

    @Test
    void createTradeRequest_returnsOk() throws Exception{
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class, Answers.RETURNS_DEEP_STUBS);
        when(caseInstance.getCaseVariables().get("operationCode")).thenReturn("CLI-001");
        when(cmmnRuntimeService
                .createCaseInstanceBuilder()
                .caseDefinitionKey(any())
                .variables(any())
                .start()).thenReturn(caseInstance);

        // When and then
        mockMvc.perform(post("/backend/trade-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void createTradeRequest_returnsValidResponse() throws Exception {
        // Given
        CaseInstance caseInstance = mock(CaseInstance.class, Answers.RETURNS_DEEP_STUBS);
        when(caseInstance.getCaseVariables().get("operationCode")).thenReturn("CLI-001");
        when(cmmnRuntimeService
                .createCaseInstanceBuilder()
                .caseDefinitionKey(any())
                .variables(any())
                .start()).thenReturn(caseInstance);
        JsonNode expectedJsonNode = mapper.valueToTree(ControllerResponse.success("tradeRequestCreated", null));

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/trade-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void updateTradeRequest_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(put("/backend/trade-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void updateTradeRequest_returnsValidResponse() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        doReturn(request).when(tradeRequestService).updateTradeRequest(any());

        // When
        MvcResult mvcResult = mockMvc.perform(put("/backend/trade-request")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("tradeRequestUpdated", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode expectedJsonNode = mapper.valueToTree(request);
        assertEquals(expectedJsonNode, resultJsonNode.get("entity"), "Returned entity should match pattern");
    }

    @Test
    void confirmTradeRequest_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(put("/backend/trade-request/myCode/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void confirmTradeRequest_returnsValidResponse() throws Exception {
        // Given

        // When
        MvcResult mvcResult = mockMvc.perform(put("/backend/trade-request/myCode/confirm")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("tradeRequestConfirmed", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode entity = resultJsonNode.get("entity");
        assertTrue(resultJsonNode.get("entity").isEmpty(), "Entity is empty");
    }

    @Test
    void getTradeRequest_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post("/backend/trade-request/get/1")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getTradeRequest_returnsValidResponse() throws Exception {
        // Given
        TradeRequest request = new TradeRequest();
        request.setCode("1");
        doReturn(request).when(tradeRequestService).getRequestByCode(any(),any());

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/trade-request/get/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("getTradeRequest", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode expectedJsonNode = mapper.valueToTree(request);
        assertEquals(expectedJsonNode, resultJsonNode.get("entity"), "Returned entity should match pattern");
    }

    @Test
    void getTradeExternalTaskById_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post("/backend/trade-request/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getTradeExternalTaskById_returnsValidResponse() throws Exception {
        // Given
        TradeExternalTaskRequest request = new TradeExternalTaskRequest();
        when(tradeRequestService.getTradeExternalTaskByTaskId(any(),any())).thenReturn(request);
        doReturn(request).when(tradeRequestService).getTradeExternalTaskByTaskId(any(),any());

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/trade-request/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("getTradeExternalTaskById", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode expectedJsonNode = mapper.valueToTree(request);
        assertEquals(expectedJsonNode, resultJsonNode.get("entity"), "Returned entity should match pattern");
    }
    @Test
    void searchContractsByContractType_ok_returnsOk() throws Exception {
        //Given

        //When and then
        mockMvc.perform(post("/backend/trade-request/contracts/cli_request/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    void searchContractsByContractType_ok_returnsValidData() throws Exception {
        //Given
        TradeContract tradeContract = new TradeContract();
        tradeContract.setCode("code");
        tradeContract.setContractReference("contractReference");
        List<TradeContract> tradeContractList = List.of(tradeContract);
        when(tradeRequestService.getContracts(any(), any())).thenReturn(tradeContractList);
        ControllerResponse expectedResult = ControllerResponse.success("searchContractsByContractType", tradeContractList);
        //When
        MvcResult mvcResult = mockMvc.perform(post("/backend/trade-request/contracts/cli_request/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
        //Then
        verify(tradeRequestService, times(1)).getContracts(any(), any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
    @Test
    void externalTaskCompleteInfo_ok_returnsOk() throws Exception {
        //Given

        //When and then
        mockMvc.perform(put("/backend/trade-request/tasks/myTaskId/complete-info")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    void externalTaskCompleteInfo_returnsValidResponse() throws Exception {
        // Given
        ControllerResponse response = new ControllerResponse();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/backend/trade-request/tasks/myTaskId/complete-info")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("externalTaskCompleteInfo", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode entity = resultJsonNode.get("entity");
        assertTrue(resultJsonNode.get("entity").isEmpty(), "Entity is empty");
    }

    @Test
    void externalTaskRequestCancellation_ok_returnsOk() throws Exception {
        //Given

        //When and then
        mockMvc.perform(put("/backend/trade-request/tasks/myTaskId/request-cancellation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    void externalTaskRequestCancellation_returnsValidResponse() throws Exception {
        // Given
        ControllerResponse response = new ControllerResponse();

        // When
        MvcResult mvcResult = mockMvc.perform(put("/backend/trade-request/tasks/myTaskId/request-cancellation")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertEquals("success", resultJsonNode.get("type").asText(), "Should return a success response type");
        assertEquals("externalTaskRequestCancellation", resultJsonNode.get("key").asText(), "Should return a valid response key");
        JsonNode entity = resultJsonNode.get("entity");
        assertTrue(resultJsonNode.get("entity").isEmpty(), "Entity is empty");
    }
}