package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CurrencyDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import com.pagonxt.onetradefinance.integrations.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(CurrencyController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class CurrencyControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/currencies";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    CurrencyService currencyService;
    @MockBean
    CurrencyDtoSerializer currencyDtoSerializer;

    @Test
    void getCurrencies_ok_returnsOk() throws Exception {
        // Given, When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("product", "productTest")
                        .param("event", "eventTest")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getCurrencies_noParam_returnsBadRequest() throws Exception {
        // Given, When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void getClientAccounts_noAuth_returnsUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getClientAccounts_ok_returnsValidData() throws Exception {
        // Given
        CurrencyDAO currencyDAO1 = new CurrencyDAO();
        currencyDAO1.setCurrency("EUR");
        CurrencyDto currencyDto1 = new CurrencyDto();
        currencyDto1.setId("EUR");
        currencyDto1.setCurrency("EUR");
        List<CurrencyDAO> value = List.of(currencyDAO1);
        List<CurrencyDto> expectedResult = List.of(currencyDto1);
        when(currencyService.getCurrencyList("productTest", "eventTest")).thenReturn(value);
        when(currencyDtoSerializer.toDto(currencyDAO1)).thenReturn(currencyDto1);
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                                .param("product", "productTest")
                                .param("event", "eventTest")
                                .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(new CurrencyResponse(expectedResult));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
