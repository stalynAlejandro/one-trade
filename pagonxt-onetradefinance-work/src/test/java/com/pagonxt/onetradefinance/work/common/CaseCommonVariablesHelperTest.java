package com.pagonxt.onetradefinance.work.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class CaseCommonVariablesHelperTest {

    @InjectMocks
    CaseCommonVariablesHelper caseCommonVariablesHelper;

    @Test
    void getRequestDocuments_ok_returnListDocuments() throws JsonProcessingException {
        CaseInstance caseInstanceMock = mock(CaseInstance.class);
        Map<String, Object> variables = new HashMap<>();
        variables.put("requestDocuments", "[\n{\n\"documentId\":\"001\",\n\"filename\":\"Un documento.pdf\"," +
                "\n\"mimeType\":\"application/pdf\",\n\t\"data\":\"File contents pdf\"\n}\n]");
        when(caseInstanceMock.getCaseVariables()).thenReturn(variables);
        List<Document> result = caseCommonVariablesHelper.getRequestDocuments(caseInstanceMock);
        assertEquals(1, result.size());
        assertEquals("001", result.get(0).getDocumentId());
   }

    @Test
    void getRequestDocuments_whenNoRequestDocument_returnEmptyListDocuments() throws JsonProcessingException {
        CaseInstance caseInstanceMock = mock(CaseInstance.class);
        Map<String, Object> variables = new HashMap<>();
        when(caseInstanceMock.getCaseVariables()).thenReturn(variables);
        List<Document> result = caseCommonVariablesHelper.getRequestDocuments(caseInstanceMock);
        assertEquals(0, result.size());
    }

    @Test
    void convertToDocumentList_ok_returnListDocuments() throws JsonProcessingException {
        List<Document> result = caseCommonVariablesHelper.convertToDocumentList("[\n{\n\"documentId\":\"001\"," +
                "\n\"filename\":\"Un documento.pdf\",\n\"mimeType\":\"application/pdf\"" +
                ",\n\t\"data\":\"File contents pdf\"\n}\n]");
        assertEquals(1, result.size());
        assertEquals("001", result.get(0).getDocumentId());
    }

    @Test
    void convertToDocumentList_whenNull_returnEmptyListDocuments() throws JsonProcessingException {
        List<Document> result = caseCommonVariablesHelper.convertToDocumentList(null);
        assertEquals(0, result.size());
    }

    @Test
    void getJsonNode_ok_returnListDocuments() throws JsonProcessingException {
        Document document = new Document("001", "Un documento.pdf", "application/pdf",
                new Date(), "documentType", "File contents pdf");
        JsonNode result = caseCommonVariablesHelper.getJsonNode(document);
        assertEquals("001", result.get("documentId").asText());
    }


    @Test
    void convertToExchangeInsuranceList_ok_returnListExchangeInsurances() throws JsonProcessingException {
        List<ExchangeInsuranceFlowable> result = CaseCommonVariablesHelper.convertToExchangeInsuranceList(
                "[\n{\n\"id_\":\"7763095\",\n\"type_\":\"forward_usa\",\n\"useDate_\":1656540000000,\n\"" +
                        "sellAvailableAmount_\": 2364.61,\n\"sellCurrency\":\"EUR\",\n\"buyAvailableAmount_\":2000," +
                        "\n\"buyCurrency\":\"GBP\",\n\"exchangeRate_\": 1.1623,\n\"amountToUse_\":1000\n}\n]");
        assertEquals(1, result.size());
        assertEquals("7763095", result.get(0).getId());
    }

    @Test
    void convertToExchangeInsuranceList_whenNull_returnEmptyListExchangeInsurances() throws JsonProcessingException {
        List<ExchangeInsuranceFlowable> result = CaseCommonVariablesHelper.convertToExchangeInsuranceList(null);
        assertEquals(0, result.size());
    }
}
