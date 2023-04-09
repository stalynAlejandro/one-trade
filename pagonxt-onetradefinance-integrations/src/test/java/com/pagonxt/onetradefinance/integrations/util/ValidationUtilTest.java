package com.pagonxt.onetradefinance.integrations.util;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.ValidationError;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@UnitTest
class ValidationUtilTest {

    @InjectMocks
    ValidationUtil validationUtil;

    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Test
    void runValidations_whenInvalidObject_thenReturnListWithValidationErrors() {
        // Given
        ExportCollectionRequest request = new ExportCollectionRequest();
        request.setAmount(10000000000d);
        int exportCollectionRequestValidations = 9;
        List<ValidationError> previousValidationErrors = new ArrayList<>();
        // When
        List<ValidationError> validationErrors = validationUtil.runValidations(request, "testParentFieldName", previousValidationErrors);
        // Then
        assertEquals(exportCollectionRequestValidations, validationErrors.size());
        ValidationError validationErrorAmount = validationErrors.stream().filter(v -> v.getFieldName().equals("amount")).findFirst().orElse(null);
        assertNotNull(validationErrorAmount);
        assertEquals("testParentFieldName", validationErrorAmount.getParentFieldName());
        assertEquals("maxNumber", validationErrorAmount.getViolation());
        assertEquals("9999999999.99", validationErrorAmount.getLimit());

    }
}
