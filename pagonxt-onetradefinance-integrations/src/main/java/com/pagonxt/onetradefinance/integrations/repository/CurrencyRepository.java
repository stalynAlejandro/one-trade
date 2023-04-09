package com.pagonxt.onetradefinance.integrations.repository;

import com.pagonxt.onetradefinance.integrations.repository.entity.CurrencyDAO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository Class to access the table currency.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyDAO, String> {
}
