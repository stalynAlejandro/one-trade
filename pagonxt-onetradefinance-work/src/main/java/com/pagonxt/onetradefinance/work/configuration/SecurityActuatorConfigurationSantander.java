package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.actuate.autoconfigure.security.servlet.ActuatorRequestMatcher;
import com.flowable.platform.common.security.SecurityConstants;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@ConditionalOnClass(EndpointRequest.class)
@Configuration(proxyBeanMethods = false)
@Order(6)
// Actuator configuration should kick in before the Form Login there should always be http basic for the endpoints
@SuppressWarnings("deprecation")
// WebSecurityConfigurerAdapter has been deprecated, but its usage is required by the Flowable configuration mechanism
@Profile("santander | santander_local")
public class SecurityActuatorConfigurationSantander extends WebSecurityConfigurerAdapter {

    @Override
    @SuppressWarnings("java:S4502")
    protected void configure(HttpSecurity http) throws Exception {

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable();

        http
                .requestMatcher(new ActuatorRequestMatcher())
                .authorizeRequests()
                .requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class)).permitAll()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasAuthority(SecurityConstants.ACCESS_ACTUATORS)
                .anyRequest().denyAll();

        http.oauth2Login();
        http.oauth2Client();

        http.oauth2ResourceServer().jwt();
    }
}
