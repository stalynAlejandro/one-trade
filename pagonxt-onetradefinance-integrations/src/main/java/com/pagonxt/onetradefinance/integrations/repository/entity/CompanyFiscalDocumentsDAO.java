package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.*;

/**
 * Entity Class for company documents.
 * Includes some attributes and getters and setters.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Entity
@Table(name = "aci_01cip_company_fiscal_documents")
public class CompanyFiscalDocumentsDAO {

    @EmbeddedId
    private CompanyFiscalDocumentsId companyFiscalDocumentsId;

    @ManyToOne
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
    private CompanyDataDAO companyDataDAO;

    /**
     * Getter method for field companyFiscalDocumentsId
     *
     * @return value of companyFiscalDocumentsId
     */
    public CompanyFiscalDocumentsId getCompanyFiscalDocumentsId() {
        return companyFiscalDocumentsId;
    }

    /**
     * Setter method for field companyFiscalDocumentsId
     *
     * @param companyFiscalDocumentsId : value of companyFiscalDocumentsId
     */
    public void setCompanyFiscalDocumentsId(CompanyFiscalDocumentsId companyFiscalDocumentsId) {
        this.companyFiscalDocumentsId = companyFiscalDocumentsId;
    }

    /**
     * Getter method for field companyDataDAO
     *
     * @return value of companyDataDAO
     */
    public CompanyDataDAO getCompanyDataDAO() {
        return companyDataDAO;
    }

    /**
     * Setter method for field companyDataDAO
     *
     * @param companyDataDAO : value of companyDataDAO
     */
    public void setCompanyDataDAO(CompanyDataDAO companyDataDAO) {
        this.companyDataDAO = companyDataDAO;
    }
}
