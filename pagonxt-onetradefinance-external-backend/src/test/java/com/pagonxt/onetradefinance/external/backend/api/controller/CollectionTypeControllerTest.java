package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CollectionTypeDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CollectionTypeDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.integrations.model.CollectionType;
import com.pagonxt.onetradefinance.integrations.service.CollectionTypeService;
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

@ControllerTest(CollectionTypeController.class)
@WithMockUser(username = "user", password = "user", authorities = { "ROLE_USER" })
class CollectionTypeControllerTest {

    public static final String URL_TEMPLATE = "/api-tradeflow/collection-types";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CollectionTypeService collectionTypeService;

    @MockBean
    CollectionTypeDtoSerializer collectionTypeDtoSerializer;

    @Test
    void searchCollectionTypes_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
                .with(httpBasic("user", "user"))
                .param("product_id", "CLE")
                .param("currency", "EUR")
            ).andExpect(status().isOk()).andReturn();
    }

    @Test
    @WithAnonymousUser
    void searchCollectionTypes_noAuth_returnsHttpUnauthorized() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get(URL_TEMPLATE)
            ).andExpect(status().isUnauthorized()).andReturn();
    }

    @Test
    void searchCollectionTypes_ok_returnsValidData() throws Exception {
        // Given
        CollectionTypeDto type1 = new CollectionTypeDto("6556010000003", "Remesas de exportacion gestion electronica","CLE", "exportDocumentary");
        CollectionTypeDto type2 = new CollectionTypeDto("6556010000001", "Remesas de exportacion simple y gestion especial","CLE", "exportElectronicManagement");
        CollectionTypeDto type3 = new CollectionTypeDto("6556010000002", "Remesas de exportacion documentarias","CLE", "exportSimpleSpecialManagement");
        List<CollectionTypeDto> expectedResult = List.of(type1, type2, type3);

        CollectionType collectionType1 = new CollectionType("CLE", "6556010000001", "Remesas de exportacion simple y gestion especial", "exportElectronicManagement", List.of("USD"));
        CollectionType collectionType2 = new CollectionType("CLE", "6556010000002", "Remesas de exportacion documentarias", "exportSimpleSpecialManagement", List.of("EUR", "USD"));
        CollectionType collectionType3 = new CollectionType("CLE", "6556010000003", "Remesas de exportacion gestion electronica", "exportDocumentary", List.of("EUR", "USD"));
        List<CollectionType> result = List.of(collectionType1, collectionType2, collectionType3);
        doReturn(result).when(collectionTypeService).getCollectionType(any(), any());
        // When
        MvcResult mvcResult = mockMvc.perform(get(URL_TEMPLATE)
                        .with(httpBasic("user", "user"))
                        .param("product_id", "CLE")
                        .param("currency", "USD"))
                .andReturn();

        // Then
        verify(collectionTypeService).getCollectionType(any(), any());
    }
}
