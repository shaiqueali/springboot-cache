package com.learning.springbootcache.service;

import com.learning.springbootcache.dto.Car;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Using @Cacheable to Cache a Method’s Result in Spring Boot
 * The @Cacheable annotation is used to mark a method as cacheable. A very important thing about cache in Spring Boot
 * is that you should never call a @Cacheable , @CacheEvict or @CachePut annotated method from the same class as it
 * will never work.
 *
 * @Cacheable has the following attributes:
 * <p>
 * String[] value – the names that will identify this cache
 * String[] cacheNames – exactly the same as value, it is an alias
 * String key – The default will be all parameters of the cacheable method unless a SpEL expression specified as key
 * keyGenerator – The bean name of the generator if we specify one
 * cacheManager – The bean name of the cacheManager for this cache, can be useful if you need different configs for
 * each cache
 * cacheResolver – The bean name of the cacheResolver for this cache, it is mutually exclusive with the cacheManager
 * attribute
 * condition – a SpEL expression that if it is met, the method will be cached
 * unless – a SpEL expression that if it is not met, the method will be cached
 * sync– It defaults to false, it should be true if multiple threads will attempt to load a value for the same key
 *
 * 6. Using @CacheEvict to Invalidate Cache in Spring Boot
 * @CacheEvict annotation is used to evict all entries or specific entries of a cache. You can use it to invalidate
 * stale or old cache entries.
 *
 * @CacheEvict has the following attributes, many of which are the same as @Cacheable, so you can jump back to
 * section 5 if you miss anything:
 *
 * String[] value – the names that will identify this cache
 * String[] cacheNames – exactly the same as value, it is an alias
 * String key – The key that will be used to evict specific cache entries. The default will be all parameters of the
 * @CacheEvict method unless a SpEL expression specified as key
 * keyGenerator – The bean name of the generator if we specify one
 * cacheManager – The bean name of the cacheManager for this cache, can be useful if you need different configs for
 * each cache
 * cacheResolver – The bean name of the cacheResolver for this cache, it is mutually exclusive with the cacheManager
 * attribute
 * condition – a SpEL expression that if it is met, the cache will be evicted
 * allEntries– It defaults to false and it specifies if all or specific entries will be evicted
 * beforeInvocation– It defaults to false. If we set it to true, and the annotated method fails for any reason(e.g.
 * exception), the eviction will still happen since it will occur before the block inside the method is executed.
 *
 * 7. Using @CachePut to Update a Cache in Spring Boot
 * @CachePut annotation is used to update cache entries that might be old or stale. The big difference with
 * @Cacheable, is that when a @CachePut annotated method is called, it will be executed as any other method and
 * insert/update the respective cache entry.
 *
 * @CachPut has the following attributes, many of which are the same as @Cacheable and @CacheEvict, so you can jump
 * back to section 5 or 6 if you miss anything:
 *
 * String[] value – the names that will identify this cache that we will update
 * String[] cacheNames – exactly the same as value, it is an alias
 * String key – The key that will be used to update cache entries. The default will be all parameters of the
 * @CachePut method unless a SpEL expression specified as key
 * keyGenerator – The bean name of the generator if we specify one
 * cacheManager – The bean name of the cacheManager for this cache, can be useful if you need different configs for
 * each cache
 * cacheResolver – The bean name of the cacheResolver for this cache, it is mutually exclusive with the cacheManager
 * attribute
 * condition – a SpEL expression that if it is met, then the cache will be updated. Note that unlike @Cacheable, we
 * can refer to the result of the method.
 * unless– a SpEL expression that if it is false, then the cache will be updated
 */
@Slf4j
@Service
@AllArgsConstructor
public class CarService {

    /**
     * Cache Without Annotations in Spring Boot
     */

    @Qualifier("priceFilterCacheManager")
    private final CacheManager brandFilterCacheManager;

    @Qualifier("priceFilterCacheManager")
    private final CacheManager priceFilterCacheManager;



    private final List<Car> cars = new ArrayList<>(
            List.of(
                    new Car(1L, "Astra", "Opel", 100, 18000d),
                    new Car(2L, "Insignia", "Opel", 120, 22000d),
                    new Car(3L, "Golf", "VW", 90, 17000d),
                    new Car(4L, "Golf", "VW", 120, 19000d),
                    new Car(5L, "Gallardo", "Lamborghini", 400, 100_000d)
            ));

    /**
     * 5.2 Using The Key Attribute
     * You can specify a custom key by using a SpEL expression, in order to override the default one(the parameters
     * of the method)
     * <p>
     * 5.2.1 #root.method, #root.target and #root.caches
     * #root.method -> If you check the logs again, after you hit the request, the key will be shown as
     * logs-> Cache entry for key 'public java.util.List com.shaique.coding.service.CarService.getCarsWithPriceFilter(java
     * .lang.Double,java.lang.Double)' found in cache 'priceFilterCache'
     * #root.target -> This will set the key as the reference of the object to which this method belongs
     * logs-> Cache entry for key 'com.shaique.coding.service.CarService@29ba033a' found in cache 'priceFilterCache'
     * #root.caches -> the key will be the object reference of the data structure that holds the cache, in this case,
     * it will be:
     * logs-> Cache entry for key '[org.springframework.cache.concurrent.ConcurrentMapCache@4a782670]' found in cache
     * 'priceFilterCache'
     * <p>
     * 5.3 Creating a Custom KeyGenerator for Cache Key
     * logs-> Cache entry for key 'com.shaique.coding.service.CarService@5499dd9egetCarsWithPriceFilter[18000.0, 20000.0]'
     * found in cache 'priceFilterCache'
     * 5.4 Setting a Different Config for Each Cache by Using CacheManager Attribute
     * Now let’s hit GET /cars?minPrice=18000&maxPrice=20000 three times, remember this cache has an expiry time of 2 secs:
     * logs-> 1st hit -> No cache entry for key 'SimpleKey [18000.0,21000.0]' in cache(s) [priceFilterCache]
     * 2nd hit -> Cache entry for key 'SimpleKey [18000.0,21000.0]' found in cache 'priceFilterCache'
     * 3rd hit ->No cache entry for key 'SimpleKey [18000.0,21000.0]' in cache(s) [priceFilterCache]
     * 5.5 Using the CacheResolver Attribute
     * 5.6 Using the Condition Attribute
     * logs -> Cache condition failed on method public java.util.List com.learning.springbootcache.service.CarService
     * .getCarsWithPriceFilter(java.lang.Double,java.lang.Double) for operation Builder[public java.util.List com
     * .learning.springbootcache.service.CarService.getCarsWithPriceFilter(java.lang.Double,java.lang.Double)]
     * caches=[priceFilterCache] | key='' | keyGenerator='' | cacheManager='' |
     * cacheResolver='priceFilterCacheResolver' | condition='#a0 > 18000' | unless='' | sync='false'
     * 5.7 Using the Unless Attribute
     */
//    @Cacheable("priceFilterCache") // Creating a Custom KeyGenerator for Cache Key
//    @Cacheable(value = "priceFilterCache", key = "#root.method") // #root.method, #root.target and #root.caches
//    @Cacheable(value = "priceFilterCache", keyGenerator = "keyGenerator") // #root.method, #root.target and #root.caches
    @Cacheable(value = "priceFilterCache", cacheManager = "priceFilterCacheManager") // Setting a Different Config for Each Cache by Using CacheManager Attribute
//    @Cacheable(value = "priceFilterCache", cacheResolver = "priceFilterCacheResolver") // Using the CacheResolver Attribute
//    @Cacheable(value = "priceFilterCache", cacheResolver = "priceFilterCacheResolver", condition = "#a0 > 18000") // Using the Condition Attribute
//    @Cacheable(value = "priceFilterCache", cacheResolver = "priceFilterCacheResolver", condition = "#result.size() == 2") // Using the Unless Attribute
    public List<Car> getCarsWithPriceFilter(Double min, Double max) {
        return cars.stream()
                   .filter(car -> car.getPrice() >= min && car.getPrice() <= max)
                   .toList();
    }

    //    @Cacheable("brandFilterCache") // Creating a Custom KeyGenerator for Cache Key
    @Cacheable(value = "brandFilterCache", cacheManager = "brandFilterCacheManager")
    public List<Car> getCarsWithBrandFilter(String brand) {
        return cars.stream()
                   .filter(car -> car.getBrand().equals(brand))
                   .toList();
    }

    /**
     * Evicting all entries of brandFilterCache
     */
    @CacheEvict(value = "brandFilterCache", cacheManager = "brandFilterCacheManager", allEntries = true)
    public void evictAllBrandCacheEntries() {
        log.info("evictAllBrandCacheEntries called.");
    }

    /**
     *  Evicting Specific Entries of brandFilterCache
     * logs -> Invalidating cache key [Opel] for operation Builder[public void com.learning.springbootcache.service
     * .CarService.evictSpecificCaches(java.lang.String)] caches=[brandFilterCache] | key='#brand' | keyGenerator=''
     * | cacheManager='brandFilterCacheManager' | cacheResolver='' | condition='',false,false on method public void
     * com.learning.springbootcache.service.CarService.evictSpecificCaches(java.lang.String)
     */
    @CacheEvict(value = "brandFilterCache", cacheManager = "brandFilterCacheManager", key = "#brand")
    public void evictSpecificCaches(final String brand) {
        log.info("evictSpecificCaches called.");
    }

    /**
     * Using @CachePut to Update a Cache in Spring Boot
     */
    @CachePut(value = "brandFilterCache", cacheManager = "brandFilterCacheManager", key = "#brand")
    public List<Car> putBrandFilterCache(String brand) {
        return cars.stream()
                   .filter(car -> car.getBrand().equals(brand))
                   .toList();
    }

    public Car create(Car car) {
        final Long newId = cars.stream().mapToLong(Car::getId).max().orElse(0L) + 1L;
        car.setId(newId);
        cars.add(car);
        return cars.stream()
                   .filter(car_ -> car_.getId().equals(newId))
                   .findAny()
                   .orElseThrow();
    }


    /**
     * Cacheable Method without @Cacheable
     *
     * Here we just look into the brandFilterCache and then:
     *
     * If it does not exist, we calculate the result, put it in a cache entry, and return the result
     * If it does exist, we just return the value from the cache
     * Note that here it is important to use orElseGet() since orElse() will calculate the result regardless of the
     * result of the previous .map.
     */
    public List<Car> getCarsWithBrandFilterWithoutCacheable(String brand) {
        Cache cache =  brandFilterCacheManager.getCache("brandFilterCache");
        var result =  Optional
                .ofNullable(cache)
                .map(cache_ -> cache_.get(new SimpleKey(brand)))
                .orElseGet(() -> insertInCacheAndReturn(brand, cache))
                .get();
        return (List<Car>) result;
    }

    private Cache.ValueWrapper insertInCacheAndReturn(String brand, Cache cache) {
        return () -> {
            List<Car> resultToBePut = cars.stream()
                                          .filter(car -> car.getBrand().equals(brand))
                                          .toList();
            cache.put(new SimpleKey(brand), resultToBePut);
            return resultToBePut;
        };
    }

    /**
     * Evicting Specific or All Cache Entries Without @CacheEvict
     */
    public void evictAllBrandCacheEntriesWithoutCacheable() {
        Objects.requireNonNull(brandFilterCacheManager.getCache("brandFilterCache")).clear();
    }

    /**
     * Updating Cache Entries Without @CachePut
     */
    public void putBrandFilterCacheWithoutCacheable(final String brand) {
        Cache cache =  brandFilterCacheManager.getCache("brandFilterCache");
        List<Car> resultToBePut = cars.stream()
                                      .filter(car -> car.getBrand().equals(brand))
                                      .toList();
        cache.put(new SimpleKey(brand), resultToBePut);
    }
}
