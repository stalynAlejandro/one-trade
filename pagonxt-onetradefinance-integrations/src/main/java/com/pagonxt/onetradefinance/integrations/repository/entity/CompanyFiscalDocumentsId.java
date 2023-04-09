package com.pagonxt.onetradefinance.integrations.repository.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representation of composite primary key for company fiscal documents entity
 */
@Embeddable
public class CompanyFiscalDocumentsId implements Serializable {
    @Column(name = "access", nullable=false)
    private String access;

    @Column(name = "company_global_id", nullable=false)
    private String companyGlobalId;

    @Column(name = "document_type_reference", nullable=false)
    private String documentTypeReference;

    @Column(name = "document_number", nullable=false)
    private String documentNumber;

    public CompanyFiscalDocumentsId(String access, String companyGlobalId, String documentTypeReference, String documentNumber) {
        this.access = access;
        this.companyGlobalId = companyGlobalId;
        this.documentTypeReference = documentTypeReference;
        this.documentNumber = documentNumber;
    }

    public CompanyFiscalDocumentsId() {
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

    public String getDocumentTypeReference() {
        return documentTypeReference;
    }

    public void setDocumentTypeReference(String documentTypeReference) {
        this.documentTypeReference = documentTypeReference;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyFiscalDocumentsId that = (CompanyFiscalDocumentsId) o;
        return access.equals(that.access) && companyGlobalId.equals(that.companyGlobalId) && documentTypeReference.equals(that.documentTypeReference) && documentNumber.equals(that.documentNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, companyGlobalId, documentTypeReference, documentNumber);
    }
}
