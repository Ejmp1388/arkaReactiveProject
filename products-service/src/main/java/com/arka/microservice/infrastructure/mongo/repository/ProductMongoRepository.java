package com.arka.microservice.infrastructure.mongo.repository;

import com.arka.microservice.infrastructure.mongo.document.ProductDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductMongoRepository extends ReactiveMongoRepository<ProductDocument, String> {
}
