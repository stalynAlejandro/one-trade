package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;

import static com.pagonxt.onetradefinance.integrations.service.MockedDirectoryService.MOCK_RECIPIENTS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@UnitTest
class MockedDirectoryServiceTest {

    @InjectMocks
    MockedDirectoryService mockedDirectoryService;

    @Test
    void getRecipientsForNotification() {
        //given

        //when
        String result = mockedDirectoryService.getRecipientsForNotification("test", "test");

        //then
        assertThat(result).isEqualTo(MOCK_RECIPIENTS);
    }
    @ParameterizedTest
    @CsvSource({"OFFICE, testId.office-user@mail.com",
                "CUSTOMER, testId.customer@mail.com",
                "BACKOFFICE, testId@mail.com"})
    void getUserEmail_whenSpecificUserType_returnSpecificMail(String userType, String expectedResult) {
        //given
        User user = new User("testId", "test name", userType);
        //when
        String result = mockedDirectoryService.getUserEmail(user);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getUserEmail_whenUserNull_returnNull() {
        // Given
        // When
        String result = mockedDirectoryService.getUserEmail(null);
        // Then
        assertNull(result);
    }

    @Test
    void getUserEmail_whenIdNull_returnNull() {
        // Given
        User user = new User(null, "test name", "OFFICE");
        // When
        String result = mockedDirectoryService.getUserEmail(user);
        // Then
        assertNull(result);
    }

    @Test
    void getOfficeUserEmail() {
        //given

        //when
        String result = mockedDirectoryService.getOfficeUserEmail("test");

        //then
        assertThat(result).isEqualTo("test.office-user@mail.com");
    }

    @Test
    void getOfficeUserEmail_whenIdNull_returnNull() {
        // Given
        // When
        String result = mockedDirectoryService.getOfficeUserEmail(null);
        // Then
        assertNull(result);
    }
    
    @Test
    void getCustomerEmail() {
        //given

        //when
        String result = mockedDirectoryService.getCustomerEmail("test");

        //then
        assertThat(result).isEqualTo("test.customer@mail.com");
    }

    @Test
    void getCustomerEmail_whenIdNull_returnNull() {
        // Given
        // When
        String result = mockedDirectoryService.getCustomerEmail(null);
        // Then
        assertNull(result);
    }

}