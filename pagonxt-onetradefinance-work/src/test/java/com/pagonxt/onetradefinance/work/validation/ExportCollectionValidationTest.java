package com.pagonxt.onetradefinance.work.validation;

import com.pagonxt.onetradefinance.integrations.model.*;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.util.ValidationUtil;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionValidationTest {
    
    @InjectMocks
    ExportCollectionValidation exportCollectionValidation;
    
    @Mock
    ValidationUtils validationUtils;
    
    @Test
    void validateDraftExportCollectionRequestWithoutAccounts_whenValidRequest_doNothing() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        // When
        exportCollectionValidation.validateDraft(request);
        // Then
        verify(validationUtils, never()).runValidations(any(), eq("nominalAccount"), eq(validationErrors));
        verify(validationUtils, never()).runValidations(any(), eq("commissionAccount"), eq(validationErrors));
    }

    @Test
    void validateDraftExportCollectionRequest_whenValidRequestWithAccounts_doNothing() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setCustomerHasAccount(true);
        List<ValidationError> validationErrors = new ArrayList<>();
        // When
        exportCollectionValidation.validateDraft(request);
        // Then
        verify(validationUtils).runValidations(any(), eq("nominalAccount"), eq(validationErrors));
        verify(validationUtils).runValidations(any(), eq("commissionAccount"), eq(validationErrors));
    }

    @Test
    void validateDraftExportCollectionRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setCustomerHasAccount(true);
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateDraft(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }

    @Test
    void validateDraftExportCollectionAdvanceRequest_whenValidRequest_doNothing() {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        request.setExportCollection(new ExportCollection());
        request.setRiskLine(new RiskLine());
        List<ValidationError> validationErrors = new ArrayList<>();
        // When
        exportCollectionValidation.validateDraft(request);
        // Then
        verify(validationUtils).validateDocuments(request, validationErrors);
    }

    @Test
    void validateDraftExportCollectionAdvanceRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setCustomerHasAccount(true);
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateDraft(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }

    @Test
    void validateConfirmExportCollectionRequestWithoutAccountsWithoutRiskLine_whenValidRequest_doNothing() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        List<ValidationError> validationErrors = new ArrayList<>();
        // When
        exportCollectionValidation.validateConfirm(request);
        // Then
        verify(validationUtils, never()).runValidations(any(), eq("nominalAccount"), eq(validationErrors));
        verify(validationUtils, never()).runValidations(any(), eq("commissionAccount"), eq(validationErrors));

        verify(validationUtils, never()).validateNotNull(eq("advanceAmount"), any(), eq(validationErrors));
        verify(validationUtils, never()).validateAmountIsLowerThan(eq("advanceAmount"), eq("amount"), any(), any(), eq(validationErrors));
        verify(validationUtils, never()).validateNotNull(eq("advanceCurrency"), any(), eq(validationErrors));
        verify(validationUtils, never()).validateDateIsAfter(eq("expiration"), any(), any(), eq(validationErrors));
        verify(validationUtils, never()).validateNotNull(eq("riskLine"), any(), eq(validationErrors));
    }

    @Test
    void validateConfirmExportCollectionRequestWithAccountsWithRiskLine_whenValidRequest_doNothing() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setCustomerHasAccount(true);
        request.setApplyingForAdvance(true);
        request.setAdvanceRiskLine(new RiskLine());
        List<ValidationError> validationErrors = new ArrayList<>();
        // When
        exportCollectionValidation.validateConfirm(request);
        // Then
        verify(validationUtils).runValidations(any(), eq("nominalAccount"), eq(validationErrors));
        verify(validationUtils).runValidations(any(), eq("commissionAccount"), eq(validationErrors));

        verify(validationUtils).validateNotNull(eq("advanceAmount"), any(), eq(validationErrors));
        verify(validationUtils).validateAmountIsLowerThan(eq("advanceAmount"), eq("amount"), any(), any(), eq(validationErrors));
        verify(validationUtils).validateNotNull(eq("advanceCurrency"), any(), eq(validationErrors));
        verify(validationUtils).validateDateIsAfter(eq("expiration"), any(), any(), eq(validationErrors));
        verify(validationUtils).validateNotNull(eq("riskLine"), any(), eq(validationErrors));
    }

    @Test
    void validateConfirmExportCollectionRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setCustomerHasAccount(true);
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateConfirm(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }

    @Test
    void validateConfirmExportCollectionModificationRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionModificationRequest request = new ExportCollectionModificationRequest();
        request.setExportCollection(new ExportCollection());
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateConfirm(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }

    @Test
    void validateConfirmExportCollectionAdvanceRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionAdvanceRequest request = new ExportCollectionAdvanceRequest();
        request.setExportCollection(new ExportCollection());
        request.setRiskLine(new RiskLine());
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateConfirm(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }

    @Test
    void validateConfirmExportCollectionAdvanceModificationRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionAdvanceModificationRequest request = new ExportCollectionAdvanceModificationRequest();
        request.setExportCollectionAdvance(new ExportCollectionAdvance());
        request.setRiskLine(new RiskLine());
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateConfirm(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }

    @Test
    void validateConfirmExportCollectionAdvanceCancellationRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionAdvanceCancellationRequest request = new ExportCollectionAdvanceCancellationRequest();
        request.setExportCollectionAdvance(new ExportCollectionAdvance());
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateConfirm(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }

    @Test
    void validateConfirmExportCollectionOtherOperationsRequest_whenInvalidRequest_thenThrowInvalidRequestException() {
        // Given
        ExportCollectionOtherOperationsRequest request = new ExportCollectionOtherOperationsRequest();
        request.setExportCollection(new ExportCollection());
        request.setOperationType("operationType");
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors.add(new ValidationError("parentFieldNameTest", "fieldNameTest", "violationTest", "limit"));
        when(validationUtils.validateDocuments(any(), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> exportCollectionValidation.validateConfirm(request));
        // Then
        assertEquals("validationError", exception.getKey());
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals(validationErrors, exception.getEntity());
    }


}