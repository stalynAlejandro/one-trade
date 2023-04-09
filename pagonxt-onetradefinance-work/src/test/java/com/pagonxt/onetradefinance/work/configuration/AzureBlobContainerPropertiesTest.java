package com.pagonxt.onetradefinance.work.configuration;

import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@UnitTest
class AzureBlobContainerPropertiesTest {

    @Test
    void testContainerNameMethods() {
        // Given
        AzureBlobContainerProperties item = new AzureBlobContainerProperties();
        // When and then
        item.setContainerName("containerName");
        Assertions.assertEquals("containerName", item.getContainerName());
    }

    @Test
    void testClientIdMethods() {
        // Given
        AzureBlobContainerProperties item = new AzureBlobContainerProperties();
        // When and then
        item.setClientId("clientId");
        Assertions.assertEquals("clientId", item.getClientId());
    }

    @Test
    void testClientSecretMethods() {
        // Given
        AzureBlobContainerProperties item = new AzureBlobContainerProperties();
        // When and then
        item.setClientSecret("clientSecret");
        Assertions.assertEquals("clientSecret", item.getClientSecret());
    }

    @Test
    void testEnabledMethods() {
        // Given
        AzureBlobContainerProperties item = new AzureBlobContainerProperties();
        // When and then
        item.setEnabled(true);
        Assertions.assertTrue(item.isEnabled());
    }

    @Test
    void testEndpointMethods() {
        // Given
        AzureBlobContainerProperties item = new AzureBlobContainerProperties();
        // When and then
        item.setEndpoint("endpoint");
        Assertions.assertEquals("endpoint", item.getEndpoint());
    }

    @Test
    void testTenantIdMethods() {
        // Given
        AzureBlobContainerProperties item = new AzureBlobContainerProperties();
        // When and then
        item.setTenantId("tenantId");
        Assertions.assertEquals("tenantId", item.getTenantId());
    }
}
