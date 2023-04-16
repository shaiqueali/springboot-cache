package com.learning.springbootcache.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Car {
    private Long id;
    private String model;
    private String brand;
    private Integer horses;
    private Double price;
}
