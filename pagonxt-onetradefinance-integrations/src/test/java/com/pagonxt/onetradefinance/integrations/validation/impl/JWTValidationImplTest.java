package com.pagonxt.onetradefinance.integrations.validation.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.santander.serenity.security.credentials.jwt.JWTTokenValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@UnitTest
class JWTValidationImplTest {

    private final String JWT_TOKEN = "eyJraWQiOiJwcmVHdHNTdHNJbnRlcm5ldCIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJzdW" +
            "IiOiJKMDAwMTA0ODkyIiwiY291bnRyeSI6Ik1YQk1TWE1YTU0iLCJuYmYiOjE2NjkxMDM5MTQsInVzZXJfdHlwZSI6ImN1c3RvbWVy" +
            "IiwiaXNzIjoicHJlR3RzSW50ZXJuZXQiLCJleHAiOjE2NjkxMDQ1MTQsImlhdCI6MTY2OTEwMzkxNH0.bqBftvAxjQgs_LQ4ekn08o" +
            "h555mdsq3gvrY5RsCxZMKqFfuB6tHEyTYEyqiUiFhRdFo28V3U2pI1FUbIJLSekizJdzUCS4XyICaCgrVmwTGSrCPGviHE3WmQvE91" +
            "p3Cc6B5UIXsXjz9xcOLcYQA7IiWTIGE5cgJeBFQkyQISOpSE2yg3qgYnjoYOkQkfPHiWxurVEFRl__Rx64kyFzViEAqZxRP8cZD5zN" +
            "cy6bfV4fmEDadrqSt6Dz7qF3HN_5LbZJfP9Jlgi0QjZy5Bl2tuWTRLpdxwcyNsbyGisMqpCPg8bHRjDgq9U00Nej6kHfXg-_oFhIHl" +
            "p7mhUcRF9cORrg";

    @InjectMocks
    JWTValidationImpl jwtValidation;
    @Mock
    JWTTokenValidator jwtTokenValidator;


    @Test
    void validate_ok_noErrorThrown() {
        DecodedJWT decodedJWT = jwtValidation.verify("Bearer " + JWT_TOKEN);
        Assertions.assertEquals("J000104892", decodedJWT.getClaim("sub").asString());
        Assertions.assertEquals("MXBMSXMXMM", decodedJWT.getClaim("country").asString());
        verify(jwtTokenValidator, times(1)).validate(JWT_TOKEN);
    }

    @Test
    void validate_whenNullAuthorization_thenThrowServiceException() {
        ServiceException exception = assertThrows(ServiceException.class,
                () -> jwtValidation.verify(null));
        assertEquals("errorToken", exception.getKey());
        assertEquals("The authorization header is not correct", exception.getMessage());
    }

    @Test
    void validate_whenInvalidAuthorization_thenThrowServiceException() {
        ServiceException exception = assertThrows(ServiceException.class,
                () -> jwtValidation.verify("invalid Authorization"));
        assertEquals("errorToken", exception.getKey());
        assertEquals("The authorization header is not correct", exception.getMessage());
    }

    @Test
    void validate_whenError_thenThrowServiceException() {
        Mockito.doThrow(BadCredentialsException.class).when(jwtTokenValidator).validate(JWT_TOKEN);
        ServiceException exception = assertThrows(ServiceException.class,
                () -> jwtValidation.verify("Bearer " + JWT_TOKEN));
        assertEquals("errorToken", exception.getKey());
        assertEquals("The token is not valid", exception.getMessage());
    }
}
