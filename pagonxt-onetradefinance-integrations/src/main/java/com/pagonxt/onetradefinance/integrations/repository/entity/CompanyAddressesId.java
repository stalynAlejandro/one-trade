package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of composite primary key for company address entity
 */
@Embeddable
public class CompanyAddressesId implements Serializable {
    @Column(name = "access", nullable=false)
    private String access;
    @Column(name = "company_global_id", nullable=false)
    private String companyGlobalId;
    @Column(name = "address_type_code", nullable=false)
    private String addressTypeCode;

    public CompanyAddressesId(String access, String companyGlobalId, String addressTypeCode) {
        this.access = access;
        this.companyGlobalId = companyGlobalId;
        this.addressTypeCode = addressTypeCode;
    }

    public CompanyAddressesId() {
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

    public String getAddressTypeCode() {
        return addressTypeCode;
    }

    public void setAddressTypeCode(String addressTypeCode) {
        this.addressTypeCode = addressTypeCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyAddressesId that = (CompanyAddressesId) o;
        return access.equals(that.access) && companyGlobalId.equals(that.companyGlobalId) && addressTypeCode.equals(that.addressTypeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, companyGlobalId, addressTypeCode);
    }
}
