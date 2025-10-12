package com.arka.microservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class    Product {
    private String id;
    private String name;
    private String description;
    private String brand;       // FK -> brands
    private List<String> categories;    // FK -> categories
    private List<String> images;
    private Map<String, Object> attributes; // E.g.: color, dpi, connectivity
    private Map<String, Double> price;      // E.g.: {"COP":80000, "USD":20}
    private boolean active;
}