package com.learning.springbootcache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.NamedCacheResolver;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean("keyGenerator")
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> target + method.getName() + Arrays.toString(params);
    }

    /**
     * 5.4 Setting a Different Config for Each Cache by Using CacheManager Attribute
     */
    @Bean("brandFilterCacheManager")
    @Primary
    public CacheManager brandCacheManager() {
        return getCacheManager("brandFilterCache", 10,1);
    }

    @Bean("priceFilterCacheManager")
    public CacheManager priceCacheManager() {
        return getCacheManager("priceFilterCache", 2, 3);
    }

    /**
     * 5.5 Using the CacheResolver Attribute
     */

    @Bean("priceFilterCacheResolver")
    public CacheResolver priceCacheResolver() {
        return new NamedCacheResolver(priceCacheManager(), "priceFilterCache");
    }

    private CacheManager getCacheManager(final String cacheManager, final int secondsExpiry, final long entriesSize) {

        final CaffeineCache cache = buildCache(cacheManager, secondsExpiry, entriesSize);
        final SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Collections.singleton(cache));
        return manager;

    }

    private CaffeineCache buildCache(final String cacheName, final int secondsExpiry, final long entriesSize) {
        return new CaffeineCache(
                cacheName,
                Caffeine
                        .newBuilder()
                        .expireAfterWrite(secondsExpiry, TimeUnit.SECONDS)
                        .maximumSize(entriesSize)
                        .build()
        );
    }
}
