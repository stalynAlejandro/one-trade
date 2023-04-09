package com.pagonxt.onetradefinance.integrations.apis.riskline;

import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineResponse;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationRiskLineProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.pagonxt.onetradefinance.integrations.constants.ApiConstants.*;

/**
 * Class to link with Santander Api and get the risk lines
 * Include methods to get risk lines by user or by id
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService
 * @see org.springframework.web.client.RestTemplate
 * @see org.springframework.http.HttpHeaders
 * @see com.pagonxt.onetradefinance.integrations.configuration.IntegrationRiskLineProperties
 * @see com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils
 * @since jdk-11.0.13
 */
@Component
public class RiskLineGateway {

    private final IntegrationRiskLineProperties integrationRiskLineProperties;
    private final HttpHeaders httpHeadersRiskLine;
    private final RestTemplate restTemplateRiskLine;
    private final ApiTokenService apiTokenService;
    private final ApiParamsUtils apiParamsUtils;
    private final JWTService jwtService;

    /**
     * Class constructor
     *
     * @param integrationRiskLineProperties IntegrationAccountProperties object
     * @param httpHeadersRiskLine           HttpHeaders object
     * @param restTemplateRiskLine          RestTemplate object
     * @param apiTokenService               ApiTokenService object
     * @param apiParamsUtils                ApiParamsUtils object
     * @param jwtService                    JWTService object
     */
    public RiskLineGateway(IntegrationRiskLineProperties integrationRiskLineProperties,
                           HttpHeaders httpHeadersRiskLine,
                           RestTemplate restTemplateRiskLine,
                           ApiTokenService apiTokenService,
                           ApiParamsUtils apiParamsUtils, JWTService jwtService) {
        this.integrationRiskLineProperties = integrationRiskLineProperties;
        this.httpHeadersRiskLine = httpHeadersRiskLine;
        this.restTemplateRiskLine = restTemplateRiskLine;
        this.apiTokenService = apiTokenService;
        this.apiParamsUtils = apiParamsUtils;
        this.jwtService = jwtService;
    }

    /**
     * Class method used to obtain a list of Santander risk lines by customer
     * @param query a RiskLineQuery object
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse
     * @return SantanderRiskLineListResponse object
     */
    public SantanderRiskLineListResponse getRiskLineByCostumerId(RiskLineQuery query) {
        String jwtToken = jwtService.getToken();
        String scope = "rsklns.read";
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(integrationRiskLineProperties.getUrl())
                .queryParam(PARAM_CUSTOMER_ID, query.getCustomerId())
                .queryParam(PARAM_PRODUCT, query.getProductId())
                .queryParam(PARAM_ACTIVE_ONLY, query.isActive())
                .queryParam(PARAM_LIMIT, integrationRiskLineProperties.getLimit());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_MATURITY_DATE_OPERATION, query.getExpirationDate());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_VALUE_DATE_OPERATION, query.getValueDateOperation());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_AMOUNT_OPERATION, query.getOperationAmount());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_CURRENCY_OPERATION, query.getOperationCurrency());
        String url = urlBuilder.build().toUriString();
        httpHeadersRiskLine.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
        try {
            return extractResult(restTemplateRiskLine.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersRiskLine), SantanderRiskLineListResponse.class));
        } catch(ExpiredTokenException e) {
            apiTokenService.evictAccessToken(jwtToken, scope);
            httpHeadersRiskLine.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
            return extractResult(restTemplateRiskLine.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersRiskLine), SantanderRiskLineListResponse.class));
        }
    }

    /**
     * Class method used to obtain a Santander risk lines by id
     * @param query a RiskLineQuery object
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse
     * @return SantanderRiskLineListResponse object
     */
    public SantanderRiskLineResponse getRiskLineById(RiskLineQuery query) {
        String jwtToken = jwtService.getToken();
        String scope = "rsklns.read";
        UriComponentsBuilder urlBuilder = UriComponentsBuilder
                .fromUriString(integrationRiskLineProperties.getUrl() + "/" + query.getRiskLineId())
                .queryParam(PARAM_PRODUCT, query.getProductId());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_CUSTOMER_ID, query.getCustomerId());
        String url = urlBuilder.build().toUriString();
        httpHeadersRiskLine.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
        try {
            return extractResult(restTemplateRiskLine.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersRiskLine), SantanderRiskLineResponse.class));
        } catch(ExpiredTokenException e) {
            apiTokenService.evictAccessToken(jwtToken, scope);
            httpHeadersRiskLine.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
            return extractResult(restTemplateRiskLine.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersRiskLine), SantanderRiskLineResponse.class));
        }
    }

    private <T> T extractResult(ResponseEntity<T> response) {
        if (HttpStatus.NO_CONTENT.equals(response.getStatusCode())) {
            return null;
        }
        T result = response.getBody();
        if (result == null) {
            throw new IntegrationException();
        }
        return result;
    }
}
