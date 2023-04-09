package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.repository.CompanyAddressesRepository;
import com.pagonxt.onetradefinance.integrations.repository.CompanyDataRepository;
import com.pagonxt.onetradefinance.integrations.repository.CompanyDataSpecification;
import com.pagonxt.onetradefinance.integrations.repository.entity.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@UnitTest
class SantanderCustomerServiceTest {
    @InjectMocks
    private SantanderCustomerService santanderCustomerService;

    @Mock
    private CompanyAddressesRepository companyAddressesRepository;
    @Mock
    private CompanyDataRepository companyDataRepository;

    @Mock
    Page<CompanyDataDAO> pageMock;

    @Test
    void searchClients_whenAllFields_returnCustomer() {
        // Given
        CustomerQuery query = new CustomerQuery("aluminios", "003", "693", null);

        CompanyDataDAO companyDataDAO = new CompanyDataDAO();
        CompanyDataId companyDataId = new CompanyDataId("ES", "-1159951693");
        companyDataDAO.setCompanyDataId(companyDataId);
        companyDataDAO.setCompanyName("ALUMINIOS ANTEQUERA SL");
        companyDataDAO.setCustomerLegalId("J003634191");

        CompanyFiscalDocumentsDAO companyFiscalDocumentsDAO = new CompanyFiscalDocumentsDAO();
        CompanyFiscalDocumentsId companyFiscalDocumentsId = new CompanyFiscalDocumentsId("ES", "-1159951693", "tax_id", "B93061240");
        companyFiscalDocumentsDAO.setCompanyFiscalDocumentsId(companyFiscalDocumentsId);
        companyDataDAO.setCompanyFiscalDocumentsDAOS(List.of(companyFiscalDocumentsDAO));

        when(pageMock.stream()).thenReturn(Stream.of(companyDataDAO));
        when(companyDataRepository.findAll(any(CompanyDataSpecification.class), any(Pageable.class)))
                .thenReturn(pageMock);
        // When
        List<Customer> result = santanderCustomerService.searchCustomers(query);
        // Then
        List<Customer> expectedResult = List.of(new Customer("-1159951693", "ES", "ALUMINIOS ANTEQUERA SL"
                , "B93061240", "", "J003634191"
                , "", ""));
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void searchClients_whenNull_returnNullCustomer() {
        // Given
        CustomerQuery query = new CustomerQuery("aluminios", "003", "693", null);

        when(pageMock.stream()).thenReturn(Stream.of((CompanyDataDAO) null));
        when(companyDataRepository.findAll(any(CompanyDataSpecification.class), any(Pageable.class)))
                .thenReturn(pageMock);
        // When
        List<Customer> result = santanderCustomerService.searchCustomers(query);
        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNull(result.get(0));
    }

    @Test
    void searchClients_ko_thenThrowServiceException() {
        // Given
        CustomerQuery query = new CustomerQuery("aluminios", "003", "693", null);

        when(companyDataRepository.findAll(any(CompanyDataSpecification.class), any(Pageable.class)))
                .thenThrow(NullPointerException.class);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> santanderCustomerService.searchCustomers(query));
        // Then
        assertEquals("errorRepository", exception.getKey());
        assertEquals("Connection with repository failed", exception.getMessage());
    }

    @Test
    void getCustomerByPersonNumber_ok_returnCustomer() {
        // Given
        CompanyDataDAO companyDataDAO = new CompanyDataDAO();
        CompanyDataId companyDataId = new CompanyDataId("ES", "-1159951693");
        companyDataDAO.setCompanyDataId(companyDataId);
        companyDataDAO.setCompanyName("ALUMINIOS ANTEQUERA SL");
        companyDataDAO.setCustomerLegalId("J003634191");

        CompanyFiscalDocumentsDAO companyFiscalDocumentsDAO = new CompanyFiscalDocumentsDAO();
        CompanyFiscalDocumentsId companyFiscalDocumentsId = new CompanyFiscalDocumentsId("ES", "-1159951693", "tax_id", "B93061240");
        companyFiscalDocumentsDAO.setCompanyFiscalDocumentsId(companyFiscalDocumentsId);
        companyDataDAO.setCompanyFiscalDocumentsDAOS(List.of(companyFiscalDocumentsDAO));
        when(companyDataRepository.findByCustomerLegalId("B93061240")).thenReturn(List.of(companyDataDAO));
        // When
        Customer result = santanderCustomerService.getCustomerByPersonNumber("B93061240");
        // Then
        Customer expectedResult = new Customer("-1159951693", "ES", "ALUMINIOS ANTEQUERA SL"
                , "B93061240", "", "J003634191", "", "");
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void getCustomerByPersonNumber_ko_thenThrowServiceException() {
        // Given
        when(companyDataRepository.findByCustomerLegalId("B93061240"))
                .thenThrow(NullPointerException.class);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> santanderCustomerService.getCustomerByPersonNumber("B93061240"));
        // Then
        assertEquals("errorRepository", exception.getKey());
        assertEquals("Connection with repository failed", exception.getMessage());
    }

    @Test
    void getCustomerByPersonNumber_whenEmptyList_returnNullCustomer() {
        // Given
        when(companyDataRepository.findByCustomerLegalId("B93061240")).thenReturn(List.of());
        // When
        Customer result = santanderCustomerService.getCustomerByPersonNumber("B93061240");
        // Then
        assertNull(result);
    }

    @Test
    void getCustomerAddressByCompanyAddressesId_ok_returnAddress() {
        CompanyAddressesId companyAddressesId = new CompanyAddressesId("ES", "globalID", "MAIN");
        CompanyAddressesDAO companyAddressesDAO = new CompanyAddressesDAO();
        companyAddressesDAO.setCompanyAddressesId(companyAddressesId);
        companyAddressesDAO.setProvince("MADRID");
        when(companyAddressesRepository.findById(companyAddressesId)).thenReturn(Optional.of(companyAddressesDAO));
        // When
        CompanyAddressesDAO result = santanderCustomerService.getCustomerAddressByCompanyAddressesId(companyAddressesId);
        // Then
        assertEquals("MADRID", result.getProvince());
    }

    @Test
    void getCustomerAddressByCompanyAddressesId_isEmpty_returnNull() {
        CompanyAddressesId companyAddressesId = new CompanyAddressesId("ES", "globalID", "MAIN");
        when(companyAddressesRepository.findById(companyAddressesId)).thenReturn(Optional.empty());
        // When
        CompanyAddressesDAO result = santanderCustomerService.getCustomerAddressByCompanyAddressesId(companyAddressesId);
        // Then
        assertNull(result);
    }

    @Test
    void getCustomerAddressByCompanyAddressesId_ko_thenThrowServiceException() {
        CompanyAddressesId companyAddressesId = new CompanyAddressesId("ES", "globalID", "MAIN");
        when(companyAddressesRepository.findById(companyAddressesId)).thenThrow(NullPointerException.class);
        // When
        ServiceException exception = assertThrows(ServiceException.class, () -> santanderCustomerService.getCustomerAddressByCompanyAddressesId(companyAddressesId));
        // Then
        assertEquals("errorRepository", exception.getKey());
        assertEquals("Connection with repository failed", exception.getMessage());
    }
}
