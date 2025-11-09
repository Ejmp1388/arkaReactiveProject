package com.arka.microservice.domain.model;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    Product {
    private String id;
    private String name;
    private String description;
    private String brand;       // FK -> brands
    private List<String> categories;    // FK -> categories
    private List<String> images;
    private Map<String, Object> attributes; // E.g.: color, dpi, connectivity
    private String currency;
    private Double price;
    private boolean active;
}