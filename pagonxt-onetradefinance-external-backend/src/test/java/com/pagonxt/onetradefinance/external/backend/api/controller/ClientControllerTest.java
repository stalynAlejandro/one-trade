package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.TestUtils;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerSearchResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ClientController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class ClientControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/clients";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CustomerService customerService;

    @MockBean
    CustomerDtoSerializer customerDtoSerializer;

    @Test
    void searchClients_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .param("name", "cola s-l.")
                .with(httpBasic("user", "user"))
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void searchClients_noAuth_returnsHttpUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .param("name", "cola s-l.")
        ).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void searchClients_noNameParam_returnsBadRequest() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .with(httpBasic("user", "user"))
        ).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void searchClients_emptyNameParam_returnsBadRequest() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .param("name", "")
                .with(httpBasic("user", "user"))
        ).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void searchClients_ok_returnsValidData() throws Exception {
        // Given
        Customer customer = new Customer("001", "ES", "Coca Cola S.L.", "B1234567", "1234", "BUC-1234567", "SME", "mock email");
        CustomerDto customerDto = new CustomerDto("001", "Coca Cola S.L.", "B1234567", "1234", "BUC-1234567", "SME", "mock email");
        List<Customer> value = List.of(customer);
        List<CustomerDto> expectedResult = List.of(customerDto);
        doReturn(value).when(customerService).searchCustomers(any());
        when(customerDtoSerializer.toDto(customer)).thenReturn(customerDto);

        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                        .param("name", "cola s-l.")
                        .with(httpBasic("user", "user"))
                ).andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(new CustomerSearchResponse(expectedResult));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void searchClients_logDebug_returnsOk() throws NoSuchFieldException, IllegalAccessException {
        // Given
        ClientController clientController = new ClientController(customerService, customerDtoSerializer);
        Logger LOG = mock(Logger.class);
        TestUtils.setFinalStatic(ClientController.class.getDeclaredField("LOG"), LOG);
        when(LOG.isDebugEnabled()).thenReturn(true);
        // When
        clientController.searchClients("nameTest", null, null, null);
        // Then
        verify(LOG).debug(anyString(), any(), any());
    }
}
