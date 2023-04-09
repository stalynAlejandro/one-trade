package com.pagonxt.onetradefinance.work.configuration;

import com.azure.identity.ClientSecretCredential;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class AzureBlobContainerClientCustomizerTest {

    @Test
    void testCustomize_initsContainer() {
        // Given
        AzureBlobContainerProperties properties = mock(AzureBlobContainerProperties.class);
        when(properties.getEndpoint()).thenReturn("endpoint");
        when(properties.getContainerName()).thenReturn("containerName");
        when(properties.getClientId()).thenReturn("clientId");
        when(properties.getClientSecret()).thenReturn("secret");
        when(properties.getTenantId()).thenReturn("tenant");
        AzureBlobContainerClientCustomizer customizer = new AzureBlobContainerClientCustomizer(properties);

        BlobContainerClientBuilder builder = mock(BlobContainerClientBuilder.class, Answers.RETURNS_DEEP_STUBS);
        when(builder.credential(any(ClientSecretCredential.class)).endpoint(any())).thenReturn(builder);

        // When and then
        Assertions.assertDoesNotThrow(
                () -> customizer.customize(builder),
                "Should not throw exception"
        );
    }
}
