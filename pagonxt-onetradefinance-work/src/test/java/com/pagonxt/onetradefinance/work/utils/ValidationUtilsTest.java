package com.pagonxt.onetradefinance.work.utils;

import com.pagonxt.onetradefinance.integrations.constants.UserConstants;
import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.service.CollectionTypeService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.OfficeInfoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@UnitTest
class ValidationUtilsTest {

    @InjectMocks
    ValidationUtils validationUtils;
    @Mock
    CollectionTypeService collectionTypeService;
    @Mock
    OfficeInfoService officeInfoService;

    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Test
    void runValidations_whenInvalidObject_thenReturnListWithValidationErrors() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setAmount(10000000000d);
        int exportCollectionRequestValidations = 9;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.runValidations(request, "testParentFieldName", previousValidationErrors);
        // Then
        assertEquals(exportCollectionRequestValidations, validationErrors.size());
        ValidationError validationErrorAmount = validationErrors.stream().filter(v -> v.getFieldName().equals("amount")).findFirst().orElse(null);
        assertNotNull(validationErrorAmount);
        assertEquals("testParentFieldName", validationErrorAmount.getParentFieldName());
        assertEquals("maxNumber", validationErrorAmount.getViolation());
        assertEquals("9999999999.99", validationErrorAmount.getLimit());

    }

    @Test
    void validateAmountRange_whenAmountIsTooHigh_thenReturnListWithValidationErrors(){
        // Given
        String description = "description";
        Double amount = 500000.0;
        Double min = 0.0;
        Double max = 10000.0;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountRange(description, amount, min, max, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals(description, validationError.getFieldName());
        assertEquals("maxNumber", validationError.getViolation());
        assertEquals(String.format(Locale.ROOT, "%.2f", max), validationError.getLimit());
    }

    @Test
    void validateAmountRange_whenAmountIsTooLow_thenReturnListWithValidationError(){
        // Given
        String description = "description";
        Double amount = -1000d;
        Double min = 0.0;
        Double max = 10000.0;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountRange(description, amount, min, max, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals(description, validationError.getFieldName());
        assertEquals("minNumber", validationError.getViolation());
        assertEquals(String.format(Locale.ROOT, "%.2f", min), validationError.getLimit());
    }

    @Test
    void validateAmountRange_whenAmountJustRight_thenReturnEmptyList(){
        // Given
        String description = "description";
        Double amount = 5000d;
        Double min = 0.0;
        Double max = 10000.0;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountRange(description, amount, min, max, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateAmountRange_whenAmountIsNull_thenReturnEmptyList(){
        // Given
        String description = "description";
        Double min = 0.0;
        Double max = 10000.0;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountRange(description, null, min, max, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateFieldSize_fieldSizeIsOutOfRange_thenReturnListWithValidationError(){
        // Given
        String description = "description";
        String field = "En un lugar de la Mancha";
        int maxSize = 10;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateFieldSize(description, field, maxSize, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals(description, validationError.getFieldName());
        assertEquals("maxSize", validationError.getViolation());
        assertEquals("10", validationError.getLimit());
    }

    @Test
    void validateFieldSize_fieldSizeOnRange_thenReturnEmptyList(){
        // Given
        String description = "description";
        String field = "La Mancha";
        int maxSize = 10;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateFieldSize(description, field, maxSize, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateFieldSize_fieldSizeIsNull_thenReturnEmptyList(){
        // Given
        String description = "description";
        int maxSize = 10;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateFieldSize(description, null, maxSize, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateOffice_officeIsESAndNotANumber_thenReturnListWithValidationError(){
        // Given
        String office = "asd";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        when(officeInfoService.isValidOffice(office)).thenReturn(true);
        // When
        List<ValidationError> validationErrors = validationUtils.validateOffice("ES", office, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals("office", validationError.getFieldName());
        assertEquals("notANumber", validationError.getViolation());
        assertNull(validationError.getLimit());
    }

    @Test
    void validateOffice_officeIsESAndSizeOutOfRange_thenReturnListWithValidationError(){
        // Given
        String office = "12345";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        when(officeInfoService.isValidOffice(office)).thenReturn(true);
        // When
        List<ValidationError> validationErrors = validationUtils.validateOffice("ES", office, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals("office", validationError.getFieldName());
        assertEquals("maxSize", validationError.getViolation());
        assertEquals("4", validationError.getLimit());
    }

    @Test
    void validateOffice_officeIsNotESAndSizeOutOfRange_thenReturnListWithValidationError(){
        // Given
        String office = "12345678910";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        when(officeInfoService.isValidOffice(office)).thenReturn(true);
        // When
        List<ValidationError> validationErrors = validationUtils.validateOffice("GB", office, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals("office", validationError.getFieldName());
        assertEquals("maxSize", validationError.getViolation());
        assertEquals("10", validationError.getLimit());
    }

    @Test
    void validateOffice_officeNotExists_thenReturnListWithValidationError(){
        // Given
        String office = "1234";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        when(officeInfoService.isValidOffice(office)).thenReturn(false);
        // When
        List<ValidationError> validationErrors = validationUtils.validateOffice("ES", office, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals("office", validationError.getFieldName());
        assertEquals("incorrect", validationError.getViolation());
        assertNull(validationError.getLimit());
    }

    @Test
    void validateOffice_officeIsNull_thenReturnEmptyList(){
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateOffice("ES", null, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateOffice_officeIsESAndSizeOnRange_thenReturnEmptyList(){
        // Given
        String office = "1234";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        when(officeInfoService.isValidOffice(office)).thenReturn(true);
        // When
        List<ValidationError> validationErrors = validationUtils.validateOffice("ES", office, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateOffice_officeIsNotESAndSizeOnRange_thenReturnEmptyList(){
        // Given
        String office = "123456789";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        when(officeInfoService.isValidOffice(office)).thenReturn(true);
        // When
        List<ValidationError> validationErrors = validationUtils.validateOffice("GB", office, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateNull_fieldIsNull_thenReturnListWithValidationError() {
        // Given
        String description = "description";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateNotNull(description, null, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals(description, validationError.getFieldName());
        assertEquals("isNull", validationError.getViolation());
        assertNull(validationError.getLimit());
    }

    @Test
    void validateNull_fieldIsNotNull_thenReturnEmptyList() {
        // Given
        String description = "description";
        String fieldValue = "xxx";
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateNotNull(description, fieldValue, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateDocuments_invalidExtension_thenReturnListWithValidationError() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        PagoNxtRequest request = new PagoNxtRequest();
        Document document = new Document();
        document.setFilename("document.abc");
        List<Document> documents = List.of(document);
        request.setDocuments(documents);
        // When
        List<ValidationError> validationErrors = validationUtils.validateDocuments(request, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals("documents", validationError.getFieldName());
        assertEquals("wrongExtension", validationError.getViolation());
        assertNull(validationError.getLimit());
    }

    @Test
    void validateDocuments_validExtension_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        PagoNxtRequest request = new PagoNxtRequest();
        Document document = new Document();
        document.setFilename("document.png");
        List<Document> documents = List.of(document);
        request.setDocuments(documents);
        // When
        List<ValidationError> validationErrors = validationUtils.validateDocuments(request, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateDateIsAfter_requestDateIsBeforeDate_thenReturnListWithValidationError() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        long milliseconds = System.currentTimeMillis();
        Date datePassed = new Date(milliseconds - 86400000);
        Date now = new Date();
        String stringNow = dateTimeFormat.format(now);
        String description = "description";
        // When
        List<ValidationError> validationErrors = validationUtils.validateDateIsAfter(description, now, datePassed, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals(description, validationError.getFieldName());
        assertEquals("minDate", validationError.getViolation());
        assertEquals(stringNow, validationError.getLimit());
    }

    @Test
    void validateDateIsAfter_requestDateIsAfterDate_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        long milliseconds = System.currentTimeMillis();
        Date dateFuture = new Date(milliseconds + 86400000);
        Date now = new Date();
        String description = "description";
        // When
        List<ValidationError> validationErrors = validationUtils.validateDateIsAfter(description, now, dateFuture, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateDateIsAfter_requestDateIsNull_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        Date now = new Date();
        String description = "description";
        // When
        List<ValidationError> validationErrors = validationUtils.validateDateIsAfter(description, now, null, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateAmountIsLowerThan_amountIsGreater_thenReturnListWithValidationError() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        Double value1 = 22.2;
        Double value2 = 22.1;
        String description1 = "description1";
        String description2 = "description2";
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountIsLowerThan(description1, description2, value1, value2, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals(description1, validationError.getFieldName());
        assertEquals("maxField", validationError.getViolation());
        assertEquals(description2, validationError.getLimit());
    }

    @Test
    void validateAmountIsLowerThan_amountIsLower_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        Double value1 = 22d;
        Double value2 = 22.1;
        String description1 = "description1";
        String description2 = "description2";
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountIsLowerThan(description1, description2, value1, value2, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateAmountIsLowerThan_amountIsNull_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        Double value2 = 22.1;
        String description1 = "description1";
        String description2 = "description2";
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountIsLowerThan(description1, description2, null, value2, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateAmountIsLowerThan_limitIsNull_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        Double value1 = 22d;
        String description1 = "description1";
        String description2 = "description2";
        // When
        List<ValidationError> validationErrors = validationUtils.validateAmountIsLowerThan(description1, description2, value1, null, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }


    @Test
    void validateCollectionType_whenInvalidString_thenThrowInvalidRequestException() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        doReturn(false).when(collectionTypeService).exists(any());
        // When
        List<ValidationError> validationErrors = validationUtils.validateCollectionType("INVALID", previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals("collectionType", validationError.getFieldName());
        assertEquals("incorrect", validationError.getViolation());
        assertNull(validationError.getLimit());
    }

    @Test
    void validateCollectionType_whenValidString_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        doReturn(true).when(collectionTypeService).exists(any());
        // When
        List<ValidationError> validationErrors = validationUtils.validateCollectionType("6556010000003", previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateCollectionType_whenNull_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtils.validateCollectionType(null, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateExchangeInsuranceAmounts_whenIncorrectSum_thenReturnListWithValidationError() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        request.setAmount(3.45d);
        ExchangeInsurance exchangeInsurance1 = new ExchangeInsurance();
        exchangeInsurance1.setUseAmount(2.89d);
        ExchangeInsurance exchangeInsurance2 = new ExchangeInsurance();
        exchangeInsurance2.setUseAmount(0.57d);
        request.setExchangeInsurances(List.of(exchangeInsurance1, exchangeInsurance2));
        // When
        List<ValidationError> validationErrors = validationUtils.validateExchangeInsuranceAmounts(request, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertEquals("exchangeInsurance", validationError.getParentFieldName());
        assertEquals("amount", validationError.getFieldName());
        assertEquals("incorrect", validationError.getViolation());
        assertNull(validationError.getLimit());
    }

    @Test
    void validateExchangeInsuranceAmounts_whenNoExchangeInsurances_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        request.setAmount(3.45d);
        request.setExchangeInsurances(null);
        // When
        List<ValidationError> validationErrors = validationUtils.validateExchangeInsuranceAmounts(request, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateExchangeInsuranceAmounts_whenNoAmount_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        request.setAmount(null);
        ExchangeInsurance exchangeInsurance1 = new ExchangeInsurance();
        exchangeInsurance1.setUseAmount(2.89d);
        ExchangeInsurance exchangeInsurance2 = new ExchangeInsurance();
        exchangeInsurance2.setUseAmount(0.56d);
        request.setExchangeInsurances(List.of(exchangeInsurance1, exchangeInsurance2));
        // When
        List<ValidationError> validationErrors = validationUtils.validateExchangeInsuranceAmounts(request, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }

    @Test
    void validateExchangeInsuranceAmounts_whenCorrectSum_thenReturnEmptyList() {
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        request.setAmount(3.45d);
        ExchangeInsurance exchangeInsurance1 = new ExchangeInsurance();
        exchangeInsurance1.setUseAmount(2.89d);
        ExchangeInsurance exchangeInsurance2 = new ExchangeInsurance();
        exchangeInsurance2.setUseAmount(0.56d);
        request.setExchangeInsurances(List.of(exchangeInsurance1, exchangeInsurance2));
        // When
        List<ValidationError> validationErrors = validationUtils.validateExchangeInsuranceAmounts(request, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }
    @Test
    void validateContractMiddleOffice_middleOfficeIsNull_thenReturnEmptyList(){
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        PagoNxtRequest request = new PagoNxtRequest();
        // When
        List<ValidationError> validationErrors = validationUtils.validateContractMiddleOffice(request, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }
    @Test
    void validateContractMiddleOffice_middleOfficeIsOk_thenReturnEmptyList(){
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        PagoNxtRequest request = new PagoNxtRequest();
        request.setMiddleOffice("myMiddleOffice");
        UserInfo userInfo = new UserInfo();
        userInfo.setOffice("myOffice");
        userInfo.setMiddleOffice("myMiddleOffice");
        User user = new User();
        user.setUserType(UserConstants.USER_TYPE_MIDDLE_OFFICE);
        userInfo.setUser(user);
        request.setRequester(userInfo);
        // When
        List<ValidationError> validationErrors = validationUtils.validateContractMiddleOffice(request, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }
    @Test
    void validateContractMiddleOffice_requesterNotMiddleOffice_thenReturnEmptyList(){
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        PagoNxtRequest request = new PagoNxtRequest();
        request.setMiddleOffice("myMiddleOffice");
        UserInfo userInfo = new UserInfo();
        userInfo.setOffice("myOffice");
        userInfo.setMiddleOffice("myMiddleOffice");
        User user = new User();
        user.setUserType(UserConstants.USER_TYPE_OFFICE);
        userInfo.setUser(user);
        request.setRequester(userInfo);
        // When
        List<ValidationError> validationErrors = validationUtils.validateContractMiddleOffice(request, previousValidationErrors);
        // Then
        assertEquals(0, validationErrors.size());
    }
    @Test
    void validateContractMiddleOffice_officeNotInMiddleOffice_thenOfficeNotAllowedValue(){
        // Given
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        PagoNxtRequest request = new PagoNxtRequest();
        request.setMiddleOffice("otherMiddleOffice");
        UserInfo userInfo = new UserInfo();
        userInfo.setOffice("myOffice");
        userInfo.setMiddleOffice("myMiddleOffice");
        User user = new User();
        user.setUserType(UserConstants.USER_TYPE_MIDDLE_OFFICE);
        userInfo.setUser(user);
        request.setRequester(userInfo);
        // When
        List<ValidationError> validationErrors = validationUtils.validateContractMiddleOffice(request, previousValidationErrors);
        // Then
        assertEquals(1, validationErrors.size());
        ValidationError validationError = validationErrors.get(0);
        assertNull(validationError.getParentFieldName());
        assertEquals("office", validationError.getFieldName());
        assertEquals("notAllowedValue", validationError.getViolation());
        assertNull(validationError.getLimit());
    }
}
