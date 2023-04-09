package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.ExchangeInsuranceDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.utils.QueryUtils;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.service.FxDealService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(FxDealController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class FxDealControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/fx-deals";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    FxDealService fxDealService;
    @MockBean
    DateFormatProperties dateFormatProperties;
    @MockBean
    ExchangeInsuranceDtoSerializer exchangeInsuranceDtoSerializer;
    @MockBean
    QueryUtils queryUtils;

    @Test
    void getExchangeInsurances_ok_returnsOk() throws Exception {
        // Given
        List<ExchangeInsurance> deals = List.of();
        when(fxDealService.searchFxDeals(any())).thenReturn(deals);

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("office", "office"))
                        .param("customer_id", "001")
                        .param("currency_buy", "EUR")
                        .param("currency_sell", "GBP")
                        .param("buy", "true")
                        .param("amount", "120.23")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getExchangeInsurances_ok_returnsExchangeInsuranceResponse() throws Exception {
        // Given
        ExchangeInsurance deal = mock(ExchangeInsurance.class);
        when(deal.getExchangeInsuranceId()).thenReturn("id");
        when(deal.getType()).thenReturn("FORWARD");
        when(fxDealService.searchFxDeals(any())).thenReturn(List.of(deal));
        ExchangeInsuranceDto item = new ExchangeInsuranceDto();
        item.setExchangeInsuranceId("id");
        item.setType("FORWARD");
        item.setExchangeRate("0.0");
        when(exchangeInsuranceDtoSerializer.toDto(deal)).thenReturn(item);
        ExchangeInsuranceResponse expectedResult = new ExchangeInsuranceResponse(List.of(item));
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);

        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("office", "office"))
                        .param("customer_id", "001")
                        .param("currency_buy", "EUR")
                        .param("currency_sell", "GBP")
                        .param("buy", "true")
                        .param("amount", "120.23")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        assertTrue(resultJsonNode.isArray(), "Result should be a list");
        assertEquals(expectedJsonNode, resultJsonNode, "Result should match pattern");
    }
}
