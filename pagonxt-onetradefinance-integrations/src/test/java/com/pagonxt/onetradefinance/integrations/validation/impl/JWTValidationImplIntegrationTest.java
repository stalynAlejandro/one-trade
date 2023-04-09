package com.pagonxt.onetradefinance.integrations.validation.impl;

import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import com.santander.serenity.security.credentials.jwt.JWTTokenValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.Mockito.*;

@SpringBootTest
@ContextConfiguration
class JWTValidationImplIntegrationTest {

    @Configuration
    @EnableCaching
    static class Config {

        @Bean
        JWTTokenValidator jwtTokenValidator() {
            return mock(JWTTokenValidator.class);
        }

        @Bean
        JWTValidation jwtValidation(JWTTokenValidator jwtTokenValidator) {
            return new JWTValidationImpl(jwtTokenValidator);
        }

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("jwtToken");
        }
    }

    @Autowired
    JWTValidation jwtValidation;

    @Autowired
    JWTTokenValidator jwtTokenValidator;

    @Test
    void validate_callTwice_getCachedResult() {
        final String JWT_TOKEN = "eyJraWQiOiJwcmVHdHNTdHNJbnRlcm5ldCIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJzdW" +
                "IiOiJKMDAwMTA0ODkyIiwiY291bnRyeSI6Ik1YQk1TWE1YTU0iLCJuYmYiOjE2NjkxMDM5MTQsInVzZXJfdHlwZSI6ImN1c3RvbWVy" +
                "IiwiaXNzIjoicHJlR3RzSW50ZXJuZXQiLCJleHAiOjE2NjkxMDQ1MTQsImlhdCI6MTY2OTEwMzkxNH0.bqBftvAxjQgs_LQ4ekn08o" +
                "h555mdsq3gvrY5RsCxZMKqFfuB6tHEyTYEyqiUiFhRdFo28V3U2pI1FUbIJLSekizJdzUCS4XyICaCgrVmwTGSrCPGviHE3WmQvE91" +
                "p3Cc6B5UIXsXjz9xcOLcYQA7IiWTIGE5cgJeBFQkyQISOpSE2yg3qgYnjoYOkQkfPHiWxurVEFRl__Rx64kyFzViEAqZxRP8cZD5zN" +
                "cy6bfV4fmEDadrqSt6Dz7qF3HN_5LbZJfP9Jlgi0QjZy5Bl2tuWTRLpdxwcyNsbyGisMqpCPg8bHRjDgq9U00Nej6kHfXg-_oFhIHl" +
                "p7mhUcRF9cORrg";
        jwtValidation.verify("Bearer " + JWT_TOKEN);
        jwtValidation.verify("Bearer " + JWT_TOKEN);
        verify(jwtTokenValidator, times(1)).validate(JWT_TOKEN);
    }
}
