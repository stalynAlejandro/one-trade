package com.pagonxt.onetradefinance.external.backend.service.model;

import com.pagonxt.onetradefinance.integrations.model.special_prices.TradeSpecialPrices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class that collects the generic data of a price card
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData
 * @since jdk-11.0.13
 */
public abstract class PricesChartData {

    /**
     * class attributes
     */
    private String officeId;

    private String officeAddress;

    private String officeAddressCountry;

    private String customerName;

    private String customerAddress;

    private String customerAddressLocality;

    private String customerAddressProvince;

    private String customerAddressCountry;

    private String ourReference;

    private String clientReference;

    private String amount;

    private String currencyCode;

    private List<CommissionGroup> commissions = new ArrayList<>();

    /**
     * Method to get the data of import or export collection request and insert
     * them into the object with the prices chart data
     * @return a Map object with the prices chart data
     */
    public Map<String, Object> getReportParameters() {
        Map<String, Object> result = new HashMap<>();
        result.put("OFFICE_ID", officeId);
        result.put("OFFICE_ADDRESS", officeAddress);
        result.put("OFFICE_ADDRESS_COUNTRY", officeAddressCountry);
        result.put("CUSTOMER_NAME", customerName);
        result.put("CUSTOMER_ADDRESS", customerAddress);
        result.put("CUSTOMER_ADDRESS_LOCALITY", customerAddressLocality);
        result.put("CUSTOMER_ADDRESS_PROVINCE", customerAddressProvince);
        result.put("CUSTOMER_ADDRESS_COUNTRY", customerAddressCountry);
        result.put("OUR_REFERENCE", ourReference);
        result.put("CLIENT_REFERENCE", clientReference);
        result.put("AMOUNT", amount);
        result.put("CURRENCY_CODE", currencyCode);
        return result;
    }

    /**
     * This method returns a list of commissions
     * @return a list of commissions
     */
    public List<Commission> getDatasourceCollection() {
        List<Commission> result = new ArrayList<>();
        boolean odd = false;
        for (CommissionGroup group : getCommissions()) {
            for (Commission commission : group) {
                commission.setOdd(odd);
                result.add(commission);
            }
            odd = !odd;
        }
        return result;
    }

    /**
     * getter method
     * @return the office id
     */
    public String getOfficeId() {
        return officeId;
    }

    /**
     * setter method
     * @param officeId a string with the office id
     */
    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    /**
     * getter method
     * @return the office address
     */
    public String getOfficeAddress() {
        return officeAddress;
    }

    /**
     * setter method
     * @param officeAddress a string with the office address
     */
    public void setOfficeAddress(String officeAddress) {
        this.officeAddress = officeAddress;
    }

    /**
     * getter method
     * @return the country of office address
     */
    public String getOfficeAddressCountry() {
        return officeAddressCountry;
    }

    /**
     * setter method
     * @param officeAddressCountry a string with the country of office address
     */
    public void setOfficeAddressCountry(String officeAddressCountry) {
        this.officeAddressCountry = officeAddressCountry;
    }

    /**
     * getter method
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * setter method
     * @param customerName a string with the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * getter method
     * @return the customer address
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * setter method
     * @param customerAddress a string with the customer address
     */
    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * getter method
     * @return the locality of the customer address
     */
    public String getCustomerAddressLocality() {
        return customerAddressLocality;
    }

    /**
     * setter method
     * @param customerAddressLocality a string with the locality of the customer address
     */
    public void setCustomerAddressLocality(String customerAddressLocality) {
        this.customerAddressLocality = customerAddressLocality;
    }

    /**
     * getter method
     * @return the province of the customer address
     */
    public String getCustomerAddressProvince() {
        return customerAddressProvince;
    }

    /**
     * setter method
     * @param customerAddressProvince a string with the province of the customer address
     */
    public void setCustomerAddressProvince(String customerAddressProvince) {
        this.customerAddressProvince = customerAddressProvince;
    }

    /**
     * getter method
     * @return the country of the customer address
     */
    public String getCustomerAddressCountry() {
        return customerAddressCountry;
    }

    /**
     * setter method
     * @param customerAddressCountry a string with the country of the customer address
     */
    public void setCustomerAddressCountry(String customerAddressCountry) {
        this.customerAddressCountry = customerAddressCountry;
    }

    /**
     * getter method
     * @return our reference
     */
    public String getOurReference() {
        return ourReference;
    }

    /**
     * setter method
     * @param ourReference a string with our reference
     */
    public void setOurReference(String ourReference) {
        this.ourReference = ourReference;
    }

    /**
     * getter method
     * @return the client reference
     */
    public String getClientReference() {
        return clientReference;
    }

    /**
     * setter method
     * @param clientReference a string with the client reference
     */
    public void setClientReference(String clientReference) {
        this.clientReference = clientReference;
    }

    /**
     * getter method
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * setter method
     * @param amount a string with the amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * getter method
     * @return the currency code
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
     * @return a list of Commissions
     */
    public List<CommissionGroup> getCommissions() {
        return commissions;
    }

    /**
     * setter method
     * @param commissions a list of commissions
     */
    public void setCommissions(List<CommissionGroup> commissions) {
        this.commissions = commissions;
    }

    /**
     * Class for create an object with a group of commissions
     * @author -
     * @version jdk-11.0.13
     * @since jdk-11.0.13
     */
    public static class CommissionGroup extends ArrayList<Commission> {

        /**
         * Class constructor
         * @param commissions list of commissions
         */
        public CommissionGroup(List<Commission> commissions) {
            super(commissions);
        }
    }

    /**
     * Model class for a commission
     * @author -
     * @version jdk-11.0.13
     * @since jdk-11.0.13
     */
    public static class Commission {

        /**
         * Class attributes
         */
        private String concept;

        private String periodicity;

        private String percentage;

        private String value;

        private boolean odd;

        /**
         * Empty class constructor
         */
        public Commission() {
        }

        /**
         * Class constructor
         * @param tradeSpecialPrices a TradeSpecialPrizes object
         */
        public Commission(TradeSpecialPrices tradeSpecialPrices) {
            this.concept = tradeSpecialPrices.getConceptName();
            if(tradeSpecialPrices.getPeriodicity() != null) {
                this.periodicity = tradeSpecialPrices.getPeriodicity();
            }
            if(tradeSpecialPrices.getPercentage() != null) {
                this.percentage = tradeSpecialPrices.getPercentage();
            }
            this.value = tradeSpecialPrices.getAmount();
        }

        /**
         * getter method
         * @return commission concept
         */
        public String getConcept() {
            return concept;
        }

        /**
         * setter method
         * @param concept a string with the commission concept
         */
        public void setConcept(String concept) {
            this.concept = concept;
        }

        /**
         * getter method
         * @return the commission periodicity
         */
        public String getPeriodicity() {
            return periodicity;
        }

        /**
         * setter method
         * @param periodicity a string with the commission periodicity
         */
        public void setPeriodicity(String periodicity) {
            this.periodicity = periodicity;
        }

        /**
         * getter method
         * @return the commission percentage
         */
        public String getPercentage() {
            return percentage;
        }

        /**
         * setter method
         * @param percentage a string with the commission percentage
         */
        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }

        /**
         * getter method
         * @return the commission value
         */
        public String getValue() {
            return value;
        }

        /**
         * setter
         * @param value a string with the commission value
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * getter method
         * @return true or false, if a commission is odd
         */
        public boolean isOdd() {
            return odd;
        }

        /**
         * setter method
         * @param odd a boolean value, if the commission id odd
         */
        public void setOdd(boolean odd) {
            this.odd = odd;
        }
    }
}
