package com.pagonxt.onetradefinance.integrations.model.special_prices;


import java.util.Objects;

/**
 * Model class to get info for queries about trade special prices
 * Include class attributes, getters and setters
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class TradeSpecialPricesQuery {

    /**
     * Agreement ID for the commercial agreement with the customer,
     * for example, a contract, an account, an operation, or a remittance
     */
    private String agreementId;

    /**
     * Product ID defined in the local bank for the trade whose prices
     * are to be included in the response.
     */
    private String productId;

    /**
     * Customer ID that uniquely identifies the customer in the local bank.
     * This parameter is mandatory if the 'Document-Type' and 'Document-Number'
     * parameters are omitted. If all 3 parameters are provided,
     * 'customer_id' has higher priority.
     */
    private String customerId;

    /**
     * Country in which the customer's local bank branch is located.
     * The value is based on the ISO 3166-1 alpha-2 (https://www.iso.org/obp/ui/#search/code/).
     */
    private String country;

    /**
     * Branch office that manages the customer account.
     * The format and length of the value is country-dependent.
     * For example:
     * - In Spain, the format is: Banco(4) + Sucursal(4) + CD(2)
     * - In the UK, it is: Sort code(6)
     * - In Germany, it is: BLZ code(8)
     * - In Portugal, it is: Banco(4) + Filial(4)
     */
    private String branchId;

    /**
     * Format: 'yyyyMMdd'
     * - yyyy: 4-digit year
     * - MM: 2-digit month (for example, 01 = January)
     * - dd: 2-digit day of the month (01 through 31)
     */
    private String queryDate;

    /**
     * Currency code.
     * The value is in the alpha-3 format defined in ISO 4217
     * (https://www.iso.org/iso-4217-currency-codes.html)
     */
    private String currencyCode;

    /**
     * 'Settlement concept (fee associated with the product) ID.
     * You can use this parameter to retrieve a specific settlement concept.
     */
    private String conceptId;

    /**
     * Trade amount for which prices are to be included in the response.
     * The value uses the data format defined in ISO 20022 and has a
     * maximum of 18 digits, of which 5 can be decimals, separated by a point.
     * If this parameter is provided, the 'currency_code' parameter is mandatory.
     */
    private Double amount;

    /**
     * Term (time period) for which prices are to be included in the response.
     * The value is a freely defined number.
     */
    private Double term;

    /**
     * Type of the term for which the 'term' parameter specifies the length.
     * The possible values are:
     * - days
     * - months
     * - years
     */
    private String termType;

    /**
     * getter method
     * @return a string with the agreement id
     */
    public String getAgreementId() {
        return agreementId;
    }

    /**
     * setter method
     * @param agreementId a string with the agreement id
     */
    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    /**
     * getter method
     * @return a string with the product id
     */
    public String getProductId() {
        return productId;
    }

    /**
     * setter method
     * @param productId a string with the product id
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * getter method
     * @return a string with the customer id
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * setter method
     * @param customerId a string with the customer id
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * getter method
     * @return a string with the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter method
     * @param country a string with the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getter method
     * @return a string with the branch id
     */
    public String getBranchId() {
        return branchId;
    }

    /**
     * setter method
     * @param branchId a string with the branch id
     */
    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    /**
     * getter method
     * @return a string with the query date
     */
    public String getQueryDate() {
        return queryDate;
    }

    /**
     * setter method
     * @param queryDate a string with the query date
     */
    public void setQueryDate(String queryDate) {
        this.queryDate = queryDate;
    }

    /**
     * getter method
     * @return a string with the currency code
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * setter method
     * @param currencyCode a string with the currency code
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * getter method
     * @return a string with the concept id
     */
    public String getConceptId() {
        return conceptId;
    }

    /**
     * setter method
     * @param conceptId a string with the concept id
     */
    public void setConceptId(String conceptId) {
        this.conceptId = conceptId;
    }

    /**
     * getter method
     * @return a double value with the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * setter method
     * @param amount a double value with the amount
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * getter method
     * @return a double value with the term
     */
    public Double getTerm() {
        return term;
    }

    /**
     * setter method
     * @param term a double value with the term
     */
    public void setTerm(Double term) {
        this.term = term;
    }

    /**
     * getter method
     * @return a string with the term type
     */
    public String getTermType() {
        return termType;
    }

    /**
     * setter method
     * @param termType a string with the term type
     */
    public void setTermType(String termType) {
        this.termType = termType;
    }

    /**
     * Equals method
     * @param o an object
     * @return true if the objects are equals, or not if they aren't equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TradeSpecialPricesQuery that = (TradeSpecialPricesQuery) o;
        return Objects.equals(agreementId, that.agreementId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(customerId, that.customerId) &&
                Objects.equals(country, that.country) &&
                Objects.equals(branchId, that.branchId) &&
                Objects.equals(queryDate, that.queryDate) &&
                Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(conceptId, that.conceptId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(term, that.term) &&
                Objects.equals(termType, that.termType);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(agreementId, productId, customerId, country, branchId, queryDate,
                currencyCode, conceptId, amount, term, termType);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "TradeSpecialPricesQuery{" +
                "agreementId='" + agreementId + '\'' +
                ", productId='" + productId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", country='" + country + '\'' +
                ", branchId='" + branchId + '\'' +
                ", queryDate='" + queryDate + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", conceptId='" + conceptId + '\'' +
                ", amount=" + amount +
                ", term=" + term +
                ", termType='" + termType + '\'' +
                '}';
    }

    /**
     * Class to build queries about trade special prices
     * Include class attributes,constructor, getters and setters
     * @author -
     * @version jdk-11.0.13
     * @see com.pagonxt.onetradefinance.integrations.model.User
     * @since jdk-11.0.13
     */
    public static final class TradeSpecialPricesQueryBuilder {
        private String agreementId;
        private String productId;
        private String customerId;
        private String country;
        private String branchId;
        private String queryDate;
        private String currencyCode;
        private String conceptId;
        private Double amount;
        private Double term;
        private String termType;

        private TradeSpecialPricesQueryBuilder() {
        }

        /**
         * class method
         * @return an empty TradeSpecialPricesQueryBuilder object
         */
        public static TradeSpecialPricesQueryBuilder tradeSpecialPricesQuery() {
            return new TradeSpecialPricesQueryBuilder();
        }

        /**
         * class method. Inserts the agreement id into TradeSpecialPricesQueryBuilder object
         * @param agreementId a string with the agreementId
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder agreementId(String agreementId) {
            this.agreementId = agreementId;
            return this;
        }

        /**
         * class method. Inserts the product id into TradeSpecialPricesQueryBuilder object
         * @param productId a string with the product id
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder productId(String productId) {
            this.productId = productId;
            return this;
        }

        /**
         * class method. Inserts the customer id into TradeSpecialPricesQueryBuilder object
         * @param customerId a string with the customer Id
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        /**
         * class method. Inserts the country into TradeSpecialPricesQueryBuilder object
         * @param country a string with the country
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder country(String country) {
            this.country = country;
            return this;
        }

        /**
         * class method. Inserts the branch id into TradeSpecialPricesQueryBuilder object
         * @param branchId a string with the branch Id
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder branchId(String branchId) {
            this.branchId = branchId;
            return this;
        }

        /**
         * class method. Inserts the query date into TradeSpecialPricesQueryBuilder object
         * @param queryDate a string with the query date
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder queryDate(String queryDate) {
            this.queryDate = queryDate;
            return this;
        }

        /**
         * class method. Inserts the currency code into TradeSpecialPricesQueryBuilder object
         * @param currencyCode a string with the currency code
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        /**
         * class method. Inserts the concept id into TradeSpecialPricesQueryBuilder object
         * @param conceptId a string with the concept id
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder conceptId(String conceptId) {
            this.conceptId = conceptId;
            return this;
        }

        /**
         * class method. Inserts the amount into TradeSpecialPricesQueryBuilder object
         * @param amount a double value with the amount
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder amount(Double amount) {
            this.amount = amount;
            return this;
        }

        /**
         * class method. Inserts the term into TradeSpecialPricesQueryBuilder object
         * @param term a double value with the term
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder term(Double term) {
            this.term = term;
            return this;
        }

        /**
         * class method. Inserts the term type into TradeSpecialPricesQueryBuilder object
         * @param termType a string with the term type
         * @return TradeSpecialPricesQueryBuilder object
         */
        public TradeSpecialPricesQueryBuilder termType(String termType) {
            this.termType = termType;
            return this;
        }

        /**
         * method to build a TradeSpecialPricesQuery object
         * @return a TradeSpecialPricesQuery object
         */
        public TradeSpecialPricesQuery build() {
            TradeSpecialPricesQuery tradeSpecialPricesQuery = new TradeSpecialPricesQuery();
            tradeSpecialPricesQuery.setAgreementId(agreementId);
            tradeSpecialPricesQuery.setProductId(productId);
            tradeSpecialPricesQuery.setCustomerId(customerId);
            tradeSpecialPricesQuery.setCountry(country);
            tradeSpecialPricesQuery.setBranchId(branchId);
            tradeSpecialPricesQuery.setQueryDate(queryDate);
            tradeSpecialPricesQuery.setCurrencyCode(currencyCode);
            tradeSpecialPricesQuery.setConceptId(conceptId);
            tradeSpecialPricesQuery.setAmount(amount);
            tradeSpecialPricesQuery.setTerm(term);
            tradeSpecialPricesQuery.setTermType(termType);
            return tradeSpecialPricesQuery;
        }
    }
}
