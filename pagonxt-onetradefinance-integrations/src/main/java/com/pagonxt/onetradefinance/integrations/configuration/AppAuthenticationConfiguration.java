package com.pagonxt.onetradefinance.integrations.configuration;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

/**
 * Configuration class to create bean for authentication between applications
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
public class AppAuthenticationConfiguration {

    @Value("${one-trade.integrations.app-authentication.jwt-secret}")
    private String jwtSecret;

    /**
     * Class method to generate a bean used in the JWT generation and validation.
     *
     * @return an hmac Key
     */
    @Bean
    public Key hmacKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
                SignatureAlgorithm.HS256.getJcaName());
    }
}
