package com.pagonxt.onetradefinance.integrations.apis.fxdeal;

import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.SantanderFxDealResponse;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationFxDealProperties;
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

import java.util.List;

import static com.pagonxt.onetradefinance.integrations.constants.ApiConstants.*;

/**
 * Class to link with Santander Api and get the fx deals
 * Include constructor and a method to search fx deals
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService
 * @see org.springframework.web.client.RestTemplate
 * @see org.springframework.http.HttpHeaders
 * @see com.pagonxt.onetradefinance.integrations.configuration.IntegrationFxDealProperties
 * @see com.pagonxt.onetradefinance.integrations.util.ApiParamsUtils
 * @since jdk-11.0.13
 */
@Component
public class FxDealGateway {

    private final IntegrationFxDealProperties integrationFxDealProperties;
    private final HttpHeaders httpHeadersFxDeal;
    private final RestTemplate restTemplateAccount;
    private final ApiTokenService apiTokenService;
    private final ApiParamsUtils apiParamsUtils;
    private final JWTService jwtService;

    /**
     * Class constructor
     *
     * @param integrationFxDealProperties IntegrationAccountProperties object
     * @param httpHeadersFxDeal           HttpHeaders object
     * @param restTemplateAccount         RestTemplate object
     * @param apiTokenService             ApiTokenService object
     * @param apiParamsUtils              ApiParamsUtils object
     * @param jwtService                  JWTService object
     */
    public FxDealGateway(IntegrationFxDealProperties integrationFxDealProperties,
                         HttpHeaders httpHeadersFxDeal,
                         RestTemplate restTemplateAccount,
                         ApiTokenService apiTokenService,
                         ApiParamsUtils apiParamsUtils, JWTService jwtService) {
        this.integrationFxDealProperties = integrationFxDealProperties;
        this.httpHeadersFxDeal = httpHeadersFxDeal;
        this.restTemplateAccount = restTemplateAccount;
        this.apiTokenService = apiTokenService;
        this.apiParamsUtils = apiParamsUtils;
        this.jwtService = jwtService;
    }

    /**
     * Class method to search fx deals
     * @param query FxDealQuery object
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery
     * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.SantanderFxDealResponse
     * @return a SantanderFxDealResponse object
     */
    public SantanderFxDealResponse searchFxDeals(FxDealQuery query) {
        String jwtToken = jwtService.getToken();
        String scope = "fxdeals.list";
        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(integrationFxDealProperties.getUrl());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_BANK_ID, query.getBankId());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_BRANCH_ID, query.getBranchId());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_DATE_TYPE, VALUE_DATE_TYPE_USE);
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_FROM_DATE, query.getFromDate());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_TO_DATE, query.getToDate());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_SELL_CURRENCY, query.getSellCurrency());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_BUY_CURRENCY, query.getBuyCurrency());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_DIRECTION, query.getDirection());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_BALANCE_TYPE, VALUE_BALANCE_TYPE_AVAULABLE);
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_BALANCE_AMOUNT, query.getBalanceFxDealAmount());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_BALANCE_CURRENCY, query.getBalanceFxDealCurrency());
        apiParamsUtils.injectQueryParam(urlBuilder, PARAM_LIMIT, integrationFxDealProperties.getLimit());
        String url = urlBuilder.build().toUriString();
        if (query.getCustomerId() != null) {
            httpHeadersFxDeal.put(HEADER_CUSTOMER_ID, List.of(query.getCustomerId()));
        }
        httpHeadersFxDeal.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
        try {
            return extractResult(restTemplateAccount
                    .exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeadersFxDeal), SantanderFxDealResponse.class));
        } catch(ExpiredTokenException e) {
            apiTokenService.evictAccessToken(jwtToken, scope);
            httpHeadersFxDeal.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
            return extractResult(restTemplateAccount.exchange(url,
                HttpMethod.GET, new HttpEntity<>(httpHeadersFxDeal), SantanderFxDealResponse.class));
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
