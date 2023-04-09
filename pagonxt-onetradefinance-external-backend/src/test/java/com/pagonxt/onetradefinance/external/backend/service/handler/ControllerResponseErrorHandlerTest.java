package com.pagonxt.onetradefinance.external.backend.service.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.HttpServerErrorException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@UnitTest
class ControllerResponseErrorHandlerTest {

    @Spy
    private final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ControllerResponseErrorHandler controllerResponseErrorHandler;

    @Test
    void handleError_securityViolation_throwsSecurityException() throws JsonProcessingException {
        // Given
        String bodyString = mapper.writeValueAsString(ControllerResponse.error("securityViolation", "user", "do that"));
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(bodyString.getBytes(StandardCharsets.UTF_8));
        ClientHttpResponse response = composeResponse(bodyStream);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> controllerResponseErrorHandler.handleError(response, statusCode),
                "Should throw SecurityException");

        // Then
        assertEquals("User user has no permission to do that", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void handleError_errorControllerResponse_throwsServiceException() throws JsonProcessingException {
        // Given
        ControllerResponse error = new ControllerResponse("error", "someError", "an error has occurred", null, (String[]) null);
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(mapper.writeValueAsString(error).getBytes(StandardCharsets.UTF_8));
        ClientHttpResponse response = composeResponse(bodyStream);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        // When
        Exception thrown = assertThrows(ServiceException.class,
                () -> controllerResponseErrorHandler.handleError(response, statusCode),
                "Should throw ServiceException");

        // Then
        assertEquals("an error has occurred", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void handleError_nonErrorControllerResponse_doesNotThrowException() throws JsonProcessingException {
        // Given
        ControllerResponse error = new ControllerResponse("warning", "somethingHappened", "this is not an error", null, (String[]) null);
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(mapper.writeValueAsString(error).getBytes(StandardCharsets.UTF_8));
        ClientHttpResponse response = composeResponse(bodyStream);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        // When and then
        assertDoesNotThrow(
                () -> controllerResponseErrorHandler.handleError(response, statusCode),
                "Should throw ServiceException");
    }

    @Test
    void handleError_notControllerResponse_fallsBackToDefaultImplementation() {
        // Given
        ByteArrayInputStream bodyStream = new ByteArrayInputStream("not a controller response".getBytes(StandardCharsets.UTF_8));
        ClientHttpResponse response = composeResponse(bodyStream);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        // When
        Exception thrown = assertThrows(HttpServerErrorException.class,
                () -> controllerResponseErrorHandler.handleError(response, statusCode),
                "Should throw HttpServerErrorException");

        // Then
        assertEquals("500 Internal Server Error: [no body]", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void handleError_securityViolationWith3Arguments_throwsSecurityExceptionWith3Arguments() throws JsonProcessingException {
        // Given
        String bodyString = mapper.writeValueAsString(ControllerResponse.error("securityViolation", "user", "resource", "type"));
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(bodyString.getBytes(StandardCharsets.UTF_8));
        ClientHttpResponse response = composeResponse(bodyStream);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> controllerResponseErrorHandler.handleError(response, statusCode),
                "Should throw SecurityException");

        // Then
        assertEquals("User user has no access to resource resource of type type", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void handleError_securityViolationWithNoArguments_throwsGenericSecurityException() throws JsonProcessingException {
        // Given
        String bodyString = mapper.writeValueAsString(ControllerResponse.error("securityViolation", new String[0]));
        ByteArrayInputStream bodyStream = new ByteArrayInputStream(bodyString.getBytes(StandardCharsets.UTF_8));
        ClientHttpResponse response = composeResponse(bodyStream);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        // When
        Exception thrown = assertThrows(SecurityException.class,
                () -> controllerResponseErrorHandler.handleError(response, statusCode),
                "Should throw SecurityException");

        // Then
        assertEquals("User provided has no permission to perform that operation", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    @Test
    void handleError_errorDecoding_fallsBackToDefaultImplementation() throws Exception {
        // Given
        ByteArrayInputStream bodyStream = new ByteArrayInputStream("{invalid json".getBytes(StandardCharsets.UTF_8));
        ClientHttpResponse response = composeResponse(bodyStream);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        doThrow(new InvalidFormatException((JsonParser) null, null, null, null)).when(mapper).readValue(anyString(), eq(ControllerResponse.class));

        // When
        Exception thrown = assertThrows(HttpServerErrorException.class,
                () -> controllerResponseErrorHandler.handleError(response, statusCode),
                "Should throw HttpServerErrorException");

        // Then
        assertEquals("500 Internal Server Error: [no body]", thrown.getMessage(), "Exception thrown should have a valid message");
    }

    private ClientHttpResponse composeResponse(InputStream bodyStream) {
        ClientHttpResponse response = new MockClientHttpResponse(bodyStream, HttpStatus.INTERNAL_SERVER_ERROR);
        MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        response.getHeaders().setContentType(contentType);
        return response;
    }
}
