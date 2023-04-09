package com.pagonxt.onetradefinance.work.validation;

import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.util.ValidationUtil;
import com.pagonxt.onetradefinance.work.utils.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class to validate fields of export collection requests
 * @author -
 * @version jdk-11.0.13
 * @see ValidationUtil
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionValidation {

    public static final String CUSTOMER = "customer";
    public static final String AMOUNT = "amount";
    public static final String ADVANCE_AMOUNT = "advanceAmount";
    public static final double AMOUNT_MAX = 9999999999.99;
    public static final double AMOUNT_MIN = 0.0;
    public static final String NOMINAL_ACCOUNT = "nominalAccount";
    public static final String COMMISSION_ACCOUNT = "commissionAccount";
    public static final String EXPORT_COLLECTION = "exportCollection";
    public static final String EXPORT_COLLECTION_ADVANCE = "exportCollectionAdvance";
    public static final String RISK_LINE = "riskLine";
    public static final String OPERATION_TYPE = "operationType";
    public static final String COMMENT = "comment";
    public static final int MAX_SIZE_50 = 50;
    public static final int MAX_SIZE_COMMENT = 500;
    public static final String EXPIRATION = "expiration";
    public static final String CURRENCY = "currency";
    public static final String ADVANCE_CURRENCY = "advanceCurrency";
    public static final String OFFICE = "office";

    //class attribute
    private final ValidationUtils validationUtils;

    /**
     * constructor method
     * @param validationUtils : a ValidationUtils object
     */
    public ExportCollectionValidation(ValidationUtils validationUtils) {
        this.validationUtils = validationUtils;
    }

    /**
     * Method to validate a draft
     * @param request : an ExportCollectionRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     */
    public void validateDraft(ExportCollectionRequest request) {
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        if (request.isCustomerHasAccount()) {
            validationErrors = validationUtils
                    .runValidations(request.getNominalAccount(), NOMINAL_ACCOUNT, validationErrors);
            validationErrors = validationUtils
                    .runValidations(request.getCommissionAccount(), COMMISSION_ACCOUNT, validationErrors);
        }
        validationErrors = validationUtils.validateNotNull(AMOUNT, request.getAmount(), validationErrors);
        validationErrors = validationUtils
                .validateAmountRange(AMOUNT, request.getAmount(), AMOUNT_MIN, AMOUNT_MAX, validationErrors);
        validationErrors = validationUtils.validateNotNull(CURRENCY, request.getCurrency(), validationErrors);
        validationErrors = validationUtils
                .validateNotNull("collectionType", request.getCollectionType(), validationErrors);
        validationErrors = validationUtils.validateCollectionType(request.getCollectionType(), validationErrors);
        validationErrors = validationUtils
                .validateFieldSize("clientReference", request.getClientReference(), 30, validationErrors);
        validationErrors = validationUtils
                .validateFieldSize("debtorName", request.getDebtorName(), MAX_SIZE_50, validationErrors);
        validationErrors = validationUtils
                .validateFieldSize("debtorBank", request.getDebtorBank(), MAX_SIZE_50, validationErrors);
        validationErrors = validationUtils.validateNotNull(OFFICE, request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(),  validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        validationErrors = validationUtils
                .validateFieldSize(COMMENT, request.getComment(), MAX_SIZE_COMMENT, validationErrors);
        validationErrors = validationUtils
                .validateDateIsAfter(EXPIRATION, new Date(), request.getAdvanceExpiration(), validationErrors);
        validationErrors = validationUtils.validateAmountIsLowerThan(ADVANCE_AMOUNT, AMOUNT, request
                .getAdvanceAmount(), request.getAmount(), validationErrors);
        validationErrors = validationUtils.validateAmountRange(ADVANCE_AMOUNT, request
                .getAdvanceAmount(), AMOUNT_MIN, AMOUNT_MAX, validationErrors);
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to validate a draft
     * @param request : an ExportCollectionAdvanceRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     */
    public void validateDraft(ExportCollectionAdvanceRequest request) {
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils
                .runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        validationErrors = validationUtils
                .validateNotNull(EXPORT_COLLECTION, request.getExportCollection().getCode(), validationErrors);
        validationErrors = validationUtils
                .validateNotNull(AMOUNT, request.getAmount(), validationErrors);
        validationErrors = validationUtils
                .validateAmountRange(AMOUNT, request.getAmount(), AMOUNT_MIN, AMOUNT_MAX, validationErrors);
        validationErrors = validationUtils.validateNotNull(CURRENCY, request.getCurrency(), validationErrors);
        validationErrors = validationUtils
                .validateDateIsAfter(EXPIRATION, new Date(), request.getExpiration(), validationErrors);
        validationErrors = validationUtils
                .validateNotNull(RISK_LINE, request.getRiskLine().getRiskLineId(), validationErrors);
        validationErrors = validationUtils.validateExchangeInsuranceAmounts(request, validationErrors);
        validationErrors = validationUtils.validateNotNull(OFFICE, request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        validationErrors = validationUtils
                .validateFieldSize(COMMENT, request.getComment(), MAX_SIZE_COMMENT, validationErrors);
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to validate data
     * @param request : an ExportCollectionRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     */
    public void validateConfirm(ExportCollectionRequest request){
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(request, null, validationErrors);
        validationErrors = validationUtils.runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        if(request.isCustomerHasAccount()){
            validationErrors = validationUtils
                    .runValidations(request.getNominalAccount(), NOMINAL_ACCOUNT, validationErrors);
            validationErrors = validationUtils
                    .runValidations(request.getCommissionAccount(), COMMISSION_ACCOUNT, validationErrors);
        }
        validationErrors = validationUtils.validateCollectionType(request.getCollectionType(), validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        if(request.isApplyingForAdvance()){
            validationErrors = validationUtils
                    .validateNotNull(ADVANCE_AMOUNT, request.getAdvanceAmount(), validationErrors);
            validationErrors = validationUtils.validateAmountIsLowerThan(ADVANCE_AMOUNT, AMOUNT, request
                    .getAdvanceAmount(), request.getAmount(), validationErrors);
            validationErrors = validationUtils
                    .validateNotNull(ADVANCE_CURRENCY, request.getAdvanceCurrency(), validationErrors);
            validationErrors = validationUtils
                    .validateDateIsAfter(EXPIRATION, new Date(), request.getAdvanceExpiration(), validationErrors);
            validationErrors = validationUtils
                    .validateNotNull(RISK_LINE, request.getAdvanceRiskLine().getRiskLineId(), validationErrors);
        }
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to validate data
     * @param request : an ExportCollectionModificationRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest
     */
    public void validateConfirm(ExportCollectionModificationRequest request){
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(request, null, validationErrors);
        validationErrors = validationUtils.runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        validationErrors = validationUtils
                .validateNotNull(EXPORT_COLLECTION, request.getExportCollection().getCode(), validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to validate data
     * @param request : an ExportCollectionAdvanceRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     */
    public void validateConfirm(ExportCollectionAdvanceRequest request){
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(request, null, validationErrors);
        validationErrors = validationUtils.runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        validationErrors = validationUtils
                .validateNotNull(EXPORT_COLLECTION, request.getExportCollection().getCode(), validationErrors);
        validationErrors = validationUtils
                .validateDateIsAfter(EXPIRATION, new Date(), request.getExpiration(), validationErrors);
        validationErrors = validationUtils
                .validateNotNull(RISK_LINE, request.getRiskLine().getRiskLineId(), validationErrors);
        validationErrors = validationUtils.validateExchangeInsuranceAmounts(request, validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to validate data
     * @param request : an ExportCollectionAdvanceModificationRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest
     */
    public void validateConfirm(ExportCollectionAdvanceModificationRequest request){
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(request, null, validationErrors);
        validationErrors = validationUtils.runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        validationErrors = validationUtils.validateNotNull(EXPORT_COLLECTION_ADVANCE, request
                .getExportCollectionAdvance().getCode(), validationErrors);
        validationErrors = validationUtils
                .validateNotNull(RISK_LINE, request.getRiskLine().getRiskLineId(), validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to validate data
     * @param request : an ExportCollectionAdvanceCancellationRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     */
    public void validateConfirm(ExportCollectionAdvanceCancellationRequest request){
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(request, null, validationErrors);
        validationErrors = validationUtils.runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        validationErrors = validationUtils.validateNotNull(EXPORT_COLLECTION_ADVANCE, request
                .getExportCollectionAdvance().getCode(), validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to validate data
     * @param request : an ExportCollectionOtherOperationsRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     */
    public void validateConfirm(ExportCollectionOtherOperationsRequest request){
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(request, null, validationErrors);
        validationErrors = validationUtils.runValidations(request.getCustomer(), CUSTOMER, validationErrors);
        validationErrors = validationUtils
                .validateNotNull(EXPORT_COLLECTION, request.getExportCollection().getCode(), validationErrors);
        validationErrors = validationUtils
                .validateNotNull(OPERATION_TYPE, request.getOperationType(), validationErrors);
        validationErrors = validationUtils.validateOffice(request.getCountry(), request.getOffice(), validationErrors);
        validationErrors = validationUtils.validateContractMiddleOffice(request, validationErrors);
        validationErrors = validationUtils.validateDocuments(request, validationErrors);
        throwInvalidRequestException(validationErrors);
    }

    /**
     * Method to throw invalid request exception
     * @param validationErrors : a list with validation errors
     */
    private static void throwInvalidRequestException(List<ValidationError> validationErrors) {
        if(!validationErrors.isEmpty()) {
            throw new InvalidRequestException("There are validation errors",
                    "validationError", new ArrayList<>(validationErrors));
        }
    }
}
