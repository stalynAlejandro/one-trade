package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.TestUtils;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountSearchResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(AccountController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class AccountControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/accounts";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AccountService accountService;

    @MockBean
    AccountDtoSerializer accountDtoSerializer;

    @Test
    void getClientAccounts_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("client", "001")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getClientAccounts_noParam_returnsBadRequest() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void getClientAccounts_emptyParam_returnsBadRequest() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("client", "")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void getClientAccounts_noAuth_returnsUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .param("client", "001"))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getClientAccounts_ok_returnsValidData() throws Exception {
        // Given
        Account account1 = new Account("001001", "001", "PT50 0002 0123 5678 9015 1", "00490001", "EUR");
        Account account2 = new Account("001001", "001", "PT50 0002 0123 5678 9015 2", "00490001", "PLN");
        AccountDto accountDto1 = new AccountDto("001001", "PT50 0002 0123 5678 9015 1", "EUR");
        AccountDto accountDto2 = new AccountDto("001001", "PT50 0002 0123 5678 9015 2", "PLN");
        List<Account> value = List.of(account1, account2);
        List<AccountDto> expectedResult = List.of(accountDto1, accountDto2);
        doReturn(value).when(accountService).getCustomerAccounts(any());
        when(accountDtoSerializer.toDto(account1)).thenReturn(accountDto1);
        when(accountDtoSerializer.toDto(account2)).thenReturn(accountDto2);
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                        .param("client", "001")
                        .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(AccountSearchResponse.of("001", expectedResult));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getClientAccounts_logDebug_returnsOk() throws NoSuchFieldException, IllegalAccessException {
        // Given
        AccountController accountController = new AccountController(accountService, accountDtoSerializer);
        Logger LOG = mock(Logger.class);
        TestUtils.setFinalStatic(AccountController.class.getDeclaredField("LOG"), LOG);
        when(LOG.isDebugEnabled()).thenReturn(true);
        // When
        accountController.getAccountsByClientId("clientTest");
        // Then
        verify(LOG).debug(anyString(), eq("clientTest"), any());
    }
}
