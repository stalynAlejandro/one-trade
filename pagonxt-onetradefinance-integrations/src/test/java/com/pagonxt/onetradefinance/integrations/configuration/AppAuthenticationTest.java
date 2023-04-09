package com.pagonxt.onetradefinance.integrations.configuration;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@UnitTest
class AppAuthenticationTest {

    @Test
    void testConstructor() {
        // Given
        String username = "username";

        // When
        AppAuthentication authentication = new AppAuthentication(username);

        // Then
        Assertions.assertEquals(username, authentication.getUserName(), "Should have a valid username");
    }

    @Test
    void testUsername() {
        // Given
        String username = "username";

        // When
        AppAuthentication authentication = new AppAuthentication("");
        authentication.setUserName(username);

        // Then
        Assertions.assertEquals(username, authentication.getUserName(), "Should have a valid username");
    }

    @Test
    void testGetAuthorities_returnSEmptyList() {
        // Given
        AppAuthentication authentication = new AppAuthentication("username");

        // When and then
        Assertions.assertNotNull(authentication.getAuthorities(), "Authorities should not be null");
        Assertions.assertEquals(0, authentication.getAuthorities().size(),
                "Authorities should be empty");
    }

    @Test
    void testGetCredentials_returnsNull() {
        // Given
        AppAuthentication authentication = new AppAuthentication("username");

        // When and then
        Assertions.assertNull(authentication.getCredentials(), "Credentials should be null");
    }

    @Test
    void testGetDetails_returnsNull() {
        // Given
        AppAuthentication authentication = new AppAuthentication("username");

        // When and then
        Assertions.assertNull(authentication.getDetails(), "Details should be null");
    }

    @Test
    void testIsAuthenticated_returnsTrue() {
        // Given
        AppAuthentication authentication = new AppAuthentication("username");

        // When and then
        Assertions.assertTrue(authentication.isAuthenticated(), "isAuthenticated should return true");
    }

    @Test
    void testSetAuthenticated_doesNothing() {
        // Given
        AppAuthentication authentication = new AppAuthentication("username");

        // When
        authentication.setAuthenticated(false);

        // When and then
        Assertions.assertTrue(authentication.isAuthenticated(), "isAuthenticated should return true");
    }

    @Test
    void testGetName_returnsValidData() {
        // Given
        String username = "username";

        // When
        AppAuthentication authentication = new AppAuthentication(username);

        // Then
        Assertions.assertEquals(username, authentication.getName(), "Should return a valid name");
    }
}
