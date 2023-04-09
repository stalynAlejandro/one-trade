package com.pagonxt.onetradefinance.external.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get info about offices
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.OfficeInfo
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Service
public class OfficeInfoService {

    //Class Attributes
    private final RestTemplateWorkService restTemplateWorkService;
    private final UserInfoService userInfoService;

    /**
     * constructor method
     * @param restTemplateWorkService   : RestTemplateWorkService object
     * @param userInfoService           : UserInfoService object
     */
    public OfficeInfoService(RestTemplateWorkService restTemplateWorkService, UserInfoService userInfoService)
    {
        this.restTemplateWorkService = restTemplateWorkService;
        this.userInfoService = userInfoService;
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
        ControllerResponse response = restTemplateWorkService
                .isValidOffice(officeCode, new AuthenticatedRequest(userInfoService.getUserInfo()));
        return Boolean.parseBoolean(response.getEntity().toString());
    }

    /**
     * This method allows checking if an office code belongs to a middle Office
     * @param middleOfficeCode : a String object with the middle office code to check
     * @return true or false
     */
    public boolean isValidMiddleOffice(String middleOfficeCode) {
        ControllerResponse response = restTemplateWorkService
                .isValidMiddleOffice(middleOfficeCode, new AuthenticatedRequest(userInfoService.getUserInfo()));
        return Boolean.parseBoolean(response.getEntity().toString());
    }

    /**
     * This allows to obtain an OfficeInfo object through an office code
     * @param officeCode a string with the office code
     * @return an OfficeInfo object with info about the office
     */
    public OfficeInfo getOfficeInfo(String officeCode) {
        ControllerResponse response = restTemplateWorkService
                .getOfficeInfo(officeCode, new AuthenticatedRequest(userInfoService.getUserInfo()));
        return new ObjectMapper().convertValue(response.getEntity(), OfficeInfo.class);
    }

    /**
     * This method allows to obtain the middle office of an office
     * @param office a string with the office id
     * @return a string with the middle office
     */
    public String getMiddleOffice(String office) {
        ControllerResponse response = restTemplateWorkService
                .getMiddleOffice(office, new AuthenticatedRequest(userInfoService.getUserInfo()));
        return response.getEntity().toString();
    }

    /**
     * This method allows to obtain a list of offices to which a middle office belongs
     * @param middleOffice a string with the middle office id
     * @return a list with the offices
     */
    public List<String> getOffices(String middleOffice) {
        ControllerResponse response = restTemplateWorkService
                .getOffices(middleOffice, new AuthenticatedRequest(userInfoService.getUserInfo()));
        ArrayList<String> officeList = new ArrayList<>();
        String result = response.getEntity().toString();
        result = result.substring(1,result.length()-1);
        if (!result.isEmpty()){
            String[] offices = result.split(",");
            for (String office : offices) {
                officeList.add(office.trim());
            }
        }
        return officeList;
    }
}
