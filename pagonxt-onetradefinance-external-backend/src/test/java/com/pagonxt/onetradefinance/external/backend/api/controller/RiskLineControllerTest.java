package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLinesList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.utils.QueryUtils;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import com.pagonxt.onetradefinance.integrations.service.RiskLineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(RiskLineController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class RiskLineControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/risk-lines";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    RiskLineService riskLineService;
    @MockBean
    RiskLineDtoSerializer riskLineDtoSerializer;
    @MockBean
    QueryUtils queryUtils;

    private final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    void getClientRiskLines_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("client", "001")
                        .param("product_id", "CLE")
                        .param("expiration_date", "2022-10-27")
                        .param("operation_amount", "100.34")
                        .param("operation_currency", "EUR")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getClientRiskLines_noParam_returnsBadRequest() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void getClientRiskLines_emptyParam_returnsBadRequest() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("client", "")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void getClientRiskLines_noAuth_returnsUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("client", "001"))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getClientRiskLines_ok_returnsValidData() throws Exception {
        // Given
        Date date1 = dateFormat.parse("26/06/2020");
        Date date2 = dateFormat.parse("22/02/2020");
        RiskLine riskLine1 = new RiskLine("001").with("001", "0049 3295 2020 15792", "approved", "1020", "1020", date1, "EUR");
        RiskLine riskLine2 = new RiskLine("002").with("001", "0049 3295 2020 28222", "waiting", "2120", "1980", date2, "EUR");
        RiskLineDto riskLineDto1 = new RiskLineDto("001").with("001", "0049 3295 2020 15792", "approved", "1020", "1020", "26/06/2020", "EUR");
        RiskLineDto riskLineDto2 = new RiskLineDto("002").with("001", "0049 3295 2020 28222", "waiting", "2120", "1980", "22/02/2020", "EUR");

        List<RiskLine> value = List.of(riskLine1, riskLine2);
        List<RiskLineDto> expectedResult = List.of(riskLineDto1, riskLineDto2);
        doReturn(value).when(riskLineService).getCustomerRiskLines(any());
        when(riskLineDtoSerializer.toDto(riskLine1)).thenReturn(riskLineDto1);
        when(riskLineDtoSerializer.toDto(riskLine2)).thenReturn(riskLineDto2);

        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                                .param("client", "001")
                                .param("product_id", "CLE")
                                .param("expiration_date", "2022-10-27")
                                .param("operation_amount", "100.34")
                                .param("operation_currency", "EUR")
                                .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(new RiskLinesList(expectedResult));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getClientRiskLines_nullExpirationDate_returnsEmptyArray() throws Exception {
        // Given
        RiskLinesList expectedResult = new RiskLinesList(new ArrayList<>());
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                                .param("client", "001")
                                .param("product_id", "CLE")
                                .param("operation_amount", "100.34")
                                .param("operation_currency", "EUR")
                                .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getClientRiskLines_nullOperationAmount_returnsEmptyArray() throws Exception {
        // Given
        RiskLinesList expectedResult = new RiskLinesList(new ArrayList<>());
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                                .param("client", "001")
                                .param("product_id", "CLE")
                                .param("expiration_date", "2022-10-27")
                                .param("operation_currency", "EUR")
                                .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getClientRiskLines_nullOperationCurrency_returnsEmptyArray() throws Exception {
        // Given
        RiskLinesList expectedResult = new RiskLinesList(new ArrayList<>());
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                                .param("client", "001")
                                .param("product_id", "CLE")
                                .param("expiration_date", "2022-10-27")
                                .param("operation_amount", "100.34")
                                .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
