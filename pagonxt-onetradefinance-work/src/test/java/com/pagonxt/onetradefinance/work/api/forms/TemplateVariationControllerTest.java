package com.pagonxt.onetradefinance.work.api.forms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.work.api.model.ContentStringResponse;
import com.pagonxt.onetradefinance.work.config.ControllerTest;
import com.pagonxt.onetradefinance.work.service.exception.TemplateVariationNotFoundException;
import com.pagonxt.onetradefinance.work.service.TemplateVariationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.doReturn;


@ControllerTest(TemplateVariationController.class)
class TemplateVariationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TemplateVariationService templateVariationService;

    @Test
    void getReturnReasonCommentTemplate_ok_returnsOk() throws Exception {
        // Given

        // When and then
        mockMvc.perform(get("/forms/template/PGN_TPL001")
                .param("returnReasonKey", "accountWithWarningsOutOfBalanceErroneous")
                .param("languageCode", "es_es"))
            .andExpect(status().isOk()).andReturn();
    }

    @Test
    void getReturnReasonCommentTemplate_wrongVariation_returnsNotFound() throws Exception {
        // Given
        doThrow(TemplateVariationNotFoundException.class).when(templateVariationService)
                .getReturnReasonCommentTemplateVariation("foo", "var");

        // When and then
        mockMvc.perform(get("/forms/template/PGN_TPL001")
                        .param("returnReasonKey", "foo")
                        .param("languageCode", "var"))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void getReturnReasonCommentTemplate_ok_returnsValidData() throws Exception {
        // Given
        doReturn("result").when(templateVariationService)
                .getReturnReasonCommentTemplateVariation("confirmDuplicity", "es_es");

        // When
        MvcResult mvcResult = mockMvc.perform(get("/forms/template/PGN_TPL001")
                        .param("returnReasonKey", "confirmDuplicity")
                        .param("languageCode", "es_es"))
                .andReturn();

        // Then
        JsonNode resultJsonNode = objectMapper.readTree(mvcResult.getResponse().getContentAsString());
        JsonNode expectedJsonNode = objectMapper.valueToTree(new ContentStringResponse("result"));
        assertThat(resultJsonNode).isEqualTo(expectedJsonNode);
    }
}
