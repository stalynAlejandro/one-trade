package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@UnitTest
class TokenGeneratorServiceTest {

    private static final String jwtSecret = "242a1b16cf2ba9a6a09715782c1bb675162355f1ade92ce169fb583e2b47aeea";

    @Test
    void generateJWTToken_generatesValidToken() {
        // Given
        Key key = getKey();
        TokenGeneratorService service = new TokenGeneratorService(key);
        String username = "username";

        // When
        String token = service.generateJWTToken(username);

        // Then
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        String result = (String) jwt.getBody().get("name");
        Assertions.assertEquals(username, result, "Generated Jwt token should have a valid user claim");
    }

    private static Key getKey() {
        return new SecretKeySpec(Base64.getDecoder().decode(jwtSecret),
                SignatureAlgorithm.HS256.getJcaName());
    }
}
