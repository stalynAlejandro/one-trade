package com.pagonxt.onetradefinance.work.service;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import org.springframework.stereotype.Service;

@Service
public class RecipientsService {

    //static final variables
    private static final String DEFINITION_KEY = "PGN_DO001";
    private static final String FIND_BY_ID = "findById";
    private static final String OFFICE = "officeCode";
    private static final String EMAIL = "email";

    //member variable
    private final DataObjectRuntimeService dataObjectRuntimeService;

    /**
     * Constructor Method
     * @param dataObjectRuntimeService : a DataObjectRuntimeService
     */
    public RecipientsService(DataObjectRuntimeService dataObjectRuntimeService) {
        this.dataObjectRuntimeService = dataObjectRuntimeService;
    }

    /**
     * This method queries the pgn_offices table and return the office email
     * @param officeId : a String object with the office id
     * @return a String with the office email
     */
    public String getOfficeEmail(String officeId) {
        DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation(FIND_BY_ID).value(OFFICE,  officeId);
        DataObjectInstanceVariableContainer result = dataObjectQuery.singleResult();
        if(result != null){
            return result.getString(EMAIL);
        } else {
            return "";
        }
    }

    /**
     * This method queries the pgn_offices table and return the middle office email
     * @param middleOfficeId : a String object with the middle office id
     * @return a String with the middle office email
     */
    public String getMiddleOfficeEmail(String middleOfficeId) {
        DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation(FIND_BY_ID).value(OFFICE,  middleOfficeId);
        DataObjectInstanceVariableContainer result = dataObjectQuery.singleResult();
        if(result != null){
            return result.getString(EMAIL);
        } else {
            return "";
        }
    }

}
