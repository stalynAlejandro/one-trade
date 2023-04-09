package com.pagonxt.onetradefinance.external.backend.configuration;

import org.opensaml.saml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.saml2.provider.service.authentication.OpenSamlAuthenticationProvider;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.saml2.provider.service.servlet.filter.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @see org.slf4j.Logger
 * @see com.pagonxt.onetradefinance.external.backend.configuration.SecurityConfigurationSamlProperties
 * @since jdk-11.0.13
 */
@Configuration
@EnableWebSecurity
@Profile("saml-local | saml-dev")
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(SecurityConfigurationSamlProperties.class)
public class SecurityConfigurationSaml {

    /**
     * Class variables
     */
    private static final Logger LOG = LoggerFactory.getLogger
            (com.pagonxt.onetradefinance.external.backend.configuration.SecurityConfigurationSaml.class);

    private static final String ROLE_USER = "ROLE_USER";

    /**
     * Class attributes
     */
    private final SecurityConfigurationSamlProperties properties;

    /**
     * Class constructor
     * @param properties SecurityConfigurationSamlProperties object
     */
    public SecurityConfigurationSaml(SecurityConfigurationSamlProperties properties) {
        this.properties = properties;
    }

    /**
     * Class method
     * SAML request might contain semicolon.
     * @see org.springframework.security.web.firewall.HttpFirewall
     * @return a HttpFirewall object
     */
    @Bean
    public HttpFirewall allowSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    /**
     * Class method
     * @param http HttpSecurity object
     * @return SecurityFilterChain object
     * @see org.springframework.security.web.SecurityFilterChain
     * @see org.springframework.security.config.annotation.web.builders
     * @throws Exception handles exceptions
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LOG.debug("Configuring HTTP Security with SAML2");

        // Super sets to stateless, which doesn't work with SAML2
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and().securityContext().securityContextRepository(new HttpSessionSecurityContextRepository());

        @SuppressWarnings("deprecation")
        /*
         * This OpenSamlAuthenticationProvider has been deprecated in favour of OpenSaml4AuthenticationProvider,
         * but just changing one class for another breaks the login process. Apparently, there are some versions
         * that need to be changed in the pom. As versions are ultimately dependent on Flowable, we are not
         * doing it, hence the @SuppressWarnings annotation.
         * More info: https://stackoverflow.com/questions/69956447/spring-security-saml-assertion-to-roles-conversion
         */
                OpenSamlAuthenticationProvider authenticationProvider = new OpenSamlAuthenticationProvider();
        authenticationProvider.setResponseAuthenticationConverter(responseToken -> {
            LOG.debug("ResponseAuthenticationConverter");

            Assertion assertion = responseToken.getResponse().getAssertions().get(0);
            LOG.debug(String.format("Assertion.issuer: %s", assertion.getIssuer().getValue()));

            String username = assertion.getSubject().getNameID().getValue();
            LOG.debug(String.format("Assertion.subject.name: %s", username));
            // Mock Office / MiddleOffice Users
            if (username.contains("marta") || username.contains("manuel")) {
                return new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.createAuthorityList
                        (ROLE_USER, "ROLE_MIDDLE_OFFICE"));
            }
            if (username.contains("olivia") || username.contains("omar") || username.contains("oscar")) {
                return new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.createAuthorityList
                        (ROLE_USER, "ROLE_OFFICE"));
            }
            return new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.createAuthorityList
                    (ROLE_USER, "ROLE_BACKOFFICE"));
        });

        RelyingPartyRegistrationResolver relyingPartyRegistrationResolver = new DefaultRelyingPartyRegistrationResolver
                (relyingPartyRegistrationRepository());
        Saml2MetadataFilter filter =
                new Saml2MetadataFilter(relyingPartyRegistrationResolver, new OpenSamlMetadataResolver());

        http
                .authorizeRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .logout(logout -> logout
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .logoutUrl("/auth/logout")
                )
                .saml2Login(saml2 -> saml2
                        .authenticationManager(new ProviderManager(authenticationProvider))
                )
                .addFilterBefore(filter, Saml2WebSsoAuthenticationFilter.class)
                .csrf().disable();
        LOG.debug("HTTP Security with SAML2 configured");
        return http.build();
    }

    /**
     * class method
     * @return RelyingPartyRegistrationRepository object
     * @see org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository
     */
    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() {
        RelyingPartyRegistration registration = RelyingPartyRegistrations
                .fromMetadataLocation(properties.getIdpMetadataLocation())
                .entityId(properties.getSpEntityId())
                .registrationId(properties.getSpRegistrationId())
                .build();
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }
}
