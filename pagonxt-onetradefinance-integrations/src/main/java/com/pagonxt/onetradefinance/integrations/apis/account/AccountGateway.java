package com.pagonxt.onetradefinance.integrations.apis.account;

import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccount;
import com.pagonxt.onetradefinance.integrations.apis.account.model.SantanderAccountListResponse;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService;
import com.pagonxt.onetradefinance.integrations.apis.common.security.service.JWTService;
import com.pagonxt.onetradefinance.integrations.configuration.IntegrationAccountProperties;
import com.pagonxt.onetradefinance.integrations.model.exception.ExpiredTokenException;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
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
 * Class to link with Santander Api and get the accounts
 * Include methods to get accounts by user or by id
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.apis.common.security.service.ApiTokenService
 * @see org.springframework.web.client.RestTemplate
 * @see org.springframework.http.HttpHeaders
 * @see com.pagonxt.onetradefinance.integrations.configuration.IntegrationAccountProperties
 * @since jdk-11.0.13
 */
@Component
public class AccountGateway {

    private final IntegrationAccountProperties integrationAccountProperties;
    private final HttpHeaders httpHeadersAccount;
    private final RestTemplate restTemplateAccount;
    private final ApiTokenService apiTokenService;
    private final JWTService jwtService;

    /**
     * Class constructor
     *
     * @param integrationAccountProperties IntegrationAccountProperties object
     * @param httpHeadersAccount           HttpHeaders object
     * @param restTemplateAccount          RestTemplate object
     * @param apiTokenService              ApiTokenService object
     * @param jwtService                   JWTService object
     */
    public AccountGateway(IntegrationAccountProperties integrationAccountProperties,
                          HttpHeaders httpHeadersAccount,
                          RestTemplate restTemplateAccount,
                          ApiTokenService apiTokenService, JWTService jwtService) {
        this.integrationAccountProperties = integrationAccountProperties;
        this.httpHeadersAccount = httpHeadersAccount;
        this.restTemplateAccount = restTemplateAccount;
        this.apiTokenService = apiTokenService;
        this.jwtService = jwtService;
    }

    /**
     * Class method used to obtain a list of Santander accounts by customer
     * @param customerId a string with the customer id
     * @return a list of Santander accounts
     */
    public SantanderAccountListResponse getCustomerAccounts(String customerId) {
        String jwtToken = jwtService.getToken();
        String scope = "acclist.read acclistcid.read";
        String url = UriComponentsBuilder.fromUriString(integrationAccountProperties.getUrl())
                        .queryParam(PARAM_CUSTOMER_ID, customerId)
                        .queryParam(PARAM_ACCOUNT_ID_TYPE, PARAM_VALUE_IBA)
                        .queryParam(PARAM_STATUS, PARAM_VALUE_OPEN)
                        .queryParam(PARAM_LIMIT, integrationAccountProperties.getLimit())
                        .build().toUriString();
        httpHeadersAccount.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
        try {
            return extractResult(restTemplateAccount.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersAccount), SantanderAccountListResponse.class));
        } catch(ExpiredTokenException e) {
            apiTokenService.evictAccessToken(jwtToken, scope);
            httpHeadersAccount.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
            return extractResult(restTemplateAccount.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersAccount), SantanderAccountListResponse.class));
        }
    }

    /**
     * Class method used to obtain a Santander account by id (account id)
     * @param accountId a string with the account id
     * @return a Santander account
     */
    public SantanderAccount getAccountById(String accountId) {
        String jwtToken = jwtService.getToken();
        String scope = "accdet.read accdetcid.read";
        String url = integrationAccountProperties.getUrl() + "/" + accountId;
        httpHeadersAccount.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
        try {
            return extractResult(restTemplateAccount.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersAccount), SantanderAccount.class));
        } catch(ExpiredTokenException e) {
            apiTokenService.evictAccessToken(jwtToken, scope);
            httpHeadersAccount.setBearerAuth(apiTokenService.getAccessToken(jwtToken, scope));
            return extractResult(restTemplateAccount.exchange(url,
                    HttpMethod.GET, new HttpEntity<>(httpHeadersAccount), SantanderAccount.class));
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
