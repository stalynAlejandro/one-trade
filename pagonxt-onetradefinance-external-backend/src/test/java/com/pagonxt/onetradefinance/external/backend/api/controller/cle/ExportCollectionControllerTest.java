package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.AccountDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionService;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class ExportCollectionControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/export-collections";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExportCollectionService exportCollectionService;

    @MockBean
    ExportCollectionDtoSerializer exportCollectionDtoSerializer;

    @MockBean
    UserService userService;

    @Test
    void getExportCollectionsByCustomer_ok_returnsOk() throws Exception {
        // Given
        ExportCollection exportCollection = new ExportCollection("001", new Customer(), new Date(1653927293741L), new Date(1653927293741L), "ref1", 1.23, "EUR", new Account());
        List<ExportCollection> value = List.of(exportCollection);
        doReturn(value).when(exportCollectionService).getCustomerExportCollections(eq("BUC-1234567"), any());

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("customer", "BUC-1234567")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getExportCollectionsByCustomer_noParam_returnsBadRequest() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void getExportCollectionsByCustomer_noAuth_returnsUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                        .param("customer", "BUC-1234567"))
                .andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void getExportCollectionsByCustomers_ok_returnsValidData() throws Exception {
        // Given
        ExportCollection exportCollection = new ExportCollection("001", new Customer(), new Date(1653927293741L), new Date(1653927293741L), "ref1", 1.23, "EUR", new Account());
        ExportCollectionDto exportCollectionDto = new ExportCollectionDto("001", new CustomerDto(), "2022-05-30T18:14:53.741+02:00", "2022-05-30T18:14:53.741+02:00", "ref1", "1.23", "EUR", new AccountDto());
        List<ExportCollection> value = List.of(exportCollection);
        List<ExportCollectionDto> expectedResult = List.of(exportCollectionDto);
        doReturn(value).when(exportCollectionService).getCustomerExportCollections(eq("BUC-1234567"), any());
        when(exportCollectionDtoSerializer.toDto(exportCollection)).thenReturn(exportCollectionDto);

        // When
        MvcResult mvcResult =
                mockMvc.perform(get(URL_TEMPLATE)
                                .param("customer", "BUC-1234567")
                                .with(httpBasic("user", "user")))
                        .andReturn();

        // Then
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(new ExportCollectionList(expectedResult));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
