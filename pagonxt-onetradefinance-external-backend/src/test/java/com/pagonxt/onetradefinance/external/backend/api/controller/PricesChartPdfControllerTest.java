package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.config.ControllerTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.PricesChartPdfService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(PricesChartPdfController.class)
@WithMockUser(username = "office", password = "office", authorities = {"ROLE_USER", "ROLE_OFFICE"})
class PricesChartPdfControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OfficeInfoService officeInfoService;

    @MockBean
    UserInfoService userInfoService;

    @MockBean
    PricesChartPdfService pricesChartPdfService;

    @Test
    void generateExportCollectionPricesChartPdf() throws Exception {
        // When
        MvcResult result = this.mockMvc.perform(get("/api-tradeflow/prices-charts/export-collection")
                        .param("export_collection_id", "CLE-XX")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        assertEquals(MediaType.APPLICATION_PDF_VALUE, result.getResponse().getContentType());
        assertEquals("attachment; filename=prices_chart.pdf", result.getResponse().getHeader("Content-Disposition"));
    }

    @Test
    void generateImportCollectionPricesChartPdf() throws Exception {
        // When
        MvcResult result = this.mockMvc.perform(get("/api-tradeflow/prices-charts/import-collection")
                        .param("import_collection_id", "CLI-XX")
                        .with(httpBasic("office", "office"))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        assertEquals(MediaType.APPLICATION_PDF_VALUE, result.getResponse().getContentType());
        assertEquals("attachment; filename=prices_chart.pdf", result.getResponse().getHeader("Content-Disposition"));
    }
}
