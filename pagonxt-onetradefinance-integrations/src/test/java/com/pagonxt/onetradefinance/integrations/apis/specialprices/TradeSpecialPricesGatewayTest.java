package com.pagonxt.onetradefinance.integrations.apis.specialprices;

import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SantanderTradeSpecialPricesResponse;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationTradeSpecialPricesProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;
import com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@UnitTest
class TradeSpecialPricesGatewayTest {

    private static final String TIME_ZONE = "Europe/Madrid";

    @Mock
    IntegrationTradeSpecialPricesProperties integrationTradeSpecialPricesProperties;

    @Spy
    HttpHeaders httpHeaderTradeSpecialPrices;

    @Mock
    RestTemplate restTemplateTradeSpecialPrices;

    @Mock
    ApiTokenService apiTokenService;

    @Captor
    private ArgumentCaptor<HttpEntity<Void>> httpEntityCaptor;

    @Spy
    private static ApiParamsUtils apiParamsUtils;
    @Mock
    private JWTService jwtService;

    @InjectMocks
    TradeSpecialPricesGateway tradeSpecialPricesGateway;

    @BeforeAll
    public static void setup() {
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone(TIME_ZONE);
        apiParamsUtils = new ApiParamsUtils(dateFormatProperties);
    }

    @Test
    void getSpecialPrices_isOk_invokesRestTemplate() {
        // Given
        when(integrationTradeSpecialPricesProperties.getUrl()).thenReturn("testUrl/special-prices/");
        when(integrationTradeSpecialPricesProperties.getLimit()).thenReturn(100);
        SantanderTradeSpecialPricesResponse body = new SantanderTradeSpecialPricesResponse();
        ResponseEntity<SantanderTradeSpecialPricesResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplateTradeSpecialPrices.exchange(anyString(), any(), any(), eq(SantanderTradeSpecialPricesResponse.class))).thenReturn(response);
        TradeSpecialPricesQuery tradeSpecialPricesQuery = generateTradeSpecialPRicesQuery();
        // When
        tradeSpecialPricesGateway.getSpecialPrices(tradeSpecialPricesQuery);
        // Then
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        verify(restTemplateTradeSpecialPrices).exchange(stringCaptor.capture(), eq(HttpMethod.GET), httpEntityCaptor.capture(), eq(SantanderTradeSpecialPricesResponse.class));
        String expectedUrl = "testUrl/special-prices/?product_id=6556010000003&query_date=20220101&country=ES&currency_code=EUR&amount=123123.00&_limit=100";
        assertEquals(expectedUrl, stringCaptor.getValue());
        HttpEntity<Void> httpEntity = httpEntityCaptor.getValue();
        List<String> accessChannel = httpEntity.getHeaders().get("Access-Channel");
        assertNotNull(accessChannel);
        assertEquals(1, accessChannel.size());
        assertEquals("web", accessChannel.get(0));
    }

    @Test
    void getSpecialPrices_whenTokenExpired_thenInvokeRestTemplateTwice() {
        // Given
        when(integrationTradeSpecialPricesProperties.getUrl()).thenReturn("testUrl/special-prices/");
        when(integrationTradeSpecialPricesProperties.getLimit()).thenReturn(100);
        SantanderTradeSpecialPricesResponse body = new SantanderTradeSpecialPricesResponse();
        ResponseEntity<SantanderTradeSpecialPricesResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplateTradeSpecialPrices.exchange(anyString(), any(), any(), eq(SantanderTradeSpecialPricesResponse.class)))
                .thenThrow(ExpiredTokenException.class).thenReturn(response);
        TradeSpecialPricesQuery tradeSpecialPricesQuery = generateTradeSpecialPRicesQuery();
        // When
        tradeSpecialPricesGateway.getSpecialPrices(tradeSpecialPricesQuery);
        // Then
        verify(restTemplateTradeSpecialPrices, times(2)).exchange(
                anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(SantanderTradeSpecialPricesResponse.class));
    }

    @Test
    void getSpecialPrices_whenNoContent_thenReturnNull() {
        // Given
        when(integrationTradeSpecialPricesProperties.getUrl()).thenReturn("testUrl/special-prices/");
        when(integrationTradeSpecialPricesProperties.getLimit()).thenReturn(100);
        ResponseEntity<SantanderTradeSpecialPricesResponse> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        when(restTemplateTradeSpecialPrices.exchange(anyString(), any(), any(), eq(SantanderTradeSpecialPricesResponse.class))).thenReturn(response);
        TradeSpecialPricesQuery tradeSpecialPricesQuery = generateTradeSpecialPRicesQuery();
        // When and Then
        SantanderTradeSpecialPricesResponse result = tradeSpecialPricesGateway.getSpecialPrices(tradeSpecialPricesQuery);
        // Then
        assertNull(result);
    }

    @Test
    void getSpecialPrices_bodyIsNull_throwsIntegrationException(){
        // Given
        when(integrationTradeSpecialPricesProperties.getUrl()).thenReturn("testUrl/special-prices/");
        when(integrationTradeSpecialPricesProperties.getLimit()).thenReturn(100);
        ResponseEntity<SantanderTradeSpecialPricesResponse> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplateTradeSpecialPrices.exchange(anyString(), any(), any(), eq(SantanderTradeSpecialPricesResponse.class))).thenReturn(response);
        TradeSpecialPricesQuery tradeSpecialPricesQuery = generateTradeSpecialPRicesQuery();
        // When and Then
        assertThrows(IntegrationException.class, () -> tradeSpecialPricesGateway.getSpecialPrices(tradeSpecialPricesQuery));
    }

    private TradeSpecialPricesQuery generateTradeSpecialPRicesQuery(){
        TradeSpecialPricesQuery query = new TradeSpecialPricesQuery();
        query.setProductId("6556010000003");
        query.setCountry("ES");
        query.setCurrencyCode("EUR");
        query.setAmount(123123.0);
        query.setQueryDate("20220101");
        return query;
    }
}
