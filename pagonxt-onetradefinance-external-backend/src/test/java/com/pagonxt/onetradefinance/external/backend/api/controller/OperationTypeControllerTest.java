package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.OperationTypeDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.OperationTypeDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.integrations.model.OperationType;
import com.pagonxt.onetradefinance.integrations.service.OperationTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(OperationTypeController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class OperationTypeControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/operation-types";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    OperationTypeService operationTypeService;

    @MockBean
    OperationTypeDtoSerializer operationTypeDtoSerializer;

    @Test
    void searchOperationTypes_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .with(httpBasic("user", "user"))
                .param("product_id", "CLE")
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void searchOperationTypes_noAuth_returnsHttpUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
        ).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void searchOperationTypes_ok_returnsValidData() throws Exception {
        // Given
        OperationTypeDto type1 = new OperationTypeDto("1", "Impagados","CLE", "exportUnpaid");
        OperationTypeDto type2 = new OperationTypeDto("2", "Reliquidaciones","CLE", "exportReliquidations");
        OperationTypeDto type3 = new OperationTypeDto("3", "Otras Gestiones","CLE", "exportOtherOperations");
        List<OperationTypeDto> expectedResult =  List.of(type1, type2, type3);

        OperationType collectionType1 = new  OperationType("CLE", "1", "Impagados", "exportUnpaid");
        OperationType collectionType2 = new  OperationType("CLE", "2", "Reliquidaciones", "exportReliquidations");
        OperationType collectionType3 = new  OperationType("CLE", "3", "Otras Gestiones", "exportOtherOperations");
        List<OperationType> result = List.of(collectionType1, collectionType2, collectionType3);
        doReturn(result).when(operationTypeService).getOperationTypeByProduct(any());
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user"))
                        .param("product_id", "CLE"))
                .andReturn();

        // Then
        verify(operationTypeService).getOperationTypeByProduct(any());
    }

}


