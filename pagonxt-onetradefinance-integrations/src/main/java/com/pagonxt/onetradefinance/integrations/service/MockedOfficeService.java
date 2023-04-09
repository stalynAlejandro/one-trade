package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.User;

import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_MIDDLE_OFFICE;
import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.USER_TYPE_OFFICE;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get mocked offices
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.User
 * @see OfficeService
 * @since jdk-11.0.13
 */
public class MockedOfficeService implements OfficeService {
    public static final String OFFICE_1 = "1234";
    public static final String OFFICE_2 = "1235"; //without middleOffice!!!
    public static final String OFFICE_3 = "1236";
    public static final String OFFICE_4 = "1237";
    public static final String MIDDLE_OFFICE_1 = "8911"; //Offices: 1234, 1237
    public static final String MIDDLE_OFFICE_2 = "8908"; //Offices: 1236


    /**
     * This method allows to obtain the requester office
     * @param requester a User object with the requester
     * @return a string with the requester office
     */
    @Override
    public String getRequesterOffice(User requester) {
        if(!USER_TYPE_OFFICE.equals(requester.getUserType())) {
            return null;
        }
        if(requester.getUserId().contains("olivia")) {
            return OFFICE_1;
        }
        if(requester.getUserId().contains("omar")) {
            return OFFICE_4;
        }
        if(requester.getUserId().contains("oscar")) {
            return OFFICE_3;
        }
        return OFFICE_1;
    }

    /**
     * This method allows to obtain the middle office of the requester
     * @param requester a User object with the requester
     * @return a string with the middle office of the requeste
     */
    @Override
    public String getRequesterMiddleOffice(User requester) {
        if(!USER_TYPE_MIDDLE_OFFICE.equals(requester.getUserType())) {
            return null;
        }
        if(requester.getUserId().contains("marta")) {
            return MIDDLE_OFFICE_1;
        }
        if(requester.getUserId().contains("manuel")) {
            return MIDDLE_OFFICE_2;
        }
        return MIDDLE_OFFICE_1;
    }

}
