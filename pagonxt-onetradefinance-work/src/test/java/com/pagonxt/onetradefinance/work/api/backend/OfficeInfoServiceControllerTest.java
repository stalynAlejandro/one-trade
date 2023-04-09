package com.pagonxt.onetradefinance.work.api.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.OfficeInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(OfficeInfoServiceController.class)
class OfficeInfoServiceControllerTest {

    private static final String DEFINITION_KEY = "PGN_DO001";
    private static final String FIND_BY_ID = "findById";
    private static final String OFFICE = "officeCode";
    private static final String FIND_MIDDLE_OFFICE = "findMiddleOffice";
    private static final String MIDDLE_OFFICE = "middleOfficeCode";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    OfficeInfoService officeInfoService;

    @MockBean
    DataObjectRuntimeService dataObjectRuntimeService;

    @Test
    void getValidOffice_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(post("/backend/office/getValidOffice/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getValidOffice_ok_returnsValidData() throws Exception {
        // Given
        boolean result = true;
        when(officeInfoService.isValidOffice(any())).thenReturn(result);
        ControllerResponse expectedResult = ControllerResponse.success("officeExists", result);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/office/getValidOffice/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(officeInfoService, times(1)).isValidOffice(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getValidMiddleOffice_ok__returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(post("/backend/office/getValidMiddleOffice/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getValidMiddleOffice_ok_returnsValidData() throws Exception {
        // Given
        boolean result = true;
        when(officeInfoService.isValidMiddleOffice(any())).thenReturn(result);
        ControllerResponse expectedResult = ControllerResponse.success("MiddleOfficeExists", result);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/office/getValidMiddleOffice/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(officeInfoService, times(1)).isValidMiddleOffice(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getMiddleOffice_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(post("/backend/office/getMiddleOffice/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getMiddleOffice_ok_returnsValidData() throws Exception {
        // Given
        String result = "8911";
        when(officeInfoService.getMiddleOffice(any())).thenReturn(result);
        ControllerResponse expectedResult = ControllerResponse.success("middleOffice", result);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/office/getMiddleOffice/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(officeInfoService, times(1)).getMiddleOffice(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getOfficeInfo_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(post("/backend/office/getOfficeInfo/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getOfficeInfo_ok_returnsValidData() throws Exception {
        // Given
        OfficeInfo result = new OfficeInfo();
        when(officeInfoService.getOfficeInfo(any())).thenReturn(result);
        ControllerResponse expectedResult = ControllerResponse.success("officeInfo", result);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/office/getOfficeInfo/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(officeInfoService, times(1)).getOfficeInfo(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

    @Test
    void getOffices_ok_returnsOk() throws Exception {
        // Given
        // When and then
        mockMvc.perform(post("/backend/office/getOffices/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getOffics_ok_returnsValidData() throws Exception {
        // Given
        List<String> result = new ArrayList<>();
        when(officeInfoService.getOffices(any())).thenReturn(result);
        ControllerResponse expectedResult = ControllerResponse.success("offices", result);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/backend/office/getOffices/1234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn();

        // Then
        verify(officeInfoService, times(1)).getOffices(any());
        JsonNode resultJsonNode = mapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = mapper.valueToTree(expectedResult);
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }

}
