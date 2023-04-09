package com.pagonxt.onetradefinance.external.backend.api.model.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto;
import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;

import java.util.List;
import java.util.Objects;

/**
 * DTO Class for export collection advance request details
 * Includes some attributes, getters and setters, equals method, hashcode method and toString method
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto
 * @see com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceRequestDetailsDto {

    //class attributes
    private ExportCollectionDto exportCollection;
    private Double advanceAmount;
    private CurrencyDto advanceCurrency;
    private List<ExchangeInsuranceDto> exchangeInsurances;
    private String exchangeInsuranceUseDate;
    private String exchangeInsuranceAmountToUse;
    private String exchangeInsuranceSellCurrency;
    private String exchangeInsuranceBuyCurrency;
    private String requestExpiration;
    private RiskLineDto riskLine;
    private String office;
    private String comments;

    /**
     * getter methdd
     * @return a ExportCollection object (request referenced)
     */
    public ExportCollectionDto getExportCollection() {
        return exportCollection;
    }

    /**
     * setter method
     * @param exportCollection a ExportCollection object (request referenced)
     */
    public void setExportCollection(ExportCollectionDto exportCollection) {
        this.exportCollection = exportCollection;
    }

    /**
     * getter method
     * @return the request amount
     */
    public Double getAdvanceAmount() {
        return advanceAmount;
    }

    /**
     * setter method
     * @param advanceAmount a string with the request amount
     */
    public void setAdvanceAmount(Double advanceAmount) {
        this.advanceAmount = advanceAmount;
    }

    /**
     * getter method
     * @return the request currency
     */
    public CurrencyDto getAdvanceCurrency() {
        return advanceCurrency;
    }

    /**
     * setter method
     * @param advanceCurrency a CurrencyDto with the request currency
     */
    public void setAdvanceCurrency(CurrencyDto advanceCurrency) {
        this.advanceCurrency = advanceCurrency;
    }

    /**
     * getter method
     * @return a list of exchange insurances
     */
    public List<ExchangeInsuranceDto> getExchangeInsurances() {
        return exchangeInsurances;
    }

    /**
     * setter method
     * @param exchangeInsurances a list of exchanges insurances
     */
    public void setExchangeInsurances(List<ExchangeInsuranceDto> exchangeInsurances) {
        this.exchangeInsurances = exchangeInsurances;
    }

    /**
     * getter method
     * @return the use date of the exchange insurance
     */
    public String getExchangeInsuranceUseDate() {
        return exchangeInsuranceUseDate;
    }

    /**
     * setter method
     * @param exchangeInsuranceUseDate a string with the use date of the exchange insurance
     */
    public void setExchangeInsuranceUseDate(String exchangeInsuranceUseDate) {
        this.exchangeInsuranceUseDate = exchangeInsuranceUseDate;
    }

    /**
     * getter method
     * @return the amount to use from the exchange insurance
     */
    public String getExchangeInsuranceAmountToUse() {
        return exchangeInsuranceAmountToUse;
    }

    /**
     * setter method
     * @param exchangeInsuranceAmountToUse a string with the amount to use from the exchange insurance
     */
    public void setExchangeInsuranceAmountToUse(String exchangeInsuranceAmountToUse) {
        this.exchangeInsuranceAmountToUse = exchangeInsuranceAmountToUse;
    }

    /**
     * getter method
     * @return the sell currency of the exchange insurance
     */
    public String getExchangeInsuranceSellCurrency() {
        return exchangeInsuranceSellCurrency;
    }

    /**
     * setter method
     * @param exchangeInsuranceSellCurrency a string with the sell currency of the exchange insurance
     */
    public void setExchangeInsuranceSellCurrency(String exchangeInsuranceSellCurrency) {
        this.exchangeInsuranceSellCurrency = exchangeInsuranceSellCurrency;
    }

    /**
     * getter method
     * @return the buy currency of the exchange insurance
     */
    public String getExchangeInsuranceBuyCurrency() {
        return exchangeInsuranceBuyCurrency;
    }

    /**
     * setter method
     * @param exchangeInsuranceBuyCurrency a string with the buy currency of the exchange insurance
     */
    public void setExchangeInsuranceBuyCurrency(String exchangeInsuranceBuyCurrency) {
        this.exchangeInsuranceBuyCurrency = exchangeInsuranceBuyCurrency;
    }

    /**
     * getter method
     * @return the request expiration date
     */
    public String getRequestExpiration() {
        return requestExpiration;
    }

    /**
     * setter method
     * @param requestExpiration a string with the request expiration date
     */
    public void setRequestExpiration(String requestExpiration) {
        this.requestExpiration = requestExpiration;
    }

    /**
     * getter method
     * @return a RiskLineDto object with the riskLine
     */
    public RiskLineDto getRiskLine() {
        return riskLine;
    }

    /**
     * setter method
     * @param riskLine a RiskLineDto object with the riskLine
     */
    public void setRiskLine(RiskLineDto riskLine) {
        this.riskLine = riskLine;
    }

    /**
     * getter method
     * @return the request office
     */
    public String getOffice() {
        return office;
    }

    /**
     * setter method
     * @param office a string with the request office
     */
    public void setOffice(String office) {
        this.office = office;
    }

    /**
     * getter method
     * @return the request comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * setter method
     * @param comments a string with the request comments
     */
    public void setComments(String comments) {
        this.comments = comments;
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
        ExportCollectionAdvanceRequestDetailsDto that = (ExportCollectionAdvanceRequestDetailsDto) o;
        return Objects.equals(exportCollection, that.exportCollection) &&
                Objects.equals(advanceAmount, that.advanceAmount) &&
                Objects.equals(advanceCurrency, that.advanceCurrency) &&
                Objects.equals(exchangeInsurances, that.exchangeInsurances) &&
                Objects.equals(exchangeInsuranceUseDate, that.exchangeInsuranceUseDate) &&
                Objects.equals(exchangeInsuranceAmountToUse, that.exchangeInsuranceAmountToUse) &&
                Objects.equals(exchangeInsuranceSellCurrency, that.exchangeInsuranceSellCurrency) &&
                Objects.equals(exchangeInsuranceBuyCurrency, that.exchangeInsuranceBuyCurrency) &&
                Objects.equals(requestExpiration, that.requestExpiration) &&
                Objects.equals(riskLine, that.riskLine) &&
                Objects.equals(office, that.office) &&
                Objects.equals(comments, that.comments);
    }

    /**
     * hashCode method
     * @return a hash value of the sequence of input values
     */
    @Override
    public int hashCode() {
        return Objects.hash(exportCollection, advanceAmount, advanceCurrency,
                exchangeInsurances, exchangeInsuranceUseDate, exchangeInsuranceAmountToUse,
                exchangeInsuranceSellCurrency, exchangeInsuranceBuyCurrency, requestExpiration,
                riskLine, office, comments);
    }

    /**
     * toString method
     * @return a String with class attributes and its values
     */
    @Override
    public String toString() {
        return "ExportCollectionAdvanceRequestDetailsDto{" +
                "exportCollection=" + exportCollection +
                ", advanceAmount='" + advanceAmount + '\'' +
                ", advanceCurrency=" + advanceCurrency +
                ", exchangeInsurances=" + exchangeInsurances +
                ", exchangeInsuranceUseDate=" + exchangeInsuranceUseDate +
                ", exchangeInsuranceAmountToUse=" + exchangeInsuranceAmountToUse +
                ", exchangeInsuranceSellCurrency='" + exchangeInsuranceSellCurrency + '\'' +
                ", exchangeInsuranceBuyCurrency='" + exchangeInsuranceBuyCurrency + '\'' +
                ", requestExpiration='" + requestExpiration + '\'' +
                ", riskLine=" + riskLine +
                ", office='" + office + '\'' +
                ", comments='" + comments + '\'' +
                '}';
    }
}
