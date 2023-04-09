package com.pagonxt.onetradefinance.work.expression.common;

import com.pagonxt.onetradefinance.integrations.configuration.IntegrationDirectoryProperties;
import com.pagonxt.onetradefinance.integrations.service.DirectoryService;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.RecipientsService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@UnitTest
class DirectoryExpressionsTest {

    private static final String EXPECTED_RECIPIENTS = "recipient1@test.com,recipient2@test.com";
    private static final String EXPECTED_OFFICE_USER_EMAIL = "officeuser1@test.com";
    private static final String EXPECTED_CUSTOMER_EMAIL = "customer1@test.com";
    private static final String EXPECTED_OFFICE_EMAIL = "office123@test.com";
    private static final String EXPECTED_MIDDLE_OFFICE_EMAIL = "midlle.office.456@test.com";

    @Mock
    DirectoryService directoryService;

    @Mock
    RecipientsService recipientsService;

    @Mock
    IntegrationDirectoryProperties integrationDirectoryProperties;

    @InjectMocks
    DirectoryExpressions directoryExpressions;

    @Test
    void testGetRecipientsForNotification() {
        // Given
        Mockito.when(directoryService.getRecipientsForNotification(any(), any())).thenReturn(EXPECTED_RECIPIENTS);

        // When
        String result = directoryExpressions.getRecipientsForNotification("initiatorId", "notificationId");

        // Then
        assertEquals(EXPECTED_RECIPIENTS, result);
    }

    @Test
    void testGetOfficeUserEmail() {
        // Given
        Mockito.when(directoryService.getOfficeUserEmail(any())).thenReturn(EXPECTED_OFFICE_USER_EMAIL);

        // When
        String result = directoryExpressions.getOfficeUserEmail("userId");

        // Then
        assertEquals(EXPECTED_OFFICE_USER_EMAIL, result);
    }

    @Test
    void testGetCustomerEmail() {
        // Given
        Mockito.when(directoryService.getCustomerEmail(any())).thenReturn(EXPECTED_CUSTOMER_EMAIL);

        // When
        String result = directoryExpressions.getCustomerEmail("customerId");

        // Then
        assertEquals(EXPECTED_CUSTOMER_EMAIL, result);
    }

    @Test
    void testGetOfficeEmail() {
        // Given
        Mockito.when(recipientsService.getOfficeEmail(any())).thenReturn(EXPECTED_OFFICE_EMAIL);

        // When
        String result = directoryExpressions.getOfficeEmail("officeId");

        // Then
        assertEquals(EXPECTED_OFFICE_EMAIL, result);
    }

    @Test
    void testGetMiddleOfficeEmail() {
        // Given
        Mockito.when(recipientsService.getMiddleOfficeEmail(any())).thenReturn(EXPECTED_MIDDLE_OFFICE_EMAIL);

        // When
        String result = directoryExpressions.getMiddleOfficeEmail("MiddleOfficeId");

        // Then
        assertEquals(EXPECTED_MIDDLE_OFFICE_EMAIL, result);
    }

}
