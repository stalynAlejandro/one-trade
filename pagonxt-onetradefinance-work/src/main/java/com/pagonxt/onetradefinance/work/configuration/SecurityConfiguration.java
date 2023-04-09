package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.autoconfigure.security.FlowableWebSecurityConfigurerAdapter;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationFailureHandler;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationSuccessHandler;
import com.flowable.platform.common.security.SecurityConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

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
@Profile("!oauth & !saml & !santander & !santander_local")
public class SecurityConfiguration extends FlowableWebSecurityConfigurerAdapter {

    /**
     * class method
     * @param http      : an HttpSecurity object
     * @throws Exception: Handles exceptions
     * @see  org.springframework.security.config.annotation.web.builders.HttpSecurity
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

        http
                .logout()
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .logoutUrl("/auth/logout")
                .and()
                // Non authenticated exception handling. The formLogin and httpBasic configure the exceptionHandling
                // We have to initialize the exception handling with a default authentication
                // entry point in order to return 401 each time and not have a
                // forward due to the formLogin or the http basic popup due to the httpBasic
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint
                        (HttpStatus.UNAUTHORIZED), AnyRequestMatcher.INSTANCE)
                .and()
                .formLogin()
                .loginProcessingUrl("/auth/login")
                .successHandler(new AjaxAuthenticationSuccessHandler())
                .failureHandler(new AjaxAuthenticationFailureHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/analytics-api/**").hasAuthority(SecurityConstants.ACCESS_REPORTS_METRICS)
                .antMatchers("/work-object-api/**").hasAuthority(SecurityConstants.ACCESS_WORKOBJECT_API)
                // allow context root for all (it triggers the loading of the initial page)
                .antMatchers("/").permitAll()
                .antMatchers(
                        "/**/*.svg", "/**/*.ico", "/**/*.png", "/**/*.woff2", "/**/*.css",
                        "/**/*.woff", "/**/*.html", "/**/*.js",
                        "/**/index.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }
}
