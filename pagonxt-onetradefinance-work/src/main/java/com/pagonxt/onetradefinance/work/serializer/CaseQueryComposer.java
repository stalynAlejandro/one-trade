package com.pagonxt.onetradefinance.work.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class to provide some methods to generate queries
 * In this case, they are some methods to get cases
 * @author -
 * @version jdk-11.0.13
 * @see  ElasticQueryComposer
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class CaseQueryComposer extends ElasticQueryComposer {

    /**
     * constructor method
     * @param dateFormatProperties : properties related to date format
     * @param objectMapper : provides functionality for reading and writing JSON
     */
    protected CaseQueryComposer(DateFormatProperties dateFormatProperties, ObjectMapper objectMapper) {
        super(dateFormatProperties, objectMapper);
    }

    /**
     * Method to get a case by code
     * @param code : a string with the code
     * @return a string object
     */
    public String composeGetCaseByCodeQuery(String code) {
        List<String> conditions = List.of(
                getVariableDoesNotExistCondition(FieldConstants.REGISTRATION_CANCELLED),
                getVariableContainsTextCondition(FieldConstants.OPERATION_CODE, code == null ? "" : code.trim())
        );
        return composeQuery(conditions, 0, 1);
    }

    /**
     * Method to get a case by id
     * @param id : a string with the id
     * @return a string object
     */
    public String composeGetCaseByIdQuery(String id) {
        List<String> conditions = List.of(
                getVariableDoesNotExistCondition(FieldConstants.REGISTRATION_CANCELLED),
                getPropertyHasExactTextCondition("id", id.trim())
        );
        return composeQuery(conditions, 0, 1);
    }
}
