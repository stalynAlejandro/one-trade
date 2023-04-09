package com.pagonxt.onetradefinance.work.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ConditionalOnProperty(prefix = "one-trade.integrations.repository", name = "mock-enabled", havingValue = "false")
@Configuration
@EnableJpaRepositories(basePackages="com.pagonxt.onetradefinance.integrations.repository",
        entityManagerFactoryRef="santanderEntityManagerFactory",
        transactionManagerRef = "santanderTransactionManager")
@EntityScan("com.pagonxt.onetradefinance.integrations.repository.entity")
public class SantanderDatasourceConfiguration {
}