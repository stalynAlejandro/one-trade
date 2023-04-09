package com.pagonxt.onetradefinance.integrations.apis.specialprices;

import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SantanderTradeSpecialPricesResponse;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationTradeSpecialPricesProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery;
import com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static com.pagonxt.onetradefinance.integrations.constants.ApiConstants.*;

/**
 * Class to link with Santander Api and get the trade special prices
 * Include a method to get special prices
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService
 * @see org.springframework.web.client.RestTemplate
 * @see org.springframework.http.HttpHeaders
 * @see com.pagonxt.onetradefinance.integrations.configuration.IntegrationTradeSpecialPricesProperties
 * @see com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils
 * @since jdk-11.0.13
 */
@Component
public class TradeSpecialPricesGateway {

    private final IntegrationTradeSpecialPricesProperties integrationTradeSpecialPricesProperties;
    private final HttpHeaders httpHeaderTradeSpecialPrices;
    private final RestTemplate restTemplateTradeSpecialPrices;
    private final ApiTokenService apiTokenService;
    private final ApiParamsUtils apiParamsUtils;
    private final JWTService jwtService;

    /**
     * Class constructor
     *
     * @param integrationTradeSpecialPricesProperties IntegrationAccountProperties object
     * @param httpHeaderTradeSpecialPrices            HttpHeaders object
     * @param restTemplateTradeSpecialPrices          RestTemplate object
     * @param apiTokenService                         ApiTokenService object
     * @param apiParamsUtils                          ApiParamsUtils object
     * @param jwtService                              JWRService object
     */
    public TradeSpecialPricesGateway(IntegrationTradeSpecialPricesProperties integrationTradeSpecialPricesProperties,
                                     HttpHeaders httpHeaderTradeSpecialPrices,
                                     RestTemplate restTemplateTradeSpecialPrices,
                                     ApiTokenService apiTokenService,
                                     ApiParamsUtils apiParamsUtils, JWTService jwtService) {
        this.integrationTradeSpecialPricesProperties = integrationTradeSpecialPricesProperties;
        this.httpHeaderTradeSpecialPrices = httpHeaderTradeSpecialPrices;
        this.restTemplateTradeSpecialPrices = restTemplateTradeSpecialPrices;
        this.apiTokenService = apiTokenService;
        this.apiParamsUtils = apiParamsUtils;
        this.jwtService = jwtService;
    }

    /**
     * Class method to get special prices data
     * @param query TradeSpecialPricesQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPricesQuery
     * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.model.SantanderTradeSpecialPricesResponse
     * @return a SantanderTradeSpecialPricesResponse object
     */
    public SantanderTradeSpecialPricesResponse getSpecialPrices(TradeSpecialPricesQuery query) {
        String jwtToken = jwtService.getToken();
        String scope = "tradespecialprice.read";
        UriComponentsBuilder urlBuilder = UriComponentsBuilder
                .fromUriString(integrationTradeSpecialPricesProperties.getUrl())
                .queryParam(PARAM_PRODUCT_ID, query.getProductId())
                .queryParam(PARAM_QUERY_DATE, query.getQueryDate());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_CUSTOMER_ID, query.getCustomerId());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_COUNTRY, query.getCountry());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_BRANCH_ID, query.getBranchId());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_CURRENCY_CODE, query.getCurrencyCode());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_AMOUNT, query.getAmount());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_TERM, query.getTerm());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_TERM_TYPE, query.getTermType());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_LIMIT, integrationTradeSpecialPricesProperties.getLimit());
        String url = urlBuilder.build().toUriString();
        httpHeaderTradeSpecialPrices.put(HEADER_ACCESS_CHANNEL, List.of("web"));
        httpHeaderTradeSpecialPrices.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
        try {
            return extractResult(restTemplateTradeSpecialPrices.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(httpHeaderTradeSpecialPrices), SantanderTradeSpecialPricesResponse.class));
        } catch(ExpiredTokenException e) {
            apiTokenService.evictAccessToken(jwtToken, scope);
            httpHeaderTradeSpecialPrices.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
            return extractResult(restTemplateTradeSpecialPrices.exchange(url, HttpMethod.GET,
                    new HttpEntity<>(httpHeaderTradeSpecialPrices), SantanderTradeSpecialPricesResponse.class));
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
