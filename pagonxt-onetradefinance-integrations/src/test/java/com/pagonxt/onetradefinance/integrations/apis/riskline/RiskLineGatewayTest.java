package com.pagonxt.onetradefinance.integrations.apis.riskline;

import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineResponse;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationRiskLineProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@UnitTest
class RiskLineGatewayTest {

    private static final String TIME_ZONE = "Europe/Madrid";

    @Mock
    IntegrationRiskLineProperties integrationRiskLineProperties;
    @Spy
    HttpHeaders httpEntityRiskLine;
    @Mock
    RestTemplate restTemplateRiskLine;
    @Mock
    private ApiTokenService apiTokenService;
    @Spy
    private static ApiParamsUtils apiParamsUtils;
    @Mock
    private JWTService jwtService;

    @InjectMocks
    RiskLineGateway riskLineGateway;

    @BeforeAll
    public static void setup() {
        DateFormatProperties dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone(TIME_ZONE);
        apiParamsUtils = new ApiParamsUtils(dateFormatProperties);
    }

    @Test
    void getRiskLineByCostumerId_ok_thenInvokeRestTemplate() {
        //Given
        doReturn("testUrl/v2/risk_lines").when(integrationRiskLineProperties).getUrl();
        doReturn(100).when(integrationRiskLineProperties).getLimit();
        SantanderRiskLineListResponse body = new SantanderRiskLineListResponse();
        ResponseEntity<SantanderRiskLineListResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplateRiskLine.exchange(anyString(), any(), any(), eq(SantanderRiskLineListResponse.class))).thenReturn(response);
        RiskLineQuery query = new RiskLineQuery();
        query.setCustomerId("customerIdTest");
        query.setProductId("productId");
        query.setActive(true);
        // When
        riskLineGateway.getRiskLineByCostumerId(query);
        // Then
        verify(restTemplateRiskLine).exchange(eq("testUrl/v2/risk_lines?customer_id=customerIdTest&product=productId&active_only=true&_limit=100"), eq(HttpMethod.GET), any(), eq(SantanderRiskLineListResponse.class));
    }

    @Test
    void getRiskLineByCostumerId_whenTokenExpired_thenInvokeRestTemplateTwice() {
        //Given
        doReturn("testUrl/v2/risk_lines").when(integrationRiskLineProperties).getUrl();
        doReturn(100).when(integrationRiskLineProperties).getLimit();
        SantanderRiskLineListResponse body = new SantanderRiskLineListResponse();
        ResponseEntity<SantanderRiskLineListResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplateRiskLine.exchange(anyString(), any(), any(), eq(SantanderRiskLineListResponse.class)))
                .thenThrow(ExpiredTokenException.class).thenReturn(response);
        RiskLineQuery query = new RiskLineQuery();
        query.setCustomerId("customerIdTest");
        query.setProductId("productId");
        query.setActive(true);
        // When
        riskLineGateway.getRiskLineByCostumerId(query);
        // Then
        verify(restTemplateRiskLine, times(2)).exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(SantanderRiskLineListResponse.class));
    }

    @Test
    void getRiskLineByCostumerId_whenNoContent_thenReturnNull() {
        // Given
        when(integrationRiskLineProperties.getUrl()).thenReturn("testUrl/v2/risk_lines");
        when(integrationRiskLineProperties.getLimit()).thenReturn(100);
        ResponseEntity<SantanderRiskLineListResponse> response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        when(restTemplateRiskLine.exchange(anyString(), any(), any(), eq(SantanderRiskLineListResponse.class))).thenReturn(response);
        // When and Then
        SantanderRiskLineListResponse result = riskLineGateway.getRiskLineByCostumerId(new RiskLineQuery());
        // Then
        assertNull(result);
    }

    @Test
    void getRiskLineByCostumerId_whenBodyNull_thenThrowIntegrationException() {
        // Given
        when(integrationRiskLineProperties.getUrl()).thenReturn("testUrl/v2/risk_lines");
        when(integrationRiskLineProperties.getLimit()).thenReturn(100);
        ResponseEntity<SantanderRiskLineListResponse> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplateRiskLine.exchange(anyString(), any(), any(), eq(SantanderRiskLineListResponse.class))).thenReturn(response);
        RiskLineQuery query = new RiskLineQuery();
        // When and Then
        assertThrows(IntegrationException.class, () -> riskLineGateway.getRiskLineByCostumerId(query));
    }

    @Test
    void getRiskLineById_ok_thenInvokeRestTemplate() {
        //Given
        doReturn("testUrl/v2/risk_lines").when(integrationRiskLineProperties).getUrl();
        SantanderRiskLineResponse body = new SantanderRiskLineResponse();
        ResponseEntity<SantanderRiskLineResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplateRiskLine.exchange(anyString(), any(), any(), eq(SantanderRiskLineResponse.class))).thenReturn(response);
        RiskLineQuery query = new RiskLineQuery();
        query.setRiskLineId("idTest");
        query.setProductId("product");
        // When
        riskLineGateway.getRiskLineById(query);
        // Then
        verify(restTemplateRiskLine).exchange(eq("testUrl/v2/risk_lines/idTest?product=product"), eq(HttpMethod.GET), any(), eq(SantanderRiskLineResponse.class));
    }

    @Test
    void getRiskLineById_whenTokenExpired_thenInvokeRestTemplateTwice() {
        //Given
        doReturn("testUrl/v2/risk_lines").when(integrationRiskLineProperties).getUrl();
        SantanderRiskLineResponse body = new SantanderRiskLineResponse();
        ResponseEntity<SantanderRiskLineResponse> response = new ResponseEntity<>(body, HttpStatus.OK);
        when(restTemplateRiskLine.exchange(anyString(), any(), any(), eq(SantanderRiskLineResponse.class)))
                .thenThrow(ExpiredTokenException.class).thenReturn(response);
        RiskLineQuery query = new RiskLineQuery();
        query.setRiskLineId("idTest");
        query.setProductId("product");
        // When
        riskLineGateway.getRiskLineById(query);
        // Then
        verify(restTemplateRiskLine, times(2)).exchange(
                anyString(), eq(HttpMethod.GET), any(), eq(SantanderRiskLineResponse.class));
    }

    @Test
    void getRiskLineById_whenBodyNull_thenThrowIntegrationException() {
        // Given
        when(integrationRiskLineProperties.getUrl()).thenReturn("testUrl/v2/risk_lines");
        ResponseEntity<SantanderRiskLineResponse> response = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplateRiskLine.exchange(anyString(), any(), any(), eq(SantanderRiskLineResponse.class))).thenReturn(response);
        RiskLineQuery query = new RiskLineQuery();
        // When and Then
        assertThrows(IntegrationException.class, () -> riskLineGateway.getRiskLineById(query));
    }
}
