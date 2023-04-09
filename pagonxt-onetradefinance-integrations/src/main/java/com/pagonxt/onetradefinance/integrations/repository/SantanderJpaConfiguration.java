package com.pagonxt.onetradefinance.integrations.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@ConditionalOnProperty(prefix = "one-trade.integrations.repository", name = "mock-enabled", havingValue = "false")
@Configuration
@EnableTransactionManagement
public class SantanderJpaConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SantanderJpaConfiguration.class);

    @Bean("santanderDataSource")
    @ConfigurationProperties(prefix="spring.santander-datasource")
    public DataSource santanderDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("santanderEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean santanderEntityManagerFactory(
            @Qualifier("santanderDataSource") DataSource dataSource,
            EntityManagerFactoryBuilder builder) {
        LOG.info("Creating santanderEntityManagerFactory bean...");
        return builder
                .dataSource(dataSource)
                .packages("com.pagonxt.onetradefinance.integrations.repository.entity")
                .build();
    }

    @Bean("santanderTransactionManager")
    public PlatformTransactionManager santanderTransactionManager(
            @Qualifier("santanderEntityManagerFactory") LocalContainerEntityManagerFactoryBean santanderEntityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(santanderEntityManagerFactory.getObject()));
    }
}
