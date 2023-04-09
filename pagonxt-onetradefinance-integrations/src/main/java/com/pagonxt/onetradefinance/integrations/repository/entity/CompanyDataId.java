package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of composite primary key for company data entity
 */
@Embeddable
public class CompanyDataId implements Serializable {
    @Column(name = "country_iso_1", nullable=false)
    private String countryIsoCode;
    @Column(name = "company_global_id", nullable=false)
    private String companyGlobalId;

    public CompanyDataId(String countryIsoCode, String companyGlobalId) {
        this.countryIsoCode = countryIsoCode;
        this.companyGlobalId = companyGlobalId;
    }

    public CompanyDataId() {
    }

    public String getCountryIsoCode() {
        return countryIsoCode;
    }

    public void setCountryIsoCode(String countryIsoCode) {
        this.countryIsoCode = countryIsoCode;
    }

    public String getCompanyGlobalId() {
        return companyGlobalId;
    }

    public void setCompanyGlobalId(String companyGlobalId) {
        this.companyGlobalId = companyGlobalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyDataId that = (CompanyDataId) o;
        return countryIsoCode.equals(that.countryIsoCode) && companyGlobalId.equals(that.companyGlobalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryIsoCode, companyGlobalId);
    }
}
