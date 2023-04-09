package com.pagonxt.onetradefinance.work.configuration;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.flowable.spring.boot.azure.blob.BlobContainerClientCustomizer;

/**
 * Class to customize the azure blob container client.
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class AzureBlobContainerClientCustomizer implements BlobContainerClientCustomizer {

    /**
     * Bean with the configuration properties.
     */
    private final AzureBlobContainerProperties azureBlobContainerProperties;

    /**
     * Constructor.
     *
     * @param azureBlobContainerProperties : the azure blob container properties
     */
    public AzureBlobContainerClientCustomizer(AzureBlobContainerProperties azureBlobContainerProperties) {
        this.azureBlobContainerProperties = azureBlobContainerProperties;
    }

    /**
     * Method to customize the azure blob container client using the configuration properties.
     *
     * @param blobContainerClientBuilder : the blob container client builder
     */
    @Override
    public void customize(BlobContainerClientBuilder blobContainerClientBuilder) {
        blobContainerClientBuilder.credential(getAzureClientCredentials())
                .endpoint(azureBlobContainerProperties.getEndpoint())
                .containerName(azureBlobContainerProperties.getContainerName());
    }

    /**
     * Method to create the client credentials using the configuration properties.
     *
     * @return the client secret credential
     */
    private ClientSecretCredential getAzureClientCredentials() {
        return new ClientSecretCredentialBuilder()
                .clientId(azureBlobContainerProperties.getClientId())
                .clientSecret(azureBlobContainerProperties.getClientSecret())
                .tenantId(azureBlobContainerProperties.getTenantId())
                .build();
    }
}
