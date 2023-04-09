package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of composite primary key for company person activer services entity
 */
@Embeddable
public class CompanyPersonActiveServicesId implements Serializable {
    @Column(name = "access", nullable=false)
    private String access;

    @Column(name = "company_global_id", nullable=false)
    private String companyGlobalId;

    @Column(name = "global_id", nullable=false)
    private String globalId;

    public CompanyPersonActiveServicesId(String access, String companyGlobalId, String globalId) {
        this.access = access;
        this.companyGlobalId = companyGlobalId;
        this.globalId = globalId;
    }

    public CompanyPersonActiveServicesId() {
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getCompanyGlobalId() {
        return companyGlobalId;
    }

    public void setCompanyGlobalId(String companyGlobalId) {
        this.companyGlobalId = companyGlobalId;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyPersonActiveServicesId that = (CompanyPersonActiveServicesId) o;
        return access.equals(that.access) && companyGlobalId.equals(that.companyGlobalId) && globalId.equals(that.globalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, companyGlobalId, globalId);
    }
}
