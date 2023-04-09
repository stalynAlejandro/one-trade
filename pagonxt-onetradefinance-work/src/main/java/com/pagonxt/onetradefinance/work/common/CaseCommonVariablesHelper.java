package com.pagonxt.onetradefinance.work.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.flowable.platform.common.util.JsonUtil;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.work.service.model.ExchangeInsuranceFlowable;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.REQUEST_DOCUMENTS;

/**
 * Class that provides some utilities for working with common case variables.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Service
public class CaseCommonVariablesHelper {

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .registerModule(new JavaTimeModule());

    /**
     * method to get the request documents
     * @param caseInstanceWithVariables : a CaseInstance object
     * @return a list with documents
     * @throws JsonProcessingException handles Json Processing exceptions
     */
    public List<Document> getRequestDocuments(CaseInstance caseInstanceWithVariables) throws JsonProcessingException {
        if (caseInstanceWithVariables.getCaseVariables().containsKey(REQUEST_DOCUMENTS)) {
            JsonNode requestDocumentsList = JsonUtil.asNode(mapper, caseInstanceWithVariables.getCaseVariables().get(REQUEST_DOCUMENTS));
            return Arrays.asList(mapper.readValue(requestDocumentsList.textValue(), Document[].class));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Method to build a document list
     * @param obj Object
     * @return a list of documents
     * @throws JsonProcessingException handles Json Processing exceptions
     */
    public List<Document> convertToDocumentList(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return List.of();
        }
        JsonNode requestDocumentsList = JsonUtil.asNode(mapper, obj);
        return Arrays.asList(mapper.readValue(requestDocumentsList.textValue(), Document[].class));
    }

    /**
     * Method to get a Json Node
     * @param obj Object
     * @return a JsonNode object
     * @throws JsonProcessingException handles Json Processing exceptions
     */
    public JsonNode getJsonNode(Object obj) throws JsonProcessingException {
        String jsonString = mapper.writeValueAsString(obj);
        return mapper.readTree(jsonString);
    }

    /**
     * Method to build a list of exchanges insurances
     * @param obj Object
     * @return a list of exchanges insurances
     * @throws JsonProcessingException handles Json Processing exceptions
     */
    public static List<ExchangeInsuranceFlowable> convertToExchangeInsuranceList(Object obj) throws JsonProcessingException {
        if (obj == null) {
            return List.of();
        }
        JsonNode exchangeInsuranceList = JsonUtil.asNode(mapper, obj);
        return Arrays.asList(mapper.readValue(exchangeInsuranceList.textValue(), ExchangeInsuranceFlowable[].class));
    }
}
