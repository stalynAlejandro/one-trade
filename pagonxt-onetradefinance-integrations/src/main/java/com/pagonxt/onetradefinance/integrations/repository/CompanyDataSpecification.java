package com.pagonxt.onetradefinance.integrations.repository;

import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyDataDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyFiscalDocumentsDAO;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class CompanyDataSpecification implements Specification<CompanyDataDAO> {

    private final CustomerQuery customerQuery;

    public CompanyDataSpecification(final CustomerQuery customerQuery) {
        super();
        this.customerQuery = customerQuery;
    }

    @Override
    public Predicate toPredicate(Root<CompanyDataDAO> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        Predicate predicateActiveCustomer = criteriaBuilder.isNull(root.get("customerEndDate"));

        Join<CompanyDataDAO, CompanyFiscalDocumentsDAO> joinCFD = root.join("companyFiscalDocumentsDAOS", JoinType.INNER);
        Predicate predicateOnlyDocumentTaxId = criteriaBuilder.equal(
                joinCFD.get("companyFiscalDocumentsId").get("documentTypeReference"), "tax_id");

        Predicate finalPredicate = criteriaBuilder.and(predicateActiveCustomer, predicateOnlyDocumentTaxId);

        if (customerQuery.getName() != null){
            Predicate predicateForCompanyName = generateLikePredicate(criteriaBuilder,
                    root.get("companyName"), customerQuery.getName());
            finalPredicate = criteriaBuilder.and(finalPredicate, predicateForCompanyName);
        }

        if (customerQuery.getPersonNumber() != null){
            Predicate predicateForPersonNumber = generateLikePredicate(criteriaBuilder,
                    root.get("customerLegalId"), customerQuery.getPersonNumber());
            finalPredicate = criteriaBuilder.and(finalPredicate, predicateForPersonNumber);
        }

        if (customerQuery.getTaxId() != null){
            Predicate predicateForTaxId = generateLikePredicate(criteriaBuilder,
                    joinCFD.get("companyFiscalDocumentsId").get("documentNumber"), customerQuery.getTaxId());
            finalPredicate = criteriaBuilder.and(finalPredicate, predicateForTaxId);
        }
        return finalPredicate;
    }

    private Predicate generateLikePredicate(CriteriaBuilder criteriaBuilder, Path<String> path, String value) {
        return criteriaBuilder.like(criteriaBuilder.lower(path), "%" + value.toLowerCase() + "%");
    }
}
