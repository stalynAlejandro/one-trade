package com.pagonxt.onetradefinance.external.backend.utils;

import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.CollectionLoanProduct;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

@Component
public class QueryUtils {

    private final DateFormat dateFormat;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final Double DEFAULT_AMOUNT = 0.00d;
    private static final String DEFAULT_CURRENCY = "EUR";

    public QueryUtils(DateFormatProperties dateFormatProperties) {
        dateFormat = dateFormatProperties.getDateFormatInstance(DATE_FORMAT);
    }

    public FxDealQuery generateFxDealQuery(String customerId, String currencyBuy, String currencySell, boolean buy, Double amountAdvance) {
        FxDealQuery fxDealQuery = new FxDealQuery();
        fxDealQuery.setCustomerId(customerId);
        fxDealQuery.setBuyCurrency(currencyBuy);
        fxDealQuery.setSellCurrency(currencySell);
        String direction = buy ? "BUY" : "SELL";
        fxDealQuery.setDirection(direction);
        fxDealQuery.setBalanceFxDealAmount(amountAdvance);
        String currencyAmount = buy ? currencyBuy : currencySell;
        fxDealQuery.setBalanceFxDealCurrency(currencyAmount);
        // TODO: Get bank_id from the country of the requester, Spain by default
        fxDealQuery.setBankId("0049");
        // Current date by default
        Date currentDate = new Date();
        fxDealQuery.setFromDate(currentDate);
        fxDealQuery.setToDate(currentDate);
        return fxDealQuery;
    }

    public RiskLineQuery generateRiskLineQuery(String client, String productId, String expirationDate, Double operationAmount, String operationCurrency) {
        RiskLineQuery riskLineQuery = new RiskLineQuery();
        riskLineQuery.setCustomerId(client);
        riskLineQuery.setProductId(CollectionLoanProduct.getProductCode(productId));
        String defaultDate = dateFormat.format(new Date());
        riskLineQuery.setValueDateOperation(defaultDate);
        riskLineQuery.setExpirationDate(Objects.requireNonNullElse(expirationDate, defaultDate));
        riskLineQuery.setOperationAmount(Objects.requireNonNullElse(operationAmount, DEFAULT_AMOUNT));
        riskLineQuery.setOperationCurrency(Objects.requireNonNullElse(operationCurrency, DEFAULT_CURRENCY));
        riskLineQuery.setActive(true);
        return riskLineQuery;
    }
}
