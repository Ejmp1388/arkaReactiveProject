package com.arka.microservice.infrastructure.mongo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "brands")
public class BrandsDocument {
    @Id
    private String id;
    private String name;
}
