package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.ExportCollectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(ExportCollectionController.class)
class ExportCollectionControllerTest {

    public static final String URL_TEMPLATE = "/backend/export-collections";
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ExportCollectionService exportCollectionService;

    @Test
    void getExportCollections_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(post(URL_TEMPLATE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getExportCollections_ok_returnsValidData() throws Exception {
        // Given
        ExportCollection exportCollection = new ExportCollection("001", new Customer(), new Date(1653927293741L), new Date(1653927293741L), "ref1", 1.23, "EUR", new Account());
        List<ExportCollection> exportCollectionList = List.of(exportCollection);
        when(exportCollectionService.getExportCollections(any())).thenReturn(exportCollectionList);
        ControllerResponse expectedResult = ControllerResponse.success("getExportCollections", exportCollectionList);

        // When
        MvcResult mvcResult =
                mockMvc.perform(post(URL_TEMPLATE)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                        .andReturn();

        // Then
        verify(exportCollectionService, times(1)).getExportCollections(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
