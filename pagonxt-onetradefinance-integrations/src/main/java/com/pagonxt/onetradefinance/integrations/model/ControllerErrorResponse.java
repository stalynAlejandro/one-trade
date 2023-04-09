package com.pagonxt.onetradefinance.integrations.model;

import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

public class ControllerErrorResponse {

    private final List<ErrorSantander> errors;

    /**
     * Constructor.
     *
     * @param errors    : the list of errors
     */
    public ControllerErrorResponse(List<ErrorSantander> errors) {
        this.errors = errors;
    }

    /**
     * class method to build an error response
     * @param code          : the error code
     * @param message       : the error message
     * @param description   : the error description
     * @param data          : the error complementary info
     * @return an error response
     */
    public static ControllerErrorResponse error(String code, String message, String description, Object data) {
        ErrorSantander errorSantander = new ErrorSantander();
        errorSantander.setCode(code);
        errorSantander.setMessage(message);
        errorSantander.setLevel("error");
        errorSantander.setDescription(description);
        errorSantander.setData(data);
        List<ErrorSantander> errors = new ArrayList<>();
        errors.add(errorSantander);
        return new ControllerErrorResponse(errors);
    }

    /**
     * class method to build an error response
     * @param serviceException : a ServiceException object
     * @return an error response
     */
    public static ControllerErrorResponse error(ServiceException serviceException) {
        return error(serviceException.getKey(), serviceException.getMessage(), serviceException.getMessage(), serviceException.getEntity());
    }


    /**
     * class method to build an error response
     * @param message : the error message
     * @return an error response
     */
    public static ControllerErrorResponse error(String message) {
        return error("", message, "", null);
    }

    public List<ErrorSantander> getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "ControllerErrorResponse{" +
                "errors=" + errors +
                '}';
    }
}
