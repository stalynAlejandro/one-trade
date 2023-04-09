package com.pagonxt.onetradefinance.external.backend.api.controller.cli;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.TestUtils;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.ImportCollectionDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeContractService;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
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

@ControllerTest(ImportCollectionController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class ImportCollectionControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/import-collections";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    TradeContractService tradeContractService;

    @MockBean
    ImportCollectionDtoSerializer importCollectionDtoSerializer;

    @MockBean
    UserInfoService userInfoService;

    @Test
    void getImportCollectionsByCustomer_ok_returnsOk() throws Exception {
        // Given
        doReturn(List.of(new TradeContract()))
                .when(tradeContractService).getCustomerImportCollections(eq("BUC-1234567"), any());
        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("customer", "BUC-1234567")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getImportCollectionsByCustomer_noParam_returnsBadRequest() throws Exception {
        // Given
        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void getImportCollectionsByCustomer_noAuth_returnsUnauthorized() throws Exception {
        // Given
        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("customer", "BUC-1234567"))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getImportCollectionsByCustomers_ok_returnsValidData() throws Exception {
        // Given
        TradeContract tradeContract = new TradeContract();
        ImportCollectionDto importCollectionDto = new ImportCollectionDto();
        List<ImportCollectionDto> expectedResult = List.of(importCollectionDto);
        doReturn(List.of(tradeContract))
                .when(tradeContractService).getCustomerImportCollections(eq("BUC-1234567"), any());
        when(importCollectionDtoSerializer.toDto(tradeContract)).thenReturn(importCollectionDto);
        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                                .param("customer", "BUC-1234567")
                                .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(new ImportCollectionList(expectedResult));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getImportCollectionsByCustomer_logDebug_returnsOk() throws NoSuchFieldException, IllegalAccessException {
        // Given
        ImportCollectionController importCollectionController = new ImportCollectionController(tradeContractService, importCollectionDtoSerializer, userInfoService);
        Logger LOG = mock(Logger.class);
        TestUtils.setFinalStatic(ImportCollectionController.class.getDeclaredField("LOG"), LOG);
        when(LOG.isDebugEnabled()).thenReturn(true);
        TradeContract tradeContract = new TradeContract();
        ImportCollectionDto importCollectionDto = new ImportCollectionDto();
        doReturn(List.of(tradeContract))
                .when(tradeContractService).getCustomerImportCollections(eq("BUC-1234567"), any());
        when(importCollectionDtoSerializer.toDto(tradeContract)).thenReturn(importCollectionDto);
        // When
        importCollectionController.getImportCollectionsByCustomer("BUC-1234567");
        // Then
        verify(LOG).debug(anyString(), any(), any());
    }
}
