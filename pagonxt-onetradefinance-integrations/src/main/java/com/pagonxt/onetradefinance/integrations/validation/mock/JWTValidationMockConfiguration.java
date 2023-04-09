package com.pagonxt.onetradefinance.integrations.validation.mock;

import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that provides properties to the bean used in the validation of the JWT token.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ConditionalOnMissingBean(JWTValidation.class)
public class JWTValidationMockConfiguration {

    /**
     * Configuration bean of the jwt validator
     *
     * @return : the jwt validator mock
     */
    @Bean
    JWTValidation jwtValidation() {
        return new JWTValidationMockImpl();
    }

}
