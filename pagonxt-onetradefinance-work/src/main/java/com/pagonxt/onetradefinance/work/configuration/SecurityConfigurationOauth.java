package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.autoconfigure.security.FlowableWebSecurityConfigurerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.autoconfigure.security.FlowableWebSecurityConfigurerAdapter
 * @since jdk-11.0.13
 */
@Configuration(proxyBeanMethods = false)
@Order(10)
@EnableWebSecurity
@Profile("oauth")
public class SecurityConfigurationOauth extends FlowableWebSecurityConfigurerAdapter {

    /**
     * class method
     * @param http      : an HttpSecurity object
     * @throws Exception: Handles exceptions
     * @see  org.springframework.security.config.annotation.web.builders.HttpSecurity
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        // Mark all requests as authenticated in order for the redirect to happen when the application is accessed
        http.authorizeRequests().anyRequest().authenticated();

        // An HttpSessionSecurityContextRepository is needed for oauth2 to work
        HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.setDisableUrlRewriting(true);
        http.securityContext().securityContextRepository(securityContextRepository);

        http.oauth2Login();
        http.oauth2Client();

        // Remove the line below if Access using Bearer authentication token is not needed
        http.oauth2ResourceServer().jwt();

        http.getSharedObject(ApplicationContext.class);
    }
}
