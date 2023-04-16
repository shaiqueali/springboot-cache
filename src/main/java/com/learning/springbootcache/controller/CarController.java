package com.learning.springbootcache.controller;

import com.learning.springbootcache.dto.Car;
import com.learning.springbootcache.service.CarService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * https://youlearncode.com/spring-boot-cache/
 */

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cars", produces = MediaType.APPLICATION_JSON_VALUE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CarController {

    CarService carService;

    @GetMapping(params = { "minPrice", "maxPrice" })
    public List<Car> getAllFilteredByPrice(@RequestParam final Double minPrice, @RequestParam final Double maxPrice) {
        return carService.getCarsWithPriceFilter(minPrice, maxPrice);
    }

    @GetMapping(params = { "brand" })
    public List<Car> getAllFilteredByBrand(@RequestParam final String brand) {
        return carService.getCarsWithBrandFilter(brand);
    }
}
