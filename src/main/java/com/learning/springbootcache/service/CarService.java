package com.learning.springbootcache.service;

import com.learning.springbootcache.dto.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
 */
@Slf4j
@Service
public class CarService {

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
     * Cache entry for key 'public java.util.List com.shaique.coding.service.CarService.getCarsWithPriceFilter(java
     * .lang.Double,java.lang.Double)' found in cache 'priceFilterCache'
     * #root.target -> This will set the key as the reference of the object to which this method belongs
     * Cache entry for key 'com.shaique.coding.service.CarService@29ba033a' found in cache 'priceFilterCache'
     * #root.caches -> the key will be the object reference of the data structure that holds the cache, in this case,
     * it will be:
     * Cache entry for key '[org.springframework.cache.concurrent.ConcurrentMapCache@4a782670]' found in cache
     * 'priceFilterCache'
     * <p>
     * 5.3 Creating a Custom KeyGenerator for Cache Key
     * Cache entry for key 'com.shaique.coding.service.CarService@5499dd9egetCarsWithPriceFilter[18000.0, 20000.0]'
     * found in cache 'priceFilterCache'
     */
    @Cacheable("priceFilterCache")
//    @Cacheable(value = "priceFilterCache", key = "#root.method")
//    @Cacheable(value = "priceFilterCache", keyGenerator = "keyGenerator")
    public List<Car> getCarsWithPriceFilter(Double min, Double max) {
        return cars.stream()
                   .filter(car -> car.getPrice() >= min && car.getPrice() <= max)
                   .toList();
    }

    @Cacheable("brandFilterCache")
    public List<Car> getCarsWithBrandFilter(String brand) {
        return cars.stream()
                   .filter(car -> car.getBrand().equals(brand))
                   .toList();
    }
}
