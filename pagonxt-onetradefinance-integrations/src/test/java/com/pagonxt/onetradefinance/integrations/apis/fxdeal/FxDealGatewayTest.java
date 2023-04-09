package com.pagonxt.onetradefinance.integrations.apis.fxdeal;

import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.SantanderFxDealResponse;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationFxDealProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class FxDealGatewayTest {

    private static final String TIME_ZONE = "Europe/Madrid";

    @InjectMocks
    private FxDealGateway fxDealGateway;
    @Mock
    private IntegrationFxDealProperties integrationFxDealProperties;
    @Spy
    private HttpHeaders headers;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private ApiTokenService apiTokenService;
    @Captor
    private ArgumentCaptor<HttpEntity<Void>> httpEntityCaptor;
    @Spy
    private static ApiParamsUtils apiParamsUtils;
    @Mock
    private JWTService jwtService;

    private static DateFormat dateFormat;

    @BeforeAll
    public static void setup() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone(TIME_ZONE);
        apiParamsUtils = new ApiParamsUtils(dateFormatProperties);
    }

    @Test
    void searchFxDeals_ok_thenInvokeRestTemplate() throws ParseException {
        // Given
        when(integrationFxDealProperties.getUrl()).thenReturn("testUrl/fx_deals/");
        when(integrationFxDealProperties.getLimit()).thenReturn(100);
        SantanderFxDealResponse body = new SantanderFxDealResponse();
        ResponseEntity<SantanderFxDealResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderFxDealResponse.class))).thenReturn(response);
        FxDealQuery fxDealQuery = generateFxDealQuery();
        // When
        fxDealGateway.searchFxDeals(fxDealQuery);
        // Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(stringCaptor.capture(), eq(HttpMethod.GET), httpEntityCaptor.capture(), eq(SantanderFxDealResponse.class));
        String expectedUrl = "testUrl/fx_deals/?bank_id=bankIdTest&branch_id=branchIdTest&date_type=USE&from_date=2022-09-29&to_date=2022-10-29" +
                "&sell_currency=sellCurrencyTest&buy_currency=buyCurrencyTest&direction=BUY&balance_fxdeal_type=A" +
                "&balance_fxdeal_amount=345.68&balance_fxdeal_currency=balanceCurrencyTest&_limit=100";
        assertEquals(expectedUrl, stringCaptor.getValue());
        HttpEntity<Void> httpEntity = httpEntityCaptor.getValue();
        List<String> customerIdList = httpEntity.getHeaders().get("Customer-Id");
        assertNotNull(customerIdList);
        assertEquals(1, customerIdList.size());
        assertEquals("customerIdTest", customerIdList.get(0));
    }

    @Test
    void searchFxDeals_whenTokenExpired_thenInvokeRestTemplateTwice() throws ParseException {
        // Given
        when(integrationFxDealProperties.getUrl()).thenReturn("testUrl/fx_deals/");
        when(integrationFxDealProperties.getLimit()).thenReturn(100);
        SantanderFxDealResponse body = new SantanderFxDealResponse();
        ResponseEntity<SantanderFxDealResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderFxDealResponse.class)))
                .thenThrow(ExpiredTokenException.class).thenReturn(response);
        FxDealQuery fxDealQuery = generateFxDealQuery();
        // When
        fxDealGateway.searchFxDeals(fxDealQuery);
        // Then
        verify(restTemplate, times(2)).exchange(anyString(), eq(HttpMethod.GET),
                any(HttpEntity.class), eq(SantanderFxDealResponse.class));
    }

    @Test
    void searchFxDeals_whenCustomerIdNull_thenInvokeRestTemplate() throws ParseException {
        // Given
        when(integrationFxDealProperties.getUrl()).thenReturn("testUrl/fx_deals/");
        when(integrationFxDealProperties.getLimit()).thenReturn(100);
        SantanderFxDealResponse body = new SantanderFxDealResponse();
        ResponseEntity<SantanderFxDealResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderFxDealResponse.class))).thenReturn(response);
        FxDealQuery fxDealQuery = generateFxDealQuery();
        fxDealQuery.setCustomerId(null);
        // When
        fxDealGateway.searchFxDeals(fxDealQuery);
        // Then
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.GET), httpEntityCaptor.capture(), eq(SantanderFxDealResponse.class));
        HttpEntity<Void> httpEntity = httpEntityCaptor.getValue();
        List<String> customerIdList = httpEntity.getHeaders().get("Customer-Id");
        assertNull(customerIdList);
    }

    @Test
    void searchFxDeals_whenNoContent_thenReturnNull() throws ParseException {
        // Given
        when(integrationFxDealProperties.getUrl()).thenReturn("testUrl/fx_deals/");
        when(integrationFxDealProperties.getLimit()).thenReturn(100);
        ResponseEntity<SantanderFxDealResponse> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderFxDealResponse.class))).thenReturn(response);
        FxDealQuery fxDealQuery = generateFxDealQuery();
        // When and Then
        SantanderFxDealResponse result = fxDealGateway.searchFxDeals(fxDealQuery);
        // Then
        assertNull(result);
    }

    @Test
    void searchFxDeals_whenBodyNull_thenThrowIntegrationException() throws ParseException {
        // Given
        when(integrationFxDealProperties.getUrl()).thenReturn("testUrl/fx_deals/");
        when(integrationFxDealProperties.getLimit()).thenReturn(100);
        ResponseEntity<SantanderFxDealResponse> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderFxDealResponse.class))).thenReturn(response);
        FxDealQuery fxDealQuery = generateFxDealQuery();
        // When and Then
        assertThrows(IntegrationException.class, () -> fxDealGateway.searchFxDeals(fxDealQuery));
    }

    @Test
    void searchFxDeals_whenParamNull_thenInvokeRestTemplateWithoutParam() throws ParseException {
        // Given
        when(integrationFxDealProperties.getUrl()).thenReturn("testUrl/fx_deals/");
        when(integrationFxDealProperties.getLimit()).thenReturn(100);
        SantanderFxDealResponse body = new SantanderFxDealResponse();
        ResponseEntity<SantanderFxDealResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), any(), any(), eq(SantanderFxDealResponse.class))).thenReturn(response);
        FxDealQuery fxDealQuery = generateFxDealQuery();
        fxDealQuery.setBranchId(null);
        // When
        fxDealGateway.searchFxDeals(fxDealQuery);
        // Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplate).exchange(stringCaptor.capture(), eq(HttpMethod.GET), httpEntityCaptor.capture(), eq(SantanderFxDealResponse.class));
        String expectedUrl = "testUrl/fx_deals/?bank_id=bankIdTest&date_type=USE&from_date=2022-09-29&to_date=2022-10-29" +
                "&sell_currency=sellCurrencyTest&buy_currency=buyCurrencyTest&direction=BUY&balance_fxdeal_type=A" +
                "&balance_fxdeal_amount=345.68&balance_fxdeal_currency=balanceCurrencyTest&_limit=100";
        assertEquals(expectedUrl, stringCaptor.getValue());
    }

    private FxDealQuery generateFxDealQuery() throws ParseException {
        FxDealQuery fxDealQuery = new FxDealQuery();
        fxDealQuery.setCustomerId("customerIdTest");
        fxDealQuery.setBankId("bankIdTest");
        fxDealQuery.setBranchId("branchIdTest");
        Date fromDate = dateFormat.parse("2022-09-29");
        Date toDate = dateFormat.parse("2022-10-29");
        fxDealQuery.setFromDate(fromDate);
        fxDealQuery.setToDate(toDate);
        fxDealQuery.setSellCurrency("sellCurrencyTest");
        fxDealQuery.setBuyCurrency("buyCurrencyTest");
        fxDealQuery.setDirection("BUY");
        fxDealQuery.setBalanceFxDealAmount(345.68d);
        fxDealQuery.setBalanceFxDealCurrency("balanceCurrencyTest");
        return fxDealQuery;
    }
}
