package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Entity Class for company data.
 * Includes some attributes and getters and setters.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Entity
@Table(name = "aci_01cip_company_data")
public class CompanyDataDAO {
    @EmbeddedId
    private CompanyDataId companyDataId;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "customer_legal_id")
    private String customerLegalId;

    @Column(name = "emoney_customer_segment")
    private String emoneyCustomerSegment;

    @Column(name = "customer_end_date")
    private LocalDate customerEndDate;

    @OneToMany
    @JoinColumn(
        name = "company_global_id",
        referencedColumnName = "company_global_id",
        insertable = false,
        updatable = false)
    @JoinColumn(
        name = "access",
        referencedColumnName = "country_iso_1",
        insertable = false,
        updatable = false)
    private List<CompanyPersonActiveServicesDAO> companyPersonActiveServicesDAOS;

    @OneToMany
    @JoinColumn(
        name = "company_global_id",
        referencedColumnName = "company_global_id",
        insertable = false,
        updatable = false)
    @JoinColumn(
        name = "access",
        referencedColumnName = "country_iso_1",
        insertable = false,
        updatable = false)
    private List<CompanyFiscalDocumentsDAO> companyFiscalDocumentsDAOS;

    /**
     * Getter method for field companyDataId
     *
     * @return value of companyDataId
     */
    public CompanyDataId getCompanyDataId() {
        return companyDataId;
    }

    /**
     * Setter method for field companyDataId
     *
     * @param companyDataId : value of companyDataId
     */
    public void setCompanyDataId(CompanyDataId companyDataId) {
        this.companyDataId = companyDataId;
    }

    /**
     * Getter method for field companyName
     *
     * @return value of companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Setter method for field companyName
     *
     * @param companyName : value of companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Getter method for field tradeName
     *
     * @return value of tradeName
     */
    public String getTradeName() {
        return tradeName;
    }

    /**
     * Setter method for field tradeName
     *
     * @param tradeName : value of tradeName
     */
    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    /**
     * Getter method for field customerLegalId
     *
     * @return value of customerLegalId
     */
    public String getCustomerLegalId() {
        return customerLegalId;
    }

    /**
     * Setter method for field customerLegalId
     *
     * @param customerLegalId : value of customerLegalId
     */
    public void setCustomerLegalId(String customerLegalId) {
        this.customerLegalId = customerLegalId;
    }

    /**
     * Getter method for field emoneyCustomerSegment
     *
     * @return value of emoneyCustomerSegment
     */
    public String getEmoneyCustomerSegment() {
        return emoneyCustomerSegment;
    }

    /**
     * Setter method for field emoneyCustomerSegment
     *
     * @param emoneyCustomerSegment : value of emoneyCustomerSegment
     */
    public void setEmoneyCustomerSegment(String emoneyCustomerSegment) {
        this.emoneyCustomerSegment = emoneyCustomerSegment;
    }

    /**
     * Getter method for field customerEndDate
     *
     * @return value of customerEndDate
     */
    public LocalDate getCustomerEndDate() {
        return customerEndDate;
    }

    /**
     * Setter method for field customerEndDate
     *
     * @param customerEndDate : value of customerEndDate
     */
    public void setCustomerEndDate(LocalDate customerEndDate) {
        this.customerEndDate = customerEndDate;
    }

    /**
     * Getter method for field companyPersonActiveServicesDAOS
     *
     * @return value of companyPersonActiveServicesDAOS
     */
    public List<CompanyPersonActiveServicesDAO> getCompanyPersonActiveServicesDAOS() {
        return companyPersonActiveServicesDAOS;
    }

    /**
     * Setter method for field companyPersonActiveServicesDAOS
     *
     * @param companyPersonActiveServicesDAOS : value of companyPersonActiveServicesDAOS
     */
    public void setCompanyPersonActiveServicesDAOS(List<CompanyPersonActiveServicesDAO> companyPersonActiveServicesDAOS) {
        this.companyPersonActiveServicesDAOS = companyPersonActiveServicesDAOS;
    }

    /**
     * Getter method for field companyFiscalDocumentsDAOS
     *
     * @return value of companyFiscalDocumentsDAOS
     */
    public List<CompanyFiscalDocumentsDAO> getCompanyFiscalDocumentsDAOS() {
        return companyFiscalDocumentsDAOS;
    }

    /**
     * Setter method for field companyFiscalDocumentsDAOS
     *
     * @param companyFiscalDocumentsDAOS : value of companyFiscalDocumentsDAOS
     */
    public void setCompanyFiscalDocumentsDAOS(List<CompanyFiscalDocumentsDAO> companyFiscalDocumentsDAOS) {
        this.companyFiscalDocumentsDAOS = companyFiscalDocumentsDAOS;
    }
}
