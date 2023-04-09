package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.external.backend.configuration.JasperConfigurationProperties;
import com.pagonxt.onetradefinance.external.backend.configuration.SecurityConfigurationAuthorProperties;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionRequestService;
import com.pagonxt.onetradefinance.external.backend.service.mapper.PricesChartDataMapper;
import com.pagonxt.onetradefinance.external.backend.service.model.ExportCollectionPricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.model.ImportCollectionPricesChartData;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeRequestService;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CollectionTypeService;
import com.pagonxt.onetradefinance.integrations.service.TradeSpecialPricesService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class PricesChartPdfServiceTest {

    @Mock
    SecurityConfigurationAuthorProperties securityConfigurationAuthorProperties;
    @Mock
    ExportCollectionRequestService exportCollectionRequestService;
    @Mock
    TradeRequestService tradeRequestService;
    @Mock
    JasperConfigurationProperties jasperConfigurationProperties;
    @Mock
    TradeSpecialPricesService tradeSpecialPricesService;
    @Mock
    CollectionTypeService collectionTypeService;
    @Mock
    DateFormatProperties dateFormatProperties;
    @Mock
    AccountService accountService;
    @Mock
    PricesChartDataMapper pricesChartDataMapper;
    @InjectMocks
    PricesChartPdfService pricesChartPdfService;


    @Test
    void streamExportCollectionPricesChartPdf_isOk() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        ReflectionTestUtils.setField(pricesChartPdfService, "dateFormat", dateFormat, DateFormat.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionRequest).getCustomer();
        doReturn("EUR").when(exportCollectionRequest).getCurrency();
        doReturn(10_000.00d).when(exportCollectionRequest).getAmount();
        doReturn("ES").when(exportCollectionRequest).getCountry();
        Account account = new Account("idTest", "customerIdTest", "iban", "00490001", "EUR");
        doReturn(account).when(exportCollectionRequest).getNominalAccount();
        doReturn(exportCollectionRequest).when(exportCollectionRequestService).getExportCollectionRequestDraft(any(), any());
        ExportCollectionPricesChartData exportCollectionPricesChartData = mock(ExportCollectionPricesChartData.class);
        doReturn(exportCollectionPricesChartData).when(pricesChartDataMapper).mapExportCollectionPricesChartData(exportCollectionRequest, "es_es");
        doReturn(account).when(accountService).getAccountById("idTest");
        doReturn("src/main/resources/").when(jasperConfigurationProperties).getResourcePath();
        doReturn("PagoNxt One Trade Finance").when(securityConfigurationAuthorProperties).getAuthor();

        String date = "29-06-2022";
        List<TradeSpecialPrices> tradeSpecialPrices = new ArrayList<>();
        TradeSpecialPrices specialPrices = new TradeSpecialPrices();
        specialPrices.setPeriodicity("99999 D");
        specialPrices.setPercentage(null);
        specialPrices.setConceptName("COM.PROT.EFE.RE");
        specialPrices.setAmount("6148.99502");
        tradeSpecialPrices.add(specialPrices);
        doReturn(tradeSpecialPrices).when(tradeSpecialPricesService).getSpecialPrices(any(), eq("es_es"));

        doReturn("collectionId").when(collectionTypeService).getIdByKey(any());
        // When
        pricesChartPdfService.streamExportCollectionPricesChartPdf("CLE-XX", outputStream, date, null);

        // Then
        verify(exportCollectionRequestService).getExportCollectionRequestDraft(eq("CLE-XX"), any());
        verify(securityConfigurationAuthorProperties).getAuthor();
    }

    @Test
    void streamExportCollectionPricesChartPdf_whenIncorrectNominalAccountId_thenThrowException() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        ReflectionTestUtils.setField(pricesChartPdfService, "dateFormat", dateFormat, DateFormat.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionRequest).getCustomer();
        doReturn("EUR").when(exportCollectionRequest).getCurrency();
        doReturn(10_000.00d).when(exportCollectionRequest).getAmount();
        doReturn("ES").when(exportCollectionRequest).getCountry();
        Account account = new Account("idTestNotFound", "customerIdTest", "iban", "00490001", "EUR");
        doReturn(account).when(exportCollectionRequest).getNominalAccount();
        doReturn(exportCollectionRequest).when(exportCollectionRequestService).getExportCollectionRequestDraft(any(), any());
        doReturn(null).when(accountService).getAccountById("idTestNotFound");
        String date = "29-06-2022";
        doReturn("collectionId").when(collectionTypeService).getIdByKey(any());
        // When
        ServiceException exception = assertThrows(ServiceException.class, () ->
                pricesChartPdfService.streamExportCollectionPricesChartPdf("CLE-XX", outputStream, date, null));

        // Then
        assertEquals("getBranchId", exception.getKey());
        assertEquals("Nominal account data not found", exception.getMessage());
    }

    @Test
    void streamExportCollectionPricesChartPdf_whenNullNominalAccountConstCenter_thenThrowException() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        ReflectionTestUtils.setField(pricesChartPdfService, "dateFormat", dateFormat, DateFormat.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionRequest).getCustomer();
        doReturn("EUR").when(exportCollectionRequest).getCurrency();
        doReturn(10_000.00d).when(exportCollectionRequest).getAmount();
        doReturn("ES").when(exportCollectionRequest).getCountry();
        Account account = new Account("idTestNotFound", "customerIdTest", "iban", null, "EUR");
        doReturn(account).when(exportCollectionRequest).getNominalAccount();
        doReturn(exportCollectionRequest).when(exportCollectionRequestService).getExportCollectionRequestDraft(any(), any());
        doReturn(account).when(accountService).getAccountById("idTestNotFound");
        String date = "29-06-2022";
        doReturn("collectionId").when(collectionTypeService).getIdByKey(any());
        // When
        ServiceException exception = assertThrows(ServiceException.class, () ->
                pricesChartPdfService.streamExportCollectionPricesChartPdf("CLE-XX", outputStream, date, null));

        // Then
        assertEquals("getBranchId", exception.getKey());
        assertEquals("Nominal account data not found", exception.getMessage());
    }

    @Test
    void streamExportCollectionPricesChartPdf_whenNoNominalAccountInRequest_thenThrowException() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        ReflectionTestUtils.setField(pricesChartPdfService, "dateFormat", dateFormat, DateFormat.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionRequest).getCustomer();
        doReturn("EUR").when(exportCollectionRequest).getCurrency();
        doReturn(10_000.00d).when(exportCollectionRequest).getAmount();
        doReturn("ES").when(exportCollectionRequest).getCountry();
        doReturn(null).when(exportCollectionRequest).getNominalAccount();
        doReturn(exportCollectionRequest).when(exportCollectionRequestService).getExportCollectionRequestDraft(any(), any());
        String date = "29-06-2022";
        doReturn("collectionId").when(collectionTypeService).getIdByKey(any());
        // When
        ServiceException exception = assertThrows(ServiceException.class, () ->
                pricesChartPdfService.streamExportCollectionPricesChartPdf("CLE-XX", outputStream, date, null));

        // Then
        assertEquals("getBranchId", exception.getKey());
        assertEquals("Nominal Account not found in request", exception.getMessage());
    }

    @Test
    void streamExportCollectionPricesChartPdf_whenNoNominalAccountIdInRequest_thenThrowException() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        ReflectionTestUtils.setField(pricesChartPdfService, "dateFormat", dateFormat, DateFormat.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ExportCollectionRequest exportCollectionRequest = mock(ExportCollectionRequest.class);
        Customer customer = mock(Customer.class);
        doReturn(customer).when(exportCollectionRequest).getCustomer();
        doReturn("EUR").when(exportCollectionRequest).getCurrency();
        doReturn(10_000.00d).when(exportCollectionRequest).getAmount();
        doReturn("ES").when(exportCollectionRequest).getCountry();
        doReturn(new Account()).when(exportCollectionRequest).getNominalAccount();
        doReturn(exportCollectionRequest).when(exportCollectionRequestService).getExportCollectionRequestDraft(any(), any());
        String date = "29-06-2022";
        doReturn("collectionId").when(collectionTypeService).getIdByKey(any());
        // When
        ServiceException exception = assertThrows(ServiceException.class, () ->
                pricesChartPdfService.streamExportCollectionPricesChartPdf("CLE-XX", outputStream, date, null));

        // Then
        assertEquals("getBranchId", exception.getKey());
        assertEquals("Nominal Account not found in request", exception.getMessage());
    }

    @Test
    void streamImportCollectionPricesChartPdf_isOk() {
        // Given
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        ReflectionTestUtils.setField(pricesChartPdfService, "dateFormat", dateFormat, DateFormat.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        TradeRequest tradeRequest = new TradeRequest();
        tradeRequest.setCustomer(mock(Customer.class));
        Map<String, Object> details = new HashMap<>();
        details.put(REQUEST_CURRENCY, "EUR");
        details.put(REQUEST_AMOUNT, 10000d);
        details.put(REQUEST_NOMINAL_ACCOUNT_ID, "idTest");
        tradeRequest.setDetails(details);
        tradeRequest.setCountry("ES");
        doReturn(tradeRequest).when(tradeRequestService).getTradeRequest(any(), any());

        Account account = new Account("idTest", "customerIdTest", "iban", "00490001", "EUR");
        doReturn(account).when(accountService).getAccountById("idTest");

        ImportCollectionPricesChartData importCollectionPricesChartData = mock(ImportCollectionPricesChartData.class);
        doReturn(importCollectionPricesChartData).when(pricesChartDataMapper).mapImportCollectionPricesChartData(tradeRequest, "es_es");

        doReturn("src/main/resources/").when(jasperConfigurationProperties).getResourcePath();
        doReturn("PagoNxt One Trade Finance").when(securityConfigurationAuthorProperties).getAuthor();

        String date = "29-06-2022";
        List<TradeSpecialPrices> tradeSpecialPrices = new ArrayList<>();
        TradeSpecialPrices specialPrices = new TradeSpecialPrices();
        specialPrices.setPeriodicity("99999 D");
        specialPrices.setPercentage(null);
        specialPrices.setConceptName("COM.PROT.EFE.RE");
        specialPrices.setAmount("6148.99502");
        tradeSpecialPrices.add(specialPrices);
        doReturn(tradeSpecialPrices).when(tradeSpecialPricesService).getSpecialPrices(any(), eq("es_es"));

        doReturn("collectionId").when(collectionTypeService).getIdByKey(any());
        // When
        pricesChartPdfService.streamImportCollectionPricesChartPdf("MTR-XX", outputStream, date, null);

        // Then
        verify(tradeRequestService).getTradeRequest(eq("MTR-XX"), any());
        verify(securityConfigurationAuthorProperties).getAuthor();
    }
}
