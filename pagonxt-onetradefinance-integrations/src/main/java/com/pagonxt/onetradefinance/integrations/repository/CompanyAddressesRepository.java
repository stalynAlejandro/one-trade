package com.pagonxt.onetradefinance.integrations.repository;

import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyAddressesRepository extends JpaRepository<CompanyAddressesDAO, CompanyAddressesId> {
}
