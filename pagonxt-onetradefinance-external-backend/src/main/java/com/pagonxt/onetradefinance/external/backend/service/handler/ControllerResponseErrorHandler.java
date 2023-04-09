package com.pagonxt.onetradefinance.external.backend.service.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

/**
 * Class for error handling
 * @author -
 * @version jdk-11.0.13
 * @see org.springframework.web.client.DefaultResponseErrorHandler
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
@Component
public class ControllerResponseErrorHandler extends DefaultResponseErrorHandler {

    /**
     * Class attribute
     */
    private final ObjectMapper objectMapper;

    /**
     * Class constructor
     * @param objectMapper ObjectMapper object
     */
    public ControllerResponseErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * This method class handles error
     * @param response ClientHttpResponse object
     * @param statusCode HttpStatus object
     * @see org.springframework.http.client.ClientHttpResponse
     * @see org.springframework.http.HttpStatus
     * @throws IOException handles IO exception
     */
    @Override
    protected void handleError(ClientHttpResponse response, HttpStatus statusCode) throws IOException {
        byte[] body = this.getResponseBody(response);
        Charset charset = this.getCharset(response);
        String responseBody = charset == null ? new String(body) : new String(body, charset);
        try {
            ControllerResponse controllerResponse = objectMapper.readValue(responseBody, ControllerResponse.class);
            if ("error".equals(controllerResponse.getType())) {
                if ("securityViolation".equals(controllerResponse.getKey())) {
                    handleSecurityViolation(controllerResponse);
                }
                String[] arguments = controllerResponse.getArguments().toArray(new String[0]);
                throw new ServiceException(controllerResponse.getMessage(), controllerResponse.getKey(), null,
                        (Serializable) controllerResponse.getEntity(), arguments);
            }
        } catch (IOException e) {
            super.handleError(response, statusCode);
        }
    }

    /**
     * This method handles violation security
     * @param controllerResponse ControllerResponse object
     * @see com.pagonxt.onetradefinance.integrations.model.ControllerResponse
     */
    private void handleSecurityViolation(ControllerResponse controllerResponse) {
        List<String> args = Optional.ofNullable(controllerResponse.getArguments()).orElse(List.of());
        if (args.size() >= 3) {
            throw new SecurityException(args.get(0), args.get(1), args.get(2));
        }
        if (args.size() == 2) {
            throw new SecurityException(args.get(0), args.get(1));
        }
        throw new SecurityException("provided", "perform that operation");
    }
}
