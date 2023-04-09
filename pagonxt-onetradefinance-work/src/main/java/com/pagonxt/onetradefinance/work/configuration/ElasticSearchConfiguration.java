package com.pagonxt.onetradefinance.work.configuration;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Base64;
import java.util.Optional;

@Configuration
public class ElasticSearchConfiguration {

    private final ElasticsearchProperties elasticsearchProperties;
    private final ResourceLoader resourceLoader;

    public ElasticSearchConfiguration(ElasticsearchProperties elasticsearchProperties, ResourceLoader resourceLoader) {
        this.elasticsearchProperties = elasticsearchProperties;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    @ConditionalOnProperty(prefix = "one-trade.elastic", name="load-certificates", havingValue="true")
    public RestClientBuilderCustomizer restClientBuilderCustomizer() {
        return new RestClientBuilderCustomizer() {

            @Override
            public void customize(RestClientBuilder builder) {
                // Shouldn't overwrite the basic rest client configuration
            }

            @Override
            public void customize(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                buildSSLContext().ifPresent(sslContext -> {
                    httpAsyncClientBuilder.setSSLContext(sslContext);
                    httpAsyncClientBuilder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);
                });
            }

            private Optional<SSLContext> buildSSLContext() {
                if (elasticsearchProperties.getKeystore() == null ||
                        elasticsearchProperties.getTruststore() == null) {
                    return Optional.empty();
                }
                try (InputStream keystore = loadCertificateInputStream(elasticsearchProperties.getKeystore());
                     InputStream truststore = loadCertificateInputStream(elasticsearchProperties.getTruststore())) {
                    KeyStore ks = KeyStore.getInstance("pkcs12");
                    KeyStore ts = KeyStore.getInstance("pkcs12");
                    ks.load(keystore, null);
                    ts.load(truststore, null);
                    SSLContextBuilder sslBuilder = SSLContexts.custom()
                            .loadTrustMaterial(ts, null)
                            .loadKeyMaterial(ks, elasticsearchProperties.getKeystorePass().toCharArray());
                    SSLContext sslContext = sslBuilder.build();
                    return Optional.of(sslContext);
                } catch (Exception exception) {
                    throw new IllegalStateException("Failed to initialize public certificate", exception);
                }
            }

            /**
             * Load cert from resource classpath: or file: or base64 string
             *
             * (It seems this bean is instantiated too early to use custom protocol resolvers e.g. base64: :-( )
             */
            private InputStream loadCertificateInputStream(String certificate) throws IOException {
                if (certificate.startsWith("file:")) {
                    Resource resource = resourceLoader.getResource(certificate);
                    if (!resource.exists()) {
                        throw new IllegalStateException(
                                "You specified a certificate for elastic but it doesn't exist: " + certificate);
                    }
                    return resource.getInputStream();
                }
                return new ByteArrayInputStream(Base64.getDecoder().decode(certificate));
            }
        };
    }

}
