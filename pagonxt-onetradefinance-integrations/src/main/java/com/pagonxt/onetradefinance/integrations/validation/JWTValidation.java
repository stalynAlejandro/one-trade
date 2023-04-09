package com.pagonxt.onetradefinance.integrations.validation;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * Interface to validate a jwt token.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */

public interface JWTValidation {

    /**
     * Method to verify that the token is validated.
     *
     * @param authorization : the authorization header
     *
     * @return the decoded JWT
     */
    DecodedJWT verify(String authorization);
}
