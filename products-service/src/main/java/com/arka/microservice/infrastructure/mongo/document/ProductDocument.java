package com.arka.microservice.infrastructure.mongo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Document(collection = "product")
public class ProductDocument {
    @Id
    private String id= UUID.randomUUID().toString(); // Auto-generated UUID
    private String name;
    private String description;
    private String brand;       // FK -> brands
    private List<String> categories;    // FK -> categories
    private List<String> images;
    private Map<String, Object> attributes; // E.g.: color, dpi, connectivity
    private Map<String, Double> price;      // E.g.: {"COP":80000, "USD":20}
    private boolean active;
}
