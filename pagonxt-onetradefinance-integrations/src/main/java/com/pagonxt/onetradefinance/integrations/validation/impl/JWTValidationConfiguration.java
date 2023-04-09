package com.pagonxt.onetradefinance.integrations.validation.impl;

import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import com.santander.serenity.security.credentials.jwt.JWTTokenValidator;
import com.santander.serenity.security.keyprovider.KeyProvider;
import com.santander.serenity.security.keyprovider.RemoteKeyProvider;
import com.santander.serenity.security.keyprovider.conf.RemoteKeyProviderProperties;
import es.santander.security.auth.legacy.LegacyModeCfg;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.net.URL;
import java.util.Arrays;



/**
 * Configuration class that provides properties to the bean used in the validation of the JWT token.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@Profile("apigee")
public class JWTValidationConfiguration {

    /**
     * Configuration properties
     */
    private final JWTValidationProperties jwtValidationProperties;

    /**
     * Constructor.
     *
     * @param jwtValidationProperties   : the configuration properties
     */
    public JWTValidationConfiguration(JWTValidationProperties jwtValidationProperties) {
        this.jwtValidationProperties = jwtValidationProperties;
    }

    /**
     * Configuration bean of the jwt validator
     *
     * @return : the jwt validator
     */
    @Bean
    JWTValidation jwtValidation() throws Exception {
        return new JWTValidationImpl(jwtTokenValidator());
    }

    /**
     * Method to generate a jwt token validator.
     *
     * @return : the jwt token validator
     */
    private JWTTokenValidator jwtTokenValidator() throws Exception {
        RemoteKeyProviderProperties remoteKeyProviderProperties = new RemoteKeyProviderProperties();
        remoteKeyProviderProperties.setUseV2(jwtValidationProperties.isUseV2());
        remoteKeyProviderProperties.setKeyServerURL(jwtValidationProperties.getUrl().toString());
        RestTemplate restTemplate = createRestTemplate();
        KeyProvider keyProvider = new RemoteKeyProvider(remoteKeyProviderProperties, restTemplate, true);
        keyProvider.afterPropertiesSet();
        return new JWTTokenValidator(keyProvider, jwtValidationProperties.getAudiences(),
                jwtValidationProperties.getIssuers(), new LegacyModeCfg());
    }

    @NotNull
    private RestTemplate createRestTemplate() {
        if (jwtValidationProperties.getTrustStore() == null) {
            return new RestTemplate();
        }
        RestTemplate restTemplate = new RestTemplate();
        final SSLContext sslContext;
        try {
            sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial(new URL(jwtValidationProperties.getTrustStore()),
                            jwtValidationProperties.getTrustStorePassword())
                    .setProtocol(jwtValidationProperties.getProtocol())
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to setup client SSL context", e
            );
        } finally {
            Arrays.fill(jwtValidationProperties.getTrustStorePassword(), (char) 0);
        }

        final HttpClient httpClient= HttpClientBuilder.create()
                .setSSLContext(sslContext)
                .build();

        final ClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }
}
