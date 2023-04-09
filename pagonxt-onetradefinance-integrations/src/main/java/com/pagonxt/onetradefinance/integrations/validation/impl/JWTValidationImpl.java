package com.pagonxt.onetradefinance.integrations.validation.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import com.santander.serenity.security.credentials.jwt.JWTTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

/**
 * Class to validate a jwt token.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class JWTValidationImpl implements JWTValidation {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(JWTValidationImpl.class);

    /**
     * Start string of the authorization bearer
     */
    public static final String BEARER_START = "Bearer ";
    /**
     * The jwt token validator
     */
    private final JWTTokenValidator jwtTokenValidator;

    /**
     * Constructor
     *
     * @param jwtTokenValidator : the jwt token validator
     */
    public JWTValidationImpl(JWTTokenValidator jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    /**
     * Method to verify that the token is validated by Santander.
     *
     * @param authorization : the authorization header
     *
     * @return the decoded JWT
     */
    @Override
    @Cacheable("jwtToken")
    public DecodedJWT verify(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_START)) {
            LOG.error("The authorization header is not correct");
            throw new ServiceException("The authorization header is not correct", "errorToken");
        }
        String jwtToken = authorization.substring(7);
        try {
            jwtTokenValidator.validate(jwtToken);
            LOG.debug("Token validated successfully");
        } catch (Exception e) {
            throw new ServiceException("The token is not valid", "errorToken", e);
        }
        return JWT.decode(jwtToken);
    }
}
