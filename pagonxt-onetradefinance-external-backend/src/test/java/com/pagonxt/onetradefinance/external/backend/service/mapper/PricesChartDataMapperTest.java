package com.pagonxt.onetradefinance.external.backend.service.mapper;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.model.ExportCollectionPricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.model.ImportCollectionPricesChartData;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesId;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@UnitTest
class PricesChartDataMapperTest {

    @InjectMocks
    PricesChartDataMapper pricesChartDataMapper;
    @Mock
    OfficeInfoService officeInfoService;
    @Mock
    CustomerService customerService;

    @Test
    void mapExportCollectionPricesChartData_ok_returnExportCollectionPricesChartData(){
        // Given
        ExportCollectionRequest exportCollectionRequest = generateExportCollectionRequestTest();
        when(customerService.getCustomerByPersonNumber("customerPersonNumber")).thenReturn(generateCustomer());
        exportCollectionRequest.setOffice("1234");
        OfficeInfo officeInfo = generateOfficeInfoTest();
        when(officeInfoService.getOfficeInfo("1234")).thenReturn(officeInfo);
        // When
        ExportCollectionPricesChartData result = pricesChartDataMapper.mapExportCollectionPricesChartData(exportCollectionRequest, "es_es");
        // Then
        assertEquals("debtorTest", result.getDebtor());
        assertEquals("debtorBank", result.getDebtorBank());
        assertEquals("customerNameTest", result.getCustomerName());
        assertEquals("1234", result.getOfficeId());
        assertEquals("addressTest placeTest", result.getOfficeAddress());
        assertEquals("ES", result.getOfficeAddressCountry());
        assertEquals("CLE-TEST", result.getOurReference());
        assertEquals("clientReferenceTest", result.getClientReference());
        assertEquals("123 456,44", result.getAmount());
        assertEquals("EUR", result.getCurrencyCode());
    }

    @Test
    void mapExportCollectionPricesChartData_addressFound_returnExportCollectionPricesChartData(){
        // Given
        ExportCollectionRequest exportCollectionRequest = generateExportCollectionRequestTest();
        when(customerService.getCustomerByPersonNumber("customerPersonNumber")).thenReturn(generateCustomer());

        CompanyAddressesId companyAddressesId = new CompanyAddressesId("ES", "customerGlobalId", "MAIN");
        CompanyAddressesDAO companyAddressesDAO = new CompanyAddressesDAO();
        companyAddressesDAO.setCompanyAddressesId(companyAddressesId);
        companyAddressesDAO.setDepartment("dep1");
        companyAddressesDAO.setFloor("floor1");
        companyAddressesDAO.setTown("town1");
        companyAddressesDAO.setProvince("MADRID");
        when(customerService.getCustomerAddressByCompanyAddressesId(companyAddressesId)).thenReturn(companyAddressesDAO);
        exportCollectionRequest.setOffice("1234");
        OfficeInfo officeInfo = generateOfficeInfoTest();
        when(officeInfoService.getOfficeInfo("1234")).thenReturn(officeInfo);
        // When
        ExportCollectionPricesChartData result = pricesChartDataMapper.mapExportCollectionPricesChartData(exportCollectionRequest, "es_es");
        // Then
        assertEquals("dep1 floor1", result.getCustomerAddress());
        assertEquals("town1", result.getCustomerAddressLocality());
        assertEquals("MADRID", result.getCustomerAddressProvince());
        assertEquals(new Locale("", "ES").getDisplayCountry(), result.getCustomerAddressCountry());
    }

    @Test
    void mapExportCollectionPricesChartData_addressFoundParamsNull_returnExportCollectionPricesChartData(){
        // Given
        ExportCollectionRequest exportCollectionRequest = generateExportCollectionRequestTest();
        when(customerService.getCustomerByPersonNumber("customerPersonNumber")).thenReturn(generateCustomer());

        CompanyAddressesId companyAddressesId = new CompanyAddressesId("ES", "customerGlobalId", "MAIN");
        CompanyAddressesDAO companyAddressesDAO = new CompanyAddressesDAO();
        companyAddressesDAO.setCompanyAddressesId(companyAddressesId);
        when(customerService.getCustomerAddressByCompanyAddressesId(companyAddressesId)).thenReturn(companyAddressesDAO);
        exportCollectionRequest.setOffice("1234");
        OfficeInfo officeInfo = generateOfficeInfoTest();
        when(officeInfoService.getOfficeInfo("1234")).thenReturn(officeInfo);
        // When
        ExportCollectionPricesChartData result = pricesChartDataMapper.mapExportCollectionPricesChartData(exportCollectionRequest, "es_es");
        // Then
        assertEquals("-", result.getCustomerAddress());
        assertEquals("-", result.getCustomerAddressLocality());
        assertEquals("-", result.getCustomerAddressProvince());
    }

    @Test
    void mapExportCollectionPricesChartData_officeCodeNull_returnExportCollectionPricesChartData(){
        // Given
        ExportCollectionRequest exportCollectionRequest = generateExportCollectionRequestTest();
        when(customerService.getCustomerByPersonNumber("customerPersonNumber")).thenReturn(generateCustomer());
        exportCollectionRequest.setOffice(null);
        // When
        ExportCollectionPricesChartData result = pricesChartDataMapper.mapExportCollectionPricesChartData(exportCollectionRequest, "es_es");
        // Then
        assertEquals("debtorTest", result.getDebtor());
        assertEquals("debtorBank", result.getDebtorBank());
        assertEquals("customerNameTest", result.getCustomerName());
        assertNull(result.getOfficeId());
        assertNull(result.getOfficeAddress());
        assertNull(result.getOfficeAddressCountry());
        assertEquals("CLE-TEST", result.getOurReference());
        assertEquals("clientReferenceTest", result.getClientReference());
        assertEquals("123 456,44", result.getAmount());
        assertEquals("EUR", result.getCurrencyCode());
    }

    @Test
    void mapExportCollectionPricesChartData_officeInfoNotFound_returnExportCollectionPricesChartData(){
        // Given
        ExportCollectionRequest exportCollectionRequest = generateExportCollectionRequestTest();
        when(customerService.getCustomerByPersonNumber("customerPersonNumber")).thenReturn(generateCustomer());
        exportCollectionRequest.setOffice("1234");
        when(officeInfoService.getOfficeInfo("1234")).thenReturn(null);
        // When
        ExportCollectionPricesChartData result = pricesChartDataMapper.mapExportCollectionPricesChartData(exportCollectionRequest, "es_es");
        // Then
        assertEquals("debtorTest", result.getDebtor());
        assertEquals("debtorBank", result.getDebtorBank());
        assertEquals("customerNameTest", result.getCustomerName());
        assertEquals("1234", result.getOfficeId());
        assertNull(result.getOfficeAddress());
        assertNull(result.getOfficeAddressCountry());
        assertEquals("CLE-TEST", result.getOurReference());
        assertEquals("clientReferenceTest", result.getClientReference());
        assertEquals("123 456,44", result.getAmount());
        assertEquals("EUR", result.getCurrencyCode());
    }

    @Test
    void mapImportCollectionPricesChartData_ok_returnImportCollectionPricesChartData(){
        // Given
        TradeRequest tradeRequest = generateTradeRequestTest();
        when(customerService.getCustomerByPersonNumber("customerPersonNumber")).thenReturn(generateCustomer());
        tradeRequest.setOffice("1234");
        OfficeInfo officeInfo = generateOfficeInfoTest();
        when(officeInfoService.getOfficeInfo("1234")).thenReturn(officeInfo);
        // When
        ImportCollectionPricesChartData result = pricesChartDataMapper.mapImportCollectionPricesChartData(tradeRequest, "es_es");
        // Then
        assertEquals("beneficiaryTest", result.getBeneficiary());
        assertEquals("beneficiaryBank", result.getBeneficiaryBank());
        assertEquals("customerNameTest", result.getCustomerName());
        assertEquals("1234", result.getOfficeId());
        assertEquals("addressTest placeTest", result.getOfficeAddress());
        assertEquals("ES", result.getOfficeAddressCountry());
        assertEquals("MTR-TEST", result.getOurReference());
        assertEquals("clientReferenceTest", result.getClientReference());
        assertEquals("123 456,44", result.getAmount());
        assertEquals("EUR", result.getCurrencyCode());
    }

    private ExportCollectionRequest generateExportCollectionRequestTest() {
        ExportCollectionRequest exportCollectionRequest = new ExportCollectionRequest();
        exportCollectionRequest.setDebtorName("debtorTest");
        exportCollectionRequest.setDebtorBank("debtorBank");
        Customer customer = new Customer();
        customer.setName("customerNameTest");
        customer.setPersonNumber("customerPersonNumber");
        exportCollectionRequest.setCustomer(customer);
        exportCollectionRequest.setCode("CLE-TEST");
        exportCollectionRequest.setClientReference("clientReferenceTest");
        exportCollectionRequest.setAmount(123456.436d);
        exportCollectionRequest.setCurrency("EUR");
        exportCollectionRequest.setCountry("ES");
        return exportCollectionRequest;
    }

    private TradeRequest generateTradeRequestTest() {
        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setCode("MTR-TEST");
        tradeRequest.setCountry("ES");
        Customer customer = new Customer();
        customer.setName("customerNameTest");
        customer.setPersonNumber("customerPersonNumber");
        tradeRequest.setCustomer(customer);
        Map<String, Object> details = new HashMap<>();
        details.put(REQUEST_CURRENCY, "EUR");
        details.put(REQUEST_AMOUNT, 123456.436d);
        details.put(REQUEST_DEBTOR_NAME, "beneficiaryTest");
        details.put(REQUEST_DEBTOR_BANK, "beneficiaryBank");
        details.put(REQUEST_CUSTOMER_REFERENCE, "clientReferenceTest");
        tradeRequest.setDetails(details);
        return tradeRequest;
    }

    private static OfficeInfo generateOfficeInfoTest() {
        OfficeInfo officeInfo = new OfficeInfo();
        officeInfo.setOffice("1234");
        officeInfo.setCountry("ES");
        officeInfo.setAddress("addressTest");
        officeInfo.setPlace("placeTest");
        return officeInfo;
    }

    private static Customer generateCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId("customerGlobalId");
        customer.setCountryISO("ES");
        customer.setName("customerNameTest");
        customer.setPersonNumber("customerPersonNumber");
        return customer;
    }
}
