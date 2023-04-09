package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.model.ValidationError;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

/**
 * Class with some utilities to validate some fields of external app
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.CollectionTypeService
 * @since jdk-11.0.13
 */
@Component
public class ValidationUtil {
    public static final String OFFICE = "office";
    public static final String DELIMITER = "_";
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * class method to run validations. Returns a list with errors of the filled fields
     * @param request         : a generic collection with request
     * @param description     : a string with the description
     * @param validationErrors: a list of validation errors
     * @param <T>             : a generic collection
     * @return                : a list with validation errors
     */
    public <T> List<ValidationError> runValidations(T request,
                                                    String description,
                                                    List<ValidationError> validationErrors) {
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        for (ConstraintViolation<T> violation : violations) {
            ValidationError error = generateValidationError(description, violation);
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * Class method to generate a validation error
     * @param description : a string with the description
     * @param violation   : a ConstraintViolation object
     * @param <T>         : a generic collection
     * @return            : a new validation error
     */
    private static <T> ValidationError generateValidationError(String description, ConstraintViolation<T> violation) {
        String field = violation.getPropertyPath().toString();
        String errorCause = violation.getMessage();
        String limit = null;
        if (errorCause.contains(DELIMITER)){
            String[] errorCauseArray = StringUtils.split(errorCause, DELIMITER);
            if(errorCauseArray != null) {
                errorCause = errorCauseArray[0];
                limit = errorCauseArray[1];
            }
        }
        return new ValidationError(description, field, errorCause, limit);
    }
}
