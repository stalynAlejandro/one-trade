package com.pagonxt.onetradefinance.work.utils;

import com.pagonxt.onetradefinance.integrations.constants.UserConstants;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.ValidationError;
import com.pagonxt.onetradefinance.integrations.service.CollectionTypeService;
import com.pagonxt.onetradefinance.work.service.OfficeInfoService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Class with some utilities to validate some fields of external app
 * @author -
 * @version jdk-11.0.13
 * @see OfficeInfoService
 * @see CollectionTypeService
 * @since jdk-11.0.13
 */
@Component
public class ValidationUtils {
    public static final Pattern PATTERN_DIGITS = Pattern.compile("^\\d*$");
    public static final String OFFICE = "office";
    public static final String DELIMITER = "_";
    public static final String INCORRECT = "incorrect";
    public static final String NOT_ALLOWED_VALUE = "notAllowedValue";
    private static final List<String> EXTENSIONS = List.of(".pdf", ".jpg", ".png");
    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final OfficeInfoService officeInfoService;

    private final CollectionTypeService collectionTypeService;

    /**
     * constructor method
     * @param officeInfoService     : An OfficeInfoService object
     * @param collectionTypeService : A CollectionTypeService object
     */
    public ValidationUtils(OfficeInfoService officeInfoService,
                           CollectionTypeService collectionTypeService) {
        this.officeInfoService = officeInfoService;
        this.collectionTypeService = collectionTypeService;
    }

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
     * class method to validate the amount range
     * If there is an error, the method adds it to the list of validations
     * @param description      : a string with the description
     * @param amount           : a double value with the amount to check
     * @param min              : a double value with the minimum range for the amount
     * @param max              : a double value with the maximum range for the amount
     * @param validationErrors : a list of validation errors
     * @return                 : a list of validation errors
     */
    public List<ValidationError> validateAmountRange(String description, Double amount,
                                                     Double min, Double max, List<ValidationError> validationErrors) {
        ValidationError error;
        if (amount == null) {
            return validationErrors;
        }
        if (amount < min) {
            error = new ValidationError(description, "minNumber", String.format(Locale.ROOT, "%.2f", min));
            validationErrors.add(error);
        } else if (amount > max) {
            error = new ValidationError(description, "maxNumber", String.format(Locale.ROOT, "%.2f", max));
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * class method to validate the field size
     * If there is an error, the method adds it to the list of validations
     * @param description      : a string with the description
     * @param field            : a string with the field to check
     * @param maxSize          : an integer value with maximum size for the field (characters number)
     * @param validationErrors : a list of validation errors
     * @return                 : a list of validation errors
     */
    public List<ValidationError> validateFieldSize(String description,
                                                   String field, int maxSize, List<ValidationError> validationErrors) {
        if(StringUtils.hasText(field) && field.length() > maxSize) {
            ValidationError error = new ValidationError(description, "maxSize", String.valueOf(maxSize));
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * class method to validate the office (field size, the data is correct...)
     * If there is an error, the method adds it to the list of validations
     * @param country          : a string with the country
     * @param office           : a string with the office
     * @param validationErrors : a list of validation errors
     * @return                 : a list of validation errors
     */
    public List<ValidationError> validateOffice(String country, String office, List<ValidationError> validationErrors) {
        if(office == null) {
            return validationErrors;
        }
        if(!officeInfoService.isValidOffice(office)) {
            ValidationError error = new ValidationError(OFFICE, INCORRECT, null);
            validationErrors.add(error);
        }
        switch (country) {
            case "ES":
                if (!isOnlyDigits(office)) {
                    ValidationError error = new ValidationError(OFFICE, "notANumber", null);
                    validationErrors.add(error);
                }
                validationErrors = validateFieldSize(OFFICE, office, 4, validationErrors);
                break;
            default:
                validationErrors = validateFieldSize(OFFICE, office, 10, validationErrors);
                break;
        }
        return validationErrors;
    }

    /**
     * class method to validate the contract of the middle office
     * If there is an error, the method adds it to the list of validations
     * @param request          : a PagoNxtRequest object
     * @param validationErrors : a list of validation errors
     * @see PagoNxtRequest
     * @return a list of validation errors
     */
    public List<ValidationError> validateContractMiddleOffice(PagoNxtRequest request,
                                                              List<ValidationError> validationErrors) {
        if(request.getMiddleOffice() == null) {
            return validationErrors;
        }
        //If Requester is of Middle Office type, request MiddleOffice must be equals requester Middle office.
        // If not, request office is not valid because his middle office is not equal that the requester middle office
         if(request.getRequester().getUser().getUserType().equals(UserConstants.USER_TYPE_MIDDLE_OFFICE) &&
                 !request.getMiddleOffice().equals(request.getRequester().getMiddleOffice())) {
            ValidationError error = new ValidationError(OFFICE, NOT_ALLOWED_VALUE, null);
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * Class method to validate the office field only has digits
     * @param office : a string with the office
     * @return true or false if the office field only has digits
     */
    private boolean isOnlyDigits(String office) {
        return PATTERN_DIGITS.matcher(office).find();
    }

    /**
     * class method to validate the fields are not null
     * If there is an error, the method adds it to the list of validations
     * @param description       : a string with the description
     * @param field             : an object with the field to check
     * @param validationErrors  : a list of validation errors
     * @return a list of validation errors
     */
    public List<ValidationError> validateNotNull(String description,
                                                 Object field,
                                                 List<ValidationError> validationErrors) {
        if (field == null) {
            ValidationError error = new ValidationError(description, "isNull", null);
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * class method to validate documents
     * If there is an error, the method adds it to the list of validations
     * @param request          : a PagoNxtRequest object
     * @param validationErrors : a list of validation errors
     * @see PagoNxtRequest
     * @return a list of validation errors
     */
    public List<ValidationError> validateDocuments(PagoNxtRequest request, List<ValidationError> validationErrors) {
        if (request.getDocuments() != null) {
            request.getDocuments().forEach(document -> validateFileExtension("documents",
                    document.getFilename().toLowerCase(), validationErrors));
        }
        return validationErrors;
    }

    /**
     * class method to validate the file extension
     * If there is an error, the method adds it to the list of validations
     * @param description      : a string with the description
     * @param fileName         : a string with the filename
     * @param validationErrors : a list of validation errors
     */
    public void validateFileExtension(String description, String fileName, List<ValidationError> validationErrors) {
        if (EXTENSIONS.stream().noneMatch(fileName::endsWith)) {
            ValidationError error = new ValidationError(description, "wrongExtension", null);
            validationErrors.add(error);
        }
    }

    /**
     * Class method to validate if a date is correct (The date value is not earlier than the request date)
     * If there is an error, the method adds it to the list of validations
     * @param description       : a string with the description
     * @param currentDate       : a Date object with the current date
     * @param requestDate       : a Date object with the request date
     * @param validationErrors  : a list of validation errors
     * @return a list of validation errors
     */
    public List<ValidationError> validateDateIsAfter(String description, Date currentDate,
                                                     Date requestDate, List<ValidationError> validationErrors) {
        if (requestDate != null  && currentDate.after(requestDate)) {
            ValidationError error = new ValidationError(description, "minDate", dateTimeFormat.format(currentDate));
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * Class method to validate if an amount is lower than other amount
     * If there is an error, the method adds it to the list of validations
     * @param firstAmountDesc   : a string with the first amount
     * @param secondAmountDesc  : a string with the second amount
     * @param firstAmount       : a double value of the first amount
     * @param secondAmount      : a double value of the second amount
     * @param validationErrors  : a list of validation errors
     * @return                  : a list of validation errors
     */
    public List<ValidationError> validateAmountIsLowerThan(String firstAmountDesc,
                                                           String secondAmountDesc,
                                                           Double firstAmount,
                                                           Double secondAmount,
                                                           List<ValidationError> validationErrors) {
        if (firstAmount != null && secondAmount != null && firstAmount > secondAmount) {
            ValidationError error = new ValidationError(firstAmountDesc, "maxField", secondAmountDesc);
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * class method to validate a collection type
     * If there is an error, the method adds it to the list of validations
     * @param collectionType   : a string with the collection type
     * @param validationErrors : a list of validation errors
     * @return a list of validation errors
     */
    public List<ValidationError> validateCollectionType(String collectionType, List<ValidationError> validationErrors) {
        if (collectionType != null && !collectionTypeService.exists(collectionType)) {
            ValidationError error = new ValidationError("collectionType", INCORRECT, null);
            validationErrors.add(error);
        }
        return validationErrors;
    }

    /**
     * class method to validate the amounts of the exchange insurances
     * If there is an error, the method adds it to the list of validations
     * @param request          : an ExportCollectionAdvanceRequest object
     * @param validationErrors : a list of validation errors
     * @see ExportCollectionAdvanceRequest
     * @return a list of validation errors
     */
    public List<ValidationError> validateExchangeInsuranceAmounts(ExportCollectionAdvanceRequest request,
                                                                  List<ValidationError> validationErrors) {
        if(request.getExchangeInsurances() != null && request.getAmount() != null) {
             Double sumExchangeInsurances = request.getExchangeInsurances().stream()
                     .map(ExchangeInsurance::getUseAmount).mapToDouble(Double::doubleValue).sum();
             if (sumExchangeInsurances.compareTo(request.getAmount()) > 0) {
                 ValidationError error = new ValidationError("exchangeInsurance", "amount", INCORRECT, null);
                 validationErrors.add(error);
             }
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
