package com.pagonxt.onetradefinance.external.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Profile("apigee")
@Configuration
@EnableWebSecurity
public class SecurityConfigurationApigee {

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
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().anyRequest().permitAll().and()
                .csrf().disable();
        return http.build();
    }
}
