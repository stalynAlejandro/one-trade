package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Account;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class MockedAccountServiceTest {

    MockedAccountService mockedAccountService = new MockedAccountService(new ObjectMapper());

    @Test
    void testGetClientAccounts() {
        // Given

        // When
        List<Account> result = mockedAccountService.getCustomerAccounts("001");

        // Then
        List<Account> expectedResult = List.of(
                new Account("001001", "001", "PT50 0002 0123 5678 9015 1", "00490001", "EUR"),
                new Account("001002", "001", "PT50 0002 0123 5678 9015 2", "00490001", "PLN"),
                new Account("001003", "001", "PT50 0002 0123 5678 9015 3", "00490001", "GBP"),
                new Account("001004", "001", "PT50 0002 0123 5678 9015 4", "00490001", "EUR")
        );
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAccountById() {
        // Given
        Account expectedResult = new Account("001001", "001", "PT50 0002 0123 5678 9015 1", "00490001", "EUR");
        // When
        Account result = mockedAccountService.getAccountById("001001");
        // Then
        assertThat(result).isEqualTo(expectedResult);
    }
}
