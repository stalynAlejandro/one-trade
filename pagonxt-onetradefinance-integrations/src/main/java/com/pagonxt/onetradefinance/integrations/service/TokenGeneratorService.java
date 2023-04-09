package com.pagonxt.onetradefinance.integrations.service;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Service class to generate the token for the communication between the external app and work.
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class TokenGeneratorService {

    /**
     * The hmac key
     */
    private final Key hmacKey;

    /**
     * Constructor.
     *
     * @param hmacKey : the hmac key
     */
    public TokenGeneratorService(Key hmacKey) {
        this.hmacKey = hmacKey;
    }

    /**
     * Method to generate a token.
     *
     * @param username : the username
     * @return the token jwt
     */
    public String generateJWTToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim("name", username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .signWith(hmacKey)
                .compact();
    }
}
