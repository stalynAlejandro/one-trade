package com.pagonxt.onetradefinance.integrations.validation.mock;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;

/**
 * Mock class to validate a jwt token.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class JWTValidationMockImpl implements JWTValidation {

    /**
     *  Method to mock the validation by Santander.
     *
     * @param authorization : the authorization header
     *
     * @return the mock decoded JWT
     */
    public DecodedJWT verify(String authorization) {
        return JWT.decode("eyJraWQiOiJwcmVHdHNTdHNJbnRlcm5ldCIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJzdW" +
                "IiOiJKMDAwMTA0ODkyIiwiY291bnRyeSI6IiIsIm5iZiI6MTY2OTMwMTI3OSwidXNlcl90eXBlIjoiY3VzdG9tZXIiLCJp" +
                "c3MiOiJwcmVHdHNJbnRlcm5ldCIsImV4cCI6MTY2OTMwMTg3OSwiaWF0IjoxNjY5MzAxMjc5fQ.OiV12NE97_hx68qZ2Dv" +
                "3cRBrEQtsg51_LJ22EIn09M0iylidlgbZIFHpbYJSyAc_1zi-FE8C7PTyb_qziyAmA__zXzhN8nkVhD_amJq6vZLUKt166" +
                "cfwMozZv6KxaP4APt1hIZKIBuRqeVtKgvRts1WMxH_ysp1eWmcCr-6t1bBCCXdn_lgCQkB3q-h7YK1MbmFeJVzKfBxqPm-" +
                "Wsgd-e6759xaV_6rY29k3Fwu9X05pdY8dsNVBNoa1niIDfpRB4S7RU9-IqUeMZXPvGROmUiHnr3QX3DM5PeMCA4JFI-uGj" +
                "6Y875-7tNvqp9zQ0yLzitjgmlIkHQkSmURTlSZxIg");
    }
}
