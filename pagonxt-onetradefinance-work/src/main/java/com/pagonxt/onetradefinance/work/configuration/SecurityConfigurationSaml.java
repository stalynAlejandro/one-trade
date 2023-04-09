package com.pagonxt.onetradefinance.work.configuration;

import com.flowable.autoconfigure.security.FlowableWebSecurityConfigurerAdapter;
import com.flowable.core.spring.security.FlowablePlatformUserDetailsService;
import com.flowable.platform.common.security.SecurityConstants;
import org.opensaml.saml.saml2.core.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
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
 * @see com.flowable.autoconfigure.security.FlowableWebSecurityConfigurerAdapter
 * @see com.pagonxt.onetradefinance.work.configuration.SecurityConfigurationSamlProperties
 * @since jdk-11.0.13
 */
@Configuration
@Order(10)
@EnableWebSecurity
@Profile("saml")
@EnableConfigurationProperties(SecurityConfigurationSamlProperties.class)
public class SecurityConfigurationSaml extends FlowableWebSecurityConfigurerAdapter {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(SecurityConfigurationSaml.class);

    //class attributes
    private final FlowablePlatformUserDetailsService flowablePlatformUserDetailsService;
    private final SecurityConfigurationSamlProperties properties;

    /**
     * constructor method
     * @param flowablePlatformUserDetailsService    : a FlowablePlatformUserDetailsService object
     * @param properties                            : a SecurityConfigurationSamlProperties object
     */
    public SecurityConfigurationSaml(FlowablePlatformUserDetailsService flowablePlatformUserDetailsService,
                                     SecurityConfigurationSamlProperties properties) {
        this.flowablePlatformUserDetailsService = flowablePlatformUserDetailsService;
        this.properties = properties;
    }

    /**
     * class method
     * SAML request might contain semicolon.
     * @return an HttpFirewall object
     */
    @Bean
    public HttpFirewall allowSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);
        return firewall;
    }

    /**
     * class method
     * @param http      : an HttpSecurity object
     * @throws Exception: Handles exceptions
     * @see  org.springframework.security.config.annotation.web.builders.HttpSecurity
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);

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

            UserDetails userDetails = this.flowablePlatformUserDetailsService.loadUserByUsername(username);
            LOG.debug(String.format("User details: %s", userDetails.toString()));

            return new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                    userDetails.getPassword(), userDetails.getAuthorities());
        });

        RelyingPartyRegistrationResolver relyingPartyRegistrationResolver =
                new DefaultRelyingPartyRegistrationResolver(relyingPartyRegistrationRepository());

        Saml2MetadataFilter filter =
                new Saml2MetadataFilter(relyingPartyRegistrationResolver, new OpenSamlMetadataResolver());

        http
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/analytics-api/**").hasAuthority(SecurityConstants.ACCESS_REPORTS_METRICS)
                        .antMatchers("/work-object-api/**").hasAuthority(SecurityConstants.ACCESS_WORKOBJECT_API)
                        .antMatchers(
                                "/**/*.svg", "/**/*.ico", "/**/*.png", "/**/*.woff2", "/**/*.css",
                                "/**/*.woff", "/**/*.html", "/**/*.js",
                                "/**/index.html").permitAll()
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
                .addFilterAfter(new HttpHeaderLogFilter(), Saml2MetadataFilter.class)
                .httpBasic(); // We still need http basic for control / design
        LOG.debug("HTTP Security with SAML2 configured");
    }

    /**
     * class method
     *
     * @return a RelyingPartyRegistrationRepository object
     * @see org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository
     *
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
