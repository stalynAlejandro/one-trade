package com.pagonxt.onetradefinance.external.backend.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.util.List;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@Profile("!saml-local & !saml-dev & !apigee")
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    /**
     * Class method
     * @param http HTTPSecurity object
     * @see org.springframework.security.web.SecurityFilterChain
     * @see org.springframework.security.config.annotation.web.builders.HttpSecurity
     * @return a SecurityFilterChain object
     * @throws Exception handles exceptions
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/actuator/health").permitAll().and()
                .authorizeRequests().anyRequest().authenticated().and()
                .csrf().disable();
        return http.build();
    }

    /**
     * Class method
     * @return BCryptPasswordEncoder object
     * @see org.springframework.security.crypto.password.PasswordEncoder
     * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Class method
     * @param encoder a PasswordEncoder object
     * @see org.springframework.security.core.userdetails.UserDetailsService
     * @see org.springframework.security.crypto.password.PasswordEncoder
     * @return InMemoryUserDetailsService object
     * @throws IOException handles IO exceptions
     */
    @Bean
    public UserDetailsService users(PasswordEncoder encoder) throws IOException {
        InMemoryUserDetailsManager result = new InMemoryUserDetailsManager();
        List<User> users = new ObjectMapper().readValue
                (new ClassPathResource("default-profile/users.json").getInputStream(), new TypeReference<>() {
        });
        for (User user : users) {
            UserDetails userDetails =
                    org.springframework.security.core.userdetails.User.builder().username(user.getUserId())
                    .password(encoder.encode(user.getUserDisplayedName())).roles(user.getUserType().split(",")).build();
            result.createUser(userDetails);
        }
        return result;
    }
}
