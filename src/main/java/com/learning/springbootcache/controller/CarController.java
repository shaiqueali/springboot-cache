package com.learning.springbootcache.controller;

import com.learning.springbootcache.dto.Car;
import com.learning.springbootcache.service.CarService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * https://youlearncode.com/spring-boot-cache/
 * https://docs.spring.io/spring-framework/docs/4.0.x/spring-framework-reference/html/cache.html
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CarController {

    CarService carService;

    @GetMapping(params = { "minPrice", "maxPrice" })
    public ResponseEntity<List<Car>> getAllFilteredByPrice(@RequestParam final Double minPrice,
            @RequestParam final Double maxPrice) {
        return ResponseEntity.ok(carService.getCarsWithPriceFilter(minPrice, maxPrice));
    }

    @GetMapping(params = { "brand" })
    public ResponseEntity<List<Car>> getAllFilteredByBrand(@RequestParam final String brand) {
        return ResponseEntity.ok(carService.getCarsWithBrandFilter(brand));
    }

    /**
     * Evicting all entries of brandFilterCache
     */
    @GetMapping("/evictAllBrandFilterCache")
    public ResponseEntity<Void> evictAllBrandFilterCache() {
        carService.evictAllBrandCacheEntries();
        return ResponseEntity.noContent().build();
    }

    /**
     * Evicting Specific Entries of brandFilterCache
     */
    @GetMapping(value = "/evictBrandFilterCache", params = "brands")
    public ResponseEntity<Void> evictBrandFilterCache(@RequestParam List<String> brands) {
        brands.forEach(carService::evictSpecificCaches);
        return ResponseEntity.noContent().build();
    }

    /**
     * Using @CachePut to Update a Cache in Spring Boot
     */

    @PutMapping(value = "/updateBrandFilterCache")
    public ResponseEntity<Void> updateBrandFilterCache(@RequestBody final List<String> brands) {
        brands.forEach(carService::putBrandFilterCache);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Car> create(@RequestBody final Car car) {
        return ResponseEntity.ok(carService.create(car));
    }
}
