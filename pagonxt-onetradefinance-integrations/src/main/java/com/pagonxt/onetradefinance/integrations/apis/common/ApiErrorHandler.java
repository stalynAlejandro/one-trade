package com.pagonxt.onetradefinance.integrations.apis.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.common.model.SantanderErrorResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Class to handle api errors in api operations(query,...)
 * @author -
 * @version jdk-11.0.13
 * @see org.springframework.web.client.DefaultResponseErrorHandler
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Component
public class ApiErrorHandler extends DefaultResponseErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ApiErrorHandler.class);
    private final ObjectMapper objectMapper;

    /**
     * Class constructor
     * @param objectMapper to serialize Java objects into JSON and deserialize JSON string into Java objects.
     */
    public ApiErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Class method to handle errors
     * @param response ClientHttpResponse object
     * @param statusCode HttpStatus object
     * @see org.springframework.http.HttpStatus
     * @see org.springframework.http.client.ClientHttpResponse
     * @throws IOException handles IO exceptions
     */
    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        byte[] body = this.getResponseBody(response);
        Charset charset = this.getCharset(response);
        String responseBody = charset == null ?
                new String(body) :
                new String(body, charset);
        try {
            SantanderErrorResponse santanderErrorResponse = objectMapper
                    .readValue(responseBody, SantanderErrorResponse.class);
            santanderErrorResponse.getErrors().forEach(e -> LOG.error(e.getMessage()));
            throw new IntegrationException();
        } catch (IOException e) {
            super.handleError(response, statusCode);
        }
    }
}
