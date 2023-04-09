package com.pagonxt.onetradefinance.external.backend.api.exception;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

/**
 * This class collects the semantics of each exception to generate meaningful error messages for the client,
 * with the clear objective of providing that client with all the information to easily diagnose the problem.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    //class attributes
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * This method handles a no such element exception
     * @param e No such element exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ControllerErrorResponse> handleNoSuchElementException(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles access denied exception
     * @param e runtime exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ControllerErrorResponse> handleAccessDeniedException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles security exception
     * @param e security exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = SecurityException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ControllerErrorResponse> handleSecurityException(SecurityException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ControllerErrorResponse.error(e));
    }

    /**
     * This method handles constraint violation exception
     * @param e runtime exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ControllerErrorResponse> handleConstraintViolationException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles date format exception
     * @param e date format exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = DateFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ControllerErrorResponse> handleDateFormatException(DateFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ControllerErrorResponse.error(e));
    }

    /**
     * This method handles delivery exception
     * @param e delivery exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = DeliveryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ControllerErrorResponse> handleDeliveryException(DeliveryException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ControllerErrorResponse.error(e));
    }

    /**
     * This method handles Flowable Server exception
     * @param e Flowable server exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = FlowableServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ControllerErrorResponse> handleFlowableServerException(FlowableServerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ControllerErrorResponse.error(e));
    }

    /**
     * This method handles service exception
     * @param e service exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ControllerErrorResponse> handleServiceException(ServiceException e) {
        ControllerErrorResponse errorResponse = ControllerErrorResponse.error(e);
        LOG.debug(errorResponse.toString());
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    /**
     * This method handles runtime exception
     * @param e runtime exception
     * @return a response with the error message
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ControllerErrorResponse> handleRuntimeException(RuntimeException e) {
        //Default runtime exception handling. Ideally, none of those exceptions should be thrown
        LOG.error(e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles a no content exception
     * @param e exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ControllerErrorResponse> handleNoContentException(NoContentException e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles a forbidden exception
     * @param e exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ControllerErrorResponse> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles a method not allowed exception
     * @param e exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = MethodNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<ControllerErrorResponse> handleMethodNotAllowedException(MethodNotAllowedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles a method not acceptable exception
     * @param e exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = NotAcceptableStatusException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ControllerErrorResponse> handleNotAcceptableStatusException(NotAcceptableStatusException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles a method not acceptable exception
     * @param e exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = URITooLongException.class)
    @ResponseStatus(HttpStatus.URI_TOO_LONG)
    public ResponseEntity<ControllerErrorResponse> handleURITooLongException(URITooLongException e) {
        return ResponseEntity.status(HttpStatus.URI_TOO_LONG).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles a method service unavailable exception
     * @param e exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = ServiceUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ControllerErrorResponse> handleServiceUnavailableException(ServiceUnavailableException e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ControllerErrorResponse.error(e.getMessage()));
    }

    /**
     * This method handles a method gateway timeout exception
     * @param e exception
     * @return a response with the error message
     */
    @ExceptionHandler(value = TimeoutException.class)
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    public ResponseEntity<ControllerErrorResponse> handleTimeoutException(TimeoutException e) {
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(ControllerErrorResponse.error(e.getMessage()));
    }

}
