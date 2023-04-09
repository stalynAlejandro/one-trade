package com.pagonxt.onetradefinance.integrations.constants;

/**
 * generic class
 * This class stores all constants used in classes of the next packages:
 * com.pagonxt.onetradefinance.integrations.apis.account
 * com.pagonxt.onetradefinance.integrations.apis.common
 * com.pagonxt.onetradefinance.integrations.apis.fxdeal
 * com.pagonxt.onetradefinance.integrations.apis.riskline
 * com.pagonxt.onetradefinance.integrations.apis.specialprices
 * com.pagonxt.onetradefinance.integrations.configuration
 * Including subpackages like model, serializer...
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class ApiConstants {

    private ApiConstants(){}
    public static final String X_SANTANDER_CLIENT_ID = "X-Santander-Client-Id";
    public static final String PARAM_CUSTOMER_ID = "customer_id";
    public static final String PARAM_LIMIT = "_limit";
    public static final String HEADER_CUSTOMER_ID = "Customer-Id";
    public static final String PARAM_ACCOUNT_ID_TYPE = "account_id_type";
    public static final String PARAM_STATUS = "status";
    public static final String PARAM_VALUE_IBA = "IBA";
    public static final String PARAM_VALUE_OPEN = "Open";
    public static final String PARAM_BANK_ID = "bank_id";
    public static final String PARAM_BRANCH_ID = "branch_id";
    public static final String PARAM_FROM_DATE = "from_date";
    public static final String PARAM_TO_DATE = "to_date";
    public static final String PARAM_BALANCE_AMOUNT = "balance_fxdeal_amount";
    public static final String PARAM_BALANCE_CURRENCY = "balance_fxdeal_currency";
    public static final String PARAM_SELL_CURRENCY = "sell_currency";
    public static final String PARAM_BUY_CURRENCY = "buy_currency";
    public static final String PARAM_DIRECTION = "direction";
    public static final String PARAM_BALANCE_TYPE = "balance_fxdeal_type";
    public static final String VALUE_BALANCE_TYPE_AVAULABLE = "A";

    public static final String PARAM_DATE_TYPE = "date_type";
    public static final String VALUE_DATE_TYPE_USE = "USE";
    public static final String PARAM_COUNTRY = "country";
    public static final String PARAM_CURRENCY_CODE = "currency_code";
    public static final String PARAM_PRODUCT_ID = "product_id";
    public static final String PARAM_AMOUNT = "amount";
    public static final String PARAM_QUERY_DATE = "query_date";
    public static final String KEEP_ALIVE = "keep-alive";
    public static final String HEADER_ACCESS_CHANNEL = "Access-Channel";
    public static final String PARAM_TERM = "term";
    public static final String PARAM_TERM_TYPE = "term_type";
    public static final String PARAM_ACTIVE_ONLY = "active_only";
    public static final String PARAM_MATURITY_DATE_OPERATION = "maturity_date_operation";
    public static final String PARAM_VALUE_DATE_OPERATION = "value_date_operation";
    public static final String PARAM_AMOUNT_OPERATION = "amount_operation";
    public static final String PARAM_CURRENCY_OPERATION = "currency_operation";
    public static final String PARAM_PRODUCT = "product";
}
