package com.pagonxt.onetradefinance.integrations.repository;

import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyDataDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyDataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompanyDataRepository extends JpaRepository<CompanyDataDAO, CompanyDataId>, JpaSpecificationExecutor<CompanyDataDAO> {

    List<CompanyDataDAO> findByCustomerLegalId(String personNumber);
}
