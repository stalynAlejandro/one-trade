package com.pagonxt.onetradefinance.external.backend.service.model;

import java.util.Map;

/**
 * Model class that collects the price card data for a remittance import
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData
 * @since jdk-11.0.13
 */
public class ImportCollectionPricesChartData extends PricesChartData {

    /**
     * class attributes
     */
    private String beneficiary;
    private String beneficiaryBank;

    /**
     * Method to get some report parameters of prices chart data and insert the class attributes
     * @return a Map object with the prices chart data, including de beneficiary and the beneficiary bank
     */
    @Override
    public Map<String, Object> getReportParameters() {
        Map<String, Object> result = super.getReportParameters();
        result.put("BENEFICIARY", beneficiary);
        result.put("BENEFICIARY_BANK", beneficiaryBank);
        return result;
    }

    /**
     * getter method
     * @return the beneficiary
     */
    public String getBeneficiary() {
        return beneficiary;
    }

    /**
     * setter method
     * @param beneficiary a string with the beneficiary
     */
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    /**
     * getter method
     * @return the beneficiary bank
     */
    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }

    /**
     * setter method
     * @param beneficiaryBank a string with the beneficiary bank
     */
    public void setBeneficiaryBank(String beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }
}
