# SPRING BOOT CACHE

## Using @Cacheable to Cache a Method’s Result in Spring Boot
The @Cacheable annotation is used to mark a method as cacheable. A very important thing about cache in Spring Boot is that you should never call a @Cacheable , @CacheEvict or @CachePut annotated method from the same class as it will never work.

@Cacheable has the following attributes:
 * String[] value – the names that will identify this cache
 * String[] cacheNames – exactly the same as value, it is an alias
 * String key – The default will be all parameters of the cacheable method unless a SpEL expression specified as key
 * keyGenerator – The bean name of the generator if we specify one
 * cacheManager – The bean name of the cacheManager for this cache, can be useful if you need different configs for each cache
 * cacheResolver – The bean name of the cacheResolver for this cache, it is mutually exclusive with the cacheManager attribute
 * condition – a SpEL expression that if it is met, the method will be cached
 * unless – a SpEL expression that if it is not met, the method will be cached
 * sync– It defaults to false, it should be true if multiple threads will attempt to load a value for the same key

Cacheable by following ways:

1. Using @Cacheable with default attributes
2. Using The Key Attribute
3. #root.methodName and #root.targetClass
4. Configuring All Existing Caches
5. Setting a Different Config for Each Cache by Using CacheManager Attribute
6. Using the CacheResolver Attribute
7. Using the Condition Attribute
8. Using the Unless Attribute

## Using @CacheEvict to Invalidate Cache in Spring Boot

@CacheEvict annotation is used to evict all entries or specific entries of a cache. You can use it to invalidate stale or old cache entries.

@CacheEvict has the following attributes, many of which are the same as @Cacheable, so you can jump back to section 5 if you miss anything:

 * String[] value – the names that will identify this cache
 * String[] cacheNames – exactly the same as value, it is an alias
 * String key – The key that will be used to evict specific cache entries. The default will be all parameters of the @CacheEvict method unless a SpEL expression specified as key
 * keyGenerator – The bean name of the generator if we specify one
 * cacheManager – The bean name of the cacheManager for this cache, can be useful if you need different configs for each cache
 * cacheResolver – The bean name of the cacheResolver for this cache, it is mutually exclusive with the cacheManager attribute
 * condition – a SpEL expression that if it is met, the cache will be evicted
 * allEntries– It defaults to false and it specifies if all or specific entries will be evicted
 * beforeInvocation– It defaults to false. If we set it to true, and the annotated method fails for any reason(e.g. exception), the eviction will still happen since it will occur before the block inside the method is executed.

Cache Evict by following ways: 

1. Evicting all entries of brandFilterCache
2. Evicting Specific Entries of brandFilterCache
3. Using @CachePut to Update a Cache in Spring Boot

## Using @CachePut to Update a Cache in Spring Boot
@CachePut annotation is used to update cache entries that might be old or stale. The big difference with @Cacheable, is that when a @CachePut annotated method is called, it will be executed as any other method and insert/update the respective cache entry.

@CachPut has the following attributes, many of which are the same as @Cacheable and @CacheEvict, so you can jump back to section 5 or 6 if you miss anything:

 * String[] value – the names that will identify this cache that we will update
 * String[] cacheNames – exactly the same as value, it is an alias
 * String key – The key that will be used to update cache entries. The default will be all parameters of the @CachePut method unless a SpEL expression specified as key
 * keyGenerator – The bean name of the generator if we specify one
 * cacheManager – The bean name of the cacheManager for this cache, can be useful if you need different configs for each cache
 * cacheResolver – The bean name of the cacheResolver for this cache, it is mutually exclusive with the cacheManager attribute
 * condition – a SpEL expression that if it is met, then the cache will be updated. Note that unlike @Cacheable, we can refer to the result of the method.
 * unless– a SpEL expression that if it is false, then the cache will be updated
 
 ## Cache Without Annotations in Spring Boot
 1. Cacheable Method without @Cacheable
 2. Evicting Specific or All Cache Entries Without @CacheEvict
 3. Updating Cache Entries Without @CachePut
 
  > Sources
  * [Spring Boot Cache – youlearncode](https://youlearncode.com/spring-boot-cache "Spring Boot Cache")
  * [Cache Abstraction – Spring.io](https://docs.spring.io/spring-framework/docs/4.0.x/spring-framework-reference/html/cache.html "Cache Abstraction")

 
 







