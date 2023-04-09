package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.model.ValidationError;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesId;
import com.pagonxt.onetradefinance.integrations.util.ValidationUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class MockedCustomerServiceTest {

    ValidationUtil validationUtil = mock(ValidationUtil.class);
    MockedCustomerService mockedCustomerService = new MockedCustomerService(new ObjectMapper(), validationUtil);

    @Test
    void searchClients_whenAllFields_returnCustomer() {
        // Given
        CustomerQuery query = new CustomerQuery("spain", "c1234568", "BUC-1234568", "1234");
        // When
        List<Customer> result = mockedCustomerService.searchCustomers(query);
        // Then
        List<Customer> expectedResult = List.of(new Customer("002", "ES", "Coca Cola Spain", "c1234568", "1234", "BUC-1234568", "SME", "mock email 2"));
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void searchClients_whenNoFields_returnAllCustomers() {
        // Given
        CustomerQuery query = new CustomerQuery(null, null, null, null);
        // When
        List<Customer> result = mockedCustomerService.searchCustomers(query);
        // Then
        assertThat(result).hasSize(4);
    }

    @Test
    void searchClients_whenClientPepsi_thenThrowIntegrationException() {
        // Given
        CustomerQuery query = new CustomerQuery("Pepsi", null, null, null);
        // When
        ServiceException exception = assertThrows(IntegrationException.class, () ->  mockedCustomerService.searchCustomers(query));
        // Then
        assertEquals("errorIntegration", exception.getKey());
        assertEquals("Error connecting with external API", exception.getMessage());
    }

    @Test
    void testGetCustomerByPersonNumber() {
        // Given
        Customer expectedResult = new Customer("002", "ES", "Coca Cola Spain", "c1234568", "1234", "BUC-1234568", "SME", "mock email 2");
        // When
        Customer result = mockedCustomerService.getCustomerByPersonNumber("BUC-1234568");
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void getCustomerAddressByCompanyAddressesId_ok_returnMockedAddress() {
        // Given
        CompanyAddressesId companyAddressesId = new CompanyAddressesId("ES", "globalID", "MAIN");
        // When
        CompanyAddressesDAO result = mockedCustomerService.getCustomerAddressByCompanyAddressesId(companyAddressesId);
        // Then
        assertEquals("ES", result.getCompanyAddressesId().getAccess());
        assertEquals("globalID", result.getCompanyAddressesId().getCompanyGlobalId());
        assertEquals("MAIN", result.getCompanyAddressesId().getAddressTypeCode());
        assertEquals("MADRID", result.getProvince());
    }

    @Test
    void testSearchClients_whenInvalidField_thenThrowInvalidRequestException() {
        // Given
        CustomerQuery customerQuery = new CustomerQuery("spain", null, null, null);
        List<ValidationError> validationErrors = new ArrayList<>();
        ValidationError validationError = new ValidationError("customerSearch", "name", "maxSize", "50");
        validationErrors.add(validationError);
        when(validationUtil.runValidations(eq(customerQuery), eq("customerSearch"), any())).thenReturn(validationErrors);
        // When
        ServiceException exception = assertThrows(InvalidRequestException.class, () -> mockedCustomerService.searchCustomers(customerQuery));
        // Then
        assertEquals("There are validation errors", exception.getMessage());
        assertEquals("validationError", exception.getKey());
        ArrayList<ValidationError> entity = (ArrayList<ValidationError>) exception.getEntity();
        assertEquals(1, entity.size());
        assertEquals(validationError, entity.get(0));
    }
}
