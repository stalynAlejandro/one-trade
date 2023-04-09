package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.spring.boot.azure.blob.BlobContainerClientCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlobContainerConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(BlobContainerConfiguration.class);

    /**
     * Bean with the configuration properties.
     */
    private final AzureBlobContainerProperties azureBlobContainerProperties;

    /**
     * Constructor.
     *
     * @param azureBlobContainerProperties : the azure blob container properties
     */
    public BlobContainerConfiguration(AzureBlobContainerProperties azureBlobContainerProperties) {
        this.azureBlobContainerProperties = azureBlobContainerProperties;
    }

    @Bean
    @ConditionalOnProperty(prefix = "santander.azure.blob",
            name = "enabled", havingValue = "true", matchIfMissing = false)
    public BlobContainerClientCustomizer getAzureBlobContainerClientCustomizer() {
        LOG.info("Injecting azure blob container client customizer");
        return new AzureBlobContainerClientCustomizer(azureBlobContainerProperties);
    }
}
