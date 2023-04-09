package com.pagonxt.onetradefinance.external.backend.service.model;

import java.util.Map;

/**
 * Model class that collects the price card data for a remittance export
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.model.PricesChartData
 * @since jdk-11.0.13
 */
public class ExportCollectionPricesChartData extends PricesChartData {

    /**
     * Class attributes
     */
    private String debtor;
    private String debtorBank;

    /**
     * Method to get some report parameters of prices chart data and insert the class attributes
     * @return a Map object with the prices chart data, including de debtor and the debtor bank
     */
    @Override
    public Map<String, Object> getReportParameters() {
        Map<String, Object> result = super.getReportParameters();
        result.put("DEBTOR", debtor);
        result.put("DEBTOR_BANK", debtorBank);
        return result;
    }

    /**
     * getter method
     * @return the debtor
     */
    public String getDebtor() {
        return debtor;
    }

    /**
     * setter method
     * @param debtor a string with the debtor
     */
    public void setDebtor(String debtor) {
        this.debtor = debtor;
    }

    /**
     * getter method
     * @return the debtor bank
     */
    public String getDebtorBank() {
        return debtorBank;
    }

    /**
     * setter method
     * @param debtorBank a string with the debtor bank
     */
    public void setDebtorBank(String debtorBank) {
        this.debtorBank = debtorBank;
    }
}
