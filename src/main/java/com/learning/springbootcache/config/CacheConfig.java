package com.learning.springbootcache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean("keyGenerator")
    public KeyGenerator keyGenerator(){
        return (target, method, params) -> target + method.getName() + Arrays.toString(params);
    }
}
