package com.pagonxt.onetradefinance.external.backend.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for the cache manager with caches "jwtToken" and "apiToken".
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    /**
     * Configuration bean of the cache manager
     *
     * @return : the cache manager
     */
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("jwtToken", "apiToken");
    }

}
