package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.autoconfigure.security.FlowableWebSecurityConfigurerAdapter;
import com.pagonxt.onetradefinance.integrations.configuration.AppAuthenticationJWTFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration(proxyBeanMethods = false)
@Order(10)
@EnableWebSecurity
@Profile("santander | santander_local")
public class SecurityConfigurationSantander extends FlowableWebSecurityConfigurerAdapter {

    private final AppAuthenticationJWTFilter appAuthenticationJWTFilter;

    @Autowired
    protected ObjectProvider<OidcClientInitiatedLogoutSuccessHandler> oidcClientInitiatedLogoutSuccessHandlerObjectProvider;

    public SecurityConfigurationSantander(AppAuthenticationJWTFilter appAuthenticationJWTFilter) {
        this.appAuthenticationJWTFilter = appAuthenticationJWTFilter;
    }

    @Override
    @SuppressWarnings("java:S4502") // (*) It is safe to disable CSRF for this endpoint
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // Authenticate internal app communications
        http.csrf().ignoringAntMatchers("/backend/**"); // (*)
        http.addFilterBefore(appAuthenticationJWTFilter, OAuth2LoginAuthenticationFilter.class);
        // Mark all requests as authenticated in order for the redirect to happen when the application is accessed
        http.authorizeRequests().anyRequest().authenticated();

        // An HttpSessionSecurityContextRepository is needed for oauth2 to work
        HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
        securityContextRepository.setDisableUrlRewriting(true);
        http.securityContext().securityContextRepository(securityContextRepository);

        http.oauth2Login();
        http.oauth2Client();

        http.logout().logoutUrl("/app/logout");

        oidcClientInitiatedLogoutSuccessHandlerObjectProvider.ifAvailable(http.logout()::logoutSuccessHandler);

        http.oauth2ResourceServer().jwt();
    }
}
