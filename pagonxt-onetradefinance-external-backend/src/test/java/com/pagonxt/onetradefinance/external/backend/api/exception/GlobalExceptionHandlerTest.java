package com.pagonxt.onetradefinance.external.backend.api.exception;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.exception.DeliveryException;
import com.pagonxt.onetradefinance.external.backend.service.exception.FlowableServerException;
import com.pagonxt.onetradefinance.integrations.model.ControllerErrorResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.exception.ForbiddenException;
import com.pagonxt.onetradefinance.integrations.model.exception.NoContentException;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceUnavailableException;
import com.pagonxt.onetradefinance.integrations.model.exception.TimeoutException;
import com.pagonxt.onetradefinance.integrations.model.exception.URITooLongException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.NotAcceptableStatusException;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@UnitTest
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleNoSuchElementException_returnsNotFoundResponse() {
        // Given
        NoSuchElementException exception = new NoSuchElementException("message");

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleNoSuchElementException(exception);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode(), "Status code should be Not found");

    }

    @Test
    void handleAccessDeniedException_returnsUnauthorizedResponse() {
        // Given
        AccessDeniedException exception = new AccessDeniedException("message", null);

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleAccessDeniedException(exception);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode(), "Status code should be Unauthorized");
    }

    @Test
    void handleSecurityException_returnsUnauthorizedResponse() {
        // Given
        SecurityException exception = new SecurityException("user", "action");

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleSecurityException(exception);

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode(), "Status code should be Unauthorized");
    }

    @Test
    void handleConstraintViolationException_returnsBadRequestResponse() {
        // Given
        ConstraintViolationException exception = new ConstraintViolationException("message", null);

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleConstraintViolationException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(), "Status code should be Bad request");

    }

    @Test
    void handleDateFormatException_returnsBadRequestResponse() {
        // Given
        DateFormatException exception = new DateFormatException("wrongDate", null);

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleDateFormatException(exception);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode(), "Status code should be Bad request");
    }

    @Test
    void handleDeliveryException_returnsInternalServerErrorResponse() {
        // Given
        DeliveryException exception = new DeliveryException("content", null);

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleDeliveryException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Status code should be Internal server error");

    }

    @Test
    void handleFlowableServerException_returnsInternalServerResponse() {
        // Given
        FlowableServerException exception = new FlowableServerException("message");

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleFlowableServerException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Status code should be Internal server error");
    }

    @Test
    void handleServiceException_returnsInternalServerResponse() {
        // Given
        ServiceException exception = new ServiceException("message", "key");

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleServiceException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Status code should be Internal server error");
    }

    @Test
    void handleRuntimeException_returnsInternalServerResponse() {
        // Given
        RuntimeException exception = new RuntimeException();

        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleRuntimeException(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode(), "Status code should be Internal server error");
    }

    @Test
    void handleNoContentException_returnsNoContentResponse() {
        // Given
        NoContentException exception = new NoContentException();
        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleNoContentException(exception);
        // Then
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
    }

    @Test
    void handleForbiddenException_returnsForbiddenResponse() {
        // Given
        ForbiddenException exception = new ForbiddenException();
        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleForbiddenException(exception);
        // Then
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void handleMethodNotAllowedException_returnsMethodNotAllowedResponse() {
        // Given
        MethodNotAllowedException exception = mock(MethodNotAllowedException.class);
        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleMethodNotAllowedException(exception);
        // Then
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, result.getStatusCode());
    }

    @Test
    void handleNotAcceptableStatusException_returnsNotAcceptableStatusResponse() {
        // Given
        NotAcceptableStatusException exception = new NotAcceptableStatusException("Test");
        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleNotAcceptableStatusException(exception);
        // Then
        assertEquals(HttpStatus.NOT_ACCEPTABLE, result.getStatusCode());
    }

    @Test
    void handleURITooLongException_returnsURITooLongResponse() {
        // Given
        URITooLongException exception = new URITooLongException();
        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleURITooLongException(exception);
        // Then
        assertEquals(HttpStatus.URI_TOO_LONG, result.getStatusCode());
    }

    @Test
    void handleServiceUnavailableException_returnsServiceUnavailableResponse() {
        // Given
        ServiceUnavailableException exception = new ServiceUnavailableException();
        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleServiceUnavailableException(exception);
        // Then
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.getStatusCode());
    }

    @Test
    void handleTimeoutException_returnsTimeoutResponse() {
        // Given
        TimeoutException exception = new TimeoutException();
        // When
        ResponseEntity<ControllerErrorResponse> result = globalExceptionHandler.handleTimeoutException(exception);
        // Then
        assertEquals(HttpStatus.GATEWAY_TIMEOUT, result.getStatusCode());
    }
}
