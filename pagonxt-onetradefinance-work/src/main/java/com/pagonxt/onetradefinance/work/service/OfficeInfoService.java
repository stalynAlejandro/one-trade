package com.pagonxt.onetradefinance.work.service;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OfficeInfoService {

    private static final String DEFINITION_KEY = "PGN_DO001";
    private static final String FIND_BY_ID = "findById";
    private static final String OFFICE = "officeCode";
    private static final String FIND_MIDDLE_OFFICE = "findMiddleOffice";
    private static final String MIDDLE_OFFICE = "middleOfficeCode";
    private static final String DEREGISTRATION_DATE = "deregistrationDate";

    //class attribute
    private final DataObjectRuntimeService dataObjectRuntimeService;

    /**
     * Constructor Method
     * @param dataObjectRuntimeService : DataObjectRuntimeService object
     */
    public OfficeInfoService(DataObjectRuntimeService dataObjectRuntimeService) {
        this.dataObjectRuntimeService = dataObjectRuntimeService;
    }

    /**
     * This method allows checking if an office exists
     * or if the user is middleOffice type, he mustn't introduce a middleOffice code
     * If the office does not exist or, it is a middle office, a message will appear in the external
     * application warning that the office is incorrect
     * @param officeCode a string with the office code
     * @return true or false if the code is valid
     */
    public boolean isValidOffice(String officeCode) {
        DataObjectInstanceVariableContainerQuery dataObjectQueryOffice = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY)
                .operation(FIND_BY_ID)
                .value(OFFICE,  officeCode);
        DataObjectInstanceVariableContainer office = dataObjectQueryOffice.singleResult();
        return office != null
                && !isValidMiddleOffice(officeCode)
                && office.getLocalDate(DEREGISTRATION_DATE) == null;
    }

    /**
     * This method allows checking if an office code belongs to a middle Office
     * @param officeCode : a String object with the middle office code to check
     * @return true or false
     */
    public boolean isValidMiddleOffice(String officeCode) {
        DataObjectInstanceVariableContainerQuery dataObjectQueryMiddleOffice = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY)
                .operation(FIND_MIDDLE_OFFICE)
                .value(MIDDLE_OFFICE,  officeCode);
        List<DataObjectInstanceVariableContainer> results = dataObjectQueryMiddleOffice.list();
        return !results.isEmpty();
    }

    /**
     * This method allows to obtain the middle office of an office
     * @param officeId a string with the office id
     * @return a string with the middle office
     */
    public String getMiddleOffice(String officeId) {
        DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation(FIND_BY_ID).value(OFFICE,  officeId);
        DataObjectInstanceVariableContainer result = dataObjectQuery.singleResult();
        if (result!=null && result.getLocalDate(DEREGISTRATION_DATE) == null ) {
            return result.getString(MIDDLE_OFFICE);
        } else {
            return "none";
        }
    }

    /**
     * This allows to obtain an OfficeInfo object through an office code
     * @param officeCode a string with the office code
     * @return an OfficeInfo object with info about the office
     */
    public OfficeInfo getOfficeInfo(String officeCode) {
        OfficeInfo officeInfo = new OfficeInfo();
        DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation(FIND_BY_ID).value(OFFICE,  officeCode);
        DataObjectInstanceVariableContainer result = dataObjectQuery.singleResult();
        if (result!=null && result.getLocalDate(DEREGISTRATION_DATE) == null) {
            officeInfo.setCountry(result.getString("country"));
            officeInfo.setOffice(result.getString("office"));
            officeInfo.setAddress(result.getString("address"));
            officeInfo.setPlace(result.getString("place"));
        }
        return officeInfo;
    }

    /**
     * This method allows to obtain a list of offices to which a middle office belongs
     * @param officeId a string with the middle office id
     * @return a list with the offices
     */
    public List<String> getOffices(String officeId) {
        DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(DEFINITION_KEY).operation(FIND_MIDDLE_OFFICE).value(MIDDLE_OFFICE,  officeId);
        List<DataObjectInstanceVariableContainer> results = dataObjectQuery.list();
        List<String> offices = new ArrayList<>();
        if(!results.isEmpty()) {
            for (DataObjectInstanceVariableContainer result : results) {
                offices.add(result.getString("office"));
            }
        }
        return offices;
    }
}
