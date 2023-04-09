package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.*;

/**
 * Entity Class for company person active services.
 * Includes some attributes and getters and setters.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Entity
@Table(name = "aci_03ccr_company_person_active_services")
public class CompanyPersonActiveServicesDAO {
    @EmbeddedId
    private CompanyPersonActiveServicesId companyPersonActiveServicesId;

    @Column(name = "segment_type_local")
    private String segmentTypeLocal;

    @Column(name = "segment_local")
    private String segmentLocal;

    @Column(name = "mercury_global")
    private Boolean mercuryGlobal;

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
     * Getter method for field companyPersonActiveServicesId
     *
     * @return value of companyPersonActiveServicesId
     */
    public CompanyPersonActiveServicesId getCompanyPersonActiveServicesId() {
        return companyPersonActiveServicesId;
    }

    /**
     * Setter method for field companyPersonActiveServicesId
     *
     * @param companyPersonActiveServicesId : value of companyPersonActiveServicesId
     */
    public void setCompanyPersonActiveServicesId(CompanyPersonActiveServicesId companyPersonActiveServicesId) {
        this.companyPersonActiveServicesId = companyPersonActiveServicesId;
    }

    /**
     * Getter method for field segmentTypeLocal
     *
     * @return value of segmentTypeLocal
     */
    public String getSegmentTypeLocal() {
        return segmentTypeLocal;
    }

    /**
     * Setter method for field segmentTypeLocal
     *
     * @param segmentTypeLocal : value of segmentTypeLocal
     */
    public void setSegmentTypeLocal(String segmentTypeLocal) {
        this.segmentTypeLocal = segmentTypeLocal;
    }

    /**
     * Getter method for field segmentLocal
     *
     * @return value of segmentLocal
     */
    public String getSegmentLocal() {
        return segmentLocal;
    }

    /**
     * Setter method for field segmentLocal
     *
     * @param segmentLocal : value of segmentLocal
     */
    public void setSegmentLocal(String segmentLocal) {
        this.segmentLocal = segmentLocal;
    }

    /**
     * Getter method for field mercuryGlobal
     *
     * @return value of mercuryGlobal
     */
    public Boolean getMercuryGlobal() {
        return mercuryGlobal;
    }

    /**
     * Setter method for field mercuryGlobal
     *
     * @param mercuryGlobal : value of mercuryGlobal
     */
    public void setMercuryGlobal(Boolean mercuryGlobal) {
        this.mercuryGlobal = mercuryGlobal;
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
