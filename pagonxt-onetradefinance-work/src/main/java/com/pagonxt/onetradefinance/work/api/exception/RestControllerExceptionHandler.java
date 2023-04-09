package com.pagonxt.onetradefinance.work.api.exception;

import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.SecurityException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.parser.bpmn.usertask.BpmnUserTaskParserException;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

/**
 * Controller class to handle exceptions to the rest controller
 * @author -
 * @version jdk-11.0.13
 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
 * @since jdk-11.0.13
 */
@ControllerAdvice(basePackages = "com.pagonxt.onetradefinance.work.api")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(RestControllerExceptionHandler.class);

    /**
     * Method to handle access denied exceptions
     * @param e a RuntimeException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Method to handle security exceptions
     * @param e a SecurityException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = SecurityException.class)
    public ResponseEntity<ControllerResponse> handleSecurityException(SecurityException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ControllerResponse.error(e));
    }

    /**
     * Method to handle no such element exceptions
     * @param e a NoSuchElementException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException e) {
        LOG.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Method to handle no resource found exceptions
     * @param e a ResourceNotFoundException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<ControllerResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        LOG.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ControllerResponse.error(e));
    }

    /**
     * Method to handle invalid request exceptions
     * @param e a InvalidRequestException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<ControllerResponse> handleInvalidRequestException(InvalidRequestException e) {
        LOG.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ControllerResponse.error(e));
    }

    /**
     * Method to handle Bpmn User task parser exceptions
     * @param e a BpmnUserTaskParserException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = BpmnUserTaskParserException.class)
    public ResponseEntity<String> handleBpmnUserTaskParserException(BpmnUserTaskParserException e) {
        LOG.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Method to handle constraint violation exceptions
     * @param e a RuntimeException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * Method to handle service exceptions
     * @param e a ServiceException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<ControllerResponse> handleServiceException(ServiceException e) {
        LOG.warn(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ControllerResponse.error(e));
    }

    /**
     * Method to handle runtime exceptions
     * @param e a RuntimeException object
     * @return a ResponseEntity object
     */
    @ExceptionHandler
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        //Default runtime exception handling. Ideally, none of those exceptions should be thrown
        LOG.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
