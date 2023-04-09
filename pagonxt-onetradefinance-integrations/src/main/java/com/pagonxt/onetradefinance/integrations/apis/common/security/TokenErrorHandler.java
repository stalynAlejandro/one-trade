package com.pagonxt.onetradefinance.integrations.apis.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.common.model.SantanderError;
import com.pagonxt.onetradefinance.integrations.apis.common.model.SantanderErrorResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

@Component
public class TokenErrorHandler extends DefaultResponseErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(TokenErrorHandler.class);
    private final ObjectMapper objectMapper;

    public TokenErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        byte[] body = this.getResponseBody(response);
        Charset charset = this.getCharset(response);
        String responseBody = new String(body, Objects.requireNonNull(charset));
        try {
            SantanderErrorResponse santanderErrorResponse = objectMapper.readValue(responseBody, SantanderErrorResponse.class);
            santanderErrorResponse.getErrors().forEach(e -> log.error(e.getMessage()));
            if(santanderErrorResponse.getErrors().size() == 1) {
                SantanderError error = santanderErrorResponse.getErrors().get(0);
                if (error.getMessage().equals("expired_credential") || "TOKEN_NOT_ACTIVE".equals(error.getCode())) {
                    throw new ExpiredTokenException();
                }
            }
            throw new IntegrationException();
        } catch (IOException e) {
            super.handleError(response, statusCode);
        }
    }
}