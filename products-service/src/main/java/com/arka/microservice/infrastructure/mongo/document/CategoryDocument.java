package com.arka.microservice.infrastructure.mongo.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "category")
public class CategoryDocument {
    @Id
    private String id;
    private String name;
}
