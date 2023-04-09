package com.pagonxt.onetradefinance.work.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Configuration class for the cache manager with caches "jwtToken" and "apiToken".
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@ConditionalOnProperty(prefix = "one-trade.cache", name = "enabled", havingValue = "true")
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
        return new ConcurrentMapCacheManager("countryHoliday");
    }

    @CacheEvict(allEntries = true, value = {"countryHoliday"})
    @Scheduled(fixedDelayString = "${one-trade.cache.expiration}",  initialDelayString = "${one-trade.cache.delay}")
    public void reportCacheEvict() {
        // This method is deliberately empty. It just holds the @CacheEvict and @Scheduled annotations.
    }

}
